package ui;

import javax.swing.*;
import java.awt.*;

public class HelpPanel extends JPanel {

    public HelpPanel() {
        setBackground(Color.BLACK);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JLabel title = new JLabel("HOW TO PLAY");
        title.setFont(Fonts.get(14f));
        title.setForeground(new Color(188, 67, 0));
        title.setAlignmentX(CENTER_ALIGNMENT);
        add(title);
        add(Box.createRigidArea(new Dimension(0, 30)));

        String[] lines = {
            "CONTROLS",
            "  W A S D  -  move",
            "  SPACE    -  fire",
            "",
            "TILES",
            "  Brick  -  destructible. 1 dmg (0-1 stars), 4 dmg (2-3 stars)",
            "  Steel  -  indestructible unless you have 3 stars",
            "  Water  -  blocks tanks and bullets",
            "  Bush   -  tanks pass through and are hidden",
            "  Ice    -  tanks pass through",
            "",
            "POWER-UPS",
            "  Star   -  raises star level (max 3)",
            "  Life   -  gives one extra life",
            "  Bomb   -  destroys all enemies on screen",
            "  Clock  -  freezes all enemies for 5 seconds",
            "  Shovel -  steel walls around base for 10 seconds",
            "  Shield -  invincibility for 5 seconds",
            "",
            "STAR LEVELS",
            "  0-1  1 bullet   1 damage",
            "  2    2 bullets  4 damage",
            "  3    2 bullets  4 damage  breaks steel",
        };

        for (String line : lines) {
            boolean isHeader = line.equals("CONTROLS") || line.equals("TILES") || line.equals("POWER-UPS") || line.equals("STAR LEVELS");
            JLabel lbl = new JLabel(line.isEmpty() ? " " : line);
            lbl.setFont(isHeader ? Fonts.get(10f) : Fonts.getOperator(16f));
            lbl.setForeground(isHeader ? new Color(188, 67, 0) : Color.WHITE);
            lbl.setAlignmentX(LEFT_ALIGNMENT);
            add(lbl);
            add(Box.createRigidArea(new Dimension(0, 5)));
        }
    }
}
