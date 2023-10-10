// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import java.util.NoSuchElementException;
import java.util.Collection;
import java.util.ArrayList;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import java.util.function.Supplier;
import java.util.Optional;
import java.util.stream.Collector;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
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
        @Nullable
        Object element;
        @Nullable
        List<Object> extras;
        
        ToOptionalState() {
            this.element = null;
            this.extras = null;
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
            else if (this.extras == null) {
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
            if (this.extras == null) {
                this.extras = new ArrayList<Object>();
            }
            this.extras.add(other.element);
            if (other.extras != null) {
                this.extras.addAll(other.extras);
            }
            if (this.extras.size() > 4) {
                this.extras.subList(4, this.extras.size()).clear();
                throw this.multiples(true);
            }
            return this;
        }
        
        Optional<Object> getOptional() {
            if (this.extras == null) {
                return Optional.ofNullable(this.element);
            }
            throw this.multiples(false);
        }
        
        Object getElement() {
            if (this.element == null) {
                throw new NoSuchElementException();
            }
            if (this.extras == null) {
                return this.element;
            }
            throw this.multiples(false);
        }
    }
}
