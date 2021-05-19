package com.volatil.dod.game;

/** Contains all information relevant for moves. */
public class Move extends Command {
  Direction d;

  /**
   * Default constructor.
   * 
   * @param d Direction of movement.
   */
  Move(Direction d) {
    super(Verb.MOVE);
    this.d = d;
  }

  Move(String input) {
    super(Verb.MOVE);
    this.d = Direction.valueOf(input.substring(5));
  }

  /**
   * @return The direction of this move.
   */
  public Direction getDirection() {
    return d;
  }
}
