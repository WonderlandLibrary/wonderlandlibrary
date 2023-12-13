/*
 * Decompiled with CFR 0.152.
 */
package optfine;

import com.google.common.collect.Iterators;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import net.minecraft.util.BlockPos;
import net.minecraft.util.LongHashMap;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.NextTickListEntry;

public class NextTickHashSet
extends TreeSet {
    private LongHashMap longHashMap = new LongHashMap();
    private int minX = Integer.MIN_VALUE;
    private int minZ = Integer.MIN_VALUE;
    private int maxX = Integer.MIN_VALUE;
    private int maxZ = Integer.MIN_VALUE;
    private static final int UNDEFINED = Integer.MIN_VALUE;

    public NextTickHashSet(Set p_i46_1_) {
        for (Object object : p_i46_1_) {
            this.add(object);
        }
    }

    @Override
    public boolean contains(Object p_contains_1_) {
        if (!(p_contains_1_ instanceof NextTickListEntry)) {
            return false;
        }
        NextTickListEntry nextticklistentry = (NextTickListEntry)p_contains_1_;
        Set set = this.getSubSet(nextticklistentry, false);
        return set == null ? false : set.contains(nextticklistentry);
    }

    @Override
    public boolean add(Object p_add_1_) {
        boolean flag1;
        if (!(p_add_1_ instanceof NextTickListEntry)) {
            return false;
        }
        NextTickListEntry nextticklistentry = (NextTickListEntry)p_add_1_;
        if (nextticklistentry == null) {
            return false;
        }
        Set set = this.getSubSet(nextticklistentry, true);
        boolean flag = set.add(nextticklistentry);
        if (flag != (flag1 = super.add(p_add_1_))) {
            throw new IllegalStateException("Added: " + flag + ", addedParent: " + flag1);
        }
        return flag1;
    }

    @Override
    public boolean remove(Object p_remove_1_) {
        boolean flag1;
        if (!(p_remove_1_ instanceof NextTickListEntry)) {
            return false;
        }
        NextTickListEntry nextticklistentry = (NextTickListEntry)p_remove_1_;
        Set set = this.getSubSet(nextticklistentry, false);
        if (set == null) {
            return false;
        }
        boolean flag = set.remove(nextticklistentry);
        if (flag != (flag1 = super.remove(nextticklistentry))) {
            throw new IllegalStateException("Added: " + flag + ", addedParent: " + flag1);
        }
        return flag1;
    }

    private Set getSubSet(NextTickListEntry p_getSubSet_1_, boolean p_getSubSet_2_) {
        if (p_getSubSet_1_ == null) {
            return null;
        }
        BlockPos blockpos = p_getSubSet_1_.position;
        int i2 = blockpos.getX() >> 4;
        int j2 = blockpos.getZ() >> 4;
        return this.getSubSet(i2, j2, p_getSubSet_2_);
    }

    private Set getSubSet(int p_getSubSet_1_, int p_getSubSet_2_, boolean p_getSubSet_3_) {
        long i2 = ChunkCoordIntPair.chunkXZ2Int(p_getSubSet_1_, p_getSubSet_2_);
        HashSet hashset = (HashSet)this.longHashMap.getValueByKey(i2);
        if (hashset == null && p_getSubSet_3_) {
            hashset = new HashSet();
            this.longHashMap.add(i2, hashset);
        }
        return hashset;
    }

    @Override
    public Iterator iterator() {
        if (this.minX == Integer.MIN_VALUE) {
            return super.iterator();
        }
        if (this.size() <= 0) {
            return Iterators.emptyIterator();
        }
        int i2 = this.minX >> 4;
        int j2 = this.minZ >> 4;
        int k2 = this.maxX >> 4;
        int l2 = this.maxZ >> 4;
        ArrayList list = new ArrayList();
        for (int i1 = i2; i1 <= k2; ++i1) {
            for (int j1 = j2; j1 <= l2; ++j1) {
                Set set = this.getSubSet(i1, j1, false);
                if (set == null) continue;
                list.add(set.iterator());
            }
        }
        if (list.size() <= 0) {
            return Iterators.emptyIterator();
        }
        if (list.size() == 1) {
            return (Iterator)list.get(0);
        }
        return Iterators.concat(list.iterator());
    }

    public void setIteratorLimits(int p_setIteratorLimits_1_, int p_setIteratorLimits_2_, int p_setIteratorLimits_3_, int p_setIteratorLimits_4_) {
        this.minX = Math.min(p_setIteratorLimits_1_, p_setIteratorLimits_3_);
        this.minZ = Math.min(p_setIteratorLimits_2_, p_setIteratorLimits_4_);
        this.maxX = Math.max(p_setIteratorLimits_1_, p_setIteratorLimits_3_);
        this.maxZ = Math.max(p_setIteratorLimits_2_, p_setIteratorLimits_4_);
    }

    public void clearIteratorLimits() {
        this.minX = Integer.MIN_VALUE;
        this.minZ = Integer.MIN_VALUE;
        this.maxX = Integer.MIN_VALUE;
        this.maxZ = Integer.MIN_VALUE;
    }
}

