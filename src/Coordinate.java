public class Coordinate {
    private final int xCoordinate, yCoordinate;

    public Coordinate(int x, int y) {
        xCoordinate = x;
        yCoordinate = y;
    }

    public int x() {
        return xCoordinate;
    }

    public int y() {
        return yCoordinate;
    }

    public boolean equals(Coordinate c) {
        return (xCoordinate == c.x() && yCoordinate == c.y()) ? true : false;
    }
}