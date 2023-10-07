package net.minecraft.util;

import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public final class Util {

    public static Util.EnumOS getOSType() {
        final String s = System.getProperty("os.name").toLowerCase();
        return s.contains("win") ? Util.EnumOS.WINDOWS : s.contains("mac") ? EnumOS.OSX : s.contains("solaris") ? EnumOS.SOLARIS : s.contains("sunos") ? EnumOS.SOLARIS : s.contains("linux") ? EnumOS.LINUX : s.contains("unix") ? EnumOS.LINUX : EnumOS.UNKNOWN;
    }

    public static <V> V func_181617_a(FutureTask<V> p_181617_0_, Logger p_181617_1_) {
        try {
            p_181617_0_.run();
            return p_181617_0_.get();
        } catch (ExecutionException | InterruptedException e) {
            p_181617_1_.fatal("Error executing task", e);
        }

        return null;
    }

    public enum EnumOS {

        LINUX,
        SOLARIS,
        WINDOWS,
        OSX,
        UNKNOWN

    }

}
