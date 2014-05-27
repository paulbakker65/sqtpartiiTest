package nl.tudelft.jpacman.level;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.IOException;

import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.PacmanConfigurationException;
import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.npc.NPC;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * A test suite for the MapParser class from JPacman.
 * @author paulbakker
 */
@RunWith(MockitoJUnitRunner.class)
public class MapParserTest {

	private MapParser mapparser;
	@Mock
	private LevelFactory levelfactory;
	@Mock
	private BoardFactory boardfactory;
	@Mock
	private Pellet pellet;
	@Mock
	private NPC ghost;

	/**
	 * We add the levelfactory and boardfactory to the mapparser. We also define what mockito
	 * should do with the methods createPellet() and createGhost().
	 */
	@Before
	public void setUp() {
		mapparser = new MapParser(levelfactory, boardfactory);
		Mockito.when(levelfactory.createPellet()).thenReturn(pellet);
		Mockito.when(levelfactory.createGhost()).thenReturn(ghost);
	}

	/**
	 * Test with only a player on an 1 by 1 map.
	 */
	@Test
	public void onlyPlayerTest() {
		char[][] map = new char[1][1];
		map[0][0] = 'P';
		mapparser.parseMap(map);
		Mockito.verify(boardfactory).createGround();
		Mockito.verify(boardfactory, Mockito.never()).createWall();
		Mockito.verify(levelfactory, Mockito.never()).createGhost();
		Mockito.verify(levelfactory, Mockito.never()).createPellet();
		Mockito.verify(levelfactory).createLevel(Mockito.any(Board.class), Mockito.anyListOf(NPC.class), Mockito.anyListOf(Square.class));
	}
	
	/**
	 * Test with only a ghost on an 1 by 1 map.
	 */
	@Test
	public void onlyGhostTest() {
		char[][] map = new char[1][1];
		map[0][0] = 'G';
		mapparser.parseMap(map);
		Mockito.verify(boardfactory).createGround();
		Mockito.verify(boardfactory, Mockito.never()).createWall();
		Mockito.verify(levelfactory).createGhost();
		Mockito.verify(levelfactory, Mockito.never()).createPellet();
		Mockito.verify(levelfactory).createLevel(Mockito.any(Board.class), Mockito.anyListOf(NPC.class), Mockito.anyListOf(Square.class));
	}
	
	/**
	 * Test with only a pellet on an 1 by 1 map.
	 */
	@Test
	public void onlyPelletTest() {
		char[][] map = new char[1][1];
		map[0][0] = '.';
		mapparser.parseMap(map);
		Mockito.verify(boardfactory).createGround();
		Mockito.verify(boardfactory, Mockito.never()).createWall();
		Mockito.verify(levelfactory, Mockito.never()).createGhost();
		Mockito.verify(levelfactory).createPellet();
		Mockito.verify(levelfactory).createLevel(Mockito.any(Board.class), Mockito.anyListOf(NPC.class), Mockito.anyListOf(Square.class));
	}
	
	/**
	 * Test with only a wall on an 1 by 1 map.
	 */
	@Test
	public void onlyWallTest() {
		char[][] map = new char[1][1];
		map[0][0] = '#';
		mapparser.parseMap(map);
		Mockito.verify(boardfactory, Mockito.never()).createGround();
		Mockito.verify(boardfactory).createWall();
		Mockito.verify(levelfactory, Mockito.never()).createGhost();
		Mockito.verify(levelfactory, Mockito.never()).createPellet();
		Mockito.verify(levelfactory).createLevel(Mockito.any(Board.class), Mockito.anyListOf(NPC.class), Mockito.anyListOf(Square.class));
		
	}

	/**
	 * A test with two players on a 2 by 1 map.
	 */
	@Test
	public void twoPlayersTest() {
		char[][] map = new char[2][1];
		map[0][0] = 'P';
		map[1][0] = 'P';
		mapparser.parseMap(map);
		Mockito.verify(boardfactory, Mockito.times(2)).createGround();
		Mockito.verify(boardfactory, Mockito.never()).createWall();
		Mockito.verify(levelfactory, Mockito.never()).createGhost();
		Mockito.verify(levelfactory).createLevel(Mockito.any(Board.class), Mockito.anyListOf(NPC.class), Mockito.anyListOf(Square.class));

	}

	/**
	 * A test with all possible units.
	 */
	@Test
	public void validTest() {
		char[][] map = new char[1][4];
		map[0][0] = 'P';
		map[0][1] = '.';
		map[0][2] = 'G';
		map[0][3] = '#';
		mapparser.parseMap(map);
		Mockito.verify(boardfactory, Mockito.times(3)).createGround();
		Mockito.verify(boardfactory).createWall();
		Mockito.verify(levelfactory).createGhost();
		Mockito.verify(levelfactory).createPellet();
		Mockito.verify(levelfactory).createLevel(Mockito.any(Board.class), Mockito.anyListOf(NPC.class), Mockito.anyListOf(Square.class));
	}

