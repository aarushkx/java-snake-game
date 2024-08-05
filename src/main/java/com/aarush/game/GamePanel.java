package main.java.com.aarush.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {

    public static final int SCREEN_WIDTH = 600;
    public static final int SCREEN_HEIGHT = 600;
    public static final int UNIT_SIZE = 25;
    public static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    public static final int DELAY = 85;
    public static final String FONT_NAME = "Georgia";

    public static final int PLAYING = 1;
    public static final int GAME_OVER = 2;
    public int gameState = PLAYING;

    public final int[] x = new int[GAME_UNITS];
    public final int[] y = new int[GAME_UNITS];
    public int bodyParts = 2;
    public int cherrysEaten;
    public int cherryX;
    public int cherryY;
    public char direction = 'R';
    public boolean running = false;
    public Timer timer;
    public Random random;
    public KeyHandler keyHandler = new KeyHandler(this);

    public GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(new Color(248, 233, 161));
        this.setFocusable(true);
        this.addKeyListener(keyHandler);
        start();
    }

    public void start() {
        newCherry();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void reset() {
        bodyParts = 2;
        cherrysEaten = 0;
        direction = 'R';
        for (int i = 0; i < bodyParts; i++) {
            x[i] = 0;
            y[i] = 0;
        }
        newCherry();
        running = true;
        if (timer != null) {
            timer.stop();
            timer = new Timer(DELAY, this);
            timer.start();
        } else {
            timer = new Timer(DELAY, this);
            timer.start();
        }
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        switch (gameState) {
            case PLAYING:
                draw(g);
                break;
            case GAME_OVER:
                gameOver(g);
                break;
        }
    }

    public void draw(Graphics g) {
        if (running) {

            // Draws a grid of lines on the provided Graphics object
            // for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
            //     g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            //     g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            // }

            // Draws the cherry
            g.setColor(new Color(150, 0, 0));
            g.fillOval(cherryX, cherryY, UNIT_SIZE, UNIT_SIZE);

            // Draws the snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(new Color(92, 58, 146));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);

                    // Draw the eyes on the snake's head
                    g.setColor(Color.WHITE);
                    int eyeSize = 12;
                    int eyeOffset = 6;
                    int eyeballSize = 6;
                    int eyeGap = 12;

                    // Coordinates for the eyes
                    int eyeY = y[i] + 6;
                    int leftEyeX = x[i] + eyeOffset;
                    int rightEyeX = x[i] + UNIT_SIZE - eyeSize - eyeOffset - eyeGap;

                    // Draw left eye
                    g.fillOval(leftEyeX, eyeY, eyeSize, eyeSize);
                    g.setColor(Color.BLACK);
                    g.fillOval(leftEyeX + (eyeSize - eyeballSize) / 2, eyeY + (eyeSize - eyeballSize) / 2, eyeballSize, eyeballSize);

                    g.setColor(Color.WHITE);

                    // Draw right eye
                    g.fillOval(rightEyeX, eyeY, eyeSize, eyeSize);
                    g.setColor(Color.BLACK);
                    g.fillOval(rightEyeX + (eyeSize - eyeballSize) / 2, eyeY + (eyeSize - eyeballSize) / 2, eyeballSize, eyeballSize);

                } else {
                    g.setColor(new Color(138, 100, 214));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            g.setColor(new Color(78, 42, 31));
            g.setFont(new Font(FONT_NAME, Font.BOLD, 25));
            g.drawString("Score: " + cherrysEaten, 10, g.getFont().getSize());

        } else {
            gameOver(g);
        }
    }

    public void newCherry() {
        cherryX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        cherryY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() {
        // Move body parts
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        // Move the head
        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkCherry() {
        if ((x[0] == cherryX) && (y[0] == cherryY)) {
            bodyParts++;
            cherrysEaten++;
            newCherry();
        }
    }

    public void checkCollisions() {
        // Checks if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
                gameState = GAME_OVER;
                return;
            }
        }

        if (x[0] < 0) {
            running = false;
        }
        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }
        if (y[0] < 0) {
            running = false;
        }
        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }

        if (x[0] < 0 || x[0] >= SCREEN_WIDTH || y[0] < 0 || y[0] >= SCREEN_HEIGHT) {
            running = false;
            gameState = GAME_OVER;
        }
    }

    public void gameOver(Graphics g) {
        // Set font and color for "Game Over"
        g.setColor(new Color(78, 42, 31));
        g.setFont(new Font(FONT_NAME, Font.BOLD, 55));
        FontMetrics metrics = g.getFontMetrics(g.getFont());

        String text1 = "Game Over";
        g.drawString(text1, (SCREEN_WIDTH - metrics.stringWidth(text1)) / 2, SCREEN_HEIGHT / 2);

        // Set font and color for the score
        g.setFont(new Font(FONT_NAME, Font.BOLD, 30));
        metrics = g.getFontMetrics(g.getFont());
        g.drawString("Score: " + cherrysEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + cherrysEaten)) / 2,
                SCREEN_HEIGHT / 2 + 60);

        // Set font and color for "Press R to play again or Esc to exit"
        g.setFont(new Font(FONT_NAME, Font.BOLD, 20));
        metrics = g.getFontMetrics(g.getFont());
        String text2 = "Press R to play again or Esc to exit";
        g.drawString(text2, (SCREEN_WIDTH - metrics.stringWidth(text2)) / 2, SCREEN_HEIGHT / 2 + 120);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkCherry();
            checkCollisions();
        }
        repaint();
    }

}
