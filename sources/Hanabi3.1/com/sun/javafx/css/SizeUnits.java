package com.sun.javafx.css;

import javafx.scene.text.Font;

public enum SizeUnits {
   PERCENT(false) {
      public String toString() {
         return "%";
      }

      public double points(double var1, double var3, Font var5) {
         return var1 / 100.0 * var3;
      }

      public double pixels(double var1, double var3, Font var5) {
         return var1 / 100.0 * var3;
      }
   },
   IN(true) {
      public String toString() {
         return "in";
      }

      public double points(double var1, double var3, Font var5) {
         return var1 * 72.0;
      }

      public double pixels(double var1, double var3, Font var5) {
         return var1 * 96.0;
      }
   },
   CM(true) {
      public String toString() {
         return "cm";
      }

      public double points(double var1, double var3, Font var5) {
         return var1 / 2.54 * 72.0;
      }

      public double pixels(double var1, double var3, Font var5) {
         return var1 / 2.54 * 96.0;
      }
   },
   MM(true) {
      public String toString() {
         return "mm";
      }

      public double points(double var1, double var3, Font var5) {
         return var1 / 25.4 * 72.0;
      }

      public double pixels(double var1, double var3, Font var5) {
         return var1 / 25.4 * 96.0;
      }
   },
   EM(false) {
      public String toString() {
         return "em";
      }

      public double points(double var1, double var3, Font var5) {
         return SizeUnits.round(var1 * SizeUnits.pointSize(var5));
      }

      public double pixels(double var1, double var3, Font var5) {
         return SizeUnits.round(var1 * SizeUnits.pixelSize(var5));
      }
   },
   EX(false) {
      public String toString() {
         return "ex";
      }

      public double points(double var1, double var3, Font var5) {
         return SizeUnits.round(var1 / 2.0 * SizeUnits.pointSize(var5));
      }

      public double pixels(double var1, double var3, Font var5) {
         return SizeUnits.round(var1 / 2.0 * SizeUnits.pixelSize(var5));
      }
   },
   PT(true) {
      public String toString() {
         return "pt";
      }

      public double points(double var1, double var3, Font var5) {
         return var1;
      }

      public double pixels(double var1, double var3, Font var5) {
         return var1 * 1.3333333333333333;
      }
   },
   PC(true) {
      public String toString() {
         return "pc";
      }

      public double points(double var1, double var3, Font var5) {
         return var1 * 12.0;
      }

      public double pixels(double var1, double var3, Font var5) {
         return var1 * 12.0 * 1.3333333333333333;
      }
   },
   PX(true) {
      public String toString() {
         return "px";
      }

      public double points(double var1, double var3, Font var5) {
         return var1 * 0.75;
      }

      public double pixels(double var1, double var3, Font var5) {
         return var1;
      }
   },
   DEG(true) {
      public String toString() {
         return "deg";
      }

      public double points(double var1, double var3, Font var5) {
         return SizeUnits.round(var1);
      }

      public double pixels(double var1, double var3, Font var5) {
         return SizeUnits.round(var1);
      }
   },
   GRAD(true) {
      public String toString() {
         return "grad";
      }

      public double points(double var1, double var3, Font var5) {
         return SizeUnits.round(var1 * 9.0 / 10.0);
      }

      public double pixels(double var1, double var3, Font var5) {
         return SizeUnits.round(var1 * 9.0 / 10.0);
      }
   },
   RAD(true) {
      public String toString() {
         return "rad";
      }

      public double points(double var1, double var3, Font var5) {
         return SizeUnits.round(var1 * 180.0 / Math.PI);
      }

      public double pixels(double var1, double var3, Font var5) {
         return SizeUnits.round(var1 * 180.0 / Math.PI);
      }
   },
   TURN(true) {
      public String toString() {
         return "turn";
      }

      public double points(double var1, double var3, Font var5) {
         return SizeUnits.round(var1 * 360.0);
      }

      public double pixels(double var1, double var3, Font var5) {
         return SizeUnits.round(var1 * 360.0);
      }
   },
   S(true) {
      public String toString() {
         return "s";
      }

      public double points(double var1, double var3, Font var5) {
         return var1;
      }

      public double pixels(double var1, double var3, Font var5) {
         return var1;
      }
   },
   MS(true) {
      public String toString() {
         return "ms";
      }

      public double points(double var1, double var3, Font var5) {
         return var1;
      }

      public double pixels(double var1, double var3, Font var5) {
         return var1;
      }
   };

   private final boolean absolute;
   private static final double DOTS_PER_INCH = 96.0;
   private static final double POINTS_PER_INCH = 72.0;
   private static final double CM_PER_INCH = 2.54;
   private static final double MM_PER_INCH = 25.4;
   private static final double POINTS_PER_PICA = 12.0;

   abstract double points(double var1, double var3, Font var5);

   abstract double pixels(double var1, double var3, Font var5);

   private SizeUnits(boolean var3) {
      this.absolute = var3;
   }

   public boolean isAbsolute() {
      return this.absolute;
   }

   private static double pointSize(Font var0) {
      return pixelSize(var0) * 0.75;
   }

   private static double pixelSize(Font var0) {
      return var0 != null ? var0.getSize() : Font.getDefault().getSize();
   }

   private static double round(double var0) {
      if (var0 == 0.0) {
         return var0;
      } else {
         double var2 = var0 < 0.0 ? -0.05 : 0.05;
         return (double)((long)((var0 + var2) * 10.0)) / 10.0;
      }
   }

   // $FF: synthetic method
   SizeUnits(boolean var3, Object var4) {
      this(var3);
   }
}
