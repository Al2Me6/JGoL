public class Board {
    private Cell[][] cells;

    public Board(){
        int DEFAULT_SIZE = 15;
        cells = new Cell[DEFAULT_SIZE][DEFAULT_SIZE];
        initialize();
    }

    public Board(int width, int height){
        cells = new Cell[width][height];
        initialize();
    }

    public void evolve(){
        // evolve Board based on four rules

    }

    private void initialize(){
        // initialize cells randomly

    }
}
