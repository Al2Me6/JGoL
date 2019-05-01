import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Manage UI components
 */
public class UI extends JFrame {
    private Board board;
    private Controls controls;
    private Coordinate dimensions;

    public UI() {
        int width = Integer.parseInt(JOptionPane.showInputDialog(null, "Board width:"));
        int height = Integer.parseInt(JOptionPane.showInputDialog(null, "Board height:"));
        dimensions = new Coordinate(width, height);
        board = new Board(dimensions);
        ButtonGrid buttonGrid = new ButtonGrid(dimensions);
        add(buttonGrid, BorderLayout.CENTER);
        controls = new Controls();
        add(controls, BorderLayout.SOUTH);
//        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private class ButtonGrid extends JPanel {
        private CellButton[][] buttons;
        private Panel gridPanel;

        public ButtonGrid(Coordinate c) {
            gridPanel = new Panel();
            gridPanel.setLayout(new GridLayout(c.x(), c.y()));
            buttons = new CellButton[c.x()][c.y()];
            for (int i = 0; i < c.x(); i++) {
                for (int j = 0; j < c.y(); j++) {
                    buttons[i][j] = new CellButton(new Coordinate(i, j), 10);
                    gridPanel.add(buttons[i][j]);
                }
            }
            add(gridPanel);
        }

        private class CellButton extends JButton {
            private JButton button;
            private Coordinate coordinate;

            public CellButton(Coordinate c, int size) {
                coordinate = c;
                button = new JButton("");
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        board.toggleState(coordinate);
                        colorize();
                    }
                });
                colorize();
                setButtonSize(size);
            }

            public void colorize() {
                button.setBackground(board.getCellState(coordinate) ? Color.BLACK : Color.WHITE);
            }

            public void setButtonSize(int px) {
                button.setPreferredSize(new Dimension(px, px));
            }

            public Coordinate getCoordinate() {
                return coordinate;
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

        /**
         * The Constructor for controls Adds all the buttons
         */
        public Controls() {
            JButton nextGen = new JButton("Evolve state");
            nextGen.addActionListener(new ActionListener() {
                // On button click, evolve the board once
                @Override
                public void actionPerformed(ActionEvent e) {
                    board.evolve();
                    updateGenerationCounter();
                }
            });
            add(nextGen);

            JButton clear = new JButton("Clear board");
            clear.addActionListener(new ActionListener() {
                // On button click, clear the board
                @Override
                public void actionPerformed(ActionEvent e) {
                    board.clear();
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
            add(generationCounter);
            updateGenerationCounter();
        }

        /**
         * Updates the generation counter
         */
        private void updateGenerationCounter() {
            generationCounter.setText(String.format("Current generation: %d", board.getGenerationCount()));
        }
    }
}
