package com.triphippie.userService.model.chatRoom;


import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.triphippie.userService.model.message.Message;
import com.triphippie.userService.model.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

@Data
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoom {
    @Id
    private String chatRoomId;

    // Collega ChatRoom a Message
    @OneToMany(mappedBy = "chatIdentifier", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Message> messages = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "sender", referencedColumnName = "username")
    private User senderId;

    @ManyToOne
    @JoinColumn(name = "receiver", referencedColumnName = "username")
    private User receiverId;


    public static String generateChatRoomId(String username1, String username2) {
        return username1 + "_" + username2;
    }

    // Override del metodo toString() per evitare la ricorsione
    @Override
    public String toString() { //senderId.getNickname(), receiverId.getNickname()
        return "ChatRoom{" +
                "chatRoomId='" + chatRoomId + '\'' +
                ", senderId='" + senderId.getUsername() + '\'' +
                ", receiverId='" + receiverId.getUsername() + '\'' +
                '}';
    }

}
