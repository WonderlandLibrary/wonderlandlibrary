/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism;

import com.sun.javafx.geom.Rectangle;
import com.sun.prism.Graphics;
import com.sun.prism.RTTexture;

public interface ReadbackGraphics
extends Graphics {
    public boolean canReadBack();

    public RTTexture readBack(Rectangle var1);

    public void releaseReadBackBuffer(RTTexture var1);
}

