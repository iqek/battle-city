package ui;

import javax.swing.*;
import java.awt.*;

public class HelpPanel extends JPanel {

    public HelpPanel() {
        setBackground(Color.BLACK);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JLabel title = new JLabel("HOW TO PLAY");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(new Color(188, 67, 0));
        title.setAlignmentX(CENTER_ALIGNMENT);
        add(title);

        add(Box.createRigidArea(new Dimension(0, 30)));

        String[] lines = {
            "W / A / S / D   —   Move",
            "SPACE           —   Fire",
            "",
            "── TILES ──────────────────────────────",
            "Brick  :  has 4 health. each bullet does 1 damage (0-1 stars)",
            "          or 4 damage (2-3 stars). destroyed when health hits 0.",
            "Steel  :  has 4 health but is invincible unless you have 3 stars.",
            "          at 3 stars, one shot destroys it.",
            "Water  :  tanks cannot pass through.",
            "Bush   :  tanks and bullets pass through, but the tank is hidden.",
            "",
            "── POWERUPS ────────────────────────────",
            "Star   :  increases your star level (max 3).",
            "Life   :  gives you one extra life.",
            "More powerups coming soon!",
            "",
            "── STAR LEVELS ─────────────────────────",
            "0-1 stars  :  1 bullet on screen, 1 damage per hit",
            "2   stars  :  2 bullets on screen, 4 damage per hit",
            "3   stars  :  2 bullets on screen, 4 damage per hit, breaks steel",
            "",
            "── TANKS ───────────────────────────────",
            "Player     :  starts with 3 lives. getting hit loses a life.",
            "Enemies    :  basic enemies take 1 hit. armored ones take more.",
        };

        for (String line : lines) {
            JLabel lbl = new JLabel(line.isEmpty() ? " " : line);
            lbl.setFont(new Font("Arial", Font.PLAIN, 14));
            lbl.setForeground(Color.WHITE);
            lbl.setAlignmentX(LEFT_ALIGNMENT);
            add(lbl);
            add(Box.createRigidArea(new Dimension(0, 4)));
        }
    }
}
