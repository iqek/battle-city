package model.entities;

import controller.CollisionManager;
import model.GameMap;

import java.util.Random;

public class EnemyTank extends Tank {

    private static final int DIRECTION_CHANGE_INTERVAL = 180; // frames (~3 sec)
    private static final int SHOOT_INTERVAL = 120;            // frames (~2 sec)

    private final GameMap map;
    private final Random random = new Random();
    private int moveTimer = 0;
    private int shootTimer = 0;
    private boolean readyToShoot = false;

    public EnemyTank(int x, int y, GameMap map) {
        super(x, y, 2, 1);
        this.map = map;
        setDirection(Direction.DOWN);
        setMoving(true);
    }

    @Override
    public void update() {
        moveTimer++;
        shootTimer++;

        if (moveTimer >= DIRECTION_CHANGE_INTERVAL) {
            moveTimer = 0;
            pickRandomDirection();
        }

        // Set flag here so shouldShoot() is a clean read
        if (shootTimer >= SHOOT_INTERVAL + random.nextInt(60)) {
            shootTimer = 0;
            readyToShoot = true;
        }

        int nextX = x;
        int nextY = y;
        switch (direction) {
            case UP    -> nextY -= speed;
            case DOWN  -> nextY += speed;
            case LEFT  -> nextX -= speed;
            case RIGHT -> nextX += speed;
        }

        if (CollisionManager.isOutOfBounds(nextX, nextY, width, height)
                || CollisionManager.hitsWall(nextX, nextY, width, height, map)) {
            pickRandomDirection();
        } else {
            x = nextX;
            y = nextY;
        }
    }

    private void pickRandomDirection() {
        Direction[] dirs = Direction.values();
        setDirection(dirs[random.nextInt(dirs.length)]);
    }

    // Returns true once per shoot interval, then resets — safe to call as a simple check
    public boolean shouldShoot() {
        if (readyToShoot) {
            readyToShoot = false;
            return true;
        }
        return false;
    }
}
