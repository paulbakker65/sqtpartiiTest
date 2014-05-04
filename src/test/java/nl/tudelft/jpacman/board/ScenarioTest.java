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
     * Launch the game, and imitate what would happen
     * in a typical game.
     * @throws InterruptedException Since we're sleeping in this test.
     */
    @Test
    public void startTest() throws InterruptedException {
        Game game = launcher.getGame();        
        Player player = game.getPlayers().get(0);
 
        //Scenario 1
        assertFalse(game.isInProgress());
        game.start();
        assertTrue(game.isInProgress());
        
        //Scenario 2.1
        assertTrue(game.isInProgress());
        Square pSquare = player.getSquare();
        Square t1Square = pSquare.getSquareAt(player.getDirection());
        Pellet pellet = (Pellet) t1Square.getOccupants().get(0);
        assertEquals(pellet.getValue(),10);
        
        assertEquals(0, player.getScore());
        game.move(player, player.getDirection());
        assertEquals(player.getSquare(), t1Square);
        assertEquals(10, player.getScore());
        assertFalse(t1Square.getOccupants().contains(pellet));
        
        //Scenario 2.2
        assertTrue(game.isInProgress());
        assertEquals(player.getDirection(),Direction.EAST);
        game.move(player, Direction.WEST);
        assertTrue(t1Square.getOccupants().isEmpty());
        
        int score = player.getScore();
        game.move(player, Direction.EAST);
        assertEquals(player.getSquare(), t1Square);
        assertEquals(player.getScore(), score);
        
        //Scenario 2.3
        assertTrue(game.isInProgress());
        Square t2Square = t1Square.getSquareAt(player.getDirection());
        PacManSprites sprites = new PacManSprites();
        Ghost ghost = new Blinky(sprites.getGhostSprite(GhostColor.RED));
        t2Square.put(ghost);
        assertTrue(t2Square.getOccupants().contains(ghost));
        
        game.move(player, player.getDirection());
        assertFalse(player.isAlive());
        assertFalse(game.isInProgress());
        
        //Scenario 2.4
        game = launcher.makeGame();
        player = game.getPlayers().get(0);
        game.start();
        assertTrue(game.isInProgress());
        Square playerSquare = player.getSquare();
        Square t3Square = playerSquare.getSquareAt(Direction.NORTH);
        assertFalse(t3Square.isAccessibleTo(player));
        
        game.move(player, Direction.NORTH);
        assertEquals(player.getSquare(), playerSquare);
        
        //Scenario 2.5
        
        
        
        //Scenario 4.1
        game = launcher.makeGame();
        player = game.getPlayers().get(0);
        game.start();
        assertTrue(game.isInProgress());
        playerSquare = player.getSquare();
        playerSquare.getSquareAt(player.getDirection()).put(ghost);
        
        
        game.stop();
        assertFalse(game.isInProgress());
        game.move(player, player.getDirection());
        assertEquals(player.getSquare(), playerSquare);
        Thread.sleep(5000);
        assertEquals(player.getSquare(), playerSquare);
        assertTrue(playerSquare.getSquareAt(player.getDirection()).getOccupants().contains(ghost));
      
        //Scenario 4.2
        assertFalse(game.isInProgress());
        game.start();
        assertTrue(game.isInProgress());
        
    }
}
