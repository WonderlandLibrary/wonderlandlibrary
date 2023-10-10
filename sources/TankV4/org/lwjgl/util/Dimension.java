package org.lwjgl.util;

import java.io.Serializable;

public final class Dimension implements Serializable, ReadableDimension, WritableDimension {
   static final long serialVersionUID = 1L;
   private int width;
   private int height;

   public Dimension() {
   }

   public Dimension(int var1, int var2) {
      this.width = var1;
      this.height = var2;
   }

   public Dimension(ReadableDimension var1) {
      this.setSize(var1);
   }

   public void setSize(int var1, int var2) {
      this.width = var1;
      this.height = var2;
   }

   public void setSize(ReadableDimension var1) {
      this.width = var1.getWidth();
      this.height = var1.getHeight();
   }

   public void getSize(WritableDimension var1) {
      var1.setSize(this);
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof ReadableDimension)) {
         return false;
      } else {
         ReadableDimension var2 = (ReadableDimension)var1;
         return this.width == var2.getWidth() && this.height == var2.getHeight();
      }
   }

   public int hashCode() {
      int var1 = this.width + this.height;
      return var1 * (var1 + 1) / 2 + this.width;
   }

   public String toString() {
      return this.getClass().getName() + "[width=" + this.width + ",height=" + this.height + "]";
   }

   public int getHeight() {
      return this.height;
   }

   public void setHeight(int var1) {
      this.height = var1;
   }

   public int getWidth() {
      return this.width;
   }

   public void setWidth(int var1) {
      this.width = var1;
   }
}
