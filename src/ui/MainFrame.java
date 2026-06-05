package ui;

import javax.swing.*;

import controller.GameController;
import controller.GameEndListener;
import core.*;

public class MainFrame extends JFrame implements GameEndListener{

    private GamePanel gamePanel;
    private GameController gameController;
    private MapEditorPanel mapEditorPanel;
    private LevelSelectPanel levelSelectPanel;
    private GameMap gameMap;
    private HelpPanel helpPanel;
    private HighScorePanel highScorePanel;
    private TitlePanel titlePanel;
    private OptionsPanel optionsPanel;

    public MainFrame() {
        setTitle("battlecity");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        gameMap = new GameMap();
        gamePanel = new GamePanel(gameMap);
        gameController = new GameController(gameMap, gamePanel);
        gamePanel.setGameController(gameController);

        mapEditorPanel = new MapEditorPanel();
        levelSelectPanel = new LevelSelectPanel(this);
        helpPanel = new HelpPanel();
        highScorePanel = new HighScorePanel();
        gameController.setGameEndListener(this);

        optionsPanel = new OptionsPanel();
        optionsPanel.setOnBack(() -> showPanel(titlePanel));

        titlePanel = new TitlePanel();
        titlePanel.setOnNewGame(() -> showPanel(levelSelectPanel));
        titlePanel.setOnConstruction(() -> showPanel(mapEditorPanel));
        titlePanel.setOnOptions(() -> showPanel(optionsPanel));

        setContentPane(mapEditorPanel);
        setJMenuBar(createMenuBar());

        pack();
        setLocationRelativeTo(null);
    }

    private void showPanel(JPanel panel) {
        setContentPane(panel);
        revalidate();
        repaint();
        panel.requestFocusInWindow();
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu gameMenu = new JMenu("Game");
        gameMenu.add(new JMenuItem("Main Menu")).addActionListener(e -> showPanel(titlePanel));
        JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.addActionListener(e -> {
            showPanel(levelSelectPanel);
        });
        gameMenu.add(newGameItem);


        gameMenu.addSeparator();
        gameMenu.add(new JMenuItem("Exit")).addActionListener(e -> System.exit(0));

        JMenu editMenu = new JMenu("Editor");
        editMenu.add(new JMenuItem("Map Editor")).addActionListener(e -> {
            showPanel(mapEditorPanel);
        });

        JMenu scoresMenu = new JMenu("Scores");
        scoresMenu.add(new JMenuItem("High Scores")).addActionListener(e -> {
            showPanel(highScorePanel);
        });

        JMenu helpMenu = new JMenu("Help");
        helpMenu.add(new JMenuItem("Help")).addActionListener(e -> {
            showPanel(helpPanel);
        });
        helpMenu.add(new JMenuItem("About")).addActionListener(e-> {
            JOptionPane.showMessageDialog(null, "İpek Çelik \n 20240702019 \n ipek.celik4@std.yeditepe.edu.tr", "About", JOptionPane.INFORMATION_MESSAGE);
        });

        menuBar.add(gameMenu);
        menuBar.add(editMenu);
        menuBar.add(scoresMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }

    public void startGame(String mapName) {
        try {
            gameController.stop();
            gamePanel.setCurrentLevel(mapName);
            gameController.setDifficulty(optionsPanel.getSelectedDifficulty());
            gamePanel.setCurrentLevel(mapName);
            gameMap.loadFromFile("resources/maps/" + mapName + ".txt");
            showPanel(gamePanel);
            gameController.start();
        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, "Could not load map: " + mapName);
        }
    }

    public void onGameEnd(boolean won, int score) {
        String message = won ? "You win! Score: " + score : "Game Over! Score: " + score;
        String name = JOptionPane.showInputDialog(this, message + "\nEnter your name:");

        if(name != null && !name.isBlank()) {
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            String date = now.toLocalDate().toString();
            String time = String.format("%02d:%02d", now.getHour(), now.getMinute());
            HighScoreManager.save(new HighScore(name, score, date, time, gamePanel.getCurrentLevel()));
        }

        showPanel(highScorePanel);
    }

}
