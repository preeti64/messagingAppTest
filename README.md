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

### Screenshots
**Send a message**
<img width="1185" alt="Screenshot 2024-04-01 at 7 45 46 PM" src="https://github.com/preeti64/messagingAppTest/assets/58847237/2506d734-1b20-4c13-9855-803b4cdf8e41">
<img width="1242" alt="image" src="https://github.com/preeti64/messagingAppTest/assets/58847237/30aa386c-48b5-46e4-98c9-35283022117c">

