// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.MessageFactory;

public interface LoggerContext
{
    public static final LoggerContext[] EMPTY_ARRAY = new LoggerContext[0];
    
    Object getExternalContext();
    
    default ExtendedLogger getLogger(final Class<?> cls) {
        final String canonicalName = cls.getCanonicalName();
        return this.getLogger((canonicalName != null) ? canonicalName : cls.getName());
    }
    
    default ExtendedLogger getLogger(final Class<?> cls, final MessageFactory messageFactory) {
        final String canonicalName = cls.getCanonicalName();
        return this.getLogger((canonicalName != null) ? canonicalName : cls.getName(), messageFactory);
    }
    
    ExtendedLogger getLogger(final String name);
    
    ExtendedLogger getLogger(final String name, final MessageFactory messageFactory);
    
    default LoggerRegistry<? extends Logger> getLoggerRegistry() {
        return null;
    }
    
    default Object getObject(final String key) {
        return null;
    }
    
    boolean hasLogger(final String name);
    
    boolean hasLogger(final String name, final Class<? extends MessageFactory> messageFactoryClass);
    
    boolean hasLogger(final String name, final MessageFactory messageFactory);
    
    default Object putObject(final String key, final Object value) {
        return null;
    }
    
    default Object putObjectIfAbsent(final String key, final Object value) {
        return null;
    }
    
    default Object removeObject(final String key) {
        return null;
    }
    
    default boolean removeObject(final String key, final Object value) {
        return false;
    }
}
