public class Board {
    private Cell[][] cells;

    public Board(){
        int DEFAULT_SIZE = 15;
        cells = new Cell[DEFAULT_SIZE][DEFAULT_SIZE];
    }

    public Board(int width, int height){
        cells = new Cell[width][height];
    }

    public void evolve(){
        // evolve Board based on four rules

    }

    public void initialize(){
        // initialize cells randomly

    }
}
