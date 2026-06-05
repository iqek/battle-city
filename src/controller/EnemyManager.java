package controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import core.Difficulty;
import core.GameMap;
import core.entities.Bullet;
import core.entities.EnemyTank;

public class EnemyManager {

    private static final int MAX_ENEMIES_ON_SCREEN = 4;
    private static final int[][] SPAWN_POINTS = {{0,0},{12*GameMap.CELL_PX, 0}, {24*GameMap.CELL_PX, 0}};

    private final GameMap map;
    private final List<EnemyTank> enemies = new ArrayList<>();
    private final List<Bullet> enemyBullets = new ArrayList<>();
    private int enemiesSpawned = 0;
    private boolean frozen = false;
    private Difficulty difficulty = Difficulty.NORMAL;
    private Thread freezeThread ;
    private Thread spawnerThread;

    public EnemyManager(GameMap map) {
        this.map = map;
    }

    public synchronized void reset(Difficulty difficulty) {
        this.difficulty = difficulty;
        if(spawnerThread!= null) spawnerThread.interrupt();
        if(freezeThread!= null) freezeThread.interrupt();
        enemies.clear();
        enemyBullets.clear();
        enemiesSpawned = 0;
        frozen = false;
        startSpawner();
    }

    private void startSpawner() {
        spawnerThread = new Thread(()->{
            while(enemiesSpawned < difficulty.totalEnemies) {
                try {
                    Thread.sleep(3000);
                    while(enemyCount() >= MAX_ENEMIES_ON_SCREEN) {
                        Thread.sleep(500);
                    }
                    spawnNext();
                } catch(InterruptedException e) {
                    return;
                }
            }
        }, "enemy-spawner");
        spawnerThread.start();
    }

    private synchronized void spawnNext() {
        if(enemiesSpawned >= difficulty.totalEnemies) return;
        int[] sp = SPAWN_POINTS[enemiesSpawned % SPAWN_POINTS.length];
        enemies.add(new EnemyTank(sp[0], sp[1], difficulty.enemySpeed, map));
        enemiesSpawned++;
    }

    public synchronized void update() {
        if(!frozen) updateEnemies();
        updateEnemyBullets();
    }

    public synchronized void freeze() {
        if(freezeThread!=null) freezeThread.interrupt();
        frozen = true;
        freezeThread = new Thread(()-> {
            try {
                Thread.sleep(5000);
            }catch(InterruptedException e) {
                return;
            }
            frozen = false;
        }, "freeze-timer");
        freezeThread.start();
    }

    private void updateEnemies() {
        for (EnemyTank e : enemies) {
            e.update();
            if (e.shouldShoot()) {
                Point origin = e.getBulletSpawnPoint(Bullet.SIZE);
                enemyBullets.add(new Bullet(origin.x, origin.y, e.getDirection(), 0, map));
            }
        }
        enemies.removeIf(e -> !e.isAlive());
    }

    private void updateEnemyBullets() {
        for (Bullet b : enemyBullets) b.update();
        enemyBullets.removeIf(b -> !b.isAlive());
    }

    public synchronized boolean allEnemiesDefeated() {
        return enemiesSpawned >= difficulty.totalEnemies && enemies.isEmpty();
    }

    public synchronized void destroyAll() {
        enemies.forEach(e -> e.setAlive(false));
    }

    public synchronized int enemyCount() { return enemies.size(); }
    public synchronized List<EnemyTank> getEnemies() { return new ArrayList<>(enemies); }
    public synchronized List<Bullet> getEnemyBullets() { return new ArrayList<>(enemyBullets); }
}
