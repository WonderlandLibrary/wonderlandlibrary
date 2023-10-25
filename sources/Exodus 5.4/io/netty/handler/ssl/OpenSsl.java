/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.tomcat.jni.Library
 *  org.apache.tomcat.jni.SSL
 */
package io.netty.handler.ssl;

import io.netty.handler.ssl.OpenSslEngine;
import io.netty.util.internal.NativeLibraryLoader;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.apache.tomcat.jni.Library;
import org.apache.tomcat.jni.SSL;

public final class OpenSsl {
    private static final Throwable UNAVAILABILITY_CAUSE;
    static final String IGNORABLE_ERROR_PREFIX = "error:00000000:";
    private static final InternalLogger logger;

    public static boolean isAvailable() {
        return UNAVAILABILITY_CAUSE == null;
    }

    static {
        logger = InternalLoggerFactory.getInstance(OpenSsl.class);
        Throwable throwable = null;
        try {
            NativeLibraryLoader.load("netty-tcnative", SSL.class.getClassLoader());
            Library.initialize((String)"provided");
            SSL.initialize(null);
        }
        catch (Throwable throwable2) {
            throwable = throwable2;
            logger.debug("Failed to load netty-tcnative; " + OpenSslEngine.class.getSimpleName() + " will be unavailable.", throwable2);
        }
        UNAVAILABILITY_CAUSE = throwable;
    }

    private OpenSsl() {
    }

    public static void ensureAvailability() {
        if (UNAVAILABILITY_CAUSE != null) {
            throw (Error)new UnsatisfiedLinkError("failed to load the required native library").initCause(UNAVAILABILITY_CAUSE);
        }
    }

    public static Throwable unavailabilityCause() {
        return UNAVAILABILITY_CAUSE;
    }
}

