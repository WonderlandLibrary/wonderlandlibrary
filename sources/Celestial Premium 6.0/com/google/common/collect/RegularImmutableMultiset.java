/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.errorprone.annotations.concurrent.LazyInit
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Hashing;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.common.primitives.Ints;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.util.Collection;
import javax.annotation.Nullable;

@GwtCompatible(serializable=true)
class RegularImmutableMultiset<E>
extends ImmutableMultiset<E> {
    static final RegularImmutableMultiset<Object> EMPTY = new RegularImmutableMultiset(ImmutableList.of());
    private final transient Multisets.ImmutableEntry<E>[] entries;
    private final transient Multisets.ImmutableEntry<E>[] hashTable;
    private final transient int size;
    private final transient int hashCode;
    @LazyInit
    private transient ImmutableSet<E> elementSet;

    RegularImmutableMultiset(Collection<? extends Multiset.Entry<? extends E>> entries) {
        int distinct = entries.size();
        Multisets.ImmutableEntry[] entryArray = new Multisets.ImmutableEntry[distinct];
        if (distinct == 0) {
            this.entries = entryArray;
            this.hashTable = null;
            this.size = 0;
            this.hashCode = 0;
            this.elementSet = ImmutableSet.of();
        } else {
            int tableSize = Hashing.closedTableSize(distinct, 1.0);
            int mask = tableSize - 1;
            Multisets.ImmutableEntry[] hashTable = new Multisets.ImmutableEntry[tableSize];
            int index = 0;
            int hashCode = 0;
            long size = 0L;
            for (Multiset.Entry<E> entry : entries) {
                Multisets.ImmutableEntry newEntry;
                E element = Preconditions.checkNotNull(entry.getElement());
                int count = entry.getCount();
                int hash = element.hashCode();
                int bucket = Hashing.smear(hash) & mask;
                Multisets.ImmutableEntry bucketHead = hashTable[bucket];
                if (bucketHead == null) {
                    boolean canReuseEntry = entry instanceof Multisets.ImmutableEntry && !(entry instanceof NonTerminalEntry);
                    newEntry = canReuseEntry ? (Multisets.ImmutableEntry)entry : new Multisets.ImmutableEntry<E>(element, count);
                } else {
                    newEntry = new NonTerminalEntry<E>(element, count, bucketHead);
                }
                hashCode += hash ^ count;
                entryArray[index++] = newEntry;
                hashTable[bucket] = newEntry;
                size += (long)count;
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
    public int count(@Nullable Object element) {
        Multisets.ImmutableEntry<E>[] hashTable = this.hashTable;
        if (element == null || hashTable == null) {
            return 0;
        }
        int hash = Hashing.smearedHash(element);
        int mask = hashTable.length - 1;
        for (Multisets.ImmutableEntry<E> entry = hashTable[hash & mask]; entry != null; entry = entry.nextInBucket()) {
            if (!Objects.equal(element, entry.getElement())) continue;
            return entry.getCount();
        }
        return 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public ImmutableSet<E> elementSet() {
        ElementSet result = this.elementSet;
        return result == null ? (this.elementSet = new ElementSet()) : result;
    }

    @Override
    Multiset.Entry<E> getEntry(int index) {
        return this.entries[index];
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    private final class ElementSet
    extends ImmutableSet.Indexed<E> {
        private ElementSet() {
        }

        @Override
        E get(int index) {
            return RegularImmutableMultiset.this.entries[index].getElement();
        }

        @Override
        public boolean contains(@Nullable Object object) {
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

    private static final class NonTerminalEntry<E>
    extends Multisets.ImmutableEntry<E> {
        private final Multisets.ImmutableEntry<E> nextInBucket;

        NonTerminalEntry(E element, int count, Multisets.ImmutableEntry<E> nextInBucket) {
            super(element, count);
            this.nextInBucket = nextInBucket;
        }

        @Override
        public Multisets.ImmutableEntry<E> nextInBucket() {
            return this.nextInBucket;
        }
    }
}

