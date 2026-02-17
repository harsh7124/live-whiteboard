package com.example.whiteboard.controller;

import com.example.whiteboard.model.Stroke;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class BoardWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public BoardWebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/boards/{boardId}/draw")
    public void handleDraw(@DestinationVariable String boardId, @Payload Stroke stroke) {
        // Broadcast stroke to all subscribers of the board
        messagingTemplate.convertAndSend(String.format("/topic/boards/%s", boardId), stroke);
    }
}
