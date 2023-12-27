/**
 * @UnitTests for the Direction enum of the RushHour game
 * @author Emma
 */

package rushhour.model;

import static org.junit.jupiter.api.Assertions.assertEquals;


import org.junit.jupiter.api.Test;

public class DirectionTest {
    
    @Test
    public void testEnum () {
        assertEquals(Direction.UP.toString(), "UP");
        assertEquals(Direction.DOWN.toString(), "DOWN");
        assertEquals(Direction.LEFT.toString(), "LEFT");
        assertEquals(Direction.RIGHT.toString(), "RIGHT");
    }

}
