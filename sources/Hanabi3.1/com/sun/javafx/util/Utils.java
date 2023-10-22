package com.sun.javafx.util;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.stage.StageHelper;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Utils {
   // $FF: synthetic field
   static final boolean $assertionsDisabled = !Utils.class.desiredAssertionStatus();

   public static float clamp(float var0, float var1, float var2) {
      if (var1 < var0) {
         return var0;
      } else {
         return var1 > var2 ? var2 : var1;
      }
   }

   public static int clamp(int var0, int var1, int var2) {
      if (var1 < var0) {
         return var0;
      } else {
         return var1 > var2 ? var2 : var1;
      }
   }

   public static double clamp(double var0, double var2, double var4) {
      if (var2 < var0) {
         return var0;
      } else {
         return var2 > var4 ? var4 : var2;
      }
   }

   public static double clampMin(double var0, double var2) {
      return var0 < var2 ? var2 : var0;
   }

   public static int clampMax(int var0, int var1) {
      return var0 > var1 ? var1 : var0;
   }

   public static double nearest(double var0, double var2, double var4) {
      double var6 = var2 - var0;
      double var8 = var4 - var2;
      return var6 < var8 ? var0 : var4;
   }

   public static String stripQuotes(String var0) {
      if (var0 == null) {
         return var0;
      } else if (var0.length() == 0) {
         return var0;
      } else {
         int var1 = 0;
         char var2 = var0.charAt(var1);
         if (var2 == '"' || var2 == '\'') {
            ++var1;
         }

         int var3 = var0.length();
         char var4 = var0.charAt(var3 - 1);
         if (var4 == '"' || var4 == '\'') {
            --var3;
         }

         return var3 - var1 < 0 ? var0 : var0.substring(var1, var3);
      }
   }

   public static String[] split(String var0, String var1) {
      if (var0 != null && var0.length() != 0) {
         if (var1 != null && var1.length() != 0) {
            if (var1.length() > var0.length()) {
               return new String[0];
            } else {
               ArrayList var2 = new ArrayList();

               for(int var3 = var0.indexOf(var1); var3 >= 0; var3 = var0.indexOf(var1)) {
                  String var4 = var0.substring(0, var3);
                  if (var4 != null && var4.length() > 0) {
                     var2.add(var4);
                  }

                  var0 = var0.substring(var3 + var1.length());
               }

               if (var0 != null && var0.length() > 0) {
                  var2.add(var0);
               }

               return (String[])var2.toArray(new String[0]);
            }
         } else {
            return new String[0];
         }
      } else {
         return new String[0];
      }
   }

   public static boolean contains(String var0, String var1) {
      if (var0 != null && var0.length() != 0) {
         if (var1 != null && var1.length() != 0) {
            if (var1.length() > var0.length()) {
               return false;
            } else {
               return var0.indexOf(var1) > -1;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static double calculateBrightness(Color var0) {
      return 0.3 * var0.getRed() + 0.59 * var0.getGreen() + 0.11 * var0.getBlue();
   }

   public static Color deriveColor(Color var0, double var1) {
      double var3 = calculateBrightness(var0);
      double var5 = var1;
      if (var1 > 0.0) {
         if (var3 > 0.85) {
            var5 = var1 * 1.6;
         } else if (!(var3 > 0.6)) {
            if (var3 > 0.5) {
               var5 = var1 * 0.9;
            } else if (var3 > 0.4) {
               var5 = var1 * 0.8;
            } else if (var3 > 0.3) {
               var5 = var1 * 0.7;
            } else {
               var5 = var1 * 0.6;
            }
         }
      } else if (var3 < 0.2) {
         var5 = var1 * 0.6;
      }

      if (var5 < -1.0) {
         var5 = -1.0;
      } else if (var5 > 1.0) {
         var5 = 1.0;
      }

      double[] var7 = RGBtoHSB(var0.getRed(), var0.getGreen(), var0.getBlue());
      if (var5 > 0.0) {
         var7[1] *= 1.0 - var5;
         var7[2] += (1.0 - var7[2]) * var5;
      } else {
         var7[2] *= var5 + 1.0;
      }

      if (var7[1] < 0.0) {
         var7[1] = 0.0;
      } else if (var7[1] > 1.0) {
         var7[1] = 1.0;
      }

      if (var7[2] < 0.0) {
         var7[2] = 0.0;
      } else if (var7[2] > 1.0) {
         var7[2] = 1.0;
      }

      Color var8 = Color.hsb((double)((int)var7[0]), var7[1], var7[2], var0.getOpacity());
      return Color.hsb((double)((int)var7[0]), var7[1], var7[2], var0.getOpacity());
   }

   private static Color interpolateLinear(double var0, Color var2, Color var3) {
      Color var4 = convertSRGBtoLinearRGB(var2);
      Color var5 = convertSRGBtoLinearRGB(var3);
      return convertLinearRGBtoSRGB(Color.color(var4.getRed() + (var5.getRed() - var4.getRed()) * var0, var4.getGreen() + (var5.getGreen() - var4.getGreen()) * var0, var4.getBlue() + (var5.getBlue() - var4.getBlue()) * var0, var4.getOpacity() + (var5.getOpacity() - var4.getOpacity()) * var0));
   }

   private static Color ladder(double var0, Stop[] var2) {
      Stop var3 = null;

      for(int var4 = 0; var4 < var2.length; ++var4) {
         Stop var5 = var2[var4];
         if (var0 <= var5.getOffset()) {
            if (var3 == null) {
               return var5.getColor();
            }

            return interpolateLinear((var0 - var3.getOffset()) / (var5.getOffset() - var3.getOffset()), var3.getColor(), var5.getColor());
         }

         var3 = var5;
      }

      return var3.getColor();
   }

   public static Color ladder(Color var0, Stop[] var1) {
      return ladder(calculateBrightness(var0), var1);
   }

   public static double[] HSBtoRGB(double var0, double var2, double var4) {
      double var6 = (var0 % 360.0 + 360.0) % 360.0;
      var0 = var6 / 360.0;
      double var8 = 0.0;
      double var10 = 0.0;
      double var12 = 0.0;
      if (var2 == 0.0) {
         var12 = var4;
         var10 = var4;
         var8 = var4;
      } else {
         double var14 = (var0 - Math.floor(var0)) * 6.0;
         double var16 = var14 - Math.floor(var14);
         double var18 = var4 * (1.0 - var2);
         double var20 = var4 * (1.0 - var2 * var16);
         double var22 = var4 * (1.0 - var2 * (1.0 - var16));
         switch ((int)var14) {
            case 0:
               var8 = var4;
               var10 = var22;
               var12 = var18;
               break;
            case 1:
               var8 = var20;
               var10 = var4;
               var12 = var18;
               break;
            case 2:
               var8 = var18;
               var10 = var4;
               var12 = var22;
               break;
            case 3:
               var8 = var18;
               var10 = var20;
               var12 = var4;
               break;
            case 4:
               var8 = var22;
               var10 = var18;
               var12 = var4;
               break;
            case 5:
               var8 = var4;
               var10 = var18;
               var12 = var20;
         }
      }

      double[] var24 = new double[]{var8, var10, var12};
      return var24;
   }

   public static double[] RGBtoHSB(double var0, double var2, double var4) {
      double[] var12 = new double[3];
      double var13 = var0 > var2 ? var0 : var2;
      if (var4 > var13) {
         var13 = var4;
      }

      double var15 = var0 < var2 ? var0 : var2;
      if (var4 < var15) {
         var15 = var4;
      }

      double var8;
      if (var13 != 0.0) {
         var8 = (var13 - var15) / var13;
      } else {
         var8 = 0.0;
      }

      double var6;
      if (var8 == 0.0) {
         var6 = 0.0;
      } else {
         double var17 = (var13 - var0) / (var13 - var15);
         double var19 = (var13 - var2) / (var13 - var15);
         double var21 = (var13 - var4) / (var13 - var15);
         if (var0 == var13) {
            var6 = var21 - var19;
         } else if (var2 == var13) {
            var6 = 2.0 + var17 - var21;
         } else {
            var6 = 4.0 + var19 - var17;
         }

         var6 /= 6.0;
         if (var6 < 0.0) {
            ++var6;
         }
      }

      var12[0] = var6 * 360.0;
      var12[1] = var8;
      var12[2] = var13;
      return var12;
   }

   public static Color convertSRGBtoLinearRGB(Color var0) {
      double[] var1 = new double[]{var0.getRed(), var0.getGreen(), var0.getBlue()};

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2] <= 0.04045) {
            var1[var2] /= 12.92;
         } else {
            var1[var2] = Math.pow((var1[var2] + 0.055) / 1.055, 2.4);
         }
      }

      return Color.color(var1[0], var1[1], var1[2], var0.getOpacity());
   }

   public static Color convertLinearRGBtoSRGB(Color var0) {
      double[] var1 = new double[]{var0.getRed(), var0.getGreen(), var0.getBlue()};

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2] <= 0.0031308) {
            var1[var2] *= 12.92;
         } else {
            var1[var2] = 1.055 * Math.pow(var1[var2], 0.4166666666666667) - 0.055;
         }
      }

      return Color.color(var1[0], var1[1], var1[2], var0.getOpacity());
   }

   public static double sum(double[] var0) {
      double var1 = 0.0;
      double[] var3 = var0;
      int var4 = var0.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         double var6 = var3[var5];
         var1 += var6;
      }

      return var1 / (double)var0.length;
   }

   public static Point2D pointRelativeTo(Node var0, Node var1, HPos var2, VPos var3, double var4, double var6, boolean var8) {
      double var9 = var1.getLayoutBounds().getWidth();
      double var11 = var1.getLayoutBounds().getHeight();
      return pointRelativeTo(var0, var9, var11, var2, var3, var4, var6, var8);
   }

   public static Point2D pointRelativeTo(Node var0, double var1, double var3, HPos var5, VPos var6, double var7, double var9, boolean var11) {
      Bounds var12 = getBounds(var0);
      Scene var13 = var0.getScene();
      NodeOrientation var14 = var0.getEffectiveNodeOrientation();
      if (var14 == NodeOrientation.RIGHT_TO_LEFT) {
         if (var5 == HPos.LEFT) {
            var5 = HPos.RIGHT;
         } else if (var5 == HPos.RIGHT) {
            var5 = HPos.LEFT;
         }

         var7 *= -1.0;
      }

      double var15 = positionX(var12, var1, var5) + var7;
      double var17 = positionY(var12, var3, var6) + var9;
      if (var14 == NodeOrientation.RIGHT_TO_LEFT && var5 == HPos.CENTER) {
         if (var13.getWindow() instanceof Stage) {
            var15 = var15 + var12.getWidth() - var1;
         } else {
            var15 = var15 - var12.getWidth() - var1;
         }
      }

      return var11 ? pointRelativeTo(var0, var1, var3, var15, var17, var5, var6) : new Point2D(var15, var17);
   }

   public static Point2D pointRelativeTo(Object var0, double var1, double var3, double var5, double var7, HPos var9, VPos var10) {
      double var11 = var5;
      double var13 = var7;
      Bounds var15 = getBounds(var0);
      Screen var16 = getScreen(var0);
      Rectangle2D var17 = hasFullScreenStage(var16) ? var16.getBounds() : var16.getVisualBounds();
      if (var9 != null) {
         if (var5 + var1 > var17.getMaxX()) {
            var11 = positionX(var15, var1, getHPosOpposite(var9, var10));
         }

         if (var11 < var17.getMinX()) {
            var11 = positionX(var15, var1, getHPosOpposite(var9, var10));
         }
      }

      if (var10 != null) {
         if (var7 + var3 > var17.getMaxY()) {
            var13 = positionY(var15, var3, getVPosOpposite(var9, var10));
         }

         if (var13 < var17.getMinY()) {
            var13 = positionY(var15, var3, getVPosOpposite(var9, var10));
         }
      }

      if (var11 + var1 > var17.getMaxX()) {
         var11 -= var11 + var1 - var17.getMaxX();
      }

      if (var11 < var17.getMinX()) {
         var11 = var17.getMinX();
      }

      if (var13 + var3 > var17.getMaxY()) {
         var13 -= var13 + var3 - var17.getMaxY();
      }

      if (var13 < var17.getMinY()) {
         var13 = var17.getMinY();
      }

      return new Point2D(var11, var13);
   }

   private static double positionX(Bounds var0, double var1, HPos var3) {
      if (var3 == HPos.CENTER) {
         return var0.getMinX();
      } else if (var3 == HPos.RIGHT) {
         return var0.getMaxX();
      } else {
         return var3 == HPos.LEFT ? var0.getMinX() - var1 : 0.0;
      }
   }

   private static double positionY(Bounds var0, double var1, VPos var3) {
      if (var3 == VPos.BOTTOM) {
         return var0.getMaxY();
      } else if (var3 == VPos.CENTER) {
         return var0.getMinY();
      } else {
         return var3 == VPos.TOP ? var0.getMinY() - var1 : 0.0;
      }
   }

   private static Bounds getBounds(Object var0) {
      if (var0 instanceof Node) {
         Node var3 = (Node)var0;
         Bounds var2 = var3.localToScreen(var3.getLayoutBounds());
         return (Bounds)(var2 != null ? var2 : new BoundingBox(0.0, 0.0, 0.0, 0.0));
      } else if (var0 instanceof Window) {
         Window var1 = (Window)var0;
         return new BoundingBox(var1.getX(), var1.getY(), var1.getWidth(), var1.getHeight());
      } else {
         return new BoundingBox(0.0, 0.0, 0.0, 0.0);
      }
   }

   private static HPos getHPosOpposite(HPos var0, VPos var1) {
      if (var1 == VPos.CENTER) {
         if (var0 == HPos.LEFT) {
            return HPos.RIGHT;
         } else if (var0 == HPos.RIGHT) {
            return HPos.LEFT;
         } else {
            return var0 == HPos.CENTER ? HPos.CENTER : HPos.CENTER;
         }
      } else {
         return HPos.CENTER;
      }
   }

   private static VPos getVPosOpposite(HPos var0, VPos var1) {
      if (var0 == HPos.CENTER) {
         if (var1 == VPos.BASELINE) {
            return VPos.BASELINE;
         } else if (var1 == VPos.BOTTOM) {
            return VPos.TOP;
         } else if (var1 == VPos.CENTER) {
            return VPos.CENTER;
         } else {
            return var1 == VPos.TOP ? VPos.BOTTOM : VPos.CENTER;
         }
      } else {
         return VPos.CENTER;
      }
   }

   public static boolean hasFullScreenStage(Screen var0) {
      ObservableList var1 = StageHelper.getStages();
      Iterator var2 = var1.iterator();

      Stage var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (Stage)var2.next();
      } while(!var3.isFullScreen() || getScreen(var3) != var0);

      return true;
   }

   public static boolean isQVGAScreen() {
      Rectangle2D var0 = Screen.getPrimary().getBounds();
      return var0.getWidth() == 320.0 && var0.getHeight() == 240.0 || var0.getWidth() == 240.0 && var0.getHeight() == 320.0;
   }

   public static Screen getScreen(Object var0) {
      Bounds var1 = getBounds(var0);
      Rectangle2D var2 = new Rectangle2D(var1.getMinX(), var1.getMinY(), var1.getWidth(), var1.getHeight());
      return getScreenForRectangle(var2);
   }

   public static Screen getScreenForRectangle(Rectangle2D var0) {
      ObservableList var1 = Screen.getScreens();
      double var2 = var0.getMinX();
      double var4 = var0.getMaxX();
      double var6 = var0.getMinY();
      double var8 = var0.getMaxY();
      Screen var10 = null;
      double var11 = 0.0;
      Iterator var13 = var1.iterator();

      while(var13.hasNext()) {
         Screen var14 = (Screen)var13.next();
         Rectangle2D var15 = var14.getBounds();
         double var16 = getIntersectionLength(var2, var4, var15.getMinX(), var15.getMaxX()) * getIntersectionLength(var6, var8, var15.getMinY(), var15.getMaxY());
         if (var11 < var16) {
            var11 = var16;
            var10 = var14;
         }
      }

      if (var10 != null) {
         return var10;
      } else {
         var10 = Screen.getPrimary();
         double var24 = Double.MAX_VALUE;
         Iterator var25 = var1.iterator();

         while(var25.hasNext()) {
            Screen var26 = (Screen)var25.next();
            Rectangle2D var17 = var26.getBounds();
            double var18 = getOuterDistance(var2, var4, var17.getMinX(), var17.getMaxX());
            double var20 = getOuterDistance(var6, var8, var17.getMinY(), var17.getMaxY());
            double var22 = var18 * var18 + var20 * var20;
            if (var24 > var22) {
               var24 = var22;
               var10 = var26;
            }
         }

         return var10;
      }
   }

   public static Screen getScreenForPoint(double var0, double var2) {
      ObservableList var4 = Screen.getScreens();
      Iterator var5 = var4.iterator();

      Screen var6;
      Rectangle2D var7;
      do {
         if (!var5.hasNext()) {
            Screen var17 = Screen.getPrimary();
            double var18 = Double.MAX_VALUE;
            Iterator var8 = var4.iterator();

            while(var8.hasNext()) {
               Screen var9 = (Screen)var8.next();
               Rectangle2D var10 = var9.getBounds();
               double var11 = getOuterDistance(var10.getMinX(), var10.getMaxX(), var0);
               double var13 = getOuterDistance(var10.getMinY(), var10.getMaxY(), var2);
               double var15 = var11 * var11 + var13 * var13;
               if (var18 >= var15) {
                  var18 = var15;
                  var17 = var9;
               }
            }

            return var17;
         }

         var6 = (Screen)var5.next();
         var7 = var6.getBounds();
      } while(!(var0 >= var7.getMinX()) || !(var0 < var7.getMaxX()) || !(var2 >= var7.getMinY()) || !(var2 < var7.getMaxY()));

      return var6;
   }

   private static double getIntersectionLength(double var0, double var2, double var4, double var6) {
      return var0 <= var4 ? getIntersectionLengthImpl(var4, var6, var2) : getIntersectionLengthImpl(var0, var2, var6);
   }

   private static double getIntersectionLengthImpl(double var0, double var2, double var4) {
      if (var4 <= var0) {
         return 0.0;
      } else {
         return var4 <= var2 ? var4 - var0 : var2 - var0;
      }
   }

   private static double getOuterDistance(double var0, double var2, double var4, double var6) {
      if (var2 <= var4) {
         return var4 - var2;
      } else {
         return var6 <= var0 ? var6 - var0 : 0.0;
      }
   }

   private static double getOuterDistance(double var0, double var2, double var4) {
      if (var4 <= var0) {
         return var0 - var4;
      } else {
         return var4 >= var2 ? var4 - var2 : 0.0;
      }
   }

   public static boolean assertionEnabled() {
      boolean var0 = false;
      if (!$assertionsDisabled) {
         var0 = true;
         if (false) {
            throw new AssertionError();
         }
      }

      return var0;
   }

   public static boolean isWindows() {
      return PlatformUtil.isWindows();
   }

   public static boolean isMac() {
      return PlatformUtil.isMac();
   }

   public static boolean isUnix() {
      return PlatformUtil.isUnix();
   }

   public static String convertUnicode(String var0) {
      int var5 = -1;
      char[] var1 = var0.toCharArray();
      int var3 = var1.length;
      int var2 = -1;
      char[] var6 = new char[var3];

      char var4;
      int var7;
      for(var7 = 0; var2 < var3 - 1; var6[var7++] = var4) {
         ++var2;
         var4 = var1[var2];
         if (var4 == '\\' && var5 != var2) {
            ++var2;
            var4 = var1[var2];
            if (var4 != 'u') {
               --var2;
               var4 = '\\';
            } else {
               do {
                  ++var2;
                  var4 = var1[var2];
               } while(var4 == 'u');

               int var8 = var2 + 3;
               if (var8 < var3) {
                  int var10 = Character.digit(var4, 16);
                  if (var10 >= 0 && var4 > 127) {
                     var4 = "0123456789abcdef".charAt(var10);
                  }

                  int var11 = var10;

                  int var12;
                  int var14;
                  for(var12 = var10; var2 < var8 && var11 >= 0; var12 = (var12 << 4) + var14) {
                     ++var2;
                     var4 = var1[var2];
                     var14 = Character.digit(var4, 16);
                     if (var14 >= 0 && var4 > 127) {
                        var4 = "0123456789abcdef".charAt(var14);
                     }

                     var11 = var14;
                  }

                  if (var11 >= 0) {
                     var4 = (char)var12;
                     var5 = var2;
                  }
               }
            }
         }
      }

      return new String(var6, 0, var7);
   }
}
