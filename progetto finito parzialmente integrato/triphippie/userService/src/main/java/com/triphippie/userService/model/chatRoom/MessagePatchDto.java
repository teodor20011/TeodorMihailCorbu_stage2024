package com.triphippie.userService.model.chatRoom;

import com.triphippie.userService.model.user.User;
import lombok.Data;

@Data
public class MessagePatchDto {

    private String chatRoomId;

    private User senderId;

    private User receiverId;
}
