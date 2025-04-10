const canvas = document.getElementById('gameCanvas');
const ctx = canvas.getContext('2d');
const scoreElement = document.getElementById('score');
const attemptsElement = document.getElementById('attempts');
const restartButton = document.getElementById('restart');
const gameOverElement = document.getElementById('gameOver');
const finalScoreElement = document.getElementById('finalScore');

// Настройки игры
const config = {
    ballFriction: 0.98,
    stopThreshold: 0.2,
    maxHoles: 3,
    initialAttempts: 10,
    holeSpawnChance: 0.01,
    powerMultiplier: 0.2,
    holeRadius: 30
};

let score = 0;
let attempts = config.initialAttempts;
let balls = [];
let holes = [];
let isDragging = false;
let dragStart = { x: 0, y: 0 };
let dragEnd = { x: 0, y: 0 };
let playerBall = null;
let gameRunning = true;
let allBallsStopped = true;
let touchId = null;

class Ball {
    constructor(x, y, radius, isPlayer = false) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = isPlayer ? '#FFFFFF' : this.getRandomColor();
        this.dx = 0;
        this.dy = 0;
        this.mass = radius * radius;
        this.isPlayer = isPlayer;
        this.inHole = false;
    }

    getRandomColor() {
        const colors = ['#FF5252', '#FF4081', '#E040FB', '#7C4DFF', '#536DFE', '#448AFF'];
        return colors[Math.floor(Math.random() * colors.length)];
    }

    update() {
        if (this.inHole) return;

        this.dx *= config.ballFriction;
        this.dy *= config.ballFriction;

        this.x += this.dx;
        this.y += this.dy;

        this.handleWallCollision();
    }

    handleWallCollision() {
        if (this.x - this.radius < 0) {
            this.x = this.radius;
            this.dx = Math.abs(this.dx) * 0.8;
        }
        if (this.x + this.radius > canvas.width) {
            this.x = canvas.width - this.radius;
            this.dx = -Math.abs(this.dx) * 0.8;
        }
        if (this.y - this.radius < 0) {
            this.y = this.radius;
            this.dy = Math.abs(this.dy) * 0.8;
        }
        if (this.y + this.radius > canvas.height) {
            this.y = canvas.height - this.radius;
            this.dy = -Math.abs(this.dy) * 0.8;
        }
    }

    draw() {
        if (this.inHole) return;

        ctx.beginPath();
        ctx.arc(this.x, this.y, this.radius, 0, Math.PI * 2);
        ctx.fillStyle = this.color;
        ctx.fill();

        if (this.isPlayer) {
            ctx.beginPath();
            ctx.arc(this.x, this.y, this.radius * 0.7, 0, Math.PI * 2);
            ctx.fillStyle = 'rgba(255, 255, 255, 0.3)';
            ctx.fill();
        }
    }

    isPointInside(x, y) {
        const dx = x - this.x;
        const dy = y - this.y;
        return dx * dx + dy * dy <= this.radius * this.radius;
    }
}

class Hole {
    constructor(x, y) {
        this.x = x;
        this.y = y;
        this.radius = config.holeRadius;
        this.pulse = 0;
        this.active = true;
    }

    draw() {
        if (!this.active) return;

        ctx.beginPath();
        ctx.arc(this.x, this.y, this.radius, 0, Math.PI * 2);
        ctx.fillStyle = '#000';
        ctx.fill();

        ctx.beginPath();
        ctx.arc(this.x, this.y, this.radius * (1 + Math.abs(Math.sin(this.pulse)) * 0.3), 0, Math.PI * 2);
        ctx.strokeStyle = `rgba(255, 255, 255, ${0.5 - Math.abs(Math.sin(this.pulse)) * 0.3})`;
        ctx.lineWidth = 3;
        ctx.stroke();

        this.pulse += 0.05;
    }
}

