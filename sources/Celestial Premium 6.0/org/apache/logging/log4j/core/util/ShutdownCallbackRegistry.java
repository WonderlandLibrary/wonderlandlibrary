/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.util;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.util.Cancellable;

public interface ShutdownCallbackRegistry {
    public static final String SHUTDOWN_CALLBACK_REGISTRY = "log4j.shutdownCallbackRegistry";
    public static final String SHUTDOWN_HOOK_ENABLED = "log4j.shutdownHookEnabled";
    public static final Marker SHUTDOWN_HOOK_MARKER = MarkerManager.getMarker("SHUTDOWN HOOK");

    public Cancellable addShutdownCallback(Runnable var1);
}

