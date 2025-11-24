package com.project.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;

import com.project.util.Database;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApprovalDAO {
    public boolean updateApproval(int managerId, int expenseId, String status, String comment){
        String sql = "UPDATE approval SET reviewer=?, approval_status=?, conmment=?, review_date=?, WHERE expense_id=?";
        Connection connection = Database.getConnection();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, managerId);
            preparedStatement.setString(2, status);
            preparedStatement.setString(3, status);
            preparedStatement.setDate(4, Date.valueOf(LocalDate.now()));
            preparedStatement.setInt(5, managerId);

            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected == 0) throw new Exception("Tried to update non-existant record!");

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
