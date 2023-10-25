/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.net.server;

import java.io.IOException;
import java.io.InputStream;
import org.apache.logging.log4j.core.LogEventListener;

public interface LogEventBridge<T extends InputStream> {
    public void logEvents(T var1, LogEventListener var2) throws IOException;

    public T wrapStream(InputStream var1) throws IOException;
}

