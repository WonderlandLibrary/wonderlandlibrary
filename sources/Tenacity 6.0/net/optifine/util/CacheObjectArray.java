// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.util;

import java.lang.reflect.Array;
import net.minecraft.src.Config;
import net.minecraft.block.state.IBlockState;
import java.util.ArrayDeque;

public class CacheObjectArray
{
    private static ArrayDeque<int[]> arrays;
    private static int maxCacheSize;
    
    private static synchronized int[] allocateArray(final int size) {
        int[] aint = CacheObjectArray.arrays.pollLast();
        if (aint == null || aint.length < size) {
            aint = new int[size];
        }
        return aint;
    }
    
    public static synchronized void freeArray(final int[] ints) {
        if (CacheObjectArray.arrays.size() < CacheObjectArray.maxCacheSize) {
            CacheObjectArray.arrays.add(ints);
        }
    }
    
    public static void main(final String[] args) throws Exception {
        final int i = 4096;
        final int j = 500000;
        testNew(i, j);
        testClone(i, j);
        testNewObj(i, j);
        testCloneObj(i, j);
        testNewObjDyn(IBlockState.class, i, j);
        final long k = testNew(i, j);
        final long l = testClone(i, j);
        final long i2 = testNewObj(i, j);
        final long j2 = testCloneObj(i, j);
        final long k2 = testNewObjDyn(IBlockState.class, i, j);
        Config.dbg("New: " + k);
        Config.dbg("Clone: " + l);
        Config.dbg("NewObj: " + i2);
        Config.dbg("CloneObj: " + j2);
        Config.dbg("NewObjDyn: " + k2);
    }
    
    private static long testClone(final int size, final int count) {
        final long i = System.currentTimeMillis();
        final int[] aint = new int[size];
        for (int j = 0; j < count; ++j) {
            final int[] array = aint.clone();
        }
        final long k = System.currentTimeMillis();
        return k - i;
    }
    
    private static long testNew(final int size, final int count) {
        final long i = System.currentTimeMillis();
        for (int j = 0; j < count; ++j) {
            final int[] array = (int[])Array.newInstance(Integer.TYPE, size);
        }
        final long k = System.currentTimeMillis();
        return k - i;
    }
    
    private static long testCloneObj(final int size, final int count) {
        final long i = System.currentTimeMillis();
        final IBlockState[] aiblockstate = new IBlockState[size];
        for (int j = 0; j < count; ++j) {
            final IBlockState[] array = aiblockstate.clone();
        }
        final long k = System.currentTimeMillis();
        return k - i;
    }
    
    private static long testNewObj(final int size, final int count) {
        final long i = System.currentTimeMillis();
        for (int j = 0; j < count; ++j) {
            final IBlockState[] array = new IBlockState[size];
        }
        final long k = System.currentTimeMillis();
        return k - i;
    }
    
    private static long testNewObjDyn(final Class cls, final int size, final int count) {
        final long i = System.currentTimeMillis();
        for (int j = 0; j < count; ++j) {
            final Object[] array = (Object[])Array.newInstance(cls, size);
        }
        final long k = System.currentTimeMillis();
        return k - i;
    }
    
    static {
        CacheObjectArray.arrays = new ArrayDeque<int[]>();
        CacheObjectArray.maxCacheSize = 10;
    }
}
