// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Objects;
import java.util.Collection;
import java.util.Spliterators;
import java.util.Spliterator;
import java.util.Set;
import java.util.Arrays;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.LinkedHashMap;
import java.util.Map;
import com.google.common.annotations.VisibleForTesting;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtIncompatible;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
class CompactLinkedHashMap<K, V> extends CompactHashMap<K, V>
{
    private static final int ENDPOINT = -2;
    @CheckForNull
    @VisibleForTesting
    transient long[] links;
    private transient int firstEntry;
    private transient int lastEntry;
    private final boolean accessOrder;
    
    public static <K, V> CompactLinkedHashMap<K, V> create() {
        return new CompactLinkedHashMap<K, V>();
    }
    
    public static <K, V> CompactLinkedHashMap<K, V> createWithExpectedSize(final int expectedSize) {
        return new CompactLinkedHashMap<K, V>(expectedSize);
    }
    
    CompactLinkedHashMap() {
        this(3);
    }
    
    CompactLinkedHashMap(final int expectedSize) {
        this(expectedSize, false);
    }
    
    CompactLinkedHashMap(final int expectedSize, final boolean accessOrder) {
        super(expectedSize);
        this.accessOrder = accessOrder;
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
        this.links = new long[expectedSize];
        return expectedSize;
    }
    
    @Override
    Map<K, V> createHashFloodingResistantDelegate(final int tableSize) {
        return new LinkedHashMap<K, V>(tableSize, 1.0f, this.accessOrder);
    }
    
    @CanIgnoreReturnValue
    @Override
    Map<K, V> convertToHashFloodingResistantImplementation() {
        final Map<K, V> result = super.convertToHashFloodingResistantImplementation();
        this.links = null;
        return result;
    }
    
    private int getPredecessor(final int entry) {
        return (int)(this.link(entry) >>> 32) - 1;
    }
    
    @Override
    int getSuccessor(final int entry) {
        return (int)this.link(entry) - 1;
    }
    
    private void setSuccessor(final int entry, final int succ) {
        final long succMask = 4294967295L;
        this.setLink(entry, (this.link(entry) & ~succMask) | ((long)(succ + 1) & succMask));
    }
    
    private void setPredecessor(final int entry, final int pred) {
        final long predMask = -4294967296L;
        this.setLink(entry, (this.link(entry) & ~predMask) | (long)(pred + 1) << 32);
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
    void insertEntry(final int entryIndex, @ParametricNullness final K key, @ParametricNullness final V value, final int hash, final int mask) {
        super.insertEntry(entryIndex, key, value, hash, mask);
        this.setSucceeds(this.lastEntry, entryIndex);
        this.setSucceeds(entryIndex, -2);
    }
    
    @Override
    void accessEntry(final int index) {
        if (this.accessOrder) {
            this.setSucceeds(this.getPredecessor(index), this.getSuccessor(index));
            this.setSucceeds(this.lastEntry, index);
            this.setSucceeds(index, -2);
            this.incrementModCount();
        }
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
        this.setLink(srcIndex, 0L);
    }
    
    @Override
    void resizeEntries(final int newCapacity) {
        super.resizeEntries(newCapacity);
        this.links = Arrays.copyOf(this.requireLinks(), newCapacity);
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
    Set<Map.Entry<K, V>> createEntrySet() {
        class EntrySetImpl extends EntrySetView
        {
            EntrySetImpl(final CompactLinkedHashMap this$0) {
                this$0.super();
            }
            
            @Override
            public Spliterator<Map.Entry<K, V>> spliterator() {
                return Spliterators.spliterator((Collection<? extends Map.Entry<K, V>>)this, 17);
            }
        }
        return (Set<Map.Entry<K, V>>)new EntrySetImpl();
    }
    
    @Override
    Set<K> createKeySet() {
        class KeySetImpl extends KeySetView
        {
            KeySetImpl(final CompactLinkedHashMap this$0) {
                this$0.super();
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
            public Spliterator<K> spliterator() {
                return Spliterators.spliterator((Collection<? extends K>)this, 17);
            }
        }
        return (Set<K>)new KeySetImpl();
    }
    
    @Override
    Collection<V> createValues() {
        class ValuesImpl extends ValuesView
        {
            ValuesImpl(final CompactLinkedHashMap this$0) {
                this$0.super();
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
            public Spliterator<V> spliterator() {
                return Spliterators.spliterator((Collection<? extends V>)this, 16);
            }
        }
        return (Collection<V>)new ValuesImpl();
    }
    
    @Override
    public void clear() {
        if (this.needsAllocArrays()) {
            return;
        }
        this.firstEntry = -2;
        this.lastEntry = -2;
        if (this.links != null) {
            Arrays.fill(this.links, 0, this.size(), 0L);
        }
        super.clear();
    }
    
    private long[] requireLinks() {
        return Objects.requireNonNull(this.links);
    }
    
    private long link(final int i) {
        return this.requireLinks()[i];
    }
    
    private void setLink(final int i, final long value) {
        this.requireLinks()[i] = value;
    }
}
