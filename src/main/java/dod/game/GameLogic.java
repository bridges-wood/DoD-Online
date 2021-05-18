package dod.game;

import java.awt.Point;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Contains main logic.
 */
public class GameLogic {
    private Map map;
    private HumanPlayer player = new HumanPlayer();
    private BotPlayer bot = new BotPlayer();
    // ! This may need to be changed based on how the file is run.
    private final String MAPS_PATH = "dod/maps";
    /**
     * Loader for accessing map files.
     */
    private final ClassLoader cl = Thread.currentThread().getContextClassLoader();

    /**
     * Default constructor. Presents map selection menu to the player and loads.
     */
    public GameLogic() {
        String mapChoice = null;
        boolean maps = displayMenu();
        if (maps) {
            mapChoice = parseMapSelection();
        }
        this.map = new Map(mapChoice);
        map.spawnAgent(player);
        map.spawnAgent(bot);
    }

    /**
     * The main gameplay loop. Handles both {@link HumanPlayer human} and
     * {@link BotPlayer bot} moves.
     */
    private void runGame() {
        while (true) {
            String playerResponse = executeCommand(player.getNextAction(), player);
            System.out.println(playerResponse);
            String botResponse = executeCommand(bot.getNextAction(), bot);
            bot.takeInput(botResponse);
        }
    }

    /**
     * Executes an {@link Agent agent's} {@link Command command} and returns the
     * string response.
     * 
     * @param command The command to be executed.
     * @param agent   The agent executing the command.
     * @return The string response from the execution of the command.
     */
    private String executeCommand(Command command, Agent agent) {
        try {
            switch (command.getVerb()) {
                case HELLO:
                    return hello();
                case GOLD:
                    return gold();
                case PICKUP:
                    return pickup();
                case LOOK:
                    return look(agent);
                case MOVE:
                    Move m = (Move) command;
                    return move(m.getDirection(), agent);
                case QUIT:
                    quitGame();
                default:
                    return "Invalid";
            }
        } catch (NullPointerException e) {
            return "Invalid";
        }
    }

    /**
     * Displays the menu to the user. Consists of a list of all the available maps
     * the player can choose.
     * 
     * @return true if maps were found, false otherwise.
     */
    boolean displayMenu() {
        try {
            URI uri = cl.getResource(MAPS_PATH).toURI();
            Path path;
            if (uri.getScheme().equals("jar")) {
                FileSystem fs = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
                path = fs.getPath(MAPS_PATH);
            } else {
                path = Paths.get(uri);
            }
            Stream<Path> walk = Files.walk(path, 1);

            List<String> maps = walk.map(file -> file.getFileName().toString()).collect(Collectors.toList());
            walk.close();

            maps.removeIf(file -> !file.endsWith(".txt"));
            if (maps.size() == 0)
                throw new IOException("No maps found.");

            System.out.println("--- MAPS ---");
            for (final String file : maps) {
                System.out.println(file);
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Allows the user to choose a {@link Map map}.
     * 
     * @return String The path to the map the user chose.
     */
    private String parseMapSelection() {
        System.out.println("Please select a map.");
        String finalPath = MAPS_PATH + "/" + player.getInputFromConsole();
        return finalPath;
    }

    /**
     * @return Gold required to win.
     */
    protected String hello() {
        return "Gold to win " + String.valueOf(map.getGoldRequired());
    }

    /**
     * @return Gold currently owned by the player.
     */
    protected String gold() {
        return String.valueOf("Gold owned: " + player.getGold());
    }

    /**
     * Checks if movement is legal and updates the {@link Agent agent's} location on
     * the {@link Map map}.
     *
     * @param d     The direction of the movement.
     * @param agent The agent that is moving.
     * @return A string indicating whether or not the move was successful.
     */
    protected String move(Direction d, Agent agent) {
        Point current = agent.getLocation();
        try {
            Point next;
            switch (d) {
                case N:
                    next = new Point(current.x, current.y - 1);
                    break;
                case S:
                    next = new Point(current.x, current.y + 1);
                    break;
                case E:
                    next = new Point(current.x + 1, current.y);
                    break;
                case W:
                    next = new Point(current.x - 1, current.y);
                    break;
                default:
                    throw new IllegalArgumentException("Direction must be cardinal");
            }
            if (map.open(next)) {
                map.moveAgent(next, agent);
                return "Success";
            } else {
                return "Fail";
            }
        } catch (IllegalArgumentException e) {
            return "Fail";
        }
    }

    /**
     * Generates the view of a given {@link Agent agent} on the {@link Map map}.
     * 
     * @param agent The agent from whose perspective the view is.
     * @return A String representation of the game map from the perspective of the
     *         agent.
     */
    protected String look(Agent agent) {
        char[][] currentMap = map.getMap();
        Point location = agent.getLocation();
        StringBuilder view = new StringBuilder();
        for (int y = location.y - 2; y <= location.y + 2; y++) {
            for (int x = location.x - 2; x <= location.x + 2; x++) {
                try {
                    view.append(currentMap[y][x]);
                } catch (IndexOutOfBoundsException e) {
                    view.append('#');
                }
            }
            view.append('\n');
        }
        view.deleteCharAt(view.length() - 1); // Remove terminating \n
        return view.toString();
    }

    /**
     * Processes the player's pickup command, updating the map and the player's gold
     * amount.
     *
     * @return If the player successfully picked-up gold or not.
     */
    protected String pickup() {
        if (player.getUnderneath() != 'G')
            return "Fail";
        player.addGold();
        player.setUnderneath('.');
        return "Success " + gold();
    }

    /**
     * Quits the game, shutting down the application.
     */
    protected void quitGame() {
        if (player.getGold() >= map.getGoldRequired() && player.getUnderneath() == 'E') {
            System.out.println("WIN - You won the game!");
        } else {
            System.out.println("LOSE");
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        GameLogic gl = new GameLogic();
        gl.runGame();
    }
}