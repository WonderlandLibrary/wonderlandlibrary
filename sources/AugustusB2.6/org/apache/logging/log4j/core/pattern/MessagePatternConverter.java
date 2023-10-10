// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.message.MultiformatMessage;
import org.apache.logging.log4j.util.MultiFormatStringBuilderFormattable;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.StringBuilderFormattable;
import org.apache.logging.log4j.core.LogEvent;
import java.util.List;
import java.util.ArrayList;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.util.Loader;
import java.util.Locale;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "MessagePatternConverter", category = "Converter")
@ConverterKeys({ "m", "msg", "message" })
@PerformanceSensitive({ "allocation" })
public class MessagePatternConverter extends LogEventPatternConverter
{
    private static final String LOOKUPS = "lookups";
    private static final String NOLOOKUPS = "nolookups";
    
    private MessagePatternConverter() {
        super("Message", "message");
    }
    
    private static boolean loadLookups(final String[] options) {
        if (options != null) {
            for (final String option : options) {
                if ("lookups".equalsIgnoreCase(option)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static TextRenderer loadMessageRenderer(final String[] options) {
        if (options != null) {
            final int length = options.length;
            int i = 0;
            while (i < length) {
                final String option = options[i];
                final String upperCase = option.toUpperCase(Locale.ROOT);
                switch (upperCase) {
                    case "ANSI": {
                        if (Loader.isJansiAvailable()) {
                            return new JAnsiTextRenderer(options, JAnsiTextRenderer.DefaultMessageStyleMap);
                        }
                        StatusLogger.getLogger().warn("You requested ANSI message rendering but JANSI is not on the classpath.");
                        return null;
                    }
                    case "HTML": {
                        return new HtmlTextRenderer(options);
                    }
                    default: {
                        ++i;
                        continue;
                    }
                }
            }
        }
        return null;
    }
    
    public static MessagePatternConverter newInstance(final Configuration config, final String[] options) {
        final boolean lookups = loadLookups(options);
        final String[] formats = withoutLookupOptions(options);
        final TextRenderer textRenderer = loadMessageRenderer(formats);
        MessagePatternConverter result = (formats == null || formats.length == 0) ? SimpleMessagePatternConverter.INSTANCE : new FormattedMessagePatternConverter(formats);
        if (lookups && config != null) {
            result = new LookupMessagePatternConverter(result, config);
        }
        if (textRenderer != null) {
            result = new RenderingPatternConverter(result, textRenderer);
        }
        return result;
    }
    
    private static String[] withoutLookupOptions(final String[] options) {
        if (options == null || options.length == 0) {
            return options;
        }
        final List<String> results = new ArrayList<String>(options.length);
        for (final String option : options) {
            if (!"lookups".equalsIgnoreCase(option) && !"nolookups".equalsIgnoreCase(option)) {
                results.add(option);
            }
        }
        return results.toArray(new String[0]);
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        throw new UnsupportedOperationException();
    }
    
    private static final class SimpleMessagePatternConverter extends MessagePatternConverter
    {
        private static final MessagePatternConverter INSTANCE;
        
        private SimpleMessagePatternConverter() {
            super(null);
        }
        
        @Override
        public void format(final LogEvent event, final StringBuilder toAppendTo) {
            final Message msg = event.getMessage();
            if (msg instanceof StringBuilderFormattable) {
                ((StringBuilderFormattable)msg).formatTo(toAppendTo);
            }
            else if (msg != null) {
                toAppendTo.append(msg.getFormattedMessage());
            }
        }
        
        static {
            INSTANCE = new SimpleMessagePatternConverter();
        }
    }
    
    private static final class FormattedMessagePatternConverter extends MessagePatternConverter
    {
        private final String[] formats;
        
        FormattedMessagePatternConverter(final String[] formats) {
            super(null);
            this.formats = formats;
        }
        
        @Override
        public void format(final LogEvent event, final StringBuilder toAppendTo) {
            final Message msg = event.getMessage();
            if (msg instanceof StringBuilderFormattable) {
                if (msg instanceof MultiFormatStringBuilderFormattable) {
                    ((MultiFormatStringBuilderFormattable)msg).formatTo(this.formats, toAppendTo);
                }
                else {
                    ((StringBuilderFormattable)msg).formatTo(toAppendTo);
                }
            }
            else if (msg != null) {
                toAppendTo.append((msg instanceof MultiformatMessage) ? ((MultiformatMessage)msg).getFormattedMessage(this.formats) : msg.getFormattedMessage());
            }
        }
    }
    
    private static final class LookupMessagePatternConverter extends MessagePatternConverter
    {
        private final MessagePatternConverter delegate;
        private final Configuration config;
        
        LookupMessagePatternConverter(final MessagePatternConverter delegate, final Configuration config) {
            super(null);
            this.delegate = delegate;
            this.config = config;
        }
        
        @Override
        public void format(final LogEvent event, final StringBuilder toAppendTo) {
            final int start = toAppendTo.length();
            this.delegate.format(event, toAppendTo);
            final int indexOfSubstitution = toAppendTo.indexOf("${", start);
            if (indexOfSubstitution >= 0) {
                this.config.getStrSubstitutor().replaceIn(event, toAppendTo, indexOfSubstitution, toAppendTo.length() - indexOfSubstitution);
            }
        }
    }
    
    private static final class RenderingPatternConverter extends MessagePatternConverter
    {
        private final MessagePatternConverter delegate;
        private final TextRenderer textRenderer;
        
        RenderingPatternConverter(final MessagePatternConverter delegate, final TextRenderer textRenderer) {
            super(null);
            this.delegate = delegate;
            this.textRenderer = textRenderer;
        }
        
        @Override
        public void format(final LogEvent event, final StringBuilder toAppendTo) {
            final StringBuilder workingBuilder = new StringBuilder(80);
            this.delegate.format(event, workingBuilder);
            this.textRenderer.render(workingBuilder, toAppendTo);
        }
    }
}
