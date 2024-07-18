package com.triphippie.userService.model.chatRoom;

import com.triphippie.userService.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatRoomOutDto {
    private String chatRoomId;

    private User senderId;

    private User receiverId;

}
