// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.core.LoggerContext;
import java.net.URI;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.core.selector.ClassLoaderContextSelector;

public class AsyncLoggerContextSelector extends ClassLoaderContextSelector
{
    public static boolean isSelected() {
        return AsyncLoggerContextSelector.class.getName().equals(PropertiesUtil.getProperties().getStringProperty("Log4jContextSelector"));
    }
    
    @Override
    protected LoggerContext createContext(final String name, final URI configLocation) {
        return new AsyncLoggerContext(name, null, configLocation);
    }
    
    @Override
    protected String toContextMapKey(final ClassLoader loader) {
        return "AsyncContext@" + Integer.toHexString(System.identityHashCode(loader));
    }
    
    @Override
    protected String defaultContextName() {
        return "DefaultAsyncContext@" + Thread.currentThread().getName();
    }
}
