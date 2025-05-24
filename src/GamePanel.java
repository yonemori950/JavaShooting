import java.awt.Color;
import java.awt.Dimension;
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
        for (Bullet b : bullets) {
            b.move();
        }
        bullets.removeIf(b -> b.y < 0);

        for (Enemy enemy : enemies) {
            enemy.move();
        }
        enemies.removeIf(enemy -> enemy.y > getHeight());

        // 弾と敵の当たり判定
        Iterator<Bullet> bulletIter = bullets.iterator();
        while (bulletIter.hasNext()) {
            Bullet b = bulletIter.next();
            Iterator<Enemy> enemyIter = enemies.iterator();
            while (enemyIter.hasNext()) {
                Enemy e1 = enemyIter.next();
                if (b.x < e1.x + 20 && b.x + 5 > e1.x && b.y < e1.y + 20 && b.y + 10 > e1.y) {
                    bulletIter.remove();
                    enemyIter.remove();
                    score += 10;
                    break;
                }
            }
        }

        // ランダムで敵出現
        if (Math.random() < 0.02) {
            enemies.add(new Enemy(new Random().nextInt(380), 0));
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
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