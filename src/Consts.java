import java.awt.Color;
import java.awt.Dimension;

/**
 * Constants and configurations needed by JGoL
 */
class Consts {
    // configurable (at your own risk!) properties
    static final Dimension STARTING_SIZE = new Dimension(1400, 900);
    // increasing the two below allows potentially smaller zoom at the cost of startup speed and memory consumption
    static final int MAX_GRID_WIDTH = 200;
    static final int MAX_GRID_HEIGHT = 200;
    static final int ZOOM_MIN = 5;
    static final int ZOOM_MAX = 20;
    static final int INITIAL_BUTTON_SIZE = 15;
    static final int SCROLL_INCREMENT = 4;
    static final Color ALIVE_COLOR = Color.BLACK;
    static final Color DEAD_COLOR = Color.WHITE;
    static final int AUTO_DELAY_MIN = 5; // don't decrease past 5; otherwise lockups WILL occur
    static final int AUTO_DELAY_MAX = 805;
    // constants, editing these will break stuff
    static final int T_UP = 0;
    static final int T_DOWN = 1;
    static final int T_LEFT = 2;
    static final int T_RIGHT = 3;
    static final int T_ZERO = 4;
    static final String[] TIME_UNITS = new String[] { "ns", "μs", "ms", "s" };
}
