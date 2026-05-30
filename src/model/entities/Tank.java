package model.entities;

import java.awt.Point;

public abstract class Tank extends Entity {

    public static final int SIZE = 48; // pixels (= 2 map cells)

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    protected Direction direction;
    protected int speed;
    protected int lives;
    protected boolean moving = false;

    public Tank(int x, int y, int speed, int lives) {
        super(x, y, SIZE, SIZE);
        this.speed = speed;
        this.lives = lives;
        this.direction = Direction.UP;
    }

    // Returns the top-left pixel where a bullet should spawn, based on direction
    public Point getBulletSpawnPoint(int bulletSize) {
        int half = (SIZE - bulletSize) / 2;
        return switch (direction) {
            case UP    -> new Point(x + half, y - bulletSize);
            case DOWN  -> new Point(x + half, y + SIZE);
            case LEFT  -> new Point(x - bulletSize, y + half);
            case RIGHT -> new Point(x + SIZE, y + half);
        };
    }

    public void loseLife() {
        lives--;
        if (lives <= 0) setAlive(false);
    }

    public void addLife() {
        lives++;
    }

    public void setDirection(Direction dir) {
        if (dir == direction) return;
        direction = dir;
    }

    public Direction getDirection() { return direction; }
    public int getSpeed()           { return speed; }
    public int getLives()           { return lives; }
    public boolean isMoving()       { return moving; }
    public void setMoving(boolean moving) { this.moving = moving; }
}
