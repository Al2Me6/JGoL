import javax.swing.JButton;

public class Board {
    private Cell[][] grid;
    private int w, h;

    public Board(int x, int y) {
        grid = new Cell[x][y];
        w = x;
        h = y;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                grid[i][j] = new Cell();
            }
        }
    }

    public boolean getCellState(int x, int y) {
        return grid[x][y].getState();
    }

    public void setCellState(int x, int y, boolean s) {
        grid[x][y].setState(s);
    }

    public JButton getCellButton(int x, int y) {
        return grid[x][y].getButton();
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
    }

    public void clear() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                setCellState(i, j, false);
            }
        }
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