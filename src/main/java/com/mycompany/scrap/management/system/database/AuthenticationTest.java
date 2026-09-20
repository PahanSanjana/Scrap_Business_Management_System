package com.mycompany.scrap.management.system.database;

import com.mycompany.scrap.management.system.model.User;
import com.mycompany.scrap.management.system.security.AuthenticationService;

public class AuthenticationTest {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("       AUTHENTICATION SERVICE TEST");
        System.out.println("========================================");

        AuthenticationService authenticationService =
                new AuthenticationService();

        String username = "test_admin_01";
        String correctPassword = "Test@12345";
        String incorrectPassword = "WrongPassword123";

        System.out.println("\nTest 1: Correct username and password");

        User authenticatedUser = authenticationService.authenticate(
                username,
                correctPassword
        );

        if (authenticatedUser != null) {

            System.out.println(
                    "Correct login test: PASSED"
            );

            System.out.println(
                    "Authenticated user: "
                            + authenticatedUser.getUsername()
            );

        } else {

            System.out.println(
                    "Correct login test: FAILED"
            );
        }

        System.out.println("\nTest 2: Incorrect password");

        User incorrectPasswordUser =
                authenticationService.authenticate(
                        username,
                        incorrectPassword
                );

        if (incorrectPasswordUser == null) {

            System.out.println(
                    "Incorrect password test: PASSED"
            );

        } else {

            System.out.println(
                    "Incorrect password test: FAILED"
            );
        }

        System.out.println("\nTest 3: Incorrect username");

        User incorrectUsernameUser =
                authenticationService.authenticate(
                        "unknown_user_999",
                        correctPassword
                );

        if (incorrectUsernameUser == null) {

            System.out.println(
                    "Incorrect username test: PASSED"
            );

        } else {

            System.out.println(
                    "Incorrect username test: FAILED"
            );
        }

        System.out.println("\nTest 4: Empty username");

        User emptyUsernameUser =
                authenticationService.authenticate(
                        "",
                        correctPassword
                );

        if (emptyUsernameUser == null) {

            System.out.println(
                    "Empty username test: PASSED"
            );

        } else {

            System.out.println(
                    "Empty username test: FAILED"
            );
        }

        System.out.println("\nTest 5: Empty password");

        User emptyPasswordUser =
                authenticationService.authenticate(
                        username,
                        ""
                );

        if (emptyPasswordUser == null) {

            System.out.println(
                    "Empty password test: PASSED"
            );

        } else {

            System.out.println(
                    "Empty password test: FAILED"
            );
        }

        System.out.println("\n========================================");
        System.out.println("       AUTHENTICATION TEST COMPLETED");
        System.out.println("========================================");
    }
}