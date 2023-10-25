/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.javafx.logging.PlatformLogger
 */
package com.sun.marlin;

import com.sun.javafx.logging.PlatformLogger;
import com.sun.marlin.MarlinConst;
import java.lang.ref.Cleaner;

public final class MarlinUtils {
    private static final PlatformLogger LOG = MarlinConst.USE_LOGGER ? PlatformLogger.getLogger((String)"prism.marlin") : null;
    private static final Cleaner cleaner = Cleaner.create();

    private MarlinUtils() {
    }

    public static void logInfo(String string) {
        if (MarlinConst.USE_LOGGER) {
            LOG.info(string);
        } else if (MarlinConst.ENABLE_LOGS) {
            System.out.print("INFO: ");
            System.out.println(string);
        }
    }

    public static void logException(String string, Throwable throwable) {
        if (MarlinConst.USE_LOGGER) {
            LOG.warning(string, throwable);
        } else if (MarlinConst.ENABLE_LOGS) {
            System.out.print("WARNING: ");
            System.out.println(string);
            throwable.printStackTrace(System.err);
        }
    }

    public static ThreadGroup getRootThreadGroup() {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        ThreadGroup threadGroup2 = threadGroup.getParent();
        while (threadGroup2 != null) {
            threadGroup = threadGroup2;
            threadGroup2 = threadGroup.getParent();
        }
        return threadGroup;
    }

    static Cleaner getCleaner() {
        return cleaner;
    }
}

