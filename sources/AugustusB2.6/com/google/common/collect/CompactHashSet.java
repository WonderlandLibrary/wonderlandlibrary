// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.function.Consumer;
import java.util.Spliterators;
import java.util.Spliterator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Arrays;
import com.google.common.base.Objects;
import java.util.LinkedHashSet;
import java.util.Set;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.primitives.Ints;
import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.Collection;
import javax.annotation.CheckForNull;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;
import java.util.AbstractSet;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
class CompactHashSet<E> extends AbstractSet<E> implements Serializable
{
    @VisibleForTesting
    static final double HASH_FLOODING_FPP = 0.001;
    private static final int MAX_HASH_BUCKET_LENGTH = 9;
    @CheckForNull
    private transient Object table;
    @CheckForNull
    private transient int[] entries;
    @CheckForNull
    @VisibleForTesting
    transient Object[] elements;
    private transient int metadata;
    private transient int size;
    
    public static <E> CompactHashSet<E> create() {
        return new CompactHashSet<E>();
    }
    
    public static <E> CompactHashSet<E> create(final Collection<? extends E> collection) {
        final CompactHashSet<E> set = createWithExpectedSize(collection.size());
        set.addAll((Collection<?>)collection);
        return set;
    }
    
    @SafeVarargs
    public static <E> CompactHashSet<E> create(final E... elements) {
        final CompactHashSet<E> set = createWithExpectedSize(elements.length);
        Collections.addAll(set, elements);
        return set;
    }
    
    public static <E> CompactHashSet<E> createWithExpectedSize(final int expectedSize) {
        return new CompactHashSet<E>(expectedSize);
    }
    
    CompactHashSet() {
        this.init(3);
    }
    
    CompactHashSet(final int expectedSize) {
        this.init(expectedSize);
    }
    
    void init(final int expectedSize) {
        Preconditions.checkArgument(expectedSize >= 0, (Object)"Expected size must be >= 0");
        this.metadata = Ints.constrainToRange(expectedSize, 1, 1073741823);
    }
    
    @VisibleForTesting
    boolean needsAllocArrays() {
        return this.table == null;
    }
    
    @CanIgnoreReturnValue
    int allocArrays() {
        Preconditions.checkState(this.needsAllocArrays(), (Object)"Arrays already allocated");
        final int expectedSize = this.metadata;
        final int buckets = CompactHashing.tableSize(expectedSize);
        this.table = CompactHashing.createTable(buckets);
        this.setHashTableMask(buckets - 1);
        this.entries = new int[expectedSize];
        this.elements = new Object[expectedSize];
        return expectedSize;
    }
    
    @CheckForNull
    @VisibleForTesting
    Set<E> delegateOrNull() {
        if (this.table instanceof Set) {
            return (Set<E>)this.table;
        }
        return null;
    }
    
    private Set<E> createHashFloodingResistantDelegate(final int tableSize) {
        return new LinkedHashSet<E>(tableSize, 1.0f);
    }
    
    @VisibleForTesting
    @CanIgnoreReturnValue
    Set<E> convertToHashFloodingResistantImplementation() {
        final Set<E> newDelegate = this.createHashFloodingResistantDelegate(this.hashTableMask() + 1);
        for (int i = this.firstEntryIndex(); i >= 0; i = this.getSuccessor(i)) {
            newDelegate.add(this.element(i));
        }
        this.table = newDelegate;
        this.entries = null;
        this.elements = null;
        this.incrementModCount();
        return newDelegate;
    }
    
    @VisibleForTesting
    boolean isUsingHashFloodingResistance() {
        return this.delegateOrNull() != null;
    }
    
    private void setHashTableMask(final int mask) {
        final int hashTableBits = 32 - Integer.numberOfLeadingZeros(mask);
        this.metadata = CompactHashing.maskCombine(this.metadata, hashTableBits, 31);
    }
    
    private int hashTableMask() {
        return (1 << (this.metadata & 0x1F)) - 1;
    }
    
