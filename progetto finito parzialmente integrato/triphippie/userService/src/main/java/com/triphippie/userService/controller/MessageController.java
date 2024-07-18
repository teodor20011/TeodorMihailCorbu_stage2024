package com.triphippie.userService.controller;

/*import com.triphippie.userService.model.chatRoom.ChatRoom;
import com.triphippie.userService.model.message.Message;
import com.triphippie.userService.model.user.User;
import com.triphippie.userService.service.ChatRoomService;
import com.triphippie.userService.service.UserService;
import com.triphippie.userService.service.MessageService;
import com.triphippie.userService.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;*/


import com.triphippie.userService.model.chatRoom.ChatRoom;
import com.triphippie.userService.model.chatRoom.ChatRoomOutDto;
import com.triphippie.userService.model.message.Message;
import com.triphippie.userService.model.message.MessageInDto;
import com.triphippie.userService.model.message.MessageOutDto;
import com.triphippie.userService.model.user.UserInDto;
import com.triphippie.userService.model.user.UserOutDto;
import com.triphippie.userService.model.user.User;
import com.triphippie.userService.service.ChatRoomService;
import com.triphippie.userService.service.UserService;
import com.triphippie.userService.service.MessageService;
import com.triphippie.userService.repository.MessageRepository;
import com.triphippie.userService.service.UserServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("api/messages")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private UserService userService;

    /*@CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("between2usersMessages/{senderId}/{receiverId}")
    public List<Message> getAllUsersMessages(@PathVariable String senderId, @PathVariable String receiverId) {

        User sender = userService.getUser(senderId);
        User receiver = userService.getUser(receiverId);
        return messageService.getAllUsersMessages(sender, receiver);
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
    }*/

    //@CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/between2usersMessages/{senderId}/{receiverId}")
    public ResponseEntity<List<MessageOutDto>> getAllUsersMessages(@PathVariable Integer senderId, @PathVariable Integer receiverId) {
        System.out.println("Endpoint /between2usersMessages/ chiamato con senderId: " + senderId + ", receiverId: " + receiverId);
        try {
            User sender = userService.getUserEntityById(senderId);
            User receiver = userService.getUserEntityById(receiverId);

            List<MessageOutDto> messages = messageService.getAllUsersMessages(sender, receiver);
            return new ResponseEntity<>(messages, HttpStatus.OK);
        } catch (UserServiceException ex) {
            System.out.println("Primo errore ");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Secondo errore ");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        }
    }

    @MessageMapping("/message/{chatRoomId}")
    public void handleChatMessage(@DestinationVariable String chatRoomId, @Payload MessageInDto message) {
        System.out.println("Messaggio ricevuto per chatroom " + chatRoomId + ": " + message.getText());

        try {
            List<ChatRoomOutDto> chatRooms = chatRoomService.getAllChatRooms();
            boolean found = false;
            for (ChatRoomOutDto chatRoom : chatRooms) {
                if (chatRoom.getChatRoomId().equals(chatRoomId)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                // Mappa User a UserInDto prima di chiamare createChat
                UserInDto senderDto = chatRoomService.mapToUserInDto(message.getSender());
                UserInDto receiverDto = chatRoomService.mapToUserInDto(message.getReceiver());
                chatRoomService.createChat(chatRoomId, senderDto, receiverDto);
            }

            messageService.saveContent(message);
            messagingTemplate.convertAndSend("/topic/chatroom/" + chatRoomId, message);

        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error handling chat message");
        }
    }
}
