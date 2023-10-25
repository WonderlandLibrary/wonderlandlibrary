/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism;

import com.sun.prism.Graphics;
import com.sun.prism.RTTexture;

public interface MaskTextureGraphics
extends Graphics {
    public void drawPixelsMasked(RTTexture var1, RTTexture var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10);

    public void maskInterpolatePixels(RTTexture var1, RTTexture var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10);
}

