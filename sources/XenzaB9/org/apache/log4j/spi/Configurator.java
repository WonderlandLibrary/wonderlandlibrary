// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.spi;

import java.net.URL;
import java.io.InputStream;

public interface Configurator
{
    public static final String INHERITED = "inherited";
    public static final String NULL = "null";
    
    void doConfigure(final InputStream inputStream, final LoggerRepository loggerRepository);
    
    void doConfigure(final URL url, final LoggerRepository loggerRepository);
}
