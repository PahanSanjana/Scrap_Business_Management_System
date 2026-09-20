package com.mycompany.scrap.management.system.security;

import com.mycompany.scrap.management.system.dao.UserDAO;
import com.mycompany.scrap.management.system.model.User;

public class AuthenticationService {

    private final UserDAO userDAO;

    /**
     * Constructor
     */
    public AuthenticationService() {

        this.userDAO = new UserDAO();
    }

    /**
     * Authenticate a user using their username and password.
     *
     * @param username Entered username
     * @param password Entered plain-text password
     * @return User object if authentication succeeds, otherwise null
     */
    public User authenticate(String username, String password) {

        // Validate empty or missing username
        if (username == null || username.isBlank()) {

            System.out.println("Login failed: Username is required.");

            return null;
        }

        // Validate empty or missing password
        if (password == null || password.isBlank()) {

            System.out.println("Login failed: Password is required.");

            return null;
        }

        // Find the user in the database
        User user = userDAO.findByUsername(username);

        // Check whether the username exists
        if (user == null) {

            System.out.println("Login failed: Invalid username or password.");

            return null;
        }

        // Check whether the account is active
        if (!user.isActive()) {

            System.out.println("Login failed: User account is inactive.");

            return null;
        }

        // Get the stored password hash
        String storedPasswordHash = user.getPassword();

        // Check whether a password hash exists
        if (storedPasswordHash == null
                || storedPasswordHash.isBlank()) {

            System.out.println("Login failed: Password hash is missing.");

            return null;
        }

        // Verify the entered password against the stored hash
        boolean passwordMatches = PasswordUtil.verifyPassword(
                password,
                storedPasswordHash
        );

        // Reject incorrect passwords
        if (!passwordMatches) {

            System.out.println("Login failed: Invalid username or password.");

            return null;
        }

        // Login successful
        System.out.println(
                "Login successful for user: "
                        + user.getUsername()
        );

        return user;
    }
}