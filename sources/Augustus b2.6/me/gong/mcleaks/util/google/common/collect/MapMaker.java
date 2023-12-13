// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import me.gong.mcleaks.util.google.common.base.Ascii;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import me.gong.mcleaks.util.google.common.base.MoreObjects;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import me.gong.mcleaks.util.google.common.base.Equivalence;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class MapMaker
{
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int DEFAULT_CONCURRENCY_LEVEL = 4;
    static final int UNSET_INT = -1;
    boolean useCustomMap;
    int initialCapacity;
    int concurrencyLevel;
    MapMakerInternalMap.Strength keyStrength;
    MapMakerInternalMap.Strength valueStrength;
    Equivalence<Object> keyEquivalence;
    
    public MapMaker() {
        this.initialCapacity = -1;
        this.concurrencyLevel = -1;
    }
    
    @CanIgnoreReturnValue
    @GwtIncompatible
    MapMaker keyEquivalence(final Equivalence<Object> equivalence) {
        Preconditions.checkState(this.keyEquivalence == null, "key equivalence was already set to %s", this.keyEquivalence);
        this.keyEquivalence = Preconditions.checkNotNull(equivalence);
        this.useCustomMap = true;
        return this;
    }
    
    Equivalence<Object> getKeyEquivalence() {
        return MoreObjects.firstNonNull(this.keyEquivalence, this.getKeyStrength().defaultEquivalence());
    }
    
    @CanIgnoreReturnValue
    public MapMaker initialCapacity(final int initialCapacity) {
        Preconditions.checkState(this.initialCapacity == -1, "initial capacity was already set to %s", this.initialCapacity);
        Preconditions.checkArgument(initialCapacity >= 0);
        this.initialCapacity = initialCapacity;
        return this;
    }
    
    int getInitialCapacity() {
        return (this.initialCapacity == -1) ? 16 : this.initialCapacity;
    }
    
    @CanIgnoreReturnValue
    public MapMaker concurrencyLevel(final int concurrencyLevel) {
        Preconditions.checkState(this.concurrencyLevel == -1, "concurrency level was already set to %s", this.concurrencyLevel);
        Preconditions.checkArgument(concurrencyLevel > 0);
        this.concurrencyLevel = concurrencyLevel;
        return this;
    }
    
    int getConcurrencyLevel() {
        return (this.concurrencyLevel == -1) ? 4 : this.concurrencyLevel;
    }
    
    @CanIgnoreReturnValue
    @GwtIncompatible
    public MapMaker weakKeys() {
        return this.setKeyStrength(MapMakerInternalMap.Strength.WEAK);
    }
    
    MapMaker setKeyStrength(final MapMakerInternalMap.Strength strength) {
        Preconditions.checkState(this.keyStrength == null, "Key strength was already set to %s", this.keyStrength);
        this.keyStrength = Preconditions.checkNotNull(strength);
        if (strength != MapMakerInternalMap.Strength.STRONG) {
            this.useCustomMap = true;
        }
        return this;
    }
    
    MapMakerInternalMap.Strength getKeyStrength() {
        return MoreObjects.firstNonNull(this.keyStrength, MapMakerInternalMap.Strength.STRONG);
    }
    
    @CanIgnoreReturnValue
    @GwtIncompatible
    public MapMaker weakValues() {
        return this.setValueStrength(MapMakerInternalMap.Strength.WEAK);
    }
    
    MapMaker setValueStrength(final MapMakerInternalMap.Strength strength) {
        Preconditions.checkState(this.valueStrength == null, "Value strength was already set to %s", this.valueStrength);
        this.valueStrength = Preconditions.checkNotNull(strength);
        if (strength != MapMakerInternalMap.Strength.STRONG) {
            this.useCustomMap = true;
        }
        return this;
    }
    
    MapMakerInternalMap.Strength getValueStrength() {
        return MoreObjects.firstNonNull(this.valueStrength, MapMakerInternalMap.Strength.STRONG);
    }
    
    public <K, V> ConcurrentMap<K, V> makeMap() {
        if (!this.useCustomMap) {
            return new ConcurrentHashMap<K, V>(this.getInitialCapacity(), 0.75f, this.getConcurrencyLevel());
        }
        return (ConcurrentMap<K, V>)MapMakerInternalMap.create(this);
    }
    
    @GwtIncompatible
     <K, V> MapMakerInternalMap<K, V, ?, ?> makeCustomMap() {
        return MapMakerInternalMap.create(this);
    }
    
    @Override
    public String toString() {
        final MoreObjects.ToStringHelper s = MoreObjects.toStringHelper(this);
        if (this.initialCapacity != -1) {
            s.add("initialCapacity", this.initialCapacity);
        }
        if (this.concurrencyLevel != -1) {
            s.add("concurrencyLevel", this.concurrencyLevel);
        }
        if (this.keyStrength != null) {
            s.add("keyStrength", Ascii.toLowerCase(this.keyStrength.toString()));
        }
        if (this.valueStrength != null) {
            s.add("valueStrength", Ascii.toLowerCase(this.valueStrength.toString()));
        }
        if (this.keyEquivalence != null) {
            s.addValue("keyEquivalence");
        }
        return s.toString();
    }
}
