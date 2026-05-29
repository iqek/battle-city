package ui;

import controller.GameController;
import model.GameMap;

import javax.swing.*;

public class MainFrame extends JFrame {

    private GamePanel gamePanel;
    private GameController gameController;
    private MapEditorPanel mapEditorPanel;
    private LevelSelectPanel levelSelectPanel;
    private GameMap gameMap;
    private HelpPanel helpPanel;

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
        scoresMenu.add(new JMenuItem("High Scores"));

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
}
