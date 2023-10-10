// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.layout;

import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.core.time.Instant;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import java.io.Serializable;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.core.JsonGenerationException;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import java.io.IOException;
import java.io.Writer;
import org.apache.logging.log4j.core.util.StringBuilderWriter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.util.KeyValuePair;
import java.nio.charset.Charset;
import org.apache.logging.log4j.core.config.Configuration;
import com.fasterxml.jackson.databind.ObjectWriter;

abstract class AbstractJacksonLayout extends AbstractStringLayout
{
    protected static final String DEFAULT_EOL = "\r\n";
    protected static final String COMPACT_EOL = "";
    protected final String eol;
    protected final ObjectWriter objectWriter;
    protected final boolean compact;
    protected final boolean complete;
    protected final boolean includeNullDelimiter;
    protected final ResolvableKeyValuePair[] additionalFields;
    
    @Deprecated
    protected AbstractJacksonLayout(final Configuration config, final ObjectWriter objectWriter, final Charset charset, final boolean compact, final boolean complete, final boolean eventEol, final Serializer headerSerializer, final Serializer footerSerializer) {
        this(config, objectWriter, charset, compact, complete, eventEol, headerSerializer, footerSerializer, false);
    }
    
    @Deprecated
    protected AbstractJacksonLayout(final Configuration config, final ObjectWriter objectWriter, final Charset charset, final boolean compact, final boolean complete, final boolean eventEol, final Serializer headerSerializer, final Serializer footerSerializer, final boolean includeNullDelimiter) {
        this(config, objectWriter, charset, compact, complete, eventEol, null, headerSerializer, footerSerializer, includeNullDelimiter, null);
    }
    
    protected AbstractJacksonLayout(final Configuration config, final ObjectWriter objectWriter, final Charset charset, final boolean compact, final boolean complete, final boolean eventEol, final String endOfLine, final Serializer headerSerializer, final Serializer footerSerializer, final boolean includeNullDelimiter, final KeyValuePair[] additionalFields) {
        super(config, charset, headerSerializer, footerSerializer);
        this.objectWriter = objectWriter;
        this.compact = compact;
        this.complete = complete;
        this.eol = ((endOfLine != null) ? endOfLine : ((compact && !eventEol) ? "" : "\r\n"));
        this.includeNullDelimiter = includeNullDelimiter;
        this.additionalFields = prepareAdditionalFields(config, additionalFields);
    }
    
    protected static boolean valueNeedsLookup(final String value) {
        return value != null && value.contains("${");
    }
    
    private static ResolvableKeyValuePair[] prepareAdditionalFields(final Configuration config, final KeyValuePair[] additionalFields) {
        if (additionalFields == null || additionalFields.length == 0) {
            return ResolvableKeyValuePair.EMPTY_ARRAY;
        }
        final ResolvableKeyValuePair[] resolvableFields = new ResolvableKeyValuePair[additionalFields.length];
        for (int i = 0; i < additionalFields.length; ++i) {
            final ResolvableKeyValuePair[] array = resolvableFields;
            final int n = i;
            final ResolvableKeyValuePair resolvableKeyValuePair = new ResolvableKeyValuePair(additionalFields[i]);
            array[n] = resolvableKeyValuePair;
            final ResolvableKeyValuePair resolvable = resolvableKeyValuePair;
            if (config == null && resolvable.valueNeedsLookup) {
                throw new IllegalArgumentException("configuration needs to be set when there are additional fields with variables");
            }
        }
        return resolvableFields;
    }
    
    @Override
    public String toSerializable(final LogEvent event) {
        final StringBuilderWriter writer = new StringBuilderWriter();
        try {
            this.toSerializable(event, writer);
            return writer.toString();
        }
        catch (IOException e) {
            AbstractJacksonLayout.LOGGER.error(e);
            return "";
        }
    }
    
    private static LogEvent convertMutableToLog4jEvent(final LogEvent event) {
        return (event instanceof Log4jLogEvent) ? event : Log4jLogEvent.createMemento(event);
    }
    
