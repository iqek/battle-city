package ui;

import controller.GameController;
import model.GameMap;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    private GameController gameController;
    private GameMap map;
    private Renderer renderer;
    private Sprites sprites;
    private String currentLevel = "";
    private int score = 0;

    public GamePanel(GameMap map) {
        this.map = map;

        try {
            sprites = new Sprites("/images/sprites.png");
        } catch (Exception e) {
            e.printStackTrace();
        }

        setPreferredSize(new Dimension(GameMap.COLS * Sprites.DRAW_SIZE + 192, GameMap.ROWS * Sprites.DRAW_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
    }

    public void setGameController(GameController gc) {
        this.gameController = gc;
        this.renderer = new Renderer(gc, map, sprites);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (renderer != null) renderer.render(g);
        drawHUD(g);
    }

    private void drawHUD(Graphics g) {
        int hudX = GameMap.COLS * Sprites.DRAW_SIZE;
        int panelWidth = 215;
        int panelHeight = GameMap.ROWS * Sprites.DRAW_SIZE;

        g.setColor(new Color(80, 80, 80));
        g.fillRect(hudX, 0, panelWidth, panelHeight);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("STAGE", hudX + 20, 60);
        g.setFont(new Font("Arial", Font.PLAIN, 13));
        g.drawString(currentLevel, hudX + 20, 80);

        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("SCORE", hudX + 20, 140);
        g.setFont(new Font("Arial", Font.PLAIN, 13));
        g.drawString(String.valueOf(score), hudX + 20, 160);

        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("LIVES", hudX + 20, 220);
        g.setFont(new Font("Arial", Font.PLAIN, 13));
        if (gameController != null) {
            g.drawString("<3 x" + gameController.getPlayer().getLives(), hudX + 20, 240);
        }
    }

    public void setCurrentLevel(String level) { this.currentLevel = level; }
    public void addScore(int points) { score += points; }
    public int  getScore(){ return score; }
    public void resetScore(){ score = 0; }
}
