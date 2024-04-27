/*
 * Decompiled with CFR 0.152.
 */
package org.apache.log4j.pattern;

import org.apache.log4j.pattern.LoggingEventPatternConverter;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;

public final class FullLocationPatternConverter
extends LoggingEventPatternConverter {
    private static final FullLocationPatternConverter INSTANCE = new FullLocationPatternConverter();

    private FullLocationPatternConverter() {
        super("Full Location", "fullLocation");
    }

    public static FullLocationPatternConverter newInstance(String[] options) {
        return INSTANCE;
    }

    public void format(LoggingEvent event, StringBuffer output) {
        LocationInfo locationInfo = event.getLocationInformation();
        if (locationInfo != null) {
            output.append(locationInfo.fullInfo);
        }
    }
}

