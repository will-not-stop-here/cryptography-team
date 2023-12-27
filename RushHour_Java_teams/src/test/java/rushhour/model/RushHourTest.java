package rushhour.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import org.junit.jupiter.api.Test;

/**
 * unit tests for the RushHour (main) class of the RushHour game
 * 
 * @author Lennard
 * @author Daphne
 */
public class RushHourTest {

    /**
     * Test to see if RushHour constructor is populating the correct
     * number of vehicles into the map. 
     * @throws IOException
     * 
     * @author Lennard
     */
    @Test
    public void rushHourTest() throws IOException {
        // Setup
        RushHour testGame = new RushHour("./data/03_00.csv");
        
        int expectedVehicleCount = 3;
        int expectedMoveCount = 0;
        
        // Invoke
        int actualVehiclCount = testGame.getVehiclesOnBoard().size();
        int actualMoveCount = testGame.getMoveCount();

        // Analyze
        assertEquals(expectedVehicleCount, actualVehiclCount);
        assertEquals(expectedMoveCount, actualMoveCount);
    }

    //***********************************************************************
    @Test
    public void deepCopyTest () throws IOException {
        RushHour r1 = new RushHour("data/03_00.csv");
        RushHour r2 = new RushHour(r1);
        //Assert that each state of the two RushHour objects do not equal
        assertEquals(r1.getMoveCount(), r2.getMoveCount());
        assertNotEquals(r1.getVehicles(), r2.getVehicles());
        assertNotEquals(r1.getGameBoard(), r2.getGameBoard());
    }


    //***********************************************************************
    /**
     * A test to see if two RushHour objects can be equated against each other 
     * @throws IOException
     * @throws RushHourException
     * 
     * @author Lennard
     */
    @Test
    public void equalsTest() throws IOException {
        // setup
        RushHour testGame1 = new RushHour("./data/03_00.csv");
        RushHour testGame2 = new RushHour("./data/03_00.csv");
        RushHour testGame3 = new RushHour("./data/03_01.csv");

        // analyze
        assert testGame1.equals(testGame2);
        assert !testGame1.equals(testGame3);
    }

    //***********************************************************************
    /**
     * A test to see if the 2D array 'board' gets updated when a move is made
     * Expected values are based on verified CLI outputs.
     * @throws IOException
     * @throws RushHourException
     * 
     * @author Lennard
     */
    @Test
    public void updateBoardTest() throws IOException, RushHourException {
        // Setup
        RushHour testGame = new RushHour("./data/03_00.csv");
        
        String expectedStartingBoard = "--O-----OA--RROA--< EXIT------------------";
        String expectedEndBoardState = "--OA----OA--RRO---< EXIT------------------";

        String upSymbol = "A";
        Move testMove = new Move(upSymbol.charAt(0), Direction.valueOf("UP"));
        
        // Invoke
        // first loop to get actual starting board
        Object[][] testBoard = testGame.getGameBoard();
        int rowTracker = 0;
        String actualStartingBoard = "";
        for (Object[] row : testBoard) {
            // loop through cells in row
            for (Object cell : row) {
                if (cell == null) {
                    actualStartingBoard += '-';
                } else {
                    actualStartingBoard += String.valueOf(cell);
                }
            }
            if (rowTracker == 2) {
                actualStartingBoard += "< EXIT";
            }
            // start new line after every row
            rowTracker ++;
        }
        
        // second loop to get actual end board state
        testGame.moveVehicle(testMove);
        testBoard = testGame.getGameBoard();
        String actualEndBoardState = "";
        rowTracker = 0;
        // loop through rows
        for (Object[] row : testBoard) {
            // loop through cells in row
            for (Object cell : row) {
                if (cell == null) {
                    actualEndBoardState += '-';
                } else {
                    actualEndBoardState += String.valueOf(cell);
                }
            }
            if (rowTracker == 2) {
                actualEndBoardState += "< EXIT";
            }
            // start new line after every row
            rowTracker ++;
        }
        
        // Analyze
        assert expectedStartingBoard.equals(actualStartingBoard);
        assert expectedEndBoardState.equals(actualEndBoardState);

    }

    // ***********************************************************************

    /**
     * Testing the isGameOver method - game ends properly
     * @throws IOException
     * @throws RushHourException
     * 
     * @author Lennard
     */
    @Test
    public void isGameOverTest() throws IOException, RushHourException {
        // setup
        RushHour testGame = new RushHour("./data/03_00.csv");
        boolean startGameExpected = false;
        boolean midGameExpected = false;
        boolean endGameExpected = true;

        String upSymbol = "A";
        Move testMove = new Move(upSymbol.charAt(0), Direction.valueOf("UP"));
        
        String downSymbol = "O";
        Move testMove2 = new Move(downSymbol.charAt(0), Direction.valueOf("DOWN"));

        String rightSymbol = "R";
        Move testMove3 = new Move(rightSymbol.charAt(0), Direction.valueOf("RIGHT"));

        // invoke
        boolean startGameActual = testGame.isGameOver();
        // make the moves to get to a win
        testGame.moveVehicle(testMove);
        testGame.moveVehicle(testMove2);
        boolean midGameActual = testGame.isGameOver();
        testGame.moveVehicle(testMove2);
        testGame.moveVehicle(testMove2);
        testGame.moveVehicle(testMove3);
        testGame.moveVehicle(testMove3);
        testGame.moveVehicle(testMove3);
        testGame.moveVehicle(testMove3);
        boolean endGameActual = testGame.isGameOver();

        // analyze
        assert startGameExpected == startGameActual;
        assert midGameExpected == midGameActual;
        assert endGameExpected == endGameActual;
    }

