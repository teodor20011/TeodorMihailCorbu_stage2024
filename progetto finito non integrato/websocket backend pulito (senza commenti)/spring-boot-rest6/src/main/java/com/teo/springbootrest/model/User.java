package com.teo.springbootrest.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

@Data
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users") //il nome user è una parola con significato in postgresql, non puoi usarla
public class User {
	@Id
	private String nickname;

    private String email;

    private String password;

	@JsonProperty("isLoggedIn")
	private boolean isLoggedIn;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<Message> messagesSender = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<Message> messagesReceiver = new ArrayList<>();
}

