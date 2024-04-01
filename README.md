# Messaging app

## About 
This project aims to create a backend for a messaging application in Java, implementing various user stories as HTTP APIs following REST principles. Users can create accounts, send messages to each other, and view their received and sent messages. All messages are persisted in PostgreSQL, and sending messages puts them on a queue using a messaging system like Kafka.

## Features

### 1. User Account Creation:

Non-users can create their accounts by providing a unique nickname.

### 2. Sending Messages:

Users can send messages to other users identified by their IDs.
Users cannot send messages to themselves.

### 3. Viewing Received Messages:
Users can view all messages they received.

### 4. Viewing Sent Messages:
- Users can view all messages they sent
  
### 5. Viewing Messages from a Specific User

### Technologies Used
Java
Spring Boot
PostgreSQL
Kafka
Docker Container

### Installation
1. Clone the repository:
git clone https://github.com/preeti64/messagingAppTest

2. Navigate to the project directory:cd messagingAppTest

3. Build the project:mvn clean install

### Usage
1. Start the application

2. Access the API endpoints using a tool like Postman.

### API Endpoints
### User Endpoints

1. Create User
   
POST http://localhost:8080/api/users?nickName=userNickname
Create a new user account with a unique nickname.
Request Body: { "nickName": "userNickname" }

<img width="1309" alt="Screenshot 2024-04-01 at 7 57 34 PM" src="https://github.com/preeti64/messagingAppTest/assets/58847237/9d341266-9447-4867-b64d-c786a7120f3a">

POST http://localhost:8080/api/users?nickName=userNickname
Check if user is unique.

Request Body: { "nickName": "userNickname" }
<img width="1313" alt="Screenshot 2024-04-01 at 8 19 32 PM" src="https://github.com/preeti64/messagingAppTest/assets/58847237/3241b741-6716-44e9-9860-956fadee7ce4">

2. Send Message
   
POST http://localhost:8080/api/messages
Send a message from one user to another.
Request Body: { "senderId": 3, "receiverId": 38, "content": "Hi this is from user3 to user38" }

<img width="1012" alt="Screenshot 2024-04-01 at 8 26 27 PM" src="https://github.com/preeti64/messagingAppTest/assets/58847237/5644ffe3-a56a-4cd0-b01d-e1c02ef140a7">

Error while sending message to self.
Request Body: { "senderId": 3, "receiverId": 3, "content": "Hi this is from user3 to user3" }
<img width="1015" alt="Screenshot 2024-04-01 at 8 28 18 PM" src="https://github.com/preeti64/messagingAppTest/assets/58847237/e4076fdb-d73c-4b22-abeb-df4c67c7a3f8">

Error when sender does not exist.
Request Body: { "senderId": 300, "receiverId": 3, "content": "Hi this is from user300 to user3" }
<img width="1026" alt="Screenshot 2024-04-01 at 8 29 07 PM" src="https://github.com/preeti64/messagingAppTest/assets/58847237/4798867f-2835-462e-9949-8efb6f3a68e2">

Error when Receiver does not exist.
Request Body: { "senderId": 3, "receiverId": 390, "content": "Hi this is from user3 to user390" }
<img width="1015" alt="Screenshot 2024-04-01 at 8 30 15 PM" src="https://github.com/preeti64/messagingAppTest/assets/58847237/9e75e82e-2b45-48a5-a58e-2b48edc2c034">

3. Get Received Messages
   
GET http://localhost:8080/api/messages/received?userId={userId}
Retrieve all messages received by the specified user.

<img width="1012" alt="Screenshot 2024-04-01 at 8 33 20 PM" src="https://github.com/preeti64/messagingAppTest/assets/58847237/66c997ef-8233-44c4-8c79-c51caf26532b">

4. Get Sent Message
GET http://localhost:8080/api/messages/sent?userId={userId}
Retrieve all messages sent by the specified user.

<img width="1029" alt="Screenshot 2024-04-01 at 8 34 55 PM" src="https://github.com/preeti64/messagingAppTest/assets/58847237/ddc597a0-4c99-4f4b-a9fa-b863632b9bdc">

5. Get Messages From User

GET http://localhost:8080/api/messages/from?senderId={senderId}&receiverId={receiverId}
Retrieve all messages sent from one user to another.

<img width="1019" alt="Screenshot 2024-04-01 at 8 38 08 PM" src="https://github.com/preeti64/messagingAppTest/assets/58847237/94d9b43c-9619-4061-a6d8-82f36f34bb64">

### Architecture
The code follows a clear separation of concerns with distinct layers:

Controller: Handles incoming HTTP requests and delegates processing to services.
Service: Contains business logic and interacts with repositories.
Repository: Performs CRUD operations on the database.
DTO: Provides clear representation of message data.

### Kafka Integration
The application utilizes Kafka for asynchronous messaging by putting messages on a queue upon sending.

<img width="644" alt="Screenshot 2024-04-01 at 8 39 50 PM" src="https://github.com/preeti64/messagingAppTest/assets/58847237/4c5b850b-f1fa-4aaa-9723-f47fcd1a44f9">

### Things to improve and add

1. Validations:

Validate input data such as nicknames, message content, and user IDs. 
Implement server-side validation for input length, format, and content.

2. User Nickname Validation

3. Message Queue Error Handling:
Implement robust error handling and retry mechanisms for message queue operations to handle failures gracefully.

4. Message Status Tracking: Allow users to see the delivery and read status of their sent messages.

5. Enable users to filter and sort their messages based on various criteria such as date, sender, receiver, or message status.



