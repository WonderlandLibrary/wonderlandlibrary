/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.util;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class LocatedImage {
    private Image image;
    private int x;
    private int y;
    private Color filter = Color.white;
    private float width;
    private float height;

    public LocatedImage(Image image, int x2, int y2) {
        this.image = image;
        this.x = x2;
        this.y = y2;
        this.width = image.getWidth();
        this.height = image.getHeight();
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

    public void setColor(Color c2) {
        this.filter = c2;
    }

    public Color getColor() {
        return this.filter;
    }

    public void setX(int x2) {
        this.x = x2;
    }

    public void setY(int y2) {
        this.y = y2;
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

