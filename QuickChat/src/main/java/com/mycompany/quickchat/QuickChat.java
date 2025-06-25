package com.mycompany.quickchat;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;
import org.json.simple.JSONObject;

public class QuickChat {

    public static Messages task1 = new Messages();

    public static void main(String[] args) {

        login user1 = new login();
        String user = "", password = "", firstName = "", lastName = "";

        // Registration
        while (!user1.checkUserName(user) || !user1.checkPasswordComplexity(password)) {
            JOptionPane.showMessageDialog(null, "Please fill in the following inputs to Register: ");
            user = JOptionPane.showInputDialog("Enter the username:");
            password = JOptionPane.showInputDialog("Enter the password:");
            firstName = JOptionPane.showInputDialog("Enter your First Name:");
            lastName = JOptionPane.showInputDialog("Enter your Last Name:");
            user1.registerUser(firstName, lastName, user, password);
        }

        // Login
        boolean checkLoginDetails = false;
        while (!checkLoginDetails) {
            JOptionPane.showMessageDialog(null, "Please fill in the following inputs to Login: ");
            user = JOptionPane.showInputDialog("Enter the username:");
            password = JOptionPane.showInputDialog("Enter the password:");

            if (user1.loginUser(user, password)) {
                checkLoginDetails = true;
                JOptionPane.showMessageDialog(null, user1.returnLoginStatus(user, password, firstName, lastName));
                JOptionPane.showMessageDialog(null, "Welcome to QuickChat");

                int taskOptions = 0;
                while (true) {
                    taskOptions = Integer.parseInt(JOptionPane.showInputDialog(
                        "Task Options:\n" +
                        "1) Send Messages\n" +
                        "2) View Message Reports\n" +
                        "3) Quit"
                    ));

                    switch (taskOptions) {
                        case 1:
                            int numberOfMessages = Integer.parseInt(JOptionPane.showInputDialog("Enter number of Messages to add: "));
                            for (int i = 0; i < numberOfMessages; i++) {
                                String messageID = task1.generateMessageID();
                                while (!task1.checkMessageID(messageID)) {
                                    messageID = task1.generateMessageID();
                                }

                                int currentMsgCount = task1.returnTotalMessages();

                                String recipient;
                                do {
                                    recipient = JOptionPane.showInputDialog("Enter recipient cell number (must start with +27 and be 9 digits after +27):");
                                } while (task1.checkRecipientCell(recipient) != 1);

                                String messageContent = JOptionPane.showInputDialog("Enter your message (Max 250 characters):");
                                if (messageContent.length() > 250) {
                                    JOptionPane.showMessageDialog(null, "Please enter a message of less than 250 characters.");
                                    i--;
                                    continue;
                                }

                                String messageHash = task1.createMessageHash(messageID, currentMsgCount, messageContent);
                                String userChoice = task1.SentMessage();

                                if (userChoice.equals("Store Message to send later")) {
                                    task1.storedMessage(messageID, recipient, messageContent, messageHash);
                                }

                                if (userChoice.equals("Disregard Message")) {
                                    task1.addDisregardedMessage("Disregarded: " + messageContent);
                                } else {
                                    task1.addMessage(messageID, recipient, messageContent, messageHash);
                                }

                                JOptionPane.showMessageDialog(null,
                                    "Message Details:\n" +
                                    "Message ID: " + messageID + "\n" +
                                    "Message Hash: " + messageHash + "\n" +
                                    "Recipient: " + recipient + "\n" +
                                    "Message: " + messageContent
                                );
                            }

                            JOptionPane.showMessageDialog(null, "Total Messages Sent: " + task1.returnTotalMessages());
                            break;

                        case 2:
                            String menuOptions = "Choose Report Option:\n"
                                    + "1. Show Sender and Recipients\n"
                                    + "2. Show Longest Message\n"
                                    + "3. Search by Message ID\n"
                                    + "4. Search by Recipient\n"
                                    + "5. Delete by Message Hash\n"
                                    + "6. Display Full Report\n"
                                    + "7. Process Stored Messages";

                            int reportOption = Integer.parseInt(JOptionPane.showInputDialog(menuOptions));

                            switch (reportOption) {
                                case 1:
                                    JOptionPane.showMessageDialog(null, task1.displaySenderAndRecipient());
                                    break;
                                case 2:
                                    JOptionPane.showMessageDialog(null, task1.displayLongestMessage());
                                    break;
                                case 3:
                                    if (task1.getMessageIDs().isEmpty()) {
                                        JOptionPane.showMessageDialog(null, "No message IDs available.");
                                        break;
                                    }
                                    String idChoice = (String) JOptionPane.showInputDialog(null,
                                            "Select a Message ID:",
                                            "Search by Message ID",
                                            JOptionPane.QUESTION_MESSAGE,
                                            null,
                                            task1.getMessageIDs().toArray(),
                                            task1.getMessageIDs().get(0));
                                    JOptionPane.showMessageDialog(null, task1.searchByMessageID(idChoice));
                                    break;
                                case 4:
                                    if (task1.returnTotalMessages() == 0) {
                                        JOptionPane.showMessageDialog(null, "No messages sent yet.");
                                        break;
                                    }
                                    String[] allRecipients = task1.getRecipients().toArray(new String[0]);
                                    String selectedRecipient = (String) JOptionPane.showInputDialog(null,
                                            "Select a recipient:",
                                            "Search by Recipient",
                                            JOptionPane.QUESTION_MESSAGE,
                                            null,
                                            allRecipients,
                                            allRecipients[0]);
                                    JOptionPane.showMessageDialog(null, task1.searchByRecipient(selectedRecipient));
                                    break;
                                case 5:
                                    if (task1.getMessageHashes().isEmpty()) {
                                        JOptionPane.showMessageDialog(null, "No message hashes available.");
                                        break;
                                    }
                                    String selectedHash = (String) JOptionPane.showInputDialog(null,
                                            "Select a message hash to delete:",
                                            "Delete by Message Hash",
                                            JOptionPane.QUESTION_MESSAGE,
                                            null,
                                            task1.getMessageHashes().toArray(),
                                            task1.getMessageHashes().get(0));
                                    JOptionPane.showMessageDialog(null, task1.deleteMessageByHash(selectedHash));
                                    break;
                                case 6:
                                    JOptionPane.showMessageDialog(null, task1.fullReport());
                                    break;
                                case 7:
                                    task1.loadStoredMessages();
                                    ArrayList<JSONObject> storedMsgs = task1.getStoredMessages();
                                    if (storedMsgs.isEmpty()) {
                                        JOptionPane.showMessageDialog(null, "No stored messages found.");
                                    } else {
                                        ArrayList<String> recipients = storedMsgs.stream()
                                            .map(obj -> (String) obj.get("Recipient"))
                                            .distinct()
                                            .collect(Collectors.toCollection(ArrayList::new));

                                        String chosenRecipient = (String) JOptionPane.showInputDialog(null,
                                                "Select a recipient to view their stored messages:",
                                                "Stored Recipients",
                                                JOptionPane.QUESTION_MESSAGE,
                                                null,
                                                recipients.toArray(),
                                                recipients.get(0));

                                        Iterator<JSONObject> iterator = storedMsgs.iterator();
                                        while (iterator.hasNext()) {
                                            JSONObject obj = iterator.next();
                                            if (chosenRecipient.equals(obj.get("Recipient"))) {
                                                String id = (String) obj.get("MessageID");
                                                String to = (String) obj.get("Recipient");
                                                String text = (String) obj.get("Message");
                                                String hash = (String) obj.get("MessageHash");

                                                String[] options = {"Send Now", "Leave as Stored"};
                                                int decision = JOptionPane.showOptionDialog(null,
                                                        "Stored Message:\n" +
                                                        "ID: " + id + "\nRecipient: " + to + "\nMessage: " + text,
                                                        "Process Stored Message",
                                                        JOptionPane.DEFAULT_OPTION,
                                                        JOptionPane.INFORMATION_MESSAGE,
                                                        null, options, options[0]);

                                                if (decision == 0) {
                                                    task1.addMessage(id, to, text, hash);
                                                    iterator.remove();
                                                }
                                            }
                                        }
                                    }
                                    break;
                                default:
                                    JOptionPane.showMessageDialog(null, "Invalid option.");
                            }
                            break;
                        case 3:
                            JOptionPane.showMessageDialog(null, "Quitting QuickChat. Goodbye!");
                            System.exit(0);
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "Invalid Option");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, user1.returnLoginStatus(user, password, firstName, lastName));
            }
        }
    }
}
