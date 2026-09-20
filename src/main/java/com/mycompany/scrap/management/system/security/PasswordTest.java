package com.mycompany.scrap.management.system.security;

public class PasswordTest {

    public static void main(String[] args) {

        String originalPassword = "Admin@12345";

        // Generate a secure password hash
        String hashedPassword =
                PasswordUtil.hashPassword(originalPassword);

        System.out.println("Password hash generated successfully!");

        // Do not display the real hash in production
        System.out.println(
                "Hash generated: "
                + !hashedPassword.isBlank()
        );

        // Test the correct password
        boolean correctPassword =
                PasswordUtil.verifyPassword(
                        originalPassword,
                        hashedPassword
                );

        System.out.println(
                "Correct password verification: "
                + correctPassword
        );

        // Test an incorrect password
        boolean incorrectPassword =
                PasswordUtil.verifyPassword(
                        "WrongPassword123",
                        hashedPassword
                );

        System.out.println(
                "Incorrect password verification: "
                + incorrectPassword
        );

        // Display final test result
        if (correctPassword && !incorrectPassword) {

            System.out.println(
                    "Password security test passed successfully!"
            );

        } else {

            System.out.println(
                    "Password security test failed!"
            );
        }
    }
}