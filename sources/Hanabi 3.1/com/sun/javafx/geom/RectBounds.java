package com.sun.javafx.geom;

public final class RectBounds extends BaseBounds {
   private float minX;
   private float maxX;
   private float minY;
   private float maxY;

   public RectBounds() {
      this.minX = this.minY = 0.0F;
      this.maxX = this.maxY = -1.0F;
   }

   public BaseBounds copy() {
      return new RectBounds(this.minX, this.minY, this.maxX, this.maxY);
   }

   public RectBounds(float var1, float var2, float var3, float var4) {
      this.setBounds(var1, var2, var3, var4);
   }

   public RectBounds(RectBounds var1) {
      this.setBounds(var1);
   }

   public RectBounds(Rectangle var1) {
      this.setBounds((float)var1.x, (float)var1.y, (float)(var1.x + var1.width), (float)(var1.y + var1.height));
   }

   public BaseBounds.BoundsType getBoundsType() {
      return BaseBounds.BoundsType.RECTANGLE;
   }

   public boolean is2D() {
      return true;
   }

   public float getWidth() {
      return this.maxX - this.minX;
   }

   public float getHeight() {
      return this.maxY - this.minY;
   }

   public float getDepth() {
      return 0.0F;
   }

   public float getMinX() {
      return this.minX;
   }

   public void setMinX(float var1) {
      this.minX = var1;
   }

   public float getMinY() {
      return this.minY;
   }

   public void setMinY(float var1) {
      this.minY = var1;
   }

   public float getMinZ() {
      return 0.0F;
   }

   public float getMaxX() {
      return this.maxX;
   }

   public void setMaxX(float var1) {
      this.maxX = var1;
   }

   public float getMaxY() {
      return this.maxY;
   }

   public void setMaxY(float var1) {
      this.maxY = var1;
   }

   public float getMaxZ() {
      return 0.0F;
   }

   public Vec2f getMin(Vec2f var1) {
      if (var1 == null) {
         var1 = new Vec2f();
      }

      var1.x = this.minX;
      var1.y = this.minY;
      return var1;
   }

   public Vec2f getMax(Vec2f var1) {
      if (var1 == null) {
         var1 = new Vec2f();
      }

      var1.x = this.maxX;
      var1.y = this.maxY;
      return var1;
   }

   public Vec3f getMin(Vec3f var1) {
      if (var1 == null) {
         var1 = new Vec3f();
      }

      var1.x = this.minX;
      var1.y = this.minY;
      var1.z = 0.0F;
      return var1;
   }

   public Vec3f getMax(Vec3f var1) {
      if (var1 == null) {
         var1 = new Vec3f();
      }

      var1.x = this.maxX;
      var1.y = this.maxY;
      var1.z = 0.0F;
      return var1;
   }

   public BaseBounds deriveWithUnion(BaseBounds var1) {
      if (var1.getBoundsType() == BaseBounds.BoundsType.RECTANGLE) {
         RectBounds var3 = (RectBounds)var1;
         this.unionWith(var3);
         return this;
      } else if (var1.getBoundsType() == BaseBounds.BoundsType.BOX) {
         BoxBounds var2 = new BoxBounds((BoxBounds)var1);
         var2.unionWith(this);
         return var2;
      } else {
         throw new UnsupportedOperationException("Unknown BoundsType");
      }
   }

   public BaseBounds deriveWithNewBounds(Rectangle var1) {
      if (var1.width >= 0 && var1.height >= 0) {
         this.setBounds((float)var1.x, (float)var1.y, (float)(var1.x + var1.width), (float)(var1.y + var1.height));
         return this;
      } else {
         return this.makeEmpty();
      }
   }

   public BaseBounds deriveWithNewBounds(BaseBounds var1) {
      if (var1.isEmpty()) {
         return this.makeEmpty();
      } else if (var1.getBoundsType() == BaseBounds.BoundsType.RECTANGLE) {
         RectBounds var2 = (RectBounds)var1;
         this.minX = var2.getMinX();
         this.minY = var2.getMinY();
         this.maxX = var2.getMaxX();
         this.maxY = var2.getMaxY();
         return this;
      } else if (var1.getBoundsType() == BaseBounds.BoundsType.BOX) {
         return new BoxBounds((BoxBounds)var1);
      } else {
         throw new UnsupportedOperationException("Unknown BoundsType");
      }
   }

   public BaseBounds deriveWithNewBounds(float var1, float var2, float var3, float var4, float var5, float var6) {
      if (!(var4 < var1) && !(var5 < var2) && !(var6 < var3)) {
         if (var3 == 0.0F && var6 == 0.0F) {
            this.minX = var1;
            this.minY = var2;
            this.maxX = var4;
            this.maxY = var5;
            return this;
         } else {
            return new BoxBounds(var1, var2, var3, var4, var5, var6);
         }
      } else {
         return this.makeEmpty();
      }
   }

