// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.optifine;

import java.util.ArrayList;
import com.google.common.collect.Iterators;
import java.util.HashSet;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.util.BlockPos;
import net.minecraft.world.NextTickListEntry;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.util.LongHashMap;
import java.util.TreeSet;

public class NextTickHashSet extends TreeSet
{
    private static final long serialVersionUID = 1L;
    private LongHashMap longHashMap;
    private int minX;
    private int minZ;
    private int maxX;
    private int maxZ;
    private static final int UNDEFINED = Integer.MIN_VALUE;
    
    public NextTickHashSet(final Set oldSet) {
        this.longHashMap = new LongHashMap();
        this.minX = Integer.MIN_VALUE;
        this.minZ = Integer.MIN_VALUE;
        this.maxX = Integer.MIN_VALUE;
        this.maxZ = Integer.MIN_VALUE;
        for (final Object obj : oldSet) {
            this.add(obj);
        }
    }
    
    @Override
    public boolean contains(final Object obj) {
        if (!(obj instanceof NextTickListEntry)) {
            return false;
        }
        final NextTickListEntry entry = (NextTickListEntry)obj;
        final Set set = this.getSubSet(entry, false);
        return set != null && set.contains(entry);
    }
    
    @Override
    public boolean add(final Object obj) {
        if (!(obj instanceof NextTickListEntry)) {
            return false;
        }
        final NextTickListEntry entry = (NextTickListEntry)obj;
        final Set set = this.getSubSet(entry, true);
        final boolean added = set.add(entry);
        final boolean addedParent = super.add(obj);
        if (added != addedParent) {
            throw new IllegalStateException("Added: " + added + ", addedParent: " + addedParent);
        }
        return addedParent;
    }
    
    @Override
    public boolean remove(final Object obj) {
        if (!(obj instanceof NextTickListEntry)) {
            return false;
        }
        final NextTickListEntry entry = (NextTickListEntry)obj;
        final Set set = this.getSubSet(entry, false);
        if (set == null) {
            return false;
        }
        final boolean removed = set.remove(entry);
        final boolean removedParent = super.remove(entry);
        if (removed != removedParent) {
            throw new IllegalStateException("Added: " + removed + ", addedParent: " + removedParent);
        }
        return removedParent;
    }
    
    private Set getSubSet(final NextTickListEntry entry, final boolean autoCreate) {
        if (entry == null) {
            return null;
        }
        final BlockPos pos = entry.field_180282_a;
        final int cx = pos.getX() >> 4;
        final int cz = pos.getZ() >> 4;
        return this.getSubSet(cx, cz, autoCreate);
    }
    
    private Set getSubSet(final int cx, final int cz, final boolean autoCreate) {
        final long key = ChunkCoordIntPair.chunkXZ2Int(cx, cz);
        HashSet set = (HashSet)this.longHashMap.getValueByKey(key);
        if (set == null && autoCreate) {
            set = new HashSet();
            this.longHashMap.add(key, set);
        }
        return set;
    }
    
    @Override
    public Iterator iterator() {
        if (this.minX == Integer.MIN_VALUE) {
            return super.iterator();
        }
        if (this.size() <= 0) {
            return (Iterator)Iterators.emptyIterator();
        }
        final int cMinX = this.minX >> 4;
        final int cMinZ = this.minZ >> 4;
        final int cMaxX = this.maxX >> 4;
        final int cMaxZ = this.maxZ >> 4;
        final ArrayList listIterators = new ArrayList();
        for (int x = cMinX; x <= cMaxX; ++x) {
            for (int z = cMinZ; z <= cMaxZ; ++z) {
                final Set set = this.getSubSet(x, z, false);
                if (set != null) {
                    listIterators.add(set.iterator());
                }
            }
        }
        if (listIterators.size() <= 0) {
            return (Iterator)Iterators.emptyIterator();
        }
        if (listIterators.size() == 1) {
            return listIterators.get(0);
        }
        return Iterators.concat((Iterator)listIterators.iterator());
    }
    
    public void setIteratorLimits(final int minX, final int minZ, final int maxX, final int maxZ) {
        this.minX = Math.min(minX, maxX);
        this.minZ = Math.min(minZ, maxZ);
        this.maxX = Math.max(minX, maxX);
        this.maxZ = Math.max(minZ, maxZ);
    }
    
    public void clearIteratorLimits() {
        this.minX = Integer.MIN_VALUE;
        this.minZ = Integer.MIN_VALUE;
        this.maxX = Integer.MIN_VALUE;
        this.maxZ = Integer.MIN_VALUE;
    }
}
