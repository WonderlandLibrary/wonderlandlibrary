package net.optifine.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import net.minecraft.src.Config;
import net.optifine.util.LongSupplier;
import net.optifine.util.NativeMemory;

public class NativeMemory {
    private static LongSupplier bufferAllocatedSupplier = NativeMemory.makeLongSupplier(new String[][]{{"sun.misc.SharedSecrets", "getJavaNioAccess", "getDirectBufferPool", "getMemoryUsed"}, {"jdk.internal.misc.SharedSecrets", "getJavaNioAccess", "getDirectBufferPool", "getMemoryUsed"}});
    private static LongSupplier bufferMaximumSupplier = NativeMemory.makeLongSupplier(new String[][]{{"sun.misc.VM", "maxDirectMemory"}, {"jdk.internal.misc.VM", "maxDirectMemory"}});

    public static long getBufferAllocated() {
        return bufferAllocatedSupplier == null ? -1L : bufferAllocatedSupplier.getAsLong();
    }

    public static long getBufferMaximum() {
        return bufferMaximumSupplier == null ? -1L : bufferMaximumSupplier.getAsLong();
    }

    private static LongSupplier makeLongSupplier(String[][] paths) {
        ArrayList list = new ArrayList();
        for (int i = 0; i < paths.length; ++i) {
            String[] astring = paths[i];
            try {
                LongSupplier longsupplier = NativeMemory.makeLongSupplier(astring);
                return longsupplier;
            }
            catch (Throwable throwable) {
                list.add((Object)throwable);
                continue;
            }
        }
        for (Throwable throwable1 : list) {
            Config.warn((String)("" + throwable1.getClass().getName() + ": " + throwable1.getMessage()));
        }
        return null;
    }

    private static LongSupplier makeLongSupplier(String[] path) throws Exception {
        if (path.length < 2) {
            return null;
        }
        Class oclass = Class.forName((String)path[0]);
        Method method = oclass.getMethod(path[1], new Class[0]);
        method.setAccessible(true);
        Object object = null;
        for (int i = 2; i < path.length; ++i) {
            String s = path[i];
            object = method.invoke(object, new Object[0]);
            method = object.getClass().getMethod(s, new Class[0]);
            method.setAccessible(true);
        }
        Method method_f = method;
        Object o_f = object;
        1 longsupplier = new /* Unavailable Anonymous Inner Class!! */;
        return longsupplier;
    }
}
