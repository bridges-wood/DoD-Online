package dod.game;

import java.awt.Point;
import java.util.Random;

/** Random number generator for all random operations. */
public class RandomNumberGenerator {
  private Random rand;

  RandomNumberGenerator() {
    this.rand = new Random(System.nanoTime());
  }

  /**
   * Generates a random int between 0 and max.
   * 
   * @param max The upper-bound (inclusive) for the result.
   * @return A random integer.
   */
  public int boundedRandom(int max) {
    return rand.nextInt(max);
  }

  /**
   * Generates a random location on the map.
   * 
   * @param map The map in question.
   * @return A random location on the map.
   */
  public Point randomLocation(char[][] map) {
    int y = boundedRandom(map.length);
    int x = boundedRandom(map[0].length);
    return new Point(x, y);
  }

  /**
   * Generates a bounded random location. Each coordinate is between 0 and the
   * axis specific bound.
   * 
   * @param xBound The upper-bound (inclusive) for x-coordinates.
   * @param yBound The upper-bound (inclusive) for y-coordinates.
   * @return A random location.
   */
  public Point randomLocation(int xBound, int yBound) {
    int y = boundedRandom(yBound);
    int x = boundedRandom(xBound);
    return new Point(x, y);
  }
}