    protected Object wrapLogEvent(final LogEvent event) {
        if (this.additionalFields.length > 0) {
            final Map<String, String> additionalFieldsMap = this.resolveAdditionalFields(event);
            return new LogEventWithAdditionalFields(event, additionalFieldsMap);
        }
        if (event instanceof Message) {
            return new ReadOnlyLogEventWrapper(event);
        }
        return event;
    }
    
    private Map<String, String> resolveAdditionalFields(final LogEvent logEvent) {
        final Map<String, String> additionalFieldsMap = new LinkedHashMap<String, String>(this.additionalFields.length);
        final StrSubstitutor strSubstitutor = this.configuration.getStrSubstitutor();
        for (final ResolvableKeyValuePair pair : this.additionalFields) {
            if (pair.valueNeedsLookup) {
                additionalFieldsMap.put(pair.key, strSubstitutor.replace(logEvent, pair.value));
            }
            else {
                additionalFieldsMap.put(pair.key, pair.value);
            }
        }
        return additionalFieldsMap;
    }
    
    public void toSerializable(final LogEvent event, final Writer writer) throws JsonGenerationException, JsonMappingException, IOException {
        this.objectWriter.writeValue(writer, this.wrapLogEvent(convertMutableToLog4jEvent(event)));
        writer.write(this.eol);
        if (this.includeNullDelimiter) {
            writer.write(0);
        }
        this.markEvent();
    }
    
    public abstract static class Builder<B extends Builder<B>> extends AbstractStringLayout.Builder<B>
    {
        @PluginBuilderAttribute
        private boolean eventEol;
        @PluginBuilderAttribute
        private String endOfLine;
        @PluginBuilderAttribute
        private boolean compact;
        @PluginBuilderAttribute
        private boolean complete;
        @PluginBuilderAttribute
        private boolean locationInfo;
        @PluginBuilderAttribute
        private boolean properties;
        @PluginBuilderAttribute
        private boolean includeStacktrace;
        @PluginBuilderAttribute
        private boolean stacktraceAsString;
        @PluginBuilderAttribute
        private boolean includeNullDelimiter;
        @PluginBuilderAttribute
        private boolean includeTimeMillis;
        @PluginElement("AdditionalField")
        private KeyValuePair[] additionalFields;
        
        public Builder() {
            this.includeStacktrace = true;
            this.stacktraceAsString = false;
            this.includeNullDelimiter = false;
            this.includeTimeMillis = false;
        }
        
        protected String toStringOrNull(final byte[] header) {
            return (header == null) ? null : new String(header, Charset.defaultCharset());
        }
        
        public boolean getEventEol() {
            return this.eventEol;
        }
        
        public String getEndOfLine() {
            return this.endOfLine;
        }
        
        public boolean isCompact() {
            return this.compact;
        }
        
        public boolean isComplete() {
            return this.complete;
        }
        
        public boolean isLocationInfo() {
            return this.locationInfo;
        }
        
        public boolean isProperties() {
            return this.properties;
        }
        
        public boolean isIncludeStacktrace() {
            return this.includeStacktrace;
        }
        
        public boolean isStacktraceAsString() {
            return this.stacktraceAsString;
        }
        
        public boolean isIncludeNullDelimiter() {
            return this.includeNullDelimiter;
        }
        
        public boolean isIncludeTimeMillis() {
            return this.includeTimeMillis;
        }
        
        public KeyValuePair[] getAdditionalFields() {
            return this.additionalFields;
        }
        
        public B setEventEol(final boolean eventEol) {
            this.eventEol = eventEol;
            return this.asBuilder();
        }
        
        public B setEndOfLine(final String endOfLine) {
            this.endOfLine = endOfLine;
            return this.asBuilder();
        }
        
        public B setCompact(final boolean compact) {
            this.compact = compact;
            return this.asBuilder();
        }
        
        public B setComplete(final boolean complete) {
            this.complete = complete;
            return this.asBuilder();
        }
        
        public B setLocationInfo(final boolean locationInfo) {
            this.locationInfo = locationInfo;
            return this.asBuilder();
        }
        
        public B setProperties(final boolean properties) {
            this.properties = properties;
            return this.asBuilder();
        }
        
        public B setIncludeStacktrace(final boolean includeStacktrace) {
            this.includeStacktrace = includeStacktrace;
            return this.asBuilder();
        }
        
