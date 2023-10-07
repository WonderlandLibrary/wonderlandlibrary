// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.spi;

import org.apache.log4j.Logger;

public interface LoggerFactory
{
    Logger makeNewLoggerInstance(final String name);
}
