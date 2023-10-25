/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.NameAbbreviator;

public abstract class NamePatternConverter
extends LogEventPatternConverter {
    private final NameAbbreviator abbreviator;

    protected NamePatternConverter(String name, String style, String[] options) {
        super(name, style);
        this.abbreviator = options != null && options.length > 0 ? NameAbbreviator.getAbbreviator(options[0]) : NameAbbreviator.getDefaultAbbreviator();
    }

    protected final String abbreviate(String buf) {
        return this.abbreviator.abbreviate(buf);
    }
}

