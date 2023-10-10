// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.ranges;

public final class IntRange extends IntProgression
{
    @Override
    public final boolean isEmpty() {
        return this.getFirst() > this.getLast();
    }
    
    @Override
    public final boolean equals(final Object other) {
        return other instanceof IntRange && ((this.isEmpty() && ((IntRange)other).isEmpty()) || (this.getFirst() == ((IntRange)other).getFirst() && this.getLast() == ((IntRange)other).getLast()));
    }
    
    @Override
    public final int hashCode() {
        if (this.isEmpty()) {
            return -1;
        }
        return 31 * this.getFirst() + this.getLast();
    }
    
    @Override
    public final String toString() {
        return this.getFirst() + ".." + this.getLast();
    }
    
    public IntRange(final int start, final int endInclusive) {
        super(start, endInclusive, 1);
    }
    
    static {
        new Companion((byte)0);
        new IntRange(1, 0);
    }
    
    public static final class Companion
    {
        private Companion() {
        }
    }
}
