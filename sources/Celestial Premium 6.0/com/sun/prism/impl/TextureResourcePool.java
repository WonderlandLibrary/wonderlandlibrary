/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl;

import com.sun.prism.PixelFormat;
import com.sun.prism.impl.ResourcePool;

public interface TextureResourcePool<T>
extends ResourcePool<T> {
    public long estimateTextureSize(int var1, int var2, PixelFormat var3);

    public long estimateRTTextureSize(int var1, int var2, boolean var3);
}

