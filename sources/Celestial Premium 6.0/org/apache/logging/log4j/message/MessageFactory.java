/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.message;

import org.apache.logging.log4j.message.Message;

public interface MessageFactory {
    public Message newMessage(Object var1);

    public Message newMessage(String var1);

    public Message newMessage(String var1, Object ... var2);
}

