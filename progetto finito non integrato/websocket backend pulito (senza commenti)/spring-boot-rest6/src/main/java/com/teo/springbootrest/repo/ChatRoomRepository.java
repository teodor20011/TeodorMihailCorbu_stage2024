package com.teo.springbootrest.repo;

import com.teo.springbootrest.model.ChatRoom;
import com.teo.springbootrest.model.Message;
import com.teo.springbootrest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {

    Optional<ChatRoom> findBySenderIdAndReceiverId(User sender, User receiver);
}
