/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.message;

import org.apache.logging.log4j.message.Message;

public interface MultiformatMessage
extends Message {
    public String getFormattedMessage(String[] var1);

    public String[] getFormats();
}

