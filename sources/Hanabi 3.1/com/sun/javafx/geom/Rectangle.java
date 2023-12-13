package com.sun.javafx.geom;

public class Rectangle {
   public int x;
   public int y;
   public int width;
   public int height;

   public Rectangle() {
      this(0, 0, 0, 0);
   }

   public Rectangle(BaseBounds var1) {
      this.setBounds(var1);
   }

   public Rectangle(Rectangle var1) {
      this(var1.x, var1.y, var1.width, var1.height);
   }

   public Rectangle(int var1, int var2, int var3, int var4) {
      this.x = var1;
      this.y = var2;
      this.width = var3;
      this.height = var4;
   }

   public Rectangle(int var1, int var2) {
      this(0, 0, var1, var2);
   }

   public void setBounds(Rectangle var1) {
      this.setBounds(var1.x, var1.y, var1.width, var1.height);
   }

   public void setBounds(int var1, int var2, int var3, int var4) {
      this.reshape(var1, var2, var3, var4);
   }

   public void setBounds(BaseBounds var1) {
      this.x = (int)Math.floor((double)var1.getMinX());
      this.y = (int)Math.floor((double)var1.getMinY());
      int var2 = (int)Math.ceil((double)var1.getMaxX());
      int var3 = (int)Math.ceil((double)var1.getMaxY());
      this.width = var2 - this.x;
      this.height = var3 - this.y;
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

   public boolean contains(Rectangle var1) {
      return this.contains(var1.x, var1.y, var1.width, var1.height);
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

   public Rectangle intersection(Rectangle var1) {
      Rectangle var2 = new Rectangle(this);
      var2.intersectWith(var1);
      return var2;
   }

   public void intersectWith(Rectangle var1) {
      if (var1 != null) {
         int var2 = this.x;
         int var3 = this.y;
         int var4 = var1.x;
         int var5 = var1.y;
         long var6 = (long)var2;
         var6 += (long)this.width;
         long var8 = (long)var3;
         var8 += (long)this.height;
         long var10 = (long)var4;
         var10 += (long)var1.width;
         long var12 = (long)var5;
         var12 += (long)var1.height;
         if (var2 < var4) {
            var2 = var4;
         }

         if (var3 < var5) {
            var3 = var5;
         }

         if (var6 > var10) {
            var6 = var10;
         }

         if (var8 > var12) {
            var8 = var12;
         }

         var6 -= (long)var2;
         var8 -= (long)var3;
         if (var6 < -2147483648L) {
            var6 = -2147483648L;
         }

         if (var8 < -2147483648L) {
            var8 = -2147483648L;
         }

         this.setBounds(var2, var3, (int)var6, (int)var8);
      }
   }

   public void translate(int var1, int var2) {
      int var3 = this.x;
      int var4 = var3 + var1;
      if (var1 < 0) {
         if (var4 > var3) {
            if (this.width >= 0) {
               this.width += var4 - Integer.MIN_VALUE;
            }

            var4 = Integer.MIN_VALUE;
         }
      } else if (var4 < var3) {
         if (this.width >= 0) {
            this.width += var4 - Integer.MAX_VALUE;
            if (this.width < 0) {
               this.width = Integer.MAX_VALUE;
            }
         }

         var4 = Integer.MAX_VALUE;
      }

      this.x = var4;
      var3 = this.y;
      var4 = var3 + var2;
      if (var2 < 0) {
         if (var4 > var3) {
            if (this.height >= 0) {
               this.height += var4 - Integer.MIN_VALUE;
            }

            var4 = Integer.MIN_VALUE;
         }
      } else if (var4 < var3) {
         if (this.height >= 0) {
            this.height += var4 - Integer.MAX_VALUE;
            if (this.height < 0) {
               this.height = Integer.MAX_VALUE;
            }
         }

         var4 = Integer.MAX_VALUE;
      }

      this.y = var4;
   }

   public RectBounds toRectBounds() {
      return new RectBounds((float)this.x, (float)this.y, (float)(this.x + this.width), (float)(this.y + this.height));
   }

   public void add(int var1, int var2) {
      if ((this.width | this.height) < 0) {
         this.x = var1;
         this.y = var2;
         this.width = this.height = 0;
      } else {
         int var3 = this.x;
         int var4 = this.y;
         long var5 = (long)this.width;
         long var7 = (long)this.height;
         var5 += (long)var3;
         var7 += (long)var4;
         if (var3 > var1) {
            var3 = var1;
         }

         if (var4 > var2) {
            var4 = var2;
         }

         if (var5 < (long)var1) {
            var5 = (long)var1;
         }

         if (var7 < (long)var2) {
            var7 = (long)var2;
         }

         var5 -= (long)var3;
         var7 -= (long)var4;
         if (var5 > 2147483647L) {
            var5 = 2147483647L;
         }

         if (var7 > 2147483647L) {
            var7 = 2147483647L;
         }

         this.reshape(var3, var4, (int)var5, (int)var7);
      }
   }

   public void add(Rectangle var1) {
      long var2 = (long)this.width;
      long var4 = (long)this.height;
      if ((var2 | var4) < 0L) {
         this.reshape(var1.x, var1.y, var1.width, var1.height);
      }

      long var6 = (long)var1.width;
      long var8 = (long)var1.height;
      if ((var6 | var8) >= 0L) {
         int var10 = this.x;
         int var11 = this.y;
         var2 += (long)var10;
         var4 += (long)var11;
         int var12 = var1.x;
         int var13 = var1.y;
         var6 += (long)var12;
         var8 += (long)var13;
         if (var10 > var12) {
            var10 = var12;
         }

         if (var11 > var13) {
            var11 = var13;
         }

         if (var2 < var6) {
            var2 = var6;
         }

         if (var4 < var8) {
            var4 = var8;
         }

         var2 -= (long)var10;
         var4 -= (long)var11;
         if (var2 > 2147483647L) {
            var2 = 2147483647L;
         }

         if (var4 > 2147483647L) {
            var4 = 2147483647L;
         }

         this.reshape(var10, var11, (int)var2, (int)var4);
      }
   }

   public void grow(int var1, int var2) {
      long var3 = (long)this.x;
      long var5 = (long)this.y;
      long var7 = (long)this.width;
      long var9 = (long)this.height;
      var7 += var3;
      var9 += var5;
      var3 -= (long)var1;
      var5 -= (long)var2;
      var7 += (long)var1;
      var9 += (long)var2;
      if (var7 < var3) {
         var7 -= var3;
         if (var7 < -2147483648L) {
            var7 = -2147483648L;
         }

         if (var3 < -2147483648L) {
            var3 = -2147483648L;
         } else if (var3 > 2147483647L) {
            var3 = 2147483647L;
         }
      } else {
         if (var3 < -2147483648L) {
            var3 = -2147483648L;
         } else if (var3 > 2147483647L) {
            var3 = 2147483647L;
         }

         var7 -= var3;
         if (var7 < -2147483648L) {
            var7 = -2147483648L;
         } else if (var7 > 2147483647L) {
            var7 = 2147483647L;
         }
      }

      if (var9 < var5) {
         var9 -= var5;
         if (var9 < -2147483648L) {
            var9 = -2147483648L;
         }

         if (var5 < -2147483648L) {
            var5 = -2147483648L;
         } else if (var5 > 2147483647L) {
            var5 = 2147483647L;
         }
      } else {
         if (var5 < -2147483648L) {
            var5 = -2147483648L;
         } else if (var5 > 2147483647L) {
            var5 = 2147483647L;
         }

         var9 -= var5;
         if (var9 < -2147483648L) {
            var9 = -2147483648L;
         } else if (var9 > 2147483647L) {
            var9 = 2147483647L;
         }
      }

      this.reshape((int)var3, (int)var5, (int)var7, (int)var9);
   }

   private void reshape(int var1, int var2, int var3, int var4) {
      this.x = var1;
      this.y = var2;
      this.width = var3;
      this.height = var4;
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

   public int hashCode() {
      int var1 = Float.floatToIntBits((float)this.x);
      var1 += Float.floatToIntBits((float)this.y) * 37;
      var1 += Float.floatToIntBits((float)this.width) * 43;
      var1 += Float.floatToIntBits((float)this.height) * 47;
      return var1;
   }

   public String toString() {
      return this.getClass().getName() + "[x=" + this.x + ",y=" + this.y + ",width=" + this.width + ",height=" + this.height + "]";
   }
}
