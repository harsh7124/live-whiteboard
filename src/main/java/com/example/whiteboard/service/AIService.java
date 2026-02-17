package com.example.whiteboard.service;

public interface AIService {
    /**
     * Request AI analysis or suggestions for a board.
     * Implementation should call Cursor or Anti-Gravity API.
     */
    String analyzeBoard(Long boardId, String contextJson);
}
