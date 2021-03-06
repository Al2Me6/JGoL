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
            // certain OS-specific LaFs break custom button coloring
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
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
