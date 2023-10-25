/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.d3d;

import com.sun.prism.Graphics;
import com.sun.prism.Material;
import com.sun.prism.d3d.D3DContext;
import com.sun.prism.d3d.D3DMesh;
import com.sun.prism.d3d.D3DPhongMaterial;
import com.sun.prism.impl.BaseMeshView;
import com.sun.prism.impl.Disposer;

class D3DMeshView
extends BaseMeshView {
    static int count = 0;
    private final D3DContext context;
    private final long nativeHandle;
    private final D3DMesh mesh;
    private D3DPhongMaterial material;

    private D3DMeshView(D3DContext d3DContext, long l, D3DMesh d3DMesh, Disposer.Record record) {
        super(record);
        this.context = d3DContext;
        this.mesh = d3DMesh;
        this.nativeHandle = l;
        ++count;
    }

    static D3DMeshView create(D3DContext d3DContext, D3DMesh d3DMesh) {
        long l = d3DContext.createD3DMeshView(d3DMesh.getNativeHandle());
        return new D3DMeshView(d3DContext, l, d3DMesh, new D3DMeshViewDisposerRecord(d3DContext, l));
    }

    @Override
    public void setCullingMode(int n) {
        this.context.setCullingMode(this.nativeHandle, n);
    }

    @Override
    public void setMaterial(Material material) {
        this.context.setMaterial(this.nativeHandle, ((D3DPhongMaterial)material).getNativeHandle());
        this.material = (D3DPhongMaterial)material;
    }

    @Override
    public void setWireframe(boolean bl) {
        this.context.setWireframe(this.nativeHandle, bl);
    }

    @Override
    public void setAmbientLight(float f, float f2, float f3) {
        this.context.setAmbientLight(this.nativeHandle, f, f2, f3);
    }

    @Override
    public void setLight(int n, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, float f17) {
        if (n >= 0 && n <= 2) {
            this.context.setLight(this.nativeHandle, n, f, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17);
        }
    }

    @Override
    public void render(Graphics graphics) {
        this.material.lockTextureMaps();
        this.context.renderMeshView(this.nativeHandle, graphics);
        this.material.unlockTextureMaps();
    }

    @Override
    public boolean isValid() {
        return !this.context.isDisposed();
    }

    @Override
    public void dispose() {
        this.material = null;
        this.disposerRecord.dispose();
        --count;
    }

    public int getCount() {
        return count;
    }

    static class D3DMeshViewDisposerRecord
    implements Disposer.Record {
        private final D3DContext context;
        private long nativeHandle;

        D3DMeshViewDisposerRecord(D3DContext d3DContext, long l) {
            this.context = d3DContext;
            this.nativeHandle = l;
        }

        void traceDispose() {
        }

        @Override
        public void dispose() {
            if (this.nativeHandle != 0L) {
                this.traceDispose();
                this.context.releaseD3DMeshView(this.nativeHandle);
                this.nativeHandle = 0L;
            }
        }
    }
}

