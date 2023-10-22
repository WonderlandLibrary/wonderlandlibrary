package cn.hanabi.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MathUtils {
   private static final Random rng = new Random();
   private static final Random random = new Random();

   public static double clamp(double value, double minimum, double maximum) {
      return value > maximum ? maximum : Math.max(value, minimum);
   }

   public static int random(int min, int max) {
      int range = max - min;
      return min + random.nextInt(range + 1);
   }

   public static float map(float x, float prev_min, float prev_max, float new_min, float new_max) {
      return (x - prev_min) / (prev_max - prev_min) * (new_max - new_min) + new_min;
   }

   public static boolean contains(float x, float y, float minX, float minY, float maxX, float maxY) {
      return x > minX && x < maxX && y > minY && y < maxY;
   }

   public static double round(double value, int places) {
      if (places < 0) {
         throw new IllegalArgumentException();
      } else {
         BigDecimal bd = new BigDecimal(value);
         bd = bd.setScale(places, RoundingMode.HALF_UP);
         return bd.doubleValue();
      }
   }

   public static double round(double num, double increment) {
      if (increment < 0.0) {
         throw new IllegalArgumentException();
      } else {
         BigDecimal bd = new BigDecimal(num);
         bd = bd.setScale((int)increment, RoundingMode.HALF_UP);
         return bd.doubleValue();
      }
   }

   public static float getRandom() {
      return rng.nextFloat();
   }

   public static int getRandom(int min, int max) {
      return max < min ? min : min + random.nextInt(max - min + 1);
   }

   public static double getRandom(double min, double max) {
      double range = max - min;
      double scaled = random.nextDouble() * range;
      if (scaled > max) {
         scaled = max;
      }

      double shifted = scaled + min;
      if (shifted > max) {
         shifted = max;
      }

      return shifted;
   }

   public static int randInt(int min, int max) {
      return (new Random()).nextInt(max - min + 1) + min;
   }

   public static float clampValue(float value, float floor, float cap) {
      return value < floor ? floor : Math.min(value, cap);
   }

   public static float getSimilarity(String string1, String string2) {
      int halflen = Math.min(string1.length(), string2.length()) / 2 + Math.min(string1.length(), string2.length()) % 2;
      StringBuffer common1 = getCommonCharacters(string1, string2, halflen);
      StringBuffer common2 = getCommonCharacters(string2, string1, halflen);
      if (common1.length() != 0 && common2.length() != 0) {
         if (common1.length() != common2.length()) {
            return 0.0F;
         } else {
            int transpositions = 0;
            int n = common1.length();

            for(int i = 0; i < n; ++i) {
               if (common1.charAt(i) != common2.charAt(i)) {
                  ++transpositions;
               }
            }

            transpositions /= 2;
            return (float)(common1.length() / string1.length() + common2.length() / string2.length() + (common1.length() - transpositions) / common1.length()) / 3.0F;
         }
      } else {
         return 0.0F;
      }
   }

   private static StringBuffer getCommonCharacters(String string1, String string2, int distanceSep) {
      StringBuffer returnCommons = new StringBuffer();
      StringBuilder copy = new StringBuilder(string2);
      int n = string1.length();
      int m = string2.length();

      for(int i = 0; i < n; ++i) {
         char ch = string1.charAt(i);
         boolean foundIt = false;

         for(int j = Math.max(0, i - distanceSep); !foundIt && j < Math.min(i + distanceSep, m - 1); ++j) {
            if (copy.charAt(j) == ch) {
               foundIt = true;
               returnCommons.append(ch);
               copy.setCharAt(j, '\u0000');
            }
         }
      }

      return returnCommons;
   }

   public static double meme(double value, int places) {
      if (places < 0) {
         throw new IllegalArgumentException();
      } else {
         BigDecimal bd = new BigDecimal(value);
         bd = bd.setScale(places, RoundingMode.HALF_UP);
         return bd.doubleValue();
      }
   }

   public static int customRandInt(int min, int max) {
      return (new Random()).nextInt(max - min + 1) + min;
   }

   public static double roundToPlace(double value, int places) {
      if (places < 0) {
         throw new IllegalArgumentException();
      } else {
         BigDecimal bd = new BigDecimal(value);
         bd = bd.setScale(places, RoundingMode.HALF_UP);
         return bd.doubleValue();
      }
   }

   public static double getDistance(double source, double target) {
      double diff = source - target;
      return Math.sqrt(diff * diff);
   }

   public static double roundDouble(double value, int places) {
      if (places < 0) {
         throw new IllegalArgumentException();
      } else {
         BigDecimal bd = new BigDecimal(value);
         bd = bd.setScale(places, RoundingMode.HALF_UP);
         return bd.doubleValue();
      }
   }

   public static float[] constrainAngle(float[] vector) {
      vector[0] %= 360.0F;

      for(vector[1] %= 360.0F; vector[0] <= -180.0F; vector[0] += 360.0F) {
      }

      while(vector[1] <= -180.0F) {
         vector[1] += 360.0F;
      }

      while(vector[0] > 180.0F) {
         vector[0] -= 360.0F;
      }

      while(vector[1] > 180.0F) {
         vector[1] -= 360.0F;
      }

      return vector;
   }

   public final int randomInt(int min, int max) {
      return ThreadLocalRandom.current().nextInt(min, max);
   }

   public final long randomLong(long min, long max) {
      return ThreadLocalRandom.current().nextLong(min, max);
   }

   public final float randomFloat(float min, float max) {
      return (float)ThreadLocalRandom.current().nextDouble((double)min, (double)max);
   }

   public final double randomGaussian(double tolerance, double average, boolean multiplyGaussian) {
      return random.nextGaussian() * (multiplyGaussian ? random.nextGaussian() : 1.0) * tolerance + average;
   }

   public static Double[] lerp(Double[] a, Double[] b, double t) {
      return new Double[]{a[0] + (b[0] - a[0]) * t, a[1] + (b[1] - a[1]) * t};
   }

   public static double distanceSq(Double[] a, Double[] b) {
      return Math.pow(a[0] - b[0], 2.0) + Math.pow(a[1] - b[1], 2.0);
   }

   public static double distanceToSegmentSq(Double[] p, Double[] v, Double[] w) {
      double l2 = distanceSq(v, w);
      return l2 == 0.0 ? distanceSq(p, v) : distanceSq(p, lerp(v, w, GLUtil.glmClamp(((p[0] - v[0]) * (w[0] - v[0]) + (p[1] - v[1]) * (w[1] - v[1])) / l2, 0.0, 1.0)));
   }

   public static Double[] calcCurvePoint(Double[][] points, double t) {
      ArrayList cpoints = new ArrayList();

      for(int i = 0; i < points.length - 1; ++i) {
         cpoints.add(lerp(points[i], points[i + 1], t));
      }

      return cpoints.size() == 1 ? (Double[])cpoints.get(0) : calcCurvePoint((Double[][])cpoints.toArray(new Double[0][0]), t);
   }

   public static Double[][] getPointsOnCurve(Double[][] points, int num) {
      ArrayList cpoints = new ArrayList();

      for(int i = 0; i < num; ++i) {
         double t = (double)i / ((double)num - 1.0);
         cpoints.add(calcCurvePoint(points, t));
      }

      return (Double[][])cpoints.toArray(new Double[0][0]);
   }

   public static Double[][] simplifyPoints(Double[][] points, double epsilon) {
      return simplifyPoints(points, epsilon, 0, points.length, new ArrayList());
   }

   public static Double[][] simplifyPoints(Double[][] points, double epsilon, int start, int end, ArrayList outPoints) {
      Double[] s = points[start];
      Double[] e = points[end - 1];
      double maxDistSq = 0.0;
      int maxNdx = 1;

      for(int i = start + 1; i < end - 1; ++i) {
         double distSq = distanceToSegmentSq(points[i], s, e);
         if (distSq > maxDistSq) {
            maxDistSq = distSq;
            maxNdx = i;
         }
      }

      if (Math.sqrt(maxDistSq) > epsilon) {
         simplifyPoints(points, epsilon, start, maxNdx + 1, outPoints);
         simplifyPoints(points, epsilon, maxNdx, end, outPoints);
      } else {
         outPoints.add(s);
         outPoints.add(e);
      }

      return (Double[][])outPoints.toArray(new Double[0][0]);
   }
}
