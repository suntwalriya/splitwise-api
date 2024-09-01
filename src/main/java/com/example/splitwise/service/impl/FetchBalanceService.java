package com.example.splitwise.service.impl;

import com.example.splitwise.entities.response.FetchBalanceResponse;
import com.example.splitwise.exception.UserNotFoundException;
import com.example.splitwise.repository.dao.*;
import com.example.splitwise.repository.table.User;
import com.example.splitwise.repository.table.UserBalance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FetchBalanceService {
    @Autowired
    private IUserDAO iUserDAO;

    @Autowired
    private IUserBalanceDAO iUserBalanceDAO;

    @Autowired
    private IUserGroupBalanceDAO iUserGroupBalanceDAO;


    // To fetch balances for a user
    public FetchBalanceResponse getBalanceDetails(Integer userId) {

        // Step 1: Fetch user information
        User user = iUserDAO.findById(userId)
                .orElseThrow(() -> {
                    log.debug("User not found for userId: {}", userId);
                    return new UserNotFoundException("Invalid credentials");
                });

        // Step 2: Calculate overall balance
        double overallBalance = calculateOverallBalance(userId);

        // Step 3: Calculate friend balances
        Map<Integer, Double> friendBalances = calculateFriendBalances(userId);

        // Step 4: Fetch group balances
        Map<Integer, Double> groupBalances = calculateGroupBalances(userId);

        // Step 5: Prepare the response
        FetchBalanceResponse response = new FetchBalanceResponse();
        response.setUser(user);
        response.setOverallBalance(overallBalance);
        response.setFriendBalances(friendBalances);
        response.setGroupBalances(groupBalances);

        return response;
    }

    // To calculate overall balance
    private double calculateOverallBalance(int userId) {
        List<UserBalance> balances = iUserBalanceDAO.findByUserId(userId);

        return balances.stream()
                .mapToDouble(UserBalance::getBalance)
                .sum();
    }

    // To calculate friend expenses
    private Map<Integer, Double> calculateFriendBalances(int userId) {
        List<UserBalance> balances = iUserBalanceDAO.findByUserId(userId);
        Map<Integer, Double> friendBalances = new HashMap<>();

        for (UserBalance balance : balances) {
            friendBalances.put(balance.getFriendId(), balance.getBalance());
        }

        return friendBalances;
    }

    // To calculate group expenses
    private Map<Integer, Double> calculateGroupBalances(int userId) {
        List<Object[]> results = iUserGroupBalanceDAO.findGroupBalancesByUserId(userId);

        Map<Integer, Double> groupBalances = new HashMap<>();
        for (Object[] result : results) {
            Integer groupId = (Integer) result[0];
            Double balance = (Double) result[1];
            groupBalances.put(groupId, balance);
        }

        return groupBalances;
    }
}
