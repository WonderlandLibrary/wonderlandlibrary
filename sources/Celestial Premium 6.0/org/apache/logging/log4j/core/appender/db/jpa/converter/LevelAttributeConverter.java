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
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.util.Strings;

@Converter(autoApply=false)
public class LevelAttributeConverter
implements AttributeConverter<Level, String> {
    public String convertToDatabaseColumn(Level level) {
        if (level == null) {
            return null;
        }
        return level.name();
    }

    public Level convertToEntityAttribute(String s) {
        if (Strings.isEmpty(s)) {
            return null;
        }
        return Level.toLevel(s, null);
    }
}

