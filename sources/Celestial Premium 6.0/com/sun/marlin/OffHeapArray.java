/*
 * Decompiled with CFR 0.150.
 */
package com.sun.marlin;

import com.sun.marlin.MarlinConst;
import com.sun.marlin.MarlinUtils;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import sun.misc.Unsafe;

final class OffHeapArray {
    static final Unsafe UNSAFE;
    static final int SIZE_INT;
    long address;
    long length;
    int used;

    OffHeapArray(Object object, long l) {
        this.address = UNSAFE.allocateMemory(l);
        this.length = l;
        this.used = 0;
        if (MarlinConst.LOG_UNSAFE_MALLOC) {
            MarlinUtils.logInfo(System.currentTimeMillis() + ": OffHeapArray.allocateMemory =   " + l + " to addr = " + this.address);
        }
        MarlinUtils.getCleaner().register(object, () -> this.free());
    }

    void resize(long l) {
        this.address = UNSAFE.reallocateMemory(this.address, l);
        this.length = l;
        if (MarlinConst.LOG_UNSAFE_MALLOC) {
            MarlinUtils.logInfo(System.currentTimeMillis() + ": OffHeapArray.reallocateMemory = " + l + " to addr = " + this.address);
        }
    }

    void free() {
        UNSAFE.freeMemory(this.address);
        if (MarlinConst.LOG_UNSAFE_MALLOC) {
            MarlinUtils.logInfo(System.currentTimeMillis() + ": OffHeapArray.freeMemory =       " + this.length + " at addr = " + this.address);
        }
        this.address = 0L;
    }

    void fill(byte by) {
        UNSAFE.setMemory(this.address, this.length, by);
    }

    static {
        Unsafe unsafe;
        UNSAFE = unsafe = AccessController.doPrivileged(new PrivilegedAction<Unsafe>(){

            @Override
            public Unsafe run() {
                Unsafe unsafe = null;
                try {
                    Field field = Unsafe.class.getDeclaredField("theUnsafe");
                    field.setAccessible(true);
                    unsafe = (Unsafe)field.get(null);
                }
                catch (Exception exception) {
                    throw new InternalError("Unable to get sun.misc.Unsafe instance", exception);
                }
                return unsafe;
            }
        });
        SIZE_INT = Unsafe.ARRAY_INT_INDEX_SCALE;
    }
}

