/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.util.PerformanceSensitive;

@Plugin(name="MarkerNamePatternConverter", category="Converter")
@ConverterKeys(value={"markerSimpleName"})
@PerformanceSensitive(value={"allocation"})
public final class MarkerSimpleNamePatternConverter
extends LogEventPatternConverter {
    private MarkerSimpleNamePatternConverter(String[] options) {
        super("MarkerSimpleName", "markerSimpleName");
    }

    public static MarkerSimpleNamePatternConverter newInstance(String[] options) {
        return new MarkerSimpleNamePatternConverter(options);
    }

    @Override
    public void format(LogEvent event, StringBuilder toAppendTo) {
        Marker marker = event.getMarker();
        if (marker != null) {
            toAppendTo.append(marker.getName());
        }
    }
}

