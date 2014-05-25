package nl.tudelft.jpacman.level;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;

import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.PacmanConfigurationException;
import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.npc.NPC;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MapParserTest {

	Launcher launcher;
	MapParser mapparser;
	@Mock
	LevelFactory levelfactory;
	@Mock
	BoardFactory boardfactory;
	@Mock
	Pellet pellet;
	@Mock
	NPC ghost;

	@Before
	public void setUp() {
		mapparser = new MapParser(levelfactory, boardfactory);
		Mockito.when(levelfactory.createPellet()).thenReturn(pellet);
		Mockito.when(levelfactory.createGhost()).thenReturn(ghost);
	}

	@Test
	public void onlyPlayerTest() {
		char[][] map = new char[1][1];
		map[0][0] = 'P';
		mapparser.parseMap(map);
		Mockito.verify(boardfactory).createGround();
		Mockito.verify(boardfactory, Mockito.never()).createWall();
		Mockito.verify(levelfactory, Mockito.never()).createGhost();
	}

	@Test
	public void twoPlayersTest() {
		char[][] map = new char[2][1];
		map[0][0] = 'P';
		map[1][0] = 'P';
		mapparser.parseMap(map);
		Mockito.verify(boardfactory, Mockito.times(2)).createGround();
		Mockito.verify(boardfactory, Mockito.never()).createWall();
		Mockito.verify(levelfactory, Mockito.never()).createGhost();

	}

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
	}

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
		}

	}

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
		}

	}

	@Test(expected = NullPointerException.class)
	public void nullTest() throws IOException {
		char[][] map = null;
		mapparser.parseMap(map);
	}

	@Test
	public void validFileTest() throws IOException {

		mapparser
				.parseMap(Launcher.class.getResourceAsStream("/simplemap.txt"));
		Mockito.verify(boardfactory, Mockito.times(3)).createGround();
		Mockito.verify(boardfactory).createWall();
		Mockito.verify(levelfactory).createGhost();
	}

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
		}

	}
	
	@Test
	public void wrongMapFileTest() throws IOException {
		try {
		mapparser.parseMap(Launcher.class.getResourceAsStream("/wrongmap.txt"));
			fail("Wrong character in map");
		} catch (PacmanConfigurationException e) {
			assertFalse(e.getMessage().equals(null));
			Mockito.verify(boardfactory, Mockito.never()).createGround();
			Mockito.verify(boardfactory, Mockito.never()).createWall();
			Mockito.verify(levelfactory, Mockito.never()).createGhost();
			Mockito.verify(levelfactory, Mockito.never()).createPellet();
		}

	}

}
