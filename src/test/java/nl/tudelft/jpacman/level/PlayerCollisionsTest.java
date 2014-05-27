package nl.tudelft.jpacman.level;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.npc.ghost.Ghost;

import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

/**
 * A test suite for the PlayerCollisions class from JPacman
 * 
 * @author timwilliams
 */
@RunWith(Theories.class)
public class PlayerCollisionsTest {

	private Square square = mock(Square.class);
	private Pellet pellet = mock(Pellet.class);
	private Player player = mock(Player.class);
	private Ghost ghost = mock(Ghost.class);

	/**
	 * Create new PlayerCollisions. When pellet.getValue() is called return 30.
	 */
	@DataPoint
	public static CollisionMap map = new PlayerCollisions();

	@DataPoint
	public static CollisionMap map1 = new DefaultPlayerInteractionMap();

	/**
	 * Test with a player colliding on a pellet
	 */
	@Theory
	public void PlayerCollidedOnPelletTest(CollisionMap playercollisions) {
		Mockito.when(pellet.getValue()).thenReturn(30);
		pellet.occupy(square); // Put the pellet on the square
		playercollisions.collide(player, pellet); // Collide

		Mockito.verify(player, Mockito.never()).setAlive(false); // Player
																	// should
																	// not be
																	// killed
		Mockito.verify(pellet).leaveSquare(); // Pellet should disappear
		Mockito.verify(player).addPoints(30); // Player should earn 30 points

		assertFalse(square.getOccupants().contains(pellet));
		// assertEquals(player.getScore(), 30);

	}

	/**
	 * Test with a player colliding on a ghost
	 */
	@Theory
	public void PlayerCollidedOnGhostTest(CollisionMap playercollisions) {

		playercollisions.collide(player, ghost); // Collide

		Mockito.verify(player).setAlive(false); // Player should be killed

		assertFalse(player.isAlive());

	}

	/**
	 * Test with a ghost colliding on a player
	 */
	@Theory
	public void GhostCollidedOnPlayerTest(CollisionMap playercollisions) {

		playercollisions.collide(ghost, player); // Collide

		Mockito.verify(player).setAlive(false); // Player should be killed

		assertFalse(player.isAlive());

	}

}
