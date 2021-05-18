package main.game;

/** Class containing all command information. */
public class Command {
  private Verb v;

  /**
   * Default constructor.
   * 
   * @param v The {@link Verb}.
   */
  Command(Verb v) {
    this.v = v;
  }

  /**
   * Creates commands for players from CLI input.
   * 
   * @param input Raw command line input.
   */
  Command(String input) {
    try {
      switch (input) {
        case "HELLO":
          this.v = Verb.HELLO;
          break;
        case "GOLD":
          this.v = Verb.GOLD;
          break;
        case "PICKUP":
          this.v = Verb.PICKUP;
          break;
        case "LOOK":
          this.v = Verb.LOOK;
          break;
        case "QUIT":
          this.v = Verb.QUIT;
          break;
        default:
          throw new IllegalArgumentException("Command not recognised.");
      }
    } catch (Exception e) {
      this.v = null;
    }
  }

  /**
   * @return The verb of this command.
   */
  public Verb getVerb() {
    return v;
  }

  @Override
  public String toString() {
    return v.toString();
  }
}