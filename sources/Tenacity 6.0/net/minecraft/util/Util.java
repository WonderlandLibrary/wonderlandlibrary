// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import java.util.concurrent.ExecutionException;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.FutureTask;

public class Util
{
    public static EnumOS getOSType() {
        final String s = System.getProperty("os.name").toLowerCase();
        return s.contains("win") ? EnumOS.WINDOWS : (s.contains("mac") ? EnumOS.OSX : (s.contains("solaris") ? EnumOS.SOLARIS : (s.contains("sunos") ? EnumOS.SOLARIS : (s.contains("linux") ? EnumOS.LINUX : (s.contains("unix") ? EnumOS.LINUX : EnumOS.UNKNOWN)))));
    }
    
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
    
    public enum EnumOS
    {
        LINUX, 
        SOLARIS, 
        WINDOWS, 
        OSX, 
        UNKNOWN;
    }
}
