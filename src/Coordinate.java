import java.util.Objects;

public class Coordinate {
    private final int xCoordinate, yCoordinate;

    /**
     * Create a new coordinate at (x, y)
     *
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Coordinate(int x, int y) {
        xCoordinate = x;
        yCoordinate = y;
    }

    /**
     * Getter for x coordinate
     *
     * @return x-coordinate
     */
    public int x() {
        return xCoordinate;
    }

    /**
     * Getter for y coordinate
     *
     * @return y y-coordinate
     */
    public int y() {
        return yCoordinate;
    }

    // Override default methods for comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return xCoordinate == that.xCoordinate && yCoordinate == that.yCoordinate;
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
