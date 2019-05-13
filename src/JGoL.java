import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * Driver class
 */
public class JGoL {
    /**
     * Run the game, creates UI JFrame
     */
    public static void main(String[] args) {
        UI ui = new UI();
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ui.setLocationRelativeTo(null);
        ui.setVisible(true);
    }
}
