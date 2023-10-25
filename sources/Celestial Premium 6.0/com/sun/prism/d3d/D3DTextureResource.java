/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.d3d;

import com.sun.prism.d3d.D3DTextureData;
import com.sun.prism.d3d.D3DVramPool;
import com.sun.prism.impl.DisposerManagedResource;

class D3DTextureResource
extends DisposerManagedResource<D3DTextureData> {
    public D3DTextureResource(D3DTextureData d3DTextureData) {
        super(d3DTextureData, D3DVramPool.instance, d3DTextureData);
    }

    @Override
    public void free() {
        if (this.resource != null) {
            ((D3DTextureData)this.resource).dispose();
        }
    }

    @Override
    public boolean isValid() {
        return this.resource != null && ((D3DTextureData)this.resource).getResource() != 0L;
    }
}

