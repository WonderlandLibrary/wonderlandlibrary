/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.message;

import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;

public interface MessageFactory2
extends MessageFactory {
    public Message newMessage(CharSequence var1);

    public Message newMessage(String var1, Object var2);

    public Message newMessage(String var1, Object var2, Object var3);

    public Message newMessage(String var1, Object var2, Object var3, Object var4);

    public Message newMessage(String var1, Object var2, Object var3, Object var4, Object var5);

    public Message newMessage(String var1, Object var2, Object var3, Object var4, Object var5, Object var6);

    public Message newMessage(String var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7);

    public Message newMessage(String var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7, Object var8);

    public Message newMessage(String var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7, Object var8, Object var9);

    public Message newMessage(String var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7, Object var8, Object var9, Object var10);

    public Message newMessage(String var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7, Object var8, Object var9, Object var10, Object var11);
}

