/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.d3d;

import com.sun.glass.ui.Screen;
import com.sun.javafx.geom.Rectangle;
import com.sun.prism.CompositeMode;
import com.sun.prism.Graphics;
import com.sun.prism.Presentable;
import com.sun.prism.PresentableState;
import com.sun.prism.RTTexture;
import com.sun.prism.d3d.D3DContext;
import com.sun.prism.d3d.D3DContextSource;
import com.sun.prism.d3d.D3DGraphics;
import com.sun.prism.d3d.D3DRTTexture;
import com.sun.prism.d3d.D3DRenderTarget;
import com.sun.prism.d3d.D3DResource;
import com.sun.prism.d3d.D3DResourceFactory;

class D3DSwapChain
extends D3DResource
implements D3DRenderTarget,
Presentable,
D3DContextSource {
    private final D3DRTTexture texBackBuffer;
    private final float pixelScaleFactorX;
    private final float pixelScaleFactorY;

    D3DSwapChain(D3DContext d3DContext, long l, D3DRTTexture d3DRTTexture, float f, float f2) {
        super(new D3DResource.D3DRecord(d3DContext, l));
        this.texBackBuffer = d3DRTTexture;
        this.pixelScaleFactorX = f;
        this.pixelScaleFactorY = f2;
    }

    @Override
    public void dispose() {
        this.texBackBuffer.dispose();
        super.dispose();
    }

    @Override
    public boolean prepare(Rectangle rectangle) {
        D3DContext d3DContext = this.getContext();
        d3DContext.flushVertexBuffer();
        D3DGraphics d3DGraphics = (D3DGraphics)D3DGraphics.create(this, d3DContext);
        if (d3DGraphics == null) {
            return false;
        }
        int n = this.texBackBuffer.getContentWidth();
        int n2 = this.texBackBuffer.getContentHeight();
        int n3 = this.getContentWidth();
        int n4 = this.getContentHeight();
        if (this.isMSAA()) {
            d3DContext.flushVertexBuffer();
            d3DGraphics.blit(this.texBackBuffer, null, 0, 0, n, n2, 0, 0, n3, n4);
        } else {
            d3DGraphics.setCompositeMode(CompositeMode.SRC);
            d3DGraphics.drawTexture(this.texBackBuffer, 0.0f, 0.0f, n3, n4, 0.0f, 0.0f, n, n2);
        }
        d3DContext.flushVertexBuffer();
        this.texBackBuffer.unlock();
        return true;
    }

    @Override
    public boolean present() {
        D3DContext d3DContext = this.getContext();
        if (d3DContext.isDisposed()) {
            return false;
        }
        int n = D3DSwapChain.nPresent(d3DContext.getContextHandle(), this.d3dResRecord.getResource());
        return d3DContext.validatePresent(n);
    }

    @Override
    public long getResourceHandle() {
        return this.d3dResRecord.getResource();
    }

    @Override
    public int getPhysicalWidth() {
        return D3DResourceFactory.nGetTextureWidth(this.d3dResRecord.getResource());
    }

    @Override
    public int getPhysicalHeight() {
        return D3DResourceFactory.nGetTextureHeight(this.d3dResRecord.getResource());
    }

    @Override
    public int getContentWidth() {
        return this.getPhysicalWidth();
    }

    @Override
    public int getContentHeight() {
        return this.getPhysicalHeight();
    }

    @Override
    public int getContentX() {
        return 0;
    }

    @Override
    public int getContentY() {
        return 0;
    }

    private static native int nPresent(long var0, long var2);

    @Override
    public D3DContext getContext() {
        return this.d3dResRecord.getContext();
    }

    @Override
    public boolean lockResources(PresentableState presentableState) {
        if (presentableState.getRenderWidth() != this.texBackBuffer.getContentWidth() || presentableState.getRenderHeight() != this.texBackBuffer.getContentHeight() || presentableState.getRenderScaleX() != this.pixelScaleFactorX || presentableState.getRenderScaleY() != this.pixelScaleFactorY) {
            return true;
        }
        this.texBackBuffer.lock();
        return this.texBackBuffer.isSurfaceLost();
    }

    @Override
    public Graphics createGraphics() {
        Graphics graphics = D3DGraphics.create(this.texBackBuffer, this.getContext());
        graphics.scale(this.pixelScaleFactorX, this.pixelScaleFactorY);
        return graphics;
    }

    public RTTexture getRTTBackBuffer() {
        return this.texBackBuffer;
    }

    @Override
    public Screen getAssociatedScreen() {
        return this.getContext().getAssociatedScreen();
    }

    @Override
    public float getPixelScaleFactorX() {
        return this.pixelScaleFactorX;
    }

    @Override
    public float getPixelScaleFactorY() {
        return this.pixelScaleFactorY;
    }

    @Override
    public boolean isOpaque() {
        return this.texBackBuffer.isOpaque();
    }

    @Override
    public void setOpaque(boolean bl) {
        this.texBackBuffer.setOpaque(bl);
    }

    @Override
    public boolean isMSAA() {
        return this.texBackBuffer != null ? this.texBackBuffer.isMSAA() : false;
    }
}

