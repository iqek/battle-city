package controller;

import model.GameMap;
import model.TileType;
import model.entities.PlayerTank;
import model.entities.PowerUp;
import model.entities.PowerUpType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Handles power-up spawning and collection
public class PowerUpManager {

    private static final int SPAWN_INTERVAL = 600; // frames (~10 sec)

    private final GameMap map;
    private final Random random = new Random();
    private final List<PowerUp> powerUps = new ArrayList<>();
    private int spawnTimer = 0;

    public PowerUpManager(GameMap map) {
        this.map = map;
    }

    public void reset() {
        powerUps.clear();
        spawnTimer = 0;
    }

    public void update(PlayerTank player) {
        spawnTimer++;
        if (spawnTimer >= SPAWN_INTERVAL) {
            spawnTimer = 0;
            trySpawnPowerUp();
        }
        checkCollisions(player);
    }

    private void trySpawnPowerUp() {
        if (!powerUps.isEmpty()) return; // only one at a time

        List<int[]> emptyCells = new ArrayList<>();
        for (int row = 1; row < GameMap.ROWS - 1; row++) {
            for (int col = 1; col < GameMap.COLS - 1; col++) {
                if (map.getTile(row, col) == TileType.EMPTY) {
                    emptyCells.add(new int[]{row, col});
                }
            }
        }
        if (emptyCells.isEmpty()) return;

        int[] cell = emptyCells.get(random.nextInt(emptyCells.size()));
        int px = cell[1] * GameMap.CELL_PX;
        int py = cell[0] * GameMap.CELL_PX;

        PowerUpType type = random.nextBoolean() ? PowerUpType.STAR : PowerUpType.LIFE;
        powerUps.add(new PowerUp(px, py, type));
    }

    private void checkCollisions(PlayerTank player) {
        powerUps.removeIf(pu -> {
            if (CollisionManager.overlaps(player, pu)) {
                if (pu.getType() == PowerUpType.STAR) player.addStar();
                else if (pu.getType() == PowerUpType.LIFE) player.addLife();
                return true;
            }
            return false;
        });
    }

    public List<PowerUp> getPowerUps() { return new ArrayList<>(powerUps); }
}
