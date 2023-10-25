package net.optifine.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.LongSupplier;

import net.minecraft.src.Config;

public class NativeMemory {
    private static LongSupplier bufferAllocatedSupplier = makeLongSupplier(new String[][]{{"sun.misc.SharedSecrets", "getJavaNioAccess", "getDirectBufferPool", "getMemoryUsed"}, {"jdk.internal.misc.SharedSecrets", "getJavaNioAccess", "getDirectBufferPool", "getMemoryUsed"}});
    private static LongSupplier bufferMaximumSupplier = makeLongSupplier(new String[][]{{"sun.misc.VM", "maxDirectMemory"}, {"jdk.internal.misc.VM", "maxDirectMemory"}});

    public static long getBufferAllocated() {
        return bufferAllocatedSupplier == null ? -1L : bufferAllocatedSupplier.getAsLong();
    }

    public static long getBufferMaximum() {
        return bufferMaximumSupplier == null ? -1L : bufferMaximumSupplier.getAsLong();
    }

    private static LongSupplier makeLongSupplier(String[][] paths) {
        List<Throwable> list = new ArrayList<Throwable>();

        for (int i = 0; i < paths.length; ++i) {
            String[] astring = paths[i];

            try {
                LongSupplier longsupplier = makeLongSupplier(astring);
                return longsupplier;
            } catch (Throwable throwable) {
                list.add(throwable);
            }
        }

        for (Throwable throwable1 : list) {
            Config.warn("" + throwable1.getClass().getName() + ": " + throwable1.getMessage());
        }

        return null;
    }

    private static LongSupplier makeLongSupplier(String[] path) throws Exception {
        if (path.length < 2) {
            return null;
        }
        Class<?> cls = Class.forName(path[0]);
        Method method = cls.getMethod(path[1], new Class[0]);
        method.setAccessible(true);
        Object object = null;
        for (int i2 = 2; i2 < path.length; ++i2) {
            String name = path[i2];
            object = method.invoke(object, new Object[0]);
            method = object.getClass().getMethod(name, new Class[0]);
            method.setAccessible(true);
        }
        final Object objectF = object;
        final Method methodF = method;
        LongSupplier ls = new LongSupplier() {
            private boolean disabled = false;

            @Override
            public long getAsLong() {
                if (this.disabled) {
                    return -1L;
                }
                try {
                    return (Long) methodF.invoke(objectF, new Object[0]);
                } catch (Throwable e2) {
                    Config.warn("" + e2.getClass().getName() + ": " + e2.getMessage());
                    this.disabled = true;
                    return -1L;
                }
            }
        };
        return ls;
    }
}
