// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.function.Consumer;
import java.util.Spliterators;
import java.util.Spliterator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.function.BiConsumer;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.Arrays;
import com.google.common.base.Objects;
import java.util.LinkedHashMap;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.primitives.Ints;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.annotation.CheckForNull;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;
import java.util.AbstractMap;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
class CompactHashMap<K, V> extends AbstractMap<K, V> implements Serializable
{
    private static final Object NOT_FOUND;
    @VisibleForTesting
    static final double HASH_FLOODING_FPP = 0.001;
    private static final int MAX_HASH_BUCKET_LENGTH = 9;
    @CheckForNull
    private transient Object table;
    @CheckForNull
    @VisibleForTesting
    transient int[] entries;
    @CheckForNull
    @VisibleForTesting
    transient Object[] keys;
    @CheckForNull
    @VisibleForTesting
    transient Object[] values;
    private transient int metadata;
    private transient int size;
    @CheckForNull
    private transient Set<K> keySetView;
    @CheckForNull
    private transient Set<Map.Entry<K, V>> entrySetView;
    @CheckForNull
    private transient Collection<V> valuesView;
    
    public static <K, V> CompactHashMap<K, V> create() {
        return new CompactHashMap<K, V>();
    }
    
    public static <K, V> CompactHashMap<K, V> createWithExpectedSize(final int expectedSize) {
        return new CompactHashMap<K, V>(expectedSize);
    }
    
    CompactHashMap() {
        this.init(3);
    }
    
    CompactHashMap(final int expectedSize) {
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
        this.keys = new Object[expectedSize];
        this.values = new Object[expectedSize];
        return expectedSize;
    }
    
    @CheckForNull
    @VisibleForTesting
    Map<K, V> delegateOrNull() {
        if (this.table instanceof Map) {
            return (Map<K, V>)this.table;
        }
        return null;
    }
    
    Map<K, V> createHashFloodingResistantDelegate(final int tableSize) {
        return new LinkedHashMap<K, V>(tableSize, 1.0f);
    }
    
