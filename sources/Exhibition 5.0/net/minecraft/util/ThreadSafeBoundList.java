// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import java.lang.reflect.Array;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReadWriteLock;

public class ThreadSafeBoundList
{
    private final Object[] field_152759_a;
    private final Class field_152760_b;
    private final ReadWriteLock field_152761_c;
    private int field_152762_d;
    private int field_152763_e;
    private static final String __OBFID = "CL_00001868";
    
    public ThreadSafeBoundList(final Class p_i1126_1_, final int p_i1126_2_) {
        this.field_152761_c = new ReentrantReadWriteLock();
        this.field_152760_b = p_i1126_1_;
        this.field_152759_a = (Object[])Array.newInstance(p_i1126_1_, p_i1126_2_);
    }
    
    public Object func_152757_a(final Object p_152757_1_) {
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
        final int var1 = this.field_152759_a.length;
        this.field_152761_c.readLock().unlock();
        return var1;
    }
    
    public Object[] func_152756_c() {
        final Object[] var1 = (Object[])Array.newInstance(this.field_152760_b, this.field_152762_d);
        this.field_152761_c.readLock().lock();
        for (int var2 = 0; var2 < this.field_152762_d; ++var2) {
            int var3 = (this.field_152763_e - this.field_152762_d + var2) % this.func_152758_b();
            if (var3 < 0) {
                var3 += this.func_152758_b();
            }
            var1[var2] = this.field_152759_a[var3];
        }
        this.field_152761_c.readLock().unlock();
        return var1;
    }
}
