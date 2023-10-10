// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.async;

import java.lang.annotation.Annotation;
import org.apache.logging.log4j.message.AsynchronouslyFormattable;
import org.apache.logging.log4j.core.util.Constants;
import org.apache.logging.log4j.message.Message;

public class InternalAsyncUtil
{
    public static Message makeMessageImmutable(final Message msg) {
        if (msg != null && !canFormatMessageInBackground(msg)) {
            msg.getFormattedMessage();
        }
        return msg;
    }
    
    private static boolean canFormatMessageInBackground(final Message message) {
        return Constants.FORMAT_MESSAGES_IN_BACKGROUND || message.getClass().isAnnotationPresent(AsynchronouslyFormattable.class);
    }
}
