package com.volatil.dod.game;

public class RemotePlayer extends Agent {
  /** The amount of gold the player has. */
  private int gold;
  private String nextAction;

  /** Default constructor. */
  public RemotePlayer() {
    this.gold = 0;
  }

  /**
   * Debug constructor.
   * 
   * @param gold The amount of gold the player starts with.
   */
  public RemotePlayer(int gold) {
    this.gold = gold;
  };

  @Override
  protected Command getNextAction() {
    String input = nextAction;
    clearNextAction();
    String formattedCommand = formatCommand(input);
    try {
      return new Move(formattedCommand);
    } catch (Exception e) {
      return new Command(formattedCommand);
    }
  }

  /**
   * Formats raw command input before it is fully parsed.
   * 
   * @param input The raw input from the user.
   * @return A better formatted string for the constructors.
   */
  protected String formatCommand(String input) {
    return input.toUpperCase();
  }

  /**
   * @return The number of gold the player has collected.
   */
  public int getGold() {
    return gold;
  }

  /** Increments the player's gold total. */
  public void addGold() {
    gold += 1;
  }

  public void setNextAction(String nextAction) {
    this.nextAction = nextAction;
  }

  private void clearNextAction() {
    this.nextAction = null;
  }
}
