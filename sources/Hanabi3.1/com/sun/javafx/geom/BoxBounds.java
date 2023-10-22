package com.sun.javafx.geom;

public class BoxBounds extends BaseBounds {
   private float minX;
   private float maxX;
   private float minY;
   private float maxY;
   private float minZ;
   private float maxZ;

   public BoxBounds() {
      this.minX = this.minY = this.minZ = 0.0F;
      this.maxX = this.maxY = this.maxZ = -1.0F;
   }

   public BaseBounds copy() {
      return new BoxBounds(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
   }

   public BoxBounds(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.setBounds(var1, var2, var3, var4, var5, var6);
   }

   public BoxBounds(BoxBounds var1) {
      this.setBounds(var1);
   }

   public BaseBounds.BoundsType getBoundsType() {
      return BaseBounds.BoundsType.BOX;
   }

   public boolean is2D() {
      return false;
   }

   public float getWidth() {
      return this.maxX - this.minX;
   }

   public float getHeight() {
      return this.maxY - this.minY;
   }

   public float getDepth() {
      return this.maxZ - this.minZ;
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
      return this.minZ;
   }

   public void setMinZ(float var1) {
      this.minZ = var1;
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
      return this.maxZ;
   }

   public void setMaxZ(float var1) {
      this.maxZ = var1;
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
      var1.z = this.minZ;
      return var1;
   }

   public Vec3f getMax(Vec3f var1) {
      if (var1 == null) {
         var1 = new Vec3f();
      }

      var1.x = this.maxX;
      var1.y = this.maxY;
      var1.z = this.maxZ;
      return var1;
   }

   public BaseBounds deriveWithUnion(BaseBounds var1) {
      if (var1.getBoundsType() != BaseBounds.BoundsType.RECTANGLE && var1.getBoundsType() != BaseBounds.BoundsType.BOX) {
         throw new UnsupportedOperationException("Unknown BoundsType");
      } else {
         this.unionWith(var1);
         return this;
      }
   }

   public BaseBounds deriveWithNewBounds(Rectangle var1) {
      if (var1.width >= 0 && var1.height >= 0) {
         this.setBounds((float)var1.x, (float)var1.y, 0.0F, (float)(var1.x + var1.width), (float)(var1.y + var1.height), 0.0F);
         return this;
      } else {
         return this.makeEmpty();
      }
   }

   public BaseBounds deriveWithNewBounds(BaseBounds var1) {
      if (var1.isEmpty()) {
         return this.makeEmpty();
      } else if (var1.getBoundsType() != BaseBounds.BoundsType.RECTANGLE && var1.getBoundsType() != BaseBounds.BoundsType.BOX) {
         throw new UnsupportedOperationException("Unknown BoundsType");
      } else {
         this.minX = var1.getMinX();
         this.minY = var1.getMinY();
         this.minZ = var1.getMinZ();
         this.maxX = var1.getMaxX();
         this.maxY = var1.getMaxY();
         this.maxZ = var1.getMaxZ();
         return this;
      }
   }

   public BaseBounds deriveWithNewBounds(float var1, float var2, float var3, float var4, float var5, float var6) {
      if (!(var4 < var1) && !(var5 < var2) && !(var6 < var3)) {
         this.minX = var1;
         this.minY = var2;
         this.minZ = var3;
         this.maxX = var4;
         this.maxY = var5;
         this.maxZ = var6;
         return this;
      } else {
         return this.makeEmpty();
      }
   }

   public BaseBounds deriveWithNewBoundsAndSort(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.setBoundsAndSort(var1, var2, var3, var4, var5, var6);
      return this;
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

   public final void setBounds(BaseBounds var1) {
      this.minX = var1.getMinX();
      this.minY = var1.getMinY();
      this.minZ = var1.getMinZ();
      this.maxX = var1.getMaxX();
      this.maxY = var1.getMaxY();
      this.maxZ = var1.getMaxZ();
   }

   public final void setBounds(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.minX = var1;
      this.minY = var2;
      this.minZ = var3;
      this.maxX = var4;
      this.maxY = var5;
      this.maxZ = var6;
   }

   public void setBoundsAndSort(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.setBounds(var1, var2, var3, var4, var5, var6);
      this.sortMinMax();
   }

   public void setBoundsAndSort(Point2D var1, Point2D var2) {
      this.setBoundsAndSort(var1.x, var1.y, 0.0F, var2.x, var2.y, 0.0F);
   }

   public void unionWith(BaseBounds var1) {
      if (!var1.isEmpty()) {
         if (this.isEmpty()) {
            this.setBounds(var1);
         } else {
            this.minX = Math.min(this.minX, var1.getMinX());
            this.minY = Math.min(this.minY, var1.getMinY());
            this.minZ = Math.min(this.minZ, var1.getMinZ());
            this.maxX = Math.max(this.maxX, var1.getMaxX());
            this.maxY = Math.max(this.maxY, var1.getMaxY());
            this.maxZ = Math.max(this.maxZ, var1.getMaxZ());
         }
      }
   }

   public void unionWith(float var1, float var2, float var3, float var4, float var5, float var6) {
      if (!(var4 < var1) && !(var5 < var2) && !(var6 < var3)) {
         if (this.isEmpty()) {
            this.setBounds(var1, var2, var3, var4, var5, var6);
         } else {
            this.minX = Math.min(this.minX, var1);
            this.minY = Math.min(this.minY, var2);
            this.minZ = Math.min(this.minZ, var3);
            this.maxX = Math.max(this.maxX, var4);
            this.maxY = Math.max(this.maxY, var5);
            this.maxZ = Math.max(this.maxZ, var6);
         }
      }
   }

   public void add(float var1, float var2, float var3) {
      this.unionWith(var1, var2, var3, var1, var2, var3);
   }

   public void add(Point2D var1) {
      this.add(var1.x, var1.y, 0.0F);
   }

   public void intersectWith(Rectangle var1) {
      float var2 = (float)var1.x;
      float var3 = (float)var1.y;
      this.intersectWith(var2, var3, 0.0F, var2 + (float)var1.width, var3 + (float)var1.height, 0.0F);
   }

   public void intersectWith(BaseBounds var1) {
      if (!this.isEmpty()) {
         if (var1.isEmpty()) {
            this.makeEmpty();
         } else {
            this.minX = Math.max(this.minX, var1.getMinX());
            this.minY = Math.max(this.minY, var1.getMinY());
            this.minZ = Math.max(this.minZ, var1.getMinZ());
            this.maxX = Math.min(this.maxX, var1.getMaxX());
            this.maxY = Math.min(this.maxY, var1.getMaxY());
            this.maxZ = Math.min(this.maxZ, var1.getMaxZ());
         }
      }
   }

   public void intersectWith(float var1, float var2, float var3, float var4, float var5, float var6) {
      if (!this.isEmpty()) {
         if (!(var4 < var1) && !(var5 < var2) && !(var6 < var3)) {
            this.minX = Math.max(this.minX, var1);
            this.minY = Math.max(this.minY, var2);
            this.minZ = Math.max(this.minZ, var3);
            this.maxX = Math.min(this.maxX, var4);
            this.maxY = Math.min(this.maxY, var5);
            this.maxZ = Math.min(this.maxZ, var6);
         } else {
            this.makeEmpty();
         }
      }
   }

   public boolean contains(Point2D var1) {
      return var1 != null && !this.isEmpty() ? this.contains(var1.x, var1.y, 0.0F) : false;
   }

   public boolean contains(float var1, float var2) {
      return this.isEmpty() ? false : this.contains(var1, var2, 0.0F);
   }

   public boolean contains(float var1, float var2, float var3) {
      if (this.isEmpty()) {
         return false;
      } else {
         return var1 >= this.minX && var1 <= this.maxX && var2 >= this.minY && var2 <= this.maxY && var3 >= this.minZ && var3 <= this.maxZ;
      }
   }

   public boolean contains(float var1, float var2, float var3, float var4, float var5, float var6) {
      if (this.isEmpty()) {
         return false;
      } else {
         return this.contains(var1, var2, var3) && this.contains(var1 + var4, var2 + var5, var3 + var6);
      }
   }

   public boolean intersects(float var1, float var2, float var3, float var4) {
      return this.intersects(var1, var2, 0.0F, var3, var4, 0.0F);
   }

   public boolean intersects(float var1, float var2, float var3, float var4, float var5, float var6) {
      if (this.isEmpty()) {
         return false;
      } else {
         return var1 + var4 >= this.minX && var2 + var5 >= this.minY && var3 + var6 >= this.minZ && var1 <= this.maxX && var2 <= this.maxY && var3 <= this.maxZ;
      }
   }

   public boolean intersects(BaseBounds var1) {
      if (var1 != null && !var1.isEmpty() && !this.isEmpty()) {
         return var1.getMaxX() >= this.minX && var1.getMaxY() >= this.minY && var1.getMaxZ() >= this.minZ && var1.getMinX() <= this.maxX && var1.getMinY() <= this.maxY && var1.getMinZ() <= this.maxZ;
      } else {
         return false;
      }
   }

   public boolean disjoint(float var1, float var2, float var3, float var4) {
      return this.disjoint(var1, var2, 0.0F, var3, var4, 0.0F);
   }

   public boolean disjoint(float var1, float var2, float var3, float var4, float var5, float var6) {
      if (this.isEmpty()) {
         return true;
      } else {
         return var1 + var4 < this.minX || var2 + var5 < this.minY || var3 + var6 < this.minZ || var1 > this.maxX || var2 > this.maxY || var3 > this.maxZ;
      }
   }

   public boolean isEmpty() {
      return this.maxX < this.minX || this.maxY < this.minY || this.maxZ < this.minZ;
   }

   public void roundOut() {
      this.minX = (float)Math.floor((double)this.minX);
      this.minY = (float)Math.floor((double)this.minY);
      this.minZ = (float)Math.floor((double)this.minZ);
      this.maxX = (float)Math.ceil((double)this.maxX);
      this.maxY = (float)Math.ceil((double)this.maxY);
      this.maxZ = (float)Math.ceil((double)this.maxZ);
   }

   public void grow(float var1, float var2, float var3) {
      this.minX -= var1;
      this.maxX += var1;
      this.minY -= var2;
      this.maxY += var2;
      this.minZ -= var3;
      this.maxZ += var3;
   }

   public BaseBounds deriveWithPadding(float var1, float var2, float var3) {
      this.grow(var1, var2, var3);
      return this;
   }

   public BoxBounds makeEmpty() {
      this.minX = this.minY = this.minZ = 0.0F;
      this.maxX = this.maxY = this.maxZ = -1.0F;
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

      if (this.minZ > this.maxZ) {
         var1 = this.maxZ;
         this.maxZ = this.minZ;
         this.minZ = var1;
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
         BoxBounds var2 = (BoxBounds)var1;
         if (this.minX != var2.getMinX()) {
            return false;
         } else if (this.minY != var2.getMinY()) {
            return false;
         } else if (this.minZ != var2.getMinZ()) {
            return false;
         } else if (this.maxX != var2.getMaxX()) {
            return false;
         } else if (this.maxY != var2.getMaxY()) {
            return false;
         } else {
            return this.maxZ == var2.getMaxZ();
         }
      }
   }

   public int hashCode() {
      int var1 = 7;
      var1 = 79 * var1 + Float.floatToIntBits(this.minX);
      var1 = 79 * var1 + Float.floatToIntBits(this.minY);
      var1 = 79 * var1 + Float.floatToIntBits(this.minZ);
      var1 = 79 * var1 + Float.floatToIntBits(this.maxX);
      var1 = 79 * var1 + Float.floatToIntBits(this.maxY);
      var1 = 79 * var1 + Float.floatToIntBits(this.maxZ);
      return var1;
   }

   public String toString() {
      return "BoxBounds { minX:" + this.minX + ", minY:" + this.minY + ", minZ:" + this.minZ + ", maxX:" + this.maxX + ", maxY:" + this.maxY + ", maxZ:" + this.maxZ + "}";
   }
}
