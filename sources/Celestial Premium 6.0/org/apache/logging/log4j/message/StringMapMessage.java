/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.message;

import java.util.Map;
import org.apache.logging.log4j.message.AsynchronouslyFormattable;
import org.apache.logging.log4j.message.MapMessage;
import org.apache.logging.log4j.util.PerformanceSensitive;

@AsynchronouslyFormattable
@PerformanceSensitive(value={"allocation"})
public class StringMapMessage
extends MapMessage<StringMapMessage, String> {
    private static final long serialVersionUID = 1L;

    public StringMapMessage() {
    }

    public StringMapMessage(int initialCapacity) {
        super(initialCapacity);
    }

    public StringMapMessage(Map<String, String> map) {
        super(map);
    }

    @Override
    public StringMapMessage newInstance(Map<String, String> map) {
        return new StringMapMessage(map);
    }
}

