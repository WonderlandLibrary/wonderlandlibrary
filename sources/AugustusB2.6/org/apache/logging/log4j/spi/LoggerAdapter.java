// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.spi;

import java.io.Closeable;

public interface LoggerAdapter<L> extends Closeable
{
    L getLogger(final String name);
}
