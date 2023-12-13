package team.massacre.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.Random;

public class MathUtil {
   public static double roundToPlace(double value, int places) {
      if (places < 0) {
         throw new IllegalArgumentException();
      } else {
         BigDecimal bd = new BigDecimal(value);
         bd = bd.setScale(places, RoundingMode.HALF_UP);
         return bd.doubleValue();
      }
   }

   public static double distance(double srcX, double srcY, double srcZ, double dstX, double dstY, double dstZ) {
      double xDist = dstX - srcX;
      double yDist = dstY - srcY;
      double zDist = dstZ - srcZ;
      return Math.sqrt(xDist * xDist + yDist * yDist + zDist * zDist);
   }

   public static double distance(double srcX, double srcZ, double dstX, double dstZ) {
      double xDist = dstX - srcX;
      double zDist = dstZ - srcZ;
      return Math.sqrt(xDist * xDist + zDist * zDist);
   }

   public static double round(double value, double inc) {
      double halfOfInc = inc / 2.0D;
      double floored = StrictMath.floor(value / inc) * inc;
      return value >= floored + halfOfInc ? (new BigDecimal(StrictMath.ceil(value / inc) * inc, MathContext.DECIMAL64)).stripTrailingZeros().doubleValue() : (new BigDecimal(floored, MathContext.DECIMAL64)).stripTrailingZeros().doubleValue();
   }

   public static double round(double value, int places) {
      if (places < 0) {
         throw new IllegalArgumentException();
      } else {
         BigDecimal bd2 = new BigDecimal(value);
         bd2 = bd2.setScale(places, RoundingMode.HALF_UP);
         return bd2.doubleValue();
      }
   }

   public static double getRandomInRange(double min, double max) {
      Random random = new Random();
      double range = max - min;
      double scaled = random.nextDouble() * range;
      double shifted = scaled + min;
      return shifted;
   }

   public static float secRanFloat(float min, float max) {
      SecureRandom rand = new SecureRandom();
      return rand.nextFloat() * (max - min) + min;
   }

   public static double randomNumber(double max, double min) {
      return Math.random() * (max - min) + min;
   }

   public static float randomFloatValue() {
      return (float)getRandomInRange(2.96219E-7D, 9.13303E-6D);
   }
}
