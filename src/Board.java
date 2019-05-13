import java.util.HashSet;

/**
 * A grid of cells, handles game logic
 */
public class Board {
    private HashSet<Coordinate> liveCells;
    private int width, height;
    private int genCount;

    /**
     * Constructor for Board
     *
     * @param w: Width of the board
     * @param h: Height of the board
     */
    public Board(int w, int h) {
        liveCells = new HashSet<>();
        width = w;
        height = h;
        genCount = 0;
    }

    /**
     * Getter for cell state
     *
     * @return The current state of the cell
     */
    public boolean getCellState(Coordinate c) {
        return liveCells.contains(c);
    }

    /**
     * Setter for cell state
     * @param c The coordinates of the cell
     * @param state The desired state of the cell
     */
    public void setCellState(Coordinate c, boolean state) {
        if (state) {
            liveCells.add(c);
        } else {
            liveCells.remove(c);
        }
    }

    public void toggleState(Coordinate c) {
        setCellState(c, !getCellState(c));
    }

    /**
     * Getter for width
     *
     * @return The width of the board
     */
    public int getWidth() {
        return width;
    }

    /**
     * Getter for height
     *
     * @return The height of the board
     */
    public int getHeight() {
        return height;
    }

    /**
     * Getter for generation count
     *
     * @return The current generation count
     */
    public int getGenCount() {
        return genCount;
    }

    /**
     * Applies the four rules to each cell, update generation count
     *
     * @return The list of cells whose status has changed
     */
    public HashSet<Coordinate> evolve() {
        HashSet<Coordinate> nextGen = deepcopy(liveCells);
        HashSet<Coordinate> delta = new HashSet<>();
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                Coordinate c = new Coordinate(i, j);
                if (applyRules(c)) {
                    if (nextGen.add(c))
                        delta.add(c);
                } else {
                    if (nextGen.remove(c))
                        delta.add(c);
                }
            }
        }
        liveCells = nextGen;
        genCount++;
        return delta;
    }

    /**
     * Kill all live cells and reset counter
     *
     * @return The list of cells whose status has changed
     */
    public HashSet<Coordinate> clear() {
        HashSet<Coordinate> res = deepcopy(liveCells);
        liveCells.clear();
        genCount = 0;
        return res;
    }

    /**
     * Apply rules to a single cell
     *
     * @param c The coordinates of the cell
     */
    private boolean applyRules(Coordinate c) {
        int count = countLiveNeighbors(c);
        if (liveCells.contains(c)) { // cell is alive
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
        return count == 3; // revive dead cell if surrounded by exactly 3 neighbors
    }

    /**
     * Count the number of live neighbors a cell has
     *
     * @param c The coordinates of the cell
     * @return The number of live cells
     */
    private int countLiveNeighbors(Coordinate c) {
        int ct = 0;
        // Check every cell around given coordinate
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (liveCells.contains(new Coordinate(c.x() + i, c.y() + j))) {
                    ct++;
                }
            }
        }
        if (liveCells.contains(c)) // don't count the cell itself as a neighbor
            ct--;
        return ct;
    }

    /**
     * Make a deep copy of a HashSet
     *
     * @return A copy of the original HashSet
     */
    private static HashSet<Coordinate> deepcopy(HashSet<Coordinate> source) {
        HashSet<Coordinate> copy = new HashSet<>();
        for (Coordinate c : source)
            copy.add(new Coordinate(c.x(), c.y()));
        return copy;
    }
}
