import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * UI class
 * Manages UI components
 */
public class UI extends JFrame {
    private Board board;
    private Controls controls;


    /**
     * Constructor for UI class
     */
    public UI() {
        int width = Integer.parseInt(JOptionPane.showInputDialog(null, "Board width:"));
        int height = Integer.parseInt(JOptionPane.showInputDialog(null, "Board height:"));
        board = new Board(width, height);
        // creating the grid
        ScrollableButtonGrid scrollableButtonGrid = new ScrollableButtonGrid(width, height);
        for (int i = 0; i < board.getWidth(); i++) {
            for (int j = 0; j < board.getHeight(); j++) {
                scrollableButtonGrid.addToGrid(board.getCellButton(i, j));
            }
        }
        setButtonSize(20);
        add(scrollableButtonGrid, BorderLayout.CENTER);
        controls = new Controls();
        add(controls, BorderLayout.SOUTH);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    /**
     * Sets the button size
     * @param px The button size in pixels
     */
    private void setButtonSize(int px) {
        for (int i = 0; i < board.getWidth(); i++) {
            for (int j = 0; j < board.getHeight(); j++) {
                board.getCellButton(i, j).setPreferredSize(new Dimension(px, px));
            }
        }
    }

    /**
     * A grid of buttons with scroll functionality
     */
    private class ScrollableButtonGrid extends JPanel {
        private Panel gridPanel;
        private JScrollPane scrollPane;

        /**
         * @param w Width of the Grid
         * @param h Height of the Grid
         */
        public ScrollableButtonGrid(int w, int h) {
            gridPanel = new Panel();
            gridPanel.setLayout(new GridLayout(h, w));
            scrollPane = new JScrollPane(gridPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            add(scrollPane, BorderLayout.CENTER);
        }

        /**
         * Adds buttons to the grid
         * @param btn A JButton to be added to the grid
         */
        public void addToGrid(JButton btn) {
            gridPanel.add(btn);
        }
    }

    /**
     * UI Components with settings control
     * Includes buttons for clearing, evolving, and autoevolving
     */
    private class Controls extends JPanel {
        private JButton autoevolve;
        private JLabel generationCounter;

        /**
         * The Constructor for controls
         * Adds all the buttons
         */
        public Controls() {
            JButton nextGen = new JButton("Evolve state");
            nextGen.addActionListener(new NextGenBtnListener());
            add(nextGen);

            JButton clear = new JButton("Clear board");
            clear.addActionListener(new ClearBtnListener());
            add(clear);

            autoevolve = new JButton("Autoevolve");
            autoevolve.addActionListener(new StartButtonListener());
            add(autoevolve);

            generationCounter = new JLabel();
            add(generationCounter);
        }

        /**
         * Changes the generation num to specified number
         * @param count The desired iteration being set
         */
        public void setCounterCount(int count) {
            generationCounter.setText(String.format("Current generation: %d", count));
        }

        /**
         * Button listener for the next generation button
         */
        private class NextGenBtnListener implements ActionListener {
            // On button click, evolve the board once
            public void actionPerformed(ActionEvent e) {
                board.evolve();
            }
        }

        /**
         * Button listener for the clear button
         */
        private class ClearBtnListener implements ActionListener {
            // On button click, clear the board
            public void actionPerformed(ActionEvent e) {
                board.clear();
            }
        }

        /**
         * Button listener for the start button
         */
        private class StartButtonListener implements ActionListener {
            // On button click, start auto evolve
            public void actionPerformed(ActionEvent e) {

            }
        }
    }
}
