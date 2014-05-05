package nl.tudelft.jpacman.board;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collection;
import nl.tudelft.jpacman.Launcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class BoundaryTest {
	
	private Launcher launcher;
	private int x;
	private int y;
	private boolean expectedOut;

	public BoundaryTest(int xIn, int yIn, boolean out) {
		this.x = xIn;
		this.y = yIn;
		expectedOut = out;
	}

	@Before
	public void setUpPacman() {
		launcher = new Launcher();
		launcher.launch();
	}

	@After
	public void tearDown() {
		launcher.dispose();
	}
	@Test
	public void testBoundary() {
		Board board = launcher.getGame().getLevel().getBoard();
		boolean actualOut = board.withinBorders(x, y);
		assertEquals(expectedOut, actualOut);
	}

	@Parameters
	public static Collection<Object[]> data() {
		Object[][] values = { { 0, 5, true }, { -1, 5, false },
				{ 23, 5, false }, { 22, 5, true }, { 5, 0, true },
				{ 5, -1, false }, { 5, 21, false }, { 5, 20, true } };
		return Arrays.asList(values);
	}

}