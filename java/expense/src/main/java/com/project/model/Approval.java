package com.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Approval {
    private int id;
    private int expenseId;
    private String status;
    private int reviewer;
    private String comment;
    private String reviewDate;
}
