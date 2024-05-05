package space.lunaclient.luna.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import net.minecraft.util.MathHelper;

public class MathUtils
{
  private static final Random rng = new Random();
  
  public MathUtils() {}
  
  public static double roundToPlace(double value, int places)
  {
    if (places < 0) {
      throw new IllegalArgumentException();
    }
    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }
  
  public static float wrapDegrees(float value)
  {
    return MathHelper.wrapAngleTo180_float(value);
  }
  
  public static int getRandom(int max)
  {
    return rng.nextInt(max);
  }
  
  public static int getRandom(int min, int max)
  {
    return min + rng.nextInt(min - max + 1);
  }
}
