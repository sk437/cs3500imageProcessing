package imageAsGraph;

/**
 * Holds useful utility commands.
 */
public class Utils {

  public static int roundDouble(double toRound) {
    if (toRound - Math.floor(toRound) < 0.5) {
      return (int) Math.floor(toRound);
    }
    else {
      return (int) Math.ceil(toRound);
    }
  }
}
