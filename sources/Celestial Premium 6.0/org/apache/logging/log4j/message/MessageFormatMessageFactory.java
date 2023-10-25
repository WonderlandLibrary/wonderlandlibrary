/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.message;

import org.apache.logging.log4j.message.AbstractMessageFactory;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFormatMessage;

public class MessageFormatMessageFactory
extends AbstractMessageFactory {
    @Override
    public Message newMessage(String message, Object ... params) {
        return new MessageFormatMessage(message, params);
    }
}

