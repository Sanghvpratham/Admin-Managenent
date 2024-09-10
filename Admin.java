package Finalone;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

class Employee {
    String id;
    String password;

    Employee(String id, String password) {
        this.id = id;
        this.password = password;
    }
}

public class Admin {
    static ArrayList<Employee> employees = new ArrayList<>();
    static final String URL = "jdbc:mysql://127.0.0.1:3306/new_schema";
    static final String USERNAME = "root";
    static final String PASSWORD = "1234";

    public static void main(String[] args) {
        // Load the MySQL JDBC driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC driver not found!");
            e.printStackTrace();
            return;
        }

        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("Select an option:");
            System.out.println("1. Admin");
            System.out.println("2. Employee");
            System.out.println("0. Exit");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    adminMenu(sc);
                    break;
                case 2:
                    employeeMenu(sc);
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);

        sc.close();
    }

    static void adminMenu(Scanner sc) {
        int adminChoice;
        do {
            System.out.println("Admin Menu:");
            System.out.println("1. Create new employee ID and password");
            System.out.println("2. View list of old employees");
            System.out.println("0. Back to main menu");
            adminChoice = sc.nextInt();

            switch (adminChoice) {
                case 1:
                    createNewEmployee(sc);
                    break;
                case 2:
                    viewOldEmployees();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (adminChoice != 0);
    }

    static void createNewEmployee(Scanner sc) {
        System.out.println("Enter employee ID:");
        String id = sc.next();
        System.out.println("Enter employee password:");
        String password = sc.next();

       
        try (Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String sql = "INSERT INTO employees (id, password) VALUES (?,?)";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, id);
            statement.setString(2, password);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("New employee added successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Error creating new employee: " + e.getMessage());
        }
    }

    static void viewOldEmployees() {
       
        try (Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String sql = "SELECT id FROM employees";
            PreparedStatement statement = con.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            System.out.println("List of old employees:");
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                System.out.println("ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving old employees: " + e.getMessage());
        }
    }

    static void employeeMenu(Scanner sc) {
        System.out.println("Here Company Welcome You.");
        System.out.println("Please Login Using Registered id and Password to access your details");
        System.out.println("Enter ID");
        String enteredID = sc.next();
        System.out.println("Enter Password");
        String enteredPassword = sc.next();
        boolean found = false;
       
        try (Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM employees WHERE id = ? AND password = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, enteredID);
            statement.setString(2, enteredPassword);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                found = true;
                System.out.println("Detail: " + resultSet.getString("id"));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving employee details: " + e.getMessage());
        }
        if (!found) {
            System.out.println("Wrong ID or password.");
        }
    }
}
