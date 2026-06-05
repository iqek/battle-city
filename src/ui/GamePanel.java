package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JPanel;

import controller.GameController;
import core.GameMap;

public class GamePanel extends JPanel{

    private final GameMap map;
    private final HudPanel hudPanel;
    private final Sprites sprites;
    private GameController gameController;
    private Renderer renderer;
    private String currentLevel;

    public GamePanel(GameMap map) {
        this.map= map;
        this.hudPanel = new HudPanel();

        try {
            sprites = new Sprites("/images/sprites.png");
        } catch(Exception e) {
            throw new RuntimeException("could not load sprites", e);
        }

        setLayout(new BorderLayout());
        add(createCanvas(), BorderLayout.CENTER);
        add(hudPanel, BorderLayout.EAST);
    }

    private JPanel createCanvas() {
        JPanel canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if(renderer != null) renderer.render(g);
                if(gameController!= null) {
                    hudPanel.update(currentLevel, gameController.getScore(), gameController.getPlayer().getLives());
                }
            }
        };
        canvas.setBackground(Color.BLACK);
        canvas.setPreferredSize(new Dimension(GameMap.COLS * Sprites.DRAW_SIZE, GameMap.ROWS * Sprites.DRAW_SIZE));
        return canvas;
    }

    public void setGameController(GameController gc) {
        this.gameController = gc;
        this.renderer = new Renderer(gc, map, sprites);

        JButton pauseButton = hudPanel.getPauseButton();
        pauseButton.addActionListener(e -> {
            gameController.togglePause();
            pauseButton.setText(gameController.isPaused() ? "RESUME" : "PAUSE");
            this.requestFocusInWindow();
        });
    }

    public String getCurrentLevel() {return currentLevel;}
    public void setCurrentLevel(String level) {currentLevel = level;}
}
