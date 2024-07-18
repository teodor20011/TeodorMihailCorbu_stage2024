package com.triphippie.userService.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.triphippie.userService.model.message.Message;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "trip_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, length = 50, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(length = 100, nullable = false)
    private String firstName;

    @Column(length = 100, nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDate dateOfBirth;

    private String email;

    private String about;

    private String city;

    private String profileImageUrl;

    @JsonProperty("isLoggedIn")
    private boolean isLoggedIn;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Message> messagesSender = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Message> messagesReceiver = new ArrayList<>();


}
