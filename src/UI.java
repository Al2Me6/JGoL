import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.util.HashSet;

/**
 * Manage UI components
 */
public class UI extends JFrame {
    private int width, height;
    private Board board;
    private ButtonGrid buttonGrid;
    private final int INITIAL_BUTTON_SIZE = 15;
    private final Dimension STARTING_SIZE = new Dimension(1400, 900);

    /**
     * Initialize the JGoL UI, create a board of a user-specified size
     */
    public UI() {
        setTitle("JGoL");
        setLayout(new BorderLayout());

        width = userInput("Board width:");
        height = userInput("Board height");

        board = new Board();

        buttonGrid = new ButtonGrid();
        JScrollPane buttonGridScrollBox = new JScrollPane(buttonGrid);
        buttonGridScrollBox.getHorizontalScrollBar().setUnitIncrement(15);
        buttonGridScrollBox.getVerticalScrollBar().setUnitIncrement(15);
        add(buttonGridScrollBox, BorderLayout.CENTER);

        Controls controls = new Controls();
        add(controls, BorderLayout.SOUTH);

        setPreferredSize(STARTING_SIZE);
        pack();
    }

    /**
     * A grid of buttons corresponding to cells
     */
    private class ButtonGrid extends JPanel {
        private CellButton[][] buttons;

