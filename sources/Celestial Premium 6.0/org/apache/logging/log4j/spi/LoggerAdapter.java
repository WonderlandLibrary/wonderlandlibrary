/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.spi;

import java.io.Closeable;

public interface LoggerAdapter<L>
extends Closeable {
    public L getLogger(String var1);
}

