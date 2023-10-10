// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.graph;

import me.gong.mcleaks.util.google.common.collect.Maps;
import java.util.Map;
import me.gong.mcleaks.util.google.common.base.MoreObjects;
import me.gong.mcleaks.util.google.common.base.Objects;
import me.gong.mcleaks.util.google.common.collect.Ordering;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import javax.annotation.Nullable;
import java.util.Comparator;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
public final class ElementOrder<T>
{
    private final Type type;
    @Nullable
    private final Comparator<T> comparator;
    
    private ElementOrder(final Type type, @Nullable final Comparator<T> comparator) {
        this.type = Preconditions.checkNotNull(type);
        this.comparator = comparator;
        Preconditions.checkState(type == Type.SORTED == (comparator != null));
    }
    
    public static <S> ElementOrder<S> unordered() {
        return new ElementOrder<S>(Type.UNORDERED, null);
    }
    
    public static <S> ElementOrder<S> insertion() {
        return new ElementOrder<S>(Type.INSERTION, null);
    }
    
    public static <S extends Comparable<? super S>> ElementOrder<S> natural() {
        return new ElementOrder<S>(Type.SORTED, (Comparator<S>)Ordering.natural());
    }
    
    public static <S> ElementOrder<S> sorted(final Comparator<S> comparator) {
        return new ElementOrder<S>(Type.SORTED, comparator);
    }
    
    public Type type() {
        return this.type;
    }
    
    public Comparator<T> comparator() {
        if (this.comparator != null) {
            return this.comparator;
        }
        throw new UnsupportedOperationException("This ordering does not define a comparator.");
    }
    
    @Override
    public boolean equals(@Nullable final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ElementOrder)) {
            return false;
        }
        final ElementOrder<?> other = (ElementOrder<?>)obj;
        return this.type == other.type && Objects.equal(this.comparator, other.comparator);
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.type, this.comparator);
    }
    
    @Override
    public String toString() {
        final MoreObjects.ToStringHelper helper = MoreObjects.toStringHelper(this).add("type", this.type);
        if (this.comparator != null) {
            helper.add("comparator", this.comparator);
        }
        return helper.toString();
    }
    
     <K extends T, V> Map<K, V> createMap(final int expectedSize) {
        switch (this.type) {
            case UNORDERED: {
                return (Map<K, V>)Maps.newHashMapWithExpectedSize(expectedSize);
            }
            case INSERTION: {
                return (Map<K, V>)Maps.newLinkedHashMapWithExpectedSize(expectedSize);
            }
            case SORTED: {
                return (Map<K, V>)Maps.newTreeMap(this.comparator());
            }
            default: {
                throw new AssertionError();
            }
        }
    }
    
     <T1 extends T> ElementOrder<T1> cast() {
        return (ElementOrder<T1>)this;
    }
    
    public enum Type
    {
        UNORDERED, 
        INSERTION, 
        SORTED;
    }
}
