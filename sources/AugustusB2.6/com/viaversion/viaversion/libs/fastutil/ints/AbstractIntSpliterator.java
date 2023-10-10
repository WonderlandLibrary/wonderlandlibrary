// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

public abstract class AbstractIntSpliterator implements IntSpliterator
{
    protected AbstractIntSpliterator() {
    }
    
    @Override
    public final boolean tryAdvance(final IntConsumer action) {
        return this.tryAdvance((java.util.function.IntConsumer)action);
    }
    
    @Override
    public final void forEachRemaining(final IntConsumer action) {
        this.forEachRemaining((java.util.function.IntConsumer)action);
    }
}
