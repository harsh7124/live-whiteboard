package com.example.whiteboard.repository;

import com.example.whiteboard.model.Stroke;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StrokeRepository extends JpaRepository<Stroke, Long> {
    List<Stroke> findByBoardIdOrderByCreatedAtAsc(Long boardId);
}
