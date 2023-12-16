// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class Multithreading
{
    private static final ExecutorService SERVICE;
    
    static {
        SERVICE = Executors.newFixedThreadPool(50, Thread::new);
    }
    
    public static void runAsync(final Runnable task) {
        Multithreading.SERVICE.execute(task);
    }
}
