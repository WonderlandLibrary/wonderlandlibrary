/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk.quantum;

import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.tk.quantum.PerformanceLogger;
import com.sun.prism.impl.PrismSettings;
import java.security.AccessController;
import java.security.PrivilegedAction;

abstract class PerformanceTrackerHelper {
    private static final PerformanceTrackerHelper instance = PerformanceTrackerHelper.createInstance();

    public static PerformanceTrackerHelper getInstance() {
        return instance;
    }

    private PerformanceTrackerHelper() {
    }

    private static PerformanceTrackerHelper createInstance() {
        PerformanceTrackerHelper performanceTrackerHelper = AccessController.doPrivileged(new PrivilegedAction<PerformanceTrackerHelper>(){

            @Override
            public PerformanceTrackerHelper run() {
                try {
                    if (PrismSettings.perfLog != null) {
                        final PerformanceTrackerDefaultImpl performanceTrackerDefaultImpl = new PerformanceTrackerDefaultImpl();
                        if (PrismSettings.perfLogExitFlush) {
                            Runtime.getRuntime().addShutdownHook(new Thread(){

                                @Override
                                public void run() {
                                    performanceTrackerDefaultImpl.outputLog();
                                }
                            });
                        }
                        return performanceTrackerDefaultImpl;
                    }
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
                return null;
            }
        });
        if (performanceTrackerHelper == null) {
            performanceTrackerHelper = new PerformanceTrackerDummyImpl();
        }
        return performanceTrackerHelper;
    }

    public abstract void logEvent(String var1);

    public abstract void outputLog();

    public abstract boolean isPerfLoggingEnabled();

    public final long nanoTime() {
        return Toolkit.getToolkit().getPrimaryTimer().nanos();
    }

    private static final class PerformanceTrackerDummyImpl
    extends PerformanceTrackerHelper {
        private PerformanceTrackerDummyImpl() {
        }

        @Override
        public void logEvent(String string) {
        }

        @Override
        public void outputLog() {
        }

        @Override
        public boolean isPerfLoggingEnabled() {
            return false;
        }
    }

    private static final class PerformanceTrackerDefaultImpl
    extends PerformanceTrackerHelper {
        private long firstTime;
        private long lastTime;

        private PerformanceTrackerDefaultImpl() {
        }

        @Override
        public void logEvent(String string) {
            long l = System.currentTimeMillis();
            if (this.firstTime == 0L) {
                this.firstTime = l;
            }
            PerformanceLogger.setTime("JavaFX> " + string + " (" + (l - this.firstTime) + "ms total, " + (l - this.lastTime) + "ms)");
            this.lastTime = l;
        }

        @Override
        public void outputLog() {
            this.logLaunchTime();
            PerformanceLogger.outputLog();
        }

        @Override
        public boolean isPerfLoggingEnabled() {
            return true;
        }

        private void logLaunchTime() {
            try {
                String string;
                if (PerformanceLogger.getStartTime() <= 0L && (string = AccessController.doPrivileged(() -> System.getProperty("launchTime"))) != null && !string.equals("")) {
                    long l = Long.parseLong(string);
                    PerformanceLogger.setStartTime("LaunchTime", l);
                }
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
}

