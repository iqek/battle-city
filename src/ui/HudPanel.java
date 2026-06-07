package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BorderFactory;

public class HudPanel extends JPanel {

    private static final Color BACKGROUND = new Color(80, 80, 80);
    private static final Font HUD_FONT = Fonts.get(10f);

    private final JLabel stageRow  = new JLabel();
    private final JLabel scoreRow  = new JLabel();
    private final JLabel livesRow  = new JLabel();
    private final JButton pauseButton = new JButton("PAUSE");

    public HudPanel() {
        setBackground(BACKGROUND);
        setPreferredSize(new Dimension(192, 0));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 8));

        add(Box.createVerticalStrut(50));
        add(makeRow(stageRow));
        add(Box.createVerticalStrut(30));
        add(makeRow(scoreRow));
        add(Box.createVerticalStrut(30));
        add(makeRow(livesRow));
        add(Box.createVerticalStrut(40));
        add(makePauseButton());
        add(Box.createVerticalGlue());

        pauseButton.setFocusable(false);
    }

    private JLabel makeRow(JLabel lbl) {
        lbl.setFont(HUD_FONT);
        lbl.setForeground(Color.WHITE);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        return lbl;
    }

    private JPanel makePauseButton() {
        pauseButton.setFont(Fonts.get(9f));
        pauseButton.setBackground(Color.DARK_GRAY);
        pauseButton.setForeground(Color.WHITE);
        pauseButton.setFocusPainted(false);
        pauseButton.setMaximumSize(new Dimension(160, 35));
        pauseButton.setAlignmentX(LEFT_ALIGNMENT);

        JPanel wrapper = new JPanel();
        wrapper.setBackground(BACKGROUND);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));
        wrapper.setAlignmentX(LEFT_ALIGNMENT);
        wrapper.add(pauseButton);
        return wrapper;
    }

    public void update(String level, int score, int lives) {
        stageRow.setText("STAGE  " + (level == null || level.isEmpty() ? "-" : level));
        scoreRow.setText("SCORE  " + score);
        livesRow.setText("LIVES  " + lives + "x <3");
    }

    public JButton getPauseButton() { return pauseButton; }
}
