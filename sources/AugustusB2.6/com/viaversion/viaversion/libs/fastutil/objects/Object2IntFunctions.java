// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import java.io.Serializable;
import java.util.function.ToIntFunction;
import java.util.Objects;
import java.util.function.Function;

public final class Object2IntFunctions
{
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Object2IntFunctions() {
    }
    
    public static <K> Object2IntFunction<K> singleton(final K key, final int value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Object2IntFunction<K> singleton(final K key, final Integer value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Object2IntFunction<K> synchronize(final Object2IntFunction<K> f) {
        return (Object2IntFunction<K>)new Object2IntFunctions.SynchronizedFunction((Object2IntFunction)f);
    }
    
    public static <K> Object2IntFunction<K> synchronize(final Object2IntFunction<K> f, final Object sync) {
        return (Object2IntFunction<K>)new Object2IntFunctions.SynchronizedFunction((Object2IntFunction)f, sync);
    }
    
    public static <K> Object2IntFunction<K> unmodifiable(final Object2IntFunction<? extends K> f) {
        return (Object2IntFunction<K>)new Object2IntFunctions.UnmodifiableFunction((Object2IntFunction)f);
    }
    
    public static <K> Object2IntFunction<K> primitive(final Function<? super K, ? extends Integer> f) {
        Objects.requireNonNull(f);
        if (f instanceof Object2IntFunction) {
            return (Object2IntFunction<K>)(Object2IntFunction)f;
        }
        if (f instanceof ToIntFunction) {
            return key -> ((ToIntFunction<Object>)f).applyAsInt(key);
        }
        return new PrimitiveFunction<K>(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction<K> extends AbstractObject2IntFunction<K> implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        @Override
        public int getInt(final Object k) {
            return 0;
        }
        
        @Override
        public int getOrDefault(final Object k, final int defaultValue) {
            return defaultValue;
        }
        
        @Override
        public boolean containsKey(final Object k) {
            return false;
        }
        
        @Override
        public int defaultReturnValue() {
            return 0;
        }
        
        @Override
        public void defaultReturnValue(final int defRetValue) {
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
            return Object2IntFunctions.EMPTY_FUNCTION;
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
            return Object2IntFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton<K> extends AbstractObject2IntFunction<K> implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final K key;
        protected final int value;
        
        protected Singleton(final K key, final int value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public boolean containsKey(final Object k) {
            return Objects.equals(this.key, k);
        }
        
        @Override
        public int getInt(final Object k) {
            return Objects.equals(this.key, k) ? this.value : this.defRetValue;
        }
        
        @Override
        public int getOrDefault(final Object k, final int defaultValue) {
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
    
    public static class PrimitiveFunction<K> implements Object2IntFunction<K>
    {
        protected final java.util.function.Function<? super K, ? extends Integer> function;
        
        protected PrimitiveFunction(final java.util.function.Function<? super K, ? extends Integer> function) {
            this.function = function;
        }
        
        @Override
        public boolean containsKey(final Object key) {
            return this.function.apply((Object)key) != null;
        }
        
        @Override
        public int getInt(final Object key) {
            final Integer v = (Integer)this.function.apply((Object)key);
            if (v == null) {
                return this.defaultReturnValue();
            }
            return v;
        }
        
        @Override
        public int getOrDefault(final Object key, final int defaultValue) {
            final Integer v = (Integer)this.function.apply((Object)key);
            if (v == null) {
                return defaultValue;
            }
            return v;
        }
        
        @Deprecated
        @Override
        public Integer get(final Object key) {
            return (Integer)this.function.apply((Object)key);
        }
        
        @Deprecated
        @Override
        public Integer getOrDefault(final Object key, final Integer defaultValue) {
            final Integer v;
            return ((v = (Integer)this.function.apply((Object)key)) == null) ? defaultValue : v;
        }
        
        @Deprecated
        @Override
        public Integer put(final K key, final Integer value) {
            throw new UnsupportedOperationException();
        }
    }
}
