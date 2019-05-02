import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Manage UI components
 */
public class UI extends JFrame {
    private Board board;
    private Controls controls;

    /**
     * Constructor for UI class
     */
    public UI() {
        System.out.println("UI");
        int width = 20;
        int height = 20;
        board = new Board(width, height);
        // creating the grid
        ScrollableButtonGrid scrollableButtonGrid = new ScrollableButtonGrid(width, height);
        controls = new Controls();
    }

    /**
     * Sets the button size
     *
     * @param px The button size in pixels
     */
    private void setButtonSize(int px) {
        System.out.println("setButtonSize");
    }

    /**
     * A grid of buttons with scroll functionality
     */
    private class ScrollableButtonGrid extends JPanel {

        /**
         * @param w Width of the Grid
         * @param h Height of the Grid
         */
        public ScrollableButtonGrid(int w, int h) {
            System.out.println("ScrollableButtonGrid");
        }

        /**
         * Adds buttons to the grid
         *
         * @param btn A JButton to be added to the grid
         */
        public void addToGrid(JButton btn) {
            System.out.println("addToGrid");
        }
    }

    /**
     * UI Components with settings control Includes buttons for clearing, evolving,
     * and autoevolving
     */
    private class Controls extends JPanel {
        /**
         * The Constructor for controls Adds all the buttons
         */
        public Controls() {
            System.out.println("Controls");
        }

        /**
         * Updates the generation counter
         */
        private void updateGenerationCounter() {
            System.out.println("updateGenerationCounter");
        }

        /**
         * Button listener for the next generation button
         */
        private class NextGenBtnListener implements ActionListener {
            // On button click, evolve the board once
            public void actionPerformed(ActionEvent e) {
                System.out.println("NextGenBtnListner.ActionListener");
                board.evolve();
                updateGenerationCounter();
            }
        }

        /**
         * Button listener for the clear button
         */
        private class ClearBtnListener implements ActionListener {
            // On button click, clear the board
            public void actionPerformed(ActionEvent e) {
                System.out.println("ClearnBtnListener.ActionListener");
                board.clear();
                updateGenerationCounter();
            }
        }

        /**
         * Button listener for the start button
         */
        private class StartButtonListener implements ActionListener {
            // On button click, start auto evolve
            public void actionPerformed(ActionEvent e) {
                System.out.println("StartButtonListener.ActionListener");
            }
        }
    }
}
