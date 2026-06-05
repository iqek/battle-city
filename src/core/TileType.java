package core;

public enum TileType {
    EMPTY, BRICK, STEEL, WATER, BUSH, ICE;

    public boolean blocksMovement() {
        return this == BRICK || this == STEEL || this == WATER;
    }

    public int initialHealth() {
        if(this == BRICK || this == STEEL) return 4;
        return 0;
    }

    public static TileType fromInt(int i) {
        return values()[i];
    }
}
