package ui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class LevelSelectPanel extends JPanel{

    private MainFrame mainFrame;

    public LevelSelectPanel(MainFrame mainFrame){
        this.mainFrame = mainFrame;
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("SELECT STAGE", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 0, 10));
        buttonPanel.setBackground(Color.BLACK);

        List<String> mapFiles = getMapFiles();

        for (String mapName : mapFiles) {
            JButton btn = new JButton(mapName);
            btn.setBackground(Color.DARK_GRAY);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.addActionListener(e -> mainFrame.startGame(mapName));
            buttonPanel.add(btn);
        }

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.setBackground(Color.BLACK);
        wrapper.add(buttonPanel);
        add(wrapper, BorderLayout.CENTER);
    }

    private List<String> getMapFiles(){
        List<String> names = new ArrayList<>();
        File mapsDir = new File("resources/maps");
        if (mapsDir.exists()) {
            File[] files = mapsDir.listFiles((dir, name) -> name.endsWith(".txt"));
            if (files != null) {
                for (File f : files) {
                    names.add(f.getName().replace(".txt", ""));
                }
            }
        }
        return names;
    }
}
