// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.ranges;

import java.util.NoSuchElementException;
import kotlin.collections.IntIterator;

public final class IntProgressionIterator extends IntIterator
{
    private final int finalElement;
    private boolean hasNext;
    private int next;
    private final int step;
    
    @Override
    public final boolean hasNext() {
        return this.hasNext;
    }
    
    @Override
    public final int nextInt() {
        final int value;
        if ((value = this.next) == this.finalElement) {
            if (!this.hasNext) {
                throw new NoSuchElementException();
            }
            this.hasNext = false;
        }
        else {
            this.next += this.step;
        }
        return value;
    }
    
    public IntProgressionIterator(final int first, final int last, final int step) {
        this.step = step;
        this.finalElement = last;
        this.hasNext = ((this.step > 0) ? (first <= last) : (first >= last));
        this.next = (this.hasNext ? first : this.finalElement);
    }
}
