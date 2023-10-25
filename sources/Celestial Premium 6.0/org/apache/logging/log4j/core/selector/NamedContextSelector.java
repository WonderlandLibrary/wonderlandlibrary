/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.selector;

import java.net.URI;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.selector.ContextSelector;

public interface NamedContextSelector
extends ContextSelector {
    public LoggerContext locateContext(String var1, Object var2, URI var3);

    public LoggerContext removeContext(String var1);
}

