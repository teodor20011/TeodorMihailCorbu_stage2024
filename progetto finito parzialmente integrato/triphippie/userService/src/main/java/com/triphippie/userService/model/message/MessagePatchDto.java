package com.triphippie.userService.model.message;

import com.triphippie.userService.model.chatRoom.ChatRoom;
import com.triphippie.userService.model.user.User;
import lombok.Data;

import java.util.Date;
@Data
public class MessagePatchDto {

    private ChatRoom chatIdentifier;

    private Date createdAt;

    private String text;

    private boolean editable;

    private User sender;

    private User receiver;
}
