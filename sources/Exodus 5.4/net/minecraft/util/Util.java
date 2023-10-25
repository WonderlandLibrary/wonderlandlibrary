/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import org.apache.logging.log4j.Logger;

public class Util {
    public static <V> V func_181617_a(FutureTask<V> futureTask, Logger logger) {
        try {
            futureTask.run();
            return futureTask.get();
        }
        catch (ExecutionException executionException) {
            logger.fatal("Error executing task", (Throwable)executionException);
        }
        catch (InterruptedException interruptedException) {
            logger.fatal("Error executing task", (Throwable)interruptedException);
        }
        return null;
    }

    public static EnumOS getOSType() {
        String string = System.getProperty("os.name").toLowerCase();
        return string.contains("win") ? EnumOS.WINDOWS : (string.contains("mac") ? EnumOS.OSX : (string.contains("solaris") ? EnumOS.SOLARIS : (string.contains("sunos") ? EnumOS.SOLARIS : (string.contains("linux") ? EnumOS.LINUX : (string.contains("unix") ? EnumOS.LINUX : EnumOS.UNKNOWN)))));
    }

    public static enum EnumOS {
        LINUX,
        SOLARIS,
        WINDOWS,
        OSX,
        UNKNOWN;

    }
}

