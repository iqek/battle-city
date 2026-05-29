package ui;

import controller.GameController;
import model.GameMap;
import model.entities.Bullet;
import model.entities.PlayerTank;
import model.entities.PowerUp;
import model.entities.PowerUpType;

import java.awt.*;

// Draws all game world objects: map tiles, player, bullets, powerups.
public class Renderer {

    private final GameController gameController;
    private final GameMap map;
    private final Sprites sprites;

    public Renderer(GameController gameController, GameMap map, Sprites sprites) {
        this.gameController = gameController;
        this.map = map;
        this.sprites = sprites;
    }

    public void render(Graphics g) {
        drawMap(g);
        drawBullets(g);
        drawPowerUps(g);
        drawPlayer(g);
        drawMapBushes(g);  // drawn last so bushes appear on top
    }

    private void drawMap(Graphics g) {
        for (int row = 0; row < GameMap.ROWS; row++) {
            for (int col = 0; col < GameMap.COLS; col++) {
                model.TileType tile = map.getTile(row, col);
                if (tile == model.TileType.BUSH) continue;  // drawn in second pass

                int[] coords = Sprites.coordsForTile(tile);
                int x = col * Sprites.DRAW_SIZE;
                int y = row * Sprites.DRAW_SIZE;

                if (coords != null) {
                    g.drawImage(sprites.getSprite(coords[0], coords[1]),
                            x, y, Sprites.DRAW_SIZE, Sprites.DRAW_SIZE, null);
                } else {
                    g.setColor(Color.BLACK);
                    g.fillRect(x, y, Sprites.DRAW_SIZE, Sprites.DRAW_SIZE);
                }
            }
        }
    }

    private void drawMapBushes(Graphics g) {
        for (int row = 0; row < GameMap.ROWS; row++) {
            for (int col = 0; col < GameMap.COLS; col++) {
                if (map.getTile(row, col) != model.TileType.BUSH) continue;
                int[] coords = Sprites.coordsForTile(model.TileType.BUSH);
                g.drawImage(sprites.getSprite(coords[0], coords[1]),
                        col * Sprites.DRAW_SIZE, row * Sprites.DRAW_SIZE,
                        Sprites.DRAW_SIZE, Sprites.DRAW_SIZE, null);
            }
        }
    }

    private void drawPlayer(Graphics g) {
        PlayerTank player = gameController.getPlayer();
        if (player == null) return;

        int[] coords = switch (player.getDirection()) {
            case UP -> Sprites.PLAYER_UP;
            case DOWN -> Sprites.PLAYER_DOWN;
            case LEFT -> Sprites.PLAYER_LEFT;
            case RIGHT -> Sprites.PLAYER_RIGHT;
        };

        g.drawImage(sprites.getLargeSprite(coords[0], coords[1]),
                player.getX(), player.getY(), Sprites.DRAW_SIZE * 2, Sprites.DRAW_SIZE * 2, null);
    }

    private void drawPowerUps(Graphics g) {
        for (PowerUp pu : gameController.getPowerUps()) {
            int[] coords = (pu.getType() == PowerUpType.STAR) ? Sprites.PU_STAR : Sprites.PU_TANK;
            g.drawImage(sprites.getLargeSprite(coords[0], coords[1]),
                    pu.getX(), pu.getY(), 24, 24, null);
        }
    }

    private void drawBullets(Graphics g) {
        g.setColor(Color.WHITE);
        for (Bullet b : gameController.getBullets()) {
            g.fillRect(b.getX(), b.getY(), Bullet.SIZE, Bullet.SIZE);
        }
    }
}
