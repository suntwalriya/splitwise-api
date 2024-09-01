package com.example.splitwise.entities.response;

import com.example.splitwise.repository.table.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FetchTaskResponse {
    private User user;
    private double overallBalance;
    private Map<Integer, Double> friendBalances;
    private Map<Integer, Double> groupBalances;
}
