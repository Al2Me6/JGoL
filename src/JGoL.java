import javax.swing.*;

/**
 * Driver class
 */
public class JGoL {
    /**
     * Run the game, create a UI JFrame
     */
    public static void main(String[] args) {
        try {
            // certain OS-specifics LaFs break custom button coloring
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        UI ui = new UI();
        ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ui.setLocationRelativeTo(null);
        ui.setVisible(true);
    }
}
