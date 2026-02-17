package com.example.whiteboard.model;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "strokes")
public class Stroke {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long boardId;

    private String clientId;

    @Column(columnDefinition = "TEXT")
    private String pathJson;

    private String color;

    private double width;

    private Instant createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getBoardId() { return boardId; }
    public void setBoardId(Long boardId) { this.boardId = boardId; }
    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    public String getPathJson() { return pathJson; }
    public void setPathJson(String pathJson) { this.pathJson = pathJson; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public double getWidth() { return width; }
    public void setWidth(double width) { this.width = width; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
