package edu.asu.DatabasePart1;
import java.sql.SQLException;
import java.util.Scanner;
import java.io.IOException;

public class StartCSE360 {

	private static final DatabaseHelper databaseHelper = new DatabaseHelper();
	private static final Scanner scanner = new Scanner(System.in);

	public static void main( String[] args )
	{

		try 
		{ 	
			databaseHelper.connectToDatabase();  // Connect to the database
			databaseHelper.register("test1", "1", "student");
			databaseHelper.resetUserAccount("test1");
			databaseHelper.exportTableToFile();
			databaseHelper.exportOneTimeCodeList();
			databaseHelper.clearTable();
		}
		 catch (SQLException e) 
		{
			System.err.println("Database error: " + e.getMessage());
			e.printStackTrace();
		}
		catch (IOException e)
		{
			System.err.println("IO error: " + e.getMessage());
			e.printStackTrace(); // Handle IOException
		}
		finally {
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
	/*
	private static void userFlow() throws SQLException {
		String email = null;
		String password = null;
		System.out.println("user flow");
		System.out.print("What would you like to do 1.Register 2.Login  ");
		String choice = scanner.nextLine();
		switch(choice) {
		case "1": 
			System.out.print("Enter User Email: ");
			email = scanner.nextLine();
			System.out.print("Enter User Password: ");
			password = scanner.nextLine(); 
			// Check if user already exists in the database
		    if (!databaseHelper.doesUserExist(email)) {
		        databaseHelper.register(email, password, "user");
		        System.out.println("User setup completed.");
		    } else {
		        System.out.println("User already exists.");
		    }
			break;
		case "2":
			System.out.print("Enter User Email: ");
			email = scanner.nextLine();
			System.out.print("Enter User Password: ");
			password = scanner.nextLine();
			if (databaseHelper.login(email, password)) {
				System.out.println("User login successful.");
//				databaseHelper.displayUsers();

			} else {
				System.out.println("Invalid user credentials. Try again!!");
			}
			break;
		}
	}
*/
	private static void adminFlow() throws SQLException {
		System.out.println("admin flow");
		System.out.print("Enter Username: ");
		String username = scanner.nextLine();
		System.out.print("Enter Admin Password: ");
		String password = scanner.nextLine();
		if (databaseHelper.login(username, password)) 
		{
			System.out.println("Admin login successful.");
			databaseHelper.displayUsersByAdmin();

		} else {
			System.out.println("Invalid admin credentials. Try again!!");
		}
	}


}
