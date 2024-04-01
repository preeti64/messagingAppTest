# Messaging app

## About 
This project aims to create a backend for a messaging application in Java, implementing various user stories as HTTP APIs following REST principles. Users can create accounts, send messages to each other, and view their received and sent messages. All messages are persisted in PostgreSQL, and sending messages puts them on a queue using a messaging system like Kafka.

## Features
### User Account Creation:

Non-users can create their accounts by providing a unique nickname.

### Sending Messages:

Users can send messages to other users identified by their IDs.
Users cannot send messages to themselves.

### Viewing Received Messages:
Users can view all messages they received.

### Viewing Sent Messages:
- Users can view all messages they sent
- Viewing Messages from a Specific User

### Endpoints