function initGame() {
    balls = [];
    holes = [];
    score = 0;
    attempts = config.initialAttempts;
    gameRunning = true;
    allBallsStopped = true;

    playerBall = new Ball(canvas.width / 2, canvas.height / 2, 20, true);
    balls.push(playerBall);

    for (let i = 0; i < 8; i++) {
        createRandomBall();
    }

    createHole();
    createHole();

    updateUI();
}

function createRandomBall() {
    const radius = 15 + Math.random() * 15;
    const x = radius + Math.random() * (canvas.width - radius * 2);
    const y = radius + Math.random() * (canvas.height - radius * 2);
    const newBall = new Ball(x, y, radius);
    balls.push(newBall);
}

function createHole() {
    if (holes.length >= config.maxHoles) return;

    const margin = 100;
    let validPosition = false;
    let x, y;

    while (!validPosition) {
        x = margin + Math.random() * (canvas.width - margin * 2);
        y = margin + Math.random() * (canvas.height - margin * 2);
        validPosition = true;

        for (const hole of holes) {
            const dx = hole.x - x;
            const dy = hole.y - y;
            if (Math.sqrt(dx * dx + dy * dy) < 150) {
                validPosition = false;
                break;
            }
        }

        const dx = x - playerBall.x;
        const dy = y - playerBall.y;
        if (Math.sqrt(dx * dx + dy * dy) < 200) {
            validPosition = false;
        }
    }

    holes.push(new Hole(x, y));
}

function checkCollisions() {
    for (let i = 0; i < balls.length; i++) {
        for (let j = i + 1; j < balls.length; j++) {
            const a = balls[i];
            const b = balls[j];
            
            const dx = b.x - a.x;
            const dy = b.y - a.y;
            const distance = Math.sqrt(dx * dx + dy * dy);
            
            if (distance < a.radius + b.radius) {
                resolveCollision(a, b);
            }
        }
    }
}

function resolveCollision(a, b) {
    const dx = b.x - a.x;
    const dy = b.y - a.y;
    const angle = Math.atan2(dy, dx);
    const sin = Math.sin(angle);
    const cos = Math.cos(angle);

    const vx1 = a.dx * cos + a.dy * sin;
    const vy1 = a.dy * cos - a.dx * sin;
    const vx2 = b.dx * cos + b.dy * sin;
    const vy2 = b.dy * cos - b.dx * sin;

    const vx1Final = ((a.mass - b.mass) * vx1 + 2 * b.mass * vx2) / (a.mass + b.mass);
    const vx2Final = ((b.mass - a.mass) * vx2 + 2 * a.mass * vx1) / (a.mass + b.mass);

    a.dx = vx1Final * cos - vy1 * sin;
    a.dy = vy1 * cos + vx1Final * sin;
    b.dx = vx2Final * cos - vy2 * sin;
    b.dy = vy2 * cos + vx2Final * sin;
}

function checkHoleCollisions() {
    let scored = false;
    
    holes.forEach((hole, index) => {
        if (!hole.active) return;

        const dx = hole.x - playerBall.x;
        const dy = hole.y - playerBall.y;
        const distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < hole.radius + playerBall.radius) {
            hole.active = false;
            score += 100;
            scored = true;
            
            setTimeout(() => {
                holes.splice(index, 1);
                createHole();
            }, 500);
        }
    });
    
    return scored;
}

function checkAllBallsStopped() {
    return balls.every(ball => 
        (Math.abs(ball.dx) < config.stopThreshold && 
        Math.abs(ball.dy) < config.stopThreshold
    ));
}

function updateUI() {
    scoreElement.textContent = score;
    attemptsElement.textContent = attempts;
}

function endGame() {
    gameRunning = false;
    finalScoreElement.textContent = score;
    gameOverElement.style.display = 'block';
    restartButton.style.display = 'block';
}

