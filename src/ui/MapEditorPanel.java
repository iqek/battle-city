package ui;

import core.TileType;
import core.GameMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MapEditorPanel extends JPanel {

    private static final int SCALE = 3;
    private static final int TILE_DRAW_SIZE = Sprites.TILE_SIZE * SCALE; //16px

    private GameMap map;
    private Sprites spriteSheet;
    private TileType selectedTile = TileType.EMPTY;
    private JTextField filenameField;

    public MapEditorPanel() {
        setLayout(new BorderLayout());

        map = new GameMap();

        try {
            spriteSheet = new Sprites("/images/sprites.png");
        } catch(IOException e){
            System.err.println("could not load sprites: " + e.getMessage());
        }

        add(createGrid(), BorderLayout.CENTER);
        add(createSideBar(), BorderLayout.EAST);
    }

    private JPanel createGrid(){
        JPanel grid = new JPanel(){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                drawMap(g);
            }

            @Override
            public Dimension getPreferredSize(){
                return new Dimension(GameMap.COLS * TILE_DRAW_SIZE, GameMap.ROWS * TILE_DRAW_SIZE);
            }
        };

        grid.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                paintTile(e, grid);
            }
        });

        grid.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                paintTile(e, grid);
            }
        });

        return grid;
    }

    private void drawMap(Graphics g){
        for(int r=0; r < GameMap.ROWS; r++){
            for(int c=0; c < GameMap.COLS; c++){
                TileType tile = map.getTile(r, c);
                BufferedImage sprite = getSpriteForTile(tile);

                int x = c * TILE_DRAW_SIZE;
                int y = r * TILE_DRAW_SIZE;

                if(sprite != null){
                    g.drawImage(sprite, x, y, TILE_DRAW_SIZE, TILE_DRAW_SIZE, null);
                }
                else{
                    g.setColor(Color.BLACK);
                    g.fillRect(x, y, TILE_DRAW_SIZE, TILE_DRAW_SIZE);
                }

                g.setColor(Color.DARK_GRAY);
                g.drawRect(x, y, TILE_DRAW_SIZE, TILE_DRAW_SIZE);
            }
        }
    }

    private void paintTile(MouseEvent e, JPanel grid){
        int col = e.getX() / TILE_DRAW_SIZE;
        int row = e.getY() / TILE_DRAW_SIZE;
        if(row >=0 && row<GameMap.ROWS && col >= 0 && col < GameMap.COLS){
            map.setTile(row, col, selectedTile);
            grid.repaint();
        }
    }

    private JPanel createSideBar(){
        JPanel bar = new JPanel(new GridLayout(0,2));
        bar.setPreferredSize(new Dimension(192, GameMap.ROWS * Sprites.DRAW_SIZE));

        for(TileType type : TileType.values()){
            BufferedImage img = getSpriteForTile(type);

            JButton btn;
            if(img!=null){
                Image scaled = img.getScaledInstance(40, 40, Image.SCALE_FAST);
                btn = new JButton(new ImageIcon(scaled));
            } else{
                btn = new JButton("EMPTY");
            }

            btn.addActionListener(e -> selectedTile = type);
            bar.add(btn);
            btn.setPreferredSize(new Dimension(40, 40));
        }

        filenameField = new JTextField("mymap", 10);
        bar.add(new JLabel("Filename:"));
        bar.add(filenameField);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> saveMap());
        bar.add(saveBtn);

        JButton loadBtn = new JButton("Load");
        loadBtn.addActionListener(e -> loadMap());
        bar.add(loadBtn);

        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(e -> {
            map.fill(TileType.EMPTY);
            repaint();
        });

        bar.add(clearBtn);

        return bar;
    }

    private void saveMap(){
        String filename = filenameField.getText().trim();
        if (filename.isEmpty()) filename = "mymap";
        try {
            map.saveToFile("resources/maps/" + filename + ".txt");
            JOptionPane.showMessageDialog(this, "Map saved");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Save failed: " + e.getMessage());
        }
    }

    private void loadMap(){
        String filename = filenameField.getText().trim();
        try {
            map.loadFromFile("resources/maps/" + filename + ".txt");
            repaint();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Load failed: " + e.getMessage());
        }
    }

    private BufferedImage getSpriteForTile(TileType type) {
        return spriteSheet.getSpriteForTile(type);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(GameMap.COLS * Sprites.DRAW_SIZE + 192, GameMap.ROWS * Sprites.DRAW_SIZE);
    }
}
