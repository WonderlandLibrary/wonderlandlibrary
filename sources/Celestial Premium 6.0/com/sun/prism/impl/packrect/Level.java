/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.packrect;

import com.sun.javafx.geom.Rectangle;

class Level {
    int length;
    int size;
    private int sizeOffset;
    private int lengthOffset;

    Level(int n, int n2, int n3) {
        this.length = n;
        this.size = n2;
        this.sizeOffset = n3;
    }

    boolean add(Rectangle rectangle, int n, int n2, int n3, int n4, boolean bl) {
        if (this.lengthOffset + n3 <= this.length && n4 <= this.size) {
            if (bl) {
                rectangle.x = this.sizeOffset;
                rectangle.y = this.lengthOffset;
            } else {
                rectangle.x = this.lengthOffset;
                rectangle.y = this.sizeOffset;
            }
            this.lengthOffset += n3;
            rectangle.x += n;
            rectangle.y += n2;
            return true;
        }
        return false;
    }
}