        /**
         * Create a new ButtonGrid with size corresponding to board
         */
        public ButtonGrid() {
            setLayout(new GridLayout(height, width, -1, -1));
            buttons = new CellButton[height][width];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    buttons[i][j] = new CellButton(new Coordinate(i, j), INITIAL_BUTTON_SIZE);
                    add(buttons[i][j]);
                }
            }
        }

        /**
         * A JButton with coordinate and color properties
         */
        private class CellButton extends JButton {
            private Coordinate coordinate;

            /**
             * Create a new CellButton at a certain location with a default size
             *
             * @param c    coordinate that the button corresponds to
             * @param size initial size of the button
             */
            public CellButton(Coordinate c, int size) {
                coordinate = c;
                setPreferredSize(new Dimension(size, size));
                setMaximumSize(new Dimension(size, size));
                setOpaque(true);
                colorize();
                addActionListener(e -> {
                    board.toggleState(coordinate);
                    colorize();
                });
            }

            /**
             * Synchronize the color of the button according to the state of the corresponding cell
             */
            public void colorize() {
                setBackground(board.getCellState(coordinate) ? Color.BLACK : Color.WHITE);
            }
        }

        /**
         * Synchronize the color of a button according to the state of the corresponding cell
         *
         * @param c coordinate of button to update
         */
        public void updateButtonColor(Coordinate c) {
            buttons[c.x()][c.y()].colorize();
        }

        /**
         * Change the size of all buttons
         *
         * @param size new size to be set
         */
        public void updateButtonSize(int size) {
            for (CellButton[] row : buttons) {
                for (CellButton b : row) {
                    b.setPreferredSize(new Dimension(size, size));
                    b.setMaximumSize(new Dimension(size, size));
                }
            }
            revalidate();
            repaint();
        }
    }

    /**
     * UI control elements
     */
    private class Controls extends JPanel {
        private JButton autoButton;
        private JLabel genCounter;
        private JLabel computeTimeLabel;
        private boolean autoEnabled = false;
        private final int AUTO_SPEED_MIN = 50;
        private final int AUTO_SPEED_MAX = 1250;
        private int autoSpeed = 750;

        /**
         * Initialize all control elements
         */
        public Controls() {
            setLayout(new FlowLayout());

            // display current generation count
            genCounter = new JLabel();
            genCounter.setHorizontalAlignment(SwingConstants.CENTER);
            add(genCounter);

            // On button click, evolve the board once
            JButton nextGen = new JButton("Evolve state");
            nextGen.addActionListener(ae -> evolveBoard());
            add(nextGen);

            // On button click, clear the board
            JButton clear = new JButton("Clear board");
            clear.addActionListener(ae -> clearBoard());
            add(clear);

            // On button click, start or stop autoevolve
            autoButton = new JButton();
            autoButton.addActionListener(ae -> {
                autoEnabled = !autoEnabled;
                if (autoEnabled) {
                    Thread autoThread = new Thread(() -> {
                        while (autoEnabled) {
                            SwingUtilities.invokeLater(this::evolveBoard);
                            try {
                                Thread.sleep(AUTO_SPEED_MAX - (autoSpeed - AUTO_SPEED_MIN));
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    autoThread.start();
                }
                updateAutoButtonText();
            });
            add(autoButton);

            // change autoevolve speed
            JPanel autoSpeedPanel = new JPanel(new FlowLayout());
            autoSpeedPanel.add(new JLabel("Autoevolve speed:"));
            JSlider autoSpeedSlider = new JSlider(JSlider.HORIZONTAL, AUTO_SPEED_MIN, AUTO_SPEED_MAX, autoSpeed);
            autoSpeedSlider.setMinorTickSpacing(50);
            autoSpeedSlider.setMajorTickSpacing(200);
            autoSpeedSlider.setPaintTicks(true);
            autoSpeedSlider.addChangeListener(ce -> autoSpeed = ((JSlider) ce.getSource()).getValue());
            autoSpeedPanel.add(autoSpeedSlider);
            add(autoSpeedPanel);

            // change button size, aka zoom
            JPanel zoomPanel = new JPanel(new FlowLayout());
            zoomPanel.add(new JLabel("Zoom:"));
            JSlider zoomSlider = new JSlider(JSlider.HORIZONTAL, 5, 20, INITIAL_BUTTON_SIZE);
            zoomSlider.setMinorTickSpacing(1);
            zoomSlider.setMajorTickSpacing(5);
            zoomSlider.setPaintTicks(true);
            zoomSlider.setPaintLabels(true);
            zoomSlider.addChangeListener(ce -> buttonGrid.updateButtonSize(((JSlider) ce.getSource()).getValue()));
            zoomPanel.add(zoomSlider);
            add(zoomPanel);

            // display compute time for previous iteration
            computeTimeLabel = new JLabel();
            add(computeTimeLabel);

            uiRefresh();
        }

        /**
         * Synchronize the generation count with board
         */
        private void updateGenCounter() {
            genCounter.setText(String.format("Current generation: %d", board.getGenCount()));
        }

        /**
         * Display the correct autoevolve state
         */
        private void updateAutoButtonText() {
            autoButton.setText((autoEnabled ? "Stop" : "Start") + " autoevolve");
        }

        /**
         * Get the most recent compute time
         */
        private void updateComputeTimeLabel() {
            computeTimeLabel.setText(String.format("Compute time: %,dns", board.getComputeTime()));
        }

        /**
         * Evolve the board one time
         */
        private void evolveBoard() {
            fullRefresh(board.evolve());
        }

        /**
         * Clear the board and stop autoevolve if enabled
         */
        private void clearBoard() {
            autoEnabled = false;
            fullRefresh(board.clear());
        }

        /**
         * Synchronize the entire UI with board, including buttons
         *
         * @param delta HashSet of cells whose status has changed
         */
        private void fullRefresh(HashSet<Coordinate> delta) {
            for (Coordinate c : delta) {
                // array bounds may overflow here due to architecture of board
                if (c.x() >= 0 && c.y() >= 0 && c.x() < width && c.y() < height)
                    buttonGrid.updateButtonColor(c);
            }
            uiRefresh();
        }

        /**
         * Synchronize all UI controls with board
         */
        private void uiRefresh() {
            updateGenCounter();
            updateAutoButtonText();
            updateComputeTimeLabel();
        }
    }

    /**
     * Asks the user a question, checking input
     *
     * @param question question to ask user
     * @return user's response
     */
    private int userInput(String question) {
        int ct = 0;
        while (true) {
            String input = JOptionPane.showInputDialog(this, question);
            if (input == null)  // user clicked cancel
                System.exit(1);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                if (ct++ >= 3) {
                    JOptionPane.showMessageDialog(this, "Too many tries!", "Input Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
                JOptionPane.showMessageDialog(this, "Please input a valid integer!", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
