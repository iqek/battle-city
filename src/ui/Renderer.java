package ui;

import controller.GameController;
import model.GameMap;
import model.TileType;
import model.entities.*;

import java.awt.*;
import java.awt.image.BufferedImage;

// Draws all game world objects: map tiles, player, bullets, power-ups, enemies
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
        drawEagle(g);
        drawBullets(g);
        drawEnemyBullets(g);
        drawPowerUps(g);
        drawPlayer(g);
        drawEnemies(g);
        drawMapBushes(g); // drawn last so bushes appear on top
    }

    private void drawMap(Graphics g) {
        for (int row = 0; row < GameMap.ROWS; row++) {
            for (int col = 0; col < GameMap.COLS; col++) {
                TileType tile = map.getTile(row, col);
                if (tile == TileType.BUSH) continue; // drawn in second pass

                int x = col * Sprites.DRAW_SIZE;
                int y = row * Sprites.DRAW_SIZE;

                BufferedImage sprite = sprites.getSpriteForTile(tile);
                if (sprite != null) {
                    g.drawImage(sprite, x, y, Sprites.DRAW_SIZE, Sprites.DRAW_SIZE, null);
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
                if (map.getTile(row, col) != TileType.BUSH) continue;
                g.drawImage(sprites.getBush(),
                        col * Sprites.DRAW_SIZE, row * Sprites.DRAW_SIZE,
                        Sprites.DRAW_SIZE, Sprites.DRAW_SIZE, null);
            }
        }
    }

    private void drawPlayer(Graphics g) {
        PlayerTank player = gameController.getPlayer();
        if (player == null) return;

        BufferedImage sprite = switch (player.getDirection()) {
            case UP    -> sprites.getPlayerUp();
            case DOWN  -> sprites.getPlayerDown();
            case LEFT  -> sprites.getPlayerLeft();
            case RIGHT -> sprites.getPlayerRight();
        };

        g.drawImage(sprite, player.getX(), player.getY(), Tank.SIZE, Tank.SIZE, null);
    }

    private void drawEnemies(Graphics g) {
        for (EnemyTank e : gameController.getEnemies()) {
            BufferedImage sprite = switch (e.getDirection()) {
                case UP    -> sprites.getEnemyUp();
                case DOWN  -> sprites.getEnemyDown();
                case LEFT  -> sprites.getEnemyLeft();
                case RIGHT -> sprites.getEnemyRight();
            };
            g.drawImage(sprite, e.getX(), e.getY(), Tank.SIZE, Tank.SIZE, null);
        }
    }

    private void drawEnemyBullets(Graphics g) {
        g.setColor(Color.RED);
        for (Bullet b : gameController.getEnemyBullets()) {
            g.fillRect(b.getX(), b.getY(), Bullet.SIZE, Bullet.SIZE);
        }
    }

    private void drawBullets(Graphics g) {
        g.setColor(Color.WHITE);
        for (Bullet b : gameController.getBullets()) {
            g.fillRect(b.getX(), b.getY(), Bullet.SIZE, Bullet.SIZE);
        }
    }

    private void drawPowerUps(Graphics g) {
        for (PowerUp pu : gameController.getPowerUps()) {
            BufferedImage sprite = switch (pu.getType()) {
                case STAR -> sprites.getPuStar();
                case LIFE -> sprites.getPuTank();
            };
            g.drawImage(sprite, pu.getX(), pu.getY(), 24, 24, null);
        }
    }

    private void drawEagle(Graphics g) {
        Eagle eagle = gameController.getEagle();
        BufferedImage sprite = eagle.isDestroyed() ? sprites.getEagleDead() : sprites.getEagle();
        g.drawImage(sprite, Eagle.X, Eagle.Y, Tank.SIZE, Tank.SIZE, null);
    }
}
