package com.project.service;

import java.util.HashMap;
import java.util.List;

import com.project.dao.ExpenseDAO;
import com.project.model.Approval;
import com.project.model.Expense;

import de.vandermeer.asciitable.AsciiTable;

public class ExpenseService {
    private final ExpenseDAO expenseDao = new ExpenseDAO();

    public void viewPendingApprovals(){
        try {
            AsciiTable at = new AsciiTable();
            at.addRule();
            at.addRow("ID","User ID", "Amount", "Description", "Category", "Date");
            at.addRule();
            List<Expense> pending = expenseDao.viewPendingApprovals();
            for(Expense e: pending){
                at.addRow(
                    e.getId(), 
                    e.getUserId(), 
                    e.getAmount(), 
                    e.getDescription() != null ? e.getDescription() : "-", 
                    e.getCategory() != null ? e.getCategory() : "-", 
                    e.getDate() != null ? e.getDate() : "-" 
                );
                at.addRule(); 
            }
            System.out.println(at.render());
        } catch (Exception e) {
            System.out.println("Failed to view pending expenses: "+e.getMessage());
        }
    }

    public void generateReport(String option, String parameter){
        try {
            HashMap<Expense, Approval> report = expenseDao.generateReport(option, parameter);
            AsciiTable at = new AsciiTable();
            at.addRule();
            at.addRow("ID", "User ID", "Amount", "Description", "Category", "Date","Status","Reviewer","Comment", "Review Date");
            at.addRule();
            report.forEach((key, value)->{
                Expense e = (Expense) key;
                Approval a = (Approval) value;
                at.addRow(
                    e.getId(), 
                    e.getUserId(), 
                    e.getAmount(), 
                    e.getDescription() != null ? e.getDescription() : "-", 
                    e.getCategory() != null ? e.getCategory() : "-", 
                    e.getDate() != null ? e.getDate() : "-", 
                    a.getStatus() != null ? a.getStatus() : "-", 
                    a.getReviewer() != 0 ? a.getReviewer() : "-", 
                    a.getComment() != null ? a.getComment() : "-", 
                    a.getReviewDate() != null ? a.getReviewDate() : "-"
                );
                at.addRule();
            });
            System.out.println(at.render());
        } catch (Exception e) {
            System.out.println("Failed to generate report: "+e.getMessage());
        }
    }   
}
