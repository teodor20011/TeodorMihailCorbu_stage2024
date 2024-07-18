package com.teo.springbootrest.service;

import com.teo.springbootrest.HibernateUtil;
import com.teo.springbootrest.model.ChatRoom;
import com.teo.springbootrest.model.Message;
import com.teo.springbootrest.model.User;
import com.teo.springbootrest.repo.ChatRoomRepository;
import com.teo.springbootrest.repo.MessageRepository;
import com.teo.springbootrest.repo.UserRepo;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private HibernateUtil hibernateUtil;

    //get all messages
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }


    public List<Message> getAllUsersMessages(User sender, User receiver) {
        System.out.println("Sender: " + sender.getNickname());
        System.out.println("Receiver: " + receiver.getNickname());
        return messageRepository.getAllUsersMessages(sender,receiver);
    }

    /*@Transactional
    public List<Message> getAllUsersMessages(User sender, User receiver) {
        // Carica i messaggi
        List<Message> messages = messageRepository.getAllUsersMessages(sender, receiver);

        // Inizializza le collezioni lazy
        hibernateUtil.initializeMessages(sender, receiver);

        return messages;
    }*/

    //tutorial websocket
   /* public List<Message> getAllUsersMessage(User sender, User receiver) {
        var chatId=chatRoomService.getChatRoomId(sender,receiver,false);
        return chatId.map(messageRepository::findByChatIdentifier).orElse(new ArrayList<>());
    }*/

    // method to add a message
    public void addMessage(Message message) {
        messageRepository.save(message);
    }



    public Message createMessage(String text, ChatRoom chatRoom, String senderNickname, String receiverNickname) {
        User sender = userRepo.findByNickname(senderNickname);
        User receiver = userRepo.findByNickname(receiverNickname);

        Message message = Message.builder()
                //.messageId(UUID.randomUUID().toString())
                .text(text)
                .createdAt(new Date())
                .chatIdentifier(chatRoom)
                .sender(sender)
                .receiver(receiver)
                .build();

        return messageRepository.save(message);
    }
    //tutorial websocket
    /*public Message save(Message message)
    {
        ChatRoom chatId=chatRoomService.getChatRoomId(message.getSender(),message.getReceiver(), true).orElseThrow();
        message.setChatId(chatId);
        messageRepository.save(message);
        return message;
    }*/

    public Message saveMessage(Message message) {
        // Logica per salvare il messaggio nel database
        return messageRepository.save(message);
    }

    public void saveContent(Message message) {
        messageRepository.save(message);
    }

    //method to get message by id
    public Message getMessage(String messageId) {
        return messageRepository.findById(messageId).orElse(new Message());
    }

    //method to update message with message object
    public void updateMessage(Message message) {
        messageRepository.save(message);
    }

    //method to delete message by id
    public void deleteMessage(String messageId) {
        messageRepository.deleteById(messageId);
    }

   /* public List<Message> getMessagesBetweenUsers(int senderId, int receiverId) {
        return messageRepository.findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(
                senderId, receiverId, senderId, receiverId);
    }*/

    //@PostConstruct
    public void load2() {
        List<User> users = userRepo.findAll();
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();
        List<Message> messages = new ArrayList<>(List.of(
                new Message(chatRooms.get(0),new Date(), "Hello from user 1 towards user 2", true, users.get(0), users.get(1)),
                new Message(chatRooms.get(1),new Date(), "Hello from user 2 towards user 3", true, users.get(1), users.get(2)),
                new Message(chatRooms.get(2),new Date(), "Hello from user 3 towards user 4", true, users.get(2), users.get(3)),
                new Message(chatRooms.get(3),new Date(), "Hello from user 4 towards user 5", true, users.get(3), users.get(4)),
                new Message(chatRooms.get(4),new Date(), "Hello from user 5 towards user 1", true, users.get(4), users.get(1))
        ));
        messageRepository.saveAll(messages);
    }





    /*public void load2() {
        List<User> users=userRepo.findAll();
        List<Message> messages = new ArrayList<>(List.of(
                new Message(1,new Date(),"a", true, users.get(1))
        ));

        messageRepository.saveAll(messages);
    }*/
}
