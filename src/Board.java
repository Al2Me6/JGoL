import javax.swing.JButton;

/**
 * Abstraction for a grid of Cells Handles game logic
 */
public class Board {
    private Cell[][] grid;
    private int w, h;
    private int generationCount;

    /**
     * Constructor for Board
     *
     * @param x Width of board
     * @param y Height of board
     */
    public Board(int x, int y) {
        System.out.println("Board");
        grid = new Cell[x][y];
        w = x;
        h = y;
        generationCount = 0;
    }

    /**
     * Get the state of a certain cell
     *
     * @param x The x-coordinate of the cell
     * @param y The y-coordinate of the cell
     * @return The state of the cell at (x, y)
     */
    public boolean getCellState(int x, int y) {
        System.out.println("getCellState");
        return true;
    }

    /**
     * Set the state of a certain cell
     *
     * @param x The x-coordinate of the cell
     * @param y The y-coordinate of the cell
     * @param s The state to set the cell at (x, y) to
     */
    public void setCellState(int x, int y, boolean s) {
        System.out.println("setCellState");
    }

    /**
     * Get the JButton of a certain cell
     *
     * @param x The x-coordinate of the cell
     * @param y The y-coordinate of the cell
     * @return The JButton associated with the cell at (x, y)
     */
    public JButton getCellButton(int x, int y) {
        System.out.println("getCellButton");
        return new JButton();
    }

    /**
     * @return The width of the board
     */
    public int getWidth() {
        System.out.println("getWidth");
        return 0;
    }

    /**
     * @return The height of the board
     */
    public int getHeight() {
        System.out.println("getHeight");
        return 0;
    }

    /**
     * @return The current generation count
     */
    public int getGenerationCount() {
        System.out.println("getGenerationCount");
        return 0;
    }

    /**
     * Applies the four rules to each cell, update generation count
     */
    public void evolve() {
        System.out.println("evolve");
    }

    /**
     * Kill all cells and reset counter
     */
    public void clear() {
        System.out.println("clear");
    }

    /**
     * Internal method for applying rules to a single cell
     *
     * @param x The x coordinate of the cell
     * @param y The y coordinate of the cell
     */
    private boolean applyRules(int x, int y) {
        System.out.println("applyRules");
        int count = countLiveNeighbors(x, y);
        return true;
    }

    /**
     * Internal method for counting live neighbors around a certain cell
     *
     * @param x The x coordinate of the cell
     * @param y The y coordinate of the cell
     */
    private int countLiveNeighbors(int x, int y) {
        System.out.println("countLiveNeighbors");
        int count = 0;
        return count;
    }
}