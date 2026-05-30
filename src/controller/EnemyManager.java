package controller;

import model.GameMap;
import model.entities.Bullet;
import model.entities.EnemyTank;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

// Handles enemy spawning, AI updates, and enemy bullet lifecycle
public class EnemyManager {

    public static final int TOTAL_ENEMIES = 20;

    private static final int ENEMY_SPAWN_INTERVAL = 180;
    private static final int MAX_ENEMIES_ON_SCREEN = 4;
    private static final int[][] SPAWN_POINTS = {
            {0,                       0},
            {12 * GameMap.CELL_PX,    0},
            {24 * GameMap.CELL_PX,    0}
    };

    private final GameMap map;
    private final List<EnemyTank> enemies = new ArrayList<>();
    private final List<Bullet> enemyBullets = new ArrayList<>();
    private int enemySpawnTimer = 0;
    private int enemiesSpawned = 0;

    public EnemyManager(GameMap map) {
        this.map = map;
    }

    public void reset() {
        enemies.clear();
        enemyBullets.clear();
        enemySpawnTimer = 0;
        enemiesSpawned = 0;
    }

    public void update() {
        spawnEnemies();
        updateEnemies();
        updateEnemyBullets();
    }

    private void spawnEnemies() {
        if (enemiesSpawned >= TOTAL_ENEMIES) return;
        if (enemies.size() >= MAX_ENEMIES_ON_SCREEN) return;
        enemySpawnTimer++;
        if (enemySpawnTimer < ENEMY_SPAWN_INTERVAL) return;

        enemySpawnTimer = 0;
        int[] sp = SPAWN_POINTS[enemiesSpawned % SPAWN_POINTS.length];
        enemies.add(new EnemyTank(sp[0], sp[1], map));
        enemiesSpawned++;
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

    public boolean allEnemiesDefeated() {
        return enemiesSpawned >= TOTAL_ENEMIES && enemies.isEmpty();
    }

    public List<EnemyTank> getEnemies()      { return new ArrayList<>(enemies); }
    public List<Bullet>    getEnemyBullets() { return new ArrayList<>(enemyBullets); }
}
