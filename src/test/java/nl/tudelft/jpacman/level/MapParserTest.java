package nl.tudelft.jpacman.level;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.PacmanConfigurationException;
import nl.tudelft.jpacman.board.BoardFactory;

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

	@Before
	public void setUp() {
		mapparser = new MapParser(levelfactory, boardfactory);

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
	public void wrongMapTest() {
		try {
			char[][] map = new char[2][1];
			map[0][0] = 'A';
			map[1][0] = 'P';
			mapparser.parseMap(map);
			fail("Wrong character in map");
		} catch (PacmanConfigurationException e) {
			if (e.getMessage().isEmpty()) {
				assertTrue(true);
			}
			Mockito.verify(boardfactory, Mockito.never()).createGround();
			Mockito.verify(boardfactory, Mockito.never()).createWall();
			Mockito.verify(levelfactory, Mockito.never()).createGhost();
		}

	}

	@Test
	public void emptyMapTest() {
		char[][] map = new char[1][1];

		try {
			mapparser.parseMap(map);
			fail("a");
		} catch (PacmanConfigurationException e) {
			if (!e.getMessage().equals(null)) {
				assertTrue(true);
			}
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

}
