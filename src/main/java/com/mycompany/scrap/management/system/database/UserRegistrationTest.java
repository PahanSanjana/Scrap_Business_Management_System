package com.mycompany.scrap.management.system.database;

import com.mycompany.scrap.management.system.dao.UserDAO;
import com.mycompany.scrap.management.system.model.User;
import com.mycompany.scrap.management.system.security.PasswordUtil;

public class UserRegistrationTest {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("     USER REGISTRATION SECURITY TEST");
        System.out.println("========================================");

        // Create the UserDAO object
        UserDAO userDAO = new UserDAO();

        // Test user details
        String username = "test_admin_01";
        String originalPassword = "Test@12345";

        User newUser = new User(
                1,
                username,
                originalPassword,
                "Test Administrator",
                "",
                ""
        );

        System.out.println("\nStep 1: Creating a new user...");

        // Check whether the user already exists
        User existingUser = userDAO.findByUsername(username);

        if (existingUser != null) {

            System.out.println("Test user already exists.");
            System.out.println("Skipping user insertion.");

        } else {

            // Create the new user
            boolean userCreated = userDAO.createUser(newUser);

            if (userCreated) {

                System.out.println("User created successfully!");

            } else {

                System.out.println("User creation failed.");
                return;
            }
        }

        System.out.println("\nStep 2: Finding the user from the database...");

        // Retrieve the user from the database
        User savedUser = userDAO.findByUsername(username);

        if (savedUser == null) {

            System.out.println("User could not be found.");
            return;
        }

        System.out.println("User found successfully!");
        System.out.println("Username: " + savedUser.getUsername());
        System.out.println("Full name: " + savedUser.getFullName());
        System.out.println("Role ID: " + savedUser.getRoleId());

        System.out.println("\nStep 3: Checking the stored password...");

        String storedPasswordHash = savedUser.getPassword();

        if (storedPasswordHash == null
                || storedPasswordHash.isBlank()) {

            System.out.println("Password hash is empty.");
            return;
        }

        // Check whether the original password was stored directly
        if (storedPasswordHash.equals(originalPassword)) {

            System.out.println(
                    "SECURITY TEST FAILED: Plain-text password detected!"
            );

            return;

        } else {

            System.out.println(
                    "The original plain-text password was not stored."
            );
        }

        System.out.println("\nStep 4: Verifying the password hash...");

        // Verify the original password against the stored hash
        boolean passwordVerified = PasswordUtil.verifyPassword(
                originalPassword,
                storedPasswordHash
        );

        if (passwordVerified) {

            System.out.println(
                    "Correct password verification: true"
            );

        } else {

            System.out.println(
                    "Correct password verification: false"
            );

            System.out.println(
                    "SECURITY TEST FAILED: Password verification failed."
            );

            return;
        }

        System.out.println("\nStep 5: Testing an incorrect password...");

        boolean incorrectPasswordVerified = PasswordUtil.verifyPassword(
                "WrongPassword123",
                storedPasswordHash
        );

        System.out.println(
                "Incorrect password verification: "
                        + incorrectPasswordVerified
        );

        if (incorrectPasswordVerified) {

            System.out.println(
                    "SECURITY TEST FAILED: Incorrect password accepted!"
            );

            return;
        }

        System.out.println("\n========================================");
        System.out.println("       USER REGISTRATION TEST PASSED");
        System.out.println("========================================");
    }
}