// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.util.StringBuilderWriter;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.logging.log4j.core.LogEvent;
import java.util.Iterator;
import org.apache.logging.log4j.util.Strings;
import java.util.Collections;
import java.util.ArrayList;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.impl.ThrowableFormatOptions;
import java.util.List;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "ThrowablePatternConverter", category = "Converter")
@ConverterKeys({ "ex", "throwable", "exception" })
public class ThrowablePatternConverter extends LogEventPatternConverter
{
    protected final List<PatternFormatter> formatters;
    private String rawOption;
    private final boolean subShortOption;
    private final boolean nonStandardLineSeparator;
    protected final ThrowableFormatOptions options;
    
    @Deprecated
    protected ThrowablePatternConverter(final String name, final String style, final String[] options) {
        this(name, style, options, null);
    }
    
    protected ThrowablePatternConverter(final String name, final String style, final String[] options, final Configuration config) {
        super(name, style);
        this.options = ThrowableFormatOptions.newInstance(options);
        if (options != null && options.length > 0) {
            this.rawOption = options[0];
        }
        if (this.options.getSuffix() != null) {
            final PatternParser parser = PatternLayout.createPatternParser(config);
            final List<PatternFormatter> parsedSuffixFormatters = parser.parse(this.options.getSuffix());
            boolean hasThrowableSuffixFormatter = false;
            for (final PatternFormatter suffixFormatter : parsedSuffixFormatters) {
                if (suffixFormatter.handlesThrowable()) {
                    hasThrowableSuffixFormatter = true;
                }
            }
            if (!hasThrowableSuffixFormatter) {
                this.formatters = parsedSuffixFormatters;
            }
            else {
                final List<PatternFormatter> suffixFormatters = new ArrayList<PatternFormatter>();
                for (final PatternFormatter suffixFormatter2 : parsedSuffixFormatters) {
                    if (!suffixFormatter2.handlesThrowable()) {
                        suffixFormatters.add(suffixFormatter2);
                    }
                }
                this.formatters = suffixFormatters;
            }
        }
        else {
            this.formatters = Collections.emptyList();
        }
        this.subShortOption = ("short.message".equalsIgnoreCase(this.rawOption) || "short.localizedMessage".equalsIgnoreCase(this.rawOption) || "short.fileName".equalsIgnoreCase(this.rawOption) || "short.lineNumber".equalsIgnoreCase(this.rawOption) || "short.methodName".equalsIgnoreCase(this.rawOption) || "short.className".equalsIgnoreCase(this.rawOption));
        this.nonStandardLineSeparator = !Strings.LINE_SEPARATOR.equals(this.options.getSeparator());
    }
    
    public static ThrowablePatternConverter newInstance(final Configuration config, final String[] options) {
        return new ThrowablePatternConverter("Throwable", "throwable", options, config);
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder buffer) {
        final Throwable t = event.getThrown();
        if (this.subShortOption) {
            this.formatSubShortOption(t, this.getSuffix(event), buffer);
        }
        else if (t != null && this.options.anyLines()) {
            this.formatOption(t, this.getSuffix(event), buffer);
        }
    }
    
    private void formatSubShortOption(final Throwable t, final String suffix, final StringBuilder buffer) {
        StackTraceElement throwingMethod = null;
        if (t != null) {
            final StackTraceElement[] trace = t.getStackTrace();
            if (trace != null && trace.length > 0) {
                throwingMethod = trace[0];
            }
        }
        if (t != null && throwingMethod != null) {
            String toAppend = "";
            if ("short.className".equalsIgnoreCase(this.rawOption)) {
                toAppend = throwingMethod.getClassName();
            }
            else if ("short.methodName".equalsIgnoreCase(this.rawOption)) {
                toAppend = throwingMethod.getMethodName();
            }
            else if ("short.lineNumber".equalsIgnoreCase(this.rawOption)) {
                toAppend = String.valueOf(throwingMethod.getLineNumber());
            }
            else if ("short.message".equalsIgnoreCase(this.rawOption)) {
                toAppend = t.getMessage();
            }
            else if ("short.localizedMessage".equalsIgnoreCase(this.rawOption)) {
                toAppend = t.getLocalizedMessage();
            }
            else if ("short.fileName".equalsIgnoreCase(this.rawOption)) {
                toAppend = throwingMethod.getFileName();
            }
            final int len = buffer.length();
            if (len > 0 && !Character.isWhitespace(buffer.charAt(len - 1))) {
                buffer.append(' ');
            }
            buffer.append(toAppend);
            if (Strings.isNotBlank(suffix)) {
                buffer.append(' ');
                buffer.append(suffix);
            }
        }
    }
    
    private void formatOption(final Throwable throwable, final String suffix, final StringBuilder buffer) {
        final int len = buffer.length();
        if (len > 0 && !Character.isWhitespace(buffer.charAt(len - 1))) {
            buffer.append(' ');
        }
        if (!this.options.allLines() || this.nonStandardLineSeparator || Strings.isNotBlank(suffix)) {
            final StringWriter w = new StringWriter();
            throwable.printStackTrace(new PrintWriter(w));
            final String[] array = w.toString().split(Strings.LINE_SEPARATOR);
            final int limit = this.options.minLines(array.length) - 1;
            final boolean suffixNotBlank = Strings.isNotBlank(suffix);
            for (int i = 0; i <= limit; ++i) {
                buffer.append(array[i]);
                if (suffixNotBlank) {
                    buffer.append(' ');
                    buffer.append(suffix);
                }
                if (i < limit) {
                    buffer.append(this.options.getSeparator());
                }
            }
        }
        else {
            throwable.printStackTrace(new PrintWriter(new StringBuilderWriter(buffer)));
        }
    }
    
    @Override
    public boolean handlesThrowable() {
        return true;
    }
    
    protected String getSuffix(final LogEvent event) {
        if (this.formatters.isEmpty()) {
            return "";
        }
        final StringBuilder toAppendTo = new StringBuilder();
        for (int i = 0, size = this.formatters.size(); i < size; ++i) {
            this.formatters.get(i).format(event, toAppendTo);
        }
        return toAppendTo.toString();
    }
    
    public ThrowableFormatOptions getOptions() {
        return this.options;
    }
}
