package ui;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import core.TileType;

public class Sprites {
    public static final int TILE_SIZE = 8; // sprite sheet tile size
    public static final int SCALE = 3;
    public static final int DRAW_SIZE = TILE_SIZE * SCALE; // 24px per tile on screen

    private final BufferedImage sheet;

    public Sprites(String path) throws IOException{
        sheet = ImageIO.read(getClass().getResourceAsStream(path));
    }


    //helpers
    private BufferedImage getSprite(int col, int row){
        return sheet.getSubimage(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    private BufferedImage getLargeSprite(int col, int row){
        return sheet.getSubimage(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE * 2, TILE_SIZE * 2);
    }


    //tiles
    public BufferedImage getBrick(){ return getSprite(32, 0); }
    public BufferedImage getSteel(){ return getSprite(32, 2); }
    public BufferedImage getWater(){ return getSprite(34, 6); }
    public BufferedImage getBush(){ return getSprite(34, 4); }
    public BufferedImage getIce(){ return getSprite(36, 4); }

    //player
    public BufferedImage getPlayerUp(){ return getLargeSprite( 0, 0); }
    public BufferedImage getPlayerDown(){ return getLargeSprite( 8, 0); }
    public BufferedImage getPlayerLeft(){ return getLargeSprite( 4, 0); }
    public BufferedImage getPlayerRight(){ return getLargeSprite(12, 0); }

    //enemy
    public BufferedImage getEnemyUp(){ return getLargeSprite(18, 2); }
    public BufferedImage getEnemyDown(){ return getLargeSprite(26, 2); }
    public BufferedImage getEnemyLeft(){ return getLargeSprite(22, 2); }
    public BufferedImage getEnemyRight(){ return getLargeSprite(30, 2); }

    //power ups
    public BufferedImage getPuStar(){ return getLargeSprite(38, 14); }
    public BufferedImage getPuTank(){ return getLargeSprite(42, 14); }
    public BufferedImage getPuShield(){ return getLargeSprite(32, 14); }
    public BufferedImage getPuClock(){ return getLargeSprite(34, 14); }
    public BufferedImage getPuShovel(){ return getLargeSprite(36, 14); }
    public BufferedImage getPuBomb(){ return getLargeSprite(40, 14); }

    //eagle
    public BufferedImage getEagle(){ return getLargeSprite(38, 4); }
    public BufferedImage getEagleDead(){ return getLargeSprite(40, 4); }

    //explosions
    public BufferedImage getExpSmallF1(){ return getLargeSprite(32, 16); }
    public BufferedImage getExpSmallF2(){ return getLargeSprite(34, 16); }
    public BufferedImage getExpSmallF3(){ return getLargeSprite(36, 16); }
    public BufferedImage getExpBigF2(){ return getLargeSprite(38, 16); }
    public BufferedImage getExpBigF3(){ return getLargeSprite(42, 16); }

    public BufferedImage getSpriteForTile(TileType tile) {
        return switch (tile) {
            case BRICK -> getBrick();
            case STEEL -> getSteel();
            case WATER -> getWater();
            case BUSH -> getBush();
            case ICE -> getIce();
            default -> null;
        };
    }
}
