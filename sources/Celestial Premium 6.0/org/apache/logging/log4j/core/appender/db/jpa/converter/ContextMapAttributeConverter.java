/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.persistence.AttributeConverter
 *  javax.persistence.Converter
 */
package org.apache.logging.log4j.core.appender.db.jpa.converter;

import java.util.Map;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply=false)
public class ContextMapAttributeConverter
implements AttributeConverter<Map<String, String>, String> {
    public String convertToDatabaseColumn(Map<String, String> contextMap) {
        if (contextMap == null) {
            return null;
        }
        return contextMap.toString();
    }

    public Map<String, String> convertToEntityAttribute(String s) {
        throw new UnsupportedOperationException("Log events can only be persisted, not extracted.");
    }
}

