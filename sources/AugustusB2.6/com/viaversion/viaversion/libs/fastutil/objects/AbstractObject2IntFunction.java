// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import java.io.Serializable;

public abstract class AbstractObject2IntFunction<K> implements Object2IntFunction<K>, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    protected int defRetValue;
    
    protected AbstractObject2IntFunction() {
    }
    
    @Override
    public void defaultReturnValue(final int rv) {
        this.defRetValue = rv;
    }
    
    @Override
    public int defaultReturnValue() {
        return this.defRetValue;
    }
}
