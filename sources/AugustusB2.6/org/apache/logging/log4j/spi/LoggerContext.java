// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.message.MessageFactory;

public interface LoggerContext
{
    Object getExternalContext();
    
    default Object getObject(final String key) {
        return null;
    }
    
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
    
    ExtendedLogger getLogger(final String name);
    
    default ExtendedLogger getLogger(final Class<?> cls) {
        final String canonicalName = cls.getCanonicalName();
        return this.getLogger((canonicalName != null) ? canonicalName : cls.getName());
    }
    
    ExtendedLogger getLogger(final String name, final MessageFactory messageFactory);
    
    default ExtendedLogger getLogger(final Class<?> cls, final MessageFactory messageFactory) {
        final String canonicalName = cls.getCanonicalName();
        return this.getLogger((canonicalName != null) ? canonicalName : cls.getName(), messageFactory);
    }
    
    boolean hasLogger(final String name);
    
    boolean hasLogger(final String name, final MessageFactory messageFactory);
    
    boolean hasLogger(final String name, final Class<? extends MessageFactory> messageFactoryClass);
}
