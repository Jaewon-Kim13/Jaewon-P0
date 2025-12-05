package com.project;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.project.service.ApprovalService;
import com.project.service.ExpenseService;
import com.project.service.UserService;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    
    public static void main(String[] args) {
        logger.info("Application started");

        UserService userService = new UserService();
        ExpenseService expenseService = new ExpenseService();
        ApprovalService approvalService = new ApprovalService();
        Scanner scanner = new Scanner(System.in);
        int userId = -1;

        app: while(true){
            System.out.println(
            """
            ===========================
             Welcome to Manager Suite:
            ===========================
                1.)Login
                2.)Create Account
                3.)Exit
            ===========================
            """);
            System.out.print("Enter Selection: ");
            String input = scanner.nextLine();
            logger.debug("User selected option: {}", input);
            switch(input){
                case("1")->{
                    logger.info("Login option selected");
                    System.out.print("\nEnter Username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter Password: ");
                    String password = scanner.nextLine();
                    userId = userService.login(username, password);
                    if(userId == -1){
                        logger.warn("Login failed for username: {}", username);
                        System.out.println("Login failed: Incorrect Username/Password");
                        continue;
                    }
                    break app;
                }
                case("2")->{
                    logger.info("Create account option selected");
                    System.out.print("Enter Username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter Password: ");
                    String password = scanner.nextLine();
                    userId = userService.createManager(username, password);
                    if(userId == -1){
                        logger.warn("Account creation failed for username: {}", username);
                        System.out.println("Login failed: Incorrect username/password");
                        continue;
                    }
                    break app;
                }
                case("3")->{
                    logger.info("User exiting application");
                    System.exit(0);
                }
                default->{
                    logger.debug("Invalid selection: {}", input);
                    System.out.println("Invalid Selction!");
                }
            }
        }

        logger.info("User logged in successfully with userId: {}", userId);
        System.out.println("Login Successful!");

        main: while (true){
            System.out.println("""
            \n=====================================
                Welcome to the Manager app:
            =====================================
                1.)View All Pending Expenses
                2.)Generate Reports
                3.)Update Expense Aproval
                4.)Exit
            =====================================
            """);
            System.out.print("Enter Selection: ");
            String input = scanner.nextLine();
            logger.debug("Main menu selection: {}", input);
            switch(input){
                case("1")->{
                    logger.info("Viewing pending expenses");
                    expenseService.viewPendingApprovals();
                }
                case("2")->{
                    logger.info("Generating report");
                    System.out.print("Enter Option(date, employee_id, category): ");
                    String option = scanner.nextLine();
                    if(option.equals("date")){
                        System.out.print("Enter value(YYYY-MM-DD): ");
                    }
                    System.out.print("Enter value: ");
                    String parameter = scanner.nextLine();
                    expenseService.generateReport(option, parameter);
                }
                case("3")->{
                    logger.info("Updating expense approval");
                    try {
                        System.out.print("Enter Expense_id(#): ");
                        int expenseID = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter status(approved|denied): ");
                        String status = scanner.nextLine();
                        System.out.print("Enter comment: ");
                        String comment = scanner.nextLine();
                        approvalService.updateApproval(expenseID, userId, status, comment);
                    } catch (Exception e) {
                        logger.error("Invalid input during approval update", e);
                        System.out.println("Enter Valid Input");
                    }
                    
                }
                case("4")->{
                    logger.info("User exiting main menu");
                    break main;
                }
                default->{
                    logger.debug("Invalid main menu selection: {}", input);
                }
            }
        }

        logger.info("Application shutting down");
        scanner.close();
    }
}