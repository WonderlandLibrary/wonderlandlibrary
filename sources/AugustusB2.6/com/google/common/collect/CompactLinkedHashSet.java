// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Objects;
import java.util.Spliterators;
import java.util.Spliterator;
import java.util.Arrays;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Set;
import java.util.Collections;
import java.util.Collection;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtIncompatible;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
class CompactLinkedHashSet<E> extends CompactHashSet<E>
{
    private static final int ENDPOINT = -2;
    @CheckForNull
    private transient int[] predecessor;
    @CheckForNull
    private transient int[] successor;
    private transient int firstEntry;
    private transient int lastEntry;
    
    public static <E> CompactLinkedHashSet<E> create() {
        return new CompactLinkedHashSet<E>();
    }
    
    public static <E> CompactLinkedHashSet<E> create(final Collection<? extends E> collection) {
        final CompactLinkedHashSet<E> set = createWithExpectedSize(collection.size());
        set.addAll((Collection<?>)collection);
        return set;
    }
    
    @SafeVarargs
    public static <E> CompactLinkedHashSet<E> create(final E... elements) {
        final CompactLinkedHashSet<E> set = createWithExpectedSize(elements.length);
        Collections.addAll(set, elements);
        return set;
    }
    
    public static <E> CompactLinkedHashSet<E> createWithExpectedSize(final int expectedSize) {
        return new CompactLinkedHashSet<E>(expectedSize);
    }
    
    CompactLinkedHashSet() {
    }
    
    CompactLinkedHashSet(final int expectedSize) {
        super(expectedSize);
    }
    
    @Override
    void init(final int expectedSize) {
        super.init(expectedSize);
        this.firstEntry = -2;
        this.lastEntry = -2;
    }
    
    @Override
    int allocArrays() {
        final int expectedSize = super.allocArrays();
        this.predecessor = new int[expectedSize];
        this.successor = new int[expectedSize];
        return expectedSize;
    }
    
    @CanIgnoreReturnValue
    @Override
    Set<E> convertToHashFloodingResistantImplementation() {
        final Set<E> result = super.convertToHashFloodingResistantImplementation();
        this.predecessor = null;
        this.successor = null;
        return result;
    }
    
    private int getPredecessor(final int entry) {
        return this.requirePredecessors()[entry] - 1;
    }
    
    @Override
    int getSuccessor(final int entry) {
        return this.requireSuccessors()[entry] - 1;
    }
    
    private void setSuccessor(final int entry, final int succ) {
        this.requireSuccessors()[entry] = succ + 1;
    }
    
    private void setPredecessor(final int entry, final int pred) {
        this.requirePredecessors()[entry] = pred + 1;
    }
    
    private void setSucceeds(final int pred, final int succ) {
        if (pred == -2) {
            this.firstEntry = succ;
        }
        else {
            this.setSuccessor(pred, succ);
        }
        if (succ == -2) {
            this.lastEntry = pred;
        }
        else {
            this.setPredecessor(succ, pred);
        }
    }
    
    @Override
    void insertEntry(final int entryIndex, @ParametricNullness final E object, final int hash, final int mask) {
        super.insertEntry(entryIndex, object, hash, mask);
        this.setSucceeds(this.lastEntry, entryIndex);
        this.setSucceeds(entryIndex, -2);
    }
    
    @Override
    void moveLastEntry(final int dstIndex, final int mask) {
        final int srcIndex = this.size() - 1;
        super.moveLastEntry(dstIndex, mask);
        this.setSucceeds(this.getPredecessor(dstIndex), this.getSuccessor(dstIndex));
        if (dstIndex < srcIndex) {
            this.setSucceeds(this.getPredecessor(srcIndex), dstIndex);
            this.setSucceeds(dstIndex, this.getSuccessor(srcIndex));
        }
        this.requirePredecessors()[srcIndex] = 0;
        this.requireSuccessors()[srcIndex] = 0;
    }
    
    @Override
    void resizeEntries(final int newCapacity) {
        super.resizeEntries(newCapacity);
        this.predecessor = Arrays.copyOf(this.requirePredecessors(), newCapacity);
        this.successor = Arrays.copyOf(this.requireSuccessors(), newCapacity);
    }
    
    @Override
    int firstEntryIndex() {
        return this.firstEntry;
    }
    
    @Override
    int adjustAfterRemove(final int indexBeforeRemove, final int indexRemoved) {
        return (indexBeforeRemove >= this.size()) ? indexRemoved : indexBeforeRemove;
    }
    
    @Override
    public Object[] toArray() {
        return ObjectArrays.toArrayImpl(this);
    }
    
    @Override
    public <T> T[] toArray(final T[] a) {
        return ObjectArrays.toArrayImpl(this, a);
    }
    
    @Override
    public Spliterator<E> spliterator() {
        return Spliterators.spliterator((Collection<? extends E>)this, 17);
    }
    
    @Override
    public void clear() {
        if (this.needsAllocArrays()) {
            return;
        }
        this.firstEntry = -2;
        this.lastEntry = -2;
        if (this.predecessor != null && this.successor != null) {
            Arrays.fill(this.predecessor, 0, this.size(), 0);
            Arrays.fill(this.successor, 0, this.size(), 0);
        }
        super.clear();
    }
    
    private int[] requirePredecessors() {
        return Objects.requireNonNull(this.predecessor);
    }
    
    private int[] requireSuccessors() {
        return Objects.requireNonNull(this.successor);
    }
}
