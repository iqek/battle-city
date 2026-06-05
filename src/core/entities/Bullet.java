package core.entities;

import controller.CollisionManager;
import core.GameMap;
import core.TileType;

public class Bullet extends Entity{

    public static final int SIZE = 6;
    public static final int SPEED = 6;

    private Tank.Direction direction;
    private final int speed;
    private int damage;
    private boolean canBreakSteel;
    private final GameMap map;

    public Bullet(int x, int y, Tank.Direction direction, int stars, GameMap map) {
        this(x, y, direction, stars, SPEED, map);
    }

    public Bullet(int x, int y, Tank.Direction direction, int stars, int speed, GameMap map) {
        super(x, y, SIZE, SIZE);
        this.direction = direction;
        this.map = map;
        this.speed = speed;
        this.damage = (stars >= 2) ? 4 : 1;
        this.canBreakSteel = (stars == 3);
    }

    @Override
    public void update() {
        switch (direction) {
            case UP -> y -= SPEED;
            case DOWN -> y += SPEED;
            case LEFT -> x -= SPEED;
            case RIGHT -> x += SPEED;
        }

        if (CollisionManager.isOutOfBounds(x, y, width, height)) {
            setAlive(false);
            return;
        }

        checkWallCollision();

    }

    private void checkWallCollision(){
        int[] cx = {x, x+width-1, x, x+width-1};
        int[] cy = {y, y, y+height-1, y+height-1};

        for(int i = 0; i < 4; i++){
            int col = cx[i] / GameMap.CELL_PX;
            int row = cy[i] / GameMap.CELL_PX;

            if (col < 0 || col >= GameMap.COLS || row < 0 || row >= GameMap.ROWS) continue;

            TileType tile = map.getTile(row, col);

            if (tile == TileType.BRICK || tile == TileType.STEEL) {
                if (tile == TileType.BRICK || canBreakSteel)
                    map.applyDamage(row, col, damage);
                setAlive(false);
                return;
            }
        }
    }

    public Tank.Direction getDirection(){ return direction; }
}
