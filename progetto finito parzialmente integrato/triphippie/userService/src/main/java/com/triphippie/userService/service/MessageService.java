package com.triphippie.userService.service;

import com.triphippie.userService.HibernateUtil;
import com.triphippie.userService.model.chatRoom.ChatRoom;
import com.triphippie.userService.model.message.Message;
import com.triphippie.userService.model.message.MessageInDto;
import com.triphippie.userService.model.message.MessageOutDto;
import com.triphippie.userService.model.user.User;
import com.triphippie.userService.model.user.UserOutDto;
import com.triphippie.userService.repository.ChatRoomRepository;
import com.triphippie.userService.repository.MessageRepository;
import com.triphippie.userService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private HibernateUtil hibernateUtil;


    private static MessageOutDto mapToMessageOut(Message message) {
        return new MessageOutDto(
                message.getMessageId(),
                message.getChatIdentifier(),
                message.getCreatedAt(),
                message.getText(),
                message.isEditable(),
                message.getSender(),
                message.getReceiver()
        );
    }

    public List<MessageOutDto> getAllUsersMessages(User sender, User receiver) {
        List<Message> messages = messageRepository.getAllUsersMessages(sender, receiver);

        List<MessageOutDto> outMessages = new ArrayList<>();
        for (Message message : messages) {
            outMessages.add(mapToMessageOut(message));
        }
        return outMessages;
    }

    public void saveContent(MessageInDto messageInDto) {
        Message message = new Message(
                messageInDto.getChatIdentifier(),
                messageInDto.getCreatedAt(),
                messageInDto.getText(),
                messageInDto.isEditable(),
                messageInDto.getSender(),
                messageInDto.getReceiver()
        );

        messageRepository.save(message);
    }


}
