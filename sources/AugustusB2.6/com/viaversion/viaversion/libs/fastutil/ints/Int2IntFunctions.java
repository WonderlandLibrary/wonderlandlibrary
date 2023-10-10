// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.io.Serializable;
import java.util.function.IntUnaryOperator;
import java.util.Objects;
import java.util.function.Function;

public final class Int2IntFunctions
{
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Int2IntFunctions() {
    }
    
    public static Int2IntFunction singleton(final int key, final int value) {
        return new Singleton(key, value);
    }
    
    public static Int2IntFunction singleton(final Integer key, final Integer value) {
        return new Singleton(key, value);
    }
    
    public static Int2IntFunction synchronize(final Int2IntFunction f) {
        return (Int2IntFunction)new Int2IntFunctions.SynchronizedFunction(f);
    }
    
    public static Int2IntFunction synchronize(final Int2IntFunction f, final Object sync) {
        return (Int2IntFunction)new Int2IntFunctions.SynchronizedFunction(f, sync);
    }
    
    public static Int2IntFunction unmodifiable(final Int2IntFunction f) {
        return (Int2IntFunction)new Int2IntFunctions.UnmodifiableFunction(f);
    }
    
    public static Int2IntFunction primitive(final Function<? super Integer, ? extends Integer> f) {
        Objects.requireNonNull(f);
        if (f instanceof Int2IntFunction) {
            return (Int2IntFunction)f;
        }
        if (f instanceof IntUnaryOperator) {
            final IntUnaryOperator obj = (IntUnaryOperator)f;
            Objects.requireNonNull(obj);
            return obj::applyAsInt;
        }
        return new PrimitiveFunction(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction extends AbstractInt2IntFunction implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        @Override
        public int get(final int k) {
            return 0;
        }
        
        @Override
        public int getOrDefault(final int k, final int defaultValue) {
            return defaultValue;
        }
        
        @Override
        public boolean containsKey(final int k) {
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
            return Int2IntFunctions.EMPTY_FUNCTION;
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
            return Int2IntFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton extends AbstractInt2IntFunction implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final int key;
        protected final int value;
        
        protected Singleton(final int key, final int value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public boolean containsKey(final int k) {
            return this.key == k;
        }
        
        @Override
        public int get(final int k) {
            return (this.key == k) ? this.value : this.defRetValue;
        }
        
        @Override
        public int getOrDefault(final int k, final int defaultValue) {
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
    
    public static class PrimitiveFunction implements Int2IntFunction
    {
        protected final java.util.function.Function<? super Integer, ? extends Integer> function;
        
        protected PrimitiveFunction(final java.util.function.Function<? super Integer, ? extends Integer> function) {
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
        public int get(final int key) {
            final Integer v = (Integer)this.function.apply(key);
            if (v == null) {
                return this.defaultReturnValue();
            }
            return v;
        }
        
        @Override
        public int getOrDefault(final int key, final int defaultValue) {
            final Integer v = (Integer)this.function.apply(key);
            if (v == null) {
                return defaultValue;
            }
            return v;
        }
        
        @Deprecated
        @Override
        public Integer get(final Object key) {
            if (key == null) {
                return null;
            }
            return (Integer)this.function.apply((Integer)key);
        }
        
        @Deprecated
        @Override
        public Integer getOrDefault(final Object key, final Integer defaultValue) {
            if (key == null) {
                return defaultValue;
            }
            final Integer v;
            return ((v = (Integer)this.function.apply((Integer)key)) == null) ? defaultValue : v;
        }
        
        @Deprecated
        @Override
        public Integer put(final Integer key, final Integer value) {
            throw new UnsupportedOperationException();
        }
    }
}
