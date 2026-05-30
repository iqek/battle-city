package ui;

import model.HighScore;
import model.HighScoreManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HighScorePanel extends JPanel {

    private JPanel listPanel;

    public HighScorePanel() {
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("HIGH SCORES", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(new Color(255, 215, 0));
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
            empty.setFont(new Font("Arial", Font.PLAIN, 14));
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
            lbl.setFont(new Font("Arial", Font.PLAIN, 14));
            lbl.setForeground(i == 0 ? new Color(255, 215, 0) : Color.WHITE); // gold for #1
            lbl.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
            listPanel.add(lbl);
        }

        listPanel.revalidate();
        listPanel.repaint();
    }
}
