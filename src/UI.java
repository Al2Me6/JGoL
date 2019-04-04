public class UI {

    private Board board;

    public UI(int sizeX, int sizeY){
        board = new Board(sizeX, sizeY);
    }
    public UI(Board b){
        board = b;
    }
}
