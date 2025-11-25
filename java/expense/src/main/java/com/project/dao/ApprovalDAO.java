package com.project.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.project.util.Database;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApprovalDAO {
    public boolean updateApproval(int managerId, int expenseId, String status, String comment){
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
            if(rowsAffected == 0) throw new Exception("Tried to update non-existant record!");

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
