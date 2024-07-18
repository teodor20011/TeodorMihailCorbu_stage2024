package com.triphippie.userService.repository;


import com.triphippie.userService.model.message.Message;
import com.triphippie.userService.model.chatRoom.ChatRoom;
import com.triphippie.userService.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {

    Optional<ChatRoom> findBySenderIdAndReceiverId(User sender, User receiver);
}
