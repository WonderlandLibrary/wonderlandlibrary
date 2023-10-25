/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.persistence.AttributeConverter
 *  javax.persistence.Converter
 */
package org.apache.logging.log4j.core.appender.db.jpa.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.status.StatusLogger;

@Converter(autoApply=false)
public class MessageAttributeConverter
implements AttributeConverter<Message, String> {
    private static final StatusLogger LOGGER = StatusLogger.getLogger();

    public String convertToDatabaseColumn(Message message) {
        if (message == null) {
            return null;
        }
        return message.getFormattedMessage();
    }

    public Message convertToEntityAttribute(String s) {
        if (Strings.isEmpty(s)) {
            return null;
        }
        return LOGGER.getMessageFactory().newMessage(s);
    }
}

