package com.triphippie.userService.model.message;


import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;
import com.triphippie.userService.model.chatRoom.ChatRoom;
import com.triphippie.userService.model.user.User;

@Data
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int messageId;

    @ManyToOne
    @JoinColumn(name = "chatIdentifier", referencedColumnName = "chatRoomId")
    private ChatRoom chatIdentifier;

    private Date createdAt;

    private String text;

    private boolean editable;

    @ManyToOne
    @JoinColumn(name = "sender", referencedColumnName = "username")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver", referencedColumnName = "username")
    private User receiver;


    public Message(ChatRoom chatIdentifier, Date createdAt, String text, boolean editable, User sender, User receiver) {
        //this.messageId= messageId;
        this.chatIdentifier=chatIdentifier;
        this.createdAt = createdAt;
        this.text = text;
        this.editable = editable;
        this.sender = sender;
        this.receiver=receiver;
    }

    // Override del metodo toString() per evitare la ricorsione
    @Override
    public String toString() {//receiver.getNickname()
        return "Message{" +
                "messageId=" + messageId +
                ", createdAt=" + createdAt +
                ", text='" + text + '\'' +
                ", editable=" + editable +
                ", sender='" + sender.getUsername() + '\'' +
                ", receiver='" + receiver.getUsername() + '\'' +
                '}';
    }

}
