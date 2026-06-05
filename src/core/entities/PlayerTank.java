package core.entities;

import controller.CollisionManager;
import core.GameMap;

public class PlayerTank extends Tank {

    private int stars;
    private Thread shieldThread;
    private boolean shieldActive = false;
    private GameMap map;

    public PlayerTank(int x, int y, GameMap map) {
        super(x, y, 3, 3);
        this.map = map;
    }

    // starting state
    public void reset(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.stars = 0;
        this.lives = 3;
        this.moving = false;
        this.direction = Direction.UP;
        this.alive = true;
        if(shieldThread != null) shieldThread.interrupt();
        setShieldActive(false);
    }

    public void respawn(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.moving = false;
        this.direction = Direction.UP;
        setShield();
    }

    @Override
    public void update() {
        if (!moving) return;

        int nextX = x;
        int nextY = y;

        switch (direction) {
            case UP -> nextY -= speed;
            case DOWN -> nextY += speed;
            case LEFT -> nextX -= speed;
            case RIGHT -> nextX += speed;
        }

        if (!CollisionManager.isOutOfBounds(nextX, nextY, width, height) && !CollisionManager.hitsWall(nextX, nextY, width, height, map)) {
            x = nextX;
            y = nextY;
        }
    }

    public void setShield() {
        if(shieldThread!=null) shieldThread.interrupt();
        setShieldActive(true);
        shieldThread = new Thread(()->{
            try {
                Thread.sleep(5000);
            } catch(InterruptedException e){
                return;
            }
            setShieldActive(false);
        }, "shield-timer");
        shieldThread.start();
    }

    public void addStar() {
        if (stars < 3) stars++;
    }
    public int getStars()       { return stars; }
    public synchronized void setShieldActive(boolean value) { shieldActive = value; }
    public synchronized boolean hasShield() { return shieldActive; }
    public int getMaxBullets()  { return stars >= 2 ? 2 : 1; }
}

