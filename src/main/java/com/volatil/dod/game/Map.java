package com.volatil.dod.game;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Reads and contains in memory the map of the game.
 *
 */
public class Map {
	/**
	 * Loader for accessing map files.
	 */
	private final ClassLoader cl = Thread.currentThread().getContextClassLoader();

	/** Random seed for placing agents in the map */
	private RandomNumberGenerator gen = new RandomNumberGenerator();

	/** Representation of the map */
	private char[][] map;

	/** Map name */
	private String mapName;

	/** Gold required for the human player to win */
	private int goldRequired;

	/**
	 * Default constructor, creates the default map "Very small Labyrinth of doom".
	 */
	public Map() {
		mapName = "Very small Labyrinth of Doom";
		goldRequired = 2;
		map = new char[][] {
				{ '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#' },
				{ '#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#' },
				{ '#', '.', '.', '.', '.', '.', '.', 'G', '.', '.', '.', '.', '.', '.', '.', '.', '.', 'E', '.', '#' },
				{ '#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#' },
				{ '#', '.', '.', 'E', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#' },
				{ '#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', 'G', '.', '.', '.', '.', '.', '.', '#' },
				{ '#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#' },
				{ '#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#' },
				{ '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#' } };
	}

	/**
	 * Constructor that accepts a map to read in from.
	 *
	 * @param filename The filename of the map file.
	 */
	public Map(String filename) {
		this();
		try {
			readMap(filename);
			System.out.println("Map loaded.");
		} catch (Exception e) {
			System.err.println("Cannot find map, using default map.");
		}
	}

	/**
	 * @return Gold required to exit the current map.
	 */
	protected int getGoldRequired() {
		return goldRequired;
	}

	/**
	 * @return The map as stored in memory.
	 */
	protected char[][] getMap() {
		return map;
	}

	/**
	 * @return The name of the current map.
	 */
	protected String getMapName() {
		return mapName;
	}

	/**
	 * Reads the map from file.
	 *
	 * @param filename Name of the map's file.
	 * @throws Exception If the map cannot be read.
	 */
	protected void readMap(String filename) throws Exception {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(cl.getResource(filename).openStream()))) {
			this.mapName = br.readLine();
			this.goldRequired = Integer.parseInt(br.readLine().split(" ")[1]);
			ArrayList<String> rawMap = new ArrayList<String>();
			String nextLine;
			while ((nextLine = br.readLine()) != null) {
				rawMap.add(nextLine);
			}
			this.map = parseMap(rawMap);
		} catch (Exception e) {
			throw new Exception("Failed to read map.");
		}
	}

	/**
	 * Converts the lines from a map file to a character array.
	 * 
	 * @param rawMap Line from the map file.
	 * @return A array representation of the map.
	 * @throws ParseException Cannot read non-rectangular maps.
	 */
	protected char[][] parseMap(ArrayList<String> rawMap) throws ParseException {
		int height = rawMap.size();
		int width = rawMap.get(0).length();
		char[][] map = new char[height][width];
		int rowIndex = 0;
		for (String row : rawMap) {
			if (row.length() != width)
				throw new ParseException("Map is not rectangular", rowIndex);
			map[rowIndex] = row.toCharArray();
			rowIndex++;
		}
		return map;
	}

	/**
	 * Places an {@link Agent agent} at the first open square found.
	 * 
	 * @param agent The agent being placed down.
	 */
	public void spawnAgent(Agent agent) {
		Point location = gen.randomLocation(map);
		while (!isValidStartPosition(location)) {
			location = gen.randomLocation(map);
		}
		map[location.y][location.x] = agent instanceof RemotePlayer ? 'P' : 'B';
		agent.updateLocation(location, '.');
	}

	/**
	 * @param location The location under consideration.
	 * @return Whether the given location is a valid starting position.
	 */
	private boolean isValidStartPosition(Point location) {
		switch (map[location.y][location.x]) {
			case '.':
				return true;
			case '#':
			case 'B':
			case 'G':
			case 'E':
			case 'P':
			default:
				return false;
		}
	}

	/**
	 * @param location The location under consideration.
	 * @return Whether the given location can be moved to.
	 */
	public boolean open(Point location) {
		switch (map[location.y][location.x]) {
			case '.':
			case 'P':
			case 'B':
			case 'G':
			case 'E':
				return true;
			default:
				return false;
		}
	}

	/**
	 * Moves an {@link Agent agent} from their current position to a new position.
	 * 
	 * @param next  The next position the agent will occupy.
	 * @param agent The agent moving to the new position.
	 */
	public String moveAgent(Point next, Agent agent) {
		Point last = agent.getLocation();
		map[last.y][last.x] = agent.getUnderneath();
		if (map[next.y][next.x] == 'P' && agent instanceof BotPlayer) {
			return "LOSE";
		}
		agent.updateLocation(next, map[next.y][next.x]);
		map[next.y][next.x] = agent instanceof RemotePlayer ? 'P' : 'B';
		return "Success";
	}

	/**
	 * Debug function for examining the map as a whole.
	 */
	public void debug() {
		StringBuilder sb = new StringBuilder();
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[0].length; x++) {
				sb.append(map[y][x]);
			}
			sb.append('\n');
		}
		System.out.println("--- MAP ---");
		System.out.print(sb.toString());
		System.out.println("--- END ---");
	}

}
