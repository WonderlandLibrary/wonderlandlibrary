/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.type.TypeReference
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  javax.persistence.AttributeConverter
 *  javax.persistence.Converter
 *  javax.persistence.PersistenceException
 */
package org.apache.logging.log4j.core.appender.db.jpa.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import javax.persistence.PersistenceException;
import org.apache.logging.log4j.core.helpers.Strings;

@Converter(autoApply=false)
public class ContextMapJsonAttributeConverter
implements AttributeConverter<Map<String, String>, String> {
    static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public String convertToDatabaseColumn(Map<String, String> contextMap) {
        if (contextMap == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(contextMap);
        }
        catch (IOException e) {
            throw new PersistenceException("Failed to convert map to JSON string.", (Throwable)e);
        }
    }

    public Map<String, String> convertToEntityAttribute(String s) {
        if (Strings.isEmpty(s)) {
            return null;
        }
        try {
            return (Map)OBJECT_MAPPER.readValue(s, (TypeReference)new TypeReference<Map<String, String>>(){});
        }
        catch (IOException e) {
            throw new PersistenceException("Failed to convert JSON string to map.", (Throwable)e);
        }
    }
}

