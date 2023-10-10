// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.ConcurrentModificationException;
import java.util.function.BiConsumer;
import java.util.Objects;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.function.Function;
import java.util.Spliterators;
import java.util.Spliterator;
import com.google.common.base.Preconditions;
import java.util.NoSuchElementException;
import javax.annotation.CheckForNull;
import java.util.Iterator;
import java.util.Map;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import java.util.Set;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(serializable = true, emulated = true)
public final class LinkedHashMultimap<K, V> extends LinkedHashMultimapGwtSerializationDependencies<K, V>
{
    private static final int DEFAULT_KEY_CAPACITY = 16;
    private static final int DEFAULT_VALUE_SET_CAPACITY = 2;
    @VisibleForTesting
    static final double VALUE_SET_LOAD_FACTOR = 1.0;
    @VisibleForTesting
    transient int valueSetCapacity;
    private transient ValueEntry<K, V> multimapHeaderEntry;
    @GwtIncompatible
    private static final long serialVersionUID = 1L;
    
    public static <K, V> LinkedHashMultimap<K, V> create() {
        return new LinkedHashMultimap<K, V>(16, 2);
    }
    
    public static <K, V> LinkedHashMultimap<K, V> create(final int expectedKeys, final int expectedValuesPerKey) {
        return new LinkedHashMultimap<K, V>(Maps.capacity(expectedKeys), Maps.capacity(expectedValuesPerKey));
    }
    
    public static <K, V> LinkedHashMultimap<K, V> create(final Multimap<? extends K, ? extends V> multimap) {
        final LinkedHashMultimap<K, V> result = create(multimap.keySet().size(), 2);
        result.putAll(multimap);
        return result;
    }
    
    private static <K, V> void succeedsInValueSet(final ValueSetLink<K, V> pred, final ValueSetLink<K, V> succ) {
        pred.setSuccessorInValueSet(succ);
        succ.setPredecessorInValueSet(pred);
    }
    
    private static <K, V> void succeedsInMultimap(final ValueEntry<K, V> pred, final ValueEntry<K, V> succ) {
        pred.setSuccessorInMultimap(succ);
        succ.setPredecessorInMultimap(pred);
    }
    
    private static <K, V> void deleteFromValueSet(final ValueSetLink<K, V> entry) {
        succeedsInValueSet(entry.getPredecessorInValueSet(), entry.getSuccessorInValueSet());
    }
    
    private static <K, V> void deleteFromMultimap(final ValueEntry<K, V> entry) {
        succeedsInMultimap(entry.getPredecessorInMultimap(), entry.getSuccessorInMultimap());
    }
    
    private LinkedHashMultimap(final int keyCapacity, final int valueSetCapacity) {
        super(Platform.newLinkedHashMapWithExpectedSize(keyCapacity));
        this.valueSetCapacity = 2;
        CollectPreconditions.checkNonnegative(valueSetCapacity, "expectedValuesPerKey");
        this.valueSetCapacity = valueSetCapacity;
        succeedsInMultimap(this.multimapHeaderEntry = ValueEntry.newHeader(), this.multimapHeaderEntry);
    }
    
    @Override
    Set<V> createCollection() {
        return Platform.newLinkedHashSetWithExpectedSize(this.valueSetCapacity);
    }
    
    @Override
    Collection<V> createCollection(@ParametricNullness final K key) {
        return new ValueSet(key, this.valueSetCapacity);
    }
    
    @CanIgnoreReturnValue
    @Override
    public Set<V> replaceValues(@ParametricNullness final K key, final Iterable<? extends V> values) {
        return super.replaceValues(key, values);
    }
    
    @Override
    public Set<Map.Entry<K, V>> entries() {
        return super.entries();
    }
    
    @Override
    public Set<K> keySet() {
        return super.keySet();
    }
    
    @Override
    public Collection<V> values() {
        return super.values();
    }
    
