package com.example.whiteboard.controller;

import com.example.whiteboard.model.Board;
import com.example.whiteboard.model.Stroke;
import com.example.whiteboard.repository.BoardRepository;
import com.example.whiteboard.repository.StrokeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/boards")
public class BoardRestController {

    private final BoardRepository boardRepository;
    private final StrokeRepository strokeRepository;

    public BoardRestController(BoardRepository boardRepository, StrokeRepository strokeRepository) {
        this.boardRepository = boardRepository;
        this.strokeRepository = strokeRepository;
    }

    @PostMapping
    public Board createBoard(@RequestBody Board board) {
        board.setCreatedAt(Instant.now());
        return boardRepository.save(board);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Board> getBoard(@PathVariable Long id) {
        Optional<Board> b = boardRepository.findById(id);
        return b.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/strokes")
    public List<Stroke> getStrokes(@PathVariable Long id) {
        return strokeRepository.findByBoardIdOrderByCreatedAtAsc(id);
    }

    @PostMapping("/{id}/strokes")
    public Stroke saveStroke(@PathVariable Long id, @RequestBody Stroke stroke) {
        stroke.setBoardId(id);
        stroke.setCreatedAt(Instant.now());
        return strokeRepository.save(stroke);
    }
}
