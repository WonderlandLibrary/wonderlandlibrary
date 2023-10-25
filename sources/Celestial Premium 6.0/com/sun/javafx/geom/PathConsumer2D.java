/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

public interface PathConsumer2D {
    public void moveTo(float var1, float var2);

    public void lineTo(float var1, float var2);

    public void quadTo(float var1, float var2, float var3, float var4);

    public void curveTo(float var1, float var2, float var3, float var4, float var5, float var6);

    public void closePath();

    public void pathDone();
}

