import java.util.HashSet;

/**
 * Abstraction for a grid of Cells
 * Handles game logic
 */
public class Board {
    private HashSet<Coordinate> liveCells;
    private Coordinate dimensions;
    private int generationCount;

    /**
     * Constructor for Board
     *
     * @param c: size of the board
     */
    public Board(Coordinate c) {
        liveCells = new HashSet<>();
        dimensions = c;
        generationCount = 0;
    }

    public boolean getCellState(Coordinate c) {
        return liveCells.contains(c);
    }

    public void setCellState(Coordinate c, boolean s) {
        if (s) {
            liveCells.add(c);
        } else {
            liveCells.remove(c);
        }
    }

    public void toggleState(Coordinate c) {
        setCellState(c, !getCellState(c));
    }

    /**
     * @return The width of the board
     */
    public int getWidth() {
        return dimensions.x();
    }

    /**
     * @return The height of the board
     */
    public int getHeight() {
        return dimensions.y();
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
    public HashSet<Coordinate> evolve() {
        HashSet<Coordinate> nextGen = clone(liveCells);
        HashSet<Coordinate> delta = new HashSet<>();
        for (int i = 0; i < dimensions.x(); i++) {
            for (int j = 0; j < dimensions.y(); j++) {
                Coordinate c = new Coordinate(i, j);
                if (applyRules(c)) {
                    nextGen.add(c);
                    delta.add(c);
                } else {
                    nextGen.remove(c);
                    delta.add(c);
                }
            }
        }
        liveCells = nextGen;
        generationCount++;
        return delta;
    }

    /**
     * Kill all liveCells and reset counter
     */
    public HashSet<Coordinate> clear() {
        HashSet<Coordinate> res = clone(liveCells);
        liveCells.clear();
        generationCount = 0;
        return res;
    }

    /**
     * Internal method for applying rules to a single cell
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


    private int countLiveNeighbors(Coordinate c) {
        int ct = 0;
        // Check every cell around given coordinate
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (liveCells.contains(new Coordinate(c.x() + i, c.y() + j)))
                    ct++;
            }
        }
        if (liveCells.contains(c)) // don't count the cell itself as a neighbor
            ct--;
        System.out.println(c.toString() + ": " + ct);
        return ct;
    }

    private static HashSet<Coordinate> clone(HashSet<Coordinate> original) {
        HashSet<Coordinate> cloned = new HashSet<>();
        for (Coordinate c : original)
            cloned.add(new Coordinate(c.x(), c.y()));
        return cloned;
    }
}
