public class Cell {
    private boolean state;

    public Cell() {
        state = false;
    }

    public Cell(boolean s) {
        state = s;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean s) {
        state = s;
    }

    public void toggleState(){
        state = !state;
    }
}