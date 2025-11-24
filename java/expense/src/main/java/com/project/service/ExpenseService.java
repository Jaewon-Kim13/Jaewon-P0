package com.project.service;

import java.util.HashMap;
import java.util.List;

import com.project.dao.ExpenseDAO;
import com.project.model.Approval;
import com.project.model.Expense;

public class ExpenseService {
    private final ExpenseDAO expenseDao = new ExpenseDAO();

    public void viewPendingApprovals(){
        try {
            List<Expense> pending = expenseDao.viewPendingApprovals();
            for(Expense expense: pending) System.out.println(expense); 
        } catch (Exception e) {
            System.out.println("Failed to view pending expenses: "+e.getMessage());
        }
    }

    public void generateReport(String option, String parameter){
        try {
            HashMap<Expense, Approval> report = expenseDao.generateReport(option, parameter);
            report.forEach((key, value)->{
                System.out.println("Expense: "+key+" Approval: "+value);
            });
        } catch (Exception e) {
            System.out.println("Failed to generate report: "+e.getMessage());
        }
    }   
}
