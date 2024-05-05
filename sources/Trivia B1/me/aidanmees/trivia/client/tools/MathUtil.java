package me.aidanmees.trivia.client.tools;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class MathUtil
{
  private static Random rng = new Random();

  
  public static boolean isInteger(String num)
  {
    try
    {
      Integer.parseInt(num);
      return true;
    }
    catch (NumberFormatException e)
    {
      e.printStackTrace();
    }
    return false;
  }
  
  public static boolean isDouble(String num)
  {
    try
    {
      Double.parseDouble(num);
      return true;
    }
    catch (NumberFormatException e)
    {
      e.printStackTrace();
    }
    return false;
  }
  
  public static boolean isFloat(String num)
  {
    try
    {
      Float.parseFloat(num);
      return true;
    }
    catch (NumberFormatException e)
    {
      e.printStackTrace();
    }
    return false;
  }
  
  public static boolean isLong(String num)
  {
    try
    {
      Long.parseLong(num);
      return true;
    }
    catch (NumberFormatException e)
    {
      e.printStackTrace();
    }
    return false;
  }
  
  public static Random getRng()
  {
    return rng;
  }
  
  public static int getMid(int x1, int x2)
  {
    return (x1 + x2) / 2;
  }
public static final Random random = new Random();
  
  public static double getDistanceBetweenAngles(float angle1, float angle2)
  {
    float d = Math.abs(angle1 - angle2) % 360.0F;
    if (d > 180.0F) {
      d = 360.0F - d;
    }
    return d;
  }
  
  public static double round(double value, int places)
  {
    if (places < 0) {
      throw new IllegalArgumentException();
    }
    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }
  
  public static float getRandom()
  {
    return rng.nextFloat();
  }
  
  public static int getRandom(int cap)
  {
    return rng.nextInt(cap);
  }
  
  public static int getRandom(int floor, int cap)
  {
    return floor + rng.nextInt(cap - floor + 1);
  }
  
  public static int randInt(int min, int max)
  {
    return new Random().nextInt(max - min) + min;
  }
  
  public static double clampValue(double value, double floor, double cap)
  {
    if (value < floor) {
      return floor;
    }
    if (value > cap) {
      return cap;
    }
    return value;
  }
  
  public static float getAngleDifference(float direction, float rotationYaw)
  {
    float phi = Math.abs(rotationYaw - direction) % 360.0F;
    float distance = phi > 180.0F ? 360.0F - phi : phi;
    return distance;
  }
}
