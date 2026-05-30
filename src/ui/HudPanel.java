package ui;

import javax.swing.*;
import java.awt.*;

// Sidebar panel showing stage, score, lives, and the pause button
public class HudPanel extends JPanel {

    private static final int WIDTH = 180;
    private static final Color BACKGROUND = new Color(80, 80, 80);
    private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 14);
    private static final Font VALUE_FONT = new Font("Arial", Font.PLAIN, 13);

    private final JLabel stageValue = new JLabel("-");
    private final JLabel scoreValue = new JLabel("0");
    private final JLabel livesValue = new JLabel("-");
    private final JButton pauseButton = new JButton("PAUSE");

    public HudPanel() {
        setBackground(BACKGROUND);
        setPreferredSize(new Dimension(WIDTH, 0)); // height stretches to match game canvas
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(Box.createVerticalStrut(40));
        add(makeSection("STAGE", stageValue));
        add(Box.createVerticalStrut(40));
        add(makeSection("SCORE", scoreValue));
        add(Box.createVerticalStrut(40));
        add(makeSection("LIVES", livesValue));
        add(Box.createVerticalStrut(40));
        add(makePauseButton());
        add(Box.createVerticalGlue());

        pauseButton.setFocusable(false); // don't steal keyboard focus from the game
    }

    private JPanel makeSection(String title, JLabel valueLabel) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(BACKGROUND);
        section.setAlignmentX(LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(LABEL_FONT);
        titleLabel.setForeground(Color.WHITE);

        valueLabel.setFont(VALUE_FONT);
        valueLabel.setForeground(Color.WHITE);

        section.add(titleLabel);
        section.add(valueLabel);
        return section;
    }

    private JPanel makePauseButton() {
        pauseButton.setBackground(Color.DARK_GRAY);
        pauseButton.setForeground(Color.WHITE);
        pauseButton.setMaximumSize(new Dimension(140, 35));
        pauseButton.setAlignmentX(LEFT_ALIGNMENT);

        JPanel wrapper = new JPanel();
        wrapper.setBackground(BACKGROUND);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));
        wrapper.setAlignmentX(LEFT_ALIGNMENT);
        wrapper.add(pauseButton);
        return wrapper;
    }

    // Called every frame from the canvas paintComponent (runs on EDT)
    public void update(String level, int score, int lives) {
        stageValue.setText(level.isEmpty() ? "-" : level);
        scoreValue.setText(String.valueOf(score));
        livesValue.setText("<3 x" + lives);
    }

    public JButton getPauseButton() { return pauseButton; }
}
