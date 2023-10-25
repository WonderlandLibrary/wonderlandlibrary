/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.d3d;

import com.sun.prism.d3d.D3DContext;
import com.sun.prism.impl.BaseMesh;
import com.sun.prism.impl.Disposer;

class D3DMesh
extends BaseMesh {
    static int count = 0;
    private final D3DContext context;
    private final long nativeHandle;

    private D3DMesh(D3DContext d3DContext, long l, Disposer.Record record) {
        super(record);
        this.context = d3DContext;
        this.nativeHandle = l;
        ++count;
    }

    static D3DMesh create(D3DContext d3DContext) {
        long l = d3DContext.createD3DMesh();
        return new D3DMesh(d3DContext, l, new D3DMeshDisposerRecord(d3DContext, l));
    }

    long getNativeHandle() {
        return this.nativeHandle;
    }

    @Override
    public boolean isValid() {
        return !this.context.isDisposed();
    }

    @Override
    public void dispose() {
        this.disposerRecord.dispose();
        --count;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public boolean buildNativeGeometry(float[] arrf, int n, int[] arrn, int n2) {
        return this.context.buildNativeGeometry(this.nativeHandle, arrf, n, arrn, n2);
    }

    @Override
    public boolean buildNativeGeometry(float[] arrf, int n, short[] arrs, int n2) {
        return this.context.buildNativeGeometry(this.nativeHandle, arrf, n, arrs, n2);
    }

    static class D3DMeshDisposerRecord
    implements Disposer.Record {
        private final D3DContext context;
        private long nativeHandle;

        D3DMeshDisposerRecord(D3DContext d3DContext, long l) {
            this.context = d3DContext;
            this.nativeHandle = l;
        }

        void traceDispose() {
        }

        @Override
        public void dispose() {
            if (this.nativeHandle != 0L) {
                this.traceDispose();
                this.context.releaseD3DMesh(this.nativeHandle);
                this.nativeHandle = 0L;
            }
        }
    }
}

