// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.io.Serializable;
import java.util.function.IntFunction;
import java.util.Objects;
import java.util.function.Function;

public final class Int2ObjectFunctions
{
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Int2ObjectFunctions() {
    }
    
    public static <V> Int2ObjectFunction<V> singleton(final int key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Int2ObjectFunction<V> singleton(final Integer key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Int2ObjectFunction<V> synchronize(final Int2ObjectFunction<V> f) {
        return (Int2ObjectFunction<V>)new Int2ObjectFunctions.SynchronizedFunction((Int2ObjectFunction)f);
    }
    
    public static <V> Int2ObjectFunction<V> synchronize(final Int2ObjectFunction<V> f, final Object sync) {
        return (Int2ObjectFunction<V>)new Int2ObjectFunctions.SynchronizedFunction((Int2ObjectFunction)f, sync);
    }
    
    public static <V> Int2ObjectFunction<V> unmodifiable(final Int2ObjectFunction<? extends V> f) {
        return (Int2ObjectFunction<V>)new Int2ObjectFunctions.UnmodifiableFunction((Int2ObjectFunction)f);
    }
    
    public static <V> Int2ObjectFunction<V> primitive(final Function<? super Integer, ? extends V> f) {
        Objects.requireNonNull(f);
        if (f instanceof Int2ObjectFunction) {
            return (Int2ObjectFunction<V>)(Int2ObjectFunction)f;
        }
        if (f instanceof IntFunction) {
            final IntFunction obj = (IntFunction)f;
            Objects.requireNonNull(obj);
            return (Int2ObjectFunction<V>)obj::apply;
        }
        return new PrimitiveFunction<V>(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction<V> extends AbstractInt2ObjectFunction<V> implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        @Override
        public V get(final int k) {
            return null;
        }
        
        @Override
        public V getOrDefault(final int k, final V defaultValue) {
            return defaultValue;
        }
        
        @Override
        public boolean containsKey(final int k) {
            return false;
        }
        
        @Override
        public V defaultReturnValue() {
            return null;
        }
        
        @Override
        public void defaultReturnValue(final V defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int size() {
            return 0;
        }
        
        @Override
        public void clear() {
        }
        
        public Object clone() {
            return Int2ObjectFunctions.EMPTY_FUNCTION;
        }
        
        @Override
        public int hashCode() {
            return 0;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof Function && ((Function)o).size() == 0;
        }
        
        @Override
        public String toString() {
            return "{}";
        }
        
        private Object readResolve() {
            return Int2ObjectFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton<V> extends AbstractInt2ObjectFunction<V> implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final int key;
        protected final V value;
        
        protected Singleton(final int key, final V value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public boolean containsKey(final int k) {
            return this.key == k;
        }
        
        @Override
        public V get(final int k) {
            return (this.key == k) ? this.value : this.defRetValue;
        }
        
        @Override
        public V getOrDefault(final int k, final V defaultValue) {
            return (this.key == k) ? this.value : defaultValue;
        }
        
        @Override
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class PrimitiveFunction<V> implements Int2ObjectFunction<V>
    {
        protected final java.util.function.Function<? super Integer, ? extends V> function;
        
        protected PrimitiveFunction(final java.util.function.Function<? super Integer, ? extends V> function) {
            this.function = function;
        }
        
        @Override
        public boolean containsKey(final int key) {
            return this.function.apply(key) != null;
        }
        
        @Deprecated
        @Override
        public boolean containsKey(final Object key) {
            return key != null && this.function.apply((Integer)key) != null;
        }
        
        @Override
        public V get(final int key) {
            final V v = (V)this.function.apply(key);
            if (v == null) {
                return null;
            }
            return v;
        }
        
        @Override
        public V getOrDefault(final int key, final V defaultValue) {
            final V v = (V)this.function.apply(key);
            if (v == null) {
                return defaultValue;
            }
            return v;
        }
        
        @Deprecated
        @Override
        public V get(final Object key) {
            if (key == null) {
                return null;
            }
            return (V)this.function.apply((Integer)key);
        }
        
        @Deprecated
        @Override
        public V getOrDefault(final Object key, final V defaultValue) {
            if (key == null) {
                return defaultValue;
            }
            final V v;
            return ((v = (V)this.function.apply((Integer)key)) == null) ? defaultValue : v;
        }
        
        @Deprecated
        @Override
        public V put(final Integer key, final V value) {
            throw new UnsupportedOperationException();
        }
    }
}
