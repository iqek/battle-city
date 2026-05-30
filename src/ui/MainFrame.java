package ui;

import controller.GameController;
import controller.GameEndListener;
import model.GameMap;
import model.HighScore;
import model.HighScoreManager;

import javax.swing.*;

public class MainFrame extends JFrame implements GameEndListener {

    private GamePanel gamePanel;
    private GameController gameController;
    private MapEditorPanel mapEditorPanel;
    private LevelSelectPanel levelSelectPanel;
    private GameMap gameMap;
    private HelpPanel helpPanel;
    private HighScorePanel highScorePanel;

    public MainFrame(){
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
        setContentPane(mapEditorPanel);
        setJMenuBar(createMenuBar());

        pack();
        setLocationRelativeTo(null);

    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu gameMenu = new JMenu("Game");
        JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.addActionListener(e -> {
            setContentPane(levelSelectPanel);
            revalidate();
            repaint();
        });
        gameMenu.add(newGameItem);


        gameMenu.addSeparator();
        gameMenu.add(new JMenuItem("Exit")).addActionListener(e -> System.exit(0));

        JMenu editMenu = new JMenu("Editor");
        editMenu.add(new JMenuItem("Map Editor")).addActionListener(e -> {
            setContentPane(mapEditorPanel);
            revalidate();
            repaint();
        });

        JMenu scoresMenu = new JMenu("Scores");
        scoresMenu.add(new JMenuItem("High Scores")).addActionListener(e -> {
            highScorePanel.refresh();
            setContentPane(highScorePanel);
            revalidate();
            repaint();
        });

        JMenu helpMenu = new JMenu("Help");
        helpMenu.add(new JMenuItem("Help")).addActionListener(e -> {
            setContentPane(helpPanel);
            revalidate();
            repaint();
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
            gameMap.loadFromFile("resources/maps/" + mapName + ".txt");
            setContentPane(gamePanel);
            revalidate();
            repaint();
            gamePanel.requestFocusInWindow();
            gameController.start();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Could not load map: " + mapName);
        }
    }

    public void onGameEnd(boolean won, int score) {
        String message = won ? "You win! Score: " + score : "Game Over! Score: " + score;
        String name = JOptionPane.showInputDialog(this, message + "\nEnter your name:");

        if (name != null && !name.isBlank()) {
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            String date = now.toLocalDate().toString();
            String time = String.format("%02d:%02d", now.getHour(), now.getMinute());
            HighScoreManager.save(new HighScore(name, score, date, time, gamePanel.getCurrentLevel()));
        }

        highScorePanel.refresh();
        setContentPane(highScorePanel);
        revalidate();
        repaint();
    }
}
