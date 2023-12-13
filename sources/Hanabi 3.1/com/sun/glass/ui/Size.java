package com.sun.glass.ui;

public final class Size {
   public int width;
   public int height;

   public Size(int var1, int var2) {
      this.width = var1;
      this.height = var2;
   }

   public Size() {
      this(0, 0);
   }

   public String toString() {
      return "Size(" + this.width + ", " + this.height + ")";
   }
}
