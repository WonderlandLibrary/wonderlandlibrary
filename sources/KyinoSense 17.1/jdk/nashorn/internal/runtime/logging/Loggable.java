/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.logging;

import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.logging.DebugLogger;

public interface Loggable {
    public DebugLogger initLogger(Context var1);

    public DebugLogger getLogger();
}

