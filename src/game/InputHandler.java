package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import core.entities.PlayerTank;
import core.entities.Tank.Direction;

public class InputHandler implements KeyListener{

    private boolean fireRequested;
    private PlayerTank player;

    public InputHandler(PlayerTank player) {
        this.player = player;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> {
                player.setDirection(Direction.UP);
                player.setMoving(true);
            }
            case KeyEvent.VK_S -> {
                player.setDirection(Direction.DOWN);
                player.setMoving(true);
            }
            case KeyEvent.VK_A -> {
                player.setDirection(Direction.LEFT);
                player.setMoving(true);
            }
            case KeyEvent.VK_D -> {
                player.setDirection(Direction.RIGHT);
                player.setMoving(true);
            }

            case KeyEvent.VK_SPACE -> requestFire();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> {
                if (player.getDirection() == Direction.UP) player.setMoving(false);
            }
            case KeyEvent.VK_S -> {
                if (player.getDirection() == Direction.DOWN) player.setMoving(false);
            }
            case KeyEvent.VK_A -> {
                if (player.getDirection() == Direction.LEFT) player.setMoving(false);
            }
            case KeyEvent.VK_D -> {
                if (player.getDirection() == Direction.RIGHT) player.setMoving(false);
            }
        }
    }

    public synchronized void requestFire(){
        fireRequested = true;
    }

    public synchronized boolean consumeFireRequest(){
        boolean f = fireRequested;
        fireRequested = false;
        return f;
    }
}