// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.NoSuchElementException;
import java.util.Collection;
import java.util.ArrayList;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.Optional;
import java.util.stream.Collector;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public final class MoreCollectors
{
    private static final Collector<Object, ?, Optional<Object>> TO_OPTIONAL;
    private static final Object NULL_PLACEHOLDER;
    private static final Collector<Object, ?, Object> ONLY_ELEMENT;
    
    public static <T> Collector<T, ?, Optional<T>> toOptional() {
        return (Collector<T, ?, Optional<T>>)MoreCollectors.TO_OPTIONAL;
    }
    
    public static <T> Collector<T, ?, T> onlyElement() {
        return (Collector<T, ?, T>)MoreCollectors.ONLY_ELEMENT;
    }
    
    private MoreCollectors() {
    }
    
    static {
        TO_OPTIONAL = Collector.of(ToOptionalState::new, ToOptionalState::add, ToOptionalState::combine, ToOptionalState::getOptional, Collector.Characteristics.UNORDERED);
        NULL_PLACEHOLDER = new Object();
        final Object result;
        ONLY_ELEMENT = Collector.of(ToOptionalState::new, (state, o) -> state.add((o == null) ? MoreCollectors.NULL_PLACEHOLDER : o), ToOptionalState::combine, state -> {
            result = state.getElement();
            return (result == MoreCollectors.NULL_PLACEHOLDER) ? null : result;
        }, Collector.Characteristics.UNORDERED);
    }
    
    private static final class ToOptionalState
    {
        static final int MAX_EXTRAS = 4;
        Object element;
        List<Object> extras;
        
        ToOptionalState() {
            this.element = null;
            this.extras = Collections.emptyList();
        }
        
        IllegalArgumentException multiples(final boolean overflow) {
            final StringBuilder sb = new StringBuilder().append("expected one element but was: <").append(this.element);
            for (final Object o : this.extras) {
                sb.append(", ").append(o);
            }
            if (overflow) {
                sb.append(", ...");
            }
            sb.append('>');
            throw new IllegalArgumentException(sb.toString());
        }
        
        void add(final Object o) {
            Preconditions.checkNotNull(o);
            if (this.element == null) {
                this.element = o;
            }
            else if (this.extras.isEmpty()) {
                (this.extras = new ArrayList<Object>(4)).add(o);
            }
            else {
                if (this.extras.size() >= 4) {
                    throw this.multiples(true);
                }
                this.extras.add(o);
            }
        }
        
        ToOptionalState combine(final ToOptionalState other) {
            if (this.element == null) {
                return other;
            }
            if (other.element == null) {
                return this;
            }
            if (this.extras.isEmpty()) {
                this.extras = new ArrayList<Object>();
            }
            this.extras.add(other.element);
            this.extras.addAll(other.extras);
            if (this.extras.size() > 4) {
                this.extras.subList(4, this.extras.size()).clear();
                throw this.multiples(true);
            }
            return this;
        }
        
        Optional<Object> getOptional() {
            if (this.extras.isEmpty()) {
                return Optional.ofNullable(this.element);
            }
            throw this.multiples(false);
        }
        
        Object getElement() {
            if (this.element == null) {
                throw new NoSuchElementException();
            }
            if (this.extras.isEmpty()) {
                return this.element;
            }
            throw this.multiples(false);
        }
    }
}
