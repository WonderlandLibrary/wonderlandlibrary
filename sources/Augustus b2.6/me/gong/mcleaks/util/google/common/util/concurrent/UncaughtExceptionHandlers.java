// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.util.concurrent;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.gong.mcleaks.util.google.common.annotations.VisibleForTesting;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;

@GwtIncompatible
public final class UncaughtExceptionHandlers
{
    private UncaughtExceptionHandlers() {
    }
    
    public static Thread.UncaughtExceptionHandler systemExit() {
        return new Exiter(Runtime.getRuntime());
    }
    
    @VisibleForTesting
    static final class Exiter implements Thread.UncaughtExceptionHandler
    {
        private static final Logger logger;
        private final Runtime runtime;
        
        Exiter(final Runtime runtime) {
            this.runtime = runtime;
        }
        
        @Override
        public void uncaughtException(final Thread t, final Throwable e) {
            try {
                Exiter.logger.log(Level.SEVERE, String.format(Locale.ROOT, "Caught an exception in %s.  Shutting down.", t), e);
            }
            catch (Throwable errorInLogging) {
                System.err.println(e.getMessage());
                System.err.println(errorInLogging.getMessage());
            }
            finally {
                this.runtime.exit(1);
            }
        }
        
        static {
            logger = Logger.getLogger(Exiter.class.getName());
        }
    }
}
