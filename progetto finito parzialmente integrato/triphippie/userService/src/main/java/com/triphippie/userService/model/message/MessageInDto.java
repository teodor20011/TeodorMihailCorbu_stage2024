package com.triphippie.userService.model.message;


import com.triphippie.userService.model.chatRoom.ChatRoom;
import com.triphippie.userService.model.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class MessageInDto {

    private ChatRoom chatIdentifier;

    private Date createdAt;

    private String text;

    private boolean editable;

    private User sender;

    private User receiver;


}
