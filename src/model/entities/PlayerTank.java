package model.entities;

import controller.CollisionManager;
import model.GameMap;

public class PlayerTank extends Tank {

    private static final int INITIAL_LIVES = 3;
    private int stars;
    private GameMap map;

    public PlayerTank(int x, int y, GameMap map) {
        super(x, y, 3, INITIAL_LIVES);
        this.map = map;
    }

    // Resets the player to starting state for a new game
    public void reset(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.stars = 0;
        this.lives = INITIAL_LIVES;
        this.moving = false;
        this.direction = Direction.UP;
        this.alive = true;
    }

    @Override
    public void update() {
        if (!moving) return;

        int nextX = x;
        int nextY = y;

        switch (direction) {
            case UP    -> nextY -= speed;
            case DOWN  -> nextY += speed;
            case LEFT  -> nextX -= speed;
            case RIGHT -> nextX += speed;
        }

        if (!CollisionManager.isOutOfBounds(nextX, nextY, width, height)
                && !CollisionManager.hitsWall(nextX, nextY, width, height, map)) {
            x = nextX;
            y = nextY;
        }
    }

    public void addStar() {
        if (stars < 3) stars++;
    }

    public int getStars()       { return stars; }
    public int getMaxBullets()  { return stars >= 2 ? 2 : 1; }
}
