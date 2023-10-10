// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

import org.apache.logging.log4j.status.StatusLogger;

public final class Closer
{
    private Closer() {
    }
    
    public static boolean close(final AutoCloseable closeable) throws Exception {
        if (closeable != null) {
            StatusLogger.getLogger().debug("Closing {} {}", closeable.getClass().getSimpleName(), closeable);
            closeable.close();
            return true;
        }
        return false;
    }
    
    public static boolean closeSilently(final AutoCloseable closeable) {
        try {
            return close(closeable);
        }
        catch (Exception ignored) {
            return false;
        }
    }
}