    @VisibleForTesting
    @CanIgnoreReturnValue
    Map<K, V> convertToHashFloodingResistantImplementation() {
        final Map<K, V> newDelegate = this.createHashFloodingResistantDelegate(this.hashTableMask() + 1);
        for (int i = this.firstEntryIndex(); i >= 0; i = this.getSuccessor(i)) {
            newDelegate.put(this.key(i), this.value(i));
        }
        this.table = newDelegate;
        this.entries = null;
        this.keys = null;
        this.values = null;
        this.incrementModCount();
        return newDelegate;
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
    
    void accessEntry(final int index) {
    }
    
    @CheckForNull
    @CanIgnoreReturnValue
    @Override
    public V put(@ParametricNullness final K key, @ParametricNullness final V value) {
        if (this.needsAllocArrays()) {
            this.allocArrays();
        }
        final Map<K, V> delegate = this.delegateOrNull();
        if (delegate != null) {
            return delegate.put(key, value);
        }
        final int[] entries = this.requireEntries();
        final Object[] keys = this.requireKeys();
        final Object[] values = this.requireValues();
        final int newEntryIndex = this.size;
        final int newSize = newEntryIndex + 1;
        final int hash = Hashing.smearedHash(key);
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
                if (CompactHashing.getHashPrefix(entry, mask) == hashPrefix && Objects.equal(key, keys[entryIndex])) {
                    final V oldValue = (V)values[entryIndex];
                    values[entryIndex] = value;
                    this.accessEntry(entryIndex);
                    return oldValue;
                }
                next = CompactHashing.getNext(entry, mask);
                ++bucketLength;
            } while (next != 0);
            if (bucketLength >= 9) {
                return this.convertToHashFloodingResistantImplementation().put(key, value);
            }
            if (newSize > mask) {
                mask = this.resizeTable(mask, CompactHashing.newCapacity(mask), hash, newEntryIndex);
            }
            else {
                entries[entryIndex] = CompactHashing.maskCombine(entry, newEntryIndex + 1, mask);
            }
        }
        this.resizeMeMaybe(newSize);
        this.insertEntry(newEntryIndex, key, value, hash, mask);
        this.size = newSize;
        this.incrementModCount();
        return null;
    }
    
    void insertEntry(final int entryIndex, @ParametricNullness final K key, @ParametricNullness final V value, final int hash, final int mask) {
        this.setEntry(entryIndex, CompactHashing.maskCombine(hash, 0, mask));
        this.setKey(entryIndex, key);
        this.setValue(entryIndex, value);
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
        this.keys = Arrays.copyOf(this.requireKeys(), newCapacity);
        this.values = Arrays.copyOf(this.requireValues(), newCapacity);
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
    
    private int indexOf(@CheckForNull final Object key) {
        if (this.needsAllocArrays()) {
            return -1;
        }
        final int hash = Hashing.smearedHash(key);
        final int mask = this.hashTableMask();
        int next = CompactHashing.tableGet(this.requireTable(), hash & mask);
        if (next == 0) {
            return -1;
        }
        final int hashPrefix = CompactHashing.getHashPrefix(hash, mask);
        do {
            final int entryIndex = next - 1;
            final int entry = this.entry(entryIndex);
            if (CompactHashing.getHashPrefix(entry, mask) == hashPrefix && Objects.equal(key, this.key(entryIndex))) {
                return entryIndex;
            }
            next = CompactHashing.getNext(entry, mask);
        } while (next != 0);
        return -1;
    }
    
    @Override
    public boolean containsKey(@CheckForNull final Object key) {
        final Map<K, V> delegate = this.delegateOrNull();
        return (delegate != null) ? delegate.containsKey(key) : (this.indexOf(key) != -1);
    }
    
    @CheckForNull
    @Override
    public V get(@CheckForNull final Object key) {
        final Map<K, V> delegate = this.delegateOrNull();
        if (delegate != null) {
            return delegate.get(key);
        }
        final int index = this.indexOf(key);
        if (index == -1) {
            return null;
        }
        this.accessEntry(index);
        return this.value(index);
    }
    
    @CheckForNull
    @CanIgnoreReturnValue
    @Override
    public V remove(@CheckForNull final Object key) {
        final Map<K, V> delegate = this.delegateOrNull();
        if (delegate != null) {
            return delegate.remove(key);
        }
        final Object oldValue = this.removeHelper(key);
        return (V)((oldValue == CompactHashMap.NOT_FOUND) ? null : oldValue);
    }
    
    private Object removeHelper(@CheckForNull final Object key) {
        if (this.needsAllocArrays()) {
            return CompactHashMap.NOT_FOUND;
        }
        final int mask = this.hashTableMask();
        final int index = CompactHashing.remove(key, null, mask, this.requireTable(), this.requireEntries(), this.requireKeys(), null);
        if (index == -1) {
            return CompactHashMap.NOT_FOUND;
        }
        final Object oldValue = this.value(index);
        this.moveLastEntry(index, mask);
        --this.size;
        this.incrementModCount();
        return oldValue;
    }
    
    void moveLastEntry(final int dstIndex, final int mask) {
        final Object table = this.requireTable();
        final int[] entries = this.requireEntries();
        final Object[] keys = this.requireKeys();
        final Object[] values = this.requireValues();
        final int srcIndex = this.size() - 1;
        if (dstIndex < srcIndex) {
            final Object key = keys[srcIndex];
            keys[dstIndex] = key;
            values[dstIndex] = values[srcIndex];
            values[srcIndex] = (keys[srcIndex] = null);
            entries[dstIndex] = entries[srcIndex];
            entries[srcIndex] = 0;
            final int tableIndex = Hashing.smearedHash(key) & mask;
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
            values[dstIndex] = (keys[dstIndex] = null);
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
    public void replaceAll(final BiFunction<? super K, ? super V, ? extends V> function) {
        Preconditions.checkNotNull(function);
        final Map<K, V> delegate = this.delegateOrNull();
        if (delegate != null) {
            delegate.replaceAll(function);
        }
        else {
            for (int i = 0; i < this.size; ++i) {
                this.setValue(i, function.apply(this.key(i), this.value(i)));
            }
        }
    }
    
    @Override
    public Set<K> keySet() {
        return (this.keySetView == null) ? (this.keySetView = this.createKeySet()) : this.keySetView;
    }
    
    Set<K> createKeySet() {
        return (Set<K>)new KeySetView();
    }
    
    Iterator<K> keySetIterator() {
        final Map<K, V> delegate = this.delegateOrNull();
        if (delegate != null) {
            return delegate.keySet().iterator();
        }
        return new Itr<K>() {
            @ParametricNullness
            @Override
            K getOutput(final int entry) {
                return (K)CompactHashMap.this.key(entry);
            }
        };
    }
    
    @Override
    public void forEach(final BiConsumer<? super K, ? super V> action) {
        Preconditions.checkNotNull(action);
        final Map<K, V> delegate = this.delegateOrNull();
        if (delegate != null) {
            delegate.forEach(action);
        }
        else {
            for (int i = this.firstEntryIndex(); i >= 0; i = this.getSuccessor(i)) {
                action.accept(this.key(i), this.value(i));
            }
        }
    }
    
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return (this.entrySetView == null) ? (this.entrySetView = this.createEntrySet()) : this.entrySetView;
    }
    
    Set<Map.Entry<K, V>> createEntrySet() {
        return (Set<Map.Entry<K, V>>)new EntrySetView();
    }
    
    Iterator<Map.Entry<K, V>> entrySetIterator() {
        final Map<K, V> delegate = this.delegateOrNull();
        if (delegate != null) {
            return delegate.entrySet().iterator();
        }
        return new Itr<Map.Entry<K, V>>() {
            @Override
            Map.Entry<K, V> getOutput(final int entry) {
                return new MapEntry(entry);
            }
        };
    }
    
    @Override
    public int size() {
        final Map<K, V> delegate = this.delegateOrNull();
        return (delegate != null) ? delegate.size() : this.size;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public boolean containsValue(@CheckForNull final Object value) {
        final Map<K, V> delegate = this.delegateOrNull();
        if (delegate != null) {
            return delegate.containsValue(value);
        }
        for (int i = 0; i < this.size; ++i) {
            if (Objects.equal(value, this.value(i))) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public Collection<V> values() {
        return (this.valuesView == null) ? (this.valuesView = this.createValues()) : this.valuesView;
    }
    
    Collection<V> createValues() {
        return (Collection<V>)new ValuesView();
    }
    
    Iterator<V> valuesIterator() {
        final Map<K, V> delegate = this.delegateOrNull();
        if (delegate != null) {
            return delegate.values().iterator();
        }
        return new Itr<V>() {
            @ParametricNullness
            @Override
            V getOutput(final int entry) {
                return (V)CompactHashMap.this.value(entry);
            }
        };
    }
    
    public void trimToSize() {
        if (this.needsAllocArrays()) {
            return;
        }
        final Map<K, V> delegate = this.delegateOrNull();
        if (delegate != null) {
            final Map<K, V> newDelegate = this.createHashFloodingResistantDelegate(this.size());
            newDelegate.putAll((Map<? extends K, ? extends V>)delegate);
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
        final Map<K, V> delegate = this.delegateOrNull();
        if (delegate != null) {
            this.metadata = Ints.constrainToRange(this.size(), 3, 1073741823);
            delegate.clear();
            this.table = null;
            this.size = 0;
        }
        else {
            Arrays.fill(this.requireKeys(), 0, this.size, null);
            Arrays.fill(this.requireValues(), 0, this.size, null);
            CompactHashing.tableClear(this.requireTable());
            Arrays.fill(this.requireEntries(), 0, this.size, 0);
            this.size = 0;
        }
    }
    
    private void writeObject(final ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeInt(this.size());
        final Iterator<Map.Entry<K, V>> entryIterator = this.entrySetIterator();
        while (entryIterator.hasNext()) {
            final Map.Entry<K, V> e = entryIterator.next();
            stream.writeObject(e.getKey());
            stream.writeObject(e.getValue());
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
            final K key = (K)stream.readObject();
            final V value = (V)stream.readObject();
            this.put(key, value);
        }
    }
    
    private Object requireTable() {
        return java.util.Objects.requireNonNull(this.table);
    }
    
    private int[] requireEntries() {
        return java.util.Objects.requireNonNull(this.entries);
    }
    
    private Object[] requireKeys() {
        return java.util.Objects.requireNonNull(this.keys);
    }
    
    private Object[] requireValues() {
        return java.util.Objects.requireNonNull(this.values);
    }
    
    private K key(final int i) {
        return (K)this.requireKeys()[i];
    }
    
    private V value(final int i) {
        return (V)this.requireValues()[i];
    }
    
    private int entry(final int i) {
        return this.requireEntries()[i];
    }
    
    private void setKey(final int i, final K key) {
        this.requireKeys()[i] = key;
    }
    
    private void setValue(final int i, final V value) {
        this.requireValues()[i] = value;
    }
    
    private void setEntry(final int i, final int value) {
        this.requireEntries()[i] = value;
    }
    
    static {
        NOT_FOUND = new Object();
    }
    
    private abstract class Itr<T> implements Iterator<T>
    {
        int expectedMetadata;
        int currentIndex;
        int indexToRemove;
        
        private Itr() {
            this.expectedMetadata = CompactHashMap.this.metadata;
            this.currentIndex = CompactHashMap.this.firstEntryIndex();
            this.indexToRemove = -1;
        }
        
        @Override
        public boolean hasNext() {
            return this.currentIndex >= 0;
        }
        
        @ParametricNullness
        abstract T getOutput(final int p0);
        
        @ParametricNullness
        @Override
        public T next() {
            this.checkForConcurrentModification();
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.indexToRemove = this.currentIndex;
            final T result = this.getOutput(this.currentIndex);
            this.currentIndex = CompactHashMap.this.getSuccessor(this.currentIndex);
            return result;
        }
        
        @Override
        public void remove() {
            this.checkForConcurrentModification();
            CollectPreconditions.checkRemove(this.indexToRemove >= 0);
            this.incrementExpectedModCount();
            CompactHashMap.this.remove(CompactHashMap.this.key(this.indexToRemove));
            this.currentIndex = CompactHashMap.this.adjustAfterRemove(this.currentIndex, this.indexToRemove);
            this.indexToRemove = -1;
        }
        
        void incrementExpectedModCount() {
            this.expectedMetadata += 32;
        }
        
        private void checkForConcurrentModification() {
            if (CompactHashMap.this.metadata != this.expectedMetadata) {
                throw new ConcurrentModificationException();
            }
        }
    }
    
    class KeySetView extends Maps.KeySet<K, V>
    {
        KeySetView() {
            super(CompactHashMap.this);
        }
        
        @Override
        public Object[] toArray() {
            if (CompactHashMap.this.needsAllocArrays()) {
                return new Object[0];
            }
            final Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
            return (delegate != null) ? delegate.keySet().toArray() : ObjectArrays.copyAsObjectArray(CompactHashMap.this.requireKeys(), 0, CompactHashMap.this.size);
        }
        
        @Override
        public <T> T[] toArray(final T[] a) {
            if (CompactHashMap.this.needsAllocArrays()) {
                if (a.length > 0) {
                    final Object[] unsoundlyCovariantArray = a;
                    unsoundlyCovariantArray[0] = null;
                }
                return a;
            }
            final Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
            return (delegate != null) ? delegate.keySet().toArray(a) : ObjectArrays.toArrayImpl(CompactHashMap.this.requireKeys(), 0, CompactHashMap.this.size, a);
        }
        
        @Override
        public boolean remove(@CheckForNull final Object o) {
            final Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
            return (delegate != null) ? delegate.keySet().remove(o) : (CompactHashMap.this.removeHelper(o) != CompactHashMap.NOT_FOUND);
        }
        
        @Override
        public Iterator<K> iterator() {
            return CompactHashMap.this.keySetIterator();
        }
        
        @Override
        public Spliterator<K> spliterator() {
            if (CompactHashMap.this.needsAllocArrays()) {
                return Spliterators.spliterator(new Object[0], 17);
            }
            final Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
            return (delegate != null) ? delegate.keySet().spliterator() : Spliterators.spliterator(CompactHashMap.this.requireKeys(), 0, CompactHashMap.this.size, 17);
        }
        
        @Override
        public void forEach(final Consumer<? super K> action) {
            Preconditions.checkNotNull(action);
            final Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
            if (delegate != null) {
                delegate.keySet().forEach(action);
            }
            else {
                for (int i = CompactHashMap.this.firstEntryIndex(); i >= 0; i = CompactHashMap.this.getSuccessor(i)) {
                    action.accept((Object)CompactHashMap.this.key(i));
                }
            }
        }
    }
    
    class EntrySetView extends Maps.EntrySet<K, V>
    {
        @Override
        Map<K, V> map() {
            return (Map<K, V>)CompactHashMap.this;
        }
        
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return CompactHashMap.this.entrySetIterator();
        }
        
        @Override
        public Spliterator<Map.Entry<K, V>> spliterator() {
            final Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
            return (Spliterator<Map.Entry<K, V>>)((delegate != null) ? delegate.entrySet().spliterator() : CollectSpliterators.indexed(CompactHashMap.this.size, 17, x$0 -> new MapEntry(x$0)));
        }
        
        @Override
        public boolean contains(@CheckForNull final Object o) {
            final Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
            if (delegate != null) {
                return delegate.entrySet().contains(o);
            }
            if (o instanceof Map.Entry) {
                final Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
                final int index = CompactHashMap.this.indexOf(entry.getKey());
                return index != -1 && Objects.equal(CompactHashMap.this.value(index), entry.getValue());
            }
            return false;
        }
        
        @Override
        public boolean remove(@CheckForNull final Object o) {
            final Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
            if (delegate != null) {
                return delegate.entrySet().remove(o);
            }
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
            if (CompactHashMap.this.needsAllocArrays()) {
                return false;
            }
            final int mask = CompactHashMap.this.hashTableMask();
            final int index = CompactHashing.remove(entry.getKey(), entry.getValue(), mask, CompactHashMap.this.requireTable(), CompactHashMap.this.requireEntries(), CompactHashMap.this.requireKeys(), CompactHashMap.this.requireValues());
            if (index == -1) {
                return false;
            }
            CompactHashMap.this.moveLastEntry(index, mask);
            CompactHashMap.this.size--;
            CompactHashMap.this.incrementModCount();
            return true;
        }
    }
    
    final class MapEntry extends AbstractMapEntry<K, V>
    {
        @ParametricNullness
        private final K key;
        private int lastKnownIndex;
        
        MapEntry(final int index) {
            this.key = (K)CompactHashMap.this.key(index);
            this.lastKnownIndex = index;
        }
        
        @ParametricNullness
        @Override
        public K getKey() {
            return this.key;
        }
        
        private void updateLastKnownIndex() {
            if (this.lastKnownIndex == -1 || this.lastKnownIndex >= CompactHashMap.this.size() || !Objects.equal(this.key, CompactHashMap.this.key(this.lastKnownIndex))) {
                this.lastKnownIndex = CompactHashMap.this.indexOf(this.key);
            }
        }
        
        @ParametricNullness
        @Override
        public V getValue() {
            final Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
            if (delegate != null) {
                return NullnessCasts.uncheckedCastNullableTToT(delegate.get(this.key));
            }
            this.updateLastKnownIndex();
            return (V)((this.lastKnownIndex == -1) ? NullnessCasts.unsafeNull() : CompactHashMap.this.value(this.lastKnownIndex));
        }
        
        @ParametricNullness
        @Override
        public V setValue(@ParametricNullness final V value) {
            final Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
            if (delegate != null) {
                return NullnessCasts.uncheckedCastNullableTToT(delegate.put(this.key, value));
            }
            this.updateLastKnownIndex();
            if (this.lastKnownIndex == -1) {
                CompactHashMap.this.put(this.key, value);
                return NullnessCasts.unsafeNull();
            }
            final V old = (V)CompactHashMap.this.value(this.lastKnownIndex);
            CompactHashMap.this.setValue(this.lastKnownIndex, value);
            return old;
        }
    }
    
    class ValuesView extends Maps.Values<K, V>
    {
        ValuesView() {
            super(CompactHashMap.this);
        }
        
        @Override
        public Iterator<V> iterator() {
            return CompactHashMap.this.valuesIterator();
        }
        
        @Override
        public void forEach(final Consumer<? super V> action) {
            Preconditions.checkNotNull(action);
            final Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
            if (delegate != null) {
                delegate.values().forEach(action);
            }
            else {
                for (int i = CompactHashMap.this.firstEntryIndex(); i >= 0; i = CompactHashMap.this.getSuccessor(i)) {
                    action.accept((Object)CompactHashMap.this.value(i));
                }
            }
        }
        
        @Override
        public Spliterator<V> spliterator() {
            if (CompactHashMap.this.needsAllocArrays()) {
                return Spliterators.spliterator(new Object[0], 16);
            }
            final Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
            return (delegate != null) ? delegate.values().spliterator() : Spliterators.spliterator(CompactHashMap.this.requireValues(), 0, CompactHashMap.this.size, 16);
        }
        
        @Override
        public Object[] toArray() {
            if (CompactHashMap.this.needsAllocArrays()) {
                return new Object[0];
            }
            final Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
            return (delegate != null) ? delegate.values().toArray() : ObjectArrays.copyAsObjectArray(CompactHashMap.this.requireValues(), 0, CompactHashMap.this.size);
        }
        
        @Override
        public <T> T[] toArray(final T[] a) {
            if (CompactHashMap.this.needsAllocArrays()) {
                if (a.length > 0) {
                    final Object[] unsoundlyCovariantArray = a;
                    unsoundlyCovariantArray[0] = null;
                }
                return a;
            }
            final Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
            return (delegate != null) ? delegate.values().toArray(a) : ObjectArrays.toArrayImpl(CompactHashMap.this.requireValues(), 0, CompactHashMap.this.size, a);
        }
    }
}
