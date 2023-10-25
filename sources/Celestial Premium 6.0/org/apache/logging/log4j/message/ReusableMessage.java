/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.message;

import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.util.StringBuilderFormattable;

@PerformanceSensitive(value={"allocation"})
public interface ReusableMessage
extends Message,
StringBuilderFormattable {
    public Object[] swapParameters(Object[] var1);

    public short getParameterCount();

    public Message memento();
}

