import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class GameFrame extends JFrame {
    public GameFrame() {
        setTitle("Javaシューティングゲーム");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(new GamePanel(this)); // JFrameをGamePanelに渡す
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void restartGame() {
        getContentPane().removeAll();

        // GamePanelの参照を変数に入れる
        GamePanel panel = new GamePanel(this);
        add(panel);

        revalidate();
        repaint();

        // GamePanelにフォーカスを明示的に当てる
        SwingUtilities.invokeLater(() -> panel.requestFocusInWindow());
    }

    public static void main(String[] args) {
        new GameFrame();
    }
}
