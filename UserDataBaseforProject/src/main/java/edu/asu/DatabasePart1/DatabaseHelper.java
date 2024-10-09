package edu.asu.DatabasePart1;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
	
	/**
	 * A Function to export the current data table to a text file for debugging
	 * @throws IOException
	 * @throws SQLException
	 */
	public void exportTableToFile() throws IOException, SQLException{
	    String query = "SELECT * FROM cse360users";
	    String fileName = "users_export.txt"; // Specify the file name here
	    
	    try
	    {
	    	statement = connection.createStatement();
	        ResultSet resultSet = statement.executeQuery(query);
	        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
	        // Write header
	    	 writer.write(String.format("%-5s %-20s %-30s %-20s %-30s %-20s %-20s %-20s %-20s", 
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
	            writer.write(String.format("%-5d %-20s %-30s %-20s %-30s %-20s %-20s %-20s %-20s",
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
	/**
	 * A Function to export the current data table but only username and their onetime code
	 * If there is any
	 * @throws IOException
	 * @throws SQLException
	 */
	public void exportOneTimeCodeList() throws IOException, SQLException{
	    String query = "SELECT * FROM cse360users";
	    String fileName = "onetimecode_export.txt"; // Specify the file name here
	    
	    try
	    {
	    	statement = connection.createStatement();
	        ResultSet resultSet = statement.executeQuery(query);
	        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
	        // Write header
	    	 writer.write(String.format("%-20s %-30s %-30s", 
	                 "Username", "One-Time Code", "Date"));
	         writer.newLine();

	        // Write data rows
	        while (resultSet.next()) {
	            String username = resultSet.getString("username");
	            String onetimeCode = resultSet.getString("onetimeCode");
	            String expireDate = resultSet.getString("expireDate");
	            // Write a line to the file
	            writer.write(String.format("%-20s %-30s %-30s",
	                    username, onetimeCode, expireDate));
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
	/**
	 * Delete the SQL table
	 * @throws SQLException
	 */
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
			    + "role VARCHAR(255), "
			    + "firstName VARCHAR(20), "
			    + "middleName VARCHAR(20), "
			    + "lastName VARCHAR(20), "
			    + "preferredFirstName VARCHAR(20),"
			    + "onetimeCode	VARCHAR(10),"
			    + "expireDate	DATETIME)";
		statement.execute(userTable);
	}

	/**
	 * Checking if the database is empty
	 * @return true if DatabaseEmpty
	 * @throws SQLException
	 */
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
		role = getUserCount() == 0 ? "ADMIN" : "USER"; // Check if it's the first user
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
	 * @param username: the username to check for the right user
	 * @param email
	 * @param firstName
	 * @param middleName
	 * @param lastName
	 * @param preferredFirstName
	 * @throws SQLException
	 */
	public void updateInfo(String username, String email, String firstName, String middleName, String lastName, String preferredFirstName)
	throws SQLException
	{
		//the command that we would like to execute on the table
		String updateInfo = "UPDATE cse360users SET email = ?, firstName = ?, middleName = ?, lastName = ?, preferredFirstName = ?"
						+ "WHERE username = ?";
		//set the data to update
		try(PreparedStatement pstmt = connection.prepareStatement(updateInfo))
		{
			pstmt.setString(1,  email);
			pstmt.setString(2,  firstName);
			pstmt.setString(3,  middleName);
			pstmt.setString(4, lastName);
			pstmt.setString(5, preferredFirstName);
			pstmt.setString(6, username);
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
	/*******************************************************************************************************************/
	/*******************************************************************************************************************/
	/*******************************************************************************************************************/
	/**A BUNCH OF ADMIN RELATED FUNCTION**/
	
	
	
	/****************************************************************************************/
	/****************************************************************************************/
	/****************************************************************************************/
	//FUNCTION FOR ADMIN TO USE
	/**
	 * A function to reset userAccount from admin perspective
	 * @param username username to reset
	 * @throws SQLException
	 * @return the one time code
	 */
	public String resetUserAccount(String username) throws SQLException
	{
		String code = randomStringGenerator();
		ZonedDateTime expirationTimeUTC = ZonedDateTime.now(java.time.ZoneOffset.UTC).plusMinutes(10);
	    
	    // Format the expiration time to a suitable string format for the database
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    String formattedExpirationTime = expirationTimeUTC.format(formatter);

	    String updateQuery = "UPDATE cse360users SET onetimeCode = ?, expireDate = ? WHERE username = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(updateQuery)) {
	        pstmt.setString(1, code);
	        pstmt.setString(2, formattedExpirationTime); // Use the formatted expiration time
	        pstmt.setString(3, username);
	        
	        int rowsAffected = pstmt.executeUpdate();
	        if (rowsAffected > 0) {
	            System.out.println("User account reset successfully.");
	            return code;
	        } else {
	            System.out.println("No user found with that username.");
	            return "";
	        }
	    }	
	}
	/**
	 * Add role for a user
	 * @param username the username to look for role
	 * @param roleToAdd role to add
	 * @return true if added success, false if not
	 * @throws SQLException
	 */
	public boolean addUserRole(String username, String roleToAdd) throws SQLException
	{
		String currentRoles = getCurrentRoles(username);
	    if (currentRoles == "") {
	        System.out.println("User not found.");
	        return false;
	    }
	    else
	    {
	    	String newRoles = addRole(currentRoles, roleToAdd);
	    	if(newRoles.equals(currentRoles))
	    	{
	    		return false;
	    	}
	    	return updateRolesInDatabase(username, newRoles);
	    }	
	}
	
	/**
	 * Remove a role from user
	 * @param username
	 * @param roleToRemove
	 * @return
	 * @throws SQLException
	 */
	public boolean removeUserRole(String username, String roleToRemove) throws SQLException
	{
		String currentRoles = getCurrentRoles(username);
	    if (currentRoles == "") {
	        System.out.println("User not found.");
	        return false;
	    }
	    else
	    {
	    	String newRoles = removeRole(currentRoles, roleToRemove);
	    	System.out.println(currentRoles);
	    	System.out.println(newRoles);
	    	if(newRoles.equals(currentRoles))
	    	{
	    		System.out.println("Role not found.");
	    		return false;
	    	}
	    	return updateRolesInDatabase(username, newRoles);
	    }
		
	}
	/**
	 * A function to delete user using username
	 * @param username the username of the user we trynna delete
	 * @throws SQLException
	 */
	public boolean deleteUserAccount(String username) throws SQLException
	{
		String query = "DELETE * FROM cse360users WHERE username = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
			int rowsAffected = pstmt.executeUpdate();
		    if (rowsAffected > 0) 
		    {
		        System.out.println("User account deleted successfully.");
		        return true;
		    }
		    else 
		    {
		        System.out.println("No user found with that username."); 
		        return false;
			}
		}	
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
			String  username = rs.getString("username"); 
			String email = rs.getString("email");
			String fullname = rs.getString("firstName") + rs.getString("middleName") + rs.getString("lastName");
			String role = rs.getString("role");  

			// Display values 
			System.out.print("Username: " + username); 
			System.out.print(", Full Name: " + fullname); 
			System.out.print(", Email: " + email);
			System.out.println(", Role: " + role); 
		} 
	}

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
	/*************************************************************************************/
	/*************************************************************************************/
	/*************************************************************************************/
	/**Helper Function for class, not needed to use in program**/
	public String getCurrentRoles(String username) throws SQLException 
	{
	    String query = "SELECT role FROM cse360users WHERE username = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) 
	    {
	        pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            return rs.getString("role");
	        }
	        return ""; // User not found or error
	    }     
	}
	
	private String removeRole(String currentRoles, String role) 
	{
	    String[] rolesArray = currentRoles.split(" ");
	    StringBuilder updatedRoles = new StringBuilder();
	    for (String r : rolesArray) {
	        if (!r.equals(role)) {
	            updatedRoles.append(r).append(" ");
	        }
	    }
	    return updatedRoles.toString().trim(); // Remove trailing space
	}

	private String addRole(String currentRoles, String role) 
	{
	    if (currentRoles.isEmpty()) {
	        return role; // If no roles, just add the new one
	    }
	 // Split current roles into an array
	    String[] rolesArray = currentRoles.split(" ");
	    
	    // Check if the role already exists
	    for (String existingRole : rolesArray) {
	        if (existingRole.equals(role)) {
	            return currentRoles; // Return current roles unchanged if the role exists
	        }
	    }
	    
	    // Append new role if it does not exist
	    return currentRoles + " " + role;
	}
	
	private boolean updateRolesInDatabase(String username, String updatedRoles) throws SQLException
	{
	    String updateQuery = "UPDATE cse360users SET role = ? WHERE username = ?";

	    try (PreparedStatement pstmt = connection.prepareStatement(updateQuery)) 
	    {
	        pstmt.setString(1, updatedRoles);
	        pstmt.setString(2, username);
	        
	        int rowsAffected = pstmt.executeUpdate();
	        if (rowsAffected > 0) {
	            return true;
	        } else {
	            return false;
	        }
	    }
	}
	/**
	 * A helper function to return a random code
	 * @return a randomcode
	 */
	private String randomStringGenerator()
	{
		String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	    int CODE_LENGTH = 10;
	    SecureRandom random = new SecureRandom();

		StringBuilder code = new StringBuilder(CODE_LENGTH);
        
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }
		return code.toString();
	}
	private int getUserCount() throws SQLException {
	    String countQuery = "SELECT COUNT(*) FROM cse360users";
	    try (PreparedStatement pstmt = connection.prepareStatement(countQuery);
	         ResultSet rs = pstmt.executeQuery()) {
	        if (rs.next()) {
	            return rs.getInt(1); // Return the count of users
	        }
	    }
	    return 0; // Return 0 if there's an error or no users
	}
}


