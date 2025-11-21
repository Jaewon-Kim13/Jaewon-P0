package com.project.model;

import java.util.Date;

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
    private Date reviewDate;
}
