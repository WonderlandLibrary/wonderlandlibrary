package com.sun.javafx.jmx;

import com.sun.javafx.tk.TKScene;
import javafx.geometry.Rectangle2D;

public class HighlightRegion extends Rectangle2D {
   private TKScene tkScene;
   private int hash = 0;

   public HighlightRegion(TKScene var1, double var2, double var4, double var6, double var8) {
      super(var2, var4, var6, var8);
      this.tkScene = var1;
   }

   public TKScene getTKScene() {
      return this.tkScene;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof HighlightRegion)) {
         return false;
      } else {
         HighlightRegion var2 = (HighlightRegion)var1;
         return this.tkScene.equals(var2.tkScene) && super.equals(var2);
      }
   }

   public int hashCode() {
      if (this.hash == 0) {
         long var1 = 7L;
         var1 = 31L * var1 + (long)super.hashCode();
         var1 = 31L * var1 + (long)this.tkScene.hashCode();
         this.hash = (int)(var1 ^ var1 >> 32);
      }

      return this.hash;
   }

   public String toString() {
      return "HighlighRegion [tkScene = " + this.tkScene + ", rectangle = " + super.toString() + "]";
   }
}
