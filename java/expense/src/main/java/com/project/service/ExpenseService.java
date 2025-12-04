package com.project.service;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.project.dao.ExpenseDAO;
import com.project.model.Approval;
import com.project.model.Expense;

import de.vandermeer.asciitable.AsciiTable;

public class ExpenseService {
    private static final Logger logger = LoggerFactory.getLogger(ExpenseService.class);
    private final ExpenseDAO expenseDao = new ExpenseDAO();

    public void viewPendingApprovals(){
        logger.info("Viewing pending approvals");
        try {
            AsciiTable at = new AsciiTable();
            at.addRule();
            at.addRow("ID","User ID", "Amount", "Description", "Category", "Date");
            at.addRule();
            List<Expense> pending = expenseDao.viewPendingApprovals();
            logger.info("Retrieved {} pending approvals", pending.size());
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
            logger.info("Pending approvals displayed successfully");
        } catch (Exception e) {
            logger.error("Failed to view pending expenses", e);
            System.out.println("Failed to view pending expenses: "+e.getMessage());
        }
    }

    public void generateReport(String option, String parameter){
        logger.info("Generating report - option: {}, parameter: {}", option, parameter);
        try {
            HashMap<Expense, Approval> report = expenseDao.generateReport(option, parameter);
            logger.info("Report generated with {} entries", report.size());
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
            logger.info("Report displayed successfully");
        } catch (Exception e) {
            logger.error("Failed to generate report - option: {}, parameter: {}", option, parameter, e);
            System.out.println("Failed to generate report: "+e.getMessage());
        }
    }   
}