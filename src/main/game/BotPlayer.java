package main.game;

import java.awt.Point;
import java.util.Stack;

import main.search.AStar;

/** AI player that attempts to pursue the player. */
public class BotPlayer extends Agent {
  /** Random number generator for all random operations. */
  private RandomNumberGenerator gen = new RandomNumberGenerator();

  /** The absolute coordinates of the targeted tile to land on. */
  private Point goal;

  /** The last view of the world from the bot. */
  private char[][] currentMap;

  /** The last command executed by the bot. */
  private Command lastCommand;

  /** The path from the current position to the target. */
  private Stack<Direction> path = new Stack<Direction>();

  @Override
  protected Command getNextAction() {
    if (currentMap == null || onTarget()) {
      goal = null;
      return lastCommand = new Command(Verb.LOOK);
    } else if (path.isEmpty()) {
      while (path.isEmpty()) {
        goal = seekGoal();
        AStar pathfinder = new AStar(this);
        path = pathfinder.search();
      }
    }
    Direction d = path.pop();
    return lastCommand = new Move(d);
  }

  /**
   * Takes in information from the execution of {@link Command commands}.
   * 
   * @param commandResponse The raw response from the command execution.
   */
  protected void takeInput(String commandResponse) {
    switch (lastCommand.getVerb()) {
      case LOOK:
        currentMap = parseMap(commandResponse);
      default:
        break;
    }
  }

  /**
   * Parses a raw map string.
   * 
   * @param map The raw map string.
   * @return A char[][] representing the map.
   */
  private char[][] parseMap(String map) {
    char[][] parsedMap = new char[5][5];
    String[] lines = map.split("\n");
    for (int i = 0; i < 5; i++) {
      parsedMap[i] = lines[i].toCharArray();
    }
    return parsedMap;
  }

  /**
   * Finds the {@link Player} if it is in the bot's map, otherwise gives a random
   * location.
   * 
   * @return The absolute position of the new target.
   */
  private Point seekGoal() {
    Point currentLocation = super.getLocation();
    if (currentMap == null)
      return null;
    if (!onTarget() && goal != null)
      return goal;

    // Look for player on map.
    for (int y = 0; y < 5; y++) {
      for (int x = 0; x < 5; x++) {
        if (currentMap[y][x] == 'P') {
          return new Point(currentLocation.x - 2, currentLocation.y - 2);
        }
      }
    }

    Point relativeTarget = gen.randomLocation(4, 4);
    while (!isValidGoal(relativeTarget) && goal != super.getLocation()) {
      relativeTarget = gen.randomLocation(4, 4);
    }

    return new Point(currentLocation.x + (relativeTarget.x - 2), currentLocation.y + (relativeTarget.y - 2));
  }

  /**
   * @return Whether or not the bot has reached its goal.
   */
  private boolean onTarget() {
    return super.getLocation().equals(goal);
  }

  /**
   * @param target The point under consideration.
   * @return Whether or not the point is a valid goal.
   */
  private boolean isValidGoal(Point target) {
    return currentMap[target.y][target.x] != '#' && target != new Point();
  }

  /**
   * @return The bot's current goal.
   */
  public Point getGoal() {
    return goal;
  }

  /**
   * @return The bot's last view of the dungeon.
   */
  public char[][] getMap() {
    return currentMap;
  }

  /** Debug method for examining internal state of the bot. */
  public void debug() {
    System.out.println("--- BOT ---");
    if (lastCommand != null)
      System.out.println("Last Command: " + lastCommand.toString());
    if (!path.isEmpty())
      System.out.println("Next Moves: " + path);
    if (goal != null)
      System.out.println("Target: " + goal.toString());
    System.out.println("--- END ---");
  }
}
