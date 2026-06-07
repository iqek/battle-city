package game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import core.Difficulty;
import core.GameMap;
import core.entities.*;
import ui.GamePanel;
import ui.MainFrame;

public class GameController implements Runnable{

    private static final int FPS = 60;
    private static final long NS_PER_FRAME = 1000000000 / FPS;

    private static final int PLAYER_START_X = 8  * GameMap.CELL_PX;
    private static final int PLAYER_START_Y = 24 * GameMap.CELL_PX;

    private Eagle eagle;
    private PlayerTank player;
    private final GameMap map;
    private final GamePanel gamePanel;
    private final InputHandler inputHandler;
    private MainFrame mainFrame;

    private final EnemyManager enemyManager;
    private final PowerUpManager powerUpManager;
    private final List<Bullet> bullets = new ArrayList<>();

    private int score = 0;
    private Difficulty difficulty = Difficulty.NORMAL;
    private Thread gameThread;
    private boolean running = false;
    private boolean paused  = false;

    public GameController(GameMap map, GamePanel gamePanel) {
        this.map = map;
        this.gamePanel = gamePanel;
        this.player = new PlayerTank(Tank.SIZE * 4, Tank.SIZE * 6, map);
        this.inputHandler = new InputHandler(player);
        this.enemyManager = new EnemyManager(map);
        this.powerUpManager = new PowerUpManager(map, enemyManager);

        gamePanel.addKeyListener(inputHandler);
        gamePanel.setFocusable(true);
    }

    public void start() {
        eagle = new Eagle();
        player.reset(PLAYER_START_X, PLAYER_START_Y);
        bullets.clear();
        enemyManager.reset(difficulty);
        powerUpManager.reset();
        score = 0;
        setRunning(true);
        setPaused(false);
        gameThread = new Thread(this, "game-loop");
        gameThread.start();
    }

    public void stop() {
        setRunning(false);
        try {
            if (gameThread != null) gameThread.join();
        } catch (InterruptedException e) {
            System.err.println("game thread interrupted while stopping");
        }
    }

    @Override
    public void run() {
        while (isRunning()) {
            long start = System.nanoTime();

            if (!isPaused()) update();
            gamePanel.repaint();
            long elapsed = System.nanoTime() - start;
            long sleepNs = NS_PER_FRAME - elapsed;
            if (sleepNs > 0) {
                try {
                    Thread.sleep(sleepNs / 1000000, (int)(sleepNs % 1000000));
                } catch (InterruptedException e) {
                    System.err.println("game loop sleep interrupted");
                }
            }
        }
    }

    private void update() {
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
        int activeBullets = 0;
        for(Bullet b : bullets) {
            if(b.isAlive()) activeBullets++;
        }
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
                    if (!e.isAlive()) addScore(difficulty.scorePerKill);
                    return true;
                }
            }
            return false;
        });
    }

    private void checkEnemyBulletPlayerCollision() {
        for (Bullet b : enemyManager.getEnemyBullets()) {
            if (CollisionManager.overlaps(b, player)) {
                if (!player.hasShield()) {
                    player.loseLife();
                    if (player.isAlive()) player.respawn(PLAYER_START_X, PLAYER_START_Y);
                }
                b.setAlive(false);
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
        setRunning(false);
        if (mainFrame != null) mainFrame.onGameEnd(won, score);
    }

    private synchronized void setRunning(boolean value) { running = value; }
    private synchronized boolean isRunning() { return running; }
    private synchronized void setPaused(boolean value) { paused = value; }
    public synchronized void togglePause() {
        paused = !paused;
        enemyManager.setAllFrozen(paused);
    }
    public synchronized boolean isPaused() { return paused; }

    public void addScore(int points) {score+=points;}
    public int getScore() { return score;}
    public Eagle getEagle() { return eagle;}
    public PlayerTank getPlayer() {return player;}
    public synchronized List<Bullet> getBullets(){return new ArrayList<>(bullets);}
    public List<EnemyTank> getEnemies(){return enemyManager.getEnemies();}
    public List<Bullet> getEnemyBullets(){return enemyManager.getEnemyBullets();}
    public List<PowerUp> getPowerUps(){return powerUpManager.getPowerUps();}
    public void setDifficulty(Difficulty d) { this.difficulty = d; }
    public void setMainFrame(MainFrame m) {
        this.mainFrame = m;
    }
}