	/**
	 * A test with a wrong character.
	 */
	@Test
	public void wrongMapTest() {
		try {
			char[][] map = new char[2][1];
			map[0][0] = 'A';
			map[1][0] = 'P';
			mapparser.parseMap(map);
			fail("Wrong character in map");
		} catch (PacmanConfigurationException e) {
			assertFalse(e.getMessage().equals(null));
			Mockito.verify(boardfactory, Mockito.never()).createGround();
			Mockito.verify(boardfactory, Mockito.never()).createWall();
			Mockito.verify(levelfactory, Mockito.never()).createGhost();
			Mockito.verify(levelfactory, Mockito.never()).createPellet();
			Mockito.verify(levelfactory, Mockito.never()).createLevel(Mockito.any(Board.class), Mockito.anyListOf(NPC.class), Mockito.anyListOf(Square.class));
		}

	}

	/**
	 * A test with an empty map.
	 */
	@Test
	public void emptyMapTest() {
		char[][] map = new char[1][1];

		try {
			mapparser.parseMap(map);
			fail("Empty map");
		} catch (PacmanConfigurationException e) {
			assertFalse(e.getMessage().equals(null));
			Mockito.verify(boardfactory, Mockito.never()).createGround();
			Mockito.verify(boardfactory, Mockito.never()).createWall();
			Mockito.verify(levelfactory, Mockito.never()).createGhost();
			Mockito.verify(levelfactory, Mockito.never()).createPellet();
			Mockito.verify(levelfactory, Mockito.never()).createLevel(Mockito.any(Board.class), Mockito.anyListOf(NPC.class), Mockito.anyListOf(Square.class));
		}

	}
	
	/**
	 * A test with null as a map.
	 * @throws IOException
	 */
	@Test(expected = NullPointerException.class)
	public void nullTest() throws IOException {
		char[][] map = null;
		mapparser.parseMap(map);
	}

	/**
	 * Test for the filereader with a valid map.
	 * @throws IOException  when there is a invalid file
	 */
	@Test
	public void validFileTest() throws IOException {

		mapparser
				.parseMap(Launcher.class.getResourceAsStream("/simplemap.txt"));
		Mockito.verify(boardfactory, Mockito.times(3)).createGround();
		Mockito.verify(boardfactory).createWall();
		Mockito.verify(levelfactory).createGhost();
		Mockito.verify(levelfactory).createLevel(Mockito.any(Board.class), Mockito.anyListOf(NPC.class), Mockito.anyListOf(Square.class));
	}

	/**
	 * Test for the filereader with a empty file.
	 * @throws IOException  when there is a invalid file
	 */
	@Test
	public void emptymapFileTest() throws IOException {

		try {
			mapparser.parseMap(Launcher.class
					.getResourceAsStream("/emptyMap.txt"));
			fail("Empty map");
		} catch (PacmanConfigurationException e) {
			assertFalse(e.getMessage().equals(null));
			Mockito.verify(boardfactory, Mockito.never()).createGround();
			Mockito.verify(boardfactory, Mockito.never()).createWall();
			Mockito.verify(levelfactory, Mockito.never()).createGhost();
			Mockito.verify(levelfactory, Mockito.never()).createPellet();
			Mockito.verify(levelfactory, Mockito.never()).createLevel(Mockito.any(Board.class), Mockito.anyListOf(NPC.class), Mockito.anyListOf(Square.class));
		}

	}

	/**
	 * Tets for the filereader with wrong characters in the file.
	 * @throws IOException when there is a invalid file
	 */
	@Test
	public void wrongMapFileTest() throws IOException {
		try {
			mapparser.parseMap(Launcher.class
					.getResourceAsStream("/wrongMap.txt"));
			fail("Wrong character in map");
		} catch (PacmanConfigurationException e) {
			assertFalse(e.getMessage().equals(null));
			Mockito.verify(boardfactory, Mockito.never()).createGround();
			Mockito.verify(boardfactory, Mockito.never()).createWall();
			Mockito.verify(levelfactory, Mockito.never()).createGhost();
			Mockito.verify(levelfactory, Mockito.never()).createPellet();
			Mockito.verify(levelfactory, Mockito.never()).createLevel(Mockito.any(Board.class), Mockito.anyListOf(NPC.class), Mockito.anyListOf(Square.class));
		}

	}

}
