package com.project.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.project.Database;
import com.project.model.Approval;
import com.project.model.Expense;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExpenseDAO {
        public static List<Expense> viewPendingApprovals(int managerId, int expenseId, String status, String comment){
        ArrayList<Expense> pending = new ArrayList<>();
        String sql = "Select"
                    +"e.id"
                    +"e.user_id"
                    +"e.amount"
                    +"e.expense_description"
                    +"e.category"
                    +"e.expense_date"
                    +"FROM expenses e"
                    +"INNER JOIN approvals a ON e.id = a.expense_id"
                    +"WHERE a.approval_status LIKE 'pending'";
        try{
            
            Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Expense expense = new Expense(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getDouble("amount"),
                    rs.getString("expense_description"),
                    rs.getString("category"),
                    rs.getDate("expense_date")
                );
                pending.add(expense);
            }
        } catch (Exception e) {
    
        }
        return pending;
    }

    public static HashMap<Expense, Approval> generateReport(String option, String parameter) throws Exception{
        HashMap<Expense, Approval> report = new HashMap<>();
        String sql = "Select"
            +"e.id"
            +"e.user_id"
            +"e.amount"
            +"e.expense_description"
            +"e.category"
            +"e.expense_date"
            +"a.id"
            //skipping a.expense_id
            +"a.approval_status"
            +"a.reviewer"
            +"a.comment"
            +"a.review_date"
            +"FROM expenses e"
            +"INNER JOIN approvals a ON e.id = a.expense_id";
    
        switch(option){
            case "employee" ->{
                int employeeId = Integer.parseInt(parameter);
                sql+="WHERE e.user_id="+employeeId;
            }
            case "category"->{
                sql+="WHERE e.category="+parameter;
            }
            case "date"->{

            } 
            default ->{
                throw new Exception("invalid selction");
            }
        }
        return report;
    }
}
