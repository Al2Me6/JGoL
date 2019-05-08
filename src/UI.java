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

    public UI() {
        board = new Board(userInput("Board width:"), userInput("Board height"));

        buttonGrid = new ButtonGrid();
        add(buttonGrid, BorderLayout.CENTER);

        Controls controls = new Controls();
        add(controls, BorderLayout.SOUTH);

        pack();
    }

    private class ButtonGrid extends JPanel {
        private CellButton[][] buttons;

        public ButtonGrid() {
            Panel gridPanel = new Panel();
            gridPanel.setLayout(new GridLayout(board.getHeight(), board.getWidth()));

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
    }

    /**
     * UI Components with settings control Includes buttons for clearing, evolving,
     * and autoevolving
     */
    private class Controls extends JPanel {
        private JButton autoevolve;
        private JLabel generationCounter;
        private JSlider sizeSlider;

        /**
         * The Constructor for controls Adds all the buttons
         */
        public Controls() {
            setLayout(new GridLayout(3, 1));
            setSize(200, 100);
            // setPreferredSize(new Dimension(200, 100));

            Panel controlButtonsPanel = new Panel(new FlowLayout());
            add(controlButtonsPanel);

            JButton nextGen = new JButton("Evolve state");
            // On button click, evolve the board once
            nextGen.addActionListener(e -> {
                updateBoard(board.evolve());
                updateGenCounter();
            });
            controlButtonsPanel.add(nextGen);

            JButton clear = new JButton("Clear board");
            // On button click, clear the board
            clear.addActionListener(e -> {
                updateBoard(board.clear());
                updateGenCounter();
            });
            controlButtonsPanel.add(clear);

            autoevolve = new JButton("Autoevolve");
            // On button click, start auto evolve
            autoevolve.addActionListener(e -> {
            });
            controlButtonsPanel.add(autoevolve);

            generationCounter = new JLabel();
            updateGenCounter();
            add(generationCounter);

            sizeSlider = new JSlider(JSlider.HORIZONTAL, 5, 30, INITIAL_BUTTON_SIZE);
            sizeSlider.setMinorTickSpacing(1);
            sizeSlider.setMajorTickSpacing(5);
            sizeSlider.setPaintTicks(true);
            sizeSlider.setPaintLabels(true);
            sizeSlider.addChangeListener(e -> {
                buttonGrid.setButtonSize(((JSlider) e.getSource()).getValue());
                refresh();
            });
            add(sizeSlider);
        }

        /**
         * Update the generation counter
         */
        private void updateGenCounter() {
            generationCounter.setText(String.format("Current generation: %d", board.getGenCount()));
        }

        private void updateBoard(HashSet<Coordinate> delta) {
            for (Coordinate c : delta)
                buttonGrid.colorizeButtons(c);
        }
    }

    private int userInput(String question) {
        while (true) {
            try {
                return Integer.parseInt(JOptionPane.showInputDialog(null, question));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please input a valid integer!", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refresh() {
        revalidate();
        repaint();
    }
}
