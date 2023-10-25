/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.pattern;

import java.util.List;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternFormatter;
import org.apache.logging.log4j.core.pattern.PatternParser;
import org.apache.logging.log4j.util.EnglishEnums;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.util.StringBuilders;

@Plugin(name="encode", category="Converter")
@ConverterKeys(value={"enc", "encode"})
@PerformanceSensitive(value={"allocation"})
public final class EncodingPatternConverter
extends LogEventPatternConverter {
    private final List<PatternFormatter> formatters;
    private final EscapeFormat escapeFormat;

    private EncodingPatternConverter(List<PatternFormatter> formatters, EscapeFormat escapeFormat) {
        super("encode", "encode");
        this.formatters = formatters;
        this.escapeFormat = escapeFormat;
    }

    public static EncodingPatternConverter newInstance(Configuration config, String[] options) {
        if (options.length > 2 || options.length == 0) {
            LOGGER.error("Incorrect number of options on escape. Expected 1 or 2, but received {}", options.length);
            return null;
        }
        if (options[0] == null) {
            LOGGER.error("No pattern supplied on escape");
            return null;
        }
        EscapeFormat escapeFormat = options.length < 2 ? EscapeFormat.HTML : EnglishEnums.valueOf(EscapeFormat.class, options[1], EscapeFormat.HTML);
        PatternParser parser = PatternLayout.createPatternParser(config);
        List<PatternFormatter> formatters = parser.parse(options[0]);
        return new EncodingPatternConverter(formatters, escapeFormat);
    }

    @Override
    public void format(LogEvent event, StringBuilder toAppendTo) {
        int start = toAppendTo.length();
        for (int i = 0; i < this.formatters.size(); ++i) {
            this.formatters.get(i).format(event, toAppendTo);
        }
        this.escapeFormat.escape(toAppendTo, start);
    }

    private static enum EscapeFormat {
        HTML{

            @Override
            void escape(StringBuilder toAppendTo, int start) {
                block10: for (int i = toAppendTo.length() - 1; i >= start; --i) {
                    char c = toAppendTo.charAt(i);
                    switch (c) {
                        case '\r': {
                            toAppendTo.setCharAt(i, '\\');
                            toAppendTo.insert(i + 1, 'r');
                            continue block10;
                        }
                        case '\n': {
                            toAppendTo.setCharAt(i, '\\');
                            toAppendTo.insert(i + 1, 'n');
                            continue block10;
                        }
                        case '&': {
                            toAppendTo.setCharAt(i, '&');
                            toAppendTo.insert(i + 1, "amp;");
                            continue block10;
                        }
                        case '<': {
                            toAppendTo.setCharAt(i, '&');
                            toAppendTo.insert(i + 1, "lt;");
                            continue block10;
                        }
                        case '>': {
                            toAppendTo.setCharAt(i, '&');
                            toAppendTo.insert(i + 1, "gt;");
                            continue block10;
                        }
                        case '\"': {
                            toAppendTo.setCharAt(i, '&');
                            toAppendTo.insert(i + 1, "quot;");
                            continue block10;
                        }
                        case '\'': {
                            toAppendTo.setCharAt(i, '&');
                            toAppendTo.insert(i + 1, "apos;");
                            continue block10;
                        }
                        case '/': {
                            toAppendTo.setCharAt(i, '&');
                            toAppendTo.insert(i + 1, "#x2F;");
                        }
                    }
                }
            }
        }
        ,
        JSON{

            @Override
            void escape(StringBuilder toAppendTo, int start) {
                StringBuilders.escapeJson(toAppendTo, start);
            }
        }
        ,
        CRLF{

            @Override
            void escape(StringBuilder toAppendTo, int start) {
                block4: for (int i = toAppendTo.length() - 1; i >= start; --i) {
                    char c = toAppendTo.charAt(i);
                    switch (c) {
                        case '\r': {
                            toAppendTo.setCharAt(i, '\\');
                            toAppendTo.insert(i + 1, 'r');
                            continue block4;
                        }
                        case '\n': {
                            toAppendTo.setCharAt(i, '\\');
                            toAppendTo.insert(i + 1, 'n');
                        }
                    }
                }
            }
        }
        ,
        XML{

            @Override
            void escape(StringBuilder toAppendTo, int start) {
                StringBuilders.escapeXml(toAppendTo, start);
            }
        };


        abstract void escape(StringBuilder var1, int var2);
    }
}

