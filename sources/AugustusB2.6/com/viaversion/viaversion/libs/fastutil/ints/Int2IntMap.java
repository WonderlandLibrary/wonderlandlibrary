// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.Objects;
import java.util.function.IntUnaryOperator;
import java.util.function.Consumer;
import java.util.function.BiConsumer;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Int2IntMap extends Int2IntFunction, Map<Integer, Integer>
{
    int size();
    
    default void clear() {
        throw new UnsupportedOperationException();
    }
    
    void defaultReturnValue(final int p0);
    
    int defaultReturnValue();
    
    ObjectSet<Entry> int2IntEntrySet();
    
    @Deprecated
    default ObjectSet<Map.Entry<Integer, Integer>> entrySet() {
        return (ObjectSet<Map.Entry<Integer, Integer>>)this.int2IntEntrySet();
    }
    
    @Deprecated
    default Integer put(final Integer key, final Integer value) {
        return super.put(key, value);
    }
    
    @Deprecated
    default Integer get(final Object key) {
        return super.get(key);
    }
    
    @Deprecated
    default Integer remove(final Object key) {
        return super.remove(key);
    }
    
    IntSet keySet();
    
    IntCollection values();
    
    boolean containsKey(final int p0);
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return super.containsKey(key);
    }
    
    boolean containsValue(final int p0);
    
    @Deprecated
    default boolean containsValue(final Object value) {
        return value != null && this.containsValue((int)value);
    }
    
    default void forEach(final BiConsumer<? super Integer, ? super Integer> consumer) {
        final ObjectSet<Entry> entrySet = this.int2IntEntrySet();
        final Consumer<Entry> wrappingConsumer = entry -> consumer.accept(entry.getIntKey(), entry.getIntValue());
        if (entrySet instanceof FastEntrySet) {
            ((FastEntrySet)entrySet).fastForEach(wrappingConsumer);
        }
        else {
            entrySet.forEach(wrappingConsumer);
        }
    }
    
    default int getOrDefault(final int key, final int defaultValue) {
        final int v;
        return ((v = this.get(key)) != this.defaultReturnValue() || this.containsKey(key)) ? v : defaultValue;
    }
    
    @Deprecated
    default Integer getOrDefault(final Object key, final Integer defaultValue) {
        return super.getOrDefault(key, defaultValue);
    }
    
    default int putIfAbsent(final int key, final int value) {
        final int v = this.get(key);
        final int drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        this.put(key, value);
        return drv;
    }
    
    default boolean remove(final int key, final int value) {
        final int curValue = this.get(key);
        if (curValue != value || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.remove(key);
        return true;
    }
    
    default boolean replace(final int key, final int oldValue, final int newValue) {
        final int curValue = this.get(key);
        if (curValue != oldValue || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.put(key, newValue);
        return true;
    }
    
    default int replace(final int key, final int value) {
        return this.containsKey(key) ? this.put(key, value) : this.defaultReturnValue();
    }
    
    default int computeIfAbsent(final int key, final IntUnaryOperator mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int v = this.get(key);
        if (v != this.defaultReturnValue() || this.containsKey(key)) {
            return v;
        }
        final int newValue = mappingFunction.applyAsInt(key);
        this.put(key, newValue);
        return newValue;
    }
    
    default int computeIfAbsentNullable(final int key, final IntFunction<? extends Integer> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int v = this.get(key);
        final int drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        final Integer mappedValue = (Integer)mappingFunction.apply(key);
        if (mappedValue == null) {
            return drv;
        }
        final int newValue = mappedValue;
        this.put(key, newValue);
        return newValue;
    }
    
    default int computeIfAbsent(final int key, final Int2IntFunction mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int v = this.get(key);
        final int drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        if (!mappingFunction.containsKey(key)) {
            return drv;
        }
        final int newValue = mappingFunction.get(key);
        this.put(key, newValue);
        return newValue;
    }
    
    @Deprecated
    default int computeIfAbsentPartial(final int key, final Int2IntFunction mappingFunction) {
        return this.computeIfAbsent(key, mappingFunction);
    }
    
    default int computeIfPresent(final int key, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int oldValue = this.get(key);
        final int drv = this.defaultReturnValue();
        if (oldValue == drv && !this.containsKey(key)) {
            return drv;
        }
        final Integer newValue = (Integer)remappingFunction.apply(key, oldValue);
        if (newValue == null) {
            this.remove(key);
            return drv;
        }
        final int newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default int compute(final int key, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int oldValue = this.get(key);
        final int drv = this.defaultReturnValue();
        final boolean contained = oldValue != drv || this.containsKey(key);
        final Integer newValue = (Integer)remappingFunction.apply(key, contained ? Integer.valueOf(oldValue) : null);
        if (newValue == null) {
            if (contained) {
                this.remove(key);
            }
            return drv;
        }
        final int newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default int merge(final int key, final int value, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int oldValue = this.get(key);
        final int drv = this.defaultReturnValue();
        int newValue;
        if (oldValue != drv || this.containsKey(key)) {
            final Integer mergedValue = (Integer)remappingFunction.apply(oldValue, value);
            if (mergedValue == null) {
                this.remove(key);
                return drv;
            }
            newValue = mergedValue;
        }
        else {
            newValue = value;
        }
        this.put(key, newValue);
        return newValue;
    }
    
    default int mergeInt(final int key, final int value, final IntBinaryOperator remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int oldValue = this.get(key);
        final int drv = this.defaultReturnValue();
        final int newValue = (oldValue != drv || this.containsKey(key)) ? remappingFunction.applyAsInt(oldValue, value) : value;
        this.put(key, newValue);
        return newValue;
    }
    
    default int mergeInt(final int key, final int value, final com.viaversion.viaversion.libs.fastutil.ints.IntBinaryOperator remappingFunction) {
        return this.mergeInt(key, value, (IntBinaryOperator)remappingFunction);
    }
    
    @Deprecated
    default Integer putIfAbsent(final Integer key, final Integer value) {
        return super.putIfAbsent(key, value);
    }
    
    @Deprecated
    default boolean remove(final Object key, final Object value) {
        return super.remove(key, value);
    }
    
    @Deprecated
    default boolean replace(final Integer key, final Integer oldValue, final Integer newValue) {
        return super.replace(key, oldValue, newValue);
    }
    
    @Deprecated
    default Integer replace(final Integer key, final Integer value) {
        return super.replace(key, value);
    }
    
    @Deprecated
    default Integer computeIfAbsent(final Integer key, final java.util.function.Function<? super Integer, ? extends Integer> mappingFunction) {
        return super.computeIfAbsent(key, mappingFunction);
    }
    
    @Deprecated
    default Integer computeIfPresent(final Integer key, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        return super.computeIfPresent(key, remappingFunction);
    }
    
    @Deprecated
    default Integer compute(final Integer key, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        return super.compute(key, remappingFunction);
    }
    
    @Deprecated
    default Integer merge(final Integer key, final Integer value, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        return super.merge(key, value, remappingFunction);
    }
    
    public interface FastEntrySet extends ObjectSet<Entry>
    {
        ObjectIterator<Entry> fastIterator();
        
        default void fastForEach(final Consumer<? super Entry> consumer) {
            this.forEach(consumer);
        }
    }
    
    public interface Entry extends Map.Entry<Integer, Integer>
    {
        int getIntKey();
        
        @Deprecated
        default Integer getKey() {
            return this.getIntKey();
        }
        
        int getIntValue();
        
        int setValue(final int p0);
        
        @Deprecated
        default Integer getValue() {
            return this.getIntValue();
        }
        
        @Deprecated
        default Integer setValue(final Integer value) {
            return this.setValue((int)value);
        }
    }
}
