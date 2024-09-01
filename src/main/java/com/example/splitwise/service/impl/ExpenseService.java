package com.example.splitwise.service.impl;

import com.example.splitwise.entities.request.CreateExpenseRequest;
import com.example.splitwise.entities.response.CreateExpenseResponse;
import com.example.splitwise.exception.GroupDoesNotExistException;
import com.example.splitwise.repository.dao.*;
import com.example.splitwise.repository.table.*;
import com.example.splitwise.service.IExpenseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ExpenseService implements IExpenseService {

    @Autowired
    private IExpenseDAO iExpenseDAO;

    @Autowired
    private IExpenseSplitDAO iExpenseSplitDAO;

    @Autowired
    private IGroupDAO iGroupDAO;

    @Autowired
    private IUserBalanceDAO iUserBalanceDAO;

    @Autowired
    private IUserGroupBalanceDAO iUserGroupBalanceDAO;

    public CreateExpenseResponse createExpense(CreateExpenseRequest request) {

        // Step 0: Validate the existence of the group if groupId is provided
        if (request.getGroupId() != null) {
            boolean groupExists = iGroupDAO.existsById(request.getGroupId());
            if (!groupExists) {
                throw new GroupDoesNotExistException("Group with ID " + request.getGroupId() + " does not exist.");
            }
        }

        // Step 1: Validate the existence of the group if groupId is provided
        if (request.getGroupId() != null) {
            boolean groupExists = iGroupDAO.existsById(request.getGroupId());
            if (!groupExists) {
                return new CreateExpenseResponse("Group with ID " + request.getGroupId() + " does not exist.", 0.0, Collections.emptyMap());
            }
        }

        // Step 2: Validate Expense Creation Request
        validateExpenseCreationRequest(request);

        // Step 3: Create and save the expense
        Expense expense = new Expense();
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setPaidBy(request.getPaidBy());
        expense.setCreatedBy(request.getCreatedBy());
        expense.setGroupId(request.getGroupId());
        expense = iExpenseDAO.save(expense);

        // Step 4: Create and save the expense splits
        Map<Integer, Double> friendBalances = new HashMap<>();
        for (CreateExpenseRequest.Participant participant : request.getParticipants()) {
            ExpenseSplit split = new ExpenseSplit();
            split.setExpenseId(expense.getId());
            split.setUserId(participant.getUserId());
            split.setAmount(participant.getAmount());
            iExpenseSplitDAO.save(split);

            // Step 5: Update the balances between the payer and each participant in user balances table
            updateUserBalance(request.getPaidBy(), participant.getUserId(), participant.getAmount(), friendBalances);
        }

        // Step 6: Update the user group balances if this is a group expense
        if (request.getGroupId() != null) {
            updateUserGroupBalancesForExpense(request, request.getGroupId());
        }

        // Step 7: Calculate the overall balance
        double overallBalance = calculateOverallBalance(request.getPaidBy());

        // Step 8: Return the response
        CreateExpenseResponse response = new CreateExpenseResponse();
        response.setMessage("Expense created successfully");
        response.setOverallBalance(overallBalance);
        response.setFriendBalances(friendBalances);
        return response;
    }

    private void updateUserGroupBalancesForExpense(CreateExpenseRequest request, Integer groupId) {

        // Net contribution for the user who paid
        double userShareForPayer = request.getParticipants().stream()
                .filter(p -> p.getUserId() == request.getPaidBy())
                .findFirst().get().getAmount();
        double netContribution = request.getAmount() - userShareForPayer;
        updateUserGroupBalance(groupId, request.getPaidBy(), netContribution);

        // Update balances for each participant
        for (CreateExpenseRequest.Participant participant : request.getParticipants()) {
            if (participant.getUserId() != request.getPaidBy()) {
                updateUserGroupBalance(groupId, participant.getUserId(), -participant.getAmount());
            }
        }
    }

    private void updateUserGroupBalance(Integer groupId, int userId, double amount) {

        // Fetch the existing balance or create a new entry if it doesn't exist
        UserGroupBalance balance = iUserGroupBalanceDAO.findByUserIdAndGroupId(userId, groupId)
                .orElseGet(() -> {
                    // Create a new UserGroupBalance if not found
                    UserGroupBalance newBalance = new UserGroupBalance();
                    newBalance.setUserId(userId);
                    newBalance.setGroupId(groupId);
                    newBalance.setBalance(0.0);
                    return newBalance;
                });

        // Update the balance
        balance.setBalance(balance.getBalance() + amount);

        // Save the updated balance back to the database
        iUserGroupBalanceDAO.save(balance);
    }

    private void updateUserBalance(int paidBy, int participant, double amount, Map<Integer, Double> friendBalances) {
        if (paidBy != participant) {
            updateBalance(paidBy, participant, amount, friendBalances);
            updateBalance(participant, paidBy, -amount, friendBalances);
        }
    }

    private void updateBalance(int userId, int friendId, double amount, Map<Integer, Double> friendBalances) {
        UserBalance balance = iUserBalanceDAO.findByUserIdAndFriendId(userId, friendId)
                .orElseGet(() -> new UserBalance(userId, friendId, 0.0));
        balance.setBalance(balance.getBalance() + amount);
        iUserBalanceDAO.save(balance);
        friendBalances.put(friendId, balance.getBalance());
    }

    private double calculateOverallBalance(int userId) {
        return iUserBalanceDAO.calculateOverallBalance(userId).orElse(0.0);
    }

    private void validateExpenseCreationRequest(CreateExpenseRequest request) {
        // Validate group existence if groupId is provided
        if (request.getGroupId() != null && !iGroupDAO.existsById(request.getGroupId())) {
            throw new GroupDoesNotExistException("Group with ID " + request.getGroupId() + " does not exist.");
        }

        // Validate that description is not null or empty
        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }

        // Validate that amount is greater than 0
        if (request.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        // Validate that paidBy is set
        if (request.getPaidBy() <= 0) {
            throw new IllegalArgumentException("PaidBy must be a valid user ID");
        }

        // Validate that createdBy is set
        if (request.getCreatedBy() <= 0) {
            throw new IllegalArgumentException("CreatedBy must be a valid user ID");
        }

        // Validate that participants are not null or empty
        if (request.getParticipants() == null || request.getParticipants().isEmpty()) {
            throw new IllegalArgumentException("Participants cannot be null or empty");
        }

        // Validate that each participant has a valid userId and amount
        for (CreateExpenseRequest.Participant participant : request.getParticipants()) {
            if (participant.getUserId() <= 0) {
                throw new IllegalArgumentException("Participant must have a valid user ID");
            }
            if (participant.getAmount() < 0) {
                throw new IllegalArgumentException("Participant amount cannot be negative");
            }
        }
    }

}
