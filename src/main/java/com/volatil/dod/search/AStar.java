package com.volatil.dod.search;

import java.awt.Point;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Stack;

import com.volatil.dod.game.BotPlayer;
import com.volatil.dod.game.Direction;

/** Implementation of A* search algorithm for {@link BotPlayer} pathfinding. */
public class AStar {
  /** Version of the agent's view exclusively for searching. */
  private SearchMap map;
  /** The position of the agent's origin relative to the {@link SearchMap}. */
  private Point origin;
  /**
   * The position of the agent's goal relative to the search {@link SearchMap}.
   */
  private Point goal;
  /** The comparator used for ordering the PriorityQueue of the frontier. */
  private Comparator<Point> scoreComparator = new Comparator<Point>() {
    @Override
    public int compare(Point a, Point b) {
      SearchNode aNode = map.get(a);
      SearchNode bNode = map.get(b);
      return Double.compare(aNode.getTotalCost(), bNode.getTotalCost());
    }
  };

  /**
   * Default constructor.
   * 
   * @param b The {@link BotPlayer bot} that is searching for a path to its goal.
   */
  public AStar(BotPlayer b) {
    Point current = b.getLocation(), goal = b.getGoal();
    this.map = new SearchMap(b);

    // Needed to translate the goal to its absolute position.
    int xOffset = -current.x + 2;
    int yOffset = -current.y + 2;

    this.goal = new Point(goal.x + xOffset, goal.y + yOffset);
    this.origin = new Point(2, 2);
  }

  /**
   * Generates the path needed to travel from the origin to the current point.
   * 
   * @param current The coordinates of the current point.
   * @return A stack of directions that the agent needs to take in order to reach
   *         the target.
   * @see Direction
   */
  public Stack<Direction> reconstructPath(Point current) {
    Point previous = map.get(current).getPrevious();
    if (previous == null) {
      return new Stack<Direction>();
    } else {
      Stack<Direction> path = reconstructPath(previous);
      path.add(determineDirection(current, previous));
      return path;
    }
  }

  /**
   * Calculates the direction needed to move between two adjacent nodes.
   * 
   * @param current  The current node.
   * @param previous The previous node.
   * @return The direction taken to move from previous to current.
   * @see Direction
   */
  private Direction determineDirection(Point current, Point previous) {
    if (previous.y - 1 == current.y) {
      return Direction.N;
    } else if (previous.y + 1 == current.y) {
      return Direction.S;
    } else if (previous.x + 1 == current.x) {
      return Direction.E;
    } else if (previous.x - 1 == current.x) {
      return Direction.W;
    } else {
      throw new IllegalArgumentException("Points are not adjacent.");
    }
  }

  /**
   * A* search algorithm for traversing the game map.
   * 
   * @return The path through the map to get to the goal, or null if the path does
   *         not exist.
   * @see <a href="https://en.wikipedia.org/wiki/A*_search_algorithm">A* Search
   *      Algorithm</a>
   */
  public Stack<Direction> search() {
    PriorityQueue<Point> frontier = new PriorityQueue<Point>(scoreComparator);

    // Initialisation of the origin.
    frontier.add(origin);
    map.updateTotalCost(origin, computeHeuristic(origin));
    map.updatePathCost(origin, 0);

    while (!frontier.isEmpty()) {
      Point current = frontier.poll();
      if (map.get(current).isGoal())
        return reconstructPath(current);

      for (Point neighbour : map.getValidNeighbours(current)) {
        double tentativeCost = map.get(current).getPathCost() + score(neighbour, goal);
        if (tentativeCost < map.get(neighbour).getTotalCost()) {
          map.updateNodeParent(neighbour, current);
          map.updatePathCost(neighbour, tentativeCost);
          double totalCost = tentativeCost + computeHeuristic(neighbour);
          map.updateTotalCost(neighbour, totalCost);

          if (!frontier.contains(neighbour))
            frontier.add(neighbour);
        }
      }
    }

    // If there is no path to the goal.
    return new Stack<Direction>();
  }

  /**
   * Computes the manhattan distance between two points.
   * 
   * @param a A point in 2D space.
   * @param b Another point in 2D space.
   * @return The manhattan distance from a to b.
   * @see <a href="https://en.wikipedia.org/wiki/Taxicab_geometry">Manhattan
   *      Distance</a>
   */
  private int score(Point a, Point b) {
    return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
  }

  /**
   * Computes the manhattan distance between any node and the goal node.
   * 
   * @param current The node from which the distance is calculated.
   * @return The manhattan distance between the current node and the goal.
   */
  private int computeHeuristic(Point current) {
    return score(current, goal);
  }
}
