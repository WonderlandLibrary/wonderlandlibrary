/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl;

import com.sun.prism.MeshView;
import com.sun.prism.impl.BaseGraphicsResource;
import com.sun.prism.impl.Disposer;

public abstract class BaseMeshView
extends BaseGraphicsResource
implements MeshView {
    protected BaseMeshView(Disposer.Record record) {
        super(record);
    }

    @Override
    public boolean isValid() {
        return true;
    }
}

