/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism;

import com.sun.prism.GraphicsResource;

public interface Mesh
extends GraphicsResource {
    public boolean buildGeometry(boolean var1, float[] var2, int[] var3, float[] var4, int[] var5, float[] var6, int[] var7, int[] var8, int[] var9, int[] var10, int[] var11);

    public int getCount();

    public boolean isValid();
}

