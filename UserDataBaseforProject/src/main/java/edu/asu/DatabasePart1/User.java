package edu.asu.DatabasePart1;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String preferredName;
    private String role;
    private boolean oneTimePasswordFlag;  // Flag for OTP
    private LocalDateTime passwordExpiration;  // Password expiration time
    private boolean accountSetupComplete;  // Flag for whether account setup is complete
    private List<Role> roles;  // List of roles assigned to the user
    private boolean isAdmin;
    
    // Constructor for creating the first admin
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.roles = new ArrayList<>();
        this.roles.add(Role.ADMIN);  // The first user is an Admin
        this.isAdmin = true;
        this.oneTimePasswordFlag = true;  // OTP required initially
        this.accountSetupComplete = false;  // Account setup is incomplete initially
        this.passwordExpiration = LocalDateTime.now().plusDays(30);  // Default password expiration in 30 days
    }

    // Constructor for users invited with a one-time code
    public User(String username, String password, List<Role> roles) {
        this.username = username;
        this.password = password;
        this.roles = new ArrayList<>(roles);
        this.oneTimePasswordFlag = true;  // OTP required initially
        this.accountSetupComplete = false;  // Account setup is incomplete initially
        this.passwordExpiration = LocalDateTime.now().plusDays(30);  // Default password expiration in 30 days
        this.isAdmin = false;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
        this.oneTimePasswordFlag = false;  // Disable OTP after password is set
        this.passwordExpiration = LocalDateTime.now().plusDays(30);  // Reset expiration date
    }

    public boolean isPasswordExpired() {
        return LocalDateTime.now().isAfter(passwordExpiration);
    }

    public boolean isOneTimePasswordFlag() {
        return oneTimePasswordFlag;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void completeAccountSetup(String email, String firstName, String middleName, String lastName, String preferredName) {
        this.email = email;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.preferredName = preferredName;
        this.accountSetupComplete = true;
    }
    
    public void userData(String username, String email, String firstName, String middleName, String lastName, String preferredName, String role)
    {
    	this.email = email;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.preferredName = preferredName;
        this.role = role;
    }

    public boolean isAccountSetupComplete() {
        return accountSetupComplete;
    }

    public String getFullName() {
        if (preferredName != null && !preferredName.isEmpty()) {
            return preferredName + " " + lastName;
        }
        return firstName + " " + lastName;
    }

    public void resetPassword(String newPassword) {
        this.password = newPassword;
        this.oneTimePasswordFlag = true;
        this.passwordExpiration = LocalDateTime.now().plusDays(1);  // Temporary password expires in 1 day
    }

    // Function to check if a user has multiple roles
    public boolean hasMultipleRoles() {
        return roles.size() > 1;
    }

    // Function to list roles as string for displaying purposes
    public String getRolesAsString() {
        StringBuilder rolesString = new StringBuilder();
        for (Role role : roles) {
            rolesString.append(role.toString()).append(", ");
        }
        return rolesString.length() > 0 ? rolesString.substring(0, rolesString.length() - 2) : "";
    }

    public void logout() {
        System.out.println(username + " has logged out.");
    }
    public enum Role {
        ADMIN,
        STUDENT,
        INSTRUCTOR
    }
}