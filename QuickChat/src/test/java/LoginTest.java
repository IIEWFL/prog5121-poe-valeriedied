package com.mycompany.quickchat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    private login loginSystem;

    @BeforeEach
    public void setUp() {
        loginSystem = new login();
    }

    @Test
    public void testValidUsernameFormat() {
        assertTrue(loginSystem.checkUserName("kyl_1"));
    }

    @Test
    public void testInvalidUsernameFormat_NoUnderscore() {
        assertFalse(loginSystem.checkUserName("kyl1"));
    }

    @Test
    public void testInvalidUsernameFormat_TooLong() {
        assertFalse(loginSystem.checkUserName("kyle_123"));
    }

    @Test
    public void testPasswordMeetsComplexityRequirements() {
        assertTrue(loginSystem.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    public void testPasswordFailsComplexityRequirements() {
        assertFalse(loginSystem.checkPasswordComplexity("password"));
    }

    @Test
    public void testLoginSuccessful() {
        loginSystem.user = "kyl_1";
        loginSystem.password = "Ch&&sec@ke99!";
        assertTrue(loginSystem.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void testLoginFailed() {
        loginSystem.user = "kyl_1";
        loginSystem.password = "Ch&&sec@ke99!";
        assertFalse(loginSystem.loginUser("kyl_1", "WrongPass123!"));
    }

    @Test
    public void testReturnLoginStatusSuccess() {
        loginSystem.user = "kyl_1";
        loginSystem.password = "Ch&&sec@ke99!";
        loginSystem.firstName = "Kyle";
        loginSystem.lastName = "Smith";

        String expected = "Welcome Kyle Smith, it is great to see you again.";
        String actual = loginSystem.returnLoginStatus("kyl_1", "Ch&&sec@ke99!", "Kyle", "Smith");

        assertEquals(expected, actual);
    }

    @Test
    public void testReturnLoginStatusFailure() {
        loginSystem.user = "kyl_1";
        loginSystem.password = "Ch&&sec@ke99!";
        loginSystem.firstName = "Kyle";
        loginSystem.lastName = "Smith";

        String expected = "Username or password is incorrect, please try again.";
        String actual = loginSystem.returnLoginStatus("wrong", "password", "Kyle", "Smith");

        assertEquals(expected, actual);
    }

    // Cell number format tests
    @Test
    public void testValidCellNumberFormat() {
        String cell = "+27838968976";
        boolean isValid = cell.startsWith("+27") && cell.length() == 12;
        assertTrue(isValid);
    }

    @Test
    public void testInvalidCellNumberFormat() {
        String cell = "08966553";
        boolean isValid = cell.startsWith("+27") && cell.length() == 12;
        assertFalse(isValid);
    }
}