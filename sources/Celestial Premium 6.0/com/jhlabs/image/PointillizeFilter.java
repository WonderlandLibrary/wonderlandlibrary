/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.CellularFilter;
import com.jhlabs.image.ImageMath;

public class PointillizeFilter
extends CellularFilter {
    private float edgeThickness = 0.4f;
    private boolean fadeEdges = false;
    private int edgeColor = -16777216;
    private float fuzziness = 0.1f;

    public PointillizeFilter() {
        this.setScale(16.0f);
        this.setRandomness(0.0f);
    }

    public void setEdgeThickness(float edgeThickness) {
        this.edgeThickness = edgeThickness;
    }

    public float getEdgeThickness() {
        return this.edgeThickness;
    }

    public void setFadeEdges(boolean fadeEdges) {
        this.fadeEdges = fadeEdges;
    }

    public boolean getFadeEdges() {
        return this.fadeEdges;
    }

    public void setEdgeColor(int edgeColor) {
        this.edgeColor = edgeColor;
    }

    public int getEdgeColor() {
        return this.edgeColor;
    }

    public void setFuzziness(float fuzziness) {
        this.fuzziness = fuzziness;
    }

    public float getFuzziness() {
        return this.fuzziness;
    }

    @Override
    public int getPixel(int x, int y, int[] inPixels, int width, int height) {
        float nx = this.m00 * (float)x + this.m01 * (float)y;
        float ny = this.m10 * (float)x + this.m11 * (float)y;
        nx /= this.scale;
        ny /= this.scale * this.stretch;
        float f = this.evaluate(nx += 1000.0f, ny += 1000.0f);
        float f1 = this.results[0].distance;
        int srcx = ImageMath.clamp((int)((this.results[0].x - 1000.0f) * this.scale), 0, width - 1);
        int srcy = ImageMath.clamp((int)((this.results[0].y - 1000.0f) * this.scale), 0, height - 1);
        int v = inPixels[srcy * width + srcx];
        if (this.fadeEdges) {
            float f2 = this.results[1].distance;
            srcx = ImageMath.clamp((int)((this.results[1].x - 1000.0f) * this.scale), 0, width - 1);
            srcy = ImageMath.clamp((int)((this.results[1].y - 1000.0f) * this.scale), 0, height - 1);
            int v2 = inPixels[srcy * width + srcx];
            v = ImageMath.mixColors(0.5f * f1 / f2, v, v2);
        } else {
            f = 1.0f - ImageMath.smoothStep(this.edgeThickness, this.edgeThickness + this.fuzziness, f1);
            v = ImageMath.mixColors(f, this.edgeColor, v);
        }
        return v;
    }

    @Override
    public String toString() {
        return "Pixellate/Pointillize...";
    }
}

