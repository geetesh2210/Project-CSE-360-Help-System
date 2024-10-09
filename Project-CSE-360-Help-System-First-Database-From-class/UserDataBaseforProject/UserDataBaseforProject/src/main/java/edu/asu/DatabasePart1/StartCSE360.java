package edu.asu.DatabasePart1;
import java.sql.SQLException;
import java.util.Scanner;
import java.io.IOException;


public class StartCSE360 {

	private static final DatabaseHelper databaseHelper = new DatabaseHelper();
	private static final Scanner scanner = new Scanner(System.in);

	public static void main( String[] args )
	{

		try { 	
			databaseHelper.connectToDatabase();  // Connect to the database

			// Check if the database is empty (no users registered)
			if (databaseHelper.isDatabaseEmpty()) {
				System.out.println( "In-Memory Database is empty" );
				//set up administrator access
				setupAdministrator();
				databaseHelper.exportTableToFile();
			}
			else {
				System.out.println( "If you are an administrator, then select A\nIf you want to delete all data select D\nEnter your choice:  " );
				String role = scanner.nextLine();

				switch (role) {
				case "A":
					adminFlow();
					break;
				case "D":
					databaseHelper.clearTable();
					break;
				default:
					System.out.println("Invalid choice. Please select 'A', 'D'");
					databaseHelper.closeConnection();
				}

			}
		} catch (SQLException e) {
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
