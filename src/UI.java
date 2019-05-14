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
     * Constructor for UI
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
         * Constructor for ButtonGrid
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
             * Constructor for CellButton
             *
             * @param c    Coordinate that the button corresponds to
             * @param size Initial size of the button
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
         * @param c Coordinate of button to update
         */
        public void updateButtonColor(Coordinate c) {
            buttons[c.x()][c.y()].colorize();
        }

        /**
         * Change the size of all buttons
         *
         * @param size New size to be set
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
     * Control UI components
     */
    private class Controls extends JPanel {
        private JButton autoevolveButton;
        private JLabel genCounter;
        private JLabel computeTimeLabel;
        private boolean autoevolveEnabled = false;
        private int autoevolveSpeed = 200;

        /**
         * Constructor for Controls
         */
        public Controls() {
            setLayout(new FlowLayout());

            genCounter = new JLabel();
            genCounter.setHorizontalAlignment(SwingConstants.CENTER);
            updateGenCounter();
            add(genCounter);

            JPanel controlButtonsPanel = new JPanel(new FlowLayout());

            // On button click, evolve the board once
            JButton nextGen = new JButton("Evolve state");
            nextGen.addActionListener(ae -> evolveBoard());
            controlButtonsPanel.add(nextGen);

            // On button click, clear the board
            JButton clear = new JButton("Clear board");
            clear.addActionListener(ae -> clearBoard());
            controlButtonsPanel.add(clear);

            // On button click, start or stop autoevolve
            autoevolveButton = new JButton();
            autoevolveButton.addActionListener(ae -> {
                autoevolveEnabled = !autoevolveEnabled;
                if (autoevolveEnabled) {
                    Thread autoevolveThread = new Thread(() -> {
                        while (autoevolveEnabled) {
                            SwingUtilities.invokeLater(this::evolveBoard);
                            try {
                                Thread.sleep(autoevolveSpeed);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    autoevolveThread.start();
                }
                updateAutoevolveButtonText();
            });
            updateAutoevolveButtonText();
            controlButtonsPanel.add(autoevolveButton);

            add(controlButtonsPanel);

            JPanel autoevolveSpeedPanel = new JPanel(new FlowLayout());

            autoevolveSpeedPanel.add(new JLabel("Autoevolve speed:"));

            JSlider autoevolveSpeedSlider = new JSlider(JSlider.HORIZONTAL, 50, 1050, autoevolveSpeed);
            autoevolveSpeedSlider.setMinorTickSpacing(50);
            autoevolveSpeedSlider.setMajorTickSpacing(200);
            autoevolveSpeedSlider.setPaintTicks(true);
            autoevolveSpeedSlider.addChangeListener(ce -> autoevolveSpeed = ((JSlider) ce.getSource()).getValue());
            autoevolveSpeedPanel.add(autoevolveSpeedSlider);

            add(autoevolveSpeedPanel);

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

            computeTimeLabel = new JLabel();
            updateComputeTimeLabel();
            add(computeTimeLabel);
        }

        private void evolveBoard() {
            updateGUI(board.evolve());
        }

        /**
         * Clear the board and stop autoevolve if enabled
         */
        private void clearBoard() {
            autoevolveEnabled = false;
            updateGUI(board.clear());
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
        private void updateAutoevolveButtonText() {
            autoevolveButton.setText((autoevolveEnabled ? "Stop" : "Start") + " autoevolve");
        }

        private void updateComputeTimeLabel() {
            computeTimeLabel.setText(String.format("Compute time: %,dns", board.getComputeTime()));
        }

        /**
         * Synchronize GUI with board
         *
         * @param delta HashSet of cells whose status has changed
         */
        private void updateGUI(HashSet<Coordinate> delta) {
            for (Coordinate c : delta) {
                // array bounds may overflow here due to architecture of board
                if (c.x() >= 0 && c.y() >= 0 && c.x() < width && c.y() < height)
                    buttonGrid.updateButtonColor(c);
            }
            updateGenCounter();
            updateAutoevolveButtonText();
            updateComputeTimeLabel();
        }
    }

    /**
     * Asks the user a question, checking input
     *
     * @param question Question to ask user
     * @return User's response
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
