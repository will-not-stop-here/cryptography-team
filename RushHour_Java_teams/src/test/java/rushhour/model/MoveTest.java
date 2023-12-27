/**
 * @UnitTests for the Move class of the RushHour game
 * 
 * @author Emma
 */

package rushhour.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MoveTest {
    /**
     * Since the move class is pretty much just a representation
     * of what car moved where, there's not much to test,
     * but I will write some test for credit's sake.
     *
     */

     @Test
     public void testToString () {
        Move m1 = new Move('a', Direction.LEFT);
        Move m2 = new Move('c', Direction.UP);
        Move m3 = new Move('f', Direction.RIGHT);
        Move m4 = new Move('z', Direction.DOWN);

        assertEquals(m1.toString(), "[Vehicle: a, Direction: LEFT]");
        assertEquals(m2.toString(), "[Vehicle: c, Direction: UP]");
        assertEquals(m3.toString(), "[Vehicle: f, Direction: RIGHT]");
        assertEquals(m4.toString(), "[Vehicle: z, Direction: DOWN]");
     }
}
