package com.example.splitwise.service.impl;

import com.example.splitwise.entities.dto.TransactionDTO;
import com.example.splitwise.entities.enums.SplitType;
import com.example.splitwise.entities.request.AddExpenseRequest;
import com.example.splitwise.entities.request.SettleExpenseRequest;
import com.example.splitwise.entities.response.AddExpenseResponse;
import com.example.splitwise.entities.response.SettleExpenseResponse;
import com.example.splitwise.exception.DuplicateExpenseException;
import com.example.splitwise.exception.GroupNotFoundException;
import com.example.splitwise.exception.InvalidSplitException;
import com.example.splitwise.exception.UserNotFoundException;
import com.example.splitwise.repository.dao.IExpenseDAO;
import com.example.splitwise.repository.dao.IExpenseSplitDAO;
import com.example.splitwise.repository.dao.IGroupDAO;
import com.example.splitwise.repository.dao.IUserDAO;
import com.example.splitwise.repository.table.Expense;
import com.example.splitwise.repository.table.ExpenseSplit;
import com.example.splitwise.repository.table.User;
import com.example.splitwise.repository.table.UserGroups;
import com.example.splitwise.service.IExpenseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class ExpenseService implements IExpenseService {

    @Autowired
    private IExpenseDAO iExpenseDAO;

    @Autowired
    private IExpenseSplitDAO iExpenseSplitDAO;

    @Autowired
    private IUserDAO iUserDAO;

    @Autowired
    private IGroupDAO iGroupDAO;

    @Transactional
    public AddExpenseResponse addExpense(AddExpenseRequest request) {

        // Validate the request
        validateExpenseCreationRequest(request);

        // Create the expense
        Expense expense = createExpense(request);

        // Handle splits
        handleSplits(request.getSplits(), expense, request.getSplitType(), request.getAmount());

        // Mapping major details to the response DTO
        AddExpenseResponse response = new AddExpenseResponse();
        response.setExpenseId(expense.getId());
        response.setDescription(expense.getDescription());
        response.setAmount(expense.getAmount());
        response.setCurrency(expense.getCurrency().toString());
        response.setSplitType(expense.getSplitType().toString());
        response.setPaidBy(request.getValidatedPayer().getName());
        response.setCreatedBy(request.getValidatedCreator().getName());
        response.setCreatedAt(expense.getCreatedAt());

        return response;
    }

    private Expense createExpense(AddExpenseRequest request) {
        Expense expense = new Expense();
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setCurrency(request.getCurrency());
        expense.setPaidBy(request.getPaidBy());
        expense.setCreatedBy(request.getCreatedById());
        expense.setSplitType(request.getSplitType());
        expense.setGroupId(request.getGroupId());

        return iExpenseDAO.save(expense);
    }

    @Transactional
    public SettleExpenseResponse settleExpenses(SettleExpenseRequest request) {
        List<Expense> expenses;

        if (request.getGroupId() != null) {
            // Fetch expenses for the group
            expenses = iExpenseDAO.findByGroupId(request.getGroupId());
        } else if (request.getUserId() != null && request.getFriendId() != null) {
            // Fetch expenses between two users
            expenses = iExpenseDAO.findByUserAndFriend(request.getUserId(), request.getFriendId());
        } else {
            throw new InvalidSplitException("Either groupId or both userId and friendId must be provided.");
        }

        List<TransactionDTO> transactions = new ArrayList<>();
        Map<Integer, Double> individualAmounts = new HashMap<>();

        for (Expense expense : expenses) {
            for (ExpenseSplit split : expense.getExpenseSplits()) {
                int userId = split.getUserId();
                double amount = split.getSplitValue() - expense.getAmount() / expense.getExpenseSplits().size();

                individualAmounts.put(userId, individualAmounts.getOrDefault(userId, 0.0) + amount);
            }
        }

        PriorityQueue<Map.Entry<Integer, Double>> maxHeap = new PriorityQueue<>(
                (a, b) -> Double.compare(b.getValue(), a.getValue())
        );

        PriorityQueue<Map.Entry<Integer, Double>> minHeap = new PriorityQueue<>(
                Comparator.comparingDouble(Map.Entry::getValue)
        );

        for (Map.Entry<Integer, Double> entry : individualAmounts.entrySet()) {
            if (entry.getValue() < 0) {
                minHeap.add(entry);
            } else if (entry.getValue() > 0) {
                maxHeap.add(entry);
            }
        }

        while (!minHeap.isEmpty()) {
            Map.Entry<Integer, Double> sender = minHeap.poll();
            Map.Entry<Integer, Double> receiver = maxHeap.poll();
            TransactionDTO transactionDTO = new TransactionDTO();

            transactionDTO.setFromUserName(iUserDAO.findById(sender.getKey()).get().getName());
            transactionDTO.setToUserName(iUserDAO.findById(receiver.getKey()).get().getName());

            double settledAmount = Math.min(Math.abs(sender.getValue()), receiver.getValue());

            sender.setValue(sender.getValue() + settledAmount);
            receiver.setValue(receiver.getValue() - settledAmount);

            if (sender.getValue() < 0) {
                minHeap.add(sender);
            }

            if (receiver.getValue() > 0) {
                maxHeap.add(receiver);
            }

            transactionDTO.setAmount(settledAmount);
            transactions.add(transactionDTO);
        }

        // Mark expenses as settled if necessary
        if (request.getGroupId() != null) {
            for (Expense expense : expenses) {
                // Mark the expense as settled (add a 'settled' field in Expense if necessary)
                // expense.setSettled(true);
                iExpenseDAO.save(expense);
            }
        }

        return new SettleExpenseResponse(transactions, true, "Expenses settled successfully.");
    }

    private void validateExpenseCreationRequest(AddExpenseRequest request) {

        // Validate the payer
        User paidBy = iUserDAO.findById(request.getPaidBy())
                .orElseThrow(() -> new UserNotFoundException("Payer not found"));

        // Validate the creator
        User createdBy = iUserDAO.findById(request.getCreatedById())
                .orElseThrow(() -> new UserNotFoundException("Creator not found"));

        // Validate the group or friend
        UserGroups group = null;
        if (request.getGroupId() != null) {
            group = iGroupDAO.findById(request.getGroupId())
                    .orElseThrow(() -> new GroupNotFoundException("Group not found"));
        } else if (request.getFriendId() != null) {
            iUserDAO.findById(request.getFriendId())
                    .orElseThrow(() -> new UserNotFoundException("Friend not found"));
        } else {
            throw new InvalidSplitException("Either groupId or friendId must be provided");
        }

        log.debug("Checking for existing expense with description: {}, amount: {}, currency: {}, splitType: {}, groupId: {}, paidBy: {}, createdBy: {}",
                request.getDescription(),
                request.getAmount(),
                request.getCurrency(),
                request.getSplitType(),
                request.getGroupId(),
                request.getPaidBy(),
                request.getCreatedById());

        List<Expense> existingExpenses = null;
        // Check for duplicate expenses
        try {
            existingExpenses = iExpenseDAO.findByDescriptionAndAmountAndCurrencyAndSplitTypeAndGroupIdAndPaidByAndCreatedBy(
                    request.getDescription(),
                    request.getAmount(),
                    request.getCurrency(),
                    request.getSplitType(),
                    request.getGroupId(),
                    request.getPaidBy(),
                    request.getCreatedById()
            );
        } catch (Exception e) {
            log.error("Error while checking for existing expense", e);
            throw e;  // or handle the error as needed
        }

        if (!existingExpenses.isEmpty()) {
            throw new DuplicateExpenseException("An expense with the same details already exists.");
        }

        // Pass validated entities as needed
        request.setValidatedPayer(paidBy);
        request.setValidatedCreator(createdBy);
        request.setValidatedGroup(group);
    }

    private void handleSplits(List<AddExpenseRequest.SplitDetail> splits, Expense expense, SplitType splitType, double totalAmount) {
        switch (splitType) {
            case EXACT:
                handleExactSplits(splits, expense);
                break;
            case EQUAL:
                handleEqualSplits(splits, expense, totalAmount);
                break;
            case PERCENT:
                handlePercentSplits(splits, expense, totalAmount);
                break;
            case RATIO:
                handleRatioSplits(splits, expense, totalAmount);
                break;
            default:
                throw new InvalidSplitException("Invalid split type");
        }
    }

    private void handleExactSplits(List<AddExpenseRequest.SplitDetail> splits, Expense expense) {
        for (AddExpenseRequest.SplitDetail splitDetail : splits) {
            User user = iUserDAO.findById(splitDetail.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            ExpenseSplit expenseSplit = new ExpenseSplit();
            expenseSplit.setExpenseId(expense.getId());
            expenseSplit.setUserId(user.getId());
            expenseSplit.setSplitValue(splitDetail.getValue());  // Exact amount

            iExpenseSplitDAO.save(expenseSplit);
        }
    }

    private void handleEqualSplits(List<AddExpenseRequest.SplitDetail> splits, Expense expense, double totalAmount) {
        double equalShare = totalAmount / splits.size();
        for (AddExpenseRequest.SplitDetail splitDetail : splits) {
            User user = iUserDAO.findById(splitDetail.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            ExpenseSplit expenseSplit = new ExpenseSplit();
            expenseSplit.setExpenseId(expense.getId());
            expenseSplit.setUserId(user.getId());
            expenseSplit.setSplitValue(equalShare);  // Equal share

            iExpenseSplitDAO.save(expenseSplit);
        }
    }

    private void handlePercentSplits(List<AddExpenseRequest.SplitDetail> splits, Expense expense, double totalAmount) {
        for (AddExpenseRequest.SplitDetail splitDetail : splits) {
            User user = iUserDAO.findById(splitDetail.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            double percentageShare = (splitDetail.getValue() / 100) * totalAmount;

            ExpenseSplit expenseSplit = new ExpenseSplit();
            expenseSplit.setExpenseId(expense.getId());
            expenseSplit.setUserId(user.getId());
            expenseSplit.setSplitValue(percentageShare);  // Calculated from percentage

            iExpenseSplitDAO.save(expenseSplit);
        }
    }

    private void handleRatioSplits(List<AddExpenseRequest.SplitDetail> splits, Expense expense, double totalAmount) {
        double totalRatio = splits.stream().mapToDouble(AddExpenseRequest.SplitDetail::getValue).sum();
        for (AddExpenseRequest.SplitDetail splitDetail : splits) {
            User user = iUserDAO.findById(splitDetail.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            double ratioShare = (splitDetail.getValue() / totalRatio) * totalAmount;

            ExpenseSplit expenseSplit = new ExpenseSplit();
            expenseSplit.setExpenseId(expense.getId());
            expenseSplit.setUserId(user.getId());
            expenseSplit.setSplitValue(ratioShare);  // Calculated from ratio

            iExpenseSplitDAO.save(expenseSplit);
        }
    }
}
