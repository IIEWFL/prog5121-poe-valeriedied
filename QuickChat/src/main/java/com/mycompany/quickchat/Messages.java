package com.mycompany.quickchat;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import javax.swing.JOptionPane;

public class Messages {

    private int totalMessages = 0;
    private ArrayList<String> sentMessages = new ArrayList<>();
    private ArrayList<String> disregardedMessages = new ArrayList<>();
    private ArrayList<JSONObject> storedMessages = new ArrayList<>();
    private ArrayList<String> messageIDs = new ArrayList<>();
    private ArrayList<String> messageHashes = new ArrayList<>();

    public boolean checkMessageID(String messageID) {
        return messageID.length() == 10;
    }

    public int checkRecipientCell(String cell) {
        return (cell.startsWith("+27") && cell.length() == 12) ? 1 : 0;
    }

    public String generateMessageID() {
        Random rand = new Random();
        long num = (long) (rand.nextDouble() * 1_000_000_0000L);
        return String.format("%010d", num);
    }

    public String createMessageHash(String messageID, int msgNum, String message) {
        String[] words = message.trim().split("\\s+");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];
        String hash = messageID.substring(0, 2) + ":" + msgNum + ":" + firstWord + lastWord;
        return hash.toUpperCase();
    }

    public String SentMessage() {
        String[] options = {"Send Message", "Disregard Message", "Store Message to send later"};
        int choice = JOptionPane.showOptionDialog(null, "Choose action for message:",
                "Message Action",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);
        return options[choice];
    }

    public void addMessage(String messageID, String recipient, String message, String hash) {
        totalMessages++;
        String formattedMessage = "Message ID: " + messageID + "\nRecipient: " + recipient +
                "\nMessage: " + message + "\nMessage Hash: " + hash;
        sentMessages.add(formattedMessage);
        messageIDs.add(messageID);
        messageHashes.add(hash);
    }

    public void addDisregardedMessage(String message) {
        disregardedMessages.add(message);
    }

    public String printMessages() {
        return String.join("\n\n", sentMessages);
    }

    public int returnTotalMessages() {
        return totalMessages;
    }

    @SuppressWarnings("unchecked")
    public void storedMessage(String messageID, String recipient, String message, String hash) {
        JSONObject msg = new JSONObject();
        msg.put("MessageID", messageID);
        msg.put("Recipient", recipient);
        msg.put("Message", message);
        msg.put("MessageHash", hash);

        try (FileWriter file = new FileWriter("stored_messages.json", true)) {
            file.write(msg.toJSONString() + "\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error storing message: " + e.getMessage());
        }
    }

    public void loadStoredMessages() {
        storedMessages.clear();
        JSONParser parser = new JSONParser();
        try (Scanner sc = new Scanner(new FileReader("stored_messages.json"))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                JSONObject obj = (JSONObject) parser.parse(line);
                storedMessages.add(obj);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading stored messages: " + e.getMessage());
        }
    }

    public String displaySenderAndRecipient() {
        StringBuilder result = new StringBuilder();
        for (String msg : sentMessages) {
            for (String line : msg.split("\n")) {
                if (line.startsWith("Recipient: ")) {
                    result.append("Sender: You\n");
                    result.append(line).append("\n\n");
                }
            }
        }
        return result.toString().isEmpty() ? "No messages sent yet." : result.toString();
    }

    public String displayLongestMessage() {
        String longestMessage = "";
        int maxLength = 0;

        for (String msg : sentMessages) {
            for (String line : msg.split("\n")) {
                if (line.startsWith("Message: ")) {
                    String content = line.substring(9);
                    if (content.length() > maxLength) {
                        longestMessage = msg;
                        maxLength = content.length();
                    }
                }
            }
        }

        return longestMessage.isEmpty() ? "No sent messages available." : longestMessage;
    }

    public String searchByMessageID(String messageID) {
        for (String msg : sentMessages) {
            if (msg.contains("Message ID: " + messageID)) {
                return msg;
            }
        }

        for (JSONObject obj : storedMessages) {
            if (messageID.equals(obj.get("MessageID"))) {
                return "Stored Message:\nRecipient: " + obj.get("Recipient") +
                        "\nMessage: " + obj.get("Message");
            }
        }

        return "No message found with ID: " + messageID;
    }

    public String searchByRecipient(String recipient) {
        ArrayList<String> matches = new ArrayList<>();

        for (String msg : sentMessages) {
            if (msg.contains("Recipient: " + recipient)) {
                matches.add(msg);
            }
        }

        for (JSONObject obj : storedMessages) {
            if (recipient.equals(obj.get("Recipient"))) {
                matches.add("Stored Message:\nRecipient: " + recipient +
                        "\nMessage: " + obj.get("Message"));
            }
        }

        return matches.isEmpty() ? "No messages found for recipient." : String.join("\n\n", matches);
    }

    public String deleteMessageByHash(String hash) {
        for (int i = 0; i < sentMessages.size(); i++) {
            String msg = sentMessages.get(i);
            if (msg.contains("Message Hash: " + hash)) {
                sentMessages.remove(i);
                if (i < messageHashes.size()) messageHashes.remove(i);
                if (i < messageIDs.size()) messageIDs.remove(i);
                totalMessages--;
                return "Message with hash " + hash + " deleted.";
            }
        }

        Iterator<JSONObject> iter = storedMessages.iterator();
        while (iter.hasNext()) {
            JSONObject obj = iter.next();
            if (obj.get("MessageHash").equals(hash)) {
                iter.remove();
                return "Stored message with hash " + hash + " deleted.";
            }
        }

        return "Message hash not found.";
    }

    public String fullReport() {
        if (sentMessages.isEmpty()) {
            return "No sent messages available.";
        }

        StringBuilder sb = new StringBuilder("FULL MESSAGE REPORT\n");
        sb.append("====================\n");
        for (String msg : sentMessages) {
            sb.append(msg).append("\n\n");
        }
        return sb.toString();
    }

    // Getters
    public ArrayList<String> getMessageIDs() {
        return messageIDs;
    }

    public ArrayList<String> getMessageHashes() {
        return messageHashes;
    }

    public ArrayList<String> getSentMessages() {
        return sentMessages;
    }

    public ArrayList<String> getDisregardedMessages() {
        return disregardedMessages;
    }

    public ArrayList<JSONObject> getStoredMessages() {
        return storedMessages;
    }

    public ArrayList<String> getRecipients() {
        ArrayList<String> recipients = new ArrayList<>();
        for (String msg : sentMessages) {
            for (String line : msg.split("\n")) {
                if (line.startsWith("Recipient: ")) {
                    String recipient = line.substring(11);
                    if (!recipients.contains(recipient)) {
                        recipients.add(recipient);
                    }
                }
            }
        }
        return recipients;
    }
}
