/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.text;

import com.sun.javafx.scene.text.TextLayout;

public interface TextLayoutFactory {
    public TextLayout createLayout();

    public TextLayout getLayout();

    public void disposeLayout(TextLayout var1);
}

