package core.entities;

public class PowerUp extends Entity {

    public static final int SIZE = Tank.SIZE;

    private final PowerUpType type;

    public PowerUp(int x, int y, PowerUpType type) {
        super(x, y, SIZE, SIZE);
        this.type = type;
    }

    @Override
    public void update() {}

    public PowerUpType getType() { return type; }
}