    //***********************************************************************

    /**
     * Testing the toString method, which prints out the board
     * @throws IOException
     * 
     * @author Lennard
     */
    @Test
    public void toStringTest() throws IOException {
        // setup 
        RushHour testGame = new RushHour("./data/03_00.csv");
    
        String expectedStartingBoard = "--O---\n--OA--\nRROA--< EXIT\n------\n------\n------\n";

        // invoke
        String actualStartingBoard = testGame.toString();

        // analyze
        assert expectedStartingBoard.equals(actualStartingBoard);
    }


    //***********************************************************************

    /**
     * Valid Test for moving a vehicle
     * @throws IOException
     * @throws RushHourException
     * 
     * @author Lennard
     */
    @Test
    public void moveVehicleTest() throws IOException, RushHourException {
        // setup
        RushHour testGame = new RushHour("./data/03_00.csv");
        char upSymbol = 'A';
        Move testMove = new Move(upSymbol, Direction.UP);

        Position expectedBottom = new Position(0, 3);
        
        // invoke
        testGame.moveVehicle(testMove);
        Position actualBottom = testGame.getVehiclesOnBoard().get(upSymbol).getBack();

        // analyze
        assert expectedBottom.equals(actualBottom);
    }

    /* SERIES OF TESTS to test for INVALID moves with the moveVehicle() */

    /**
     * Vehicle selected not on the board
     * @throws IOException
     * @throws RushHourException
     * 
     * @author Daphne
     */
    @Test
    public void moveVehicleInvalidNoVehicleTest() throws IOException, RushHourException {
        // setup
        RushHour testGame = new RushHour("./data/03_00.csv");
        char vertVehicleChar = 'E';
        Move testMove = new Move(vertVehicleChar, Direction.RIGHT);
        //Invoke
        try{
            testGame.moveVehicle(testMove);
            assert false; //Exception not thrown, force test to fail
        }
        //Analyze
        catch(RushHourException e){
            assert true;
        }
    }

    
    /**
     * Trying to move a vertical vehicle right
     * @throws IOException
     * @throws RushHourException
     * 
     * @author Daphne
     */
    @Test
    public void moveVehicleInvalidVerticalRIGHTTest() throws IOException, RushHourException {
        // setup
        RushHour testGame = new RushHour("./data/03_00.csv");
        char vertVehicleChar = 'A';
        Move testMove = new Move(vertVehicleChar, Direction.RIGHT);
        //Invoke
        try{
            testGame.moveVehicle(testMove);
            assert false; //Exception not thrown, force test to fail
        }
        //Analyze
        catch(RushHourException e){
            assert true;
        }
    }

    /**
     * Trying to move a vertical vehicle left
     * @throws IOException
     * @throws RushHourException
     * 
     * @author Daphne
     */
    @Test
    public void moveVehicleInvalidVerticalLeftTTest() throws IOException, RushHourException {
        // setup
        RushHour testGame = new RushHour("./data/03_00.csv");
        char vertVehicleChar = 'O';
        Move testMove = new Move(vertVehicleChar, Direction.LEFT);
        //Invoke
        try{
            testGame.moveVehicle(testMove);
            assert false; //Exception not thrown, force test to fail
        }
        //Analyze
        catch(RushHourException e){
            assert true;
        }
    }

    //
    /**
     * Trying to move a horizontal vehicle up
     * @throws IOException
     * @throws RushHourException
     * 
     * @author Daphne
     */
    @Test
    public void moveVehicleInvalidHorizontalUPTest() throws IOException, RushHourException {
        // setup
        RushHour testGame = new RushHour("./data/03_00.csv");
        char horizVehicleChar = 'R';
        Move testMove = new Move(horizVehicleChar, Direction.UP);
        //Invoke
        try{
            testGame.moveVehicle(testMove);
            assert false; //Exception not thrown, force test to fail
        }
        //Analyze
        catch(RushHourException e){
            assert true;
        }
    }

    /**
     * Trying to move a horizontal vehicle down
     * @throws IOException
     * @throws RushHourException
     * 
     * @author Daphne
     */
    @Test
    public void moveVehicleInvalidHorizontalDOWNTest() throws IOException, RushHourException {
        // setup
        RushHour testGame = new RushHour("./data/03_00.csv");
        char horizVehicleChar = 'R';
        Move testMove = new Move(horizVehicleChar, Direction.DOWN);
        //Invoke
        try{
            testGame.moveVehicle(testMove);
            assert false; //Exception not thrown, force test to fail
        }
        //Analyze
        catch(RushHourException e){
            assert true;
        }
    }

