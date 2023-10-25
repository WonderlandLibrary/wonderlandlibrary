/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.impl;

import org.apache.logging.log4j.core.LoggerContext;

public final class ContextAnchor {
    public static final ThreadLocal<LoggerContext> THREAD_CONTEXT = new ThreadLocal();

    private ContextAnchor() {
    }
}

