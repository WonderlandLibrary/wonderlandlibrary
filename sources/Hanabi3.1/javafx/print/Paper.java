package javafx.print;

import com.sun.javafx.print.Units;

public final class Paper {
   private String name;
   private double width;
   private double height;
   private Units units;
   public static final Paper A0;
   public static final Paper A1;
   public static final Paper A2;
   public static final Paper A3;
   public static final Paper A4;
   public static final Paper A5;
   public static final Paper A6;
   public static final Paper DESIGNATED_LONG;
   public static final Paper NA_LETTER;
   public static final Paper LEGAL;
   public static final Paper TABLOID;
   public static final Paper EXECUTIVE;
   public static final Paper NA_8X10;
   public static final Paper MONARCH_ENVELOPE;
   public static final Paper NA_NUMBER_10_ENVELOPE;
   public static final Paper C;
   public static final Paper JIS_B4;
   public static final Paper JIS_B5;
   public static final Paper JIS_B6;
   public static final Paper JAPANESE_POSTCARD;

   Paper(String var1, double var2, double var4, Units var6) throws IllegalArgumentException {
      if (!(var2 <= 0.0) && !(var4 <= 0.0)) {
         if (var1 == null) {
            throw new IllegalArgumentException("Null name");
         } else {
            this.name = var1;
            this.width = var2;
            this.height = var4;
            this.units = var6;
         }
      } else {
         throw new IllegalArgumentException("Illegal dimension");
      }
   }

   public final String getName() {
      return this.name;
   }

   private double getSizeInPoints(double var1) {
      switch (this.units) {
         case POINT:
            return (double)((int)(var1 + 0.5));
         case INCH:
            return (double)((int)(var1 * 72.0 + 0.5));
         case MM:
            return (double)((int)(var1 * 72.0 / 25.4 + 0.5));
         default:
            return var1;
      }
   }

   public final double getWidth() {
      return this.getSizeInPoints(this.width);
   }

   public final double getHeight() {
      return this.getSizeInPoints(this.height);
   }

   public final int hashCode() {
      return (int)this.width + ((int)this.height << 16) + this.units.hashCode();
   }

   public final boolean equals(Object var1) {
      return var1 != null && var1 instanceof Paper && this.name.equals(((Paper)var1).name) && this.width == ((Paper)var1).width && this.height == ((Paper)var1).height && this.units == ((Paper)var1).units;
   }

   public final String toString() {
      return "Paper: " + this.name + " size=" + this.width + "x" + this.height + " " + this.units;
   }

   static {
      A0 = new Paper("A0", 841.0, 1189.0, Units.MM);
      A1 = new Paper("A1", 594.0, 841.0, Units.MM);
      A2 = new Paper("A2", 420.0, 594.0, Units.MM);
      A3 = new Paper("A3", 297.0, 420.0, Units.MM);
      A4 = new Paper("A4", 210.0, 297.0, Units.MM);
      A5 = new Paper("A5", 148.0, 210.0, Units.MM);
      A6 = new Paper("A6", 105.0, 148.0, Units.MM);
      DESIGNATED_LONG = new Paper("Designated Long", 110.0, 220.0, Units.MM);
      NA_LETTER = new Paper("Letter", 8.5, 11.0, Units.INCH);
      LEGAL = new Paper("Legal", 8.4, 14.0, Units.INCH);
      TABLOID = new Paper("Tabloid", 11.0, 17.0, Units.INCH);
      EXECUTIVE = new Paper("Executive", 7.25, 10.5, Units.INCH);
      NA_8X10 = new Paper("8x10", 8.0, 10.0, Units.INCH);
      MONARCH_ENVELOPE = new Paper("Monarch Envelope", 3.87, 7.5, Units.INCH);
      NA_NUMBER_10_ENVELOPE = new Paper("Number 10 Envelope", 4.125, 9.5, Units.INCH);
      C = new Paper("C", 17.0, 22.0, Units.INCH);
      JIS_B4 = new Paper("B4", 257.0, 364.0, Units.MM);
      JIS_B5 = new Paper("B5", 182.0, 257.0, Units.MM);
      JIS_B6 = new Paper("B6", 128.0, 182.0, Units.MM);
      JAPANESE_POSTCARD = new Paper("Japanese Postcard", 100.0, 148.0, Units.MM);
   }
}
