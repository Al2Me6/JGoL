import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class Cell {
    private boolean state;
    private JButton button;

    public Cell() {
        state = false;
        button = new JButton();
        button.addActionListener(new ToggleStateListener());
        colorize();
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean s) {
        state = s;
        colorize();
    }

    public JButton getButton() {
        return button;
    }

    private void colorize() {
        button.setBackground(state ? Color.black : Color.white);
    }

    private class ToggleStateListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            state = !state;
            colorize();
        }
    }
}