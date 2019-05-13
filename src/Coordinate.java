import java.util.Objects;

public class Coordinate {
    private final int xCoordinate, yCoordinate;

    /**
     * Constructor for Coordinate
     *
     * @param x The x coordinate
     * @param y The y coordinate
     */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return xCoordinate == that.xCoordinate &&
                yCoordinate == that.yCoordinate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xCoordinate, yCoordinate);
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", xCoordinate, yCoordinate);
    }
}
