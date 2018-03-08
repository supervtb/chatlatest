package com.example.demo.models;

import javax.persistence.*;

/**
 * Created by albertchubakov on 07.03.2018.
 */

@Entity
@Table(name = "lastmessage")
public class LastMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name="username")
    private String username;
    @Column(name="message")
    private String message;


    public LastMessage() {
    }

    public LastMessage(String username, String message) {
        this.username = username;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
