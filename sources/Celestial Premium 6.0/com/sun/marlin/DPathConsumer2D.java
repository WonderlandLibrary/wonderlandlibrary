/*
 * Decompiled with CFR 0.150.
 */
package com.sun.marlin;

public interface DPathConsumer2D {
    public void moveTo(double var1, double var3);

    public void lineTo(double var1, double var3);

    public void quadTo(double var1, double var3, double var5, double var7);

    public void curveTo(double var1, double var3, double var5, double var7, double var9, double var11);

    public void closePath();

    public void pathDone();
}

