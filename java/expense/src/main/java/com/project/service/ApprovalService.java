package com.project.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.project.dao.ApprovalDAO;

public class ApprovalService {
    private static final Logger logger = LoggerFactory.getLogger(ApprovalService.class);
    private final ApprovalDAO approvalDao = new ApprovalDAO();

    public void updateApproval(int id, int managerId, String status, String comment){
        logger.info("Approval update requested - expense_id: {}, manager_id: {}, status: {}", id, managerId, status);
        try { 
            if(!status.equals("approved") && !status.equals("denied")) {
                logger.warn("Invalid status attempted: {}", status);
                throw new Exception("Enter Valid Status('approved' or 'denied')");
            }
            if(approvalDao.updateApproval(managerId, id, status, comment)) {
                logger.info("Approval update successful for expense_id: {}", id);
                System.out.println("Update Successful!");
            }
        }catch (Exception ex) {
            logger.error("Approval update failed for expense_id: {}", id, ex);
            System.out.println("Update failed:"+ex.getMessage());
        } 
    }
}
