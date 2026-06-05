package ui;

import core.Difficulty;
import core.GameMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class OptionsPanel extends JPanel{
    private Difficulty selectedDifficulty = Difficulty.NORMAL;
    private Runnable onBack;

    public OptionsPanel() {
        setBackground(Color.BLACK);
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                Difficulty[] values = Difficulty.values();
                int idx = selectedDifficulty.ordinal();
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP    -> { selectedDifficulty = values[(idx - 1 + values.length) % values.length]; repaint(); }
                    case KeyEvent.VK_DOWN  -> { selectedDifficulty = values[(idx + 1) % values.length]; repaint(); }
                    case KeyEvent.VK_ENTER, KeyEvent.VK_ESCAPE -> { if (onBack != null) onBack.run(); }
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Difficulty[] values = Difficulty.values();
                int startY = difficultyStartY();
                for (int i = 0; i < values.length; i++) {
                    int y = startY + i * 50;
                    if (e.getY() >= y - 16 && e.getY() <= y + 8) {
                        selectedDifficulty = values[i];
                        repaint();
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

        g.setFont(new Font("Arial", Font.BOLD, 28));
        FontMetrics titleFm = g.getFontMetrics();
        g.setColor(Color.WHITE);
        g.drawString("OPTIONS", cx - titleFm.stringWidth("OPTIONS") / 2, getHeight() / 4);

        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.setColor(new Color(180, 180, 180));
        g.drawString("DIFFICULTY", cx - g.getFontMetrics().stringWidth("DIFFICULTY") / 2, getHeight() / 4 + 50);

        g.setFont(new Font("Arial", Font.BOLD, 22));
        FontMetrics fm = g.getFontMetrics();
        Difficulty[] values = Difficulty.values();
        int startY = difficultyStartY();

        for (int i = 0; i < values.length; i++) {
            int y = startY + i * 50;
            g.setColor(values[i] == selectedDifficulty ? Color.YELLOW : Color.WHITE);
            g.drawString(values[i].name(), cx - fm.stringWidth(values[i].name()) / 2, y);
        }

        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.setColor(Color.GRAY);
        String hint = "ENTER / ESC to go back";
        g.drawString(hint, cx - g.getFontMetrics().stringWidth(hint) / 2, getHeight() - 40);
    }

    private int difficultyStartY() { return getHeight() / 2; }

    public Difficulty getSelectedDifficulty() { return selectedDifficulty; }
    public void setOnBack(Runnable r)          { this.onBack = r; }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(
                GameMap.COLS * Sprites.DRAW_SIZE + 192,
                GameMap.ROWS * Sprites.DRAW_SIZE);
    }
}
