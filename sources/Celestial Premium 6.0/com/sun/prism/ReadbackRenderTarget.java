/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism;

import com.sun.prism.RenderTarget;
import com.sun.prism.Texture;

public interface ReadbackRenderTarget
extends RenderTarget {
    public Texture getBackBuffer();
}

