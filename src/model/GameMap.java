package model;

import java.io.*;
import java.util.Scanner;

public class GameMap {

    public static final int ROWS = 26;
    public static final int COLS = 26;

    public static final int CELL_PX = GameConstants.CELL_SIZE;

    private TileType[][] grid;
    private int[][] health;

    public GameMap(){
        grid = new TileType[ROWS][COLS];
        health = new int[ROWS][COLS];
        fill(TileType.EMPTY);
    }

    public void fill(TileType type){
        for(int r = 0; r < ROWS; r++){
            for(int c =0; c< COLS; c++) {
                grid[r][c] = type;
            }
        }
    }

    public TileType getTile(int row, int col){ return grid[row][col]; }

    public void setTile(int row, int col, TileType type) {
        grid[row][col] = type;
        health[row][col] = type.initialHealth();
    }

    public void saveToFile(String path) throws IOException{
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        for(int r=0; r < ROWS; r++){
            for(int c =0; c< COLS; c++) {
                writer.write(grid[r][c].ordinal() + (c < COLS - 1 ? " " : ""));
            }
            writer.newLine();
        }
        writer.close();
    }

    public void loadFromFile(String path) throws IOException {
        Scanner sc = new Scanner(new File(path));
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++)
                setTile(r, c, TileType.fromInt(sc.nextInt())); // setTile so health initializes too
        sc.close();
    }

    public void applyDamage(int row, int col, int damage){
        health[row][col] -= damage;
        if(health[row][col] <= 0){
            grid[row][col] = TileType.EMPTY;
            health[row][col] = 0;
        }
    }
}
