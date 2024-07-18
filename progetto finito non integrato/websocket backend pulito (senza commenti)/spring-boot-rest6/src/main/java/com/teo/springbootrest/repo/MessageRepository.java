package com.teo.springbootrest.repo;


import com.teo.springbootrest.model.ChatRoom;
import com.teo.springbootrest.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.teo.springbootrest.model.Message;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {
    @Transactional
    @Query("SELECT m FROM Message m WHERE (m.sender = ?1 AND m.receiver=?2)  OR (m.sender = ?2 AND m.receiver = ?1)" )	//query per pescare tutti imessaggi tra 2 utenti specificati
    List<Message> getAllUsersMessages(User sender, User receiver);

    List<Message> findByChatIdentifier(ChatRoom chatRoom);
}
