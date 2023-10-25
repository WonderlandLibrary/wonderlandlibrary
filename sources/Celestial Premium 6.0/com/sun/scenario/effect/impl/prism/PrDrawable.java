/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.prism;

import com.sun.prism.Graphics;
import com.sun.prism.RTTexture;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.impl.ImagePool;
import com.sun.scenario.effect.impl.PoolFilterable;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.prism.PrRenderer;
import com.sun.scenario.effect.impl.prism.PrTexture;
import java.lang.ref.WeakReference;

public abstract class PrDrawable
extends PrTexture<RTTexture>
implements PoolFilterable {
    private WeakReference<ImagePool> pool;

    public static PrDrawable create(FilterContext filterContext, RTTexture rTTexture) {
        return ((PrRenderer)Renderer.getRenderer(filterContext)).createDrawable(rTTexture);
    }

    protected PrDrawable(RTTexture rTTexture) {
        super(rTTexture);
    }

    @Override
    public void setImagePool(ImagePool imagePool) {
        this.pool = new WeakReference<ImagePool>(imagePool);
    }

    @Override
    public ImagePool getImagePool() {
        return this.pool == null ? null : (ImagePool)this.pool.get();
    }

    @Override
    public float getPixelScale() {
        return 1.0f;
    }

    @Override
    public int getMaxContentWidth() {
        return ((RTTexture)this.getTextureObject()).getMaxContentWidth();
    }

    @Override
    public int getMaxContentHeight() {
        return ((RTTexture)this.getTextureObject()).getMaxContentHeight();
    }

    @Override
    public void setContentWidth(int n) {
        ((RTTexture)this.getTextureObject()).setContentWidth(n);
    }

    @Override
    public void setContentHeight(int n) {
        ((RTTexture)this.getTextureObject()).setContentHeight(n);
    }

    public abstract Graphics createGraphics();

    public void clear() {
        Graphics graphics = this.createGraphics();
        graphics.clear();
    }
}

