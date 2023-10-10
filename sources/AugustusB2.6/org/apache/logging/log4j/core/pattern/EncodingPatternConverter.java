// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.util.StringBuilders;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.util.EnglishEnums;
import org.apache.logging.log4j.core.config.Configuration;
import java.util.function.Function;
import java.util.List;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "encode", category = "Converter")
@ConverterKeys({ "enc", "encode" })
@PerformanceSensitive({ "allocation" })
public final class EncodingPatternConverter extends LogEventPatternConverter
{
    private final List<PatternFormatter> formatters;
    private final EscapeFormat escapeFormat;
    
    private EncodingPatternConverter(final List<PatternFormatter> formatters, final EscapeFormat escapeFormat) {
        super("encode", "encode");
        this.formatters = formatters;
        this.escapeFormat = escapeFormat;
    }
    
    @Override
    public boolean handlesThrowable() {
        return this.formatters != null && this.formatters.stream().map((Function<? super Object, ?>)PatternFormatter::getConverter).anyMatch(LogEventPatternConverter::handlesThrowable);
    }
    
    public static EncodingPatternConverter newInstance(final Configuration config, final String[] options) {
        if (options.length > 2 || options.length == 0) {
            EncodingPatternConverter.LOGGER.error("Incorrect number of options on escape. Expected 1 or 2, but received {}", (Object)options.length);
            return null;
        }
        if (options[0] == null) {
            EncodingPatternConverter.LOGGER.error("No pattern supplied on escape");
            return null;
        }
        final EscapeFormat escapeFormat = (options.length < 2) ? EscapeFormat.HTML : EnglishEnums.valueOf(EscapeFormat.class, options[1], EscapeFormat.HTML);
        final PatternParser parser = PatternLayout.createPatternParser(config);
        final List<PatternFormatter> formatters = parser.parse(options[0]);
        return new EncodingPatternConverter(formatters, escapeFormat);
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        final int start = toAppendTo.length();
        for (int i = 0; i < this.formatters.size(); ++i) {
            this.formatters.get(i).format(event, toAppendTo);
        }
        this.escapeFormat.escape(toAppendTo, start);
    }
    
    private enum EscapeFormat
    {
        HTML {
            @Override
            void escape(final StringBuilder toAppendTo, final int start) {
                int firstSpecialChar;
                final int origLength = firstSpecialChar = toAppendTo.length();
                for (int i = origLength - 1; i >= start; --i) {
                    final char c = toAppendTo.charAt(i);
                    final String escaped = this.escapeChar(c);
                    if (escaped != null) {
                        firstSpecialChar = i;
                        for (int j = 0; j < escaped.length() - 1; ++j) {
                            toAppendTo.append(' ');
                        }
                    }
                }
                int i = origLength - 1;
                int k = toAppendTo.length();
                while (i >= firstSpecialChar) {
                    final char c2 = toAppendTo.charAt(i);
                    final String escaped2 = this.escapeChar(c2);
                    if (escaped2 == null) {
                        toAppendTo.setCharAt(--k, c2);
                    }
                    else {
                        toAppendTo.replace(k - escaped2.length(), k, escaped2);
                        k -= escaped2.length();
                    }
                    --i;
                }
            }
            
            private String escapeChar(final char c) {
                switch (c) {
                    case '\r': {
                        return "\\r";
                    }
                    case '\n': {
                        return "\\n";
                    }
                    case '&': {
                        return "&amp;";
                    }
                    case '<': {
                        return "&lt;";
                    }
                    case '>': {
                        return "&gt;";
                    }
                    case '\"': {
                        return "&quot;";
                    }
                    case '\'': {
                        return "&apos;";
                    }
                    case '/': {
                        return "&#x2F;";
                    }
                    default: {
                        return null;
                    }
                }
            }
        }, 
        JSON {
            @Override
            void escape(final StringBuilder toAppendTo, final int start) {
                StringBuilders.escapeJson(toAppendTo, start);
            }
        }, 
        CRLF {
            @Override
            void escape(final StringBuilder toAppendTo, final int start) {
                int firstSpecialChar;
                final int origLength = firstSpecialChar = toAppendTo.length();
                for (int i = origLength - 1; i >= start; --i) {
                    final char c = toAppendTo.charAt(i);
                    if (c == '\r' || c == '\n') {
                        firstSpecialChar = i;
                        toAppendTo.append(' ');
                    }
                }
                int i = origLength - 1;
                int j = toAppendTo.length();
                while (i >= firstSpecialChar) {
                    final char c2 = toAppendTo.charAt(i);
                    switch (c2) {
                        case '\r': {
                            toAppendTo.setCharAt(--j, 'r');
                            toAppendTo.setCharAt(--j, '\\');
                            break;
                        }
                        case '\n': {
                            toAppendTo.setCharAt(--j, 'n');
                            toAppendTo.setCharAt(--j, '\\');
                            break;
                        }
                        default: {
                            toAppendTo.setCharAt(--j, c2);
                            break;
                        }
                    }
                    --i;
                }
            }
        }, 
        XML {
            @Override
            void escape(final StringBuilder toAppendTo, final int start) {
                StringBuilders.escapeXml(toAppendTo, start);
            }
        };
        
        abstract void escape(final StringBuilder toAppendTo, final int start);
    }
}
