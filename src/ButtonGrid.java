import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

/**
 * A grid of buttons corresponding to cells
 */
public class ButtonGrid extends JPanel {
    private Board board;
    private CellButton[][] buttons;
    private int transformX, transformY;

    /**
     * Create a new ButtonGrid with size corresponding to board
     */
    public ButtonGrid(Board board) {
        this.board = board;

        setLayout(new GridLayout(Consts.MAX_GRID_HEIGHT, Consts.MAX_GRID_WIDTH, -1, -1));
        buttons = new CellButton[Consts.MAX_GRID_WIDTH][Consts.MAX_GRID_HEIGHT];
        // wonky iteration order to translate from Cartesian to UI coordinate system
        for (int j = Consts.MAX_GRID_HEIGHT - 1; j >= 0; j--) {
            for (int i = 0; i < Consts.MAX_GRID_WIDTH; i++) {
                buttons[i][j] = new CellButton(new Coordinate(i, j), Consts.INITIAL_BUTTON_SIZE);
                add(buttons[i][j]);
            }
        }
        updateTransform(Consts.T_ZERO);
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
            if (btnC.x() >= 0 && btnC.y() >= 0 && btnC.x() < Consts.MAX_GRID_WIDTH && btnC.y() < Consts.MAX_GRID_HEIGHT) {
                buttons[(int) btnC.x()][(int) btnC.y()].colorize();
            }
        }
    }

    /**
     * Stage the parameters for the transformations on the board
     *
     * @param transform the id of the type of transportation that is to be performed
     */
    public void updateTransform(int transform) {
        // wipe all currently alive cells from board
        for (Coordinate c : board.getLiveCells()) {
            Coordinate btnC = board2button(c);
            if (btnC.x() >= 0 && btnC.y() >= 0 && btnC.x() < Consts.MAX_GRID_WIDTH && btnC.y() < Consts.MAX_GRID_HEIGHT) {
                buttons[(int) btnC.x()][(int) btnC.y()].setBackground(Consts.DEAD_COLOR);
            }
        }
        for (int i = 0; i < Consts.SCROLL_INCREMENT; i++) {
            switch (transform) {
                case Consts.T_UP:
                    transformY++;
                    break;
                case Consts.T_DOWN:
                    transformY--;
                    break;
                case Consts.T_RIGHT:
                    transformX++;
                    break;
                case Consts.T_LEFT:
                    transformX--;
                    break;
                case Consts.T_ZERO:
                    transformX = 0;
                    transformY = 0;
                    break;
            }
        }
        // repopulate board using new transformation
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
        private void colorize() {
            setBackground(board.getCellState(button2board(coordinate)) ? Consts.ALIVE_COLOR : Consts.DEAD_COLOR);
        }

        /**
         * Convert the coordinates of the button to the coordinates on the board
         *
         * @param c the coordinates of the button
         * @return the coordinates on the board
         */
        private Coordinate button2board(Coordinate c) {
            return new Coordinate(c.x() + transformX, c.y() + transformY);
        }
    }
}
