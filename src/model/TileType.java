package model;

public enum TileType {
    EMPTY, BRICK, STEEL, WATER, BUSH, ICE;

    // Whether tanks and bullets cannot pass through this tile
    public boolean blocksMovement() {
        return this == BRICK || this == STEEL || this == WATER;
    }

    // How much health this tile starts with (0 means indestructible or empty)
    public int initialHealth() {
        return (this == BRICK || this == STEEL) ? 4 : 0;
    }

    public static TileType fromInt(int i) {
        return values()[i];
    }
}
