package com.teo.springbootrest.model;


import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JoinColumn(name = "sender", referencedColumnName = "nickname")
    private User senderId;

    @ManyToOne
    @JoinColumn(name = "receiver", referencedColumnName = "nickname")
    private User receiverId;


    public static String generateChatRoomId(String nickname1, String nickname2) {
        return nickname1 + "_" + nickname2;
    }

    // Override del metodo toString() per evitare la ricorsione
    @Override
    public String toString() {
        return "ChatRoom{" +
                "chatRoomId='" + chatRoomId + '\'' +
                ", senderId='" + senderId.getNickname() + '\'' +
                ", receiverId='" + receiverId.getNickname() + '\'' +
                '}';
    }
}


