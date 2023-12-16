// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine;

import java.util.List;
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
        for (final Object object : oldSet) {
            this.add(object);
        }
    }
    
    @Override
    public boolean contains(final Object obj) {
        if (!(obj instanceof NextTickListEntry)) {
            return false;
        }
        final NextTickListEntry nextticklistentry = (NextTickListEntry)obj;
        final Set set = this.getSubSet(nextticklistentry, false);
        return set != null && set.contains(nextticklistentry);
    }
    
    @Override
    public boolean add(final Object obj) {
        if (!(obj instanceof NextTickListEntry)) {
            return false;
        }
        final NextTickListEntry nextticklistentry = (NextTickListEntry)obj;
        if (nextticklistentry == null) {
            return false;
        }
        final Set set = this.getSubSet(nextticklistentry, true);
        final boolean flag = set.add(nextticklistentry);
        final boolean flag2 = super.add(obj);
        if (flag != flag2) {
            throw new IllegalStateException("Added: " + flag + ", addedParent: " + flag2);
        }
        return flag2;
    }
    
    @Override
    public boolean remove(final Object obj) {
        if (!(obj instanceof NextTickListEntry)) {
            return false;
        }
        final NextTickListEntry nextticklistentry = (NextTickListEntry)obj;
        final Set set = this.getSubSet(nextticklistentry, false);
        if (set == null) {
            return false;
        }
        final boolean flag = set.remove(nextticklistentry);
        final boolean flag2 = super.remove(nextticklistentry);
        if (flag != flag2) {
            throw new IllegalStateException("Added: " + flag + ", addedParent: " + flag2);
        }
        return flag2;
    }
    
    private Set getSubSet(final NextTickListEntry entry, final boolean autoCreate) {
        if (entry == null) {
            return null;
        }
        final BlockPos blockpos = entry.position;
        final int i = blockpos.getX() >> 4;
        final int j = blockpos.getZ() >> 4;
        return this.getSubSet(i, j, autoCreate);
    }
    
    private Set getSubSet(final int cx, final int cz, final boolean autoCreate) {
        final long i = ChunkCoordIntPair.chunkXZ2Int(cx, cz);
        HashSet hashset = this.longHashMap.getValueByKey(i);
        if (hashset == null && autoCreate) {
            hashset = new HashSet();
            this.longHashMap.add(i, hashset);
        }
        return hashset;
    }
    
    @Override
    public Iterator iterator() {
        if (this.minX == Integer.MIN_VALUE) {
            return super.iterator();
        }
        if (this.size() <= 0) {
            return (Iterator)Iterators.emptyIterator();
        }
        final int i = this.minX >> 4;
        final int j = this.minZ >> 4;
        final int k = this.maxX >> 4;
        final int l = this.maxZ >> 4;
        final List list = new ArrayList();
        for (int i2 = i; i2 <= k; ++i2) {
            for (int j2 = j; j2 <= l; ++j2) {
                final Set set = this.getSubSet(i2, j2, false);
                if (set != null) {
                    list.add(set.iterator());
                }
            }
        }
        if (list.size() <= 0) {
            return (Iterator)Iterators.emptyIterator();
        }
        if (list.size() == 1) {
            return list.get(0);
        }
        return Iterators.concat((Iterator)list.iterator());
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
