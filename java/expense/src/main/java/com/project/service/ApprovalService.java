package com.project.service;

import com.project.dao.ApprovalDAO;

public class ApprovalService {
    private final ApprovalDAO approvalDao = new ApprovalDAO();

    public void updateApproval(int id, int managerId, String status, String comment){        
        try { 
            if(!status.equals("approved") && !status.equals("denied")) throw new Exception("Enter Valid Status('approved' or 'denied')");
            if(approvalDao.updateApproval(managerId, id, status, comment)) System.out.println("Update Successful!");
        }catch (Exception ex) {
            System.out.println("Update failed:"+ex.getMessage());
        } 
    }
}
