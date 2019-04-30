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
        state = false;
        button = new JButton();
        button.addActionListener(new ToggleStateListener());
        colorize();
    }

    /**
     * @return The state of the cell
     */
    public boolean getState() {
        return state;
    }

    /**
     * @param s New state to set
     */
    public void setState(boolean s) {
        state = s;
        colorize();
    }

    /**
     * @return The cell's JButton
     */
    public JButton getButton() {
        return button;
    }

    /**
     * Private method for changing color of button according to state
     */
    private void colorize() {
        button.setBackground(state ? Color.black : Color.white);
    }

    /**
     * Private button listener
     */
    private class ToggleStateListener implements ActionListener {
        /**
         * Implementation of actionPerformed Toggles state and updates color
         */
        public void actionPerformed(ActionEvent e) {
            state = !state;
            colorize();
        }
    }
}