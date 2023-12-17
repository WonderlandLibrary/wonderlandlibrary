// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.util;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import net.minecraft.src.Config;
import java.util.ArrayList;
import java.util.function.LongSupplier;

public class NativeMemory
{
    private static LongSupplier bufferAllocatedSupplier;
    private static LongSupplier bufferMaximumSupplier;
    
    public static long getBufferAllocated() {
        return (NativeMemory.bufferAllocatedSupplier == null) ? -1L : NativeMemory.bufferAllocatedSupplier.getAsLong();
    }
    
    public static long getBufferMaximum() {
        return (NativeMemory.bufferMaximumSupplier == null) ? -1L : NativeMemory.bufferMaximumSupplier.getAsLong();
    }
    
    private static LongSupplier makeLongSupplier(final String[][] paths) {
        final List<Throwable> list = new ArrayList<Throwable>();
        int i = 0;
        while (i < paths.length) {
            final String[] astring = paths[i];
            try {
                final LongSupplier longsupplier = makeLongSupplier(astring);
                return longsupplier;
            }
            catch (Throwable throwable) {
                list.add(throwable);
                ++i;
                continue;
            }
            break;
        }
        for (final Throwable throwable2 : list) {
            Config.warn("" + throwable2.getClass().getName() + ": " + throwable2.getMessage());
        }
        return null;
    }
    
    private static LongSupplier makeLongSupplier(final String[] path) throws Exception {
        if (path.length < 2) {
            return null;
        }
        final Class<?> cls = Class.forName(path[0]);
        Method method = cls.getMethod(path[1], (Class<?>[])new Class[0]);
        method.setAccessible(true);
        Object object = null;
        for (int i2 = 2; i2 < path.length; ++i2) {
            final String name = path[i2];
            object = method.invoke(object, new Object[0]);
            method = object.getClass().getMethod(name, (Class<?>[])new Class[0]);
            method.setAccessible(true);
        }
        final Object objectF = object;
        final Method methodF = method;
        final LongSupplier ls = new LongSupplier() {
            private boolean disabled = false;
            
            @Override
            public long getAsLong() {
                if (this.disabled) {
                    return -1L;
                }
                try {
                    return (long)methodF.invoke(objectF, new Object[0]);
                }
                catch (Throwable e2) {
                    Config.warn("" + e2.getClass().getName() + ": " + e2.getMessage());
                    this.disabled = true;
                    return -1L;
                }
            }
        };
        return ls;
    }
    
    static {
        NativeMemory.bufferAllocatedSupplier = makeLongSupplier(new String[][] { { "sun.misc.SharedSecrets", "getJavaNioAccess", "getDirectBufferPool", "getMemoryUsed" }, { "jdk.internal.misc.SharedSecrets", "getJavaNioAccess", "getDirectBufferPool", "getMemoryUsed" } });
        NativeMemory.bufferMaximumSupplier = makeLongSupplier(new String[][] { { "sun.misc.VM", "maxDirectMemory" }, { "jdk.internal.misc.VM", "maxDirectMemory" } });
    }
}
