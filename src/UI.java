import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

/**
 * Manage UI components
 */
public class UI extends JFrame {
    private Board board;
    private ButtonGrid buttonGrid;
    private static final int INITIAL_BUTTON_SIZE = 15;
    private static final Dimension STARTING_SIZE = new Dimension(1400, 900);
    private static final String[] TIME_UNITS = new String[]{"ns", "μs", "ms", "s"};
    private static final int T_UP = 0;
    private static final int T_DOWN = 1;
    private static final int T_LEFT = 2;
    private static final int T_RIGHT = 3;
    private static final int T_ZERO = 4;

    /**
     * Initialize the JGoL UI, create a board of a user-specified size
     */
    public UI() {
        setTitle("JGoL");
        setIconImage(new ImageIcon(getClass().getResource("logo.png")).getImage());
        setLayout(new BorderLayout());

        board = new Board();

        buttonGrid = new ButtonGrid();
        JPanel buttonNavigationPanel = new JPanel(new BorderLayout());
        JScrollPane buttonGridScrollBox = new JScrollPane(buttonGrid, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        buttonGridScrollBox.getHorizontalScrollBar().setUnitIncrement(0);
        buttonGridScrollBox.getVerticalScrollBar().setUnitIncrement(0);
        buttonNavigationPanel.add(buttonGridScrollBox, BorderLayout.CENTER);
        // buttonNavigationPanel.add(buttonGrid, BorderLayout.CENTER);

        JButton leftButton = new JButton("⏪");
        leftButton.addActionListener(ae -> buttonGrid.updateTransform(T_LEFT, 4));
        buttonNavigationPanel.add(leftButton, BorderLayout.LINE_START);
        JButton rightButton = new JButton("⏩");
        rightButton.addActionListener(ae -> buttonGrid.updateTransform(T_RIGHT, 4));
        buttonNavigationPanel.add(rightButton, BorderLayout.LINE_END);
        JButton upButton = new JButton("⏫");
        upButton.addActionListener(ae -> buttonGrid.updateTransform(T_UP, 4));
        buttonNavigationPanel.add(upButton, BorderLayout.NORTH);
        JButton downButton = new JButton("⏬");
        downButton.addActionListener(ae -> buttonGrid.updateTransform(T_DOWN, 4));
        buttonNavigationPanel.add(downButton, BorderLayout.SOUTH);

        add(buttonNavigationPanel, BorderLayout.CENTER);

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
        private static final int MAX_GRID_WIDTH = 200;
        private static final int MAX_GRID_HEIGHT = 200;
        private final Color ALIVE_COLOR = Color.BLACK;
        private final Color DEAD_COLOR = Color.WHITE;

        /**
         * Create a new ButtonGrid with size corresponding to board
         */
        public ButtonGrid() {
            setLayout(new GridLayout(MAX_GRID_HEIGHT, MAX_GRID_WIDTH, -1, -1));
            buttons = new CellButton[MAX_GRID_WIDTH][MAX_GRID_HEIGHT];
            // wonky iteration order to translate UI coordinate system to mathematical coordinate system
            for (int j = MAX_GRID_HEIGHT - 1; j >= 0; j--) {
                for (int i = 0; i < MAX_GRID_WIDTH; i++) {
                    buttons[i][j] = new CellButton(new Coordinate(i, j), INITIAL_BUTTON_SIZE);
                    add(buttons[i][j]);
                }
            }

            updateTransform(T_ZERO, 1);
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
             * Synchronize the color of the button according to the state of the corresponding cell
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
                    b.setMinimumSize(new Dimension(size, size));
                }
            }
            revalidate();
            repaint();
        }

        /**
         * Refresh button colors
         *
         * @param delta HashSet of changed coordinates
         */
        public void buttonRefresh(HashSet<Coordinate> delta) {
            for (Coordinate c : delta) {
                Coordinate btnC = board2button(c);
                if (btnC.x() >= 0 && btnC.y() >= 0 && btnC.x() < MAX_GRID_WIDTH && btnC.y() < MAX_GRID_HEIGHT) {
                    buttons[(int) btnC.x()][(int) btnC.y()].colorize();
                }
            }
        }

        /**
         * Stage the parameters for the transformations on the board
         *
         * @param transformPerformed the id of the type of transportation that is to be performed
         * @param increment the size of the transformation to be performed
         */
        public void updateTransform(int transformPerformed, int increment) {
            // wipe all currently alive cells from board
            for (Coordinate c : board.getLiveCells()) {
                Coordinate btnC = board2button(c);
                if (btnC.x() >= 0 && btnC.y() >= 0 && btnC.x() < MAX_GRID_WIDTH && btnC.y() < MAX_GRID_HEIGHT) {
                    buttons[(int) btnC.x()][(int) btnC.y()].setBackground(DEAD_COLOR);
                }
            }
            for (int i = 0; i < increment; i++) {
                switch (transformPerformed) {
                    case T_UP:
                        transformY++;
                        break;
                    case T_DOWN:
                        transformY--;
                        break;
                    case T_LEFT:
                        transformX--;
                        break;
                    case T_RIGHT:
                        transformX++;
                        break;
                    case T_ZERO:
                        transformX = 0;
                        transformY = 0;
                        break;
                }
            }
            // repopulate board with new transformation
            buttonRefresh(board.getLiveCells());
        }

        /**
         * Convert the coordinates on the board to the coordinates associated with the button
         *
         * @param c the coordinates on the board
         * @return the coordinate associated with the button
         */
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

            JButton homeButton = new JButton("Home");
            homeButton.addActionListener(ae -> buttonGrid.updateTransform(T_ZERO, 1));
            add(homeButton);

            uiRefresh();

            // WASD keys for infinite scroll
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(ke -> {
                char code = Character.toLowerCase(ke.getKeyChar());
                if (code == 'w' || code == 's' || code == 'd' || code == 'a' || code == 'z') {
                    switch (code) {
                        case 'w':
                            buttonGrid.updateTransform(T_UP, 1);
                            break;
                        case 's':
                            buttonGrid.updateTransform(T_DOWN, 1);
                            break;
                        case 'd':
                            buttonGrid.updateTransform(T_RIGHT, 1);
                            break;
                        case 'a':
                            buttonGrid.updateTransform(T_LEFT, 1);
                            break;
                        case 'z':
                            buttonGrid.updateTransform(T_ZERO, 1);
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
            buttonGrid.updateTransform(T_ZERO, 1);
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
