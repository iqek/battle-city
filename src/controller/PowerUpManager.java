package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import core.GameMap;
import core.TileType;
import core.entities.*;

public class PowerUpManager {

    private static final int EAGLE_ROW = Eagle.Y / GameMap.CELL_PX;
    private static final int EAGLE_COL = Eagle.X / GameMap.CELL_PX;
    private static final int[][] SHOVEL_CELLS = {
            {EAGLE_ROW - 1, EAGLE_COL - 1}, {EAGLE_ROW - 1, EAGLE_COL},
            {EAGLE_ROW - 1, EAGLE_COL + 1}, {EAGLE_ROW - 1, EAGLE_COL + 2},
            {EAGLE_ROW,     EAGLE_COL - 1}, {EAGLE_ROW,     EAGLE_COL + 2},
            {EAGLE_ROW + 1, EAGLE_COL - 1}, {EAGLE_ROW + 1, EAGLE_COL + 2},
    };

    private final GameMap map;
    private final Random random = new Random();
    private final EnemyManager enemyManager;
    private final List<PowerUp> powerUps = new ArrayList<>();
    private TileType[] shovelTiles;
    private Thread spawnerThread;
    private Thread shovelThread;

    public PowerUpManager(GameMap map, EnemyManager enemyManager) {
        this.map = map;
        this.enemyManager = enemyManager;
    }

    public synchronized void reset() {
        if(spawnerThread!=null) spawnerThread.interrupt();
        if(shovelThread!=null) shovelThread.interrupt();
        powerUps.clear();
        startSpawner();
    }

    private void startSpawner() {
        spawnerThread = new Thread(()->{
            while(!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(10_000);
                } catch(InterruptedException e) {
                    return;
                }
                trySpawnPowerUp();
            }
        }, "powerup-spawner");
        spawnerThread.start();
    }

    public synchronized void update(PlayerTank player) {
        checkCollisions(player);
    }

    private synchronized void trySpawnPowerUp() {
        if(!powerUps.isEmpty()) return;

        List<int[]> emptyCells = new ArrayList<>();
        for (int row = 1; row < GameMap.ROWS - 1; row++) {
            for (int col = 1; col < GameMap.COLS - 1; col++) {
                if (map.getTile(row, col) == TileType.EMPTY)
                    emptyCells.add(new int[]{row, col});
            }
        }
        if (emptyCells.isEmpty()) return;

        int[] cell = emptyCells.get(random.nextInt(emptyCells.size()));
        PowerUpType type = PowerUpType.values()[random.nextInt(PowerUpType.values().length)];
        powerUps.add(new PowerUp(cell[1] * GameMap.CELL_PX, cell[0] * GameMap.CELL_PX, type));
    }

    private void checkCollisions(PlayerTank player) {
        powerUps.removeIf(pu -> {
            if (CollisionManager.overlaps(player, pu)) {
                if (pu.getType() == PowerUpType.STAR) player.addStar();
                else if (pu.getType() == PowerUpType.TANK) player.addLife();
                else if(pu.getType() == PowerUpType.BOMB) enemyManager.destroyAll();
                else if(pu.getType() == PowerUpType.CLOCK) enemyManager.freeze();
                else if(pu.getType() == PowerUpType.SHOVEL) activateShovel();
                else if(pu.getType() == PowerUpType.SHIELD) player.setShield();
                return true;
            }
            return false;
        });
    }

    private void activateShovel() {
        shovelTiles = new TileType[SHOVEL_CELLS.length];
        for (int i = 0; i < SHOVEL_CELLS.length; i++) {
            int row = SHOVEL_CELLS[i][0], col = SHOVEL_CELLS[i][1];
            shovelTiles[i] = map.getTile(row, col);
            map.setTile(row, col, TileType.STEEL);
        }
        if(shovelThread!=null) shovelThread.interrupt();
        shovelThread = new Thread(() -> {
            try {
                Thread.sleep(10_000);
            }
            catch (InterruptedException e) { }
            restoreShovelTiles();
        }, "shovel-timer");
        shovelThread.start();
    }

    private void restoreShovelTiles() {
        for (int i = 0; i < SHOVEL_CELLS.length; i++) {
            map.setTile(SHOVEL_CELLS[i][0], SHOVEL_CELLS[i][1], shovelTiles[i]);
        }
    }

    public synchronized List<PowerUp> getPowerUps() { return new ArrayList<>(powerUps); }
}
