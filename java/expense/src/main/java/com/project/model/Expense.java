package com.project.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Expense {
    private int id;
    private int userId;
    private double amount;
    private String description;
    private String category;
    private Date date;
}
