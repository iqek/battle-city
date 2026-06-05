package ui;

import core.GameMap;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class TitlePanel extends JPanel {

    private static final String[] OPTIONS = {"1 PLAYER", "CONSTRUCTION", "OPTIONS"};
    private static final int LOGO_TARGET_WIDTH = 300; // max width the logo is scaled to

    private int selectedIndex = 0;
    private BufferedImage logo;

    private Runnable onNewGame;
    private Runnable onConstruction;
    private Runnable onOptions;

    public TitlePanel() {
        setBackground(Color.BLACK);
        setFocusable(true);

        try {
            logo = ImageIO.read(getClass().getResourceAsStream("/images/title.png"));
        } catch (Exception e) {
            logo = null;
        }

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP    -> { selectedIndex = (selectedIndex - 1 + OPTIONS.length) % OPTIONS.length; repaint(); }
                    case KeyEvent.VK_DOWN  -> { selectedIndex = (selectedIndex + 1) % OPTIONS.length; repaint(); }
                    case KeyEvent.VK_ENTER -> confirm();
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int startY = optionStartY();
                for (int i = 0; i < OPTIONS.length; i++) {
                    int y = startY + i * 50;
                    if (e.getY() >= y - 16 && e.getY() <= y + 8) {
                        selectedIndex = i;
                        confirm();
                        return;
                    }
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int cx = getWidth() / 2;

        if (logo != null) {
            int w = LOGO_TARGET_WIDTH;
            int h = logo.getHeight() * w / logo.getWidth(); // preserve aspect ratio
            g.drawImage(logo, cx - w / 2, getHeight() / 4 - h / 2, w, h, null);
        }

        g.setFont(new Font("Arial", Font.BOLD, 22));
        FontMetrics fm = g.getFontMetrics();
        int startY = optionStartY();

        for (int i = 0; i < OPTIONS.length; i++) {
            int y = startY + i * 50;
            g.setColor(i == selectedIndex ? Color.YELLOW : Color.WHITE);
            g.drawString(OPTIONS[i], cx - fm.stringWidth(OPTIONS[i]) / 2, y);
        }
    }

    private int optionStartY() { return getHeight() / 2 + 30; }

    private void confirm() {
        if      (selectedIndex == 0 && onNewGame      != null) onNewGame.run();
        else if (selectedIndex == 1 && onConstruction != null) onConstruction.run();
        else if (selectedIndex == 2 && onOptions      != null) onOptions.run();
    }

    public void setOnNewGame(Runnable r)       { this.onNewGame = r; }
    public void setOnConstruction(Runnable r)  { this.onConstruction = r; }
    public void setOnOptions(Runnable r)       { this.onOptions = r; }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(
                GameMap.COLS * Sprites.DRAW_SIZE + 192,
                GameMap.ROWS * Sprites.DRAW_SIZE);
    }
}