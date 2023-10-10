// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.layout;

import java.util.List;
import org.apache.logging.log4j.core.layout.internal.IncludeChecker;
import org.apache.logging.log4j.core.layout.internal.ExcludeChecker;
import java.util.ArrayList;
import org.apache.logging.log4j.core.util.Patterns;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.util.Builder;
import java.util.zip.GZIPOutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.logging.log4j.core.net.Severity;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.util.StringBuilderFormattable;
import org.apache.logging.log4j.message.MapMessage;
import org.apache.logging.log4j.util.TriConsumer;
import org.apache.logging.log4j.util.Strings;
import org.apache.logging.log4j.core.util.JsonUtils;
import java.util.zip.DeflaterOutputStream;
import java.io.IOException;
import org.apache.logging.log4j.status.StatusLogger;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.util.Objects;
import org.apache.logging.log4j.core.LogEvent;
import java.util.Collections;
import java.util.Map;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.util.NetUtils;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.core.layout.internal.ListChecker;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.util.KeyValuePair;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "GelfLayout", category = "Core", elementType = "layout", printObject = true)
public final class GelfLayout extends AbstractStringLayout
{
    private static final char C = ',';
    private static final int COMPRESSION_THRESHOLD = 1024;
    private static final char Q = '\"';
    private static final String QC = "\",";
    private static final String QU = "\"_";
    private final KeyValuePair[] additionalFields;
    private final int compressionThreshold;
    private final CompressionType compressionType;
    private final String host;
    private final boolean includeStacktrace;
    private final boolean includeThreadContext;
    private final boolean includeMapMessage;
    private final boolean includeNullDelimiter;
    private final boolean includeNewLineDelimiter;
    private final boolean omitEmptyFields;
    private final PatternLayout layout;
    private final FieldWriter mdcWriter;
    private final FieldWriter mapWriter;
    private static final ThreadLocal<StringBuilder> messageStringBuilder;
    private static final ThreadLocal<StringBuilder> timestampStringBuilder;
    
    @Deprecated
    public GelfLayout(final String host, final KeyValuePair[] additionalFields, final CompressionType compressionType, final int compressionThreshold, final boolean includeStacktrace) {
        this(null, host, additionalFields, compressionType, compressionThreshold, includeStacktrace, true, true, false, false, false, null, null, null, "", "");
    }
    
    private GelfLayout(final Configuration config, final String host, final KeyValuePair[] additionalFields, final CompressionType compressionType, final int compressionThreshold, final boolean includeStacktrace, final boolean includeThreadContext, final boolean includeMapMessage, final boolean includeNullDelimiter, final boolean includeNewLineDelimiter, final boolean omitEmptyFields, final ListChecker mdcChecker, final ListChecker mapChecker, final PatternLayout patternLayout, final String mdcPrefix, final String mapPrefix) {
        super(config, StandardCharsets.UTF_8, null, null);
        this.host = ((host != null) ? host : NetUtils.getLocalHostname());
        this.additionalFields = ((additionalFields != null) ? additionalFields : KeyValuePair.EMPTY_ARRAY);
        if (config == null) {
            for (final KeyValuePair additionalField : this.additionalFields) {
                if (valueNeedsLookup(additionalField.getValue())) {
                    throw new IllegalArgumentException("configuration needs to be set when there are additional fields with variables");
                }
            }
        }
        this.compressionType = compressionType;
        this.compressionThreshold = compressionThreshold;
        this.includeStacktrace = includeStacktrace;
        this.includeThreadContext = includeThreadContext;
        this.includeMapMessage = includeMapMessage;
        this.includeNullDelimiter = includeNullDelimiter;
        this.includeNewLineDelimiter = includeNewLineDelimiter;
        this.omitEmptyFields = omitEmptyFields;
        if (includeNullDelimiter && compressionType != CompressionType.OFF) {
            throw new IllegalArgumentException("null delimiter cannot be used with compression");
        }
        this.mdcWriter = new FieldWriter(mdcChecker, mdcPrefix);
        this.mapWriter = new FieldWriter(mapChecker, mapPrefix);
        this.layout = patternLayout;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("host=").append(this.host);
        sb.append(", compressionType=").append(this.compressionType.toString());
        sb.append(", compressionThreshold=").append(this.compressionThreshold);
        sb.append(", includeStackTrace=").append(this.includeStacktrace);
        sb.append(", includeThreadContext=").append(this.includeThreadContext);
        sb.append(", includeNullDelimiter=").append(this.includeNullDelimiter);
        sb.append(", includeNewLineDelimiter=").append(this.includeNewLineDelimiter);
        final String threadVars = this.mdcWriter.getChecker().toString();
        if (threadVars.length() > 0) {
            sb.append(", ").append(threadVars);
        }
        final String mapVars = this.mapWriter.getChecker().toString();
        if (mapVars.length() > 0) {
            sb.append(", ").append(mapVars);
        }
        if (this.layout != null) {
            sb.append(", PatternLayout{").append(this.layout.toString()).append("}");
        }
        return sb.toString();
    }
    
