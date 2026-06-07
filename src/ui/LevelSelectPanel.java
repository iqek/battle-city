package ui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class LevelSelectPanel extends JPanel {

    private MainFrame mainFrame;
    private Runnable onBack;
    private JPanel wrapper;

    public LevelSelectPanel(MainFrame mainFrame){
        this.mainFrame = mainFrame;
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("SELECT STAGE", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(Fonts.get(14f));
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.setBackground(Color.BLACK);
        add(wrapper, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        southPanel.setBackground(Color.BLACK);
        southPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));
        JButton backBtn = new JButton("<-");
        backBtn.setFont(Fonts.get(10f));
        backBtn.setBackground(Color.DARK_GRAY);
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.addActionListener(e -> { if(onBack != null) onBack.run(); });
        southPanel.add(backBtn);
        add(southPanel, BorderLayout.SOUTH);
    }

    public void refresh() {
        wrapper.removeAll();
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 0, 12));
        buttonPanel.setBackground(Color.BLACK);
        for (String mapName : getMapFiles()) {
            JButton btn = new JButton(mapName);
            btn.setBackground(Color.DARK_GRAY);
            btn.setForeground(Color.WHITE);
            btn.setFont(Fonts.get(11f));
            btn.setFocusPainted(false);
            btn.setPreferredSize(new Dimension(300, 44));
            btn.addActionListener(e -> mainFrame.startGame(mapName));
            buttonPanel.add(btn);
        }
        wrapper.add(buttonPanel);
        wrapper.revalidate();
        wrapper.repaint();
    }

    public void setOnBack(Runnable r) { this.onBack = r; }

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
