/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.JsonNode
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.fasterxml.jackson.databind.node.JsonNodeFactory
 *  com.fasterxml.jackson.databind.node.ObjectNode
 *  javax.persistence.AttributeConverter
 *  javax.persistence.Converter
 *  javax.persistence.PersistenceException
 */
package org.apache.logging.log4j.core.appender.db.jpa.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import javax.persistence.PersistenceException;
import org.apache.logging.log4j.core.impl.ContextDataFactory;
import org.apache.logging.log4j.util.BiConsumer;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.apache.logging.log4j.util.StringMap;
import org.apache.logging.log4j.util.Strings;

@Converter(autoApply=false)
public class ContextDataJsonAttributeConverter
implements AttributeConverter<ReadOnlyStringMap, String> {
    static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public String convertToDatabaseColumn(ReadOnlyStringMap contextData) {
        if (contextData == null) {
            return null;
        }
        try {
            JsonNodeFactory factory = OBJECT_MAPPER.getNodeFactory();
            final ObjectNode root = factory.objectNode();
            contextData.forEach(new BiConsumer<String, Object>(){

                @Override
                public void accept(String key, Object value) {
                    root.put(key, String.valueOf(value));
                }
            });
            return OBJECT_MAPPER.writeValueAsString((Object)root);
        }
        catch (Exception e) {
            throw new PersistenceException("Failed to convert contextData to JSON string.", (Throwable)e);
        }
    }

    public ReadOnlyStringMap convertToEntityAttribute(String s) {
        if (Strings.isEmpty(s)) {
            return null;
        }
        try {
            StringMap result = ContextDataFactory.createContextData();
            ObjectNode root = (ObjectNode)OBJECT_MAPPER.readTree(s);
            Iterator entries = root.fields();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry)entries.next();
                String value = ((JsonNode)entry.getValue()).textValue();
                result.putValue((String)entry.getKey(), value);
            }
            return result;
        }
        catch (IOException e) {
            throw new PersistenceException("Failed to convert JSON string to map.", (Throwable)e);
        }
    }
}

