package nl.tudelft.jpacman.level;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.npc.ghost.Ghost;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * A test suite for the PlayerCollisions class from JPacman
 * @author timwilliams
 */
@RunWith(MockitoJUnitRunner.class)
public class PlayerCollisionsTest {

	private PlayerCollisions playercollisions;
	private Square square = mock(Square.class);
	private Pellet pellet = mock(Pellet.class);
	private Player player = mock(Player.class);
	private Ghost ghost = mock(Ghost.class);
	
	/**
	 * Create new PlayerCollisions. When pellet.getValue() is called return 30.
	 */
	@Before
	public void setUp() {
		playercollisions = new PlayerCollisions();
		Mockito.when(pellet.getValue()).thenReturn(30);
	}
	
	/**
	 * Test with a player colliding on a pellet
	 */
	@Test
	public void PlayerCollidedOnPelletTest() {

		pellet.occupy(square);										// Put the pellet on the square
		playercollisions.collide(player, pellet);					// Collide
		
		Mockito.verify(player, Mockito.never()).setAlive(false);	// Player should not be killed
		Mockito.verify(pellet).leaveSquare();						// Pellet should disappear
		Mockito.verify(player).addPoints(30);						// Player should earn 30 points
		
		assertFalse(square.getOccupants().contains(pellet));
		//assertEquals(player.getScore(), 30);

	}

	/**
	 * Test with a player colliding on a ghost
	 */
	@Test
	public void PlayerCollidedOnGhostTest() {

		playercollisions.collide(player, ghost);					// Collide
		
		Mockito.verify(player).setAlive(false);						// Player should be killed
		
		assertFalse(player.isAlive());

	}
	
	
	/**
	 * Test with a ghost colliding on a player
	 */
	@Test
	public void GhostCollidedOnPlayerTest() {

		playercollisions.collide(ghost, player);					// Collide
		
		Mockito.verify(player).setAlive(false);						// Player should be killed
		
		assertFalse(player.isAlive());

	}
	
	
}
