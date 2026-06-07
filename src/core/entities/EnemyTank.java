package core.entities;

import java.util.Random;

import game.CollisionManager;
import core.GameMap;

public class EnemyTank extends Tank {

    private final GameMap map;
    private final Random random = new Random();
    private Thread directionThread;
    private Thread shootThread;
    private boolean readyToShoot = false;
    private boolean frozen = false;

    public EnemyTank(int x, int y, int speed, GameMap map) {
        super(x,y,speed,1);
        this.map = map;
        setDirection(Direction.DOWN);
        setMoving(true);
        startThreads();
    }

    private void startThreads() {
        directionThread = new Thread(() -> {
            while(isAlive()) {
                try {
                    Thread.sleep(3000);
                } catch(InterruptedException e) {
                    return;
                }
                if(isAlive() && !frozen) pickRandomDirection();
            }
        }, "enemy-dir");

        shootThread = new Thread(()->{
            while(isAlive()) {
                try {
                    Thread.sleep(2000 + random.nextInt(1000));
                }catch(InterruptedException e){
                    return;
                }
                if(isAlive() && !frozen) setReadyToShoot();
            }
        }, "enemy-shoot");

        directionThread.start();
        shootThread.start();
    }

    @Override
    public void setAlive(boolean alive) {
        super.setAlive(alive);

        if(!alive) {
            if(directionThread != null) directionThread.interrupt();
            if(shootThread != null) shootThread.interrupt();
        }
    }

    @Override
    public void update() {
        int nextX = x;
        int nextY = y;
        switch(direction) {
            case UP -> nextY -= speed;
            case DOWN -> nextY += speed;
            case LEFT -> nextX -= speed;
            case RIGHT -> nextX +=speed;
        }

        if (CollisionManager.isOutOfBounds(nextX, nextY, width, height) || CollisionManager.hitsWall(nextX, nextY, width, height, map)) {
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

    public synchronized void setFrozen(boolean frozen) { this.frozen = frozen; }
    private synchronized void setReadyToShoot() { readyToShoot = true; }

    public boolean shouldShoot() {
        if (readyToShoot) {
            readyToShoot = false;
            return true;
        }
        return false;
    }
}
