package net.minecraft.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import net.minecraft.util.Util;
import org.apache.logging.log4j.Logger;

public class Util {
    public static EnumOS getOSType() {
        String s = System.getProperty((String)"os.name").toLowerCase();
        return s.contains((CharSequence)"win") ? EnumOS.WINDOWS : (s.contains((CharSequence)"mac") ? EnumOS.OSX : (s.contains((CharSequence)"solaris") ? EnumOS.SOLARIS : (s.contains((CharSequence)"sunos") ? EnumOS.SOLARIS : (s.contains((CharSequence)"linux") ? EnumOS.LINUX : (s.contains((CharSequence)"unix") ? EnumOS.LINUX : EnumOS.UNKNOWN)))));
    }

    public static <V> V runTask(FutureTask<V> task, Logger logger) {
        try {
            task.run();
            return (V)task.get();
        }
        catch (ExecutionException executionexception) {
            logger.fatal("Error executing task", (Throwable)executionexception);
            if (executionexception.getCause() instanceof OutOfMemoryError) {
                OutOfMemoryError outofmemoryerror = (OutOfMemoryError)executionexception.getCause();
                throw outofmemoryerror;
            }
        }
        catch (InterruptedException interruptedexception) {
            logger.fatal("Error executing task", (Throwable)interruptedexception);
        }
        return null;
    }
}