   public BaseBounds deriveWithNewBoundsAndSort(float var1, float var2, float var3, float var4, float var5, float var6) {
      if (var3 == 0.0F && var6 == 0.0F) {
         this.setBoundsAndSort(var1, var2, var3, var4, var5, var6);
         return this;
      } else {
         BoxBounds var7 = new BoxBounds();
         var7.setBoundsAndSort(var1, var2, var3, var4, var5, var6);
         return var7;
      }
   }

   public final void setBounds(RectBounds var1) {
      this.minX = var1.getMinX();
      this.minY = var1.getMinY();
      this.maxX = var1.getMaxX();
      this.maxY = var1.getMaxY();
   }

   public final void setBounds(float var1, float var2, float var3, float var4) {
      this.minX = var1;
      this.minY = var2;
      this.maxX = var3;
      this.maxY = var4;
   }

   public void setBoundsAndSort(float var1, float var2, float var3, float var4) {
      this.setBounds(var1, var2, var3, var4);
      this.sortMinMax();
   }

   public void setBoundsAndSort(float var1, float var2, float var3, float var4, float var5, float var6) {
      if (var3 == 0.0F && var6 == 0.0F) {
         this.setBounds(var1, var2, var4, var5);
         this.sortMinMax();
      } else {
         throw new UnsupportedOperationException("Unknown BoundsType");
      }
   }

   public void setBoundsAndSort(Point2D var1, Point2D var2) {
      this.setBoundsAndSort(var1.x, var1.y, var2.x, var2.y);
   }

   public RectBounds flattenInto(RectBounds var1) {
      if (var1 == null) {
         var1 = new RectBounds();
      }

      if (this.isEmpty()) {
         return var1.makeEmpty();
      } else {
         var1.setBounds(this.minX, this.minY, this.maxX, this.maxY);
         return var1;
      }
   }

   public void unionWith(RectBounds var1) {
      if (!var1.isEmpty()) {
         if (this.isEmpty()) {
            this.setBounds(var1);
         } else {
            this.minX = Math.min(this.minX, var1.getMinX());
            this.minY = Math.min(this.minY, var1.getMinY());
            this.maxX = Math.max(this.maxX, var1.getMaxX());
            this.maxY = Math.max(this.maxY, var1.getMaxY());
         }
      }
   }

   public void unionWith(float var1, float var2, float var3, float var4) {
      if (!(var3 < var1) && !(var4 < var2)) {
         if (this.isEmpty()) {
            this.setBounds(var1, var2, var3, var4);
         } else {
            this.minX = Math.min(this.minX, var1);
            this.minY = Math.min(this.minY, var2);
            this.maxX = Math.max(this.maxX, var3);
            this.maxY = Math.max(this.maxY, var4);
         }
      }
   }

   public void add(float var1, float var2, float var3) {
      if (var3 != 0.0F) {
         throw new UnsupportedOperationException("Unknown BoundsType");
      } else {
         this.unionWith(var1, var2, var1, var2);
      }
   }

   public void add(float var1, float var2) {
      this.unionWith(var1, var2, var1, var2);
   }

   public void add(Point2D var1) {
      this.add(var1.x, var1.y);
   }

   public void intersectWith(BaseBounds var1) {
      if (!this.isEmpty()) {
         if (var1.isEmpty()) {
            this.makeEmpty();
         } else {
            this.minX = Math.max(this.minX, var1.getMinX());
            this.minY = Math.max(this.minY, var1.getMinY());
            this.maxX = Math.min(this.maxX, var1.getMaxX());
            this.maxY = Math.min(this.maxY, var1.getMaxY());
         }
      }
   }

   public void intersectWith(Rectangle var1) {
      float var2 = (float)var1.x;
      float var3 = (float)var1.y;
      this.intersectWith(var2, var3, var2 + (float)var1.width, var3 + (float)var1.height);
   }

   public void intersectWith(float var1, float var2, float var3, float var4) {
      if (!this.isEmpty()) {
         if (!(var3 < var1) && !(var4 < var2)) {
            this.minX = Math.max(this.minX, var1);
            this.minY = Math.max(this.minY, var2);
            this.maxX = Math.min(this.maxX, var3);
            this.maxY = Math.min(this.maxY, var4);
         } else {
            this.makeEmpty();
         }
      }
   }

   public void intersectWith(float var1, float var2, float var3, float var4, float var5, float var6) {
      if (!this.isEmpty()) {
         if (!(var4 < var1) && !(var5 < var2) && !(var6 < var3)) {
            this.minX = Math.max(this.minX, var1);
            this.minY = Math.max(this.minY, var2);
            this.maxX = Math.min(this.maxX, var4);
            this.maxY = Math.min(this.maxY, var5);
         } else {
            this.makeEmpty();
         }
      }
   }

