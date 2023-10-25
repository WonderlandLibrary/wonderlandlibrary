/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.paint;

import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.impl.paint.LinearGradientContext;
import com.sun.prism.impl.paint.MultipleGradientContext;
import com.sun.prism.impl.paint.RadialGradientContext;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Gradient;
import com.sun.prism.paint.LinearGradient;
import com.sun.prism.paint.Paint;
import com.sun.prism.paint.RadialGradient;
import com.sun.prism.paint.Stop;

public class PaintUtil {
    private static final Affine2D gradXform = new Affine2D();

    public static void fillImageWithGradient(int[] arrn, Gradient gradient, BaseTransform baseTransform, int n, int n2, int n3, int n4, float f, float f2, float f3, float f4) {
        MultipleGradientContext multipleGradientContext;
        Object object;
        Gradient gradient2 = gradient;
        int n5 = gradient2.getNumStops();
        float[] arrf = new float[n5];
        Color[] arrcolor = new Color[n5];
        for (int i = 0; i < n5; ++i) {
            object = gradient2.getStops().get(i);
            arrf[i] = ((Stop)object).getOffset();
            arrcolor[i] = ((Stop)object).getColor();
        }
        if (gradient.getType() == Paint.Type.LINEAR_GRADIENT) {
            float f5;
            float f6;
            float f7;
            float f8;
            object = (LinearGradient)gradient;
            if (((Paint)object).isProportional()) {
                f8 = ((LinearGradient)object).getX1() * f3 + f;
                f7 = ((LinearGradient)object).getY1() * f4 + f2;
                f6 = ((LinearGradient)object).getX2() * f3 + f;
                f5 = ((LinearGradient)object).getY2() * f4 + f2;
            } else {
                f8 = ((LinearGradient)object).getX1();
                f7 = ((LinearGradient)object).getY1();
                f6 = ((LinearGradient)object).getX2();
                f5 = ((LinearGradient)object).getY2();
            }
            if (f8 == f6 && f7 == f5) {
                f8 -= 1.0E-6f;
                f6 += 1.0E-6f;
            }
            multipleGradientContext = new LinearGradientContext((LinearGradient)object, baseTransform, f8, f7, f6, f5, arrf, arrcolor, ((Gradient)object).getSpreadMethod());
        } else {
            float f9;
            float f10;
            object = (RadialGradient)gradient;
            gradXform.setTransform(baseTransform);
            float f11 = ((RadialGradient)object).getRadius();
            float f12 = ((RadialGradient)object).getCenterX();
            float f13 = ((RadialGradient)object).getCenterY();
            double d = Math.toRadians(((RadialGradient)object).getFocusAngle());
            float f14 = ((RadialGradient)object).getFocusDistance();
            if (((Paint)object).isProportional()) {
                f10 = f + f3 / 2.0f;
                f9 = f2 + f4 / 2.0f;
                float f15 = Math.min(f3, f4);
                f12 = (f12 - 0.5f) * f15 + f10;
                f13 = (f13 - 0.5f) * f15 + f9;
                if (f3 != f4 && f3 != 0.0f && f4 != 0.0f) {
                    gradXform.translate(f10, f9);
                    gradXform.scale(f3 / f15, f4 / f15);
                    gradXform.translate(-f10, -f9);
                }
                f11 *= f15;
            }
            if (f11 <= 0.0f) {
                f11 = 0.001f;
            }
            f10 = (float)((double)f12 + (double)(f14 *= f11) * Math.cos(d));
            f9 = (float)((double)f13 + (double)f14 * Math.sin(d));
            multipleGradientContext = new RadialGradientContext((RadialGradient)object, gradXform, f12, f13, f11, f10, f9, arrf, arrcolor, ((Gradient)object).getSpreadMethod());
        }
        ((MultipleGradientContext)multipleGradientContext).fillRaster(arrn, 0, 0, n, n2, n3, n4);
    }
}

