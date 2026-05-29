package model.entities;

import controller.CollisionManager;
import model.GameMap;
import model.TileType;

public class PlayerTank extends Tank {

    private static final int TILE_SIZE = 24;
    private int stars;
    private GameMap map;

    public PlayerTank(int x, int y, GameMap map){
        super(x,y,3,3);
        this.map = map;
    }


    @Override
    public void update() {

        if (!moving) return;

        int nextX = x;
        int nextY = y;

        switch(direction){
            case UP -> nextY -= speed;
            case DOWN -> nextY += speed;
            case LEFT -> nextX -= speed;
            case RIGHT -> nextX +=speed;
        }

        if (!CollisionManager.isOutOfBounds(nextX, nextY, width, height) && !CollisionManager.hitsWall(nextX, nextY, width, height, map)) {
            x = nextX;
            y = nextY;
        }
    }

    public void setDirection(Direction dir){
        if(dir == direction) return;
        direction = dir;
    }

    public void addStar() {
        if (stars < 3) stars ++;
    }

    public int getStars() { return stars; }
    public int getMaxBullets(){ return stars >= 2? 2 : 1; }
    public int getBulletDamage(){ return stars>= 2? 4 : 1; }
}
