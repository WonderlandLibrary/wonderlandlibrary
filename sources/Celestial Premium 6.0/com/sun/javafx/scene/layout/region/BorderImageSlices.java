/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.layout.BorderWidths
 */
package com.sun.javafx.scene.layout.region;

import javafx.scene.layout.BorderWidths;

public class BorderImageSlices {
    public static final BorderImageSlices EMPTY = new BorderImageSlices(BorderWidths.EMPTY, false);
    public static final BorderImageSlices DEFAULT = new BorderImageSlices(BorderWidths.FULL, false);
    public BorderWidths widths;
    public boolean filled;

    public BorderImageSlices(BorderWidths borderWidths, boolean bl) {
        this.widths = borderWidths;
        this.filled = bl;
    }
}

