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

public class ScenarioTest {

	private Launcher launcher;

	@Before
	public void setUpPacman() {
		launcher = new Launcher();
		launcher.launch();
	}

	@After
	public void tearDown() {
		launcher.dispose();
	}

	/**
	 * Launch the game, and imitate what would happen in a typical game.
	 * 
	 * @throws InterruptedException
	 *             Since we're sleeping in this test.
	 */
	@Test
	public void scenario1Test() throws InterruptedException {
		Game game = launcher.getGame();

		// Scenario 1
		assertFalse(game.isInProgress());
		game.start();
		assertTrue(game.isInProgress());
	}

	@Test
	public void scenario2_1Test() throws InterruptedException {

		Game game = launcher.getGame();
		Player player = game.getPlayers().get(0);
		game.start();
		assertTrue(game.isInProgress());
		Square pSquare = player.getSquare();
		Square t1Square = pSquare.getSquareAt(player.getDirection());
		Pellet pellet = (Pellet) t1Square.getOccupants().get(0);
		assertEquals(pellet.getValue(), 10);

		assertEquals(0, player.getScore());
		game.move(player, player.getDirection());
		assertEquals(player.getSquare(), t1Square);
		assertEquals(10, player.getScore());
		assertFalse(t1Square.getOccupants().contains(pellet));

	}

	@Test
	public void scenario2_2Test() throws InterruptedException {
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

	@Test
	public void scenario2_3Test() throws InterruptedException {
		Game game = launcher.getGame();
		Player player = game.getPlayers().get(0);
		game.start();
		assertTrue(game.isInProgress());

		Square pSquare = player.getSquare();
		Square t1Square = pSquare.getSquareAt(player.getDirection());

		PacManSprites sprites = new PacManSprites();
		Ghost ghost = new Blinky(sprites.getGhostSprite(GhostColor.RED));

		t1Square.put(ghost);
		assertTrue(t1Square.getOccupants().contains(ghost));

		game.move(player, player.getDirection());
		assertFalse(player.isAlive());
		assertFalse(game.isInProgress());
	}

	@Test
	public void scenario2_4Test() throws InterruptedException {
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

	// Scenario 2.5

	@Test
	public void scenario3_1Test() throws InterruptedException {
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

	@Test
	public void scenario3_2Test() throws InterruptedException {
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
		assertEquals(gpellet.getValue(), 10);
		assertTrue(g2Square.isAccessibleTo(ghost));

		// Move the ghost to the left
		g0Square.remove(ghost);
		g2Square.put(ghost);
		
		//Checks if the square has a pellet and a ghost
		Pellet g1pellet = (Pellet) g2Square.getOccupants().get(0);
		g2Square.getOccupants().contains(g1pellet);
		g2Square.getOccupants().contains(ghost);
		
		// Check if the pellet is still there (index should be smaller than index of ghost)
		assertTrue(g2Square.getOccupants().indexOf(ghost) > g2Square.getOccupants().indexOf(g1pellet));

	}

	@Test
	public void scenario3_3Test() throws InterruptedException {
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

		//Checks if the square has a pellet and a ghost
		g1pellet = (Pellet) g2Square.getOccupants().get(0);
		g2Square.getOccupants().contains(g1pellet);
		g2Square.getOccupants().contains(ghost);
		
		// Check if the pellet is still there (index should be greater than index of ghost)
		assertTrue(g2Square.getOccupants().indexOf(ghost) < g2Square.getOccupants().indexOf(g1pellet));
	}

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

		Thread.sleep(5000);
		assertFalse(game.isInProgress());
	}

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
		Thread.sleep(2000);
		assertEquals(player.getSquare(), playerSquare);
		assertTrue(playerSquare.getSquareAt(player.getDirection())
				.getOccupants().contains(ghost));
	}

	@Test
	public void scenario4_2Test() throws InterruptedException {

		Game game = launcher.getGame();
		assertFalse(game.isInProgress());
		game.start();
		assertTrue(game.isInProgress());

	}
}
