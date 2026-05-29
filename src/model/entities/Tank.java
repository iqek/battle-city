package model.entities;

public abstract class Tank extends Entity {

    public enum Direction{
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    protected Direction direction;
    protected int speed;
    protected int lives;
    protected boolean moving = false;

    public Tank(int x, int y, int speed, int lives) {
        super(x,y,48,48);

        this.speed = speed;
        this.lives = lives;
        this.direction = Direction.UP;
    }

    public void loseLife(){
        lives--;
        if(lives <= 0)
            setAlive(false);
    }

    public void addLife(){
        lives++;
    }

    public Direction getDirection() { return direction; }
    public int getSpeed() { return speed; }
    public int getLives() { return lives; }
    public boolean isMoving() { return moving; }
    public void setMoving(boolean moving) { this.moving = moving; }
}
