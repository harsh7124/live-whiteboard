package com.example.whiteboard.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CursorAIServiceImpl implements AIService {

    @Value("${ai.cursor.url:https://api.cursor.com}")
    private String cursorUrl;

    @Value("${ai.cursor.key:}")
    private String apiKey;

    private final RestTemplate rest = new RestTemplate();

    @Override
    public String analyzeBoard(Long boardId, String contextJson) {
        // Stubbed implementation: in production, call Cursor / Anti-Gravity API here.
        // Example: POST to cursorUrl + /analyze with API key header and JSON body.
        return "[AI] Suggestion placeholder for board " + boardId;
    }
}
