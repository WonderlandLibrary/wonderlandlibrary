/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism;

import com.sun.glass.ui.Screen;
import com.sun.prism.Graphics;
import com.sun.prism.Surface;

public interface RenderTarget
extends Surface {
    public Screen getAssociatedScreen();

    public Graphics createGraphics();

    public boolean isOpaque();

    public void setOpaque(boolean var1);

    public boolean isMSAA();
}