        public B setStacktraceAsString(final boolean stacktraceAsString) {
            this.stacktraceAsString = stacktraceAsString;
            return this.asBuilder();
        }
        
        public B setIncludeNullDelimiter(final boolean includeNullDelimiter) {
            this.includeNullDelimiter = includeNullDelimiter;
            return this.asBuilder();
        }
        
        public B setIncludeTimeMillis(final boolean includeTimeMillis) {
            this.includeTimeMillis = includeTimeMillis;
            return this.asBuilder();
        }
        
        public B setAdditionalFields(final KeyValuePair[] additionalFields) {
            this.additionalFields = additionalFields;
            return this.asBuilder();
        }
    }
    
    @JsonRootName("Event")
    @JacksonXmlRootElement(namespace = "http://logging.apache.org/log4j/2.0/events", localName = "Event")
    public static class LogEventWithAdditionalFields
    {
        private final Object logEvent;
        private final Map<String, String> additionalFields;
        
        public LogEventWithAdditionalFields(final Object logEvent, final Map<String, String> additionalFields) {
            this.logEvent = logEvent;
            this.additionalFields = additionalFields;
        }
        
        @JsonUnwrapped
        public Object getLogEvent() {
            return this.logEvent;
        }
        
        @JsonAnyGetter
        public Map<String, String> getAdditionalFields() {
            return this.additionalFields;
        }
    }
    
    protected static class ResolvableKeyValuePair
    {
        static final ResolvableKeyValuePair[] EMPTY_ARRAY;
        final String key;
        final String value;
        final boolean valueNeedsLookup;
        
        ResolvableKeyValuePair(final KeyValuePair pair) {
            this.key = pair.getKey();
            this.value = pair.getValue();
            this.valueNeedsLookup = AbstractJacksonLayout.valueNeedsLookup(this.value);
        }
        
        static {
            EMPTY_ARRAY = new ResolvableKeyValuePair[0];
        }
    }
    
    private static class ReadOnlyLogEventWrapper implements LogEvent
    {
        @JsonIgnore
        private final LogEvent event;
        
        public ReadOnlyLogEventWrapper(final LogEvent event) {
            this.event = event;
        }
        
        @Override
        public LogEvent toImmutable() {
            return this.event.toImmutable();
        }
        
        @Override
        public Map<String, String> getContextMap() {
            return this.event.getContextMap();
        }
        
        @Override
        public ReadOnlyStringMap getContextData() {
            return this.event.getContextData();
        }
        
        @Override
        public ThreadContext.ContextStack getContextStack() {
            return this.event.getContextStack();
        }
        
        @Override
        public String getLoggerFqcn() {
            return this.event.getLoggerFqcn();
        }
        
        @Override
        public Level getLevel() {
            return this.event.getLevel();
        }
        
        @Override
        public String getLoggerName() {
            return this.event.getLoggerName();
        }
        
        @Override
        public Marker getMarker() {
            return this.event.getMarker();
        }
        
        @Override
        public Message getMessage() {
            return this.event.getMessage();
        }
        
        @Override
        public long getTimeMillis() {
            return this.event.getTimeMillis();
        }
        
        @Override
        public Instant getInstant() {
            return this.event.getInstant();
        }
        
        @Override
        public StackTraceElement getSource() {
            return this.event.getSource();
        }
        
        @Override
        public String getThreadName() {
            return this.event.getThreadName();
        }
        
        @Override
        public long getThreadId() {
            return this.event.getThreadId();
        }
        
        @Override
        public int getThreadPriority() {
            return this.event.getThreadPriority();
        }
        
        @Override
        public Throwable getThrown() {
            return this.event.getThrown();
        }
        
        @Override
        public ThrowableProxy getThrownProxy() {
            return this.event.getThrownProxy();
        }
        
        @Override
        public boolean isEndOfBatch() {
            return this.event.isEndOfBatch();
        }
        
        @Override
        public boolean isIncludeLocation() {
            return this.event.isIncludeLocation();
        }
        
        @Override
        public void setEndOfBatch(final boolean endOfBatch) {
        }
        
        @Override
        public void setIncludeLocation(final boolean locationRequired) {
        }
        
        @Override
        public long getNanoTime() {
            return this.event.getNanoTime();
        }
    }
}
