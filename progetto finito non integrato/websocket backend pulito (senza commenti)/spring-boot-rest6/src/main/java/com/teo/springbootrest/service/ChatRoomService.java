package com.teo.springbootrest.service;

import com.teo.springbootrest.model.ChatRoom;
import com.teo.springbootrest.model.Message;
import com.teo.springbootrest.model.User;
import com.teo.springbootrest.repo.ChatRoomRepository;
import com.teo.springbootrest.repo.MessageRepository;
import com.teo.springbootrest.repo.UserRepo;
import lombok.RequiredArgsConstructor;
//import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepo userRepo;

    public ChatRoom createChatRoom(String chatRoomId, String senderNickname, String receiverNickname) {
        User sender = userRepo.findByNickname(senderNickname);
        User receiver = userRepo.findByNickname(receiverNickname);

        ChatRoom chatRoom = ChatRoom.builder()
                .chatRoomId(chatRoomId)
                .senderId(sender)
                .receiverId(receiver)
                .build();

        return chatRoomRepository.save(chatRoom);
    }

    public List<ChatRoom> getAllChatRooms() {
        return chatRoomRepository.findAll();
    }

    public ChatRoom getChatRoom(String chatRoomId) {
        return chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new RuntimeException("Chat room not found"));
    }

    public void createChat(String chatRoomId, User sender, User receiver) {
        if (chatRoomId == null || sender == null || receiver == null) {
            throw new IllegalArgumentException("chatRoomId, sender, and receiver must not be null");
        }
        ChatRoom addedMessage = new ChatRoom(chatRoomId,new ArrayList<>(),sender,receiver);
        chatRoomRepository.save(addedMessage);
    }

    /*public Optional<ChatRoom> getChatRoomId(User senderId, User receiverId, boolean createNewRoomIfNotExists) {
        return chatRoomRepository.findBySenderIdAndReceiverId(senderId, receiverId)
                .map(ChatRoom::getMessages)
                .or(()-> {
            if(createNewRoomIfNotExists)
            {
                var chatId= createChatId(senderId, receiverId);
                return Optional.of(chatId);
            }
            return Optional.empty();
        });
    }*/


    /*public Optional<String> getChatRoomId(User senderId, User receiverId, boolean createNewRoomIfNotExists) {
        return chatRoomRepository.findBySenderIdAndReceiverId(senderId, receiverId)
                .map(ChatRoom::getChatRoomId)  // Usa il getter per ottenere l'ID della chat room
                .or(() -> {
                    if (createNewRoomIfNotExists) {
                        var chatId = createChatId(senderId, receiverId);
                        return Optional.of(chatId);
                    }
                    return Optional.empty();
                });
    }*/

    /*public Optional<String> getChatRoomId(User senderId, User receiverId, boolean createNewRoomIfNotExists) {
        return chatRoomRepository.findBySenderIdAndReceiverId(senderId, receiverId)
                .map(ChatRoom::getChatRoomId)
                .or(() -> {
                    if (createNewRoomIfNotExists) {
                        var chatId = createChatId(senderId, receiverId);
                        return Optional.of(chatId);
                    }
                    return Optional.empty();
                });
    }*/


    /*private String createChatId(User senderId, User receiverId) {
        //qui scrivere query che estrae solo l'id di sender e id di receiver, quindi da User a String
        String chatId= String.format("%s_%s", senderId.getNickname(), receiverId.getNickname());

        ChatRoom senderReceiver= ChatRoom.builder()
                .messages(chatId)
                .senderId(senderId)
                .receiverId(receiverId)
                .build();

        ChatRoom receiverSender= ChatRoom.builder()
                .messages(chatId)
                .senderId(receiverId)
                .receiverId(senderId)
                .build();
        chatRoomRepository.save(senderReceiver);
        chatRoomRepository.save(receiverSender);

        return chatId;
    }*/

  /*  public String createChatId(User sender, User receiver) {
        String chatId1 = String.format("%s_%s", sender.getNickname(), receiver.getNickname());
        //String chatId2 = String.format("%s_%s", receiver.getNickname(), sender.getNickname());

        // Creazione della prima chat room (sender -> receiver)
        ChatRoom senderReceiver = chatRoomRepository.findBySenderIdAndReceiverId(sender, receiver)
                .orElseGet(() -> ChatRoom.builder()
                        .chatRoomId(chatId1)
                        .senderId(sender)
                        .receiverId(receiver)
                        .build());

        // Creazione della seconda chat room (receiver -> sender)
        ChatRoom receiverSender = chatRoomRepository.findBySenderIdAndReceiverId(receiver, sender)
                .orElseGet(() -> ChatRoom.builder()
                        .chatRoomId(chatId1)
                        .senderId(receiver)
                        .receiverId(sender)
                        .build());

        // Salvataggio delle chat room
        chatRoomRepository.save(senderReceiver);
        chatRoomRepository.save(receiverSender);

        return chatId1; // Puoi scegliere quale chatId restituire
    }*/

    public void load3() {
        // arrayList to store User objects
        List<User> users = userRepo.findAll();
        List<ChatRoom> chatRoom =
                new ArrayList<>(List.of(
                        new ChatRoom("a_b", new ArrayList<>(), users.get(0), users.get(1)),
                        new ChatRoom("a_c", new ArrayList<>(), users.get(0), users.get(2)),
                        new ChatRoom("c_a", new ArrayList<>(), users.get(2), users.get(0)),
                        new ChatRoom("b_c", new ArrayList<>(), users.get(1), users.get(2)),
                        new ChatRoom("c_d", new ArrayList<>(), users.get(1), users.get(3)),
                        new ChatRoom("d_e", new ArrayList<>(), users.get(1), users.get(1)),
                        new ChatRoom("e_a", new ArrayList<>(), users.get(0), users.get(2))
                ));

        chatRoomRepository.saveAll(chatRoom);
    }



}
