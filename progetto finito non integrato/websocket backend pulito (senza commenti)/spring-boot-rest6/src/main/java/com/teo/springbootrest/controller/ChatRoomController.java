package com.teo.springbootrest.controller;
import com.teo.springbootrest.model.ChatRoom;
import com.teo.springbootrest.model.Message;
import com.teo.springbootrest.repo.ChatRoomRepository;
import com.teo.springbootrest.repo.MessageRepository;
import com.teo.springbootrest.service.ChatRoomService;
import com.teo.springbootrest.service.MessageService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import com.teo.springbootrest.model.User;
import com.teo.springbootrest.repo.UserRepo;
import com.teo.springbootrest.service.UserService;

import jakarta.persistence.Entity;
import lombok.*;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {
    @Autowired		//per usare un metodo definito nel repo
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chatroom/{chatRoomId}")
    public void sendMessage(@DestinationVariable String chatRoomId, @Payload Message message) {
        messagingTemplate.convertAndSend("/chatroom/" + chatRoomId, message);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/chatroom")
    public List<ChatRoom> getAllMessages() {
        return chatRoomService.getAllChatRooms();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/chatroom")
    public ResponseEntity<ChatRoom> createChatRoom(@RequestBody CreateChatRoomRequest request) {
        String chatRoomId = ChatRoom.generateChatRoomId(request.getSender(), request.getReceiver());
        ChatRoom chatRoom = chatRoomService.createChatRoom(chatRoomId, request.getSender(), request.getReceiver());
        return ResponseEntity.ok(chatRoom);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("load3")
    public String loadData3() {
        chatRoomService.load3();
        return "success";
    }
}



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class CreateChatRoomRequest {
    private String sender;
    private String receiver;

}
