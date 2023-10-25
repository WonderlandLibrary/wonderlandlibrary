/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.util;

import org.apache.logging.log4j.status.StatusLogger;

public final class Closer {
    private Closer() {
    }

    public static boolean close(AutoCloseable closeable) throws Exception {
        if (closeable != null) {
            StatusLogger.getLogger().debug("Closing {} {}", closeable.getClass().getSimpleName(), closeable);
            closeable.close();
            return true;
        }
        return false;
    }

    public static boolean closeSilently(AutoCloseable closeable) {
        try {
            return Closer.close(closeable);
        }
        catch (Exception ignored) {
            return false;
        }
    }
}

