/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism;

import com.sun.prism.RenderTarget;
import com.sun.prism.Texture;
import java.nio.Buffer;

public interface RTTexture
extends Texture,
RenderTarget {
    public int[] getPixels();

    public boolean readPixels(Buffer var1);

    public boolean readPixels(Buffer var1, int var2, int var3, int var4, int var5);

    public boolean isVolatile();
}

