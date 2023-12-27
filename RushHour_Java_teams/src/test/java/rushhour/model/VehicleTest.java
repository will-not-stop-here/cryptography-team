package rushhour.model;

/**
 * @UnitTests for the Vehicle class of the RushHour game
 */

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * unit tests for the Vehicle class
 * @author Daphne
 */
public class VehicleTest {
    //Creating Vehicle Objects to be used in testing
    char HV = 'H';
    Position backHV = new Position(1, 2);
    Position frontHV = new Position(1, 3);
    Vehicle horizontalVehicle = new Vehicle(HV, backHV, frontHV); //Horizontal = back column < front column

    char VV = 'V';
    Position backVV = new Position(2, 4);
    Position frontVV = new Position(3, 4);
    Vehicle verticalVehicle = new Vehicle(VV, backVV, frontVV); //Horizontal = back row < front row


    //####################################################################
    //TESTING CLASS INSTANTIATION - symbol, back & front
    @Test
    public void vehicleSymbolTest() {
        //Setup - completed at top, Vehicles created
        //Invoke
        char actualHV = horizontalVehicle.getSymbol();
        char actualVV = verticalVehicle.getSymbol();

        //Analyze
        assertEquals(HV, actualHV);
        assertEquals(VV, actualVV);
    }

    @Test
    public void vehicleBackTest() {
        //Setup - completed at top, Vehicles created
        int expectedBackRowHV = 1;
        int expectedBackColHV = 2;
        int expectedBackRowVV = 2;
        int expectedBackColVV = 4;

        //Invoke
        Position actualBackHV = horizontalVehicle.getBack();
        int actualBackRowHV = horizontalVehicle.getBack().getRow();
        int actualBackColHV = horizontalVehicle.getBack().getCol();
        Position actualBackVV = verticalVehicle.getBack();
        int actualBackRowVV = verticalVehicle.getBack().getRow();
        int actualBackColVV = verticalVehicle.getBack().getCol();

        //Analyze
        assertEquals(backHV, actualBackHV);
        assertEquals(expectedBackRowHV, actualBackRowHV);
        assertEquals(expectedBackColHV, actualBackColHV);

        assertEquals(backVV, actualBackVV);
        assertEquals(expectedBackRowVV, actualBackRowVV);
        assertEquals(expectedBackColVV, actualBackColVV);
    }

    @Test
    public void vehicleFrontTest() {
        //Setup - completed at top, Vehicles created
        int expectedFrontRowHV = 1;
        int expectedFrontColHV = 3;
        int expectedFrontRowVV = 3;
        int expectedFrontColVV = 4;

        //Invoke
        Position actualFrontHV = horizontalVehicle.getFront();
        int actualFrontRowHV = horizontalVehicle.getFront().getRow();
        int actualFrontColHV = horizontalVehicle.getFront().getCol();
        Position actualFrontVV = verticalVehicle.getFront();
        int actualFrontRowVV = verticalVehicle.getFront().getRow();
        int actualFrontColVV = verticalVehicle.getFront().getCol();

        //Analyze
        assertEquals(frontHV, actualFrontHV);
        assertEquals(expectedFrontRowHV, actualFrontRowHV);
        assertEquals(expectedFrontColHV, actualFrontColHV);

        assertEquals(frontVV, actualFrontVV);
        assertEquals(expectedFrontRowVV, actualFrontRowVV);
        assertEquals(expectedFrontColVV, actualFrontColVV);
    }


    //*************************************************************
    //TESTING COPY CONSTRUCTOR - Ensure Two Vehicles do NOT Match when copied
    @Test
    public void deepCopyTest () {
        Vehicle testVehicle = new Vehicle('A', new Position (5, 5), new Position(5, 4));
        Vehicle testVehicleTwo = new Vehicle(testVehicle);

        assertNotEquals(testVehicle, testVehicleTwo);
        assertEquals(testVehicle.getBack(), testVehicleTwo.getBack());
        assertEquals(testVehicle.getFront(), testVehicleTwo.getFront());
        assertEquals(testVehicle.getSymbol(), testVehicleTwo.getSymbol());
    }

