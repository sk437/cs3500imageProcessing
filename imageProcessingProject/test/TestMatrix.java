import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import mutators.matrices.Matrix;
import mutators.matrices.MatrixImpl;
import org.junit.Test;

/**
 * To test the MatrixImpl class.
 */
public class TestMatrix {

  private Matrix m0;
  private Matrix m1;
  private Matrix m2;
  private Matrix m3;

  /**
   * To initialize test variables.
   */
  private void setUp() {
    this.m0 = new MatrixImpl(new ArrayList<Double>(Arrays.asList(1.0, 2.0, 3.0, 4.0)), 2, 2);
    this.m1 = new MatrixImpl(new ArrayList<Double>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0)),
        3, 2);
    this.m2 = new MatrixImpl(new ArrayList<Double>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0)),
        2, 3);
    this.m3 = new MatrixImpl(
        new ArrayList<Double>(Arrays.asList(1.1, 2.2, 3.3, 4.4, 5.5, 6.6, 7.7, 8.8,
            9.9)), 3, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullValue() {
    Matrix mat0 = new MatrixImpl(null, 2, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullDouble() {
    ArrayList<Double> withNull = new ArrayList<Double>();
    withNull.add(null);
    Matrix mat = new MatrixImpl(withNull, 1, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInsufficientValues() {
    Matrix mat0 = new MatrixImpl(new ArrayList<Double>(Arrays.asList(1.0)), 2, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTooManyValues() {
    Matrix mat0 = new MatrixImpl(new ArrayList<Double>(Arrays.asList(1.0, 2.0, 3.0, 4.0)),
        1, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testZeroWidth() {
    Matrix mat0 = new MatrixImpl(new ArrayList<Double>(Arrays.asList(1.0)), 0, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testZeroHeight() {
    Matrix mat0 = new MatrixImpl(new ArrayList<Double>(Arrays.asList(1.0)), 1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeWidth() {
    Matrix mat0 = new MatrixImpl(new ArrayList<Double>(Arrays.asList(1.0)), -1, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeHeight() {
    Matrix mat0 = new MatrixImpl(new ArrayList<Double>(Arrays.asList(1.0)), 2, -1);
  }

  @Test
  public void testConstruction() {
    Matrix mat0 = new MatrixImpl(new ArrayList<Double>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0)),
        2, 3);
    assertEquals(2, mat0.getWidth());
    assertEquals(3, mat0.getHeight());
    assertEquals(1.0, mat0.getValue(0, 0), 0.1);
    assertEquals(2.0, mat0.getValue(1, 0), 0.1);
    assertEquals(3.0, mat0.getValue(0, 1), 0.1);
    assertEquals(4.0, mat0.getValue(1, 1), 0.1);
    assertEquals(5.0, mat0.getValue(0, 2), 0.1);
    assertEquals(6.0, mat0.getValue(1, 2), 0.1);
  }

  @Test
  public void testGetWidth() {
    this.setUp();
    assertEquals(2, m0.getWidth());
    assertEquals(3, m1.getWidth());
  }

  @Test
  public void getHeight() {
    this.setUp();
    assertEquals(2, m0.getHeight());
    assertEquals(2, m1.getHeight());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetXOutOfBounds() {
    this.setUp();
    this.m0.getValue(5, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetYOutOfBounds() {
    this.setUp();
    this.m0.getValue(0, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetNegativeX() {
    this.setUp();
    this.m0.getValue(-3, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetNegativeY() {
    this.setUp();
    this.m0.getValue(0, -2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetNegativeBoth() {
    this.setUp();
    this.m0.getValue(-3, -2);
  }

  @Test
  public void testGetValue() {
    this.setUp();
    assertEquals(1.0, this.m0.getValue(0, 0), 0.1);
    assertEquals(2.0, this.m1.getValue(1, 0), 0.1);
    assertEquals(4.0, this.m2.getValue(1, 1), 0.1);
    assertEquals(9.9, this.m3.getValue(2, 2), 0.1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullSecond() {
    this.setUp();
    this.m0.matrixMultiply(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFullyInvalidMultiplication() {
    this.setUp();
    this.m0.matrixMultiply(m3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMultiplication() {
    this.setUp();
    this.m0.matrixMultiply(m2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMultiplicationOrder() {
    this.setUp();
    this.m1.matrixMultiply(m0);
  }

  @Test
  public void testMultiplication() {
    this.setUp();
    Matrix result = this.m0.matrixMultiply(m1);
    assertEquals(3, result.getWidth());
    assertEquals(2, result.getHeight());
    assertEquals(9.0, result.getValue(0, 0), 0.1);
    assertEquals(12.0, result.getValue(1, 0), 0.1);
    assertEquals(15.0, result.getValue(2, 0), 0.1);
    assertEquals(19.0, result.getValue(0, 1), 0.1);
    assertEquals(26.0, result.getValue(1, 1), 0.1);
    assertEquals(33.0, result.getValue(2, 1), 0.1);
    Matrix result2 = this.m2.matrixMultiply(m1);
    assertEquals(3, result2.getWidth());
    assertEquals(3, result2.getHeight());
    assertEquals(9.0, result2.getValue(0, 0), 0.1);
    assertEquals(12.0, result2.getValue(1, 0), 0.1);
    assertEquals(15.0, result2.getValue(2, 0), 0.1);
    assertEquals(19.0, result2.getValue(0, 1), 0.1);
    assertEquals(26.0, result2.getValue(1, 1), 0.1);
    assertEquals(33.0, result2.getValue(2, 1), 0.1);
    assertEquals(29.0, result2.getValue(0, 2), 0.1);
    assertEquals(40.0, result2.getValue(1, 2), 0.1);
    assertEquals(51.0, result2.getValue(2, 2), 0.1);
  }
}
