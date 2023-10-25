/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.d3d;

import com.sun.javafx.geom.transform.Affine3D;
import com.sun.prism.CompositeMode;
import com.sun.prism.Graphics;
import com.sun.prism.RenderTarget;
import com.sun.prism.d3d.D3DContext;
import com.sun.prism.d3d.D3DContextSource;
import com.sun.prism.d3d.D3DRenderTarget;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.ps.BaseShaderGraphics;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;

class D3DGraphics
extends BaseShaderGraphics
implements D3DContextSource {
    private D3DContext context;

    private D3DGraphics(D3DContext d3DContext, RenderTarget renderTarget) {
        super(d3DContext, renderTarget);
        this.context = d3DContext;
    }

    @Override
    public void getPaintShaderTransform(Affine3D affine3D) {
        super.getPaintShaderTransform(affine3D);
        affine3D.preTranslate(-0.5, -0.5, 0.0);
    }

    static Graphics create(RenderTarget renderTarget, D3DContext d3DContext) {
        if (renderTarget == null) {
            return null;
        }
        long l = ((D3DRenderTarget)((Object)renderTarget)).getResourceHandle();
        if (l == 0L) {
            return null;
        }
        if (PrismSettings.verbose && d3DContext.isLost()) {
            System.err.println("Create graphics while the device is lost");
        }
        return new D3DGraphics(d3DContext, renderTarget);
    }

    @Override
    public void clearQuad(float f, float f2, float f3, float f4) {
        this.context.setRenderTarget(this);
        this.context.flushVertexBuffer();
        CompositeMode compositeMode = this.getCompositeMode();
        this.setCompositeMode(CompositeMode.CLEAR);
        Paint paint = this.getPaint();
        this.setPaint(Color.BLACK);
        this.fillQuad(f, f2, f3, f4);
        this.context.flushVertexBuffer();
        this.setPaint(paint);
        this.setCompositeMode(compositeMode);
    }

    @Override
    public void clear(Color color) {
        this.context.validateClearOp(this);
        this.getRenderTarget().setOpaque(color.isOpaque());
        int n = D3DGraphics.nClear(this.context.getContextHandle(), color.getIntArgbPre(), this.isDepthBuffer(), false);
        D3DContext.validate(n);
    }

    @Override
    public void sync() {
        this.context.flushVertexBuffer();
    }

    @Override
    public D3DContext getContext() {
        return this.context;
    }

    private static native int nClear(long var0, int var2, boolean var3, boolean var4);
}

