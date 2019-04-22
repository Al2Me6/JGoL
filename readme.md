# (More Sane) Project Plan

Alvin Meng, Autin Mitra, Connor Mooney

2019-04-22

## Problem

The goal of the project is to implement John Conway’s Game of Life in Java. It is a zero-player game, where everything is determined by the initial state, not requiring any further input. Game of Life serves as a model for cellular automation, where sets of rules determines the creation and destruction of cells in a grid.

The game is created on a grid (i.e. the board), where cells are generated and destroyed based on four simple cases:

* Any live cell with one or no neighbors dies from solitude (removed from grid)
* Any live cell with 4+ neighbors dies by overpopulation (removed from grid)
* Any live cell with 2 or 3 neighbors survives (remains the same in grid)
* Any empty cell with three neighbors is populated (added to the grid)

For each generation, the game iterates through each cell on the board, applies the four preceding rules, and change the state of the cells accordingly.

At the very beginning, the user will choose the initially populated cells using the UI by clicking on specific cells in the grid, which will then turn black to indicate that the cell is alive (a dead cell is white). The user will then be presented with buttons to go to the next generation and to clear. The user may clear the board at any time and restart.

## Examples

| Initial state | Next state | Explanation |
| ------------- | ---------- | ----------- |
|               |            |             |
|               |            |             |
|               |            |             |
|               |            |             |
|               |            |             |
|               |            |             |
|               |            |             |
|               |            |             |
|               |            |             |

## Pseudocode

```
// driver
class JGoL
    define main method
        create new UI named ui
        set the default close operation of ui to JFrame.EXIT_ON_CLOSE
        pack ui using ui.pack() // inherited from JFrame
        make ui visible

// create and manage UI
class UI extends JFrame
    define private Board board

    // scrollable grid container for cells
    private class ScrollGridPanel extends JPanel
        define private GridLayout grid
        define private JScrollPanel sPanel
        define private GridLayoutPanel gPanel

        constructor with arguments w, h
            set grid width and height to w, h
            set the layout of gPanel to grid
            add gPanel to sPanel
            enable vertical and horizontal scroll on sPanel
            add sPanel to UI

        define method addToGrid() with argument JButton btn
            add btn to gPanel

    // control next generation and clear buttons
    private class ControlPanel extends JPanel
        implement a button listener class named NextGenBtnListener
            call board.evolve()

        implement a button listener class named ClearBtnListener
            call board.clear()

        constructor with no argument and no return
            create a JButton called nextGenBtn
            create a JButton called clearBtn
            set nextGenBtn's listener to NextGenBtnListener
            set clearBtn's listener to ClearBtnListener
            add nextGenBtn and clearBtn to UI

    constructor with no argument
        call JFrame superclass constructor with argument "JGoL"
        ask user for the width and height via a JOptionPane prompt
        set board as a new Board(width, height)
        // creating the grid
        create a ScrollGridPanel of (width, height) named scrollGridPanel
        for x = 0 to board.getHeight() - 1
            for y = 0 to board.getWidth() - 1
                call scrollGridPanel.addToGrid() with argument board.getButton(x, y)
        add ScrollGridPanel to UI
        // creating the button control panel
        create a ControlPanel named controlPanel
        add controlPanel to UI

// abstracts grids of cells
class Board
    define private Cell[][] grid
    define private integers w, h

    constructor with arguments width and height
        set grid to be an array of Cell[width][height]
        set w, h as width and height

    public method getCellState with arguments x, y and return type boolean
        return grid[x][y].getState

    public method setCellState with arguments x, y, and boolean s
        grid[x][y].setState(s)

    public method getCellButton with arguments x, y and return type JButton
        return grid[x][y].getButton

    public method getWidth with no argument and return type integer
        return w

    public method getHeight with no argument and return type integer
        return h

    public method evolve with no argument and no return
        define boolean[][] nextGen with dimensions w, h
        for every index x, y in grid
            set nextGen[x][y] to the returned value of applyRules(x, y)
        for every index x, y in nextGen
            call setCellState with arguments x, y, nextGen[x][y]

    public method clear with no argument and no return
        for every index x,y in grid
            call setCellState with arguments x, y, false

    private method applyRules with arguments x, y and return type boolean
        define integer count as countLiveNeighbors(x, y)

        if getCellState(x, y) returns true // cell is alive
            if count equals 0 or count equals 1
                return false // kill cell by solitude
            else if count equals 2 or count equals 3
                return true // cell is still alive
            else if count is greater than or equal to 4
                return false // kill cell by overpopulation
        else // cell is dead
            if count equals 3
                return true // cell is born
            else
                return false // nothing happens; dead cell is still dead

    private method countLiveNeighbors with arguments x, y and return type integer
        define integer count as 0

        for integer i from -1 to 1
            for integer j from -1 to 1
                try
                    if getCellState(x + i, y + j) returns true
                        add one to count
                catch array index error
                    do nothing // cell is outside of boundaries of grid
        if getCellState(x, y) returns true
            subtract one from count // input point is not a neighbor
        return count


// individual cells
class Cell
    define private boolean state
    define private JButton button

    constructor with no argument
        set state to false
        set button to a JButton with listener ToggleStateListener
        call colorize

    public method getState with no argument and return type boolean
        return state

    public method setState with boolean argument s and no return
        set state to the value of s
        call colorize

    public method getButton with no argument and return type JButton
        return button

    private method colorize with no argument and no return:
        if state is true:
            set button's color to black
        else:
            set button's color to white

    implement an action listener named ToggleStateListener
        set state to !state
        call colorize
```

## Method headers

```java
public class JGoL {
    public static void main(String[] args) {}
}

public class UI extends JFrame {
    private class ScrollGridPanel extends JPanel {
        public ScrollGridPanel(int w, int h) {}
        public addToGrid(JButton btn) {}
    }
    private class ControlPanel extends JPanel {
        private class NextGenBtnListener {
            public void actionPerformed(ActionEvent e) {}
        }
        private class ClearBtnListener {
            public void actionPerformed(ActionEvent e) {}
        }
        public ControlPanel() {}
    }
    public UI() {}
    private void printBoard() {}
}

public class Board {
    public Board(int width, int height) {}
    public boolean getCellState(int x, int y) {}
    public void setCellState(int x, int y, boolean s) {}
    public JButton getCellButton(int x, int y) {}
    public int getWidth() {}
    public int getHeight() {}
    public void evolve() {}
    public void clear() {}
    private boolean applyRules(int row, int column) {}
    private int countLiveNeighbors(int row, int column) {}
}

public class Cell {
    public Cell() {}
    public boolean getState() {}
    public void setState(boolean s) {}
    public JButton getButton() {}
    private void colorize() {}
    private class ToggleStateListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {}
    }
}
```