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
        setLayout(null);

        JButton backBtn = new JButton("<-");
        backBtn.setFont(Fonts.get(10f));
        backBtn.setBackground(Color.DARK_GRAY);
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setBounds(20, GameMap.ROWS * Sprites.DRAW_SIZE - 60, 100, 35);
        backBtn.addActionListener(e -> { if(onBack != null) onBack.run(); });
        add(backBtn);

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

        g.setFont(Fonts.get(16f));
        FontMetrics titleFm = g.getFontMetrics();
        g.setColor(Color.WHITE);
        g.drawString("OPTIONS", cx - titleFm.stringWidth("OPTIONS") / 2, getHeight() / 4);

        g.setFont(Fonts.get(10f));
        g.setColor(new Color(180, 180, 180));
        g.drawString("DIFFICULTY", cx - g.getFontMetrics().stringWidth("DIFFICULTY") / 2, getHeight() / 4 + 50);

        g.setFont(Fonts.get(12f));
        FontMetrics fm = g.getFontMetrics();
        Difficulty[] values = Difficulty.values();
        int startY = difficultyStartY();

        for (int i = 0; i < values.length; i++) {
            int y = startY + i * 50;
            g.setColor(values[i] == selectedDifficulty ? Color.YELLOW : Color.WHITE);
            g.drawString(values[i].name(), cx - fm.stringWidth(values[i].name()) / 2, y);
        }

    }

    private int difficultyStartY() { return getHeight() / 2; }

    public Difficulty getSelectedDifficulty() { return selectedDifficulty; }
    public void setOnBack(Runnable r) { this.onBack = r; }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(
                GameMap.COLS * Sprites.DRAW_SIZE + 192,
                GameMap.ROWS * Sprites.DRAW_SIZE);
    }
}
