// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.io.Serializable;

public abstract class AbstractInt2ObjectFunction<V> implements Int2ObjectFunction<V>, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    protected V defRetValue;
    
    protected AbstractInt2ObjectFunction() {
    }
    
    @Override
    public void defaultReturnValue(final V rv) {
        this.defRetValue = rv;
    }
    
    @Override
    public V defaultReturnValue() {
        return this.defRetValue;
    }
}