    //************************************************************
    //TESTING MOVE METHOD - Move each DIRECTION per class
    @Test
    public void moveUPTest() throws RushHourException {
        //Setup - completed at top, Vehicles created
        int expectedNewBackRow = 1;
        int expectedNewBackCol = 4;
        int expectedNewFrontRow = 2;
        int expectedNewFrontCol = 4;

        //Invoke
        verticalVehicle.move(Direction.UP);
        int actualBackRow = verticalVehicle.getBack().getRow();
        int actualBackCol = verticalVehicle.getBack().getCol();
        int actualFrontRow = verticalVehicle.getFront().getRow();
        int actualFrontCol = verticalVehicle.getFront().getCol();

        //Analyze
        assertEquals(expectedNewBackRow, actualBackRow);
        assertEquals(expectedNewBackCol, actualBackCol);
        assertEquals(expectedNewFrontRow, actualFrontRow);
        assertEquals(expectedNewFrontCol, actualFrontCol);
    }

    @Test
    public void moveDOWNTest() throws RushHourException {
        //Setup - completed at top, Vehicles created
        int expectedNewBackRow = 3;
        int expectedNewBackCol = 4;
        int expectedNewFrontRow = 4;
        int expectedNewFrontCol = 4;

        //Invoke
        verticalVehicle.move(Direction.DOWN);
        int actualBackRow = verticalVehicle.getBack().getRow();
        int actualBackCol = verticalVehicle.getBack().getCol();
        int actualFrontRow = verticalVehicle.getFront().getRow();
        int actualFrontCol = verticalVehicle.getFront().getCol();

        //Analyze
        assertEquals(expectedNewBackRow, actualBackRow);
        assertEquals(expectedNewBackCol, actualBackCol);
        assertEquals(expectedNewFrontRow, actualFrontRow);
        assertEquals(expectedNewFrontCol, actualFrontCol);
    }

    @Test
    public void moveLEFTTest() throws RushHourException {
        //Setup - completed at top, Vehicles created
        int expectedNewBackRow = 1;
        int expectedNewBackCol = 1;
        int expectedNewFrontRow = 1;
        int expectedNewFrontCol = 2;

        //Invoke
        horizontalVehicle.move(Direction.LEFT);
        int actualBackRow = horizontalVehicle.getBack().getRow();
        int actualBackCol = horizontalVehicle.getBack().getCol();
        int actualFrontRow = horizontalVehicle.getFront().getRow();
        int actualFrontCol = horizontalVehicle.getFront().getCol();

        //Analyze
        assertEquals(expectedNewBackRow, actualBackRow);
        assertEquals(expectedNewBackCol, actualBackCol);
        assertEquals(expectedNewFrontRow, actualFrontRow);
        assertEquals(expectedNewFrontCol, actualFrontCol);
    }

    @Test
    public void moveRIGHTTest() throws RushHourException {
        //Setup - completed at top, Vehicles created
        int expectedNewBackRow = 1;
        int expectedNewBackCol = 3;
        int expectedNewFrontRow = 1;
        int expectedNewFrontCol = 4;

        //Invoke
        horizontalVehicle.move(Direction.RIGHT);
        int actualBackRow = horizontalVehicle.getBack().getRow();
        int actualBackCol = horizontalVehicle.getBack().getCol();
        int actualFrontRow = horizontalVehicle.getFront().getRow();
        int actualFrontCol = horizontalVehicle.getFront().getCol();

        //Analyze
        assertEquals(expectedNewBackRow, actualBackRow);
        assertEquals(expectedNewBackCol, actualBackCol);
        assertEquals(expectedNewFrontRow, actualFrontRow);
        assertEquals(expectedNewFrontCol, actualFrontCol);
    }

