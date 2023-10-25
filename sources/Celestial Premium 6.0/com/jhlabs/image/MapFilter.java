/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.TransformFilter;
import com.jhlabs.math.Function2D;

public class MapFilter
extends TransformFilter {
    private Function2D xMapFunction;
    private Function2D yMapFunction;

    public void setXMapFunction(Function2D xMapFunction) {
        this.xMapFunction = xMapFunction;
    }

    public Function2D getXMapFunction() {
        return this.xMapFunction;
    }

    public void setYMapFunction(Function2D yMapFunction) {
        this.yMapFunction = yMapFunction;
    }

    public Function2D getYMapFunction() {
        return this.yMapFunction;
    }

    @Override
    protected void transformInverse(int x, int y, float[] out) {
        float xMap = this.xMapFunction.evaluate(x, y);
        float yMap = this.yMapFunction.evaluate(x, y);
        out[0] = xMap * (float)this.transformedSpace.width;
        out[1] = yMap * (float)this.transformedSpace.height;
    }

    public String toString() {
        return "Distort/Map Coordinates...";
    }
}

