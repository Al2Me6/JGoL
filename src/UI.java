import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashSet;

/**
 * Manage UI components
 */
public class UI extends JFrame {
    private Board board;
    private ButtonGrid buttonGrid;

    /**
     * Initialize the JGoL UI, create a board of a user-specified size
     */
    public UI() {
        setTitle("JGoL");
        setIconImage(new ImageIcon(getClass().getResource("logo.png")).getImage());
        setPreferredSize(Consts.STARTING_SIZE);
        setLayout(new BorderLayout());

        board = new Board();
        buttonGrid = new ButtonGrid(board);

        // board, with extra navigation (scroll) buttons
        JPanel gameBoard = new JPanel(new BorderLayout());
        // hide buttons that don't currently fit on screen because of zoom setting
        JScrollPane buttonGridOverflow = new JScrollPane(buttonGrid, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        buttonGridOverflow.getHorizontalScrollBar().setUnitIncrement(0);
        buttonGridOverflow.getVerticalScrollBar().setUnitIncrement(0);
        gameBoard.add(buttonGridOverflow, BorderLayout.CENTER);
        // navigation buttons
        JButton upButton = new JButton("⏫");
        upButton.addActionListener(ae -> buttonGrid.updateTransform(Consts.T_UP));
        gameBoard.add(upButton, BorderLayout.NORTH);
        JButton downButton = new JButton("⏬");
        downButton.addActionListener(ae -> buttonGrid.updateTransform(Consts.T_DOWN));
        gameBoard.add(downButton, BorderLayout.SOUTH);
        JButton rightButton = new JButton("⏩");
        rightButton.addActionListener(ae -> buttonGrid.updateTransform(Consts.T_RIGHT));
        gameBoard.add(rightButton, BorderLayout.LINE_END);
        JButton leftButton = new JButton("⏪");
        leftButton.addActionListener(ae -> buttonGrid.updateTransform(Consts.T_LEFT));
        gameBoard.add(leftButton, BorderLayout.LINE_START);
        add(gameBoard, BorderLayout.CENTER);

        Controls controls = new Controls();
        add(controls, BorderLayout.SOUTH);

        pack();

        // WASD keys for infinite scroll
        // pressing Z resets to the home coordinate
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(ke -> {
            if (ke.getID() == KeyEvent.KEY_TYPED) {
                char code = Character.toLowerCase(ke.getKeyChar());
                switch (code) {
                    case 'w':
                        buttonGrid.updateTransform(Consts.T_UP);
                        break;
                    case 's':
                        buttonGrid.updateTransform(Consts.T_DOWN);
                        break;
                    case 'd':
                        buttonGrid.updateTransform(Consts.T_RIGHT);
                        break;
                    case 'a':
                        buttonGrid.updateTransform(Consts.T_LEFT);
                        break;
                    case 'z':
                        buttonGrid.updateTransform(Consts.T_ZERO);
                        break;
                }
            }
            return true;
        });
    }

    /**
     * UI control elements
     */
    private class Controls extends JPanel {
        private JButton autoButton;
        private JLabel genCounter;
        private JLabel computeTimeLabel;
        private boolean autoEnabled = false;
        private int autoDelay = 700;

        /**
         * Initialize all control elements
         */
        public Controls() {
            setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

            // display current status
            JPanel countersPanel = new JPanel(new FlowLayout());
            // display current generation count
            genCounter = new JLabel();
            genCounter.setHorizontalAlignment(SwingConstants.CENTER);
            countersPanel.add(genCounter);
            // display compute time for previous iteration
            computeTimeLabel = new JLabel();
            countersPanel.add(computeTimeLabel);
            add(countersPanel);

            // main control buttons
            JPanel controlsPanel = new JPanel(new FlowLayout());
            // On button click, evolve the board once
            JButton nextGen = new JButton("Evolve state");
            nextGen.addActionListener(ae -> evolveBoard());
            controlsPanel.add(nextGen);
            // On button click, clear the board
            JButton clear = new JButton("Clear board");
            clear.addActionListener(ae -> clearBoard());
            controlsPanel.add(clear);
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
                                Thread.sleep(Consts.AUTO_DELAY_MAX - (autoDelay - Consts.AUTO_DELAY_MIN));
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    autoThread.start();
                }
                updateAutoButtonText();
            });
            controlsPanel.add(autoButton);
            JButton homeButton = new JButton("Home");
            homeButton.addActionListener(ae -> buttonGrid.updateTransform(Consts.T_ZERO));
            controlsPanel.add(homeButton);
            add(controlsPanel);

            // sliders
            JPanel slidersPanel = new JPanel(new FlowLayout());
            // change autoevolve speed
            slidersPanel.add(new JLabel("Autoevolve speed:"));
            JSlider autoSpeedSlider = new JSlider(JSlider.HORIZONTAL, Consts.AUTO_DELAY_MIN, Consts.AUTO_DELAY_MAX, autoDelay);
            autoSpeedSlider.setMinorTickSpacing(50);
            autoSpeedSlider.setMajorTickSpacing(200);
            autoSpeedSlider.setPaintTicks(true);
            autoSpeedSlider.addChangeListener(ce -> autoDelay = ((JSlider) ce.getSource()).getValue());
            slidersPanel.add(autoSpeedSlider);
            // change button size, aka zoom
            slidersPanel.add(new JLabel("Zoom:"));
            JSlider zoomSlider = new JSlider(JSlider.HORIZONTAL, Consts.ZOOM_MIN, Consts.ZOOM_MAX, Consts.INITIAL_BUTTON_SIZE);
            zoomSlider.setMinorTickSpacing(1);
            zoomSlider.setMajorTickSpacing(5);
            zoomSlider.setPaintTicks(true);
            zoomSlider.setPaintLabels(true);
            zoomSlider.addChangeListener(ce -> buttonGrid.updateButtonSize(((JSlider) ce.getSource()).getValue()));
            slidersPanel.add(zoomSlider);
            add(slidersPanel);

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
            buttonGrid.updateTransform(Consts.T_ZERO);
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
        return String.format("%,.3f%s", decimalTime, Consts.TIME_UNITS[ct]);
    }
}
