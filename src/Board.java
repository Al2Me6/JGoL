public class Board {
    private Cell[][] cells;

    public Board(){
        cells = new Cell[200][200];
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
