package edu.asu.DatabasePart1;
import java.sql.SQLException;
import java.util.Scanner;
import java.io.IOException;

public class StartCSE360 {

	private static final DatabaseHelper databaseHelper = new DatabaseHelper();
	private static final Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {

		try { 	
			databaseHelper.connectToDatabase();  // Connect to the database

			// Check if the database is empty (no users registered)
			if (databaseHelper.isDatabaseEmpty()) {
				System.out.println("In-Memory Database is empty");
				// Set up administrator access
				setupAdministrator();
				databaseHelper.exportTableToFile();
			} else {
				// Directly perform admin flow (skip choice prompt)
				adminFlow();
			}
		} catch (SQLException e) {
			System.err.println("Database error: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IO error: " + e.getMessage());
			e.printStackTrace(); // Handle IOException
		} finally {
			System.out.println("Good Bye!!");
			databaseHelper.closeConnection();
		}
	}

	private static void setupAdministrator() throws SQLException {
		System.out.println("Setting up the Administrator access.");
		System.out.print("Enter Admin username: ");
		String username = scanner.nextLine();
		System.out.print("Enter Admin Password: ");
		String password = scanner.nextLine();
		databaseHelper.register(username, password, "admin");
		System.out.println("Administrator setup completed.");
	}

	private static void adminFlow() throws SQLException {
	    System.out.println("Admin Flow (with hardcoded credentials)");

	    // Hardcoded admin credentials
	    String username = "admin";
	    String password = "123";

	    // Attempt to log in with hardcoded credentials
	    if (databaseHelper.login(username, password)) {
	        System.out.println("Admin login successful.");

	        // Perform admin tasks
	        databaseHelper.updateInfo("student", "new_email@student.com", "John", "D", "Doe", "Dr.");
	        System.out.println("Student info updated successfully.");

	        boolean roleRemoved = databaseHelper.removeUserRole("student", "student");
	        if (roleRemoved) {
	            System.out.println("Student role removed successfully.");
	        } else {
	            System.out.println("Failed to remove student role.");
	        }

	        // Display all users by admin
	        System.out.println("Displaying Users:");
	        databaseHelper.displayUsersByAdmin();

	    } else {
	        System.out.println("Invalid admin credentials. Please try again.");
	    }
	}
}

