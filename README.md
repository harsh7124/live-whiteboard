# Live Whiteboard (Spring Boot + Postgres)

## Overview

- Real-time collaborative whiteboard using Spring Boot WebSocket (STOMP) and a simple frontend.
- PostgreSQL used to persist boards and strokes.
- AI integration hooks (Cursor / Anti-Gravity) are provided as stubs; add API keys and implement calls in `CursorAIServiceImpl`.

## Project Structure

- `src/main/java/com/example/whiteboard` — application code
  - `config/WebSocketConfig.java` — STOMP over WebSocket configuration
  - `controller/BoardWebSocketController.java` — receives drawing messages and broadcasts to `/topic/boards/{boardId}`
  - `controller/BoardRestController.java` — REST endpoints to create boards and persist strokes
  - `model/Board.java`, `model/Stroke.java` — JPA entities
  - `repository/*` — Spring Data repositories
  - `service/AIService` + `CursorAIServiceImpl` — AI integration stub
- `src/main/resources/static` — simple HTML + JS client (`index.html`, `app.js`)
- `docker-compose.yml` — Postgres + app for local development
- `Dockerfile` — build container image

## Database Design (Plan)

### Tables:

- **boards**
  - `id` (PK, auto-increment)
  - `name` (VARCHAR)
  - `created_at` (TIMESTAMP)

- **strokes**
  - `id` (PK, auto-increment)
  - `board_id` (FK -> boards.id)
  - `client_id` (VARCHAR, identifies the user who drew)
  - `path_json` (TEXT, JSON array of points `[{x, y}, ...]`)
  - `color` (VARCHAR, hex color code)
  - `width` (DOUBLE)
  - `created_at` (TIMESTAMP)

### Design Rationale:

- **Append-only strokes model**: Each stroke is immutable after creation, making concurrent updates trivial. Perfect for real-time sync.
- **JSON path storage**: Flexible schema; each stroke stores its full path as JSON. For large boards, consider compressing or bucketing strokes.
- **Indexed by board_id**: Quick retrieval of all strokes for a board during load/recovery.
- **Future enhancements**: Add `operation` column for undo/redo, or separate `events` table for full replay.

## AI Integration Plan

- Use `AIService` interface to call external AI providers (Cursor or Anti-Gravity). 
- Provide API key via `AI_CURSOR_KEY` environment variable.
- Example features: auto-cleanup of messy strokes, extract diagram text, suggest layout, generate slide notes from board contents.

## How to Run Locally

### Prerequisites

- Docker & Docker Compose installed
- OR Maven 3.8+, Java 11+, and PostgreSQL 14+

### Option 1: With Docker Compose (Recommended)

```bash
cd live-whiteboard
docker-compose up --build
```

Then open http://localhost:8080 in your browser. Multiple windows will sync via WebSocket.

### Option 2: Manual Setup

1. **Start PostgreSQL** (or use a managed service):

```bash
# If installed locally
psql -U postgres -c "CREATE DATABASE whiteboard;"
```

2. **Build & run Spring Boot**:

```bash
mvn clean package
java -jar target/live-whiteboard-0.1.0.jar
```

3. **Access the app**:

Open http://localhost:8080

## Deployment Notes

### Deploying to Cloud

**Option A: Render.com (Recommended for quick setup)**

1. Push this repo to GitHub.
2. Create new Web Service on Render, connect your GitHub repo.
3. Add Postgres database addon via Render dashboard.
4. Set environment variables (see below).
5. Deploy.

**Option B: Railway.app**

1. Push to GitHub.
2. Import project to Railway.
3. Add Postgres service.
4. Configure environment variables.
5. Deploy with one click.

**Option C: AWS / GCP / Azure**

Use any K8s or App Platform offering. Build the Docker image and push to your registry.

### Environment Variables

For production, set these:

```bash
JDBC_DATABASE_URL=jdbc:postgresql://your-db-host:5432/whiteboard
JDBC_DATABASE_USERNAME=postgres
JDBC_DATABASE_PASSWORD=secure-password
AI_CURSOR_URL=https://api.cursor.com
AI_CURSOR_KEY=your-api-key
PORT=8080
```

## Improvements for Interview Readiness

- [ ] Add user authentication (JWT + WebSocket auth)
- [ ] Add board-level permissions and presence indicators
- [ ] Implement efficient canvas state (server snapshots, compact stroke storage)
- [ ] Implement real AI features with Cursor or Anti-Gravity (1-2 working calls)
- [ ] Add unit/integration tests and CI workflow (GitHub Actions)
- [ ] Add request validation, error handling, and logging
- [ ] Implement undo/redo on server side
- [ ] Add metrics/monitoring (e.g., Prometheus)

## Quick Start Commands

```bash
# Clone the repo
git clone https://github.com/your-username/live-whiteboard.git
cd live-whiteboard

# Run locally with Docker
docker-compose up --build

# OR build and run manually
mvn clean package
java -jar target/live-whiteboard-0.1.0.jar
```

---

**Questions?** Feel free to open an issue or reach out!
