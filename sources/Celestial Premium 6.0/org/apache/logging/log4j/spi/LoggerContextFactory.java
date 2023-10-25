/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.spi;

import java.net.URI;
import org.apache.logging.log4j.spi.LoggerContext;

public interface LoggerContextFactory {
    public LoggerContext getContext(String var1, ClassLoader var2, boolean var3);

    public LoggerContext getContext(String var1, ClassLoader var2, boolean var3, URI var4);

    public void removeContext(LoggerContext var1);
}

