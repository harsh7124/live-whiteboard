let socket = null;
let stompClient = null;
let canvas = document.getElementById('canvas');
let ctx = canvas.getContext('2d');
let drawing = false;
let currentPath = [];

function connect() {
  const boardId = document.getElementById('boardId').value;
  socket = new SockJS('/ws');
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function () {
    stompClient.subscribe(`/topic/boards/${boardId}`, function (msg) {
      const stroke = JSON.parse(msg.body);
      drawStroke(stroke, false);
    });
  });
}

function sendStroke(boardId, stroke) {
  if (!stompClient) return;
  stompClient.send(`/app/boards/${boardId}/draw`, {}, JSON.stringify(stroke));
  // Also persist via REST
  fetch(`/api/boards/${boardId}/strokes`, {
    method: 'POST', headers: {'Content-Type':'application/json'}, body: JSON.stringify(stroke)
  }).catch(console.warn);
}

function drawStroke(stroke, isLocal) {
  try {
    const path = JSON.parse(stroke.pathJson);
    ctx.strokeStyle = stroke.color || '#000';
    ctx.lineWidth = stroke.width || 2;
    ctx.beginPath();
    for (let i = 0; i < path.length; i++) {
      const p = path[i];
      if (i === 0) ctx.moveTo(p.x, p.y); else ctx.lineTo(p.x, p.y);
    }
    ctx.stroke();
  } catch (e) { console.warn(e); }
}

canvas.addEventListener('mousedown', e => { drawing = true; currentPath = []; pushPoint(e); });
canvas.addEventListener('mousemove', e => { if (!drawing) return; pushPoint(e); redrawTemp(); });
canvas.addEventListener('mouseup', e => { if (!drawing) return; drawing = false; finalizeStroke(); });
canvas.addEventListener('mouseleave', e => { if (!drawing) return; drawing = false; finalizeStroke(); });

function pushPoint(e) {
  const rect = canvas.getBoundingClientRect();
  currentPath.push({ x: e.clientX - rect.left, y: e.clientY - rect.top });
}

function redrawTemp() {
  if (currentPath.length === 0) return;
  ctx.beginPath(); ctx.moveTo(currentPath[0].x, currentPath[0].y);
  for (let i=1;i<currentPath.length;i++) ctx.lineTo(currentPath[i].x, currentPath[i].y);
  ctx.stroke();
}

function finalizeStroke() {
  const boardId = document.getElementById('boardId').value;
  const stroke = { clientId: 'client-' + Math.random().toString(36).slice(2,8), pathJson: JSON.stringify(currentPath), color: '#000', width: 2 };
  drawStroke(stroke, true);
  sendStroke(boardId, stroke);
  currentPath = [];
}

document.getElementById('connect').addEventListener('click', () => {
  connect();
});

document.getElementById('clear').addEventListener('click', () => { ctx.clearRect(0,0,canvas.width,canvas.height); });
