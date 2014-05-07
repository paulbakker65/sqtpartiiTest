package nl.tudelft.jpacman.board;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import nl.tudelft.jpacman.Launcher;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Test for withinBorders.
 * @author paulbakker
 *
 */
@RunWith(Parameterized.class)
public class BoundaryTest {

	private Launcher launcher;
	private int x;
	private int y;
	private boolean expectedOut;

	/**
	 * Constructor that puts the expected outcome in the class.
	 * 
	 * @param xIn Width
	 * @param yIn Height
	 * @param out Outcome
	 */
	public BoundaryTest(int xIn, int yIn, boolean out) {
		this.x = xIn;
		this.y = yIn;
		expectedOut = out;
	}

	/**
	 * We set up the launcher.
	 */
	@Before
	public void setUpPacman() {
		launcher = new Launcher();
		launcher.launch();
	}

	/**
	 * We stop the launcher.
	 */
	@After
	public void tearDown() {
		launcher.dispose();
	}

	/**
	 * Compare the outcome of the withinBorder method with the expected outcome
	 * that the constructor has created.
	 */
	@Test
	public void testBoundary() {
		Board board = launcher.getGame().getLevel().getBoard();
		boolean actualOut = board.withinBorders(x, y);
		assertEquals(expectedOut, actualOut);
	}

	/**
	 * The expected results are stored here.
	 * 
	 * @return expected results
	 */
	@Parameters
	public static Collection<Object[]> data() {
		final Object[][] values = { { 0, 5, true }, { -1, 5, false },
				{ 23, 5, false }, { 22, 5, true }, { 5, 0, true },
				{ 5, -1, false }, { 5, 21, false }, { 5, 20, true } };
		return Arrays.asList(values);
	}

}