package ui;

import core.HighScore;
import core.HighScoreManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HighScorePanel extends JPanel {

    private JPanel listPanel;

    public HighScorePanel() {
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("HIGH SCORES", SwingConstants.CENTER);
        title.setFont(Fonts.get(14f));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(24, 0, 16, 0));
        add(title, BorderLayout.NORTH);

        listPanel = new JPanel();
        listPanel.setBackground(Color.BLACK);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(BorderFactory.createEmptyBorder(10, 80, 40, 80));
        add(listPanel, BorderLayout.CENTER);

        refresh();
    }

    public void refresh() {
        listPanel.removeAll();
        List<HighScore> scores = HighScoreManager.getTopTen();

        if (scores.isEmpty()) {
            JLabel empty = new JLabel("No scores yet!");
            empty.setForeground(Color.GRAY);
            empty.setFont(Fonts.getOperator(16f));
            empty.setAlignmentX(CENTER_ALIGNMENT);
            listPanel.add(empty);
        }

        for (int i = 0; i < scores.size(); i++) {
            HighScore hs = scores.get(i);
            String text = (i + 1) + ".  " + hs.getName()
                    + "  —  " + hs.getScore() + " pts"
                    + "  |  " + hs.getMap()
                    + "  |  " + hs.getDate() + "  " + hs.getTime();
            JLabel lbl = new JLabel(text);
            lbl.setFont(Fonts.getOperator(16f));
            lbl.setForeground(Color.WHITE); // gold for #1
            lbl.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
            listPanel.add(lbl);
        }

        listPanel.revalidate();
        listPanel.repaint();
    }
}
