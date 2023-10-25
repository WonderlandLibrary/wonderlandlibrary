/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonCreator
 *  com.fasterxml.jackson.annotation.JsonCreator$Mode
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonValue
 */
package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.logging.log4j.Level;

@JsonIgnoreProperties(value={"name", "declaringClass", "standardLevel"})
abstract class LevelMixIn {
    LevelMixIn() {
    }

    @JsonCreator(mode=JsonCreator.Mode.DELEGATING)
    public static Level getLevel(String name) {
        return null;
    }

    @JsonValue
    public abstract String name();
}

