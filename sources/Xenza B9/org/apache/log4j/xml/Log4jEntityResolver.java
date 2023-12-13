// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.xml;

import org.apache.logging.log4j.status.StatusLogger;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import org.apache.logging.log4j.util.Constants;
import org.xml.sax.InputSource;
import org.apache.logging.log4j.Logger;
import org.xml.sax.EntityResolver;

public class Log4jEntityResolver implements EntityResolver
{
    private static final Logger LOGGER;
    private static final String PUBLIC_ID = "-//APACHE//DTD LOG4J 1.2//EN";
    
    @Override
    public InputSource resolveEntity(final String publicId, final String systemId) {
        if (systemId.endsWith("log4j.dtd") || "-//APACHE//DTD LOG4J 1.2//EN".equals(publicId)) {
            final Class<?> clazz = this.getClass();
            InputStream in = clazz.getResourceAsStream("/org/apache/log4j/xml/log4j.dtd");
            if (in == null) {
                Log4jEntityResolver.LOGGER.warn("Could not find [log4j.dtd] using [{}] class loader, parsed without DTD.", clazz.getClassLoader());
                in = new ByteArrayInputStream(Constants.EMPTY_BYTE_ARRAY);
            }
            return new InputSource(in);
        }
        return null;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
