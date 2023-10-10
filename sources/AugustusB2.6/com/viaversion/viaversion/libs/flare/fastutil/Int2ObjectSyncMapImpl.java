// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.flare.fastutil;

import java.util.function.Consumer;
import java.util.NoSuchElementException;
import com.viaversion.viaversion.libs.fastutil.objects.AbstractObjectSet;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMaps;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSet;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectFunction;
import java.util.Objects;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
import java.util.function.IntFunction;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
import com.viaversion.viaversion.libs.fastutil.ints.AbstractInt2ObjectMap;

final class Int2ObjectSyncMapImpl<V> extends AbstractInt2ObjectMap<V> implements Int2ObjectSyncMap<V>
{
    private static final long serialVersionUID = 1L;
    private final transient Object lock;
    private transient volatile Int2ObjectMap<ExpungingEntry<V>> read;
    private transient volatile boolean amended;
    private transient Int2ObjectMap<ExpungingEntry<V>> dirty;
    private transient int misses;
    private final transient IntFunction<Int2ObjectMap<ExpungingEntry<V>>> function;
    private transient EntrySetView entrySet;
    
    Int2ObjectSyncMapImpl(final IntFunction<Int2ObjectMap<ExpungingEntry<V>>> function, final int initialCapacity) {
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
    public boolean containsValue(final Object value) {
        for (final Int2ObjectMap.Entry<V> entry : this.int2ObjectEntrySet()) {
            if (Objects.equals(entry.getValue(), value)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean containsKey(final int key) {
        final ExpungingEntry<V> entry = this.getEntry(key);
        return entry != null && entry.exists();
    }
    
    @Override
    public V get(final int key) {
        final ExpungingEntry<V> entry = this.getEntry(key);
        return (entry != null) ? entry.get() : null;
    }
    
    @Override
    public V getOrDefault(final int key, final V defaultValue) {
        Objects.requireNonNull(defaultValue, "defaultValue");
        final ExpungingEntry<V> entry = this.getEntry(key);
        return (entry != null) ? entry.getOr(defaultValue) : defaultValue;
    }
    
    public ExpungingEntry<V> getEntry(final int key) {
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
    public V computeIfAbsent(final int key, final IntFunction<? extends V> mappingFunction) {
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
                        final V computed = (V)mappingFunction.apply(key);
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
    public V computeIfAbsent(final int key, final Int2ObjectFunction<? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction, "mappingFunction");
        ExpungingEntry<V> entry = this.read.get(key);
        InsertionResult<V> result = (entry != null) ? entry.computeIfAbsentPrimitive(key, mappingFunction) : null;
        if (result == null || result.operation() == 2) {
            synchronized (this.lock) {
                if ((entry = this.read.get(key)) != null) {
                    if (entry.tryUnexpungeAndComputePrimitive(key, mappingFunction)) {
                        if (entry.exists()) {
                            this.dirty.put(key, entry);
                        }
                        return entry.get();
                    }
                    result = entry.computeIfAbsentPrimitive(key, mappingFunction);
                }
                else {
                    if (this.dirty == null || (entry = this.dirty.get(key)) == null) {
                        if (!this.amended) {
                            this.dirtyLocked();
                            this.amended = true;
                        }
                        final V computed = (V)mappingFunction.get(key);
                        if (computed != null) {
                            this.dirty.put(key, new ExpungingEntryImpl<V>(computed));
                        }
                        return computed;
                    }
                    result = entry.computeIfAbsentPrimitive(key, mappingFunction);
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
    public V computeIfPresent(final int key, final BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
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
    public V compute(final int key, final BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
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
                        final V computed = (V)remappingFunction.apply(Integer.valueOf(key), (Object)null);
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
    public V putIfAbsent(final int key, final V value) {
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
    public V put(final int key, final V value) {
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
    public V remove(final int key) {
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
    public boolean remove(final int key, final Object value) {
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
    public V replace(final int key, final V value) {
        Objects.requireNonNull(value, "value");
        final ExpungingEntry<V> entry = this.getEntry(key);
        return (entry != null) ? entry.tryReplace(value) : null;
    }
    
    @Override
    public boolean replace(final int key, final V oldValue, final V newValue) {
        Objects.requireNonNull(oldValue, "oldValue");
        Objects.requireNonNull(newValue, "newValue");
        final ExpungingEntry<V> entry = this.getEntry(key);
        return entry != null && entry.replace(oldValue, newValue);
    }
    
    @Override
    public void forEach(final BiConsumer<? super Integer, ? super V> action) {
        Objects.requireNonNull(action, "action");
        this.promote();
        for (final Int2ObjectMap.Entry<ExpungingEntry<V>> that : this.read.int2ObjectEntrySet()) {
            final V value;
            if ((value = (V)that.getValue().get()) != null) {
                action.accept(Integer.valueOf(that.getIntKey()), (Object)value);
            }
        }
    }
    
    @Override
    public void putAll(final Map<? extends Integer, ? extends V> map) {
        Objects.requireNonNull(map, "map");
        for (final Map.Entry<? extends Integer, ? extends V> entry : map.entrySet()) {
            this.put((int)entry.getKey(), entry.getValue());
        }
    }
    
    @Override
    public void replaceAll(final BiFunction<? super Integer, ? super V, ? extends V> function) {
        Objects.requireNonNull(function, "function");
        this.promote();
        for (final Int2ObjectMap.Entry<ExpungingEntry<V>> that : this.read.int2ObjectEntrySet()) {
            final ExpungingEntry<V> entry;
            final V value;
            if ((value = (entry = that.getValue()).get()) != null) {
                entry.tryReplace((V)function.apply(Integer.valueOf(that.getIntKey()), (Object)value));
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
    public ObjectSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet() {
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
        Int2ObjectMaps.fastForEach(this.read, entry -> {
            if (!entry.getValue().tryExpunge()) {
                this.dirty.put(((Int2ObjectMap.Entry)entry).getIntKey(), entry.getValue());
            }
        });
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
        public InsertionResult<V> computeIfAbsent(final int key, final IntFunction<? extends V> function) {
            V next = null;
            while (true) {
                final Object previous = this.value;
                if (previous == ExpungingEntryImpl.EXPUNGED) {
                    return new InsertionResultImpl<V>((byte)2, null, null);
                }
                if (previous != null) {
                    return new InsertionResultImpl<V>((byte)0, (V)previous, (V)previous);
                }
                if (ExpungingEntryImpl.UPDATER.compareAndSet(this, null, (next != null) ? next : (next = (V)function.apply(key)))) {
                    return new InsertionResultImpl<V>((byte)1, null, next);
                }
            }
        }
        
        @Override
        public InsertionResult<V> computeIfAbsentPrimitive(final int key, final Int2ObjectFunction<? extends V> function) {
            V next = null;
            while (true) {
                final Object previous = this.value;
                if (previous == ExpungingEntryImpl.EXPUNGED) {
                    return new InsertionResultImpl<V>((byte)2, null, null);
                }
                if (previous != null) {
                    return new InsertionResultImpl<V>((byte)0, (V)previous, (V)previous);
                }
                if (ExpungingEntryImpl.UPDATER.compareAndSet(this, null, (next != null) ? next : (next = (function.containsKey(key) ? function.get(key) : null)))) {
                    return new InsertionResultImpl<V>((byte)1, null, next);
                }
            }
        }
        
        @Override
        public InsertionResult<V> computeIfPresent(final int key, final BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
            V next = null;
            while (true) {
                final Object previous = this.value;
                if (previous == ExpungingEntryImpl.EXPUNGED) {
                    return new InsertionResultImpl<V>((byte)2, null, null);
                }
                if (previous == null) {
                    return new InsertionResultImpl<V>((byte)0, null, null);
                }
                if (ExpungingEntryImpl.UPDATER.compareAndSet(this, previous, (next != null) ? next : (next = (V)remappingFunction.apply(Integer.valueOf(key), (Object)previous)))) {
                    return new InsertionResultImpl<V>((byte)1, (V)previous, next);
                }
            }
        }
        
        @Override
        public InsertionResult<V> compute(final int key, final BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
            V next = null;
            while (true) {
                final Object previous = this.value;
                if (previous == ExpungingEntryImpl.EXPUNGED) {
                    return new InsertionResultImpl<V>((byte)2, null, null);
                }
                if (ExpungingEntryImpl.UPDATER.compareAndSet(this, previous, (next != null) ? next : (next = (V)remappingFunction.apply(Integer.valueOf(key), (Object)previous)))) {
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
        public boolean tryUnexpungeAndCompute(final int key, final IntFunction<? extends V> function) {
            if (this.value == ExpungingEntryImpl.EXPUNGED) {
                final Object value = function.apply(key);
                return ExpungingEntryImpl.UPDATER.compareAndSet(this, ExpungingEntryImpl.EXPUNGED, value);
            }
            return false;
        }
        
        @Override
        public boolean tryUnexpungeAndComputePrimitive(final int key, final Int2ObjectFunction<? extends V> function) {
            if (this.value == ExpungingEntryImpl.EXPUNGED) {
                final Object value = function.containsKey(key) ? function.get(key) : null;
                return ExpungingEntryImpl.UPDATER.compareAndSet(this, ExpungingEntryImpl.EXPUNGED, value);
            }
            return false;
        }
        
        @Override
        public boolean tryUnexpungeAndCompute(final int key, final BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
            if (this.value == ExpungingEntryImpl.EXPUNGED) {
                final Object value = remappingFunction.apply(Integer.valueOf(key), (Object)null);
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
    
    final class MapEntry implements Int2ObjectMap.Entry<V>
    {
        private final int key;
        private V value;
        
        MapEntry(final int key, final V value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public int getIntKey() {
            return this.key;
        }
        
        @Override
        public V getValue() {
            return this.value;
        }
        
        @Override
        public V setValue(final V value) {
            Objects.requireNonNull(value, "value");
            final Int2ObjectSyncMapImpl this$0 = Int2ObjectSyncMapImpl.this;
            final int key = this.key;
            this.value = value;
            return this$0.put(key, value);
        }
        
        @Override
        public String toString() {
            return "Int2ObjectSyncMapImpl.MapEntry{key=" + this.getIntKey() + ", value=" + this.getValue() + "}";
        }
        
        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Int2ObjectMap.Entry)) {
                return false;
            }
            final Int2ObjectMap.Entry<?> that = (Int2ObjectMap.Entry<?>)other;
            return Objects.equals(this.getIntKey(), that.getIntKey()) && Objects.equals(this.getValue(), that.getValue());
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(this.getIntKey(), this.getValue());
        }
    }
    
    final class EntrySetView extends AbstractObjectSet<Int2ObjectMap.Entry<V>>
    {
        @Override
        public int size() {
            return Int2ObjectSyncMapImpl.this.size();
        }
        
        @Override
        public boolean contains(final Object entry) {
            if (!(entry instanceof Int2ObjectMap.Entry)) {
                return false;
            }
            final Int2ObjectMap.Entry<?> mapEntry = (Int2ObjectMap.Entry<?>)entry;
            final V value = Int2ObjectSyncMapImpl.this.get(mapEntry.getIntKey());
            return value != null && Objects.equals(value, mapEntry.getValue());
        }
        
        @Override
        public boolean remove(final Object entry) {
            if (!(entry instanceof Int2ObjectMap.Entry)) {
                return false;
            }
            final Int2ObjectMap.Entry<?> mapEntry = (Int2ObjectMap.Entry<?>)entry;
            return Int2ObjectSyncMapImpl.this.remove(mapEntry.getIntKey()) != null;
        }
        
        @Override
        public void clear() {
            Int2ObjectSyncMapImpl.this.clear();
        }
        
        @Override
        public ObjectIterator<Int2ObjectMap.Entry<V>> iterator() {
            Int2ObjectSyncMapImpl.this.promote();
            return new EntryIterator(Int2ObjectSyncMapImpl.this.read.int2ObjectEntrySet().iterator());
        }
    }
    
    final class EntryIterator implements ObjectIterator<Int2ObjectMap.Entry<V>>
    {
        private final Iterator<Int2ObjectMap.Entry<ExpungingEntry<V>>> backingIterator;
        private Int2ObjectMap.Entry<V> next;
        private Int2ObjectMap.Entry<V> current;
        
        EntryIterator(final Iterator<Int2ObjectMap.Entry<ExpungingEntry<V>>> backingIterator) {
            this.backingIterator = backingIterator;
            this.next = this.nextValue();
        }
        
        @Override
        public boolean hasNext() {
            return this.next != null;
        }
        
        @Override
        public Int2ObjectMap.Entry<V> next() {
            final Int2ObjectMap.Entry<V> next = this.next;
            this.current = next;
            if (next == null) {
                throw new NoSuchElementException();
            }
            this.next = this.nextValue();
            return this.current;
        }
        
        private Int2ObjectMap.Entry<V> nextValue() {
            while (this.backingIterator.hasNext()) {
                final Int2ObjectMap.Entry<ExpungingEntry<V>> entry;
                final V value;
                if ((value = (V)(entry = this.backingIterator.next()).getValue().get()) != null) {
                    return new MapEntry(entry.getIntKey(), value);
                }
            }
            return null;
        }
        
        @Override
        public void remove() {
            if (this.current == null) {
                throw new IllegalStateException();
            }
            Int2ObjectSyncMapImpl.this.remove(this.current.getIntKey());
            this.current = null;
        }
        
        @Override
        public void forEachRemaining(final Consumer<? super Int2ObjectMap.Entry<V>> action) {
            Objects.requireNonNull(action, "action");
            if (this.next != null) {
                action.accept(this.next);
            }
            final V value;
            this.backingIterator.forEachRemaining(entry -> {
                value = entry.getValue().get();
                if (value != null) {
                    action.accept((Object)new MapEntry(((Int2ObjectMap.Entry)entry).getIntKey(), value));
                }
            });
        }
    }
}
