package com.triphippie.userService.controller;

import com.triphippie.userService.model.message.Message;
import com.triphippie.userService.model.user.UserInDto;
import com.triphippie.userService.service.ChatRoomService;
import com.triphippie.userService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/chatRooms")
public class ChatRoomController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private UserService userService;

    /*@MessageMapping("/chatroom/{chatRoomId}")
    public void sendMessage(@DestinationVariable String chatRoomId, @Payload Message message) {
        messagingTemplate.convertAndSend("/chatroom/" + chatRoomId, message);
    }*/

    @MessageMapping("/chatroom/{chatRoomId}")
    public void sendMessage(@DestinationVariable String chatRoomId, @Payload Message message) {
        // Ottieni i dettagli degli utenti dai loro ID
        UserInDto senderDto = chatRoomService.getUserInDtoById(message.getSender().getId());
        UserInDto receiverDto = chatRoomService.getUserInDtoById(message.getReceiver().getId());

        // Crea la chat room se non esiste già
        if (!chatRoomService.existsById(chatRoomId)) {
            chatRoomService.createChat(chatRoomId, senderDto, receiverDto);
        }

        // Invia il messaggio alla chat room
        messagingTemplate.convertAndSend("/chatroom/" + chatRoomId, message);
    }

}
