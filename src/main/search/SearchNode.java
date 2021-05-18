package main.search;

import java.awt.Point;

/** Representation of a point on the map for search purposes. */
public class SearchNode {
  private double totalCost = Double.MAX_VALUE;
  private double pathCost = Double.MAX_VALUE;
  private Point previous = null;
  private char contents;
  private boolean goal = false;

  /**
   * Default constructor.
   * 
   * @param contents The value of the tile on the map.
   */
  public SearchNode(char contents) {
    this.contents = contents;
  }

  /**
   * @return The total cost of the node.
   */
  protected double getTotalCost() {
    return totalCost;
  }

  /**
   * @param cost The new total cost of the node.
   */
  protected void setTotalCost(double cost) {
    this.totalCost = cost;
  }

  /**
   * @return The cost of getting from the origin to this node.
   */
  protected double getPathCost() {
    return pathCost;
  }

  /**
   * @param cost The new cost of getting from the origin to this node.
   */
  protected void setPathCost(double cost) {
    this.pathCost = cost;
  }

  /**
   * @return The node from which this one is reached when moving from the origin.
   */
  protected Point getPrevious() {
    return previous;
  }

  /**
   * @param previous The new node from which this one is reached when travelling
   *                 from the origin.
   */
  protected void setPrevious(Point previous) {
    this.previous = previous;
  }

  /**
   * @return The character that exists at this point in the dungeon.
   */
  protected char getContents() {
    return contents;
  }

  /**
   * @return Whether this node is the goal node.
   */
  protected boolean isGoal() {
    return goal;
  }

  /**
   * Makes this node the goal node.
   */
  protected void makeGoal() {
    this.goal = true;
  }
}
