package com.teo.springbootrest.model;

import lombok.*;

import javax.persistence.Entity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class ChatNotification {
    private String id;
    private User sender;
    private User receiver;
    private String text;
}
