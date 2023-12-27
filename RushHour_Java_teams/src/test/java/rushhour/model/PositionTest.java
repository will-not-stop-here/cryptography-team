/**
 * @UnitTests for the Position class of the RushHour game
 * 
 * @author Emma
 */

package rushhour.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


import org.junit.jupiter.api.Test;

public class PositionTest {
    
    public final Position p1 = new Position(5, 5);
    public final Position p2 = new Position(5, 5);
    public final Position p3 = new Position(1,1);

    @Test
    public void testToString () {
        assertEquals(p1.toString(), "[Row: 5 Col: 5]");
        assertEquals(p3.toString(), "[Row: 1 Col: 1]");
    }

    @Test
    public void testGetters () {
        assertEquals(p1.getRow(), 5);
        assertEquals(p1.getCol(), 5);
    }

    @Test
    public void testEquality () {
        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
    }
}
