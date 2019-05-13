import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

/**
 * Manage UI components
 */
public class UI extends JFrame {
    private Board board;
    private ButtonGrid buttonGrid;
    private final int INITIAL_BUTTON_SIZE = 15;
    private final Dimension STARTING_SIZE = new Dimension(800, 600);
    private final int SCROLL_INCREMENT = 15;

    public UI() {
        board = new Board(userInput("Board width:"), userInput("Board height"));

        buttonGrid = new ButtonGrid();
        JScrollPane scrollPane = new JScrollPane(buttonGrid);
        scrollPane.getHorizontalScrollBar().addAdjustmentListener(e -> {
            buttonGrid.refresh();
        });
        scrollPane.getVerticalScrollBar().addAdjustmentListener(e -> {
            buttonGrid.refresh();
        });
        scrollPane.getHorizontalScrollBar().setUnitIncrement(SCROLL_INCREMENT);
        scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_INCREMENT);
        add(scrollPane, BorderLayout.CENTER);

        Controls controls = new Controls();
        add(controls, BorderLayout.SOUTH);

        setPreferredSize(STARTING_SIZE);
        pack();
    }

    private class ButtonGrid extends JPanel {
        private CellButton[][] buttons;
        private JPanel gridPanel;

        public ButtonGrid() {
            gridPanel = new JPanel();
            gridPanel.setLayout(new GridLayout(board.getHeight(), board.getWidth(), -1, -1));
            buttons = new CellButton[board.getHeight()][board.getWidth()];
            for (int i = 0; i < board.getHeight(); i++) {
                for (int j = 0; j < board.getWidth(); j++) {
                    buttons[i][j] = new CellButton(new Coordinate(i, j), INITIAL_BUTTON_SIZE);
                    gridPanel.add(buttons[i][j]);
                }
            }
            add(gridPanel);
        }

        private class CellButton extends JButton {
            private Coordinate coordinate;

            public CellButton(Coordinate c, int size) {
                coordinate = c;
                setSize(size);
                setOpaque(true);
                colorize();
                addActionListener(e -> {
                    board.toggleState(coordinate);
                    colorize();
                });
            }

            public void colorize() {
                setBackground(board.getCellState(coordinate) ? Color.BLACK : Color.WHITE);
            }

            public void setSize(int px) {
                setPreferredSize(new Dimension(px, px));
                revalidate();
                repaint();
            }
        }

        public void colorizeButtons(Coordinate c) {
            buttons[c.x()][c.y()].colorize();
        }

        public void setButtonSize(int size) {
            for (CellButton[] row : buttons) {
                for (CellButton b : row)
                    b.setSize(size);
            }
        }

        public void refresh() {
            gridPanel.revalidate();
            gridPanel.repaint();
        }
    }

    /**
     * Control UI components
     */
    private class Controls extends JPanel {
        private JButton autoevolve;
        private JLabel genCounter;
        private JSlider sizeSlider;
        private boolean autoevolveState = false;

        /**
         * Constructor for Controls
         */
        public Controls() {
            setLayout(new GridLayout(3, 1));

            JPanel controlButtonsPanel = new JPanel(new FlowLayout());

            // On button click, evolve the board once
            JButton nextGen = new JButton("Evolve state");
            nextGen.addActionListener(e -> {
                evolve();
            });
            controlButtonsPanel.add(nextGen);

            // On button click, clear the board
            JButton clear = new JButton("Clear board");
            clear.addActionListener(e -> {
                clear();
            });
            controlButtonsPanel.add(clear);

            // On button click, start auto evolve
            autoevolve = new JButton("Start autoevolve");
            autoevolve.addActionListener(e -> {
                autoevolveState = !autoevolveState;
                if (autoevolveState) {
                    Thread autoevolveThread = new Thread(() -> {
                        while (autoevolveState) {
                            evolve();
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException ex) {
                            }
                        }
                    });
                    autoevolveThread.start();
                }
                updateAutoevolveButtonText();
            });
            controlButtonsPanel.add(autoevolve);

            add(controlButtonsPanel);

            genCounter = new JLabel();
            genCounter.setHorizontalAlignment(SwingConstants.CENTER);
            updateGenCounter();
            add(genCounter);

            JPanel sliderPanel = new JPanel(new FlowLayout());

            sliderPanel.add(new JLabel("Zoom: "));

            sizeSlider = new JSlider(JSlider.HORIZONTAL, 5, 30, INITIAL_BUTTON_SIZE);
            sizeSlider.setMinorTickSpacing(1);
            sizeSlider.setMajorTickSpacing(5);
            sizeSlider.setPaintTicks(true);
            sizeSlider.setPaintLabels(true);
            sizeSlider.addChangeListener(e -> {
                buttonGrid.setButtonSize(((JSlider) e.getSource()).getValue());
            });
            sliderPanel.add(sizeSlider);

            add(sliderPanel);
        }

        private void evolve() {
            if (SwingUtilities.isEventDispatchThread()) {
                updateBoard(board.evolve());
            } else {
                SwingUtilities.invokeLater(() -> {
                    updateBoard(board.evolve());
                });
            }
        }

        private void clear() {
            autoevolveState = false;
            updateAutoevolveButtonText();
            updateBoard(board.clear());
        }

        private void updateAutoevolveButtonText() {
            autoevolve.setText((autoevolveState ? "Stop" : "Start") + " autoevolve");
        }

        /**
         * Update the generation counter
         */
        private void updateGenCounter() {
            genCounter.setText(String.format("Current generation: %d", board.getGenCount()));
        }

        private void updateBoard(HashSet<Coordinate> delta) {
            for (Coordinate c : delta)
                buttonGrid.colorizeButtons(c);
            updateGenCounter();
        }
    }

    /**
     * Asks the user a question, checking input
     *
     * @param question Question to ask user
     * @return User's response
     */
    private int userInput(String question) {
        int count = 0;
        while (true) {
            try {
                return Integer.parseInt(JOptionPane.showInputDialog(this, question));
            } catch (NumberFormatException e) {
                if (count++ >= 3) {
                    JOptionPane.showMessageDialog(this, "Too many tries!", "Input Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
                JOptionPane.showMessageDialog(this, "Please input a valid integer!", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
