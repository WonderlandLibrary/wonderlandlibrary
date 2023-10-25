/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import javax.annotation.Nullable;
import org.apache.logging.log4j.Logger;

public class Util {
    public static EnumOS getOSType() {
        String s = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if (s.contains("win")) {
            return EnumOS.WINDOWS;
        }
        if (s.contains("mac")) {
            return EnumOS.OSX;
        }
        if (s.contains("solaris")) {
            return EnumOS.SOLARIS;
        }
        if (s.contains("sunos")) {
            return EnumOS.SOLARIS;
        }
        if (s.contains("linux")) {
            return EnumOS.LINUX;
        }
        return s.contains("unix") ? EnumOS.LINUX : EnumOS.UNKNOWN;
    }

    @Nullable
    public static <V> V runTask(FutureTask<V> task, Logger logger) {
        try {
            task.run();
            return task.get();
        }
        catch (ExecutionException executionException) {
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
        return null;
    }

    public static <T> T getLastElement(List<T> list) {
        return list.get(list.size() - 1);
    }

    public static enum EnumOS {
        LINUX,
        SOLARIS,
        WINDOWS,
        OSX,
        UNKNOWN;

    }
}

