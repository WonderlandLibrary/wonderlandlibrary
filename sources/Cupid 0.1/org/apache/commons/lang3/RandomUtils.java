package org.apache.commons.lang3;

import java.util.Random;

public class RandomUtils {
  private static final Random RANDOM = new Random();
  
  public static byte[] nextBytes(int count) {
    Validate.isTrue((count >= 0), "Count cannot be negative.", new Object[0]);
    byte[] result = new byte[count];
    RANDOM.nextBytes(result);
    return result;
  }
  
  public static int nextInt(int startInclusive, int endExclusive) {
    Validate.isTrue((endExclusive >= startInclusive), "Start value must be smaller or equal to end value.", new Object[0]);
    Validate.isTrue((startInclusive >= 0), "Both range values must be non-negative.", new Object[0]);
    if (startInclusive == endExclusive)
      return startInclusive; 
    return startInclusive + RANDOM.nextInt(endExclusive - startInclusive);
  }
  
  public static long nextLong(long startInclusive, long endExclusive) {
    Validate.isTrue((endExclusive >= startInclusive), "Start value must be smaller or equal to end value.", new Object[0]);
    Validate.isTrue((startInclusive >= 0L), "Both range values must be non-negative.", new Object[0]);
    if (startInclusive == endExclusive)
      return startInclusive; 
    return (long)nextDouble(startInclusive, endExclusive);
  }
  
  public static double nextDouble(double startInclusive, double endInclusive) {
    Validate.isTrue((endInclusive >= startInclusive), "Start value must be smaller or equal to end value.", new Object[0]);
    Validate.isTrue((startInclusive >= 0.0D), "Both range values must be non-negative.", new Object[0]);
    if (startInclusive == endInclusive)
      return startInclusive; 
    return startInclusive + (endInclusive - startInclusive) * RANDOM.nextDouble();
  }
  
  public static float nextFloat(float startInclusive, float endInclusive) {
    Validate.isTrue((endInclusive >= startInclusive), "Start value must be smaller or equal to end value.", new Object[0]);
    Validate.isTrue((startInclusive >= 0.0F), "Both range values must be non-negative.", new Object[0]);
    if (startInclusive == endInclusive)
      return startInclusive; 
    return startInclusive + (endInclusive - startInclusive) * RANDOM.nextFloat();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\RandomUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */