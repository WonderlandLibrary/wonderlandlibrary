// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.flare;

import java.util.function.Consumer;
import java.util.NoSuchElementException;
import java.util.AbstractSet;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.Objects;
import java.util.Iterator;
import java.util.function.IntFunction;
import java.util.Map;
import java.util.AbstractMap;

final class SyncMapImpl<K, V> extends AbstractMap<K, V> implements SyncMap<K, V>
{
    private final transient Object lock;
    private transient volatile Map<K, ExpungingEntry<V>> read;
    private transient volatile boolean amended;
    private transient Map<K, ExpungingEntry<V>> dirty;
    private transient int misses;
    private final transient IntFunction<Map<K, ExpungingEntry<V>>> function;
    private transient EntrySetView entrySet;
    
    SyncMapImpl(final IntFunction<Map<K, ExpungingEntry<V>>> function, final int initialCapacity) {
        this.lock = new Object();
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Initial capacity must be greater than 0");
        }
        this.function = function;
        this.read = function.apply(initialCapacity);
    }
    
    @Override
    public int size() {
        this.promote();
        int size = 0;
        for (final ExpungingEntry<V> value : this.read.values()) {
            if (value.exists()) {
                ++size;
            }
        }
        return size;
    }
    
    @Override
    public boolean isEmpty() {
        this.promote();
        for (final ExpungingEntry<V> value : this.read.values()) {
            if (value.exists()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean containsKey(final Object key) {
        final ExpungingEntry<V> entry = this.getEntry(key);
        return entry != null && entry.exists();
    }
    
    @Override
    public V get(final Object key) {
        final ExpungingEntry<V> entry = this.getEntry(key);
        return (entry != null) ? entry.get() : null;
    }
    
    @Override
    public V getOrDefault(final Object key, final V defaultValue) {
        Objects.requireNonNull(defaultValue, "defaultValue");
        final ExpungingEntry<V> entry = this.getEntry(key);
        return (entry != null) ? entry.getOr(defaultValue) : defaultValue;
    }
    
    private ExpungingEntry<V> getEntry(final Object key) {
        ExpungingEntry<V> entry = this.read.get(key);
        if (entry == null && this.amended) {
            synchronized (this.lock) {
                if ((entry = this.read.get(key)) == null && this.amended && this.dirty != null) {
                    entry = this.dirty.get(key);
                    this.missLocked();
                }
            }
        }
        return entry;
    }
    
    @Override
    public V computeIfAbsent(final K key, final Function<? super K, ? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction, "mappingFunction");
        ExpungingEntry<V> entry = this.read.get(key);
        InsertionResult<V> result = (entry != null) ? entry.computeIfAbsent(key, mappingFunction) : null;
        if (result == null || result.operation() == 2) {
            synchronized (this.lock) {
                if ((entry = this.read.get(key)) != null) {
                    if (entry.tryUnexpungeAndCompute(key, mappingFunction)) {
                        if (entry.exists()) {
                            this.dirty.put(key, entry);
                        }
                        return entry.get();
                    }
                    result = entry.computeIfAbsent(key, mappingFunction);
                }
                else {
                    if (this.dirty == null || (entry = this.dirty.get(key)) == null) {
                        if (!this.amended) {
                            this.dirtyLocked();
                            this.amended = true;
                        }
                        final V computed = (V)mappingFunction.apply((Object)key);
                        if (computed != null) {
                            this.dirty.put(key, new ExpungingEntryImpl<V>(computed));
                        }
                        return computed;
                    }
                    result = entry.computeIfAbsent(key, mappingFunction);
                    if (result.current() == null) {
                        this.dirty.remove(key);
                    }
                    this.missLocked();
                }
            }
        }
        return result.current();
    }
    
    @Override
    public V computeIfPresent(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction, "remappingFunction");
        ExpungingEntry<V> entry = this.read.get(key);
        InsertionResult<V> result = (entry != null) ? entry.computeIfPresent(key, remappingFunction) : null;
        if (result == null || result.operation() == 2) {
            synchronized (this.lock) {
                if ((entry = this.read.get(key)) != null) {
                    result = entry.computeIfPresent(key, remappingFunction);
                }
                else if (this.dirty != null && (entry = this.dirty.get(key)) != null) {
                    result = entry.computeIfPresent(key, remappingFunction);
                    if (result.current() == null) {
                        this.dirty.remove(key);
                    }
                    this.missLocked();
                }
            }
        }
        return (result != null) ? result.current() : null;
    }
    
    @Override
    public V compute(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction, "remappingFunction");
        ExpungingEntry<V> entry = this.read.get(key);
        InsertionResult<V> result = (entry != null) ? entry.compute(key, remappingFunction) : null;
        if (result == null || result.operation() == 2) {
            synchronized (this.lock) {
                if ((entry = this.read.get(key)) != null) {
                    if (entry.tryUnexpungeAndCompute(key, remappingFunction)) {
                        if (entry.exists()) {
                            this.dirty.put(key, entry);
                        }
                        return entry.get();
                    }
                    result = entry.compute(key, remappingFunction);
                }
                else {
                    if (this.dirty == null || (entry = this.dirty.get(key)) == null) {
                        if (!this.amended) {
                            this.dirtyLocked();
                            this.amended = true;
                        }
                        final V computed = (V)remappingFunction.apply((Object)key, (Object)null);
                        if (computed != null) {
                            this.dirty.put(key, new ExpungingEntryImpl<V>(computed));
                        }
                        return computed;
                    }
                    result = entry.compute(key, remappingFunction);
                    if (result.current() == null) {
                        this.dirty.remove(key);
                    }
                    this.missLocked();
                }
            }
        }
        return result.current();
    }
    
    @Override
    public V putIfAbsent(final K key, final V value) {
        Objects.requireNonNull(value, "value");
        ExpungingEntry<V> entry = this.read.get(key);
        InsertionResult<V> result = (entry != null) ? entry.setIfAbsent(value) : null;
        if (result == null || result.operation() == 2) {
            synchronized (this.lock) {
                if ((entry = this.read.get(key)) != null) {
                    if (entry.tryUnexpungeAndSet(value)) {
                        this.dirty.put(key, entry);
                    }
                    else {
                        result = entry.setIfAbsent(value);
                    }
                }
                else if (this.dirty != null && (entry = this.dirty.get(key)) != null) {
                    result = entry.setIfAbsent(value);
                    this.missLocked();
                }
                else {
                    if (!this.amended) {
                        this.dirtyLocked();
                        this.amended = true;
                    }
                    this.dirty.put(key, new ExpungingEntryImpl<V>(value));
                }
            }
        }
        return (result != null) ? result.previous() : null;
    }
    
    @Override
    public V put(final K key, final V value) {
        Objects.requireNonNull(value, "value");
        ExpungingEntry<V> entry = this.read.get(key);
        V previous = (entry != null) ? entry.get() : null;
        if (entry != null && entry.trySet(value)) {
            return previous;
        }
        synchronized (this.lock) {
            if ((entry = this.read.get(key)) != null) {
                previous = entry.get();
                if (entry.tryUnexpungeAndSet(value)) {
                    this.dirty.put(key, entry);
                }
                else {
                    entry.set(value);
                }
            }
            else if (this.dirty != null && (entry = this.dirty.get(key)) != null) {
                previous = entry.get();
                entry.set(value);
                this.missLocked();
            }
            else {
                if (!this.amended) {
                    this.dirtyLocked();
                    this.amended = true;
                }
                this.dirty.put(key, new ExpungingEntryImpl<V>(value));
            }
        }
        return previous;
    }
    
    @Override
    public V remove(final Object key) {
        ExpungingEntry<V> entry = this.read.get(key);
        if (entry == null && this.amended) {
            synchronized (this.lock) {
                if ((entry = this.read.get(key)) == null && this.amended && this.dirty != null) {
                    entry = this.dirty.remove(key);
                    this.missLocked();
                }
            }
        }
        return (entry != null) ? entry.clear() : null;
    }
    
    @Override
    public boolean remove(final Object key, final Object value) {
        Objects.requireNonNull(value, "value");
        ExpungingEntry<V> entry = this.read.get(key);
        if (entry == null && this.amended) {
            synchronized (this.lock) {
                if ((entry = this.read.get(key)) == null && this.amended && this.dirty != null) {
                    final boolean present = (entry = this.dirty.get(key)) != null && entry.replace(value, null);
                    if (present) {
                        this.dirty.remove(key);
                    }
                    this.missLocked();
                    return present;
                }
            }
        }
        return entry != null && entry.replace(value, null);
    }
    
    @Override
    public V replace(final K key, final V value) {
        Objects.requireNonNull(value, "value");
        final ExpungingEntry<V> entry = this.getEntry(key);
        return (entry != null) ? entry.tryReplace(value) : null;
    }
    
    @Override
    public boolean replace(final K key, final V oldValue, final V newValue) {
        Objects.requireNonNull(oldValue, "oldValue");
        Objects.requireNonNull(newValue, "newValue");
        final ExpungingEntry<V> entry = this.getEntry(key);
        return entry != null && entry.replace(oldValue, newValue);
    }
    
    @Override
    public void forEach(final BiConsumer<? super K, ? super V> action) {
        Objects.requireNonNull(action, "action");
        this.promote();
        for (final Map.Entry<K, ExpungingEntry<V>> that : this.read.entrySet()) {
            final V value;
            if ((value = that.getValue().get()) != null) {
                action.accept((Object)that.getKey(), (Object)value);
            }
        }
    }
    
    @Override
    public void replaceAll(final BiFunction<? super K, ? super V, ? extends V> function) {
        Objects.requireNonNull(function, "function");
        this.promote();
        for (final Map.Entry<K, ExpungingEntry<V>> that : this.read.entrySet()) {
            final ExpungingEntry<V> entry;
            final V value;
            if ((value = (entry = that.getValue()).get()) != null) {
                entry.tryReplace((V)function.apply((Object)that.getKey(), (Object)value));
            }
        }
    }
    
    @Override
    public void clear() {
        synchronized (this.lock) {
            this.read = this.function.apply(this.read.size());
            this.dirty = null;
            this.amended = false;
            this.misses = 0;
        }
    }
    
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        if (this.entrySet != null) {
            return this.entrySet;
        }
        return this.entrySet = new EntrySetView();
    }
    
    private void promote() {
        if (this.amended) {
            synchronized (this.lock) {
                if (this.amended) {
                    this.promoteLocked();
                }
            }
        }
    }
    
    private void promoteLocked() {
        if (this.dirty != null) {
            this.read = this.dirty;
        }
        this.amended = false;
        this.dirty = null;
        this.misses = 0;
    }
    
    private void missLocked() {
        if (++this.misses >= this.dirty.size()) {
            this.promoteLocked();
        }
    }
    
    private void dirtyLocked() {
        if (this.dirty != null) {
            return;
        }
        this.dirty = this.function.apply(this.read.size());
        for (final Map.Entry<K, ExpungingEntry<V>> entry : this.read.entrySet()) {
            if (!entry.getValue().tryExpunge()) {
                this.dirty.put(entry.getKey(), entry.getValue());
            }
        }
    }
    
    static final class ExpungingEntryImpl<V> implements ExpungingEntry<V>
    {
        private static final AtomicReferenceFieldUpdater<ExpungingEntryImpl, Object> UPDATER;
        private static final Object EXPUNGED;
        private volatile Object value;
        
        ExpungingEntryImpl(final V value) {
            this.value = value;
        }
        
        @Override
        public boolean exists() {
            return this.value != null && this.value != ExpungingEntryImpl.EXPUNGED;
        }
        
        @Override
        public V get() {
            return (V)((this.value == ExpungingEntryImpl.EXPUNGED) ? null : this.value);
        }
        
        @Override
        public V getOr(final V other) {
            final Object value = this.value;
            return (V)((value != null && value != ExpungingEntryImpl.EXPUNGED) ? this.value : other);
        }
        
        @Override
        public InsertionResult<V> setIfAbsent(final V value) {
            while (true) {
                final Object previous = this.value;
                if (previous == ExpungingEntryImpl.EXPUNGED) {
                    return new InsertionResultImpl<V>((byte)2, null, null);
                }
                if (previous != null) {
                    return new InsertionResultImpl<V>((byte)0, (V)previous, (V)previous);
                }
                if (ExpungingEntryImpl.UPDATER.compareAndSet(this, null, value)) {
                    return new InsertionResultImpl<V>((byte)1, null, value);
                }
            }
        }
        
        @Override
        public <K> InsertionResult<V> computeIfAbsent(final K key, final Function<? super K, ? extends V> function) {
            V next = null;
            while (true) {
                final Object previous = this.value;
                if (previous == ExpungingEntryImpl.EXPUNGED) {
                    return new InsertionResultImpl<V>((byte)2, null, null);
                }
                if (previous != null) {
                    return new InsertionResultImpl<V>((byte)0, (V)previous, (V)previous);
                }
                if (ExpungingEntryImpl.UPDATER.compareAndSet(this, null, (next != null) ? next : (next = (V)function.apply((Object)key)))) {
                    return new InsertionResultImpl<V>((byte)1, null, next);
                }
            }
        }
        
        @Override
        public <K> InsertionResult<V> computeIfPresent(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
            V next = null;
            while (true) {
                final Object previous = this.value;
                if (previous == ExpungingEntryImpl.EXPUNGED) {
                    return new InsertionResultImpl<V>((byte)2, null, null);
                }
                if (previous == null) {
                    return new InsertionResultImpl<V>((byte)0, null, null);
                }
                if (ExpungingEntryImpl.UPDATER.compareAndSet(this, previous, (next != null) ? next : (next = (V)remappingFunction.apply((Object)key, (Object)previous)))) {
                    return new InsertionResultImpl<V>((byte)1, (V)previous, next);
                }
            }
        }
        
        @Override
        public <K> InsertionResult<V> compute(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
            V next = null;
            while (true) {
                final Object previous = this.value;
                if (previous == ExpungingEntryImpl.EXPUNGED) {
                    return new InsertionResultImpl<V>((byte)2, null, null);
                }
                if (ExpungingEntryImpl.UPDATER.compareAndSet(this, previous, (next != null) ? next : (next = (V)remappingFunction.apply((Object)key, (Object)previous)))) {
                    return new InsertionResultImpl<V>((byte)((previous != next) ? 1 : 0), (V)previous, next);
                }
            }
        }
        
        @Override
        public void set(final V value) {
            ExpungingEntryImpl.UPDATER.set(this, value);
        }
        
        @Override
        public boolean replace(final Object compare, final V value) {
            while (true) {
                final Object previous = this.value;
                if (previous == ExpungingEntryImpl.EXPUNGED || !Objects.equals(previous, compare)) {
                    return false;
                }
                if (ExpungingEntryImpl.UPDATER.compareAndSet(this, previous, value)) {
                    return true;
                }
            }
        }
        
        @Override
        public V clear() {
            while (true) {
                final Object previous = this.value;
                if (previous == null || previous == ExpungingEntryImpl.EXPUNGED) {
                    return null;
                }
                if (ExpungingEntryImpl.UPDATER.compareAndSet(this, previous, null)) {
                    return (V)previous;
                }
            }
        }
        
        @Override
        public boolean trySet(final V value) {
            while (true) {
                final Object previous = this.value;
                if (previous == ExpungingEntryImpl.EXPUNGED) {
                    return false;
                }
                if (ExpungingEntryImpl.UPDATER.compareAndSet(this, previous, value)) {
                    return true;
                }
            }
        }
        
        @Override
        public V tryReplace(final V value) {
            while (true) {
                final Object previous = this.value;
                if (previous == null || previous == ExpungingEntryImpl.EXPUNGED) {
                    return null;
                }
                if (ExpungingEntryImpl.UPDATER.compareAndSet(this, previous, value)) {
                    return (V)previous;
                }
            }
        }
        
        @Override
        public boolean tryExpunge() {
            while (this.value == null) {
                if (ExpungingEntryImpl.UPDATER.compareAndSet(this, null, ExpungingEntryImpl.EXPUNGED)) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public boolean tryUnexpungeAndSet(final V value) {
            return ExpungingEntryImpl.UPDATER.compareAndSet(this, ExpungingEntryImpl.EXPUNGED, value);
        }
        
        @Override
        public <K> boolean tryUnexpungeAndCompute(final K key, final Function<? super K, ? extends V> function) {
            if (this.value == ExpungingEntryImpl.EXPUNGED) {
                final Object value = function.apply((Object)key);
                return ExpungingEntryImpl.UPDATER.compareAndSet(this, ExpungingEntryImpl.EXPUNGED, value);
            }
            return false;
        }
        
        @Override
        public <K> boolean tryUnexpungeAndCompute(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
            if (this.value == ExpungingEntryImpl.EXPUNGED) {
                final Object value = remappingFunction.apply((Object)key, (Object)null);
                return ExpungingEntryImpl.UPDATER.compareAndSet(this, ExpungingEntryImpl.EXPUNGED, value);
            }
            return false;
        }
        
        static {
            UPDATER = AtomicReferenceFieldUpdater.newUpdater(ExpungingEntryImpl.class, Object.class, "value");
            EXPUNGED = new Object();
        }
    }
    
    static final class InsertionResultImpl<V> implements InsertionResult<V>
    {
        private static final byte UNCHANGED = 0;
        private static final byte UPDATED = 1;
        private static final byte EXPUNGED = 2;
        private final byte operation;
        private final V previous;
        private final V current;
        
        InsertionResultImpl(final byte operation, final V previous, final V current) {
            this.operation = operation;
            this.previous = previous;
            this.current = current;
        }
        
        @Override
        public byte operation() {
            return this.operation;
        }
        
        @Override
        public V previous() {
            return this.previous;
        }
        
        @Override
        public V current() {
            return this.current;
        }
    }
    
    final class MapEntry implements Map.Entry<K, V>
    {
        private final K key;
        private V value;
        
        MapEntry(final K key, final V value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public K getKey() {
            return this.key;
        }
        
        @Override
        public V getValue() {
            return this.value;
        }
        
        @Override
        public V setValue(final V value) {
            Objects.requireNonNull(value, "value");
            final SyncMapImpl this$0 = SyncMapImpl.this;
            final K key = this.key;
            this.value = value;
            return this$0.put(key, value);
        }
        
        @Override
        public String toString() {
            return "SyncMapImpl.MapEntry{key=" + this.getKey() + ", value=" + this.getValue() + "}";
        }
        
        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> that = (Map.Entry<?, ?>)other;
            return Objects.equals(this.getKey(), that.getKey()) && Objects.equals(this.getValue(), that.getValue());
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(this.getKey(), this.getValue());
        }
    }
    
    final class EntrySetView extends AbstractSet<Map.Entry<K, V>>
    {
        @Override
        public int size() {
            return SyncMapImpl.this.size();
        }
        
        @Override
        public boolean contains(final Object entry) {
            if (!(entry instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> mapEntry = (Map.Entry<?, ?>)entry;
            final V value = SyncMapImpl.this.get(mapEntry.getKey());
            return value != null && Objects.equals(value, mapEntry.getValue());
        }
        
        @Override
        public boolean remove(final Object entry) {
            if (!(entry instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> mapEntry = (Map.Entry<?, ?>)entry;
            return SyncMapImpl.this.remove(mapEntry.getKey()) != null;
        }
        
        @Override
        public void clear() {
            SyncMapImpl.this.clear();
        }
        
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            SyncMapImpl.this.promote();
            return new EntryIterator(SyncMapImpl.this.read.entrySet().iterator());
        }
    }
    
    final class EntryIterator implements Iterator<Map.Entry<K, V>>
    {
        private final Iterator<Map.Entry<K, ExpungingEntry<V>>> backingIterator;
        private Map.Entry<K, V> next;
        private Map.Entry<K, V> current;
        
        EntryIterator(final Iterator<Map.Entry<K, ExpungingEntry<V>>> backingIterator) {
            this.backingIterator = backingIterator;
            this.next = this.nextValue();
        }
        
        @Override
        public boolean hasNext() {
            return this.next != null;
        }
        
        @Override
        public Map.Entry<K, V> next() {
            final Map.Entry<K, V> next = this.next;
            this.current = next;
            if (next == null) {
                throw new NoSuchElementException();
            }
            this.next = this.nextValue();
            return this.current;
        }
        
        private Map.Entry<K, V> nextValue() {
            while (this.backingIterator.hasNext()) {
                final Map.Entry<K, ExpungingEntry<V>> entry;
                final V value;
                if ((value = (entry = this.backingIterator.next()).getValue().get()) != null) {
                    return new MapEntry(entry.getKey(), value);
                }
            }
            return null;
        }
        
        @Override
        public void remove() {
            if (this.current == null) {
                throw new IllegalStateException();
            }
            SyncMapImpl.this.remove(this.current.getKey());
            this.current = null;
        }
        
        @Override
        public void forEachRemaining(final Consumer<? super Map.Entry<K, V>> action) {
            Objects.requireNonNull(action, "action");
            if (this.next != null) {
                action.accept(this.next);
            }
            final V value;
            this.backingIterator.forEachRemaining(entry -> {
                value = entry.getValue().get();
                if (value != null) {
                    action.accept((Object)new MapEntry((K)entry.getKey(), value));
                }
            });
        }
    }
}
