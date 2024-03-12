// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.util;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import net.minecraft.src.Config;
import java.util.ArrayList;

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
        final Class oclass = Class.forName(path[0]);
        Method method = oclass.getMethod(path[1], (Class[])new Class[0]);
        method.setAccessible(true);
        Object object = null;
        for (int i = 2; i < path.length; ++i) {
            final String s = path[i];
            object = method.invoke(object, new Object[0]);
            method = object.getClass().getMethod(s, (Class<?>[])new Class[0]);
            method.setAccessible(true);
        }
        final Method method2 = method;
        final Object o = object;
        final LongSupplier longsupplier = new LongSupplier() {
            private boolean disabled = false;
            
            @Override
            public long getAsLong() {
                if (this.disabled) {
                    return -1L;
                }
                try {
                    return (long)method2.invoke(o, new Object[0]);
                }
                catch (Throwable throwable) {
                    Config.warn("" + throwable.getClass().getName() + ": " + throwable.getMessage());
                    this.disabled = true;
                    return -1L;
                }
            }
        };
        return longsupplier;
    }
    
    static {
        NativeMemory.bufferAllocatedSupplier = makeLongSupplier(new String[][] { { "sun.misc.SharedSecrets", "getJavaNioAccess", "getDirectBufferPool", "getMemoryUsed" }, { "jdk.internal.misc.SharedSecrets", "getJavaNioAccess", "getDirectBufferPool", "getMemoryUsed" } });
        NativeMemory.bufferMaximumSupplier = makeLongSupplier(new String[][] { { "sun.misc.VM", "maxDirectMemory" }, { "jdk.internal.misc.VM", "maxDirectMemory" } });
    }
}
