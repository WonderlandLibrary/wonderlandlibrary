/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;

public class ExecutorServices {
    private static final Logger LOGGER = StatusLogger.getLogger();

    public static boolean shutdown(ExecutorService executorService, long timeout, TimeUnit timeUnit, String source) {
        if (executorService == null || executorService.isTerminated()) {
            return true;
        }
        executorService.shutdown();
        if (timeout > 0L && timeUnit == null) {
            throw new IllegalArgumentException(String.format("%s can't shutdown %s when timeout = %,d and timeUnit = %s.", new Object[]{source, executorService, timeout, timeUnit}));
        }
        if (timeout > 0L) {
            try {
                if (!executorService.awaitTermination(timeout, timeUnit)) {
                    executorService.shutdownNow();
                    if (!executorService.awaitTermination(timeout, timeUnit)) {
                        LOGGER.error("{} pool {} did not terminate after {} {}", source, executorService, timeout, (Object)timeUnit);
                    }
                    return false;
                }
            }
            catch (InterruptedException ie) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        } else {
            executorService.shutdown();
        }
        return true;
    }
}

