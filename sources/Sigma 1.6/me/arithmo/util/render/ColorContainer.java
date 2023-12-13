/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.Expose
 */
package me.arithmo.util.render;

import com.google.gson.annotations.Expose;
import me.arithmo.util.render.Colors;

public class ColorContainer {
    @Expose
    private int red;
    @Expose
    private int green;
    @Expose
    private int blue;
    @Expose
    private int alpha;

    public ColorContainer(int r, int g, int b, int a) {
        this.red = r;
        this.green = g;
        this.blue = b;
        this.alpha = a;
    }

    public ColorContainer(int r, int g, int b) {
        this(r, g, b, 225);
    }

    public ColorContainer(int b) {
        this(b, b, b);
    }

    public ColorContainer(int b, int a) {
        this(b, b, b, a);
    }

    public void setColor(int r, int g, int b, int a) {
        this.red = r;
        this.green = g;
        this.blue = b;
        this.alpha = a;
    }

    public void setRed(int red) {
        this.red = red;
        this.setColor(red, this.green, this.blue, this.alpha);
    }

    public void setGreen(int green) {
        this.green = green;
        this.setColor(this.red, green, this.blue, this.alpha);
    }

    public void setBlue(int blue) {
        this.blue = blue;
        this.setColor(this.red, this.green, blue, this.alpha);
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
        this.setColor(this.red, this.green, this.blue, alpha);
    }

    public int getRed() {
        return this.red;
    }

    public int getGreen() {
        return this.green;
    }

    public int getBlue() {
        return this.blue;
    }

    public int getAlpha() {
        return this.alpha;
    }

    public int getIntCol() {
        return Colors.getColor(this.red, this.green, this.blue, this.alpha);
    }
}

