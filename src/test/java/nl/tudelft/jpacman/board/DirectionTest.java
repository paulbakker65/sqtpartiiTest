package nl.tudelft.jpacman.board;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * A very simple test class to have a starting point where to put tests.
 * 
 * @author Arie van Deursen
 *
 */
public class DirectionTest {

	/**
	 * Do we get the correct horizontal delta when moving north?
	 */
	@Test
	public void directionTest() {
		assertEquals(0, Direction.NORTH.getDeltaX());
	}
	
	/**
	 * Do we get the correct horizontal delta when moving south?
	 */
	@Test
	public void directionTest2() {
		assertEquals(0, Direction.SOUTH.getDeltaX());
	}
	
	/**
	 * Do we get the correct horizontal delta when moving east?
	 */
	@Test
	public void directionTest3() {
		assertEquals(1, Direction.EAST.getDeltaX());
	}
	
	/**
	 * Do we get the correct horizontal delta when moving west?
	 */
	@Test
	public void directionTest4() {
		assertEquals(-1, Direction.WEST.getDeltaX());
	}
	
	

}
