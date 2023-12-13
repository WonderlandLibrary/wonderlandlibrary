/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import org.apache.logging.log4j.Logger;

public class Util {
    public static EnumOS getOSType() {
        String s2 = System.getProperty("os.name").toLowerCase();
        return s2.contains("win") ? EnumOS.WINDOWS : (s2.contains("mac") ? EnumOS.OSX : (s2.contains("solaris") ? EnumOS.SOLARIS : (s2.contains("sunos") ? EnumOS.SOLARIS : (s2.contains("linux") ? EnumOS.LINUX : (s2.contains("unix") ? EnumOS.LINUX : EnumOS.UNKNOWN)))));
    }

    public static <V> V func_181617_a(FutureTask<V> p_181617_0_, Logger p_181617_1_) {
        try {
            p_181617_0_.run();
            return p_181617_0_.get();
        }
        catch (ExecutionException executionexception) {
            p_181617_1_.fatal("Error executing task", (Throwable)executionexception);
        }
        catch (InterruptedException interruptedexception) {
            p_181617_1_.fatal("Error executing task", (Throwable)interruptedexception);
        }
        return null;
    }

    public static enum EnumOS {
        LINUX,
        SOLARIS,
        WINDOWS,
        OSX,
        UNKNOWN;

    }
}

