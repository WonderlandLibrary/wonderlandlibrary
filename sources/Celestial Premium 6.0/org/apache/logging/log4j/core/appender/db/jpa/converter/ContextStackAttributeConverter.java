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
import org.apache.logging.log4j.ThreadContext;

@Converter(autoApply=false)
public class ContextStackAttributeConverter
implements AttributeConverter<ThreadContext.ContextStack, String> {
    public String convertToDatabaseColumn(ThreadContext.ContextStack contextStack) {
        if (contextStack == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (String value : contextStack.asList()) {
            if (builder.length() > 0) {
                builder.append('\n');
            }
            builder.append(value);
        }
        return builder.toString();
    }

    public ThreadContext.ContextStack convertToEntityAttribute(String s) {
        throw new UnsupportedOperationException("Log events can only be persisted, not extracted.");
    }
}

