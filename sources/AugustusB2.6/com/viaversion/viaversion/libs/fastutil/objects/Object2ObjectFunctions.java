// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import java.util.Objects;
import com.viaversion.viaversion.libs.fastutil.Function;
import java.io.Serializable;

public final class Object2ObjectFunctions
{
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Object2ObjectFunctions() {
    }
    
    public static <K, V> Object2ObjectFunction<K, V> singleton(final K key, final V value) {
        return new Singleton<K, V>(key, value);
    }
    
    public static <K, V> Object2ObjectFunction<K, V> synchronize(final Object2ObjectFunction<K, V> f) {
        return (Object2ObjectFunction<K, V>)new Object2ObjectFunctions.SynchronizedFunction((Object2ObjectFunction)f);
    }
    
    public static <K, V> Object2ObjectFunction<K, V> synchronize(final Object2ObjectFunction<K, V> f, final Object sync) {
        return (Object2ObjectFunction<K, V>)new Object2ObjectFunctions.SynchronizedFunction((Object2ObjectFunction)f, sync);
    }
    
    public static <K, V> Object2ObjectFunction<K, V> unmodifiable(final Object2ObjectFunction<? extends K, ? extends V> f) {
        return (Object2ObjectFunction<K, V>)new Object2ObjectFunctions.UnmodifiableFunction((Object2ObjectFunction)f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction<K, V> extends AbstractObject2ObjectFunction<K, V> implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        @Override
        public V get(final Object k) {
            return null;
        }
        
        @Override
        public V getOrDefault(final Object k, final V defaultValue) {
            return defaultValue;
        }
        
        @Override
        public boolean containsKey(final Object k) {
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
            return Object2ObjectFunctions.EMPTY_FUNCTION;
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
            return Object2ObjectFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton<K, V> extends AbstractObject2ObjectFunction<K, V> implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final K key;
        protected final V value;
        
        protected Singleton(final K key, final V value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public boolean containsKey(final Object k) {
            return Objects.equals(this.key, k);
        }
        
        @Override
        public V get(final Object k) {
            return Objects.equals(this.key, k) ? this.value : this.defRetValue;
        }
        
        @Override
        public V getOrDefault(final Object k, final V defaultValue) {
            return Objects.equals(this.key, k) ? this.value : defaultValue;
        }
        
        @Override
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
}
