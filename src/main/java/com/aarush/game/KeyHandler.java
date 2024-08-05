package main.java.com.aarush.game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyHandler extends KeyAdapter {

    private GamePanel gamePanel;

    public KeyHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_R:
                if (gamePanel.gameState == GamePanel.GAME_OVER) {
                    gamePanel.gameState = GamePanel.PLAYING;
                    gamePanel.reset();
                }
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                if (gamePanel.direction != 'D') {
                    gamePanel.direction = 'U';
                }
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                if (gamePanel.direction != 'U') {
                    gamePanel.direction = 'D';
                }
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                if (gamePanel.direction != 'R') {
                    gamePanel.direction = 'L';
                }
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                if (gamePanel.direction != 'L') {
                    gamePanel.direction = 'R';
                }
                break;

            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
        }
    }

}
