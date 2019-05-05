import javax.swing.*;

/**
 * Driver class
 */
public class JGoL {
    /**
     * Run the game, creates UI JFrame
     */
    public static void main(String[] args) {
        UI ui = new UI();
        ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ui.setExtendedState(JFrame.MAXIMIZED_BOTH);
        ui.setVisible(true);
    }
}
