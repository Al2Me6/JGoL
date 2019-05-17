import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * Driver class
 */
public class JGoL {
    /**
     * Run the game, create a UI JFrame
     */
    public static void main(String[] args) {
        UI ui = new UI();
        try {
            // certain OS-specifics LaFs break custom button coloring
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ui.setLocationRelativeTo(null);
        ui.setVisible(true);
    }
}
