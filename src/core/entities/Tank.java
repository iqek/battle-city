package core.entities;

import java.awt.Point;

public abstract class Tank extends Entity {

    public static final int SIZE = 48;
    public static final int HITBOX = 44;
    public static final int DRAW_OFFSET = (SIZE - HITBOX) / 2; // centers the sprite over the hitbox

    public enum Direction{
        UP, DOWN, LEFT, RIGHT
    }

    protected Direction direction;
    protected int speed;
    protected int lives;
    protected boolean moving = false;

    public Tank(int x, int y, int speed, int lives) {
        super(x,y,HITBOX,HITBOX);
        this.speed = speed;
        this.lives = lives;
        this.direction = Direction.UP;
    }

    public Point getBulletSpawnPoint(int bulletSize) {
        int half = (HITBOX - bulletSize) / 2;
        return switch (direction) {
            case UP -> new Point(x + half, y - bulletSize);
            case DOWN -> new Point(x + half, y + SIZE);
            case LEFT -> new Point(x - bulletSize, y + half);
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
    public int getLives() { return lives; }
    public void setMoving(boolean moving) { this.moving = moving; }
}
