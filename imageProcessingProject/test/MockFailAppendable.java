import java.io.IOException;

/**
 * A fake appendable that only throws exceptions, intending to fail to write to it.
 */
public class MockFailAppendable implements Appendable {

  /**
   * Would append given sequence to the structure, but instead only throws an exception.
   *
   * @param csq would be a character sequence to be appended
   * @return would return the appendable itself, with updated content
   * @throws IOException if writing fails, which we care about!
   */
  @Override
  public Appendable append(CharSequence csq) throws IOException {
    throw new IOException();
  }

  /**
   * Would append given sequence to the structure, but instead only throws an exception.
   *
   * @param csq   would be a character sequence to be appended
   * @param start would be where to start the append
   * @param end   would be where to end the append
   * @return would return the appendable itself, with updated content
   * @throws IOException if writing fails, which we care about!
   */
  @Override
  public Appendable append(CharSequence csq, int start, int end) throws IOException {
    throw new IOException();
  }

  /**
   * Would append given character to the structure, but instead only throws an exception.
   *
   * @param c would be a character to be appended
   * @return would return the appendable itself, with updated content
   * @throws IOException if writing fails, which we care about!
   */
  @Override
  public Appendable append(char c) throws IOException {
    throw new IOException();
  }
}

