package com.triphippie.userService.service;

import com.triphippie.userService.model.chatRoom.ChatRoom;
import com.triphippie.userService.model.chatRoom.ChatRoomOutDto;
import com.triphippie.userService.model.user.User;
import com.triphippie.userService.model.user.UserInDto;
import com.triphippie.userService.model.user.UserOutDto;
import com.triphippie.userService.repository.ChatRoomRepository;
import com.triphippie.userService.repository.MessageRepository;
import com.triphippie.userService.repository.UserRepository;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.triphippie.userService.service.UserService;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepo;


    private static ChatRoomOutDto mapToChatRoomOut(ChatRoom chatRoom) {
        return new ChatRoomOutDto(
                chatRoom.getChatRoomId(),
                chatRoom.getSenderId(),
                chatRoom.getReceiverId()
        );
    }

    private User mapToUser(UserInDto userInDto) {
        User user = new User();
        user.setUsername(userInDto.getUsername());
        user.setPassword(userInDto.getPassword());
        user.setFirstName(userInDto.getFirstName());
        user.setLastName(userInDto.getLastName());
        user.setDateOfBirth(userInDto.getDateOfBirth());
        user.setEmail(userInDto.getEmail());
        user.setCity(userInDto.getCity());
        user.setLoggedIn(userInDto.isLoggedIn());

        return user;
    }

    public UserInDto mapToUserInDto(User user) {
        UserInDto userInDto = new UserInDto();
        userInDto.setUsername(user.getUsername());
        userInDto.setPassword(user.getPassword());
        userInDto.setFirstName(user.getFirstName());
        userInDto.setLastName(user.getLastName());
        userInDto.setDateOfBirth(user.getDateOfBirth());
        userInDto.setEmail(user.getEmail());
        userInDto.setAbout(user.getAbout());
        userInDto.setCity(user.getCity());
        userInDto.setLoggedIn(user.isLoggedIn());
        return userInDto;
    }

    public UserInDto getUserInDtoById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return mapToUserInDto(user);
    }


    public List<ChatRoomOutDto> getAllChatRooms() {
        List<ChatRoom> chatRooms =chatRoomRepository.findAll();
        List<ChatRoomOutDto> outChatRooms = new ArrayList<>();
        for (ChatRoom u : chatRooms) {
            outChatRooms.add(mapToChatRoomOut(u));
        }
        return outChatRooms;
    }



    public void createChat(String chatRoomId, UserInDto senderDto, UserInDto receiverDto){
        if (chatRoomId == null || senderDto == null || receiverDto == null) {
            throw new IllegalArgumentException("chatRoomId, sender, and receiver must not be null");
        }

        if (chatRoomRepository.existsById(chatRoomId)) {
            throw new IllegalArgumentException("Chat room with the same ID already exists");
        }

        User sender = mapToUser(senderDto);
        User receiver = mapToUser(receiverDto);

        ChatRoom newChatRoom = new ChatRoom();
        newChatRoom.setChatRoomId(chatRoomId);
        newChatRoom.setSenderId(sender);
        newChatRoom.setReceiverId(receiver);
        newChatRoom.setMessages(new ArrayList<>());

        chatRoomRepository.save(newChatRoom);
    }

    public boolean existsById(String chatRoomId) {
        return chatRoomRepository.existsById(chatRoomId);
    }

}
