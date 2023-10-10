// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import java.util.Set;
import me.gong.mcleaks.util.google.common.base.Objects;
import javax.annotation.Nullable;
import java.util.Iterator;
import me.gong.mcleaks.util.google.common.primitives.Ints;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.util.Collection;
import me.gong.mcleaks.util.google.errorprone.annotations.concurrent.LazyInit;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible(serializable = true)
class RegularImmutableMultiset<E> extends ImmutableMultiset<E>
{
    static final RegularImmutableMultiset<Object> EMPTY;
    private final transient Multisets.ImmutableEntry<E>[] entries;
    private final transient Multisets.ImmutableEntry<E>[] hashTable;
    private final transient int size;
    private final transient int hashCode;
    @LazyInit
    private transient ImmutableSet<E> elementSet;
    
    RegularImmutableMultiset(final Collection<? extends Multiset.Entry<? extends E>> entries) {
        final int distinct = entries.size();
        final Multisets.ImmutableEntry<E>[] entryArray = (Multisets.ImmutableEntry<E>[])new Multisets.ImmutableEntry[distinct];
        if (distinct == 0) {
            this.entries = entryArray;
            this.hashTable = null;
            this.size = 0;
            this.hashCode = 0;
            this.elementSet = ImmutableSet.of();
        }
        else {
            final int tableSize = Hashing.closedTableSize(distinct, 1.0);
            final int mask = tableSize - 1;
            final Multisets.ImmutableEntry<E>[] hashTable = (Multisets.ImmutableEntry<E>[])new Multisets.ImmutableEntry[tableSize];
            int index = 0;
            int hashCode = 0;
            long size = 0L;
            for (final Multiset.Entry<? extends E> entry : entries) {
                final E element = Preconditions.checkNotNull((E)entry.getElement());
                final int count = entry.getCount();
                final int hash = element.hashCode();
                final int bucket = Hashing.smear(hash) & mask;
                final Multisets.ImmutableEntry<E> bucketHead = hashTable[bucket];
                Multisets.ImmutableEntry<E> newEntry;
                if (bucketHead == null) {
                    final boolean canReuseEntry = entry instanceof Multisets.ImmutableEntry && !(entry instanceof NonTerminalEntry);
                    newEntry = (canReuseEntry ? ((Multisets.ImmutableEntry)entry) : new Multisets.ImmutableEntry<E>((E)element, count));
                }
                else {
                    newEntry = new NonTerminalEntry<E>(element, count, bucketHead);
                }
                hashCode += (hash ^ count);
                hashTable[bucket] = (entryArray[index++] = newEntry);
                size += count;
            }
            this.entries = entryArray;
            this.hashTable = hashTable;
            this.size = Ints.saturatedCast(size);
            this.hashCode = hashCode;
        }
    }
    
    @Override
    boolean isPartialView() {
        return false;
    }
    
    @Override
    public int count(@Nullable final Object element) {
        final Multisets.ImmutableEntry<E>[] hashTable = this.hashTable;
        if (element == null || hashTable == null) {
            return 0;
        }
        final int hash = Hashing.smearedHash(element);
        final int mask = hashTable.length - 1;
        for (Multisets.ImmutableEntry<E> entry = hashTable[hash & mask]; entry != null; entry = entry.nextInBucket()) {
            if (Objects.equal(element, entry.getElement())) {
                return entry.getCount();
            }
        }
        return 0;
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public ImmutableSet<E> elementSet() {
        final ImmutableSet<E> result = this.elementSet;
        return (result == null) ? (this.elementSet = new ElementSet()) : result;
    }
    
    @Override
    Multiset.Entry<E> getEntry(final int index) {
        return this.entries[index];
    }
    
    @Override
    public int hashCode() {
        return this.hashCode;
    }
    
    static {
        EMPTY = new RegularImmutableMultiset<Object>((Collection<? extends Multiset.Entry<?>>)ImmutableList.of());
    }
    
    private static final class NonTerminalEntry<E> extends Multisets.ImmutableEntry<E>
    {
        private final Multisets.ImmutableEntry<E> nextInBucket;
        
        NonTerminalEntry(final E element, final int count, final Multisets.ImmutableEntry<E> nextInBucket) {
            super(element, count);
            this.nextInBucket = nextInBucket;
        }
        
        @Override
        public Multisets.ImmutableEntry<E> nextInBucket() {
            return this.nextInBucket;
        }
    }
    
    private final class ElementSet extends Indexed<E>
    {
        @Override
        E get(final int index) {
            return RegularImmutableMultiset.this.entries[index].getElement();
        }
        
        @Override
        public boolean contains(@Nullable final Object object) {
            return RegularImmutableMultiset.this.contains(object);
        }
        
        @Override
        boolean isPartialView() {
            return true;
        }
        
        @Override
        public int size() {
            return RegularImmutableMultiset.this.entries.length;
        }
    }
}
