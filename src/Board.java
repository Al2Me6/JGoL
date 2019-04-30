import java.util.HashSet;

import javax.swing.JButton;

/**
 * Abstraction for a grid of Cells Handles game logic
 */
public class Board {
    private HashSet<Coordinate> cells;
    private int w, h;
    private int generationCount;

    /**
     * Constructor for Board
     *
     * @param x Width of board
     * @param y Height of board
     */
    public Board(int x, int y) {
        cells = new HashSet<Coordinate>();
        w = x;
        h = y;
        generationCount = 0;
    }

    public boolean getCellState(Coordinate c) {
        return cells.contains(c);
    }

    public void setCellState(Coordinate c, boolean s) {
        if (s) {
            cells.add(c);
        } else {
            cells.remove(c);
        }
    }

    public void toggleState(Coordinate c) {
        setCellState(c, !getCellState(c));
    }

    /**
     * @return The width of the board
     */
    public int getWidth() {
        return w;
    }

    /**
     * @return The height of the board
     */
    public int getHeight() {
        return h;
    }

    /**
     * @return The current generation count
     */
    public int getGenerationCount() {
        return generationCount;
    }

    /**
     * Applies the four rules to each cell, update generation count
     */
    public void evolve() {
        HashSet<Coordinate> nextGen = new HashSet<>();
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                Coordinate c = new Coordinate(i, j);
                if (applyRules(c)) {
                    nextGen.add(c);
                } else {
                    cells.remove(c);
                }
            }
        }
        cells = nextGen;
        generationCount++;
    }

    /**
     * Kill all cells and reset counter
     */
    public void clear() {
        cells.clear();
        generationCount = 0;
    }

    /**
     * Internal method for applying rules to a single cell
     *
     * @param x The x coordinate of the cell
     * @param y The y coordinate of the cell
     */
    private boolean applyRules(Coordinate c) {
        int count = countLiveNeighbors(c);
        if (cells.contains(c)) { // cell is alive
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


    private int countLiveNeighbors(Coordinate c) {
        int count = 0;
        // Check every cell around given coordinate
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (cells.contains(new Coordinate(c.getX() + i, c.getY() + j)))
                    count++;
            }
        }
        if (cells.contains(c)) // don't count the cell itself as a neighbor
            count--;
        return count;
    }
}