   public boolean contains(Point2D var1) {
      if (var1 != null && !this.isEmpty()) {
         return var1.x >= this.minX && var1.x <= this.maxX && var1.y >= this.minY && var1.y <= this.maxY;
      } else {
         return false;
      }
   }

   public boolean contains(float var1, float var2) {
      if (this.isEmpty()) {
         return false;
      } else {
         return var1 >= this.minX && var1 <= this.maxX && var2 >= this.minY && var2 <= this.maxY;
      }
   }

   public boolean contains(RectBounds var1) {
      if (!this.isEmpty() && !var1.isEmpty()) {
         return this.minX <= var1.minX && this.maxX >= var1.maxX && this.minY <= var1.minY && this.maxY >= var1.maxY;
      } else {
         return false;
      }
   }

   public boolean intersects(float var1, float var2, float var3, float var4) {
      if (this.isEmpty()) {
         return false;
      } else {
         return var1 + var3 >= this.minX && var2 + var4 >= this.minY && var1 <= this.maxX && var2 <= this.maxY;
      }
   }

   public boolean intersects(BaseBounds var1) {
      if (var1 != null && !var1.isEmpty() && !this.isEmpty()) {
         return var1.getMaxX() >= this.minX && var1.getMaxY() >= this.minY && var1.getMaxZ() >= this.getMinZ() && var1.getMinX() <= this.maxX && var1.getMinY() <= this.maxY && var1.getMinZ() <= this.getMaxZ();
      } else {
         return false;
      }
   }

   public boolean disjoint(float var1, float var2, float var3, float var4) {
      if (this.isEmpty()) {
         return true;
      } else {
         return var1 + var3 < this.minX || var2 + var4 < this.minY || var1 > this.maxX || var2 > this.maxY;
      }
   }

   public boolean disjoint(RectBounds var1) {
      if (var1 != null && !var1.isEmpty() && !this.isEmpty()) {
         return var1.getMaxX() < this.minX || var1.getMaxY() < this.minY || var1.getMinX() > this.maxX || var1.getMinY() > this.maxY;
      } else {
         return true;
      }
   }

   public boolean isEmpty() {
      return !(this.maxX >= this.minX) || !(this.maxY >= this.minY);
   }

   public void roundOut() {
      this.minX = (float)Math.floor((double)this.minX);
      this.minY = (float)Math.floor((double)this.minY);
      this.maxX = (float)Math.ceil((double)this.maxX);
      this.maxY = (float)Math.ceil((double)this.maxY);
   }

   public void grow(float var1, float var2) {
      this.minX -= var1;
      this.maxX += var1;
      this.minY -= var2;
      this.maxY += var2;
   }

   public BaseBounds deriveWithPadding(float var1, float var2, float var3) {
      if (var3 == 0.0F) {
         this.grow(var1, var2);
         return this;
      } else {
         BoxBounds var4 = new BoxBounds(this.minX, this.minY, 0.0F, this.maxX, this.maxY, 0.0F);
         var4.grow(var1, var2, var3);
         return var4;
      }
   }

   public RectBounds makeEmpty() {
      this.minX = this.minY = 0.0F;
      this.maxX = this.maxY = -1.0F;
      return this;
   }

   protected void sortMinMax() {
      float var1;
      if (this.minX > this.maxX) {
         var1 = this.maxX;
         this.maxX = this.minX;
         this.minX = var1;
      }

      if (this.minY > this.maxY) {
         var1 = this.maxY;
         this.maxY = this.minY;
         this.minY = var1;
      }

   }

   public void translate(float var1, float var2, float var3) {
      this.setMinX(this.getMinX() + var1);
      this.setMinY(this.getMinY() + var2);
      this.setMaxX(this.getMaxX() + var1);
      this.setMaxY(this.getMaxY() + var2);
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         RectBounds var2 = (RectBounds)var1;
         if (this.minX != var2.getMinX()) {
            return false;
         } else if (this.minY != var2.getMinY()) {
            return false;
         } else if (this.maxX != var2.getMaxX()) {
            return false;
         } else {
            return this.maxY == var2.getMaxY();
         }
      }
   }

   public int hashCode() {
      int var1 = 7;
      var1 = 79 * var1 + Float.floatToIntBits(this.minX);
      var1 = 79 * var1 + Float.floatToIntBits(this.minY);
      var1 = 79 * var1 + Float.floatToIntBits(this.maxX);
      var1 = 79 * var1 + Float.floatToIntBits(this.maxY);
      return var1;
   }

   public String toString() {
      return "RectBounds { minX:" + this.minX + ", minY:" + this.minY + ", maxX:" + this.maxX + ", maxY:" + this.maxY + "} (w:" + (this.maxX - this.minX) + ", h:" + (this.maxY - this.minY) + ")";
   }
}
