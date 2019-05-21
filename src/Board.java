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
     * @return current state of the cell
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

    public HashSet<Coordinate> getLiveCells() {
        return liveCells;
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
        // cannot change the set that is being iterated over while iterating, so store delta separately
        HashSet<Coordinate> births = new HashSet<>(); // all cells to be born
        HashSet<Coordinate> deaths = new HashSet<>(); // all cells to be killed
        HashSet<Coordinate> checked = new HashSet<>(); // keep track of already-checked cells
        // since a cell can only be born if it has live neighbors, iterating around live cells is sufficient to catch births
        for (Coordinate c : liveCells) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    Coordinate test = new Coordinate(c.x() + i, c.y() + j);
                    if (checked.add(test)) { // if test is already (a member of) checked, skip
                        // to be born if...
                        if (applyRules(test)) { // ...will be alive...
                            if (!getCellState(test)) { // ...and is currently dead
                                births.add(test);
                            }
                        // to be killed if...
                        } else { // ...will be dead...
                            if (getCellState(test)) { // ...and is currently alive
                                deaths.add(test);
                            }
                        }
                    }
                }
            }
        }
        // apply delta
        liveCells.addAll(births);
        liveCells.removeAll(deaths);
        genCount++;
        births.addAll(deaths); // overall delta to return
        computeTime = System.nanoTime() - startTime;
        return births;
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
        return delta;
    }

    /**
     * Apply rules to a single cell
     *
     * @param c the coordinates of the cell
     */
    private boolean applyRules(Coordinate c) {
        int count = countLiveNeighbors(c);
        if (getCellState(c)) { // cell is alive
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
     * @return the number of live cells around that cell
     */
    private int countLiveNeighbors(Coordinate c) {
        int ct = 0;
        // Check every cell around given coordinate
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (getCellState(new Coordinate(c.x() + i, c.y() + j))) {
                    ct++;
                }
            }
        }
        if (getCellState(c)) {// don't count the cell itself as a neighbor
            ct--;
        }
        return ct;
    }

    /**
     * Make a deep copy of a HashSet
     *
     * @return a copy of the original HashSet
     */
    private static HashSet<Coordinate> deepcopy(HashSet<Coordinate> source) {
        HashSet<Coordinate> copy = new HashSet<>();
        for (Coordinate c : source) {
            copy.add(new Coordinate(c.x(), c.y()));
        }
        return copy;
    }
}
