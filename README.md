QuickChat Application
Overview
QuickChat is a simple messaging application that allows users to:

Register & log in

Send, store, or disregard messages

View detailed message reports

All interaction is done through graphical dialogs using JOptionPane.

Features
1. User Registration and Login
Users must register with a valid username and a complex password.

Once registered, users can log in to access the application.

2. Send Messages
Messages are sent to recipient phone numbers (validated to South African format: +27XXXXXXXXX).

Each message includes a 10-digit Message ID, recipient number, and message content (maximum 250 characters).

A message hash is generated for each message.

3. Store Messages
Messages can be saved to send later.

Stored messages are written to a JSON file (stored_messages.json).

4. Disregard Messages
Users can disregard messages they do not wish to send.

These messages are tracked separately.

5. Message Reports
Users can view several types of reports:

Display all senders and recipients (only names and numbers shown)

Display the longest message

Search messages by Message ID

Search messages by Recipient

Delete messages by message hash

Display a full report of all messages sent

6. Exit Application
Users can cleanly exit the application from the menu.

Notes on Unit Tests
Unit tests are implemented to verify message storage, retrieval, search, and deletion.

Currently, 85% of the tests pass successfully.

One test fails consistently, and the cause is unknown to me which i sincerely apologise for, please provide feedback or assistance to clarify as to why.


Troubleshooting Build Issues in NetBeans
If the application fails to run due to a "Could not find or load main class" error or other build failures, try the following:

Right-click the project in the NetBeans Project Explorer.

Select Properties.

In the Run section on the left panel, locate the Main Class field.

Click Browse and choose:

Copy
Edit
com.mycompany.quickchat.QuickChat
Click OK to confirm.

Clean and build the project again, then run it.

This issue was encountered during development and was resolved by explicitly setting the main class.
