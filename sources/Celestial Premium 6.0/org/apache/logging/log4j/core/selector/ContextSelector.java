/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.selector;

import java.net.URI;
import java.util.List;
import org.apache.logging.log4j.core.LoggerContext;

public interface ContextSelector {
    public LoggerContext getContext(String var1, ClassLoader var2, boolean var3);

    public LoggerContext getContext(String var1, ClassLoader var2, boolean var3, URI var4);

    public List<LoggerContext> getLoggerContexts();

    public void removeContext(LoggerContext var1);
}

