package com.project.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.project.util.Database;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApprovalDAO {
    private static final Logger logger = LoggerFactory.getLogger(ApprovalDAO.class);
    
    public boolean updateApproval(int managerId, int expenseId, String status, String comment){
        logger.info("Updating approval for expense_id: {}, status: {}", expenseId, status);
        
        String sql = "UPDATE approvals SET reviewer=?, approval_status=?, comment=?, review_date=? WHERE expense_id=?";
        Connection connection = Database.getConnection();

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDateString = currentDate.format(formatter);
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, managerId);
            preparedStatement.setString(2, status);
            preparedStatement.setString(3, comment);
            preparedStatement.setString(4, formattedDateString);
            preparedStatement.setInt(5, expenseId);

            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected == 0) {
                logger.warn("No rows updated - expense_id {} may not exist", expenseId);
                throw new Exception("Tried to update non-existant record!");
            }
            logger.info("Successfully updated approval for expense_id: {}", expenseId);
            return true;
        } catch (Exception e) {
            logger.error("Failed to update approval for expense_id: {}", expenseId, e);
        }
        return false;
    }
}
