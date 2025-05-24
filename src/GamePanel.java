import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private int playerX = 200;
    private int playerY = 400;
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private int score = 0;
    private boolean isGameOver = false;

    public GamePanel() {
        setPreferredSize(new Dimension(400, 500));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        timer = new Timer(15, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (isGameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 32));
            g.drawString("GAME OVER", 100, 250);
            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.drawString("Score: " + score, 150, 290);
            return;
        }

        g.setColor(Color.WHITE);
        g.fillRect(playerX, playerY, 20, 20); // プレイヤー

        g.setColor(Color.YELLOW);
        for (Bullet b : bullets) {
            g.fillRect(b.x, b.y, 5, 10);
        }

        g.setColor(Color.RED);
        for (Enemy e : enemies) {
            g.fillRect(e.x, e.y, 20, 20);
        }

        g.setColor(Color.GREEN);
        g.drawString("Score: " + score, 10, 20);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isGameOver) return;

        for (Bullet b : bullets) {
            b.move();
        }
        bullets.removeIf(b -> b.y < 0);

        for (Enemy enemy : enemies) {
            enemy.move();
            if (enemy.y + 20 > getHeight()) {
                isGameOver = true;
                timer.stop();
                repaint();
                return;
            }
        }
        enemies.removeIf(enemy -> enemy.y > getHeight());

        Iterator<Bullet> bulletIter = bullets.iterator();
        while (bulletIter.hasNext()) {
            Bullet b = bulletIter.next();
            Iterator<Enemy> enemyIter = enemies.iterator();
            while (enemyIter.hasNext()) {
                Enemy enemy = enemyIter.next();
                if (b.x < enemy.x + 20 && b.x + 5 > enemy.x && b.y < enemy.y + 20 && b.y + 10 > enemy.y) {
                    bulletIter.remove();
                    enemyIter.remove();
                    score += 10;
                    break;
                }
            }
        }

        if (Math.random() < 0.02) {
            enemies.add(new Enemy(new Random().nextInt(380), 0));
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (isGameOver) return;

        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT && playerX > 0) playerX -= 10;
        if (key == KeyEvent.VK_RIGHT && playerX < getWidth() - 20) playerX += 10;
        if (key == KeyEvent.VK_UP && playerY > 0) playerY -= 10;
        if (key == KeyEvent.VK_DOWN && playerY < getHeight() - 20) playerY += 10;
        if (key == KeyEvent.VK_SPACE) {
            bullets.add(new Bullet(playerX + 7, playerY));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
}

class Bullet {
    int x, y;
    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void move() {
        y -= 10;
    }
}

class Enemy {
    int x, y;
    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void move() {
        y += 5;
    }
}