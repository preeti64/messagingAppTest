    package com.example.messagingapp.controller;

    import com.example.messagingapp.model.Message;
    import com.example.messagingapp.model.User;
    import com.example.messagingapp.repository.MessageRepository;
    import com.example.messagingapp.repository.UserRepository;
    //import com.example.messagingapp.service.MessageService;
    import com.example.messagingapp.service.MessageServiceImpl;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/api/messages")
    public class MessageController {

        @Autowired
        private  MessageServiceImpl messageServiceImpl;

        @Autowired
        private UserRepository userRepository;

        @PostMapping
        public ResponseEntity<?> sendMessage(@RequestBody SendMessageRequest request) {
            try {
                User sender = userRepository.findById(request.getSenderId())
                        .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
                User receiver = userRepository.findById(request.getReceiverId())
                        .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

                Message message = messageServiceImpl.sendMessage(sender, receiver, request.getContent());

                return ResponseEntity.ok(message);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Not Found (404) status code if sender or receiver not found
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error"); // Internal Server Error (500) for other exceptions
            }
        }
    //
    @GetMapping("/received")
    public ResponseEntity<List<Message>> getReceivedMessages(@RequestParam Long userId) {
        User receiver = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<Message> receivedMessages = messageServiceImpl.getReceivedMessages(receiver);
        return ResponseEntity.ok(receivedMessages);
    }

        @GetMapping("/sent")
        public ResponseEntity<List<Message>> getSentMessages(@RequestParam Long userId) {
            User sender = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            List<Message> sentMessages = messageServiceImpl.getSentMessages(sender);
            return ResponseEntity.ok(sentMessages);
        }

        @GetMapping("/from")
        public ResponseEntity<List<Message>> getMessagesFromUser(@RequestParam Long senderId, @RequestParam Long receiverId) {
            User sender = userRepository.findById(senderId)
                    .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
            User receiver = userRepository.findById(receiverId)
                    .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));
            List<Message> messagesFromUser = messageServiceImpl.getMessagesFromUser(sender, receiver);
            return ResponseEntity.ok(messagesFromUser);
        }
        static class SendMessageRequest {
            private Long senderId;
            private Long receiverId;
            private String content;

            public Long getSenderId() {
                return senderId;
            }

            public void setSenderId(Long senderId) {
                this.senderId = senderId;
            }

            public Long getReceiverId() {
                return receiverId;
            }

            public void setReceiverId(Long receiverId) {
                this.receiverId = receiverId;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }

    }