    @Deprecated
    public static GelfLayout createLayout(@PluginAttribute("host") final String host, @PluginElement("AdditionalField") final KeyValuePair[] additionalFields, @PluginAttribute(value = "compressionType", defaultString = "GZIP") final CompressionType compressionType, @PluginAttribute(value = "compressionThreshold", defaultInt = 1024) final int compressionThreshold, @PluginAttribute(value = "includeStacktrace", defaultBoolean = true) final boolean includeStacktrace) {
        return new GelfLayout(null, host, additionalFields, compressionType, compressionThreshold, includeStacktrace, true, true, false, false, false, null, null, null, "", "");
    }
    
    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return new Builder<B>().asBuilder();
    }
    
    @Override
    public Map<String, String> getContentFormat() {
        return Collections.emptyMap();
    }
    
    @Override
    public String getContentType() {
        return "application/json; charset=" + this.getCharset();
    }
    
    @Override
    public byte[] toByteArray(final LogEvent event) {
        final StringBuilder text = this.toText(event, AbstractStringLayout.getStringBuilder(), false);
        final byte[] bytes = this.getBytes(text.toString());
        return (this.compressionType != CompressionType.OFF && bytes.length > this.compressionThreshold) ? this.compress(bytes) : bytes;
    }
    
    @Override
    public void encode(final LogEvent event, final ByteBufferDestination destination) {
        if (this.compressionType != CompressionType.OFF) {
            super.encode(event, destination);
            return;
        }
        final StringBuilder text = this.toText(event, AbstractStringLayout.getStringBuilder(), true);
        final Encoder<StringBuilder> helper = this.getStringBuilderEncoder();
        helper.encode(text, destination);
    }
    
    @Override
    public boolean requiresLocation() {
        return Objects.nonNull(this.layout) && this.layout.requiresLocation();
    }
    
    private byte[] compress(final byte[] bytes) {
        try {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream(this.compressionThreshold / 8);
            try (final DeflaterOutputStream stream = this.compressionType.createDeflaterOutputStream(baos)) {
                if (stream == null) {
                    return bytes;
                }
                stream.write(bytes);
                stream.finish();
            }
            return baos.toByteArray();
        }
        catch (IOException e) {
            StatusLogger.getLogger().error(e);
            return bytes;
        }
    }
    
    @Override
    public String toSerializable(final LogEvent event) {
        final StringBuilder text = this.toText(event, AbstractStringLayout.getStringBuilder(), false);
        return text.toString();
    }
    
    private StringBuilder toText(final LogEvent event, final StringBuilder builder, final boolean gcFree) {
        builder.append('{');
        builder.append("\"version\":\"1.1\",");
        builder.append("\"host\":\"");
        JsonUtils.quoteAsString(toNullSafeString(this.host), builder);
        builder.append("\",");
        builder.append("\"timestamp\":").append(formatTimestamp(event.getTimeMillis())).append(',');
        builder.append("\"level\":").append(this.formatLevel(event.getLevel())).append(',');
        if (event.getThreadName() != null) {
            builder.append("\"_thread\":\"");
            JsonUtils.quoteAsString(event.getThreadName(), builder);
            builder.append("\",");
        }
        if (event.getLoggerName() != null) {
            builder.append("\"_logger\":\"");
            JsonUtils.quoteAsString(event.getLoggerName(), builder);
            builder.append("\",");
        }
        if (this.additionalFields.length > 0) {
            final StrSubstitutor strSubstitutor = this.getConfiguration().getStrSubstitutor();
            for (final KeyValuePair additionalField : this.additionalFields) {
                final String value2 = valueNeedsLookup(additionalField.getValue()) ? strSubstitutor.replace(event, additionalField.getValue()) : additionalField.getValue();
                if (Strings.isNotEmpty(value2) || !this.omitEmptyFields) {
                    builder.append("\"_");
                    JsonUtils.quoteAsString(additionalField.getKey(), builder);
                    builder.append("\":\"");
                    JsonUtils.quoteAsString(toNullSafeString(value2), builder);
                    builder.append("\",");
                }
            }
        }
        if (this.includeThreadContext) {
            event.getContextData().forEach(this.mdcWriter, builder);
        }
        if (this.includeMapMessage && event.getMessage() instanceof MapMessage) {
            ((MapMessage)event.getMessage()).forEach((key, value) -> this.mapWriter.accept(key, value, builder));
        }
        if (event.getThrown() != null || this.layout != null) {
            builder.append("\"full_message\":\"");
            if (this.layout != null) {
                final StringBuilder messageBuffer = getMessageStringBuilder();
                this.layout.serialize(event, messageBuffer);
                JsonUtils.quoteAsString(messageBuffer, builder);
            }
            else if (this.includeStacktrace) {
                JsonUtils.quoteAsString(formatThrowable(event.getThrown()), builder);
            }
            else {
                JsonUtils.quoteAsString(event.getThrown().toString(), builder);
            }
            builder.append("\",");
        }
        builder.append("\"short_message\":\"");
        final Message message = event.getMessage();
        if (message instanceof CharSequence) {
            JsonUtils.quoteAsString((CharSequence)message, builder);
        }
        else if (gcFree && message instanceof StringBuilderFormattable) {
            final StringBuilder messageBuffer2 = getMessageStringBuilder();
            try {
                ((StringBuilderFormattable)message).formatTo(messageBuffer2);
                JsonUtils.quoteAsString(messageBuffer2, builder);
            }
            finally {
                AbstractStringLayout.trimToMaxSize(messageBuffer2);
            }
        }
        else {
            JsonUtils.quoteAsString(toNullSafeString(message.getFormattedMessage()), builder);
        }
        builder.append('\"');
        builder.append('}');
        if (this.includeNullDelimiter) {
            builder.append('\0');
        }
        if (this.includeNewLineDelimiter) {
            builder.append('\n');
        }
        return builder;
    }
    
    private static boolean valueNeedsLookup(final String value) {
        return value != null && value.contains("${");
    }
    
    private static StringBuilder getMessageStringBuilder() {
        StringBuilder result = GelfLayout.messageStringBuilder.get();
        if (result == null) {
            result = new StringBuilder(1024);
            GelfLayout.messageStringBuilder.set(result);
        }
        result.setLength(0);
        return result;
    }
    
    private static CharSequence toNullSafeString(final CharSequence s) {
        return (s == null) ? "" : s;
    }
    
    static CharSequence formatTimestamp(final long timeMillis) {
        if (timeMillis < 1000L) {
            return "0";
        }
        final StringBuilder builder = getTimestampStringBuilder();
        builder.append(timeMillis);
        builder.insert(builder.length() - 3, '.');
        return builder;
    }
    
    private static StringBuilder getTimestampStringBuilder() {
        StringBuilder result = GelfLayout.timestampStringBuilder.get();
        if (result == null) {
            result = new StringBuilder(20);
            GelfLayout.timestampStringBuilder.set(result);
        }
        result.setLength(0);
        return result;
    }
    
    private int formatLevel(final Level level) {
        return Severity.getSeverity(level).getCode();
    }
    
    static CharSequence formatThrowable(final Throwable throwable) {
        final StringWriter sw = new StringWriter(2048);
        final PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        pw.flush();
        return sw.getBuffer();
    }
    
    static {
        messageStringBuilder = new ThreadLocal<StringBuilder>();
        timestampStringBuilder = new ThreadLocal<StringBuilder>();
    }
    
    public enum CompressionType
    {
        GZIP {
            @Override
            public DeflaterOutputStream createDeflaterOutputStream(final OutputStream os) throws IOException {
                return new GZIPOutputStream(os);
            }
        }, 
        ZLIB {
            @Override
            public DeflaterOutputStream createDeflaterOutputStream(final OutputStream os) throws IOException {
                return new DeflaterOutputStream(os);
            }
        }, 
        OFF {
            @Override
            public DeflaterOutputStream createDeflaterOutputStream(final OutputStream os) throws IOException {
                return null;
            }
        };
        
        public abstract DeflaterOutputStream createDeflaterOutputStream(final OutputStream os) throws IOException;
    }
    
    public static class Builder<B extends Builder<B>> extends AbstractStringLayout.Builder<B> implements org.apache.logging.log4j.core.util.Builder<GelfLayout>
    {
        @PluginBuilderAttribute
        private String host;
        @PluginElement("AdditionalField")
        private KeyValuePair[] additionalFields;
        @PluginBuilderAttribute
        private CompressionType compressionType;
        @PluginBuilderAttribute
        private int compressionThreshold;
        @PluginBuilderAttribute
        private boolean includeStacktrace;
        @PluginBuilderAttribute
        private boolean includeThreadContext;
        @PluginBuilderAttribute
        private boolean includeNullDelimiter;
        @PluginBuilderAttribute
        private boolean includeNewLineDelimiter;
        @PluginBuilderAttribute
        private String threadContextIncludes;
        @PluginBuilderAttribute
        private String threadContextExcludes;
        @PluginBuilderAttribute
        private String mapMessageIncludes;
        @PluginBuilderAttribute
        private String mapMessageExcludes;
        @PluginBuilderAttribute
        private boolean includeMapMessage;
        @PluginBuilderAttribute
        private boolean omitEmptyFields;
        @PluginBuilderAttribute
        private String messagePattern;
        @PluginBuilderAttribute
        private String threadContextPrefix;
        @PluginBuilderAttribute
        private String mapPrefix;
        @PluginElement("PatternSelector")
        private PatternSelector patternSelector;
        
        public Builder() {
            this.compressionType = CompressionType.GZIP;
            this.compressionThreshold = 1024;
            this.includeStacktrace = true;
            this.includeThreadContext = true;
            this.includeNullDelimiter = false;
            this.includeNewLineDelimiter = false;
            this.threadContextIncludes = null;
            this.threadContextExcludes = null;
            this.mapMessageIncludes = null;
            this.mapMessageExcludes = null;
            this.includeMapMessage = true;
            this.omitEmptyFields = false;
            this.messagePattern = null;
            this.threadContextPrefix = "";
            this.mapPrefix = "";
            this.patternSelector = null;
            this.setCharset(StandardCharsets.UTF_8);
        }
        
        @Override
        public GelfLayout build() {
            final ListChecker mdcChecker = this.createChecker(this.threadContextExcludes, this.threadContextIncludes);
            final ListChecker mapChecker = this.createChecker(this.mapMessageExcludes, this.mapMessageIncludes);
            PatternLayout patternLayout = null;
            if (this.messagePattern != null && this.patternSelector != null) {
                AbstractLayout.LOGGER.error("A message pattern and PatternSelector cannot both be specified on GelfLayout, ignoring message pattern");
                this.messagePattern = null;
            }
            if (this.messagePattern != null) {
                patternLayout = PatternLayout.newBuilder().withPattern(this.messagePattern).withAlwaysWriteExceptions(this.includeStacktrace).withConfiguration(this.getConfiguration()).build();
            }
            if (this.patternSelector != null) {
                patternLayout = PatternLayout.newBuilder().withPatternSelector(this.patternSelector).withAlwaysWriteExceptions(this.includeStacktrace).withConfiguration(this.getConfiguration()).build();
            }
            return new GelfLayout(this.getConfiguration(), this.host, this.additionalFields, this.compressionType, this.compressionThreshold, this.includeStacktrace, this.includeThreadContext, this.includeMapMessage, this.includeNullDelimiter, this.includeNewLineDelimiter, this.omitEmptyFields, mdcChecker, mapChecker, patternLayout, this.threadContextPrefix, this.mapPrefix, null);
        }
        
        private ListChecker createChecker(final String excludes, final String includes) {
            ListChecker checker = null;
            if (excludes != null) {
                final String[] array = excludes.split(Patterns.COMMA_SEPARATOR);
                if (array.length > 0) {
                    final List<String> excludeList = new ArrayList<String>(array.length);
                    for (final String str : array) {
                        excludeList.add(str.trim());
                    }
                    checker = new ExcludeChecker(excludeList);
                }
            }
            if (includes != null) {
                final String[] array = includes.split(Patterns.COMMA_SEPARATOR);
                if (array.length > 0) {
                    final List<String> includeList = new ArrayList<String>(array.length);
                    for (final String str : array) {
                        includeList.add(str.trim());
                    }
                    checker = new IncludeChecker(includeList);
                }
            }
            if (checker == null) {
                checker = ListChecker.NOOP_CHECKER;
            }
            return checker;
        }
        
        public String getHost() {
            return this.host;
        }
        
        public CompressionType getCompressionType() {
            return this.compressionType;
        }
        
        public int getCompressionThreshold() {
            return this.compressionThreshold;
        }
        
        public boolean isIncludeStacktrace() {
            return this.includeStacktrace;
        }
        
        public boolean isIncludeThreadContext() {
            return this.includeThreadContext;
        }
        
        public boolean isIncludeNullDelimiter() {
            return this.includeNullDelimiter;
        }
        
        public boolean isIncludeNewLineDelimiter() {
            return this.includeNewLineDelimiter;
        }
        
        public KeyValuePair[] getAdditionalFields() {
            return this.additionalFields;
        }
        
        public B setHost(final String host) {
            this.host = host;
            return this.asBuilder();
        }
        
        public B setCompressionType(final CompressionType compressionType) {
            this.compressionType = compressionType;
            return this.asBuilder();
        }
        
        public B setCompressionThreshold(final int compressionThreshold) {
            this.compressionThreshold = compressionThreshold;
            return this.asBuilder();
        }
        
        public B setIncludeStacktrace(final boolean includeStacktrace) {
            this.includeStacktrace = includeStacktrace;
            return this.asBuilder();
        }
        
        public B setIncludeThreadContext(final boolean includeThreadContext) {
            this.includeThreadContext = includeThreadContext;
            return this.asBuilder();
        }
        
        public B setIncludeNullDelimiter(final boolean includeNullDelimiter) {
            this.includeNullDelimiter = includeNullDelimiter;
            return this.asBuilder();
        }
        
        public B setIncludeNewLineDelimiter(final boolean includeNewLineDelimiter) {
            this.includeNewLineDelimiter = includeNewLineDelimiter;
            return this.asBuilder();
        }
        
        public B setAdditionalFields(final KeyValuePair[] additionalFields) {
            this.additionalFields = additionalFields;
            return this.asBuilder();
        }
        
        public B setMessagePattern(final String pattern) {
            this.messagePattern = pattern;
            return this.asBuilder();
        }
        
        public B setPatternSelector(final PatternSelector patternSelector) {
            this.patternSelector = patternSelector;
            return this.asBuilder();
        }
        
        public B setMdcIncludes(final String mdcIncludes) {
            this.threadContextIncludes = mdcIncludes;
            return this.asBuilder();
        }
        
        public B setMdcExcludes(final String mdcExcludes) {
            this.threadContextExcludes = mdcExcludes;
            return this.asBuilder();
        }
        
        public B setIncludeMapMessage(final boolean includeMapMessage) {
            this.includeMapMessage = includeMapMessage;
            return this.asBuilder();
        }
        
        public B setMapMessageIncludes(final String mapMessageIncludes) {
            this.mapMessageIncludes = mapMessageIncludes;
            return this.asBuilder();
        }
        
        public B setMapMessageExcludes(final String mapMessageExcludes) {
            this.mapMessageExcludes = mapMessageExcludes;
            return this.asBuilder();
        }
        
        public B setThreadContextPrefix(final String prefix) {
            if (prefix != null) {
                this.threadContextPrefix = prefix;
            }
            return this.asBuilder();
        }
        
        public B setMapPrefix(final String prefix) {
            if (prefix != null) {
                this.mapPrefix = prefix;
            }
            return this.asBuilder();
        }
    }
    
    private class FieldWriter implements TriConsumer<String, Object, StringBuilder>
    {
        private final ListChecker checker;
        private final String prefix;
        
        FieldWriter(final ListChecker checker, final String prefix) {
            this.checker = checker;
            this.prefix = prefix;
        }
        
        @Override
        public void accept(final String key, final Object value, final StringBuilder stringBuilder) {
            final String stringValue = String.valueOf(value);
            if (this.checker.check(key) && (Strings.isNotEmpty(stringValue) || !GelfLayout.this.omitEmptyFields)) {
                stringBuilder.append("\"_");
                JsonUtils.quoteAsString(Strings.concat(this.prefix, key), stringBuilder);
                stringBuilder.append("\":\"");
                JsonUtils.quoteAsString(toNullSafeString(stringValue), stringBuilder);
                stringBuilder.append("\",");
            }
        }
        
        public ListChecker getChecker() {
            return this.checker;
        }
    }
}
