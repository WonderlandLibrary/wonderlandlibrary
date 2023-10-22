package com.sun.javafx.scene.paint;

import java.util.LinkedList;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;

public class GradientUtils {
   public static String lengthToString(double var0, boolean var2) {
      return var2 ? var0 * 100.0 + "%" : var0 + "px";
   }

   public static class Parser {
      private int index;
      private String[] tokens;
      private boolean proportional;
      private boolean proportionalSet = false;

      private String[] splitString(String var1, Delimiter var2, boolean var3) {
         LinkedList var4 = new LinkedList();
         StringBuilder var5 = new StringBuilder();
         int var6 = 0;

         for(char[] var7 = var1.toCharArray(); var6 < var7.length; ++var6) {
            char var8 = var7[var6];
            if (!var2.isDelimiter(var8)) {
               if (var8 == '(') {
                  while(var6 < var7.length) {
                     var5.append(var7[var6]);
                     if (var7[var6] == ')') {
                        break;
                     }

                     ++var6;
                  }
               } else {
                  var5.append(var7[var6]);
               }
            } else {
               if (!var3 || var5.length() > 0) {
                  var4.add(var5.toString());
               }

               var5.setLength(0);
            }
         }

         if (!var3 || var5.length() > 0) {
            var4.add(var5.toString());
         }

         return (String[])var4.toArray(new String[var4.size()]);
      }

      public Parser(String var1) {
         this.tokens = this.splitString(var1, (var0) -> {
            return var0 == ',';
         }, false);
         this.index = 0;
      }

      public int getSize() {
         return this.tokens.length;
      }

      public void shift() {
         ++this.index;
      }

      public String getCurrentToken() {
         String var1 = this.tokens[this.index].trim();
         if (var1.isEmpty()) {
            throw new IllegalArgumentException("Invalid gradient specification: found empty token.");
         } else {
            return var1;
         }
      }

      public String[] splitCurrentToken() {
         return this.getCurrentToken().split("\\s");
      }

      public static void checkNumberOfArguments(String[] var0, int var1) {
         if (var0.length < var1 + 1) {
            throw new IllegalArgumentException("Invalid gradient specification: parameter '" + var0[0] + "' needs " + var1 + " argument(s).");
         }
      }

      public static double parseAngle(String var0) {
         double var1 = 0.0;
         if (var0.endsWith("deg")) {
            var0 = var0.substring(0, var0.length() - 3);
            var1 = Double.parseDouble(var0);
         } else if (var0.endsWith("grad")) {
            var0 = var0.substring(0, var0.length() - 4);
            var1 = Double.parseDouble(var0);
            var1 = var1 * 9.0 / 10.0;
         } else if (var0.endsWith("rad")) {
            var0 = var0.substring(0, var0.length() - 3);
            var1 = Double.parseDouble(var0);
            var1 = var1 * 180.0 / Math.PI;
         } else {
            if (!var0.endsWith("turn")) {
               throw new IllegalArgumentException("Invalid gradient specification:angle must end in deg, rad, grad, or turn");
            }

            var0 = var0.substring(0, var0.length() - 4);
            var1 = Double.parseDouble(var0);
            var1 *= 360.0;
         }

         return var1;
      }

      public static double parsePercentage(String var0) {
         if (var0.endsWith("%")) {
            var0 = var0.substring(0, var0.length() - 1);
            double var1 = Double.parseDouble(var0) / 100.0;
            return var1;
         } else {
            throw new IllegalArgumentException("Invalid gradient specification: focus-distance must be specified as percentage");
         }
      }

      public Point parsePoint(String var1) {
         Point var2 = new Point();
         if (var1.endsWith("%")) {
            var2.proportional = true;
            var1 = var1.substring(0, var1.length() - 1);
         } else if (var1.endsWith("px")) {
            var1 = var1.substring(0, var1.length() - 2);
         }

         var2.value = Double.parseDouble(var1);
         if (var2.proportional) {
            var2.value /= 100.0;
         }

         if (this.proportionalSet && this.proportional != var2.proportional) {
            throw new IllegalArgumentException("Invalid gradient specification:cannot mix proportional and non-proportional values");
         } else {
            this.proportionalSet = true;
            this.proportional = var2.proportional;
            return var2;
         }
      }

      public Stop[] parseStops(boolean var1, double var2) {
         int var4 = this.tokens.length - this.index;
         Color[] var5 = new Color[var4];
         double[] var6 = new double[var4];
         Stop[] var7 = new Stop[var4];

         double var12;
         for(int var8 = 0; var8 < var4; ++var8) {
            String var9 = this.tokens[var8 + this.index].trim();
            String[] var10 = this.splitString(var9, (var0) -> {
               return Character.isWhitespace(var0);
            }, true);
            if (var10.length == 0) {
               throw new IllegalArgumentException("Invalid gradient specification, empty stop found");
            }

            String var11 = var10[0];
            var12 = -1.0;
            Color var14 = Color.web(var11);
            if (var10.length == 2) {
               String var15 = var10[1];
               if (var15.endsWith("%")) {
                  var15 = var15.substring(0, var15.length() - 1);
                  var12 = Double.parseDouble(var15) / 100.0;
               } else {
                  if (var1) {
                     throw new IllegalArgumentException("Invalid gradient specification, non-proportional stops not permited in proportional gradient: " + var15);
                  }

                  if (var15.endsWith("px")) {
                     var15 = var15.substring(0, var15.length() - 2);
                  }

                  var12 = Double.parseDouble(var15) / var2;
               }
            } else if (var10.length > 2) {
               throw new IllegalArgumentException("Invalid gradient specification, unexpected content in stop specification: " + var10[2]);
            }

            var5[var8] = var14;
            var6[var8] = var12;
         }

         if (var6[0] < 0.0) {
            var6[0] = 0.0;
         }

         if (var6[var6.length - 1] < 0.0) {
            var6[var6.length - 1] = 1.0;
         }

         double var18 = var6[0];

         int var19;
         for(var19 = 1; var19 < var6.length; ++var19) {
            if (var6[var19] < var18 && var6[var19] > 0.0) {
               var6[var19] = var18;
            } else {
               var18 = var6[var19];
            }
         }

         var19 = -1;

         int var20;
         for(var20 = 1; var20 < var6.length; ++var20) {
            var12 = var6[var20];
            if (var12 < 0.0 && var19 < 0) {
               var19 = var20;
            } else if (var12 >= 0.0 && var19 > 0) {
               int var22 = var20 - var19 + 1;
               double var23 = (var6[var20] - var6[var19 - 1]) / (double)var22;

               for(int var17 = 0; var17 < var22 - 1; ++var17) {
                  var6[var19 + var17] = var6[var19 - 1] + var23 * (double)(var17 + 1);
               }
            }
         }

         for(var20 = 0; var20 < var7.length; ++var20) {
            Stop var21 = new Stop(var6[var20], var5[var20]);
            var7[var20] = var21;
         }

         return var7;
      }

      private interface Delimiter {
         boolean isDelimiter(char var1);
      }
   }

   public static class Point {
      public static final Point MIN = new Point(0.0, true);
      public static final Point MAX = new Point(1.0, true);
      public double value;
      public boolean proportional;

      public String toString() {
         return "value = " + this.value + ", proportional = " + this.proportional;
      }

      public Point(double var1, boolean var3) {
         this.value = var1;
         this.proportional = var3;
      }

      public Point() {
      }
   }
}
