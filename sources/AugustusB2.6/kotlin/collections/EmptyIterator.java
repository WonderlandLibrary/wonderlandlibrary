// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.collections;

import java.util.NoSuchElementException;
import java.util.ListIterator;

public final class EmptyIterator implements ListIterator
{
    public static final EmptyIterator INSTANCE;
    
    @Override
    public final boolean hasNext() {
        return false;
    }
    
    @Override
    public final boolean hasPrevious() {
        return false;
    }
    
    @Override
    public final int nextIndex() {
        return 0;
    }
    
    @Override
    public final int previousIndex() {
        return -1;
    }
    
    private EmptyIterator() {
    }
    
    static {
        INSTANCE = new EmptyIterator();
    }
    
    @Override
    public final void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
}
