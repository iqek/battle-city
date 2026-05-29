package ui;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import model.TileType;

public class Sprites {
    public static int TILE_SIZE = 8;
    public static final int SCALE = 3;
    public static final int DRAW_SIZE = TILE_SIZE * SCALE;

    private final BufferedImage sheet;

    public Sprites(String path) throws IOException{
        sheet = ImageIO.read(getClass().getResourceAsStream(path));
    }

    public BufferedImage getSprite(int col, int row){
        return sheet.getSubimage(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE );
    }

    public BufferedImage getLargeSprite(int col, int row) {
        return sheet.getSubimage(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE * 2, TILE_SIZE * 2);
    }

    // --- TERRAIN (8x8 sub-tiles, coords in 8px units) ---
    public static final int[] BRICK = {32, 0};
    public static final int[] STEEL = {32, 2};
    public static final int[] WATER = {34, 6};
    public static final int[] BUSH  = {34, 4};
    public static final int[] ICE   = {36, 4};

    // --- PLAYER TANK (drawn with getLargeSprite → returns 16x16) ---
    public static final int[] PLAYER_UP    = {0,  0};
    public static final int[] PLAYER_LEFT  = {4,  0};
    public static final int[] PLAYER_DOWN = {8,  0};
    public static final int[] PLAYER_RIGHT = {12, 0};

    public static final int[] ENEMY_UP    = {0,  8};
    public static final int[] ENEMY_LEFT  = {4,  8};
    public static final int[] ENEMY_DOWN  = {8,  8};
    public static final int[] ENEMY_RIGHT = {12, 8};

    // --- POWERUPS (also getLargeSprite) ---
    public static final int[] PU_SHIELD = {32, 14};
    public static final int[] PU_CLOCK  = {34, 14};
    public static final int[] PU_SHOVEL = {36, 14};
    public static final int[] PU_BOMB   = {40, 14};
    public static final int[] PU_STAR   = {38, 14};
    public static final int[] PU_TANK   = {42, 14};

    // --- EXPLOSIONS (also getLargeSprite) ---
    public static final int[] EXP_SMALL_F1 = {32, 16};
    public static final int[] EXP_SMALL_F2 = {34, 16};
    public static final int[] EXP_SMALL_F3 = {36, 16};
    public static final int[] EXP_BIG_F2   = {38, 16};
    public static final int[] EXP_BIG_F3   = {42, 16};

    public static int[] coordsForTile(TileType tile) {
        return switch (tile) {
            case BRICK -> BRICK;
            case STEEL -> STEEL;
            case WATER -> WATER;
            case BUSH -> BUSH;
            case ICE -> ICE;
            default -> null;
        };
    }
}
