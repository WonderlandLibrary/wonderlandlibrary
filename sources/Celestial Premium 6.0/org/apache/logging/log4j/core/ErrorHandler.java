/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core;

import org.apache.logging.log4j.core.LogEvent;

public interface ErrorHandler {
    public void error(String var1);

    public void error(String var1, Throwable var2);

    public void error(String var1, LogEvent var2, Throwable var3);
}

