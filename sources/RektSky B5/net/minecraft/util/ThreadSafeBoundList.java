/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import java.lang.reflect.Array;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ThreadSafeBoundList<T> {
    private final T[] field_152759_a;
    private final Class<? extends T> field_152760_b;
    private final ReadWriteLock field_152761_c = new ReentrantReadWriteLock();
    private int field_152762_d;
    private int field_152763_e;

    public ThreadSafeBoundList(Class<? extends T> p_i1126_1_, int p_i1126_2_) {
        this.field_152760_b = p_i1126_1_;
        this.field_152759_a = (Object[])Array.newInstance(p_i1126_1_, p_i1126_2_);
    }

    public T func_152757_a(T p_152757_1_) {
        this.field_152761_c.writeLock().lock();
        this.field_152759_a[this.field_152763_e] = p_152757_1_;
        this.field_152763_e = (this.field_152763_e + 1) % this.func_152758_b();
        if (this.field_152762_d < this.func_152758_b()) {
            ++this.field_152762_d;
        }
        this.field_152761_c.writeLock().unlock();
        return p_152757_1_;
    }

    public int func_152758_b() {
        this.field_152761_c.readLock().lock();
        int i2 = this.field_152759_a.length;
        this.field_152761_c.readLock().unlock();
        return i2;
    }

    public T[] func_152756_c() {
        Object[] at = (Object[])Array.newInstance(this.field_152760_b, this.field_152762_d);
        this.field_152761_c.readLock().lock();
        for (int i2 = 0; i2 < this.field_152762_d; ++i2) {
            int j2 = (this.field_152763_e - this.field_152762_d + i2) % this.func_152758_b();
            if (j2 < 0) {
                j2 += this.func_152758_b();
            }
            at[i2] = this.field_152759_a[j2];
        }
        this.field_152761_c.readLock().unlock();
        return at;
    }
}

