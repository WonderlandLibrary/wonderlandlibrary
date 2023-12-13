package org.lwjgl.util;

import java.io.Serializable;

public final class Rectangle implements ReadableRectangle, WritableRectangle, Serializable {
   static final long serialVersionUID = 1L;
   private int x;
   private int y;
   private int width;
   private int height;

   public Rectangle() {
   }

   public Rectangle(int var1, int var2, int var3, int var4) {
      this.x = var1;
      this.y = var2;
      this.width = var3;
      this.height = var4;
   }

   public Rectangle(ReadablePoint var1, ReadableDimension var2) {
      this.x = var1.getX();
      this.y = var1.getY();
      this.width = var2.getWidth();
      this.height = var2.getHeight();
   }

   public Rectangle(ReadableRectangle var1) {
      this.x = var1.getX();
      this.y = var1.getY();
      this.width = var1.getWidth();
      this.height = var1.getHeight();
   }

   public void setLocation(int var1, int var2) {
      this.x = var1;
      this.y = var2;
   }

   public void setLocation(ReadablePoint var1) {
      this.x = var1.getX();
      this.y = var1.getY();
   }

   public void setSize(int var1, int var2) {
      this.width = var1;
      this.height = var2;
   }

   public void setSize(ReadableDimension var1) {
      this.width = var1.getWidth();
      this.height = var1.getHeight();
   }

   public void setBounds(int var1, int var2, int var3, int var4) {
      this.x = var1;
      this.y = var2;
      this.width = var3;
      this.height = var4;
   }

   public void setBounds(ReadablePoint var1, ReadableDimension var2) {
      this.x = var1.getX();
      this.y = var1.getY();
      this.width = var2.getWidth();
      this.height = var2.getHeight();
   }

   public void setBounds(ReadableRectangle var1) {
      this.x = var1.getX();
      this.y = var1.getY();
      this.width = var1.getWidth();
      this.height = var1.getHeight();
   }

   public void getBounds(WritableRectangle var1) {
      var1.setBounds(this.x, this.y, this.width, this.height);
   }

   public void getLocation(WritablePoint var1) {
      var1.setLocation(this.x, this.y);
   }

   public void getSize(WritableDimension var1) {
      var1.setSize(this.width, this.height);
   }

   public void translate(int var1, int var2) {
      this.x += var1;
      this.y += var2;
   }

   public void translate(ReadablePoint var1) {
      this.x += var1.getX();
      this.y += var1.getY();
   }

   public void untranslate(ReadablePoint var1) {
      this.x -= var1.getX();
      this.y -= var1.getY();
   }

   public boolean contains(ReadablePoint var1) {
      return this.contains(var1.getX(), var1.getY());
   }

   public boolean contains(int var1, int var2) {
      int var3 = this.width;
      int var4 = this.height;
      if ((var3 | var4) < 0) {
         return false;
      } else {
         int var5 = this.x;
         int var6 = this.y;
         if (var1 >= var5 && var2 >= var6) {
            var3 += var5;
            var4 += var6;
            return (var3 < var5 || var3 > var1) && (var4 < var6 || var4 > var2);
         } else {
            return false;
         }
      }
   }

   public boolean contains(ReadableRectangle var1) {
      return this.contains(var1.getX(), var1.getY(), var1.getWidth(), var1.getHeight());
   }

   public boolean contains(int var1, int var2, int var3, int var4) {
      int var5 = this.width;
      int var6 = this.height;
      if ((var5 | var6 | var3 | var4) < 0) {
         return false;
      } else {
         int var7 = this.x;
         int var8 = this.y;
         if (var1 >= var7 && var2 >= var8) {
            var5 += var7;
            var3 += var1;
            if (var3 <= var1) {
               if (var5 >= var7 || var3 > var5) {
                  return false;
               }
            } else if (var5 >= var7 && var3 > var5) {
               return false;
            }

            var6 += var8;
            var4 += var2;
            if (var4 <= var2) {
               if (var6 >= var8 || var4 > var6) {
                  return false;
               }
            } else if (var6 >= var8 && var4 > var6) {
               return false;
            }

            return true;
         } else {
            return false;
         }
      }
   }

