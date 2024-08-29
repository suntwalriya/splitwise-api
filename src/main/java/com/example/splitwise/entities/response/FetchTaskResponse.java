package com.example.splitwise.entities.response;

import com.example.splitwise.repository.table.Expense;
import com.example.splitwise.repository.table.ExpenseSplit;
import com.example.splitwise.repository.table.User;
import com.example.splitwise.repository.table.UserGroups;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FetchTaskResponse {
    private User user;
    private List<UserGroups> groups;
    private List<Expense> expenses;
    private List<ExpenseSplit> expenseSplits;

}
