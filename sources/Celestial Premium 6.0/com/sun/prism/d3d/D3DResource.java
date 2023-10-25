/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.d3d;

import com.sun.prism.d3d.D3DContext;
import com.sun.prism.d3d.D3DResourceFactory;
import com.sun.prism.impl.BaseGraphicsResource;
import com.sun.prism.impl.Disposer;

class D3DResource
extends BaseGraphicsResource {
    protected final D3DRecord d3dResRecord;

    D3DResource(D3DRecord d3DRecord) {
        super(d3DRecord);
        this.d3dResRecord = d3DRecord;
    }

    @Override
    public void dispose() {
        this.d3dResRecord.dispose();
    }

    static class D3DRecord
    implements Disposer.Record {
        private final D3DContext context;
        private long pResource;
        private boolean isDefaultPool;

        D3DRecord(D3DContext d3DContext, long l) {
            this.context = d3DContext;
            this.pResource = l;
            if (l != 0L) {
                d3DContext.getResourceFactory().addRecord(this);
                this.isDefaultPool = D3DResourceFactory.nIsDefaultPool(l);
            } else {
                this.isDefaultPool = false;
            }
        }

        long getResource() {
            return this.pResource;
        }

        D3DContext getContext() {
            return this.context;
        }

        boolean isDefaultPool() {
            return this.isDefaultPool;
        }

        protected void markDisposed() {
            this.pResource = 0L;
        }

        @Override
        public void dispose() {
            if (this.pResource != 0L) {
                this.context.getResourceFactory().removeRecord(this);
                D3DResourceFactory.nReleaseResource(this.context.getContextHandle(), this.pResource);
                this.pResource = 0L;
            }
        }
    }
}

