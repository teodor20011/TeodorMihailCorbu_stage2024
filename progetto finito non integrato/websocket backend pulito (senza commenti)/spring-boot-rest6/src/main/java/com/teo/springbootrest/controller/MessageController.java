package com.teo.springbootrest.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.teo.springbootrest.model.ChatNotification;
import com.teo.springbootrest.model.ChatRoom;
import com.teo.springbootrest.model.Message;
import com.teo.springbootrest.model.User;
import com.teo.springbootrest.repo.MessageRepository;
import com.teo.springbootrest.repo.UserRepo;
import com.teo.springbootrest.service.ChatRoomService;
import com.teo.springbootrest.service.MessageService;
import com.teo.springbootrest.service.UserService;
import jakarta.persistence.Entity;
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageController {

    @Autowired		//per usare un metodo definito nel repo
    private MessageRepository messageRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private UserService userService;


    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("messages")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/message/{messageId}")
    public Message getMessage(@PathVariable String messageId) {
        return messageService.getMessage(messageId);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("between2usersMessages/{senderId}/{receiverId}")
    public List<Message> getAllUsersMessages(@PathVariable String senderId, @PathVariable String receiverId) {

        User sender = userService.getUser(senderId);
        User receiver = userService.getUser(receiverId);
        return messageService.getAllUsersMessages(sender, receiver);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/messagee")
    public ResponseEntity<Message> sendMessage(@RequestBody SendMessageRequest request) {
        ChatRoom chatRoom = chatRoomService.getChatRoom(request.getChatRoomId());
        Message message = messageService.createMessage(request.getText(), chatRoom, request.getSender(), request.getReceiver());
        return ResponseEntity.ok(message);
    }

    @Transactional
    @MessageMapping("/between2usersMessages")
    public List<Message> getAllUsersMessages(@Payload JsonNode message) {
        System.out.println("Chiamata between2usersMessages andata a buon fine");

        User sender = parseUserFromJson(message.get("sender"));
        User receiver = parseUserFromJson(message.get("receiver"));
        System.out.println("Sender:" + sender);
        System.out.println("Receiver:" + receiver);

        List<Message> messages = messageService.getAllUsersMessages(sender, receiver);
        System.out.println("Messages fetched:" + messages);

        String senderName = message.get("sender").get("nickname").asText();
        messagingTemplate.convertAndSendToUser(senderName, "/queue/messages", messages);
        System.out.println("Messages sent to user:" + senderName);
        System.out.println("Messages sent to user:"+messages);
        return messages;

    }

    // Metodo per convertire il JSON in un oggetto User
    private User parseUserFromJson(JsonNode userNode) {
        User user = new User();
        user.setNickname(userNode.get("nickname").asText());
        user.setEmail(userNode.get("email").asText());
        user.setPassword(userNode.get("password").asText());
        user.setLoggedIn(userNode.get("isLoggedIn").asBoolean());
        return user;
    }

    @MessageMapping("/message/{chatRoomId}")
    public void handleChatMessage(@DestinationVariable String chatRoomId, @Payload Message message) {
        System.out.println("Messaggio ricevuto per chatroom " + chatRoomId + ": " + message.getText());
        List<ChatRoom> chatRooms = chatRoomService.getAllChatRooms();
        boolean found=false;
        for (ChatRoom chatRoom : chatRooms) {
            if (chatRoom.getChatRoomId().equals(chatRoomId)) {
                found = true;
                break;
            }
        }
        if (!found){
            chatRoomService.createChat(chatRoomId, message.getSender(), message.getReceiver());
        }
         messageService.saveContent(message);

        messagingTemplate.convertAndSend("/topic/chatroom/" + chatRoomId, message);
    }

    @PostMapping("/message")
    public ResponseEntity<Message> sendMessage(@RequestBody Message message) {
        Message savedMessage = messageService.saveMessage(message);
        return ResponseEntity.ok(savedMessage);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("messageAdd")
    public Message addMessage(@RequestBody Message message) {
        messageService.addMessage(message);
        return message;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("message")
    public Message updateMessage(@RequestBody Message message) {
        messageService.updateMessage(message);
        return message;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("message/{messageId}")
    public String deleteMessage(@PathVariable String messageId)
    {
        messageService.deleteMessage(messageId);
        return "Deleted";
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("load2")
    public String loadData2() {
        messageService.load2();
        return "success";
    }
}



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class SendMessageRequest {
    private String chatRoomId;
    private String text;
    private String sender;
    private String receiver;
}
