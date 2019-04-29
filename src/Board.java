import javax.swing.JButton;

/**
 * Board class
 * Abstraction for a grid of Cells
 */
public class Board {
    private Cell[][] grid;
    private int w, h;
    private int generationCount;

    /**
     * Constructor for Board
     * @param x Width of board
     * @param y Height of board
     */
    public Board(int x, int y) {
        grid = new Cell[x][y];
        w = x;
        h = y;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                grid[i][j] = new Cell();
            }
        }
        generationCount = 0;
    }

    /**
     * Get the state of a certain cell
     * @param x The x-coordinate of the cell
     * @param y The y-coordinate of the cell
     * @return The state of the cell at (x, y)
     */
    public boolean getCellState(int x, int y) {
        return grid[x][y].getState();
    }

    /**
     * Set the state of a certain cell
     * @param x The x-coordinate of the cell
     * @param y The y-coordinate of the cell
     * @param s The state to set the cell at (x, y) to
     */
    public void setCellState(int x, int y, boolean s) {
        grid[x][y].setState(s);
    }

    /**
     * Get the JButton of a certain cell
     * @param x The x-coordinate of the cell
     * @param y The y-coordinate of the cell
     * @return The JButton associated with the cell at (x, y)
     */
    public JButton getCellButton(int x, int y) {
        return grid[x][y].getButton();
    }

    /**
     * Getter for width
     * @return The width of the board
     */
    public int getWidth() {
        return w;
    }

    /**
     * Getter for height
     * @return The height of the board
     */
    public int getHeight() {
        return h;
    }

    // TODO: Implement more efficient algorithm with data structures
    public void evolve() {
        boolean[][] nextGen = new boolean[w][h];
        for (int i = 0; i < nextGen.length; i++) {
            for (int j = 0; j < nextGen[i].length; j++) {
                nextGen[i][j] = applyRules(i, j);
            }
        }
        for (int i = 0; i < nextGen.length; i++) {
            for (int j = 0; j < nextGen[i].length; j++) {
                setCellState(i, j, nextGen[i][j]);
            }
        }
        generationCount++;
    }

    public void clear() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                setCellState(i, j, false);
            }
        }
        generationCount = 0;
    }

    private boolean applyRules(int x, int y) {
        int count = countLiveNeighbors(x, y);
        if (getCellState(x, y)) { // cell is alive
            switch (count) {
            // death by solitude
            case 0:
                return false;
            case 1:
                return false;
            // still alive
            case 2:
                return true;
            case 3:
                return true;
            // more than 3 neighbors, death by overpopulation
            default:
                return false;
            }
        }
        // cell is dead
        if (count == 3) // new cell is born
            return true;
        return false; // dead cell is still dead
    }

    private int countLiveNeighbors(int x, int y) {
        int count = 0;
        // Check every cell around given coordinate
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                try {
                    if (getCellState(x + i, y + j))
                        count++;
                } catch (ArrayIndexOutOfBoundsException e) {
                    // Index outside of grid, meaning (x, y) on edge of grid. Can safely ignore
                }
            }
        }
        if (getCellState(x, y)) // don't count the cell itself as a neighbor
            count--;
        return count;
    }
}