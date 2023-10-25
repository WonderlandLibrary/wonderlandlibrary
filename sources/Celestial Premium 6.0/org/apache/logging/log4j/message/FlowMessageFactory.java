/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.message;

import org.apache.logging.log4j.message.EntryMessage;
import org.apache.logging.log4j.message.ExitMessage;
import org.apache.logging.log4j.message.Message;

public interface FlowMessageFactory {
    public EntryMessage newEntryMessage(Message var1);

    public ExitMessage newExitMessage(Object var1, Message var2);

    public ExitMessage newExitMessage(EntryMessage var1);

    public ExitMessage newExitMessage(Object var1, EntryMessage var2);
}

