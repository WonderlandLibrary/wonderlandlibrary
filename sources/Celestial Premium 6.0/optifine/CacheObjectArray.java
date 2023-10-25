/*
 * Decompiled with CFR 0.150.
 */
package optifine;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import net.minecraft.block.state.IBlockState;
import optifine.Config;

public class CacheObjectArray {
    private static ArrayDeque<int[]> arrays = new ArrayDeque();
    private static int maxCacheSize = 10;

    private static synchronized int[] allocateArray(int p_allocateArray_0_) {
        int[] aint = arrays.pollLast();
        if (aint == null || aint.length < p_allocateArray_0_) {
            aint = new int[p_allocateArray_0_];
        }
        return aint;
    }

    public static synchronized void freeArray(int[] p_freeArray_0_) {
        if (arrays.size() < maxCacheSize) {
            arrays.add(p_freeArray_0_);
        }
    }

    public static void main(String[] p_main_0_) throws Exception {
        int i = 4096;
        int j = 500000;
        CacheObjectArray.testNew(i, j);
        CacheObjectArray.testClone(i, j);
        CacheObjectArray.testNewObj(i, j);
        CacheObjectArray.testCloneObj(i, j);
        CacheObjectArray.testNewObjDyn(IBlockState.class, i, j);
        long k = CacheObjectArray.testNew(i, j);
        long l = CacheObjectArray.testClone(i, j);
        long i1 = CacheObjectArray.testNewObj(i, j);
        long j1 = CacheObjectArray.testCloneObj(i, j);
        long k1 = CacheObjectArray.testNewObjDyn(IBlockState.class, i, j);
        Config.dbg("New: " + k);
        Config.dbg("Clone: " + l);
        Config.dbg("NewObj: " + i1);
        Config.dbg("CloneObj: " + j1);
        Config.dbg("NewObjDyn: " + k1);
    }

    private static long testClone(int p_testClone_0_, int p_testClone_1_) {
        long i = System.currentTimeMillis();
        int[] aint = new int[p_testClone_0_];
        for (int j = 0; j < p_testClone_1_; ++j) {
            int[] arrn = (int[])aint.clone();
        }
        long k = System.currentTimeMillis();
        return k - i;
    }

    private static long testNew(int p_testNew_0_, int p_testNew_1_) {
        long i = System.currentTimeMillis();
        for (int j = 0; j < p_testNew_1_; ++j) {
            int[] arrn = (int[])Array.newInstance(Integer.TYPE, p_testNew_0_);
        }
        long k = System.currentTimeMillis();
        return k - i;
    }

    private static long testCloneObj(int p_testCloneObj_0_, int p_testCloneObj_1_) {
        long i = System.currentTimeMillis();
        IBlockState[] aiblockstate = new IBlockState[p_testCloneObj_0_];
        for (int j = 0; j < p_testCloneObj_1_; ++j) {
            IBlockState[] arriBlockState = (IBlockState[])aiblockstate.clone();
        }
        long k = System.currentTimeMillis();
        return k - i;
    }

    private static long testNewObj(int p_testNewObj_0_, int p_testNewObj_1_) {
        long i = System.currentTimeMillis();
        for (int j = 0; j < p_testNewObj_1_; ++j) {
            IBlockState[] arriBlockState = new IBlockState[p_testNewObj_0_];
        }
        long k = System.currentTimeMillis();
        return k - i;
    }

    private static long testNewObjDyn(Class p_testNewObjDyn_0_, int p_testNewObjDyn_1_, int p_testNewObjDyn_2_) {
        long i = System.currentTimeMillis();
        for (int j = 0; j < p_testNewObjDyn_2_; ++j) {
            Object[] arrobject = (Object[])Array.newInstance(p_testNewObjDyn_0_, p_testNewObjDyn_1_);
        }
        long k = System.currentTimeMillis();
        return k - i;
    }
}

