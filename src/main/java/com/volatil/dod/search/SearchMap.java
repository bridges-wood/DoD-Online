package com.volatil.dod.search;

import java.awt.Point;
import java.util.Arrays;
import java.util.LinkedList;

import com.volatil.dod.game.BotPlayer;

/** Specialised version of the map for idiomatic searching. */
public class SearchMap {
  private SearchNode[][] map;

  /**
   * Default constructor.
   * 
   * @param b The {@link BotPlayer bot} which is searching for a path to its
   *          target.
   */
  SearchMap(BotPlayer b) {
    Point current = b.getLocation(), goal = b.getGoal();

    // Offsets between absolute coordinates and the map's coordinates.
    int xOffset = -current.x + 2;
    int yOffset = -current.y + 2;

    // Generation of the map.
    char[][] botMap = b.getMap();
    int ySize = botMap.length, xSize = botMap[0].length;
    SearchNode[][] searchMap = new SearchNode[ySize][xSize];
    for (int y = 0; y < ySize; y++) {
      for (int x = 0; x < xSize; x++) {
        searchMap[y][x] = new SearchNode(botMap[y][x]);
      }
    }

    // Sets the goal.
    searchMap[goal.y + yOffset][goal.x + xOffset].makeGoal();
    this.map = searchMap;
  }

  /**
   * @param target The point under consideration.
   * @return All neighbours of the point in question which can be moved to.
   */
  public Point[] getValidNeighbours(Point target) {
    LinkedList<Point> neighbours = new LinkedList<Point>();
    neighbours.add(new Point(target.x, target.y - 1)); // N
    neighbours.add(new Point(target.x, target.y + 1)); // S
    neighbours.add(new Point(target.x - 1, target.y)); // E
    neighbours.add(new Point(target.x + 1, target.y)); // W
    neighbours = reduceNeighbours(neighbours);
    return Arrays.copyOf(neighbours.toArray(), neighbours.size(), Point[].class);
  }

  /**
   * Removes all invalid points from the list of neighbours.
   * 
   * @param neighbours The list of neighbours of a given point.
   * @return All valid points from the initial list.
   */
  private LinkedList<Point> reduceNeighbours(LinkedList<Point> neighbours) {
    // Removes all nodes that are not in the map, and those that cannot be moved to.
    neighbours.removeIf(n -> {
      try {
        SearchNode node = get(n);
        return node.getContents() == '#';
      } catch (NullPointerException e) {
        return true;
      }
    });
    return neighbours;
  }

  /**
   * @param p The coordinates of the {@link SearchNode}.
   * @return The SearchNode at the coordinates specified.
   * @see SearchNode
   */
  public SearchNode get(Point p) {
    try {
      return map[p.y][p.x];
    } catch (ArrayIndexOutOfBoundsException e) {
      return null;
    }
  }

  /**
   * Updates the total cost of the {@link SearchNode} at a point on the map.
   * 
   * @param target    The coordinates of the node.
   * @param totalCost The new total cost.
   */
  public void updateTotalCost(Point target, double totalCost) {
    map[target.y][target.x].setTotalCost(totalCost);
  }

  /**
   * Updates the path cost of the {@link SearchNode} at a point on the map.
   * 
   * @param target   The coordinates of the node.
   * @param pathCost The new path cost.
   */
  public void updatePathCost(Point target, double pathCost) {
    map[target.y][target.x].setPathCost(pathCost);
  }

  /**
   * Updates the parent node of the {@link SearchNode} at a point on the map.
   * 
   * @param target The coordinates of the node.
   * @param parent The coordinates of the target's parent node.
   */
  public void updateNodeParent(Point target, Point parent) {
    map[target.y][target.x].setPrevious(parent);
  }
}
