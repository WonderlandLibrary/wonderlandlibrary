package javafx.scene.layout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.NamedArg;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;

public final class BorderStrokeStyle {
   private static final List DOTTED_LIST = Collections.unmodifiableList(asList(0.0, 2.0));
   private static final List DASHED_LIST = Collections.unmodifiableList(asList(2.0, 1.4));
   public static final BorderStrokeStyle NONE;
   public static final BorderStrokeStyle DOTTED;
   public static final BorderStrokeStyle DASHED;
   public static final BorderStrokeStyle SOLID;
   private final StrokeType type;
   private final StrokeLineJoin lineJoin;
   private final StrokeLineCap lineCap;
   private final double miterLimit;
   private final double dashOffset;
   private final List dashArray;
   private final int hash;

   public final StrokeType getType() {
      return this.type;
   }

   public final StrokeLineJoin getLineJoin() {
      return this.lineJoin;
   }

   public final StrokeLineCap getLineCap() {
      return this.lineCap;
   }

   public final double getMiterLimit() {
      return this.miterLimit;
   }

   public final double getDashOffset() {
      return this.dashOffset;
   }

   public final List getDashArray() {
      return this.dashArray;
   }

   public BorderStrokeStyle(@NamedArg("type") StrokeType var1, @NamedArg("lineJoin") StrokeLineJoin var2, @NamedArg("lineCap") StrokeLineCap var3, @NamedArg("miterLimit") double var4, @NamedArg("dashOffset") double var6, @NamedArg("dashArray") List var8) {
      this.type = var1 != null ? var1 : StrokeType.CENTERED;
      this.lineJoin = var2 != null ? var2 : StrokeLineJoin.MITER;
      this.lineCap = var3 != null ? var3 : StrokeLineCap.BUTT;
      this.miterLimit = var4;
      this.dashOffset = var6;
      if (var8 == null) {
         this.dashArray = Collections.emptyList();
      } else if (var8 != DASHED_LIST && var8 != DOTTED_LIST) {
         ArrayList var9 = new ArrayList(var8);
         this.dashArray = Collections.unmodifiableList(var9);
      } else {
         this.dashArray = var8;
      }

      int var12 = this.type.hashCode();
      var12 = 31 * var12 + this.lineJoin.hashCode();
      var12 = 31 * var12 + this.lineCap.hashCode();
      long var10 = this.miterLimit != 0.0 ? Double.doubleToLongBits(this.miterLimit) : 0L;
      var12 = 31 * var12 + (int)(var10 ^ var10 >>> 32);
      var10 = this.dashOffset != 0.0 ? Double.doubleToLongBits(this.dashOffset) : 0L;
      var12 = 31 * var12 + (int)(var10 ^ var10 >>> 32);
      var12 = 31 * var12 + this.dashArray.hashCode();
      this.hash = var12;
   }

   public String toString() {
      if (this == NONE) {
         return "BorderStyle.NONE";
      } else if (this == DASHED) {
         return "BorderStyle.DASHED";
      } else if (this == DOTTED) {
         return "BorderStyle.DOTTED";
      } else if (this == SOLID) {
         return "BorderStyle.SOLID";
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("BorderStyle: ");
         var1.append(this.type);
         var1.append(", ");
         var1.append(this.lineJoin);
         var1.append(", ");
         var1.append(this.lineCap);
         var1.append(", ");
         var1.append(this.miterLimit);
         var1.append(", ");
         var1.append(this.dashOffset);
         var1.append(", [");
         if (this.dashArray != null) {
            var1.append(this.dashArray);
         }

         var1.append("]");
         return var1.toString();
      }
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if ((this != NONE || var1 == NONE) && (var1 != NONE || this == NONE)) {
         if (var1 != null && this.getClass() == var1.getClass()) {
            BorderStrokeStyle var2 = (BorderStrokeStyle)var1;
            if (this.hash != var2.hash) {
               return false;
            } else if (Double.compare(var2.dashOffset, this.dashOffset) != 0) {
               return false;
            } else if (Double.compare(var2.miterLimit, this.miterLimit) != 0) {
               return false;
            } else if (!this.dashArray.equals(var2.dashArray)) {
               return false;
            } else if (this.lineCap != var2.lineCap) {
               return false;
            } else if (this.lineJoin != var2.lineJoin) {
               return false;
            } else {
               return this.type == var2.type;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.hash;
   }

   private static List asList(double... var0) {
      ArrayList var1 = new ArrayList(var0.length);

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1.add(var0[var2]);
      }

      return var1;
   }

   static {
      NONE = new BorderStrokeStyle(StrokeType.INSIDE, StrokeLineJoin.MITER, StrokeLineCap.BUTT, 0.0, 0.0, (List)null);
      DOTTED = new BorderStrokeStyle(StrokeType.INSIDE, StrokeLineJoin.MITER, StrokeLineCap.ROUND, 10.0, 0.0, DOTTED_LIST);
      DASHED = new BorderStrokeStyle(StrokeType.INSIDE, StrokeLineJoin.MITER, StrokeLineCap.BUTT, 10.0, 0.0, DASHED_LIST);
      SOLID = new BorderStrokeStyle(StrokeType.INSIDE, StrokeLineJoin.MITER, StrokeLineCap.BUTT, 10.0, 0.0, (List)null);
   }
}
