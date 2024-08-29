package com.example.splitwise.service.impl;

import com.example.splitwise.entities.response.FetchTaskResponse;
import com.example.splitwise.exception.UserNotFoundException;
import com.example.splitwise.repository.dao.IExpenseDAO;
import com.example.splitwise.repository.dao.IExpenseSplitDAO;
import com.example.splitwise.repository.dao.IUserDAO;
import com.example.splitwise.repository.dao.IUserGroupDAO;
import com.example.splitwise.repository.table.Expense;
import com.example.splitwise.repository.table.ExpenseSplit;
import com.example.splitwise.repository.table.User;
import com.example.splitwise.repository.table.UserGroups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FetchTaskService {
    @Autowired
    private IUserDAO iUserDAO;

    @Autowired
    private IUserGroupDAO iUserGroupDAO;

    @Autowired
    private IExpenseDAO iExpenseDAO;

    @Autowired
    private IExpenseSplitDAO iExpenseSplitDAO;

    public FetchTaskResponse getFetchTaskDetails(Integer userId) {
        // Fetch user information
        User user = iUserDAO.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Fetch groups the user created or belongs to (assuming created_by_id ties user to groups)
        List<UserGroups> groups = iUserGroupDAO.findByCreatedById(userId);

        // Extract group IDs from the UserGroups list
        List<Integer> groupIds = groups.stream()
                .map(UserGroups::getId)  // Assuming `getId()` returns the group ID
                .collect(Collectors.toList());

        // Fetch expenses related to the user or the groups they belong to
        List<Expense> expenses = iExpenseDAO.findByUserOrGroupIds(userId, groupIds);

        // Fetch splits for the expenses
        List<ExpenseSplit> expenseSplits = iExpenseSplitDAO.findByExpenseIdIn(
                expenses.stream().map(Expense::getId).collect(Collectors.toList())
        );

        // Aggregate the data
        FetchTaskResponse response = new FetchTaskResponse();
        response.setUser(user);
        response.setGroups(groups);
        response.setExpenses(expenses);
        response.setExpenseSplits(expenseSplits);

        return response;
    }
}
