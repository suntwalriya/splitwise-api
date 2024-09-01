package com.example.splitwise.service.impl;

import com.example.splitwise.entities.response.FetchTaskResponse;
import com.example.splitwise.exception.UserNotFoundException;
import com.example.splitwise.repository.dao.*;
import com.example.splitwise.repository.table.User;
import com.example.splitwise.repository.table.UserBalance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FetchTaskService {
    @Autowired
    private IUserDAO iUserDAO;

    @Autowired
    private IUserBalanceDAO iUserBalanceDAO;

    @Autowired
    private IUserGroupBalanceDAO iUserGroupBalanceDAO;


    // To fetch tasks for a user
    public FetchTaskResponse getFetchTaskDetails(Integer userId) {

        // Step 1: Fetch user information
        User user = iUserDAO.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Step 2: Calculate overall balance
        double overallBalance = calculateOverallBalance(userId);

        // Step 3: Calculate friend balances
        Map<Integer, Double> friendBalances = calculateFriendBalances(userId);

        // Step 4: Fetch group balances
        Map<Integer, Double> groupBalances = calculateGroupBalances(userId);

        // Step 5: Prepare the response
        FetchTaskResponse response = new FetchTaskResponse();
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

    // To calculate group expenses
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
