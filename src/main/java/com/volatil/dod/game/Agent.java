package com.volatil.dod.game;

import java.awt.Point;

/** Parent class for any agent in the game. */
public abstract class Agent {
  private Point coordinates;
  private char underneath = '.';

  /**
   * @return The next action of this agent.
   */
  protected abstract Command getNextAction();

  /**
   * @return The coordinates of the agent in the dungeon.
   */
  public Point getLocation() {
    return coordinates;
  }

  /**
   * Updates the location of the agent.
   * 
   * @param p The new position of the agent.
   * @param c The tile previously on the new position.
   */
  protected void updateLocation(Point p, char c) {
    this.coordinates = p;
    this.underneath = c;
  }

  /**
   * @return The tile underneath the agent.
   */
  public char getUnderneath() {
    return underneath;
  }

  /**
   * Changes what is below the agent.
   * 
   * @param c The new tile below the agent.
   */
  public void setUnderneath(char c) {
    this.underneath = c;
  }

}
