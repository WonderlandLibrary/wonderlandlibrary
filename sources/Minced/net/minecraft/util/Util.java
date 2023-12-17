// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import java.util.List;
import javax.annotation.Nullable;
import java.util.concurrent.ExecutionException;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.FutureTask;
import java.util.Locale;

public class Util
{
    public static EnumOS getOSType() {
        final String s = System.getProperty("os.name").toLowerCase(Locale.ROOT);
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
    public static <V> V runTask(final FutureTask<V> task, final Logger logger) {
        try {
            task.run();
            return task.get();
        }
        catch (ExecutionException executionexception) {
            logger.fatal("Error executing task", (Throwable)executionexception);
            if (executionexception.getCause() instanceof OutOfMemoryError) {
                final OutOfMemoryError outofmemoryerror = (OutOfMemoryError)executionexception.getCause();
                throw outofmemoryerror;
            }
        }
        catch (InterruptedException interruptedexception) {
            logger.fatal("Error executing task", (Throwable)interruptedexception);
        }
        return null;
    }
    
    public static <T> T getLastElement(final List<T> list) {
        return list.get(list.size() - 1);
    }
    
    public enum EnumOS
    {
        LINUX, 
        SOLARIS, 
        WINDOWS, 
        OSX, 
        UNKNOWN;
    }
}
