package com.mycompany.quickchat;

import static org.junit.jupiter.api.Assertions.*;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MessageTest {

    Messages messages;

    @BeforeEach
    void setUp() {
        messages = new Messages();

        // Test Data Message 1 (Sent)
        String msgID1 = "0000000001";
        String recipient1 = "+27834557896";
        String content1 = "Did you get the cake?";
        String hash1 = messages.createMessageHash(msgID1, 0, content1);
        messages.addMessage(msgID1, recipient1, content1, hash1);

        // Test Data Message 2 (Stored)
        String msgID2 = "0000000002";
        String recipient2 = "+27838884567";
        String content2 = "Where are you? You are late! I have asked you to be on time.";
        String hash2 = messages.createMessageHash(msgID2, 1, content2);
        messages.storedMessage(msgID2, recipient2, content2, hash2);

        // Add stored message also to in-memory storedMessages list for testing
        JSONObject storedMsg2 = new JSONObject();
        storedMsg2.put("MessageID", msgID2);
        storedMsg2.put("Recipient", recipient2);
        storedMsg2.put("Message", content2);
        storedMsg2.put("MessageHash", hash2);
        messages.getStoredMessages().add(storedMsg2);

        // Test Data Message 3 (Disregarded)
        String content3 = "Yohoooo, I am at your gate.";
        messages.addDisregardedMessage(content3);

        // Test Data Message 4 (Sent)
        String msgID4 = "0838884567";  // developer ID
        String recipient4 = "0838884567"; // same as developer ID
        String content4 = "It is dinner time!";
        String hash4 = messages.createMessageHash(msgID4, 2, content4);
        messages.addMessage(msgID4, recipient4, content4, hash4);

        // Test Data Message 5 (Stored)
        String msgID5 = "0000000005";
        String recipient5 = "+27838884567";
        String content5 = "Ok, I am leaving without you.";
        String hash5 = messages.createMessageHash(msgID5, 3, content5);
        messages.storedMessage(msgID5, recipient5, content5, hash5);

        // Add stored message 5 also to in-memory storedMessages list
        JSONObject storedMsg5 = new JSONObject();
        storedMsg5.put("MessageID", msgID5);
        storedMsg5.put("Recipient", recipient5);
        storedMsg5.put("Message", content5);
        storedMsg5.put("MessageHash", hash5);
        messages.getStoredMessages().add(storedMsg5);
    }

    @Test
    void testSentMessagesArrayPopulatedCorrectly() {
        assertTrue(messages.getSentMessages().stream().anyMatch(m -> m.contains("Did you get the cake?")));
        assertTrue(messages.getSentMessages().stream().anyMatch(m -> m.contains("It is dinner time!")));
        assertTrue(messages.getDisregardedMessages().contains("Yohoooo, I am at your gate."));
    }

    @Test
    void testDisplayLongestMessage() {
        String longest = messages.displayLongestMessage();
        // Longest message is stored message 2
        assertTrue(longest.contains("Where are you? You are late! I have asked you to be on time."));
    }

    @Test
    void testSearchByMessageID() {
        String result = messages.searchByMessageID("0838884567");
        assertTrue(result.contains("It is dinner time!"));
    }

    @Test
    void testSearchByRecipient() {
        String recipient = "+27838884567";
        String result = messages.searchByRecipient(recipient);
        assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(result.contains("Ok, I am leaving without you."));
    }

    @Test
    void testDeleteMessageByHash() {
        String hashToDelete = null;
        for (String hash : messages.getMessageHashes()) {
            String msgID = messages.getMessageIDs().get(messages.getMessageHashes().indexOf(hash));
            String content = messages.searchByMessageID(msgID);
            if (content.contains("Did you get the cake?")) {
                hashToDelete = hash;
                break;
            }
        }
        assertNotNull(hashToDelete);

        String deleteResult = messages.deleteMessageByHash(hashToDelete);
        assertEquals("Message with hash " + hashToDelete + " deleted.", deleteResult);

        // Confirm message is no longer found
        String afterDeleteSearch = messages.searchByMessageID(messages.getMessageIDs().get(messages.getMessageHashes().indexOf(hashToDelete)));
        assertTrue(afterDeleteSearch.contains("No message found"));
    }

    @Test
    void testFullReportContainsAllSentMessages() {
        String report = messages.fullReport();

        assertTrue(report.contains("Did you get the cake?"));
        assertTrue(report.contains("It is dinner time!"));

        // Disregarded and stored messages should not be in sent messages report
        assertFalse(report.contains("Yohoooo, I am at your gate."));
        assertFalse(report.contains("Where are you? You are late!"));
    }
}
