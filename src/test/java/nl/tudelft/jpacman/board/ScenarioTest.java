package nl.tudelft.jpacman.board;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.level.Pellet;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.npc.ghost.Blinky;
import nl.tudelft.jpacman.npc.ghost.Ghost;
import nl.tudelft.jpacman.npc.ghost.GhostColor;
import nl.tudelft.jpacman.sprite.PacManSprites;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * We test user scenario's.
 * @author paulbakker
 *
 */
public class ScenarioTest {

	private Launcher launcher;
	private final int ten = 10;
	private final int sleep = 5000;

	/**
	 * .
	 * @return ten
	 */
	public int getTen() {
		return ten;
	}

	/**
	 * .
	 * @return sleep
	 */
	public int getSleep() {
		return sleep;
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
	 * First check if the game in not already in progress Then we start the game
	 * and check if it has really started.
	 */
	@Test
	public void scenario1Test() {
		Game game = launcher.getGame();

		// Scenario 1
		assertFalse(game.isInProgress());
		game.start();
		assertTrue(game.isInProgress());
	}

	/**
	 * We check if the player's score has increased when he moves to a square
	 * with a pellet. We do this by checking if the score is 0 before a move and
	 * 10 after the move to the square. We also check the score of the
	 * individual pellet. After the move we also chek if the square does not
	 * contain the pellet anymore
	 */
	@Test
	public void scenario2_1Test() {

		Game game = launcher.getGame();
		Player player = game.getPlayers().get(0);
		game.start();
		assertTrue(game.isInProgress());
		Square pSquare = player.getSquare();
		Square t1Square = pSquare.getSquareAt(player.getDirection());
		Pellet pellet = (Pellet) t1Square.getOccupants().get(0);
		assertEquals(pellet.getValue(), ten);

		assertEquals(0, player.getScore());
		game.move(player, player.getDirection());
		assertEquals(player.getSquare(), t1Square);
		assertEquals(ten, player.getScore());
		assertFalse(t1Square.getOccupants().contains(pellet));

	}

	/**
	 * We check if the score of a player is not increased after he moves to a
	 * square that is empty. We do this by first removing 1 pellet next to the
	 * player. Then we keep the score of as a integer and move again to the
	 * empty square and check if the score is not raised
	 */
	@Test
	public void scenario2_2Test() {
		Game game = launcher.getGame();
		Player player = game.getPlayers().get(0);
		game.start();
		assertTrue(game.isInProgress());

		Square pSquare = player.getSquare();
		Square t1Square = pSquare.getSquareAt(player.getDirection());
		assertEquals(player.getDirection(), Direction.EAST);
		game.move(player, Direction.EAST);
		game.move(player, Direction.WEST);
		assertTrue(t1Square.getOccupants().isEmpty());

		int score = player.getScore();
		game.move(player, Direction.EAST);
		assertEquals(player.getSquare(), t1Square);
		assertEquals(player.getScore(), score);
	}

	/**
	 * We check if a player dies when he is next to a cell containing a ghost
	 * and moves to that cell We do this by putting a ghost next to the player,
	 * and move the player to the cell with the ghost.
	 */
	@Test
	public void scenario2_3Test() {
		Game game = launcher.getGame();
		Player player = game.getPlayers().get(0);
		game.start();
		assertTrue(game.isInProgress());

		Square pSquare = player.getSquare();
		Square t1Square = pSquare.getSquareAt(player.getDirection());

		PacManSprites sprites = new PacManSprites();
		Ghost ghost = new Blinky(sprites.getGhostSprite(GhostColor.RED));

		t1Square.put(ghost);
		assertTrue(pSquare.getOccupants().contains(player));
		assertTrue(t1Square.getOccupants().contains(ghost));

		game.move(player, player.getDirection());
		assertFalse(player.isAlive());
		assertFalse(game.isInProgress());
	}

	/**
	 * We check if a moves is not conducted when a player tries to move to a
	 * square containing a wall. We do this by checking if the square above the
	 * player is accessible by the player, then trying to move the player to
	 * this square and check if the player is still on the same square
	 */

	@Test
	public void scenario2_4Test() {
		Game game = launcher.getGame();
		Player player = game.getPlayers().get(0);
		game = launcher.makeGame();
		player = game.getPlayers().get(0);
		game.start();
		assertTrue(game.isInProgress());
		Square playerSquare = player.getSquare();
		Square t3Square = playerSquare.getSquareAt(Direction.NORTH);

		assertFalse(t3Square.isAccessibleTo(player));

		game.move(player, Direction.NORTH);
		assertEquals(player.getSquare(), playerSquare);
	}

	/**
	 * We check if a player wins when he eats the last pellet We do this by
	 * removing all pellets except the pellet right from the player. Then we
	 * check if the amount of remaining pellets is 1, whe check if the score is
	 * raised when we eat the last pellet and check if the amount of remaining
	 * pellets is 0. Afterwards the player is still alive and the game should
	 * not be in progress anymore.
	 */

	@Test
	public void scenario2_5Test() {
		Game game = launcher.getGame();
		Player player = game.getPlayers().get(0);
		game.start();
		Square square = player.getSquare();

		for (int i = 0; i < game.getLevel().getBoard().getHeight(); i++) {
			for (int j = 0; j < game.getLevel().getBoard().getWidth(); j++) {
				if (!(game.getLevel().getBoard().squareAt(j, i).getOccupants()
						.isEmpty())) {
					if (game.getLevel().getBoard().squareAt(j, i)
							.getOccupants().contains(player)) {
						j++;
					} else {
						Unit pellet = game.getLevel().getBoard().squareAt(j, i)
								.getOccupants().get(0);
						game.getLevel().getBoard().squareAt(j, i)
								.remove(pellet);
						assertTrue(game.getLevel().getBoard().squareAt(j, i)
								.getOccupants().isEmpty());
					}
				}

			}
		}

		Square gSquare = square.getSquareAt(Direction.EAST);

		assertEquals(game.getLevel().remainingPellets(), 1);
		assertTrue(square.getOccupants().contains(player));
		assertFalse(gSquare.getOccupants().isEmpty());

		assertEquals(player.getScore(), 0);
		game.move(player, Direction.EAST);
		assertEquals(player.getScore(), ten);
		assertEquals(game.getLevel().remainingPellets(), 0);

		assertFalse(game.isInProgress());
		assertTrue(player.isAlive());

	}

	/**
	 * We check if a ghost is abe to move to a square, when that square is empty
	 * We do this by moving the player east and west to obtain an empty square.
	 * Then we put a ghost right to that empty square and check if the ghost can
	 * access that empty square.
	 */
	@Test
	public void scenario3_1Test() {
		Game game = launcher.getGame();
		Player player = game.getPlayers().get(0);
		game = launcher.makeGame();
		player = game.getPlayers().get(0);
		game.start();
		assertTrue(game.isInProgress());
		game.move(player, Direction.EAST);
		game.move(player, Direction.WEST);

		Square gSquare = player.getSquare().getSquareAt(Direction.EAST)
				.getSquareAt(Direction.EAST);
		PacManSprites sprites = new PacManSprites();
		Ghost ghost = new Blinky(sprites.getGhostSprite(GhostColor.RED));

		gSquare.put(ghost);

		Square g1Square = gSquare.getSquareAt(Direction.WEST);
		assertTrue(g1Square.getOccupants().isEmpty());
		assertTrue(g1Square.isAccessibleTo(ghost));

	}

	/**
	 * We check if the food is not vissible, when a ghost is next to a square
	 * containing food and it moves to that square. We do this by putting a
	 * ghost 2 squares at the right from the player, we check if the square left
	 * from the ghost is accessible by the ghost, then we move it to the left by
	 * replacing the ghost. We check if the square contains a pellet and a ghost
	 * and we check if the pellet is not vissible anymore
	 */
	@Test
	public void scenario3_2Test() {
		Game game = launcher.getGame();
		Player player = game.getPlayers().get(0);
		game = launcher.makeGame();
		player = game.getPlayers().get(0);
		game.start();
		assertTrue(game.isInProgress());

		// Put a ghost 2 squares East from the player
		Square g0Square = player.getSquare().getSquareAt(Direction.EAST)
				.getSquareAt(Direction.EAST);
		PacManSprites sprites = new PacManSprites();
		Ghost ghost = new Blinky(sprites.getGhostSprite(GhostColor.RED));
		g0Square.put(ghost);

		// Check if the square left to the ghost is accessible
		Square g2Square = g0Square.getSquareAt(Direction.WEST);
		Pellet gpellet = (Pellet) g2Square.getOccupants().get(0);
		assertEquals(gpellet.getValue(), ten);
		assertTrue(g2Square.isAccessibleTo(ghost));

		// Move the ghost to the left
		g0Square.remove(ghost);
		g2Square.put(ghost);

		// Checks if the square has a pellet and a ghost
		Pellet g1pellet = (Pellet) g2Square.getOccupants().get(0);
		g2Square.getOccupants().contains(g1pellet);
		g2Square.getOccupants().contains(ghost);

		// Check if the pellet is still there (index should be smaller than
		// index of ghost)
		assertTrue(g2Square.getOccupants().indexOf(ghost) > g2Square
				.getOccupants().indexOf(g1pellet));

	}

	/**
	 * We check if a pellet is vissible again when a ghost moves from a square
	 * containing a pellet to an other square. We do this by moving a ghost to
	 * another square and check if the pellet on the old square is vissible
	 * again (the index of the pellet + 1 should be equal to the number of
	 * occupants. This means it has the greatest index and that it is vissible
	 * again.)
	 */
	@Test
	public void scenario3_3Test() {
		Game game = launcher.getGame();
		Player player = game.getPlayers().get(0);
		PacManSprites sprites = new PacManSprites();
		Ghost ghost = new Blinky(sprites.getGhostSprite(GhostColor.RED));

		Square g0Square = player.getSquare().getSquareAt(Direction.EAST)
				.getSquareAt(Direction.EAST);
		Square g2Square = g0Square.getSquareAt(Direction.WEST);
		Pellet g1pellet = (Pellet) g2Square.getOccupants().get(0);
		// Remove the ghost and put it back on the square to the right
		g2Square.remove(ghost);
		g0Square.put(ghost);

		// Checks if the square has a pellet and a ghost
		g1pellet = (Pellet) g2Square.getOccupants().get(0);
		g2Square.getOccupants().contains(g1pellet);
		g2Square.getOccupants().contains(ghost);

		// Check if the pellet is still there (index should be greater than
		// index of ghost)
		assertEquals(g2Square.getOccupants().indexOf(g1pellet) + 1, g2Square
				.getOccupants().size());
	}

	/**
	 * We check if the game is over when a ghost is next to the player and the
	 * ghost moves to the player We do this by putting a ghost at the square
	 * right from the player and check if the square containing the player is
	 * accessible to the ghost. We let the game continue, so that the ghost oves
	 * to the player, and we can check if the game is still in progress.
	 * 
	 * @throws InterruptedException
	 *             Since we're sleeping in this test.
	 */
	@Test
	public void scenario3_4Test() throws InterruptedException {
		Game game = launcher.getGame();
		Player player = game.getPlayers().get(0);
		game = launcher.makeGame();
		player = game.getPlayers().get(0);
		game.start();
		assertTrue(game.isInProgress());

		Square gSquare = player.getSquare().getSquareAt(Direction.EAST);
		PacManSprites sprites = new PacManSprites();
		Ghost ghost = new Blinky(sprites.getGhostSprite(GhostColor.RED));
		gSquare.put(ghost);

		Square g1Square = gSquare.getSquareAt(Direction.WEST);
		assertTrue(g1Square.getOccupants().contains(player));
		assertTrue(g1Square.isAccessibleTo(ghost));

		Thread.sleep(sleep);
		assertFalse(game.isInProgress());
	}

	/**
	 * We check if the player or the ghost can't move after the game has been
	 * stopped We put a ghost next to the player and stop the game. Then we try
	 * to move the player, this should not be allowed. Then we let the thread
	 * sleep to check if the ghost moves, this should not be allowed.
	 * 
	 * @throws InterruptedException
	 *             Since we're sleeping in this test.
	 */
	@Test
	public void scenario4_1Test() throws InterruptedException {
		Game game = launcher.getGame();
		Player player = game.getPlayers().get(0);
		game = launcher.makeGame();
		player = game.getPlayers().get(0);
		game.start();
		assertTrue(game.isInProgress());
		Square playerSquare = player.getSquare();

		PacManSprites sprites = new PacManSprites();
		Ghost ghost = new Blinky(sprites.getGhostSprite(GhostColor.RED));

		playerSquare.getSquareAt(player.getDirection()).put(ghost);

		game.stop();
		assertFalse(game.isInProgress());
		game.move(player, player.getDirection());
		assertEquals(player.getSquare(), playerSquare);
		Thread.sleep(sleep);
		assertEquals(player.getSquare(), playerSquare);
		assertTrue(playerSquare.getSquareAt(player.getDirection())
				.getOccupants().contains(ghost));
	}

	/**
	 * The game is not started yet and we check if the game is started after a
	 * stop command of the previous scenario.
	 */
	@Test
	public void scenario4_2Test() {

		Game game = launcher.getGame();
		assertFalse(game.isInProgress());
		game.start();
		assertTrue(game.isInProgress());

	}
}
