package com.project.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.project.model.Approval;
import com.project.model.Expense;
import com.project.util.Database;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExpenseDAO {
        public List<Expense> viewPendingApprovals(){
        ArrayList<Expense> pending = new ArrayList<>();
        String sql = "SELECT "
                    +"e.id, "
                    +"e.user_id, "
                    +"e.amount, "
                    +"e.expense_description, "
                    +"e.category, "
                    +"e.expense_date "
                    +"FROM expenses e "
                    +"INNER JOIN approvals a ON e.id = a.expense_id "
                    +"WHERE a.approval_status LIKE 'pending'";
        Connection connection = Database.getConnection();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Expense expense = new Expense(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getDouble("amount"),
                    rs.getString("expense_description"),
                    rs.getString("category"),
                    rs.getString("expense_date")
                );
                pending.add(expense);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pending;
    }

    public HashMap<Expense, Approval> generateReport(String option, String parameter) throws Exception {
        HashMap<Expense, Approval> report = new HashMap<>();
        
        String sql = "SELECT "
            + "e.id, "
            + "e.user_id, "
            + "e.amount, "
            + "e.expense_description, "
            + "e.category, "
            + "e.expense_date, "
            + "a.id, "
            + "a.approval_status, "
            + "a.reviewer, "
            + "a.comment, "
            + "a.review_date "
            + "FROM expenses e "
            + "INNER JOIN approvals a ON e.id = a.expense_id ";
        Connection connection = Database.getConnection();
        try {
            PreparedStatement ps;
            switch(option) {
                case "employee" -> {
                    sql += "WHERE e.user_id = ?";
                    ps = connection.prepareStatement(sql);
                    ps.setInt(1, Integer.parseInt(parameter));
                }
                case "category" -> {
                    sql += "WHERE e.category = ?";
                    ps = connection.prepareStatement(sql);
                    ps.setString(1, parameter);
                }
                case "date" -> {
                    sql += "WHERE e.expense_date = ?";  // Fixed column name
                    ps = connection.prepareStatement(sql);
                    ps.setDate(1, java.sql.Date.valueOf(parameter)); // Better date handling
                }
                    
                default -> throw new Exception("Invalid selection: " + option);
            }
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Expense expense = new Expense(
                    rs.getInt(1),      // e.id
                    rs.getInt(2),      // e.user_id
                    rs.getDouble(3),   // e.amount
                    rs.getString(4),   // e.expense_description
                    rs.getString(5),   // e.category
                    rs.getString(6)      // e.expense_date
                );
                
                Approval approval = new Approval(
                    rs.getInt(7),      // a.id
                    rs.getInt(1),      // e.id (for expense_id)
                    rs.getString(8),   // a.approval_status
                    rs.getInt(9),      // a.reviewer
                    rs.getString(10),  // a.comment
                    rs.getDate(11)     // a.review_date
                );
                
                report.put(expense, approval);
            }
            
        } catch (SQLException e) {
            throw new Exception("Database error while generating report: " + e.getMessage(), e);
        }

        return report;
    }
}
