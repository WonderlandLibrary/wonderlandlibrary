/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Shape;
import com.sun.prism.Graphics;

public interface ShapeRep {
    public boolean is3DCapable();

    public void invalidate(InvalidationType var1);

    public void fill(Graphics var1, Shape var2, BaseBounds var3);

    public void draw(Graphics var1, Shape var2, BaseBounds var3);

    public void dispose();

    public static final class InvalidationType
    extends Enum<InvalidationType> {
        public static final /* enum */ InvalidationType LOCATION = new InvalidationType();
        public static final /* enum */ InvalidationType LOCATION_AND_GEOMETRY = new InvalidationType();
        private static final /* synthetic */ InvalidationType[] $VALUES;

        public static InvalidationType[] values() {
            return (InvalidationType[])$VALUES.clone();
        }

        public static InvalidationType valueOf(String string) {
            return Enum.valueOf(InvalidationType.class, string);
        }

        private static /* synthetic */ InvalidationType[] $values() {
            return new InvalidationType[]{LOCATION, LOCATION_AND_GEOMETRY};
        }

        static {
            $VALUES = InvalidationType.$values();
        }
    }
}

