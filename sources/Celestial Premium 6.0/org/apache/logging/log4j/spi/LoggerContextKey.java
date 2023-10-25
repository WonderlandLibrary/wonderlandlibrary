/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.AbstractLogger;

@Deprecated
public class LoggerContextKey {
    public static String create(String name) {
        return LoggerContextKey.create(name, AbstractLogger.DEFAULT_MESSAGE_FACTORY_CLASS);
    }

    public static String create(String name, MessageFactory messageFactory) {
        Class<MessageFactory> messageFactoryClass = messageFactory != null ? messageFactory.getClass() : AbstractLogger.DEFAULT_MESSAGE_FACTORY_CLASS;
        return LoggerContextKey.create(name, messageFactoryClass);
    }

    public static String create(String name, Class<? extends MessageFactory> messageFactoryClass) {
        Class<? extends MessageFactory> mfClass = messageFactoryClass != null ? messageFactoryClass : AbstractLogger.DEFAULT_MESSAGE_FACTORY_CLASS;
        return name + "." + mfClass.getName();
    }
}

