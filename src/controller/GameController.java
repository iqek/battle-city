package controller;

import model.GameMap;
import model.entities.*;
import ui.GamePanel;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class GameController implements Runnable {

    private static final int FPS = 60;
    private static final long NS_PER_FRAME = 1_000_000_000 / FPS;

    private static final int PLAYER_START_X = Tank.SIZE * 4;
    private static final int PLAYER_START_Y = Tank.SIZE * 6;

    private Eagle eagle;
    private PlayerTank player;
    private final GameMap map;
    private final GamePanel gamePanel;
    private final InputHandler inputHandler;
    private GameEndListener gameEndListener;

    private final EnemyManager enemyManager;
    private final PowerUpManager powerUpManager;
    private final List<Bullet> bullets = new ArrayList<>();

    private int score = 0;
    private volatile boolean paused = false;
    private boolean running;
    private Thread gameThread;

    public GameController(GameMap map, GamePanel gamePanel) {
        this.map = map;
        this.gamePanel = gamePanel;
        this.player = new PlayerTank(PLAYER_START_X, PLAYER_START_Y, map);
        this.inputHandler = new InputHandler(player);
        this.enemyManager = new EnemyManager(map);
        this.powerUpManager = new PowerUpManager(map);

        gamePanel.addKeyListener(inputHandler);
        gamePanel.setFocusable(true);
    }

    public void start() {
        eagle = new Eagle();
        player.reset(PLAYER_START_X, PLAYER_START_Y);
        bullets.clear();
        enemyManager.reset();
        powerUpManager.reset();
        score = 0;
        running = true;
        paused = false;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void stop() {
        running = false;
        try {
            if (gameThread != null) gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (running) {
            long start = System.nanoTime();

            if (!paused) update();
            gamePanel.repaint();

            long elapsed = System.nanoTime() - start;
            long sleepTime = NS_PER_FRAME - elapsed;
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime / 1_000_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void update() {
        if (eagle.isDestroyed() || !player.isAlive()) {
            triggerGameEnd(false);
            return;
        }
        if (enemyManager.allEnemiesDefeated()) {
            triggerGameEnd(true);
            return;
        }

        player.update();
        if (inputHandler.consumeFireRequest()) tryFire();
        updateBullets();

        enemyManager.update();
        checkPlayerBulletEnemyCollisions();
        checkEnemyBulletPlayerCollision();
        checkBulletEagleCollision();

        powerUpManager.update(player);
    }

    private synchronized void updateBullets() {
        for (Bullet b : bullets) b.update();
        bullets.removeIf(b -> !b.isAlive());
    }

    public synchronized void tryFire() {
        long activeBullets = bullets.stream().filter(Bullet::isAlive).count();
        if (activeBullets >= player.getMaxBullets()) return;

        Point origin = player.getBulletSpawnPoint(Bullet.SIZE);
        bullets.add(new Bullet(origin.x, origin.y, player.getDirection(), player.getStars(), map));
    }

    private void checkPlayerBulletEnemyCollisions() {
        List<EnemyTank> enemies = enemyManager.getEnemies();
        bullets.removeIf(b -> {
            for (EnemyTank e : enemies) {
                if (CollisionManager.overlaps(b, e)) {
                    e.loseLife();
                    if (!e.isAlive()) addScore(100);
                    return true;
                }
            }
            return false;
        });
    }

    private void checkEnemyBulletPlayerCollision() {
        for (Bullet b : enemyManager.getEnemyBullets()) {
            if (CollisionManager.overlaps(b, player)) {
                player.loseLife();
                b.setAlive(false); // cleaned up by EnemyManager on next update
            }
        }
    }

    private void checkBulletEagleCollision() {
        for (Bullet b : bullets) {
            if (CollisionManager.overlaps(b, eagle)) {
                eagle.destroy();
                b.setAlive(false);
                return;
            }
        }
        for (Bullet b : enemyManager.getEnemyBullets()) {
            if (CollisionManager.overlaps(b, eagle)) {
                eagle.destroy();
                b.setAlive(false);
                return;
            }
        }
    }

    private void triggerGameEnd(boolean won) {
        running = false;
        if (gameEndListener != null) {
            javax.swing.SwingUtilities.invokeLater(() -> gameEndListener.onGameEnd(won, score));
        }
    }

    public void addScore(int points)            { score += points; }
    public int  getScore()                      { return score; }

    public Eagle          getEagle()            { return eagle; }
    public PlayerTank     getPlayer()           { return player; }
    public InputHandler   getInputHandler()     { return inputHandler; }
    public synchronized List<Bullet>    getBullets()       { return new ArrayList<>(bullets); }
    public List<EnemyTank> getEnemies()         { return enemyManager.getEnemies(); }
    public List<Bullet>   getEnemyBullets()     { return enemyManager.getEnemyBullets(); }
    public List<PowerUp>  getPowerUps()         { return powerUpManager.getPowerUps(); }

    public void togglePause()  { paused = !paused; }
    public boolean isPaused()  { return paused; }

    public void setGameEndListener(GameEndListener listener) { this.gameEndListener = listener; }
}
