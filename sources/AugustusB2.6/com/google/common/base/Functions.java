// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import java.io.Serializable;
import javax.annotation.CheckForNull;
import java.util.Map;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public final class Functions
{
    private Functions() {
    }
    
    public static Function<Object, String> toStringFunction() {
        return ToStringFunction.INSTANCE;
    }
    
    public static <E> Function<E, E> identity() {
        return (Function<E, E>)IdentityFunction.INSTANCE;
    }
    
    public static <K, V> Function<K, V> forMap(final Map<K, V> map) {
        return new FunctionForMapNoDefault<K, V>(map);
    }
    
    public static <K, V> Function<K, V> forMap(final Map<K, ? extends V> map, @ParametricNullness final V defaultValue) {
        return new ForMapWithDefault<K, V>(map, defaultValue);
    }
    
    public static <A, B, C> Function<A, C> compose(final Function<B, C> g, final Function<A, ? extends B> f) {
        return new FunctionComposition<A, Object, C>(g, f);
    }
    
    public static <T> Function<T, Boolean> forPredicate(final Predicate<T> predicate) {
        return new PredicateFunction<T>((Predicate)predicate);
    }
    
    public static <E> Function<Object, E> constant(@ParametricNullness final E value) {
        return new ConstantFunction<E>(value);
    }
    
    public static <F, T> Function<F, T> forSupplier(final Supplier<T> supplier) {
        return new SupplierFunction<F, T>((Supplier)supplier);
    }
    
    private enum ToStringFunction implements Function<Object, String>
    {
        INSTANCE;
        
        @Override
        public String apply(final Object o) {
            Preconditions.checkNotNull(o);
            return o.toString();
        }
        
        @Override
        public String toString() {
            return "Functions.toStringFunction()";
        }
        
        private static /* synthetic */ ToStringFunction[] $values() {
            return new ToStringFunction[] { ToStringFunction.INSTANCE };
        }
        
        static {
            $VALUES = $values();
        }
    }
    
    private enum IdentityFunction implements Function<Object, Object>
    {
        INSTANCE;
        
        @CheckForNull
        @Override
        public Object apply(@CheckForNull final Object o) {
            return o;
        }
        
        @Override
        public String toString() {
            return "Functions.identity()";
        }
        
        private static /* synthetic */ IdentityFunction[] $values() {
            return new IdentityFunction[] { IdentityFunction.INSTANCE };
        }
        
        static {
            $VALUES = $values();
        }
    }
    
    private static class FunctionForMapNoDefault<K, V> implements Function<K, V>, Serializable
    {
        final Map<K, V> map;
        private static final long serialVersionUID = 0L;
        
        FunctionForMapNoDefault(final Map<K, V> map) {
            this.map = Preconditions.checkNotNull(map);
        }
        
        @ParametricNullness
        @Override
        public V apply(@ParametricNullness final K key) {
            final V result = this.map.get(key);
            Preconditions.checkArgument(result != null || this.map.containsKey(key), "Key '%s' not present in map", key);
            return NullnessCasts.uncheckedCastNullableTToT(result);
        }
        
        @Override
        public boolean equals(@CheckForNull final Object o) {
            if (o instanceof FunctionForMapNoDefault) {
                final FunctionForMapNoDefault<?, ?> that = (FunctionForMapNoDefault<?, ?>)o;
                return this.map.equals(that.map);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.map.hashCode();
        }
        
        @Override
        public String toString() {
            final String value = String.valueOf(this.map);
            return new StringBuilder(18 + String.valueOf(value).length()).append("Functions.forMap(").append(value).append(")").toString();
        }
    }
    
    private static class ForMapWithDefault<K, V> implements Function<K, V>, Serializable
    {
        final Map<K, ? extends V> map;
        @ParametricNullness
        final V defaultValue;
        private static final long serialVersionUID = 0L;
        
        ForMapWithDefault(final Map<K, ? extends V> map, @ParametricNullness final V defaultValue) {
            this.map = Preconditions.checkNotNull(map);
            this.defaultValue = defaultValue;
        }
        
        @ParametricNullness
        @Override
        public V apply(@ParametricNullness final K key) {
            final V result = (V)this.map.get(key);
            return (result != null || this.map.containsKey(key)) ? NullnessCasts.uncheckedCastNullableTToT(result) : this.defaultValue;
        }
        
        @Override
        public boolean equals(@CheckForNull final Object o) {
            if (o instanceof ForMapWithDefault) {
                final ForMapWithDefault<?, ?> that = (ForMapWithDefault<?, ?>)o;
                return this.map.equals(that.map) && Objects.equal(this.defaultValue, that.defaultValue);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return Objects.hashCode(this.map, this.defaultValue);
        }
        
        @Override
        public String toString() {
            final String value = String.valueOf(this.map);
            final String value2 = String.valueOf(this.defaultValue);
            return new StringBuilder(33 + String.valueOf(value).length() + String.valueOf(value2).length()).append("Functions.forMap(").append(value).append(", defaultValue=").append(value2).append(")").toString();
        }
    }
    
    private static class FunctionComposition<A, B, C> implements Function<A, C>, Serializable
    {
        private final Function<B, C> g;
        private final Function<A, ? extends B> f;
        private static final long serialVersionUID = 0L;
        
        public FunctionComposition(final Function<B, C> g, final Function<A, ? extends B> f) {
            this.g = Preconditions.checkNotNull(g);
            this.f = Preconditions.checkNotNull(f);
        }
        
        @ParametricNullness
        @Override
        public C apply(@ParametricNullness final A a) {
            return this.g.apply((B)this.f.apply(a));
        }
        
        @Override
        public boolean equals(@CheckForNull final Object obj) {
            if (obj instanceof FunctionComposition) {
                final FunctionComposition<?, ?, ?> that = (FunctionComposition<?, ?, ?>)obj;
                return this.f.equals(that.f) && this.g.equals(that.g);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.f.hashCode() ^ this.g.hashCode();
        }
        
        @Override
        public String toString() {
            final String value = String.valueOf(this.g);
            final String value2 = String.valueOf(this.f);
            return new StringBuilder(2 + String.valueOf(value).length() + String.valueOf(value2).length()).append(value).append("(").append(value2).append(")").toString();
        }
    }
    
    private static class PredicateFunction<T> implements Function<T, Boolean>, Serializable
    {
        private final Predicate<T> predicate;
        private static final long serialVersionUID = 0L;
        
        private PredicateFunction(final Predicate<T> predicate) {
            this.predicate = Preconditions.checkNotNull(predicate);
        }
        
        @Override
        public Boolean apply(@ParametricNullness final T t) {
            return this.predicate.apply(t);
        }
        
        @Override
        public boolean equals(@CheckForNull final Object obj) {
            if (obj instanceof PredicateFunction) {
                final PredicateFunction<?> that = (PredicateFunction<?>)obj;
                return this.predicate.equals(that.predicate);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.predicate.hashCode();
        }
        
        @Override
        public String toString() {
            final String value = String.valueOf(this.predicate);
            return new StringBuilder(24 + String.valueOf(value).length()).append("Functions.forPredicate(").append(value).append(")").toString();
        }
    }
    
    private static class ConstantFunction<E> implements Function<Object, E>, Serializable
    {
        @ParametricNullness
        private final E value;
        private static final long serialVersionUID = 0L;
        
        public ConstantFunction(@ParametricNullness final E value) {
            this.value = value;
        }
        
        @ParametricNullness
        @Override
        public E apply(@CheckForNull final Object from) {
            return this.value;
        }
        
        @Override
        public boolean equals(@CheckForNull final Object obj) {
            if (obj instanceof ConstantFunction) {
                final ConstantFunction<?> that = (ConstantFunction<?>)obj;
                return Objects.equal(this.value, that.value);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return (this.value == null) ? 0 : this.value.hashCode();
        }
        
        @Override
        public String toString() {
            final String value = String.valueOf(this.value);
            return new StringBuilder(20 + String.valueOf(value).length()).append("Functions.constant(").append(value).append(")").toString();
        }
    }
    
    private static class SupplierFunction<F, T> implements Function<F, T>, Serializable
    {
        private final Supplier<T> supplier;
        private static final long serialVersionUID = 0L;
        
        private SupplierFunction(final Supplier<T> supplier) {
            this.supplier = Preconditions.checkNotNull(supplier);
        }
        
        @ParametricNullness
        @Override
        public T apply(@ParametricNullness final F input) {
            return this.supplier.get();
        }
        
        @Override
        public boolean equals(@CheckForNull final Object obj) {
            if (obj instanceof SupplierFunction) {
                final SupplierFunction<?, ?> that = (SupplierFunction<?, ?>)obj;
                return this.supplier.equals(that.supplier);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.supplier.hashCode();
        }
        
        @Override
        public String toString() {
            final String value = String.valueOf(this.supplier);
            return new StringBuilder(23 + String.valueOf(value).length()).append("Functions.forSupplier(").append(value).append(")").toString();
        }
    }
}
