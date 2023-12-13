// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;
import java.util.Iterator;

@GwtCompatible
public abstract class ForwardingIterator<T> extends ForwardingObject implements Iterator<T>
{
    protected ForwardingIterator() {
    }
    
    @Override
    protected abstract Iterator<T> delegate();
    
    @Override
    public boolean hasNext() {
        return this.delegate().hasNext();
    }
    
    @CanIgnoreReturnValue
    @Override
    public T next() {
        return this.delegate().next();
    }
    
    @Override
    public void remove() {
        this.delegate().remove();
    }
}
