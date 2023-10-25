/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.message;

import org.apache.logging.log4j.message.Message;

public interface FlowMessage
extends Message {
    public String getText();

    public Message getMessage();
}

