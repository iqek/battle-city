package model;

public enum TileType {
    EMPTY, BRICK, STEEL, WATER, BUSH, ICE;

    public static TileType fromInt(int i){
        return values()[i];
    }
}
