/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.Shape;

public abstract class ShapePair
extends Shape {
    public static final int INCLUDES_O_NOT_I = 1;
    public static final int INCLUDES_I_NOT_O = 2;
    public static final int INCLUDES_O_AND_I = 4;
    public static final int INCLUDES_NEITHER = 8;
    public static final int TYPE_SUBTRACT = 1;
    public static final int TYPE_INTERSECT = 4;

    public abstract int getCombinationType();

    public abstract Shape getOuterShape();

    public abstract Shape getInnerShape();
}

