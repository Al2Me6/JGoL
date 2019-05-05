import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;

/**
 * Manage UI components
 */
public class UI extends JFrame {
    private Board board;
    private ButtonGrid buttonGrid;

    public UI() {
        int width = Integer.parseInt(JOptionPane.showInputDialog(null, "Board width:"));
        int height = Integer.parseInt(JOptionPane.showInputDialog(null, "Board height:"));

        board = new Board(new Coordinate(width, height));

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
            gridPanel.setLayout(new GridLayout(board.getWidth(), board.getHeight()));

            buttons = new CellButton[board.getWidth()][board.getHeight()];
            for (int i = 0; i < board.getWidth(); i++) {
                for (int j = 0; j < board.getHeight(); j++) {
                    buttons[i][j] = new CellButton(new Coordinate(i, j), 15);
                    gridPanel.add(buttons[i][j]);
                }
            }

            add(gridPanel);
        }

        private class CellButton extends JButton {
            private Coordinate coordinate;

            public CellButton(Coordinate c, int size) {
                coordinate = c;
                setButtonSize(size);
                colorize();
                addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        board.toggleState(coordinate);
                        colorize();
                    }
                });
            }

            public void colorize() {
                setBackground(board.getCellState(coordinate) ? Color.BLACK : Color.WHITE);
            }

            public void setButtonSize(int px) {
                setPreferredSize(new Dimension(px, px));
            }
        }

        public void colorizeButton(Coordinate c) {
            buttons[c.x()][c.y()].colorize();
        }
    }

    /**
     * UI Components with settings control Includes buttons for clearing, evolving, and autoevolving
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
            nextGen.addActionListener(new ActionListener() {
                // On button click, evolve the board once
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateBoard(board.evolve());
                    updateGenerationCounter();
                }
            });
            add(nextGen);

            JButton clear = new JButton("Clear board");
            clear.addActionListener(new ActionListener() {
                // On button click, clear the board
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateBoard(board.clear());
                    updateGenerationCounter();
                }
            });
            add(clear);

            autoevolve = new JButton("Autoevolve");
            autoevolve.addActionListener(new ActionListener() {
                // On button click, start auto evolve
                @Override
                public void actionPerformed(ActionEvent e) {
                }
            });
            add(autoevolve);

            generationCounter = new JLabel();
            updateGenerationCounter();
            add(generationCounter);
        }

        /**
         * Update the generation counter
         */
        private void updateGenerationCounter() {
            generationCounter.setText(String.format("Current generation: %d", board.getGenerationCount()));
        }

        private void updateBoard(HashSet<Coordinate> delta) {
            for (Coordinate c : delta)
                buttonGrid.colorizeButton(c);
        }
    }
}
