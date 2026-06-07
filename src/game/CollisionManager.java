package game;

import core.GameMap;
import core.entities.Entity;

public class CollisionManager {

    public static boolean isOutOfBounds(int x, int y, int width, int height) {
        int mapSize = GameMap.COLS * GameMap.CELL_PX;
        return x < 0 || y < 0 || x + width > mapSize || y + height > mapSize;
    }

    public static boolean hitsWall(int x, int y, int width, int height, GameMap map) {
        int[] cx = {x, x + width - 1, x, x + width - 1};
        int[] cy = {y, y, y + height - 1, y + height - 1};

        for (int i = 0; i < 4; i++) {
            int col = cx[i] / GameMap.CELL_PX;
            int row = cy[i] / GameMap.CELL_PX;
            if (col < 0 || col >= GameMap.COLS || row < 0 || row >= GameMap.ROWS) continue;
            if (map.getTile(row, col).blocksMovement())
                return true;
        }
        return false;
    }

    public static boolean overlaps(Entity a, Entity b) {
        return a.getBounds().intersects(b.getBounds());
    }
}
