public class Coordinate {
    private final int X, Y;

    public Coordinate(int x, int y) {
        X = x;
        Y = y;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public boolean equals(Coordinate c) {
        return (X == c.getX() && Y == c.getY()) ? true : false;
    }
}