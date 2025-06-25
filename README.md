QuickChat Application
Overview
QuickChat is a simple messaging application that allows users to register, log in, send messages, store messages for later, disregard messages, and view detailed message reports. The application uses a graphical interface for input and output dialogs via JOptionPane.

Features
User Registration and Login:
Users must register with a valid username and password, then log in to access messaging features.

Send Messages:
Users can send messages to recipients. Each message includes a generated message ID, recipient phone number (validated to South African format), and message content (up to 250 characters).

Store Messages:
Messages can be stored for sending later. Stored messages are saved in a JSON file.

Disregard Messages:
Users may choose to disregard messages, which are tracked separately.

Message Reports:
Users can view several reports, including:

Display all senders and recipients

Display the longest message (from sent messages)

Search messages by Message ID or Recipient

Delete messages by message hash

Display a full message report

Exit Application:
Users can quit the application cleanly through the menu.




Notes on Unit Tests
Unit tests have been created to verify key functionality such as message storage, retrieval, searching, and deletion.

Currently, 85% of the unit tests pass successfully.

There is one test that fails consistently, and I have not yet been able to identify the root cause.

I apologize for this incomplete test coverage and will continue working on fixing this issue in future updates.