    @Override
    Iterator<Map.Entry<K, V>> entryIterator() {
        return new Iterator<Map.Entry<K, V>>() {
            ValueEntry<K, V> nextEntry = LinkedHashMultimap.this.multimapHeaderEntry.getSuccessorInMultimap();
            @CheckForNull
            ValueEntry<K, V> toRemove;
            
            @Override
            public boolean hasNext() {
                return this.nextEntry != LinkedHashMultimap.this.multimapHeaderEntry;
            }
            
            @Override
            public Map.Entry<K, V> next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final ValueEntry<K, V> result = this.nextEntry;
                this.toRemove = result;
                this.nextEntry = this.nextEntry.getSuccessorInMultimap();
                return result;
            }
            
            @Override
            public void remove() {
                Preconditions.checkState(this.toRemove != null, (Object)"no calls to next() since the last call to remove()");
                LinkedHashMultimap.this.remove(this.toRemove.getKey(), this.toRemove.getValue());
                this.toRemove = null;
            }
        };
    }
    
    @Override
    Spliterator<Map.Entry<K, V>> entrySpliterator() {
        return Spliterators.spliterator((Collection<? extends Map.Entry<K, V>>)this.entries(), 17);
    }
    
    @Override
    Iterator<V> valueIterator() {
        return Maps.valueIterator(this.entryIterator());
    }
    
    @Override
    Spliterator<V> valueSpliterator() {
        return CollectSpliterators.map(this.entrySpliterator(), (Function<? super Map.Entry<K, V>, ? extends V>)Map.Entry::getValue);
    }
    
    @Override
    public void clear() {
        super.clear();
        succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
    }
    
    @GwtIncompatible
    private void writeObject(final ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeInt(this.keySet().size());
        for (final K key : this.keySet()) {
            stream.writeObject(key);
        }
        stream.writeInt(this.size());
        for (final Map.Entry<K, V> entry : this.entries()) {
            stream.writeObject(entry.getKey());
            stream.writeObject(entry.getValue());
        }
    }
    
    @GwtIncompatible
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        succeedsInMultimap(this.multimapHeaderEntry = ValueEntry.newHeader(), this.multimapHeaderEntry);
        this.valueSetCapacity = 2;
        final int distinctKeys = stream.readInt();
        final Map<K, Collection<V>> map = Platform.newLinkedHashMapWithExpectedSize(12);
        for (int i = 0; i < distinctKeys; ++i) {
            final K key = (K)stream.readObject();
            map.put(key, this.createCollection(key));
        }
        for (int entries = stream.readInt(), j = 0; j < entries; ++j) {
            final K key2 = (K)stream.readObject();
            final V value = (V)stream.readObject();
            Objects.requireNonNull(map.get(key2)).add(value);
        }
        this.setMap(map);
    }
    
    @VisibleForTesting
    static final class ValueEntry<K, V> extends ImmutableEntry<K, V> implements ValueSetLink<K, V>
    {
        final int smearedValueHash;
        @CheckForNull
        ValueEntry<K, V> nextInValueBucket;
        @CheckForNull
        ValueSetLink<K, V> predecessorInValueSet;
        @CheckForNull
        ValueSetLink<K, V> successorInValueSet;
        @CheckForNull
        ValueEntry<K, V> predecessorInMultimap;
        @CheckForNull
        ValueEntry<K, V> successorInMultimap;
        
        ValueEntry(@ParametricNullness final K key, @ParametricNullness final V value, final int smearedValueHash, @CheckForNull final ValueEntry<K, V> nextInValueBucket) {
            super(key, value);
            this.smearedValueHash = smearedValueHash;
            this.nextInValueBucket = nextInValueBucket;
        }
        
        static <K, V> ValueEntry<K, V> newHeader() {
            return new ValueEntry<K, V>(null, null, 0, null);
        }
        
        boolean matchesValue(@CheckForNull final Object v, final int smearedVHash) {
            return this.smearedValueHash == smearedVHash && com.google.common.base.Objects.equal(this.getValue(), v);
        }
        
        @Override
        public ValueSetLink<K, V> getPredecessorInValueSet() {
            return Objects.requireNonNull(this.predecessorInValueSet);
        }
        
        @Override
        public ValueSetLink<K, V> getSuccessorInValueSet() {
            return Objects.requireNonNull(this.successorInValueSet);
        }
        
        @Override
        public void setPredecessorInValueSet(final ValueSetLink<K, V> entry) {
            this.predecessorInValueSet = entry;
        }
        
        @Override
        public void setSuccessorInValueSet(final ValueSetLink<K, V> entry) {
            this.successorInValueSet = entry;
        }
        
        public ValueEntry<K, V> getPredecessorInMultimap() {
            return Objects.requireNonNull(this.predecessorInMultimap);
        }
        
        public ValueEntry<K, V> getSuccessorInMultimap() {
            return Objects.requireNonNull(this.successorInMultimap);
        }
        
        public void setSuccessorInMultimap(final ValueEntry<K, V> multimapSuccessor) {
            this.successorInMultimap = multimapSuccessor;
        }
        
        public void setPredecessorInMultimap(final ValueEntry<K, V> multimapPredecessor) {
            this.predecessorInMultimap = multimapPredecessor;
        }
    }
    
    @VisibleForTesting
    final class ValueSet extends Sets.ImprovedAbstractSet<V> implements ValueSetLink<K, V>
    {
        @ParametricNullness
        private final K key;
        @VisibleForTesting
        ValueEntry<K, V>[] hashTable;
        private int size;
        private int modCount;
        private ValueSetLink<K, V> firstEntry;
        private ValueSetLink<K, V> lastEntry;
        
        ValueSet(final K key, final int expectedValues) {
            this.size = 0;
            this.modCount = 0;
            this.key = key;
            this.firstEntry = this;
            this.lastEntry = this;
            final int tableSize = Hashing.closedTableSize(expectedValues, 1.0);
            final ValueEntry<K, V>[] hashTable = (ValueEntry<K, V>[])new ValueEntry[tableSize];
            this.hashTable = hashTable;
        }
        
        private int mask() {
            return this.hashTable.length - 1;
        }
        
        @Override
        public ValueSetLink<K, V> getPredecessorInValueSet() {
            return this.lastEntry;
        }
        
        @Override
        public ValueSetLink<K, V> getSuccessorInValueSet() {
            return this.firstEntry;
        }
        
        @Override
        public void setPredecessorInValueSet(final ValueSetLink<K, V> entry) {
            this.lastEntry = entry;
        }
        
        @Override
        public void setSuccessorInValueSet(final ValueSetLink<K, V> entry) {
            this.firstEntry = entry;
        }
        
        @Override
        public Iterator<V> iterator() {
            return new Iterator<V>() {
                ValueSetLink<K, V> nextEntry = ValueSet.this.firstEntry;
                @CheckForNull
                ValueEntry<K, V> toRemove;
                int expectedModCount = ValueSet.this.modCount;
                
                private void checkForComodification() {
                    if (ValueSet.this.modCount != this.expectedModCount) {
                        throw new ConcurrentModificationException();
                    }
                }
                
                @Override
                public boolean hasNext() {
                    this.checkForComodification();
                    return this.nextEntry != ValueSet.this;
                }
                
                @ParametricNullness
                @Override
                public V next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final ValueEntry<K, V> entry = (ValueEntry<K, V>)(ValueEntry)this.nextEntry;
                    final V result = entry.getValue();
                    this.toRemove = entry;
                    this.nextEntry = entry.getSuccessorInValueSet();
                    return result;
                }
                
                @Override
                public void remove() {
                    this.checkForComodification();
                    Preconditions.checkState(this.toRemove != null, (Object)"no calls to next() since the last call to remove()");
                    ValueSet.this.remove(this.toRemove.getValue());
                    this.expectedModCount = ValueSet.this.modCount;
                    this.toRemove = null;
                }
            };
        }
        
        @Override
        public void forEach(final Consumer<? super V> action) {
            Preconditions.checkNotNull(action);
            for (ValueSetLink<K, V> entry = this.firstEntry; entry != this; entry = entry.getSuccessorInValueSet()) {
                action.accept((Object)((ValueEntry)entry).getValue());
            }
        }
        
        @Override
        public int size() {
            return this.size;
        }
        
        @Override
        public boolean contains(@CheckForNull final Object o) {
            final int smearedHash = Hashing.smearedHash(o);
            for (ValueEntry<K, V> entry = this.hashTable[smearedHash & this.mask()]; entry != null; entry = entry.nextInValueBucket) {
                if (entry.matchesValue(o, smearedHash)) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public boolean add(@ParametricNullness final V value) {
            final int smearedHash = Hashing.smearedHash(value);
            final int bucket = smearedHash & this.mask();
            ValueEntry<K, V> entry;
            ValueEntry<K, V> rowHead;
            for (rowHead = (entry = this.hashTable[bucket]); entry != null; entry = entry.nextInValueBucket) {
                if (entry.matchesValue(value, smearedHash)) {
                    return false;
                }
            }
            final ValueEntry<K, V> newEntry = new ValueEntry<K, V>(this.key, value, smearedHash, rowHead);
            succeedsInValueSet(this.lastEntry, (ValueSetLink<Object, Object>)newEntry);
            succeedsInValueSet(newEntry, (ValueSetLink<Object, Object>)this);
            succeedsInMultimap(LinkedHashMultimap.this.multimapHeaderEntry.getPredecessorInMultimap(), (ValueEntry<Object, Object>)newEntry);
            succeedsInMultimap(newEntry, (ValueEntry<Object, Object>)LinkedHashMultimap.this.multimapHeaderEntry);
            this.hashTable[bucket] = newEntry;
            ++this.size;
            ++this.modCount;
            this.rehashIfNecessary();
            return true;
        }
        
        private void rehashIfNecessary() {
            if (Hashing.needsResizing(this.size, this.hashTable.length, 1.0)) {
                final ValueEntry<K, V>[] hashTable = (ValueEntry<K, V>[])new ValueEntry[this.hashTable.length * 2];
                this.hashTable = hashTable;
                final int mask = hashTable.length - 1;
                for (ValueSetLink<K, V> entry = this.firstEntry; entry != this; entry = entry.getSuccessorInValueSet()) {
                    final ValueEntry<K, V> valueEntry = (ValueEntry<K, V>)(ValueEntry)entry;
                    final int bucket = valueEntry.smearedValueHash & mask;
                    valueEntry.nextInValueBucket = hashTable[bucket];
                    hashTable[bucket] = valueEntry;
                }
            }
        }
        
        @CanIgnoreReturnValue
        @Override
        public boolean remove(@CheckForNull final Object o) {
            final int smearedHash = Hashing.smearedHash(o);
            final int bucket = smearedHash & this.mask();
            ValueEntry<K, V> prev = null;
            for (ValueEntry<K, V> entry = this.hashTable[bucket]; entry != null; entry = entry.nextInValueBucket) {
                if (entry.matchesValue(o, smearedHash)) {
                    if (prev == null) {
                        this.hashTable[bucket] = entry.nextInValueBucket;
                    }
                    else {
                        prev.nextInValueBucket = entry.nextInValueBucket;
                    }
                    deleteFromValueSet((ValueSetLink<Object, Object>)entry);
                    deleteFromMultimap((ValueEntry<Object, Object>)entry);
                    --this.size;
                    ++this.modCount;
                    return true;
                }
                prev = entry;
            }
            return false;
        }
        
        @Override
        public void clear() {
            Arrays.fill(this.hashTable, null);
            this.size = 0;
            for (ValueSetLink<K, V> entry = this.firstEntry; entry != this; entry = entry.getSuccessorInValueSet()) {
                final ValueEntry<K, V> valueEntry = (ValueEntry<K, V>)(ValueEntry)entry;
                deleteFromMultimap((ValueEntry<Object, Object>)valueEntry);
            }
            succeedsInValueSet(this, (ValueSetLink<Object, Object>)this);
            ++this.modCount;
        }
    }
    
    private interface ValueSetLink<K, V>
    {
        ValueSetLink<K, V> getPredecessorInValueSet();
        
        ValueSetLink<K, V> getSuccessorInValueSet();
        
        void setPredecessorInValueSet(final ValueSetLink<K, V> p0);
        
        void setSuccessorInValueSet(final ValueSetLink<K, V> p0);
    }
}
