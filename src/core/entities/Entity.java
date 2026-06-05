package core.entities;

import java.awt.Rectangle;

public abstract class Entity {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected boolean alive;

    public Entity(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.alive = true;
    }

    public abstract void update();

    public Rectangle getBounds() { return new Rectangle(x,y,width,height); }

    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isAlive() { return alive; }
    public void setAlive(boolean alive) { this.alive = alive; }
}