    //************************************************************
    //TESTING IS_HORIZONTAL METHOD - Valid & Invalid
    @Test
    public void horizontalTrueTest() {
        //Setup - completed at top, Vehicles created
        //Invoke
        boolean actual = horizontalVehicle.isHorizontal();
        //Analyze
        assertTrue(actual);
    }

    @Test
    public void horizontalFalseTest() {
        //Setup - completed at top, Vehicles created
        //Invoke
        boolean actual = verticalVehicle.isHorizontal();
        //Analyze
        assertFalse(actual);
    }


    //************************************************************
    //TESTING IS_VERTICAL METHOD - Valid & Invalid
    @Test
    public void verticalTrueTest() {
        //Setup - completed at top, Vehicles created
        //Invoke
        boolean actual = verticalVehicle.isVertical();
        //Analyze
        assertTrue(actual);
    }

    @Test
    public void verticalFalseTest() {
        //Setup - completed at top, Vehicles created
        //Invoke
        boolean actual = horizontalVehicle.isVertical();
        //Analyze
        assertFalse(actual);
    }


    //************************************************************
    //TESTING SPACE_IN_FRONT METHOD - Vertical & Horizontal
    @Test
    public void spaceInFrontVerticalTest() {
        //Setup - completed at top, Vehicles created
        int expectedSpaceFrontRow = 4;
        int expectedSpaceFrontCol = 4;

        //Invoke
        Position inFront = verticalVehicle.spaceInFront();
        int actualSpaceFrontRow = inFront.getRow();
        int actualSpaceFrontCol = inFront.getCol();

        //Analyze
        assertEquals(expectedSpaceFrontRow , actualSpaceFrontRow);
        assertEquals(expectedSpaceFrontCol, actualSpaceFrontCol);
    }

    @Test
    public void spaceInFrontHorizontalTest() {
        //Setup - completed at top, Vehicles created
        int expectedSpaceFrontRow = 1;
        int expectedSpaceFrontCol = 4;

        //Invoke
        Position inFront = horizontalVehicle.spaceInFront();
        int actualSpaceFrontRow = inFront.getRow();
        int actualSpaceFrontCol = inFront.getCol();

        //Analyze
        assertEquals(expectedSpaceFrontRow , actualSpaceFrontRow);
        assertEquals(expectedSpaceFrontCol, actualSpaceFrontCol);
    }


    //************************************************************
    //TESTING SPACE_IN_BACK METHOD - Vertical & Horizontal
    @Test
    public void spaceInBackVerticalTest() {
        //Setup - completed at top, Vehicles created
        int expectedSpaceBackRow = 1;
        int expectedSpaceBackCol = 4;

        //Invoke
        Position inBack = verticalVehicle.spaceInBack();
        int actualSpaceBackRow = inBack.getRow();
        int actualSpaceBackCol = inBack.getCol();

        //Analyze
        assertEquals(expectedSpaceBackRow , actualSpaceBackRow);
        assertEquals(expectedSpaceBackCol, actualSpaceBackCol);
    }

    @Test
    public void spaceInBackHorizontalTest() {
        //Setup - completed at top, Vehicles created
        int expectedSpaceBackRow = 1;
        int expectedSpaceBackCol = 1;

        //Invoke
        Position inBack = horizontalVehicle.spaceInBack();
        int actualSpaceBackRow = inBack.getRow();
        int actualSpaceBackCol = inBack.getCol();

        //Analyze
        assertEquals(expectedSpaceBackRow , actualSpaceBackRow);
        assertEquals(expectedSpaceBackCol, actualSpaceBackCol);
    }


    //************************************************************
    //TESTING TO STRING METHOD
    @Test
    public void testToString () {
        Vehicle testVehicle = new Vehicle('A', new Position (5, 5), new Position(5, 4));
        assertEquals(testVehicle.toString(), "[Vehicle: A]");
    }

}
