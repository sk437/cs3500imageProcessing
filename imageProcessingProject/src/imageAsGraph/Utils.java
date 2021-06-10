package imageAsGraph;

/**
 * Holds useful utility commands.
 */
public class Utils {

  /**
   * Rounds a double into an integer, rounding up at 0.5 or higher.
   *
   * @param toRound The double to be rounded
   * @return The integer result
   */
  public static int roundDouble(double toRound) {
    if (toRound - Math.floor(toRound) < 0.5) {
      return (int) Math.floor(toRound);
    } else {
      return (int) Math.ceil(toRound);
    }
  }
}
