package javafx.scene.control;

import javafx.beans.NamedArg;

public final class IndexRange {
   private int start;
   private int end;
   public static final String VALUE_DELIMITER = ",";

   public IndexRange(@NamedArg("start") int var1, @NamedArg("end") int var2) {
      if (var2 < var1) {
         throw new IllegalArgumentException();
      } else {
         this.start = var1;
         this.end = var2;
      }
   }

   public IndexRange(@NamedArg("range") IndexRange var1) {
      this.start = var1.start;
      this.end = var1.end;
   }

   public int getStart() {
      return this.start;
   }

   public int getEnd() {
      return this.end;
   }

   public int getLength() {
      return this.end - this.start;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IndexRange)) {
         return false;
      } else {
         IndexRange var2 = (IndexRange)var1;
         return this.start == var2.start && this.end == var2.end;
      }
   }

   public int hashCode() {
      return 31 * this.start + this.end;
   }

   public String toString() {
      return this.start + "," + " " + this.end;
   }

   public static IndexRange normalize(int var0, int var1) {
      return new IndexRange(Math.min(var0, var1), Math.max(var0, var1));
   }

   public static IndexRange valueOf(String var0) {
      if (var0 == null) {
         throw new IllegalArgumentException();
      } else {
         String[] var1 = var0.split(",");
         if (var1.length != 2) {
            throw new IllegalArgumentException();
         } else {
            int var2 = Integer.parseInt(var1[0].trim());
            int var3 = Integer.parseInt(var1[1].trim());
            return normalize(var2, var3);
         }
      }
   }
}
