package main.game;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Runs the game with a human player and contains code needed to read inputs.
 *
 */
public class HumanPlayer extends Agent {
    /** The amount of gold the player has. */
    private int gold;
    /** Global BufferedReader for taking player input. */
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    /** Default constructor. */
    public HumanPlayer() {
        this.gold = 0;
    }

    /**
     * Debug constructor.
     * 
     * @param gold The amount of gold the player starts with.
     */
    public HumanPlayer(int gold) {
        this.gold = gold;
    };

    @Override
    protected Command getNextAction() {
        String input = getInputFromConsole();
        String formattedCommand = formatCommand(input);
        try {
            return new Move(formattedCommand);
        } catch (Exception e) {
            return new Command(formattedCommand);
        }
    }

    /**
     * Reads player's input from the console.
     * 
     * @return A string containing the input the player entered.
     */
    protected String getInputFromConsole() {
        try {
            System.out.print("> ");
            return br.readLine();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to get input.");
            return "Invalid";
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

}