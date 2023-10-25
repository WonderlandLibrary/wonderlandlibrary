/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.pattern.PatternConverter;
import org.apache.logging.log4j.status.StatusLogger;

public abstract class AbstractPatternConverter
implements PatternConverter {
    protected static final Logger LOGGER = StatusLogger.getLogger();
    private final String name;
    private final String style;

    protected AbstractPatternConverter(String name, String style) {
        this.name = name;
        this.style = style;
    }

    @Override
    public final String getName() {
        return this.name;
    }

    @Override
    public String getStyleClass(Object e) {
        return this.style;
    }
}