    void incrementModCount() {
        this.metadata += 32;
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean add(@ParametricNullness final E object) {
        if (this.needsAllocArrays()) {
            this.allocArrays();
        }
        final Set<E> delegate = this.delegateOrNull();
        if (delegate != null) {
            return delegate.add(object);
        }
        final int[] entries = this.requireEntries();
        final Object[] elements = this.requireElements();
        final int newEntryIndex = this.size;
        final int newSize = newEntryIndex + 1;
        final int hash = Hashing.smearedHash(object);
        int mask = this.hashTableMask();
        final int tableIndex = hash & mask;
        int next = CompactHashing.tableGet(this.requireTable(), tableIndex);
        if (next == 0) {
            if (newSize > mask) {
                mask = this.resizeTable(mask, CompactHashing.newCapacity(mask), hash, newEntryIndex);
            }
            else {
                CompactHashing.tableSet(this.requireTable(), tableIndex, newEntryIndex + 1);
            }
        }
        else {
            final int hashPrefix = CompactHashing.getHashPrefix(hash, mask);
            int bucketLength = 0;
            int entryIndex;
            int entry;
            do {
                entryIndex = next - 1;
                entry = entries[entryIndex];
                if (CompactHashing.getHashPrefix(entry, mask) == hashPrefix && Objects.equal(object, elements[entryIndex])) {
                    return false;
                }
                next = CompactHashing.getNext(entry, mask);
                ++bucketLength;
            } while (next != 0);
            if (bucketLength >= 9) {
                return this.convertToHashFloodingResistantImplementation().add(object);
            }
            if (newSize > mask) {
                mask = this.resizeTable(mask, CompactHashing.newCapacity(mask), hash, newEntryIndex);
            }
            else {
                entries[entryIndex] = CompactHashing.maskCombine(entry, newEntryIndex + 1, mask);
            }
        }
        this.resizeMeMaybe(newSize);
        this.insertEntry(newEntryIndex, object, hash, mask);
        this.size = newSize;
        this.incrementModCount();
        return true;
    }
    
    void insertEntry(final int entryIndex, @ParametricNullness final E object, final int hash, final int mask) {
        this.setEntry(entryIndex, CompactHashing.maskCombine(hash, 0, mask));
        this.setElement(entryIndex, object);
    }
    
    private void resizeMeMaybe(final int newSize) {
        final int entriesSize = this.requireEntries().length;
        if (newSize > entriesSize) {
            final int newCapacity = Math.min(1073741823, entriesSize + Math.max(1, entriesSize >>> 1) | 0x1);
            if (newCapacity != entriesSize) {
                this.resizeEntries(newCapacity);
            }
        }
    }
    
    void resizeEntries(final int newCapacity) {
        this.entries = Arrays.copyOf(this.requireEntries(), newCapacity);
        this.elements = Arrays.copyOf(this.requireElements(), newCapacity);
    }
    
    @CanIgnoreReturnValue
    private int resizeTable(final int oldMask, final int newCapacity, final int targetHash, final int targetEntryIndex) {
        final Object newTable = CompactHashing.createTable(newCapacity);
        final int newMask = newCapacity - 1;
        if (targetEntryIndex != 0) {
            CompactHashing.tableSet(newTable, targetHash & newMask, targetEntryIndex + 1);
        }
        final Object oldTable = this.requireTable();
        final int[] entries = this.requireEntries();
        for (int oldTableIndex = 0; oldTableIndex <= oldMask; ++oldTableIndex) {
            int oldEntry;
            for (int oldNext = CompactHashing.tableGet(oldTable, oldTableIndex); oldNext != 0; oldNext = CompactHashing.getNext(oldEntry, oldMask)) {
                final int entryIndex = oldNext - 1;
                oldEntry = entries[entryIndex];
                final int hash = CompactHashing.getHashPrefix(oldEntry, oldMask) | oldTableIndex;
                final int newTableIndex = hash & newMask;
                final int newNext = CompactHashing.tableGet(newTable, newTableIndex);
                CompactHashing.tableSet(newTable, newTableIndex, oldNext);
                entries[entryIndex] = CompactHashing.maskCombine(hash, newNext, newMask);
            }
        }
        this.table = newTable;
        this.setHashTableMask(newMask);
        return newMask;
    }
    
    @Override
    public boolean contains(@CheckForNull final Object object) {
        if (this.needsAllocArrays()) {
            return false;
        }
        final Set<E> delegate = this.delegateOrNull();
        if (delegate != null) {
            return delegate.contains(object);
        }
        final int hash = Hashing.smearedHash(object);
        final int mask = this.hashTableMask();
        int next = CompactHashing.tableGet(this.requireTable(), hash & mask);
        if (next == 0) {
            return false;
        }
        final int hashPrefix = CompactHashing.getHashPrefix(hash, mask);
        do {
            final int entryIndex = next - 1;
            final int entry = this.entry(entryIndex);
            if (CompactHashing.getHashPrefix(entry, mask) == hashPrefix && Objects.equal(object, this.element(entryIndex))) {
                return true;
            }
            next = CompactHashing.getNext(entry, mask);
        } while (next != 0);
        return false;
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean remove(@CheckForNull final Object object) {
        if (this.needsAllocArrays()) {
            return false;
        }
        final Set<E> delegate = this.delegateOrNull();
        if (delegate != null) {
            return delegate.remove(object);
        }
        final int mask = this.hashTableMask();
        final int index = CompactHashing.remove(object, null, mask, this.requireTable(), this.requireEntries(), this.requireElements(), null);
        if (index == -1) {
            return false;
        }
        this.moveLastEntry(index, mask);
        --this.size;
        this.incrementModCount();
        return true;
    }
    
    void moveLastEntry(final int dstIndex, final int mask) {
        final Object table = this.requireTable();
        final int[] entries = this.requireEntries();
        final Object[] elements = this.requireElements();
        final int srcIndex = this.size() - 1;
        if (dstIndex < srcIndex) {
            final Object object = elements[srcIndex];
            elements[dstIndex] = object;
            elements[srcIndex] = null;
            entries[dstIndex] = entries[srcIndex];
            entries[srcIndex] = 0;
            final int tableIndex = Hashing.smearedHash(object) & mask;
            int next = CompactHashing.tableGet(table, tableIndex);
            final int srcNext = srcIndex + 1;
            if (next == srcNext) {
                CompactHashing.tableSet(table, tableIndex, dstIndex + 1);
            }
            else {
                int entryIndex;
                int entry;
                do {
                    entryIndex = next - 1;
                    entry = entries[entryIndex];
                    next = CompactHashing.getNext(entry, mask);
                } while (next != srcNext);
                entries[entryIndex] = CompactHashing.maskCombine(entry, dstIndex + 1, mask);
            }
        }
        else {
            elements[dstIndex] = null;
            entries[dstIndex] = 0;
        }
    }
    
    int firstEntryIndex() {
        return this.isEmpty() ? -1 : 0;
    }
    
    int getSuccessor(final int entryIndex) {
        return (entryIndex + 1 < this.size) ? (entryIndex + 1) : -1;
    }
    
    int adjustAfterRemove(final int indexBeforeRemove, final int indexRemoved) {
        return indexBeforeRemove - 1;
    }
    
    @Override
    public Iterator<E> iterator() {
        final Set<E> delegate = this.delegateOrNull();
        if (delegate != null) {
            return delegate.iterator();
        }
        return new Iterator<E>() {
            int expectedMetadata = CompactHashSet.this.metadata;
            int currentIndex = CompactHashSet.this.firstEntryIndex();
            int indexToRemove = -1;
            
            @Override
            public boolean hasNext() {
                return this.currentIndex >= 0;
            }
            
            @ParametricNullness
            @Override
            public E next() {
                this.checkForConcurrentModification();
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                this.indexToRemove = this.currentIndex;
                final E result = (E)CompactHashSet.this.element(this.currentIndex);
                this.currentIndex = CompactHashSet.this.getSuccessor(this.currentIndex);
                return result;
            }
            
            @Override
            public void remove() {
                this.checkForConcurrentModification();
                CollectPreconditions.checkRemove(this.indexToRemove >= 0);
                this.incrementExpectedModCount();
                CompactHashSet.this.remove(CompactHashSet.this.element(this.indexToRemove));
                this.currentIndex = CompactHashSet.this.adjustAfterRemove(this.currentIndex, this.indexToRemove);
                this.indexToRemove = -1;
            }
            
            void incrementExpectedModCount() {
                this.expectedMetadata += 32;
            }
            
            private void checkForConcurrentModification() {
                if (CompactHashSet.this.metadata != this.expectedMetadata) {
                    throw new ConcurrentModificationException();
                }
            }
        };
    }
    
    @Override
    public Spliterator<E> spliterator() {
        if (this.needsAllocArrays()) {
            return Spliterators.spliterator(new Object[0], 17);
        }
        final Set<E> delegate = this.delegateOrNull();
        return (delegate != null) ? delegate.spliterator() : Spliterators.spliterator(this.requireElements(), 0, this.size, 17);
    }
    
    @Override
    public void forEach(final Consumer<? super E> action) {
        Preconditions.checkNotNull(action);
        final Set<E> delegate = this.delegateOrNull();
        if (delegate != null) {
            delegate.forEach(action);
        }
        else {
            for (int i = this.firstEntryIndex(); i >= 0; i = this.getSuccessor(i)) {
                action.accept(this.element(i));
            }
        }
    }
    
    @Override
    public int size() {
        final Set<E> delegate = this.delegateOrNull();
        return (delegate != null) ? delegate.size() : this.size;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public Object[] toArray() {
        if (this.needsAllocArrays()) {
            return new Object[0];
        }
        final Set<E> delegate = this.delegateOrNull();
        return (delegate != null) ? delegate.toArray() : Arrays.copyOf(this.requireElements(), this.size);
    }
    
    @CanIgnoreReturnValue
    @Override
    public <T> T[] toArray(final T[] a) {
        if (this.needsAllocArrays()) {
            if (a.length > 0) {
                a[0] = null;
            }
            return a;
        }
        final Set<E> delegate = this.delegateOrNull();
        return (delegate != null) ? delegate.toArray(a) : ObjectArrays.toArrayImpl(this.requireElements(), 0, this.size, a);
    }
    
    public void trimToSize() {
        if (this.needsAllocArrays()) {
            return;
        }
        final Set<E> delegate = this.delegateOrNull();
        if (delegate != null) {
            final Set<E> newDelegate = this.createHashFloodingResistantDelegate(this.size());
            newDelegate.addAll((Collection<? extends E>)delegate);
            this.table = newDelegate;
            return;
        }
        final int size = this.size;
        if (size < this.requireEntries().length) {
            this.resizeEntries(size);
        }
        final int minimumTableSize = CompactHashing.tableSize(size);
        final int mask = this.hashTableMask();
        if (minimumTableSize < mask) {
            this.resizeTable(mask, minimumTableSize, 0, 0);
        }
    }
    
    @Override
    public void clear() {
        if (this.needsAllocArrays()) {
            return;
        }
        this.incrementModCount();
        final Set<E> delegate = this.delegateOrNull();
        if (delegate != null) {
            this.metadata = Ints.constrainToRange(this.size(), 3, 1073741823);
            delegate.clear();
            this.table = null;
            this.size = 0;
        }
        else {
            Arrays.fill(this.requireElements(), 0, this.size, null);
            CompactHashing.tableClear(this.requireTable());
            Arrays.fill(this.requireEntries(), 0, this.size, 0);
            this.size = 0;
        }
    }
    
    private void writeObject(final ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeInt(this.size());
        for (final E e : this) {
            stream.writeObject(e);
        }
    }
    
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        final int elementCount = stream.readInt();
        if (elementCount < 0) {
            throw new InvalidObjectException(new StringBuilder(25).append("Invalid size: ").append(elementCount).toString());
        }
        this.init(elementCount);
        for (int i = 0; i < elementCount; ++i) {
            final E element = (E)stream.readObject();
            this.add(element);
        }
    }
    
    private Object requireTable() {
        return java.util.Objects.requireNonNull(this.table);
    }
    
    private int[] requireEntries() {
        return java.util.Objects.requireNonNull(this.entries);
    }
    
    private Object[] requireElements() {
        return java.util.Objects.requireNonNull(this.elements);
    }
    
    private E element(final int i) {
        return (E)this.requireElements()[i];
    }
    
    private int entry(final int i) {
        return this.requireEntries()[i];
    }
    
    private void setElement(final int i, final E value) {
        this.requireElements()[i] = value;
    }
    
    private void setEntry(final int i, final int value) {
        this.requireEntries()[i] = value;
    }
}
