import javax.swing.*;

/**
 * Driver class
 */
public class JGoL {
    /**
     * Run the game, creates UI JFrame
     */
    public static void main(String[] args) {
        UI frame = new UI();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
