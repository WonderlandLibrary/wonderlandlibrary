// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.logging.log4j.Level;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "name", "declaringClass", "standardLevel" })
abstract class LevelMixIn
{
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Level getLevel(final String name) {
        return null;
    }
    
    @JsonValue
    public abstract String name();
}
