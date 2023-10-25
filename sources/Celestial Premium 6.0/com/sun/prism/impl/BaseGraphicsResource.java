/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl;

import com.sun.prism.GraphicsResource;
import com.sun.prism.impl.Disposer;

public abstract class BaseGraphicsResource
implements GraphicsResource {
    private final Object disposerReferent;
    protected final Disposer.Record disposerRecord;

    public BaseGraphicsResource(BaseGraphicsResource baseGraphicsResource) {
        this.disposerReferent = baseGraphicsResource.disposerReferent;
        this.disposerRecord = baseGraphicsResource.disposerRecord;
    }

    protected BaseGraphicsResource(Disposer.Record record) {
        this.disposerReferent = new Object();
        this.disposerRecord = record;
        Disposer.addRecord(this.disposerReferent, record);
    }

    @Override
    public abstract void dispose();
}

