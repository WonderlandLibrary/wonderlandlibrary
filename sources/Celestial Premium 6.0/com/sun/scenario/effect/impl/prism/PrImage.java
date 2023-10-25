/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.prism;

import com.sun.prism.Image;
import com.sun.scenario.effect.Filterable;

public class PrImage
implements Filterable {
    private final Image image;

    private PrImage(Image image) {
        this.image = image;
    }

    public static PrImage create(Image image) {
        return new PrImage(image);
    }

    public Image getImage() {
        return this.image;
    }

    @Override
    public Object getData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getContentWidth() {
        return this.image.getWidth();
    }

    @Override
    public int getContentHeight() {
        return this.image.getHeight();
    }

    @Override
    public int getPhysicalWidth() {
        return this.image.getWidth();
    }

    @Override
    public int getPhysicalHeight() {
        return this.image.getHeight();
    }

    @Override
    public float getPixelScale() {
        return this.image.getPixelScale();
    }

    @Override
    public int getMaxContentWidth() {
        return this.image.getWidth();
    }

    @Override
    public int getMaxContentHeight() {
        return this.image.getHeight();
    }

    @Override
    public void setContentWidth(int n) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void setContentHeight(int n) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void lock() {
    }

    @Override
    public void unlock() {
    }

    @Override
    public boolean isLost() {
        return false;
    }

    @Override
    public void flush() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

