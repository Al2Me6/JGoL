# Project Plan

Alvin Meng, Autin Mitra, Connor Mooney

2019-04-21

## Problem

The goal of the project is to implement John Conway’s Game of Life in Java. It is a zero-player game, where everything is determined by the initial state, requiring no further input. Game of Life serves as a model for cellular automation, where sets of rules determines the creation and destruction of cells in a grid.

The game is created on a grid (i.e. the board), where cells are generated and destroyed based on four simple cases:

* Any live cell with one or no neighbors dies from solitude (removed from grid)
* Any live cell with 4+ neighbors dies by overpopulation (removed from grid)
* Any live cell with 2 or 3 neighbors survives (remains the same in grid)
* Any empty cell with three neighbors is populated (added to the grid) 

For each generation, the game iterates through each cell on the board, applies the four preceding rules, and change the state of the cells accordingly. 

At the very beginning, the user will choose the initially populated cells using the UI by clicking on specific cells in the grid, which will then turn black to indicate that the cell is alive (a dead cell is white). Then, the user will click a button to indicate that they are done with the initial layout. The user will then be presented with buttons to go to the next generation and to clear. The user may clear the board at any time and restart.

## Examples

|Initial state|Final state|Explanation|
|---|---|---|
|   |   |   |
|   |   |   |
|   |   |   |
|   |   |   |
|   |   |   |
|   |   |   |
|   |   |   |
|   |   |   |
|   |   |   |

## Pseudocode

```
class JGoL 
    define main method
        create new UI called ui
        set the default close operation of ui to JFrame.EXIT_ON_CLOSE
        pack ui using ui.pack() (inherited from JFrame)
        make ui visible
        

class UI extends JFrame

    define private variable of type Board named board
    define a 2D array of JButtons named btns

    implement a button listener class named CellClickListener
        for JButton btn in btns
            set String name as btn.getActionCommand
            if name equals e.getActionCommand
                split name with "," into an array named coords
                set int x, y as the int value of coords[0] and coords[1]
                use board.setCell with arguments x, y, !board.getCell(x, y)
                set btn's color to green

    implement a button listener class named NextGenBtnListener
        call board.evolve() 
        call this.printBoard()

    implement a button listener class named ClearBtnListener
        board.clear()
        for JButton btn in btns
            set btn's color to white

    constructor with no argument
        call JFrame superclass constructor with argument "JGoL"
        ask user for the width and height via a JOptionPane prompt
        set board as a new Board(width, height)
        set btns as an array of JButtons with dimensions of width, height
        for each x from 0 to length - 1
            for each y from 0 to height - 1
                set btns[x][y] as a new Button with action command x + "," + y
                set the actionListener of btns[x][y] to CellClickListener
                color btns[x][y] white
                set btns[x][y]'s dimensions to 5x5
        set the window size to dimensions width * 5, height * 5 + 20
        setResizable as false
        create a JPanel called controls
        create a JButton called nextGenBtn
        create a JButton called clearBtn
        set nextGenBtn's listenser to NextGenBtnListener
        set clearBtn's listener to ClearBtnListener
        add nextGenBtn and clearBtn to controls
        use inhereted method add(JPanel p) with argument controls

    define method printBoard
        for x=0 to board.getHeight() - 1
            for y=0 to board.getWidth() - 1
                for JButton btn in btns
                    if btn.getActionCommand() is x + "," + y
                        if(board.getCell(x, y) is true)
                            set btn color to green
                        otherwise set the color white
        

class Board
    define private variable Cell[][] named grid
    define private int w, h

    constructor with arguments width and height
        set grid to be an array of Cell[width][height]
        set w, h as width and height

    method evolve with no arguments and no return
        define 2D array of type boolean[][] with width and height same as grid named nextGen
        for every index x in grid
            for every index y in grid[x]
                call applyRules with arguments x, y
                set the nextGen[x][y] to the value returned by applyRules
        for every index x in nextGen
            for every index y in nextGen[x]
                call setCell with args x, y, nextGen[x][y]

    method applyRules with arguments x, y and return type boolean
        define integer count as countLiveNeighbors(x, y)
        
        // All the cases
        if getCell(x, y) returns true
            if count equals 0 or count equals 1
                return false // return dead cell (solitude)
            else if count equals 2 or count equals 3
                return true // cell is still alive
            else if count is greater than or equal to 4
                return false  // return dead cell (overpopulation)
        else if count equals 3
            return true // cell is born
        return false // A new cell cannot be born, so return empty/dead cell

    method countLiveNeighbors with arguments x, y and returnf type integer
        define integer count as 0
        from integer i = -1 to i = 1
            from integer j = -1 to j = 1
            try
                if getCell(x + i, y + j) returns true
                    add one to count
            catch array index error
                do nothing (cell is outside of boundaries)
        if getCell(x, y) returns true
            subtract one from count // input point is not a neighbor
        return count

    method getCell with arguments x, y and return type boolean
        return grid[x][y].getState

    method setCell with arguments x, y, state
        call grid[x][y].setState with argument state

    method getWidth
        return w

    method getHeight
        return h

    method clear
        for every cell x,y in the grid
            call setCell with arguments x, y, false



class Cell
    define private boolean state

    constructor with no argument
        set state to false

    method getState with return type boolean
        return state

    method setState with boolean argument s and no return
        set state to the value of s
```

## Method headers

```java
public class JGoL {
    public static void main(String[] args) {}
}

public class UI {
    private class CellClickListener {
        public void actionPerformed(ActionEvent e) {}
    }
    private class NextGenBtnListener {
        public void actionPerformed(ActionEvent e) {}
    }
    private class ClearBtnListener {
        public void actionPerformed(ActionEvent e) {}
    }
    public UI() {}
    private void printBoard() {}
}

public class Board {
    public Board(int width, int height) {}
    public void evolve() {}
    private boolean applyRules(int row, int column) {}
    private int countLiveNeighbors(int row, int column) {}
    public boolean getCell(int x, int y) {}
    public void setCell(int x, int y, boolean s) {}
    public int getWidth() {}
    public int getHeight() {}
    public void clear() {}
}

public class Cell {
    public Cell(boolean s) {}
    public boolean getState() {}
    public void setState(boolean s) {}
}
```