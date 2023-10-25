/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Graphics;
import com.sun.prism.impl.shape.BasicShapeRep;

public class BasicRoundRectRep
extends BasicShapeRep {
    private static final float[] TMP_ARR = new float[4];

    @Override
    public void fill(Graphics graphics, Shape shape, BaseBounds baseBounds) {
        BasicRoundRectRep.fillRoundRect(graphics, (RoundRectangle2D)shape);
    }

    public static void fillRoundRect(Graphics graphics, RoundRectangle2D roundRectangle2D) {
        if (roundRectangle2D.width <= 0.0f || roundRectangle2D.height <= 0.0f) {
            return;
        }
        float f = roundRectangle2D.arcWidth;
        float f2 = roundRectangle2D.arcHeight;
        if (f > 0.0f && f2 > 0.0f) {
            graphics.fillRoundRect(roundRectangle2D.x, roundRectangle2D.y, roundRectangle2D.width, roundRectangle2D.height, f, f2);
        } else if (BasicRoundRectRep.isAARequiredForFill(graphics, roundRectangle2D)) {
            graphics.fillRect(roundRectangle2D.x, roundRectangle2D.y, roundRectangle2D.width, roundRectangle2D.height);
        } else {
            graphics.fillQuad(roundRectangle2D.x, roundRectangle2D.y, roundRectangle2D.x + roundRectangle2D.width, roundRectangle2D.y + roundRectangle2D.height);
        }
    }

    @Override
    public void draw(Graphics graphics, Shape shape, BaseBounds baseBounds) {
        BasicRoundRectRep.drawRoundRect(graphics, (RoundRectangle2D)shape);
    }

    public static void drawRoundRect(Graphics graphics, RoundRectangle2D roundRectangle2D) {
        float f = roundRectangle2D.arcWidth;
        float f2 = roundRectangle2D.arcHeight;
        if (f > 0.0f && f2 > 0.0f) {
            graphics.drawRoundRect(roundRectangle2D.x, roundRectangle2D.y, roundRectangle2D.width, roundRectangle2D.height, f, f2);
        } else {
            graphics.drawRect(roundRectangle2D.x, roundRectangle2D.y, roundRectangle2D.width, roundRectangle2D.height);
        }
    }

    private static boolean notIntEnough(float f) {
        return (double)Math.abs(f - (float)Math.round(f)) > 0.06;
    }

    private static boolean notOnIntGrid(float f, float f2, float f3, float f4) {
        return BasicRoundRectRep.notIntEnough(f) || BasicRoundRectRep.notIntEnough(f2) || BasicRoundRectRep.notIntEnough(f3) || BasicRoundRectRep.notIntEnough(f4);
    }

    protected static boolean isAARequiredForFill(Graphics graphics, RoundRectangle2D roundRectangle2D) {
        boolean bl;
        BaseTransform baseTransform = graphics.getTransformNoClone();
        long l = baseTransform.getType();
        boolean bl2 = bl = (l & 0xFFFFFFFFFFFFFFF0L) != 0L;
        if (bl) {
            return true;
        }
        if (baseTransform == null || baseTransform.isIdentity()) {
            return BasicRoundRectRep.notOnIntGrid(roundRectangle2D.x, roundRectangle2D.y, roundRectangle2D.x + roundRectangle2D.width, roundRectangle2D.y + roundRectangle2D.height);
        }
        BasicRoundRectRep.TMP_ARR[0] = roundRectangle2D.x;
        BasicRoundRectRep.TMP_ARR[1] = roundRectangle2D.y;
        BasicRoundRectRep.TMP_ARR[2] = roundRectangle2D.x + roundRectangle2D.width;
        BasicRoundRectRep.TMP_ARR[3] = roundRectangle2D.y + roundRectangle2D.height;
        baseTransform.transform(TMP_ARR, 0, TMP_ARR, 0, 2);
        return BasicRoundRectRep.notOnIntGrid(TMP_ARR[0], TMP_ARR[1], TMP_ARR[2], TMP_ARR[3]);
    }
}

