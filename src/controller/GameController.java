package controller;

import model.GameMap;
import model.TileType;
import model.entities.Bullet;
import model.entities.PlayerTank;
import model.entities.PowerUp;
import model.entities.PowerUpType;
import model.entities.Tank;
import ui.GamePanel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameController implements Runnable {

    private static final int FPS = 60;
    private static final long NS_PER_FRAME = 1_000_000_000 / FPS;

    private static final int SPAWN_INTERVAL = 600; // 10 seconds at 60 fps

    private List<Bullet>  bullets  = new ArrayList<>();
    private List<PowerUp> powerUps = new ArrayList<>();
    private int spawnTimer = 0;
    private final Random random = new Random();

    private PlayerTank player;
    private GameMap map;
    private GamePanel gamePanel;
    private InputHandler inputHandler;

    private boolean running;
    private Thread gameThread;

    public GameController(GameMap map, GamePanel gamePanel) {
        this.map = map;
        this.gamePanel = gamePanel;
        this.player = new PlayerTank(48 * 4, 48 * 6, map);
        this.inputHandler = new InputHandler(player);

        gamePanel.addKeyListener(inputHandler);
        gamePanel.setFocusable(true);
    }

    public void start(){
        running = true;
        gameThread = new Thread(this); // Creates a thread that executes the run() method of the specified Runnable object
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
        while(running){
            long start = System.nanoTime();

            update();
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

    public void update(){
        player.update();
        if(inputHandler.consumeFireRequest()) tryFire();
        updateBullets();
        spawnTimer++;
        if (spawnTimer >= SPAWN_INTERVAL) {
            spawnTimer = 0;
            trySpawnPowerUp();
        }
        checkPowerUpCollisions();
    }

    private synchronized void updateBullets(){
        for (Bullet b : bullets) {
            b.update();
        }

        List<Bullet> toRemove = new ArrayList<>();
        for (Bullet b : bullets) {
            if (!b.isAlive()) {
                toRemove.add(b);
            }
        }
        bullets.removeAll(toRemove);
    }

    public synchronized void tryFire() {

        int activeBullets = 0;
        for (Bullet b : bullets) {
            if (b.isAlive()) activeBullets++;
        }

        if (activeBullets >= player.getMaxBullets()) return;

        int bx, by;
        int half = (48 - Bullet.SIZE) / 2;
        int tankSize = 48;
        Tank.Direction dir = player.getDirection();

        if (dir == Tank.Direction.UP) {
            bx = player.getX() + half;
            by = player.getY() - Bullet.SIZE;
        } else if (dir == Tank.Direction.DOWN) {
            bx = player.getX() + half;
            by = player.getY() + tankSize;
        } else if (dir == Tank.Direction.LEFT) {
            bx = player.getX() - Bullet.SIZE;
            by = player.getY() + half;
        } else { // RIGHT
            bx = player.getX() + tankSize;
            by = player.getY() + half;
        }

        bullets.add(new Bullet(bx, by, dir, player.getStars(), map));
    }

    private void trySpawnPowerUp() {
        // only one powerup on screen at a time
        if (!powerUps.isEmpty()) return;

        // collect all empty cells
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

        PowerUpType type = (random.nextBoolean()) ? PowerUpType.STAR : PowerUpType.LIFE;
        powerUps.add(new PowerUp(px, py, type));
    }

    private void checkPowerUpCollisions() {
        List<PowerUp> toRemove = new ArrayList<>();
        for (PowerUp pu : powerUps) {
            if (CollisionManager.overlaps(player, pu)) {
                if (pu.getType() == PowerUpType.STAR) {
                    player.addStar();
                } else if (pu.getType() == PowerUpType.LIFE) {
                    player.addLife();
                }
                toRemove.add(pu);
            }
        }
        powerUps.removeAll(toRemove);
    }

    public PlayerTank getPlayer() { return player; }
    public InputHandler getInputHandler() { return inputHandler; }
    public synchronized List<Bullet> getBullets() { return new ArrayList<>(bullets); }
    public List<PowerUp> getPowerUps() { return new ArrayList<>(powerUps); }
}
