// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.ranges;

import com.badlogic.gdx.graphics.Pixmap;
import java.util.Iterator;

public class IntProgression implements Iterable<Integer>
{
    private final int first;
    private final int last;
    private final int step;
    public static final Companion Companion;
    
    public final int getFirst() {
        return this.first;
    }
    
    public final int getLast() {
        return this.last;
    }
    
    public final int getStep() {
        return this.step;
    }
    
    public boolean isEmpty() {
        if (this.step > 0) {
            return this.first > this.last;
        }
        return this.first < this.last;
    }
    
    @Override
    public boolean equals(final Object other) {
        return other instanceof IntProgression && ((this.isEmpty() && ((IntProgression)other).isEmpty()) || (this.first == ((IntProgression)other).first && this.last == ((IntProgression)other).last && this.step == ((IntProgression)other).step));
    }
    
    @Override
    public int hashCode() {
        if (this.isEmpty()) {
            return -1;
        }
        return 31 * (31 * this.first + this.last) + this.step;
    }
    
    @Override
    public String toString() {
        if (this.step > 0) {
            return this.first + ".." + this.last + " step " + this.step;
        }
        return this.first + " downTo " + this.last + " step " + -this.step;
    }
    
    public IntProgression(final int start, final int endInclusive, final int step) {
        if (step == 0) {
            throw new IllegalArgumentException("Step must be non-zero.");
        }
        if (step == Integer.MIN_VALUE) {
            throw new IllegalArgumentException("Step must be greater than Int.MIN_VALUE to avoid overflow on negation.");
        }
        this.first = start;
        this.last = Pixmap.getProgressionLastElement(start, endInclusive, step);
        this.step = step;
    }
    
    static {
        Companion = new Companion((byte)0);
    }
    
    public static final class Companion
    {
        private Companion() {
        }
    }
}
