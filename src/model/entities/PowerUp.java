package model.entities;

public class PowerUp extends Entity {

    public static final int SIZE = Tank.SIZE;  // drawn at same size as the tank

    private final PowerUpType type;

    public PowerUp(int x, int y, PowerUpType type) {
        super(x, y, SIZE, SIZE);
        this.type = type;
    }

    @Override
    public void update() {
        // powerups don't move
    }

    public PowerUpType getType() { return type; }
}
