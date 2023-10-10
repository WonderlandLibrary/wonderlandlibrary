// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.message.MessageFactory;

@Deprecated
public class LoggerContextKey
{
    public static String create(final String name) {
        return create(name, AbstractLogger.DEFAULT_MESSAGE_FACTORY_CLASS);
    }
    
    public static String create(final String name, final MessageFactory messageFactory) {
        final Class<? extends MessageFactory> messageFactoryClass = (messageFactory != null) ? messageFactory.getClass() : AbstractLogger.DEFAULT_MESSAGE_FACTORY_CLASS;
        return create(name, messageFactoryClass);
    }
    
    public static String create(final String name, final Class<? extends MessageFactory> messageFactoryClass) {
        final Class<? extends MessageFactory> mfClass = (messageFactoryClass != null) ? messageFactoryClass : AbstractLogger.DEFAULT_MESSAGE_FACTORY_CLASS;
        return name + "." + mfClass.getName();
    }
}
