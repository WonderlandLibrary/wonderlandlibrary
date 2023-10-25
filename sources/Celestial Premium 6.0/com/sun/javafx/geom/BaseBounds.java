/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.BoxBounds;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Vec2f;
import com.sun.javafx.geom.Vec3f;

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

    public static BaseBounds getInstance(float f, float f2, float f3, float f4, float f5, float f6) {
        if (f3 == 0.0f && f6 == 0.0f) {
            return BaseBounds.getInstance(f, f2, f4, f5);
        }
        return new BoxBounds(f, f2, f3, f4, f5, f6);
    }

    public static BaseBounds getInstance(float f, float f2, float f3, float f4) {
        return new RectBounds(f, f2, f3, f4);
    }

    public static final class BoundsType
    extends Enum<BoundsType> {
        public static final /* enum */ BoundsType RECTANGLE = new BoundsType();
        public static final /* enum */ BoundsType BOX = new BoundsType();
        private static final /* synthetic */ BoundsType[] $VALUES;

        public static BoundsType[] values() {
            return (BoundsType[])$VALUES.clone();
        }

        public static BoundsType valueOf(String string) {
            return Enum.valueOf(BoundsType.class, string);
        }

        private static /* synthetic */ BoundsType[] $values() {
            return new BoundsType[]{RECTANGLE, BOX};
        }

        static {
            $VALUES = BoundsType.$values();
        }
    }
}

