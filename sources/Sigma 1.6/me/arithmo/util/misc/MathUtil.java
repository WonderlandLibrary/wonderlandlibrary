package me.arithmo.util.misc;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class MathUtil
{
  private static final Random rng = new Random();
  
  public static double round(double value, int places)
  {
    if (places < 0) {
      throw new IllegalArgumentException();
    }
    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }
  
  public static Random getRng()
  {
    return rng;
  }
  
  public static int customRandInt(int min, int max)
  {
    return new Random().nextInt(max - min + 1) + min;
  }
  
  public static int getRandom(int cap)
  {
    return rng.nextInt(cap);
  }
  
  public static int getRandom(int floor, int cap)
  {
    return floor + rng.nextInt(cap - floor + 1);
  }
  
  public static double roundToPlace(double value, int places)
  {
    if (places < 0) {
      throw new IllegalArgumentException();
    }
    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }

public static double square(double in)
  {
    return in * in;
  }
}
