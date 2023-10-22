package com.sun.javafx.geom;

public abstract class BaseBounds {
   BaseBounds() {
   }

   public abstract BaseBounds copy();

   public abstract boolean is2D();

   public abstract BoundsType getBoundsType();

   public abstract float getWidth();

   public abstract float getHeight();

   public abstract float getDepth();

   public abstract float getMinX();

   public abstract float getMinY();

   public abstract float getMinZ();

   public abstract float getMaxX();

   public abstract float getMaxY();

   public abstract float getMaxZ();

   public abstract void translate(float var1, float var2, float var3);

   public abstract Vec2f getMin(Vec2f var1);

   public abstract Vec2f getMax(Vec2f var1);

   public abstract Vec3f getMin(Vec3f var1);

   public abstract Vec3f getMax(Vec3f var1);

   public abstract BaseBounds deriveWithUnion(BaseBounds var1);

   public abstract BaseBounds deriveWithNewBounds(Rectangle var1);

   public abstract BaseBounds deriveWithNewBounds(BaseBounds var1);

   public abstract BaseBounds deriveWithNewBounds(float var1, float var2, float var3, float var4, float var5, float var6);

   public abstract BaseBounds deriveWithNewBoundsAndSort(float var1, float var2, float var3, float var4, float var5, float var6);

   public abstract BaseBounds deriveWithPadding(float var1, float var2, float var3);

   public abstract void intersectWith(Rectangle var1);

   public abstract void intersectWith(BaseBounds var1);

   public abstract void intersectWith(float var1, float var2, float var3, float var4, float var5, float var6);

   public abstract void setBoundsAndSort(Point2D var1, Point2D var2);

   public abstract void setBoundsAndSort(float var1, float var2, float var3, float var4, float var5, float var6);

   public abstract void add(Point2D var1);

   public abstract void add(float var1, float var2, float var3);

   public abstract boolean contains(Point2D var1);

   public abstract boolean contains(float var1, float var2);

   public abstract boolean intersects(float var1, float var2, float var3, float var4);

   public abstract boolean isEmpty();

   public abstract void roundOut();

   public abstract RectBounds flattenInto(RectBounds var1);

   public abstract BaseBounds makeEmpty();

   public abstract boolean disjoint(float var1, float var2, float var3, float var4);

   protected abstract void sortMinMax();

   public static BaseBounds getInstance(float var0, float var1, float var2, float var3, float var4, float var5) {
      return (BaseBounds)(var2 == 0.0F && var5 == 0.0F ? getInstance(var0, var1, var3, var4) : new BoxBounds(var0, var1, var2, var3, var4, var5));
   }

   public static BaseBounds getInstance(float var0, float var1, float var2, float var3) {
      return new RectBounds(var0, var1, var2, var3);
   }

   public static enum BoundsType {
      RECTANGLE,
      BOX;
   }
}
