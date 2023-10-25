/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.prism;

import com.sun.javafx.geom.Rectangle;
import com.sun.prism.Texture;
import com.sun.scenario.effect.LockableResource;

public class PrTexture<T extends Texture>
implements LockableResource {
    private final T tex;
    private final Rectangle bounds;

    public PrTexture(T t) {
        if (t == null) {
            throw new IllegalArgumentException("Texture must be non-null");
        }
        this.tex = t;
        this.bounds = new Rectangle(t.getPhysicalWidth(), t.getPhysicalHeight());
    }

    @Override
    public void lock() {
        if (this.tex != null) {
            this.tex.lock();
        }
    }

    @Override
    public void unlock() {
        if (this.tex != null) {
            this.tex.unlock();
        }
    }

    @Override
    public boolean isLost() {
        return this.tex.isSurfaceLost();
    }

    public Rectangle getNativeBounds() {
        return this.bounds;
    }

    public T getTextureObject() {
        return this.tex;
    }
}

