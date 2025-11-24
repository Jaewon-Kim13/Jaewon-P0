package com.project;

import java.util.Scanner;

import com.project.service.ApprovalService;
import com.project.service.ExpenseService;
import com.project.service.UserService;

public class Main {
    public static void main(String[] args) {

        UserService userService = new UserService();
        ExpenseService expenseService = new ExpenseService();
        ApprovalService approvalService = new ApprovalService();
        Scanner scanner = new Scanner(System.in);
        int userId = -1;

        app: while(true){
            System.out.println("""
                               Welcome to Manager Suite:
                               1.)Login
                               2.)Create Account
                               3.)Exit""");
            String input = scanner.nextLine();
            switch(input){
                case("1")->{
                    System.out.println("Enter Username: ");
                    String username = scanner.nextLine();
                    System.out.println("Enter Password: ");
                    String password = scanner.nextLine();
                    userId = userService.login(username, password);
                    if(userId == -1){
                        System.out.println("Login failed: Incorrect Username/Password");
                        continue;
                    }
                    break app;
                }
                case("2")->{
                    System.out.println("Enter Username: ");
                    String username = scanner.nextLine();
                    System.out.println("Enter Password: ");
                    String password = scanner.nextLine();
                    userId = userService.createManager(username, password);
                    if(userId == -1){
                        System.out.println("Login failed");
                        continue;
                    }
                    break app;
                }
                case("3")->{
                    System.exit(0);
                }
                default->{
                    System.out.println("");
                }
            }
        }

        System.out.println("Login Successful!");

        main: while (true){
            System.out.println("""
                Welcome to the Manager app:
                1.)View All Pending Expenses
                2.)Generate Reports
                3.)Update Expense Aproval
                4.)Exit
            """);
            String input = scanner.nextLine();
            switch(input){
                case("1")->{
                    expenseService.viewPendingApprovals();
                }
                case("2")->{
                    System.out.println("Enter Option(date, employee_id, category): ");
                    String option = scanner.nextLine();
                    System.out.println("Enter value: ");
                    String parameter = scanner.nextLine();
                    expenseService.generateReport(option, parameter);
                }
                case("3")->{
                    System.out.println("Enter Expense_id: ");
                    int expenseID = Integer.parseInt(scanner.nextLine());
                    System.out.println("Enter status(approved|denied): ");
                    String status = scanner.nextLine();
                    System.out.println("Enter comment: ");
                    String comment = scanner.nextLine();
                    approvalService.updateApproval(userId,expenseID, status, comment);
                }
                case("4")->{break main;}
                default->{}
            }
        }

        scanner.close();
    }
}