    /**
     * Horizontal Vehicle is at the edge of the board
     * @throws IOException
     * @throws RushHourException
     * 
     * @author Daphne
     */
    @Test
    public void moveVehicleInvalidAtEdgeHorizTest() throws IOException, RushHourException {
        // setup
        RushHour testGame = new RushHour("./data/03_00.csv");
        char horizVehicleChar = 'R';
        Move testMove = new Move(horizVehicleChar, Direction.LEFT);
        //Invoke
        try{
            testGame.moveVehicle(testMove);
            assert false; //Exception not thrown, force test to fail
        }
        //Analyze
        catch(RushHourException e){
            assert true;
        }
    }

    /**
     * Vertical Vehicle is at the edge of the board
     * @throws IOException
     * @throws RushHourException
     * 
     * @author Daphne
     */
    @Test
    public void moveVehicleInvalidAtEdgeVertTest() throws IOException, RushHourException {
        // setup
        RushHour testGame = new RushHour("./data/03_00.csv");
        char horizVehicleChar = 'O';
        Move testMove = new Move(horizVehicleChar, Direction.UP);
        //Invoke
        try{
            testGame.moveVehicle(testMove);
            assert false; //Exception not thrown, force test to fail
        }
        //Analyze
        catch(RushHourException e){
            assert true;
        }
    }

    /**
     * Vertical Vehicle is at the edge of the board - MULTIPLE MOVES
     * @throws IOException
     * @throws RushHourException
     * 
     * @author Daphne
     */
    @Test
    public void moveVehicleInvalidAtEdgeVert2MovesTest() throws IOException, RushHourException {
        // setup
        RushHour testGame = new RushHour("./data/03_00.csv");
        char horizVehicleChar = 'A';
        Move testMove = new Move(horizVehicleChar, Direction.UP);
        //Invoke
        testGame.moveVehicle(testMove);
        try{
            testGame.moveVehicle(testMove); //second move fails
            assert false; //Exception not thrown, force test to fail
        }
        //Analyze
        catch(RushHourException e){
            assert true;
        }
    }

    /**
     * Another Vehicle in the way
     * @throws IOException
     * @throws RushHourException
     * 
     * @author Daphne
     */
    @Test
    public void moveVehicleInvalidBlockedTest() throws IOException, RushHourException {
        // setup
        RushHour testGame = new RushHour("./data/03_00.csv");
        char horizVehicleChar = 'R';
        Move testMove = new Move(horizVehicleChar, Direction.RIGHT);
        //Invoke
        try{
            testGame.moveVehicle(testMove);
            assert false; //Exception not thrown, force test to fail
        }
        //Analyze
        catch(RushHourException e){
            assert true;
        }
    }


    //***********************************************************************

    /**
     * Tests that a valid list of possible moves is created
     * @throws IOException
     * 
     * @author Daphne
     */
    @Test
    public void getPossMovesValidTest() throws IOException{
        // Setup
        RushHour testGame = new RushHour("./data/03_00.csv");
        Collection<Move> expectedPossMoves = new LinkedList<>();
        Move move1 = new Move('A', Direction.UP);
        Move move2 = new Move('A', Direction.DOWN);
        Move move3 = new Move('O', Direction.DOWN);
        expectedPossMoves.add(move1);
        expectedPossMoves.add(move2);
        expectedPossMoves.add(move3);

        // Invoke
        Collection<Move> actualList = testGame.getPossibleMoves();

        // Analyze
        assertEquals(expectedPossMoves.size(), actualList.size());
        assertEquals(expectedPossMoves.toString(),actualList.toString());
    }

    /**
     * Tests that no invalid moves are added to the poss moves list
     * @throws IOException
     * 
     * @author Daphne
     */
    @Test
    public void getPossMovesInvalidTest() throws IOException{
        // Setup
        RushHour testGame = new RushHour("./data/13_00.csv");
        // Collection<Move> testPossMoves = new LinkedList<>();
        Move move1 = new Move('D', Direction.UP);
        Move move2 = new Move('Q', Direction.RIGHT);
        Move move2a = new Move('Q', Direction.LEFT);
        Move move3 = new Move('O', Direction.DOWN);
        // expectedPossMoves.add(move1);
        // expectedPossMoves.add(move2);
        // expectedPossMoves.add(move2a);
        // expectedPossMoves.add(move3);

        // Invoke
        Collection<Move> actualList = testGame.getPossibleMoves();

        // Analyze
        assertFalse(actualList.contains(move1));
        assertFalse(actualList.contains(move2));
        assertFalse(actualList.contains(move2a));
        assertFalse(actualList.contains(move3));
    }


    //***********************************************************************
    //TODO Emma Write Test for GetVehicles method
    //Vehicles created in RushHour(class) DO equal expected vehicles


    //***********************************************************************
    //TODO Emma Write Test for resetMoveCount method
    /**Create RushHour object, make a couple moves, ensure moves are >0 
     * Then, reset the moveCount and esnure =0
     * */
}
