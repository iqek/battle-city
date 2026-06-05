package core.entities;

public class Eagle extends Entity{

    public static final int X = 288;
    public static final int Y = 576;
    private boolean destroyed = false;

    public Eagle() {
        super(X, Y, Tank.SIZE, Tank.SIZE);
    }

    @Override
    public void update(){}

    public void destroy() {destroyed = true; }
    public boolean isDestroyed() { return destroyed; }
}
