/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl;

import com.sun.prism.PhongMaterial;
import com.sun.prism.impl.BaseGraphicsResource;
import com.sun.prism.impl.Disposer;

public abstract class BasePhongMaterial
extends BaseGraphicsResource
implements PhongMaterial {
    protected BasePhongMaterial(Disposer.Record record) {
        super(record);
    }

    @Override
    public boolean isValid() {
        return true;
    }
}

