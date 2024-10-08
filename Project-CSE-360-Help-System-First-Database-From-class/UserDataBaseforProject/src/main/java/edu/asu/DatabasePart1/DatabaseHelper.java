package edu.asu.DatabasePart1;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A helper class to store data
 * Credit: CSE360-Fall2024 class by Carter
 * Copied with minor edit to match the purposes
 */
class DatabaseHelper {

	// JDBC driver name and database URL 
	static final String JDBC_DRIVER = "org.h2.Driver";   
	static final String DB_URL = "jdbc:h2:./UserDatabase";  

	//  Database credentials 
	static final String USER = "sa"; 
	static final String PASS = ""; 

	private Connection connection = null;
	private Statement statement = null; 
	//	PreparedStatement pstmt
	
	public void exportTableToFile() throws IOException, SQLException{
	    String query = "SELECT * FROM cse360users";
	    String fileName = "users_export.txt"; // Specify the file name here
	    
	    try
	    {
	    	statement = connection.createStatement();
	        ResultSet resultSet = statement.executeQuery(query);
	        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
	        // Write header
	    	 writer.write(String.format("%-5s %-20s %-30s %-20s %-10s %-20s %-20s %-20s %-20s", 
	                 "ID", "Username", "Email", "Password", "Role", 
	                 "First Name", "Last Name", "Middle Name", "Preferred Name"));
	         writer.newLine();

	        // Write data rows
	        while (resultSet.next()) {
	            int id = resultSet.getInt("id");
	            String username = resultSet.getString("username");
	            String email = resultSet.getString("email");
	            String password = resultSet.getString("password");
	            String role = resultSet.getString("role");
	            String firstName = resultSet.getString("firstName");
	            String lastName = resultSet.getString("lastName");
	            String middleName = resultSet.getString("middleName");
	            String preferredFirstName = resultSet.getString("preferredFirstName"); //preferredFirstName

	            // Write a line to the file
	            writer.write(String.format("%-5d %-20s %-30s %-20s %-10s %-20s %-20s %-20s %-20s",
	                    id, username, email, password, role, 
	                    firstName, lastName, middleName, preferredFirstName));
	            writer.newLine();
	        }
	        writer.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw e; // Rethrow or handle as appropriate
	    } catch (IOException e) {
	        e.printStackTrace();
	        throw e; // Rethrow or handle as appropriate
	    }
	}
	
	public void clearTable() throws SQLException{
		String truncateQuery = "DROP TABLE IF EXISTS cse360users";
		try
		{
			statement = connection.createStatement();
			statement.executeUpdate(truncateQuery);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * A method to connect to database
	 * @throws SQLException
	 * If not found then print out errors
	 */
	public void connectToDatabase() throws SQLException {
		try {
			Class.forName(JDBC_DRIVER); // Load the JDBC driver
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			statement = connection.createStatement(); 
			createTables();  // Create the necessary tables if they don't exist
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC Driver not found: " + e.getMessage());
		}
	}
	/**
	 * A method to create SQL table if not existed
	 * @throws SQLException
	 */
	private void createTables() throws SQLException {
		String userTable = "CREATE TABLE IF NOT EXISTS cse360users ("
			    + "id INT AUTO_INCREMENT PRIMARY KEY, "
			    + "username VARCHAR(255) UNIQUE, "
			    + "email VARCHAR(255) UNIQUE, "
			    + "password VARCHAR(255), "
			    + "role VARCHAR(20), "
			    + "firstName VARCHAR(20), "
			    + "middleName VARCHAR(20), "
			    + "lastName VARCHAR(20), "
			    + "preferredFirstName VARCHAR(20))";
		statement.execute(userTable);
	}


	// Check if the database is empty
	public boolean isDatabaseEmpty() throws SQLException {
		String query = "SELECT COUNT(*) AS count FROM cse360users";
		ResultSet resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			return resultSet.getInt("count") == 0;
		}
		return true;
	}
	/**
	 * A method to insert the data to table up on register
	 * @param username: the username that user enter
	 * @param password: the password
	 * @param role: the role
	 * @throws SQLException
	 * If every thing is valid the data will be insert
	 */
	public void register(String username, String password, String role) throws SQLException {
		//the command to that we would like to execute on the table
		String insertUser = "INSERT INTO cse360users (username, password, role) VALUES (?, ?, ?)";
		//set the data to insert
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			pstmt.setString(3, role);
			pstmt.executeUpdate();
		}
	}
	/**
	 * A method to update userInfo upon first time log in
	 * @param userID: the userID to check for the right user
	 * @param email
	 * @param firstName
	 * @param middleName
	 * @param lastName
	 * @param preferredFirstName
	 * @throws SQLException
	 */
	public void updateInfo(int userID, String email, String firstName, String middleName, String lastName, String preferredFirstName)
	throws SQLException
	{
		//the command that we would like to execute on the table
		String updateInfo = "UPDATE cse360users SET email = ?, firstName = ?, middleName = ?, lastName = ?, preferredFirstName = ?"
						+ "WHERE id = ?";
		//set the data to update
		try(PreparedStatement pstmt = connection.prepareStatement(updateInfo))
		{
			pstmt.setString(1,  email);
			pstmt.setString(2,  firstName);
			pstmt.setString(3,  middleName);
			pstmt.setString(4, lastName);
			pstmt.setString(5, preferredFirstName);
			pstmt.setInt(6, userID);
			pstmt.executeUpdate(); //update
		}	
	}
	/**
	 * A method for a user to log in
	 * @param username
	 * @param password
	 * @return true if login successfully
	 * @throws SQLException
	 */
	public boolean login(String username, String password) throws SQLException {
		String query = "SELECT * FROM cse360users WHERE username = ? AND password = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		}
	}
	/**
	 * A method to check if username exists
	 * @param username
	 * @return true if exists
	 */
	public boolean doesUserExist(String username) {
	    String query = "SELECT COUNT(*) FROM cse360users WHERE username = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        
	        pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            // If the count is greater than 0, the user exists
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // If an error occurs, assume user doesn't exist
	}
	/**
	 * A method to display all user in the database
	 * @throws SQLException
	 */
	public void displayUsersByAdmin() throws SQLException{
		String sql = "SELECT * FROM cse360users"; 
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql); 

		while(rs.next()) { 
			// Retrieve by column name 
			int id  = rs.getInt("id"); 
			String  username = rs.getString("username"); 
			String email = rs.getString("email");
			String fullname = rs.getString("firstName") + rs.getString("middleName") + rs.getString("lastName");
			String role = rs.getString("role");  

			// Display values 
			System.out.print("ID: " + id); 
			System.out.print(", Username: " + username); 
			System.out.print(", Full Name: " + fullname); 
			System.out.print(", Email: " + email);
			System.out.println(", Role: " + role); 
		} 
	}
	
	/*
	public void displayUsersByUser() throws SQLException{
		String sql = "SELECT * FROM cse360users"; 
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql); 

		while(rs.next()) { 
			// Retrieve by column name 
			int id  = rs.getInt("id"); 
			String  email = rs.getString("email"); 
			String password = rs.getString("password"); 
			String role = rs.getString("role");  

			// Display values 
			System.out.print("ID: " + id); 
			System.out.print(", Age: " + email); 
			System.out.print(", First: " + password); 
			System.out.println(", Last: " + role); 
		} 
	}

*/
	public void closeConnection() {
		try{ 
			if(statement!=null) statement.close(); 
		} catch(SQLException se2) { 
			se2.printStackTrace();
		} 
		try { 
			if(connection!=null) connection.close(); 
		} catch(SQLException se){ 
			se.printStackTrace(); 
		} 
	}

}
