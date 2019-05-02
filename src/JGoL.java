import javax.swing.*;

/**
 * Driver class
 */
public class JGoL {
    /**
     * Run the game, creates UI JFrame
     */
    public static void main(String[] args) {
        System.out.println("main");
        UI frame = new UI();
        frame.pack();
        frame.setVisible(true);
    }
}
