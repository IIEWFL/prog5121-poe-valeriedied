/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quickchat;


import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

public class login {
    // Member variables to store user information
    String user;
    String password;
    String firstName;
    String lastName;

    // Method to check if the user name follows the required format
    boolean checkUserName(String user) {
        return !(user.length() > 5 || user.indexOf('_') == -1);
    }
// Method to check the complexity of a password using regex
    boolean checkPasswordComplexity(String password) {
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[!@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        return m.matches();
    }
// Method to check if the provided user and password match
    boolean loginUser(String user, String password) {
        return user.equals(this.user) && password.equals(this.password);
    }
 // Method to generate a login status message
    String returnLoginStatus(String user, String password, String firstName1, String lastName1) {
        if (loginUser(user, password)) {
            return "Welcome " + firstName + " " + lastName + ", it is great to see you again.";
        } else {
            return "Username or password is incorrect, please try again.";
        }
    }

    // Method to register a new user
    public void registerUser(String firstName, String lastName, String user, String password) {
// Check and set user name
        if (checkUserName(user)) {
            this.user = user;
            JOptionPane.showMessageDialog(null, "Username successfully captured");
        } else {
            JOptionPane.showMessageDialog(null, "Username is not correctly formatted. Please ensure that your username contains an underscore and is no more than 5 characters long.");
        }
// Check and set password
        if (checkPasswordComplexity(password)) {
            this.password = password;
            JOptionPane.showMessageDialog(null, "Password successfully captured");
        } else {
            JOptionPane.showMessageDialog(null, "Password is not correctly formatted. Please ensure that the password contains at least 8 characters, a capital letter, a number, and a special character.");
        }
// If both user name and password are valid, set first name, last name, and display login status
        if (checkUserName(user) && checkPasswordComplexity(password)) {
            this.firstName = firstName;
            this.lastName = lastName;
            JOptionPane.showMessageDialog(null, returnLoginStatus(user, password, firstName, lastName));
        }
    }


 
   
}