function gameLoop() {
    if (!gameRunning) return;

    ctx.clearRect(0, 0, canvas.width, canvas.height);

    balls.forEach(ball => ball.update());
    checkCollisions();
    const scored = checkHoleCollisions();
    
    const currentStopped = checkAllBallsStopped();
    if (currentStopped !== allBallsStopped) {
        allBallsStopped = currentStopped;
        
        if (allBallsStopped && !scored) {
            attempts--;
            updateUI();
            
            if (attempts <= 0) {
                endGame();
            } else {
                playerBall.x = canvas.width / 2;
                playerBall.y = canvas.height / 2;
                playerBall.dx = 0;
                playerBall.dy = 0;
            }
        }
    }

    // Отрисовка
    ctx.fillStyle = '#222';
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    holes.forEach(hole => hole.draw());
    balls.forEach(ball => ball.draw());

    if (isDragging) {
        ctx.beginPath();
        ctx.moveTo(playerBall.x, playerBall.y);
        ctx.lineTo(dragEnd.x, dragEnd.y);
        ctx.strokeStyle = 'rgba(255, 255, 255, 0.7)';
        ctx.lineWidth = 2;
        ctx.stroke();
    }

    if (allBallsStopped) {
        ctx.fillStyle = 'rgba(255, 255, 255, 0.7)';
        ctx.font = '20px Arial';
        ctx.textAlign = 'center';
        ctx.fillText('Потяните шар для броска', canvas.width/2, canvas.height - 50);
    }

    requestAnimationFrame(gameLoop);
}

// Обработчики событий
function handleInputStart(x, y) {
    if (!gameRunning || !allBallsStopped) return false;
    if (!playerBall.isPointInside(x, y)) return false;
    
    isDragging = true;
    dragStart.x = playerBall.x;
    dragStart.y = playerBall.y;
    dragEnd.x = x;
    dragEnd.y = y;
    return true;
}

canvas.addEventListener('mousedown', e => {
    const rect = canvas.getBoundingClientRect();
    const x = e.clientX - rect.left;
    const y = e.clientY - rect.top;
    
    if (handleInputStart(x, y)) {
        e.preventDefault();
    }
});

canvas.addEventListener('mousemove', e => {
    if (!isDragging) return;
    
    const rect = canvas.getBoundingClientRect();
    dragEnd.x = e.clientX - rect.left;
    dragEnd.y = e.clientY - rect.top;
});

canvas.addEventListener('mouseup', () => {
    if (!isDragging) return;
    
    isDragging = false;
    const dx = (dragStart.x - dragEnd.x) * config.powerMultiplier;
    const dy = (dragStart.y - dragEnd.y) * config.powerMultiplier;
    
    playerBall.dx = dx;
    playerBall.dy = dy;
});

canvas.addEventListener('touchstart', e => {
    const touch = e.touches[0];
    const rect = canvas.getBoundingClientRect();
    const x = touch.clientX - rect.left;
    const y = touch.clientY - rect.top;
    
    if (handleInputStart(x, y)) {
        touchId = touch.identifier;
        e.preventDefault();
    }
});

canvas.addEventListener('touchmove', e => {
    if (!isDragging) return;
    
    const touch = Array.from(e.touches).find(t => t.identifier === touchId);
    if (!touch) return;
    
    const rect = canvas.getBoundingClientRect();
    dragEnd.x = touch.clientX - rect.left;
    dragEnd.y = touch.clientY - rect.top;
    e.preventDefault();
});

canvas.addEventListener('touchend', () => {
    if (!isDragging) return;
    
    isDragging = false;
    const dx = (dragStart.x - dragEnd.x) * config.powerMultiplier;
    const dy = (dragStart.y - dragEnd.y) * config.powerMultiplier;
    
    playerBall.dx = dx;
    playerBall.dy = dy;
});

restartButton.addEventListener('click', () => {
    gameOverElement.style.display = 'none';
    restartButton.style.display = 'none';
    initGame();
    gameLoop();
});

function resizeCanvas() {
    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;
}
window.addEventListener('resize', resizeCanvas);
resizeCanvas();

initGame();
gameLoop();