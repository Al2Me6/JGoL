import java.util.HashSet;

/**
 * A grid of cells, handles game logic
 */
public class Board {
    private HashSet<Coordinate> liveCells;
    private int genCount;
    private long computeTime = 0;

    /**
     * Initialize a blank board
     */
    public Board() {
        liveCells = new HashSet<>();

        genCount = 0;
    }

    /**
     * Getter for cell state
     *
     * @param c coordinates of the cell
     * @return  current state of the cell
     */
    public boolean getCellState(Coordinate c) {
        return liveCells.contains(c);
    }

    /**
     * Setter for cell state
     *
     * @param c     coordinates of the cell
     * @param state desired state of the cell
     */
    public void setCellState(Coordinate c, boolean state) {
        if (state) {
            liveCells.add(c);
        } else {
            liveCells.remove(c);
        }
    }

    /**
     * Flip the cell's state
     *
     * @param c coordinates of the cell
     */
    public void toggleState(Coordinate c) {
        setCellState(c, !getCellState(c));
    }

    /**
     * Getter for generation count
     *
     * @return current generation count
     */
    public int getGenCount() {
        return genCount;
    }

    /**
     * Getter for compute time
     *
     * @return last compute time in nanoseconds
     */
    public long getComputeTime() {
        return computeTime;
    }

    /**
     * Apply the four rules to each cell, update generation count
     *
     * @return HashSet of cells whose status has changed
     */
    public HashSet<Coordinate> evolve() {
        long startTime = System.nanoTime();
        HashSet<Coordinate> add = new HashSet<>();
        HashSet<Coordinate> remove = new HashSet<>();
        HashSet<Coordinate> tested = new HashSet<>();
        for (Coordinate c : liveCells) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    Coordinate test = new Coordinate(c.x() + i, c.y() + j);
                    if (tested.add(test)) { // if test is already a member of tested, skip
                        if (applyRules(test)) {
                            if (!getCellState(test))
                                add.add(test);
                        } else {
                            if (getCellState(test))
                                remove.add(test);
                        }
                    }
                }
            }
        }
        liveCells.addAll(add);
        liveCells.removeAll(remove);
        genCount++;
        computeTime = System.nanoTime() - startTime;
        add.addAll(remove); // overall delta
        return add;
    }

    /**
     * Kill all live cells and reset counter
     *
     * @return HashSet of cells whose status has changed
     */
    public HashSet<Coordinate> clear() {
        HashSet<Coordinate> delta = deepcopy(liveCells);
        liveCells.clear();
        genCount = 0;
        computeTime = 0;
        return delta;
    }

    /**
     * Apply rules to a single cell
     *
     * @param c the coordinates of the cell
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
     * @param c the coordinates of the cell
     * @return  the number of live cells around that cell
     */
    private int countLiveNeighbors(Coordinate c) {
        int ct = 0;
        // Check every cell around given coordinate
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (getCellState(new Coordinate(c.x() + i, c.y() + j)))
                    ct++;
            }
        }
        if (getCellState(c)) // don't count the cell itself as a neighbor
            ct--;
        return ct;
    }

    /**
     * Make a deep copy of a HashSet
     *
     * @return a copy of the original HashSet
     */
    private static HashSet<Coordinate> deepcopy(HashSet<Coordinate> source) {
        HashSet<Coordinate> copy = new HashSet<>();
        for (Coordinate c : source)
            copy.add(new Coordinate(c.x(), c.y()));
        return copy;
    }
}