   public boolean intersects(ReadableRectangle var1) {
      int var2 = this.width;
      int var3 = this.height;
      int var4 = var1.getWidth();
      int var5 = var1.getHeight();
      if (var4 > 0 && var5 > 0 && var2 > 0 && var3 > 0) {
         int var6 = this.x;
         int var7 = this.y;
         int var8 = var1.getX();
         int var9 = var1.getY();
         var4 += var8;
         var5 += var9;
         var2 += var6;
         var3 += var7;
         return (var4 < var8 || var4 > var6) && (var5 < var9 || var5 > var7) && (var2 < var6 || var2 > var8) && (var3 < var7 || var3 > var9);
      } else {
         return false;
      }
   }

   public Rectangle intersection(ReadableRectangle var1, Rectangle var2) {
      int var3 = this.x;
      int var4 = this.y;
      int var5 = var1.getX();
      int var6 = var1.getY();
      long var7 = (long)var3;
      var7 += (long)this.width;
      long var9 = (long)var4;
      var9 += (long)this.height;
      long var11 = (long)var5;
      var11 += (long)var1.getWidth();
      long var13 = (long)var6;
      var13 += (long)var1.getHeight();
      if (var3 < var5) {
         var3 = var5;
      }

      if (var4 < var6) {
         var4 = var6;
      }

      if (var7 > var11) {
         var7 = var11;
      }

      if (var9 > var13) {
         var9 = var13;
      }

      var7 -= (long)var3;
      var9 -= (long)var4;
      if (var7 < -2147483648L) {
         var7 = -2147483648L;
      }

      if (var9 < -2147483648L) {
         var9 = -2147483648L;
      }

      if (var2 == null) {
         var2 = new Rectangle(var3, var4, (int)var7, (int)var9);
      } else {
         var2.setBounds(var3, var4, (int)var7, (int)var9);
      }

      return var2;
   }

   public WritableRectangle union(ReadableRectangle var1, WritableRectangle var2) {
      int var3 = Math.min(this.x, var1.getX());
      int var4 = Math.max(this.x + this.width, var1.getX() + var1.getWidth());
      int var5 = Math.min(this.y, var1.getY());
      int var6 = Math.max(this.y + this.height, var1.getY() + var1.getHeight());
      var2.setBounds(var3, var5, var4 - var3, var6 - var5);
      return var2;
   }

   public void add(int var1, int var2) {
      int var3 = Math.min(this.x, var1);
      int var4 = Math.max(this.x + this.width, var1);
      int var5 = Math.min(this.y, var2);
      int var6 = Math.max(this.y + this.height, var2);
      this.x = var3;
      this.y = var5;
      this.width = var4 - var3;
      this.height = var6 - var5;
   }

   public void add(ReadablePoint var1) {
      this.add(var1.getX(), var1.getY());
   }

   public void add(ReadableRectangle var1) {
      int var2 = Math.min(this.x, var1.getX());
      int var3 = Math.max(this.x + this.width, var1.getX() + var1.getWidth());
      int var4 = Math.min(this.y, var1.getY());
      int var5 = Math.max(this.y + this.height, var1.getY() + var1.getHeight());
      this.x = var2;
      this.y = var4;
      this.width = var3 - var2;
      this.height = var5 - var4;
   }

   public void grow(int var1, int var2) {
      this.x -= var1;
      this.y -= var2;
      this.width += var1 * 2;
      this.height += var2 * 2;
   }

   public boolean isEmpty() {
      return this.width <= 0 || this.height <= 0;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof Rectangle)) {
         return super.equals(var1);
      } else {
         Rectangle var2 = (Rectangle)var1;
         return this.x == var2.x && this.y == var2.y && this.width == var2.width && this.height == var2.height;
      }
   }

   public String toString() {
      return this.getClass().getName() + "[x=" + this.x + ",y=" + this.y + ",width=" + this.width + ",height=" + this.height + "]";
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

   public int getX() {
      return this.x;
   }

   public void setX(int var1) {
      this.x = var1;
   }

   public int getY() {
      return this.y;
   }

   public void setY(int var1) {
      this.y = var1;
   }
}
