package me.rhys.base.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;

public class MathUtil {
  public static Random random;
  
  public static double round(double value, int places) {
    if (places < 0)
      throw new IllegalArgumentException(); 
    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }
  
  public static double preciseRound(double value, int precision) {
    int scale = (int)Math.pow(10.0D, precision);
    return Math.round(value * scale) / scale;
  }
  
  public static double randOddDouble(double min, double max) {
    if (min % 2.0D == 0.0D)
      min++; 
    return min + 2.0D * ThreadLocalRandom.current().nextDouble((max - min) / 2.0D + 1.0D);
  }
  
  public static int randomNumber(int max, int min) {
    return Math.round(min + (float)Math.random() * (max - min));
  }
  
  public static double setRandom(double min, double max) {
    Random random = new Random();
    return min + random.nextDouble() * (max - min);
  }
  
  public static float trimFloat(int degree, float d) {
    String format = "#.#";
    for (int i = 1; i < degree; i++)
      format = String.valueOf(format) + "#"; 
    DecimalFormat twoDForm = new DecimalFormat(format);
    return Float.parseFloat(twoDForm.format(d).replaceAll(",", "."));
  }
  
  public static float setRandom(float min, float max) {
    Random random = new Random();
    return min + random.nextFloat() * (max - min);
  }
  
  public static double round(double value, int places, RoundingMode mode) {
    if (places < 0)
      throw new IllegalArgumentException(); 
    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, mode);
    return bd.doubleValue();
  }
  
  public static float round(float value, float value2, boolean random) {
    if (random) {
      if (ThreadLocalRandom.current().nextBoolean()) {
        value -= value % value2;
      } else {
        value += value % value2;
      } 
    } else {
      value -= value % value2;
    } 
    return value;
  }
  
  public static double round(double value) {
    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(0, RoundingMode.UP);
    return bd.doubleValue();
  }
  
  public static int floor(double var0) {
    int var2 = (int)var0;
    return (var0 < var2) ? (var2 - 1) : var2;
  }
  
  public static int getJumpEffect() {
    return (Minecraft.getMinecraft()).thePlayer.isPotionActive(Potion.jump) ? ((Minecraft.getMinecraft()).thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) : 0;
  }
  
  public static float yawTo180F(float flub) {
    if ((flub %= 360.0F) >= 180.0F)
      flub -= 360.0F; 
    if (flub < -180.0F)
      flub += 360.0F; 
    return flub;
  }
  
  public static double yawTo180D(double dub) {
    if ((dub %= 360.0D) >= 180.0D)
      dub -= 360.0D; 
    if (dub < -180.0D)
      dub += 360.0D; 
    return dub;
  }
  
  public static float randFloat(float min, float max) {
    Random rand = new Random();
    return rand.nextFloat() * (max - min) + min;
  }
  
  public static double randDouble(double min, double max) {
    Random random = new Random();
    return random.nextDouble() * (max - min) + min;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\util\MathUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */