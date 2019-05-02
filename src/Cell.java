import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 * An individual cell
 */
public class Cell {
    private boolean state;
    private JButton button;

    /**
     * Constructor for Cell
     */
    public Cell() {
        System.out.println("Cell");
        button = new JButton();
    }

    /**
     * @return The state of the cell
     */
    public boolean getState() {
        System.out.println("getState");
        return true;
    }

    /**
     * @param s New state to set
     */
    public void setState(boolean s) {
        System.out.println("setState");
    }

    /**
     * @return The cell's JButton
     */
    public JButton getButton() {
        System.out.println("getButton");
        return new JButton();
    }

    /**
     * Private method for changing color of button according to state
     */
    private void colorize() {
        System.out.println("colorize");
    }

    /**
     * Private button listener
     */
    private class ToggleStateListener implements ActionListener {
        /**
         * Implementation of actionPerformed Toggles state and updates color
         */
        public void actionPerformed(ActionEvent e) {
            System.out.println("ToggleStateListener.actionPerformed");
        }
    }
}