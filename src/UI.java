import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

/**
 * Manage UI components
 */
public class UI extends JFrame {
    private int width, height;
    private Board board;
    private ButtonGrid buttonGrid;
    private static final int INITIAL_BUTTON_SIZE = 15;
    private static final Dimension STARTING_SIZE = new Dimension(1400, 900);
    private static final String[] TIME_UNITS = new String[] { "ns", "μs", "ms", "s" };
    private static final int T_UP = 0;
    private static final int T_DOWN = 1;
    private static final int T_LEFT = 2;
    private static final int T_RIGHT = 3;

    /**
     * Initialize the JGoL UI, create a board of a user-specified size
     */
    public UI() {
        setTitle("JGoL");
        setLayout(new BorderLayout());

        width = 100;
        height = 90;

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
        private int transformX, transformY;
        private final Color ALIVE_COLOR = Color.BLACK;
        private final Color DEAD_COLOR = Color.WHITE;

        /**
         * Create a new ButtonGrid with size corresponding to board
         */
        public ButtonGrid() {
            setLayout(new GridLayout(height, width, -1, -1));
            buttons = new CellButton[width][height];

            // wonky iteration order to translate UI coordinate system to mathematical
            // coordinate system
            for (int j = height - 1; j >= 0; j--) {
                for (int i = 0; i < width; i++) {
                    buttons[i][j] = new CellButton(new Coordinate(i, j), INITIAL_BUTTON_SIZE);
                    add(buttons[i][j]);
                }
            }

            transformX = 0;
            transformY = 0;
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
                    board.toggleState(button2board(coordinate));
                    colorize();
                });
            }

            /**
             * Synchronize the color of the button according to the state of the
             * corresponding cell
             */
            public void colorize() {
                setBackground(board.getCellState(button2board(coordinate)) ? ALIVE_COLOR : DEAD_COLOR);
            }

            private Coordinate button2board(Coordinate c) {
                return new Coordinate(c.x() + transformX, c.y() + transformY);
            }
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

        public void buttonRefresh(HashSet<Coordinate> delta) {
            for (Coordinate c : delta) {
                Coordinate btnC = board2button(c);
                if (btnC.x() >= 0 && btnC.y() >= 0 && btnC.x() < width && btnC.y() < height) {
                    buttons[(int) btnC.x()][(int) btnC.y()].colorize();
                }
            }
        }

        public void scroll(int transformPerformed) {
            // wipe all currently alive cells from board
            for (Coordinate c : board.getLiveCells()) {
                Coordinate btnC = board2button(c);
                if (btnC.x() >= 0 && btnC.y() >= 0 && btnC.x() < width && btnC.y() < height) {
                    buttons[(int) btnC.x()][(int) btnC.y()].setBackground(DEAD_COLOR);
                }
            }
            switch (transformPerformed) {
                case T_UP:
                    transformY--;
                    break;
                case T_DOWN:
                    transformY++;
                    break;
                case T_LEFT:
                    transformX++;
                    break;
                case T_RIGHT:
                    transformX--;
                    break;
            }
            // repopulate board with new transformation
            buttonRefresh(board.getLiveCells());
        }

        private Coordinate board2button(Coordinate c) {
            return new Coordinate(c.x() - transformX, c.y() - transformY);
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
                                // "faster" refresh is numerically lower sleep time, so flip here
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

            // WASD keys for infinite scroll
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(ke -> {
                char code = Character.toLowerCase(ke.getKeyChar());
                if (code == 'w' || code == 's' || code == 'd' || code == 'a') {
                    switch (code) {
                    case 'w':
                        buttonGrid.scroll(T_UP);
                        break;
                    case 's':
                        buttonGrid.scroll(T_DOWN);
                        break;
                    case 'd':
                        buttonGrid.scroll(T_RIGHT);
                        break;
                    case 'a':
                        buttonGrid.scroll(T_LEFT);
                        break;
                    }
                }
                return false;
            });
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
            computeTimeLabel.setText("Compute time: " + formatTime(board.getComputeTime()));
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
            buttonGrid.buttonRefresh(delta);
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
     * Convert nanoseconds to bigger units as necessary
     *
     * @param time original time, in nanoseconds
     * @return a string expressing the input in the largest applicable unit of time
     */
    private static String formatTime(long time) {
        int ct = 0;
        while (time > 1000000 && ct < 2) {
            time /= 1000;
            ct++;
        }
        float decimalTime;
        if (time > 1000) {
            decimalTime = (float) time / 1000;
            ct++;
        } else {
            decimalTime = time;
        }
        return String.format("%,.3f%s", decimalTime, TIME_UNITS[ct]);
    }
}
