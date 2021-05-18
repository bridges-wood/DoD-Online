# Dungeon of Doom

### Prerequisites

- JRE 11.0.9 or above.
- Maven 3.63 or above.

## Installation

### Custom

1. Ensure that all code is in the appropriate package (using the package names in each file).
2. Place all map files into a directory called `maps` inside the root directory.
3. In the root directory, run `mvn package`.

### Default

1. Download the `jar` file from the latest release.

## Running

To run the game run `java -jar release.jar`.

## Gameplay

Beware the _Dungeon of Doom_! Can you escape the claustrophobic confines of the dungeon, or will the monsters of the dark consume you?

### The World

The world of the dungeon is most terrifying, read ahead to gain the knowledge to navigate its arcane depths...

- `P` - You.
- `B` - The fearsome _basilisk_. A mighty serpent that will pursue you if it catches your scent. **Beware!**
- `.` - The cobbled floor of the dungeon. Passable by both you and the basilisk.
- `G` - Gold. Your ticket out of the dungeon if you can collect enough before the basilisk consumes you...
- `E` - The exits of the dungeons. Ply the guards with enough coin and you may yet live, but fail to collect enough and you will surely die.
- `#` - Grated walls of the dungeon, impassable to both humans and basilisks alike.

### Controls

The following commands are available to you in the dungeon:

- `HELLO` - Displays the amount of gold that must be collected before you are able to escape the dungeon.
- `GOLD` - Displays the amount of gold that you currently possess.
- `MOVE <direction>` - Move your character in any one of the cardinal directions (`N,S,E,W`).
- `PICKUP` - Pick up any gold that you are standing on.
- `LOOK` - Your only way to see in the dark! Displays a 5x5 grid the tiles around you.
- `QUIT` - Quits the game. If you are standing on an exit tile, and have sufficient gold, you win, otherwise, the terrors of the dungeon devour you and you lose.

## Implementation - Here be spoilers!

### The Basilisk (aka Bot).

The bot is only able to use the same faculties as the player to traverse and sense the dungeon so it uses the same command objects to communicate with the executors as the player.

#### Decision Making

The bot's turn is as follows, if it does not have a map of its surroundings or it has reached its destination previously specified it performs a `LOOK` and updates its map.

If it now has a map, but no path to a new destination, it looks at the map and if it sees the player, makes the player the new target, otherwise a random point within its view is selected. Then an A\* search is performed to get it to the new position, if this is not possible a new point is chosen until a stack of directions is provided by the search. The advantage of using a stack of directions is that it can be built recursively by backtracking through the path from the origin to the goal and then easily de-populated as each move is executed by the bot.

If the bot has yet to empty the stack of moves, it performs a `MOVE` in the appropriate direction.

### Commands

Command is a super class for all executable options each turn, the only subclass being moves. Moves have the verb `MOVE` but also always have a direction associated with them. This structure allows for easy switching between the different types of commands and also means that the logic for taking a string and turning it to a command only has to happen once, and objects can be used the rest of the time.
