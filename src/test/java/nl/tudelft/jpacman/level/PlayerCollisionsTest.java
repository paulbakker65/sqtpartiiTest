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
 * A test suite for the PlayerCollisions class from JPacman.
 * 
 * @author timwilliams
 */
@RunWith(Theories.class)
public class PlayerCollisionsTest {

	private Square square = mock(Square.class);
	private Pellet pellet = mock(Pellet.class);
	private Player player = mock(Player.class);
	private Ghost ghost = mock(Ghost.class);
	private final int pelletValue = 30;

	/**
	 * Create new CollisionsMap with PlayerCollision.
	 */
	@DataPoint
	public static CollisionMap map = new PlayerCollisions();

	/**
	 * Create new CollisionsMap with DefaultPlayerInteractionMap.
	 */
	@DataPoint
	public static CollisionMap map1 = new DefaultPlayerInteractionMap();

	/**
	 * Test with a player colliding on a pellet.
	 * @param playercollisions CollisionMap
	 */
	@Theory
	public void playerCollidedOnpelletTest(CollisionMap playercollisions) {
		Mockito.when(pellet.getValue()).thenReturn(pelletValue);
		pellet.occupy(square); // Put the pellet on the square
		playercollisions.collide(player, pellet); // Collide

		Mockito.verify(player, Mockito.never()).setAlive(false); // Player
																	// should
																	// not be
																	// killed
		Mockito.verify(pellet).leaveSquare(); // pellet should disappear
		Mockito.verify(player).addPoints(pelletValue); // Player should earn 30 points

	}

	/**
	 * Test with a player colliding on a ghost.
	 * @param playercollisions CollisionMap
	 */
	@Theory
	public void playerCollidedOnGhostTest(CollisionMap playercollisions) {

		playercollisions.collide(player, ghost); // Collide

		Mockito.verify(player).setAlive(false); // Player should be killed

		assertFalse(player.isAlive());

	}

	/**
	 * Test with a ghost colliding on a player.
	 * @param playercollisions CollisionMap
	 */
	@Theory
	public void ghostCollidedOnPlayerTest(CollisionMap playercollisions) {

		playercollisions.collide(ghost, player); // Collide

		Mockito.verify(player).setAlive(false); // Player should be killed

		assertFalse(player.isAlive());

	}

	/**
	 * Test with a ghost colliding on a pellet.
	 * @param playercollisions CollisionMap
	 */
	@Theory
	public void ghostCollidedOnpelletTest(CollisionMap playercollisions) {
		Mockito.when(pellet.getValue()).thenReturn(pelletValue);
		pellet.occupy(square);
		playercollisions.collide(ghost, pellet);

		Mockito.verify(pellet, Mockito.never()).getValue();
		Mockito.verify(pellet, Mockito.never()).leaveSquare();
		Mockito.verify(player, Mockito.never()).addPoints(pelletValue);

	}

}
