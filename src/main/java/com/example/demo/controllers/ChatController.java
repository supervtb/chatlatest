package com.example.demo.controllers;


import com.example.demo.LastMessageRepository;
import com.example.demo.models.ChatMessage;
import com.example.demo.models.LastMessage;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Created by albertchubakov on 03.03.2018.
 */
@Controller
public class ChatController {

    @Autowired
    private LastMessageRepository lastMessageRepository;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        if(chatMessage!= null){
            LastMessage lastMessage = new LastMessage();
            lastMessage.setUsername(chatMessage.getSender());
            lastMessage.setMessage(chatMessage.getContent());
            lastMessageRepository.save(lastMessage);
        }
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

    @MessageMapping("/chat.getMes")
    @SendTo("/topic/private")
    public ChatMessage getMes() {
        ArrayList<LastMessage> lastMessages = new ArrayList<>();
        lastMessages.addAll((Collection<? extends LastMessage>) lastMessageRepository.findAll());
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender("server");
        Gson gson = new Gson();
        String json = gson.toJson(lastMessages);
        chatMessage.setContent(json);
        return chatMessage;
    }


}
