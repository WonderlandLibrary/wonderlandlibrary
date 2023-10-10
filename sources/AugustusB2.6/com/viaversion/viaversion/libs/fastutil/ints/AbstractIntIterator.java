// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

public abstract class AbstractIntIterator implements IntIterator
{
    protected AbstractIntIterator() {
    }
    
    @Override
    public final void forEachRemaining(final IntConsumer action) {
        this.forEachRemaining((java.util.function.IntConsumer)action);
    }
}
