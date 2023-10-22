package com.sun.javafx.css;

final class StyleClass {
   private final String styleClassName;
   private final int index;

   StyleClass(String var1, int var2) {
      this.styleClassName = var1;
      this.index = var2;
   }

   public String getStyleClassName() {
      return this.styleClassName;
   }

   public String toString() {
      return this.styleClassName;
   }

   public int getIndex() {
      return this.index;
   }
}
