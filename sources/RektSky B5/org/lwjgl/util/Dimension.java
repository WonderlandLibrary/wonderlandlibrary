/*
 * Decompiled with CFR 0.152.
 */
package org.lwjgl.util;

import java.io.Serializable;
import org.lwjgl.util.ReadableDimension;
import org.lwjgl.util.WritableDimension;

public final class Dimension
implements Serializable,
ReadableDimension,
WritableDimension {
    static final long serialVersionUID = 1L;
    private int width;
    private int height;

    public Dimension() {
    }

    public Dimension(int w2, int h2) {
        this.width = w2;
        this.height = h2;
    }

    public Dimension(ReadableDimension d2) {
        this.setSize(d2);
    }

    public void setSize(int w2, int h2) {
        this.width = w2;
        this.height = h2;
    }

    public void setSize(ReadableDimension d2) {
        this.width = d2.getWidth();
        this.height = d2.getHeight();
    }

    public void getSize(WritableDimension dest) {
        dest.setSize(this);
    }

    public boolean equals(Object obj) {
        if (obj instanceof ReadableDimension) {
            ReadableDimension d2 = (ReadableDimension)obj;
            return this.width == d2.getWidth() && this.height == d2.getHeight();
        }
        return false;
    }

    public int hashCode() {
        int sum = this.width + this.height;
        return sum * (sum + 1) / 2 + this.width;
    }

    public String toString() {
        return this.getClass().getName() + "[width=" + this.width + ",height=" + this.height + "]";
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}

