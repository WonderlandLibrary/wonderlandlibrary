/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.util;

import me.kiras.aimwhere.libraries.slick.Color;
import me.kiras.aimwhere.libraries.slick.Image;

public class LocatedImage {
    private Image image;
    private int x;
    private int y;
    private Color filter = Color.white;
    private float width;
    private float height;

    public LocatedImage(Image image2, int x, int y) {
        this.image = image2;
        this.x = x;
        this.y = y;
        this.width = image2.getWidth();
        this.height = image2.getHeight();
    }

    public float getHeight() {
        return this.height;
    }

    public float getWidth() {
        return this.width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setColor(Color c) {
        this.filter = c;
    }

    public Color getColor() {
        return this.filter;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void draw() {
        this.image.draw(this.x, this.y, this.width, this.height, this.filter);
    }
}

