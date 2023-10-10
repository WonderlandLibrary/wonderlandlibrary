// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jackson;

import org.apache.logging.log4j.core.time.Instant;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Level;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import org.apache.logging.log4j.ThreadContext;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.apache.logging.log4j.core.LogEvent;

@JsonRootName("Event")
@JacksonXmlRootElement(namespace = "http://logging.apache.org/log4j/2.0/events", localName = "Event")
@JsonFilter("org.apache.logging.log4j.core.impl.Log4jLogEvent")
@JsonPropertyOrder({ "timeMillis", "instant", "threadName", "level", "loggerName", "marker", "message", "thrown", "ContextMap", "contextStack", "loggerFQCN", "Source", "endOfBatch" })
abstract class LogEventJsonMixIn implements LogEvent
{
    private static final long serialVersionUID = 1L;
    
    @JsonIgnore
    @Override
    public abstract Map<String, String> getContextMap();
    
    @JsonProperty("contextMap")
    @JacksonXmlProperty(namespace = "http://logging.apache.org/log4j/2.0/events", localName = "ContextMap")
    @JsonSerialize(using = ContextDataSerializer.class)
    @JsonDeserialize(using = ContextDataDeserializer.class)
    @Override
    public abstract ReadOnlyStringMap getContextData();
    
    @JsonProperty("contextStack")
    @JacksonXmlElementWrapper(namespace = "http://logging.apache.org/log4j/2.0/events", localName = "ContextStack")
    @JacksonXmlProperty(namespace = "http://logging.apache.org/log4j/2.0/events", localName = "ContextStackItem")
    @Override
    public abstract ThreadContext.ContextStack getContextStack();
    
    @JsonProperty
    @JacksonXmlProperty(isAttribute = true)
    @Override
    public abstract Level getLevel();
    
    @JsonProperty
    @JacksonXmlProperty(isAttribute = true)
    @Override
    public abstract String getLoggerFqcn();
    
    @JsonProperty
    @JacksonXmlProperty(isAttribute = true)
    @Override
    public abstract String getLoggerName();
    
    @JsonProperty("marker")
    @JacksonXmlProperty(namespace = "http://logging.apache.org/log4j/2.0/events", localName = "Marker")
    @Override
    public abstract Marker getMarker();
    
    @JsonProperty("message")
    @JsonDeserialize(using = SimpleMessageDeserializer.class)
    @JacksonXmlProperty(namespace = "http://logging.apache.org/log4j/2.0/events", localName = "Message")
    @Override
    public abstract Message getMessage();
    
    @JsonProperty("source")
    @JsonDeserialize(using = Log4jStackTraceElementDeserializer.class)
    @JacksonXmlProperty(namespace = "http://logging.apache.org/log4j/2.0/events", localName = "Source")
    @Override
    public abstract StackTraceElement getSource();
    
    @JsonProperty("threadId")
    @JacksonXmlProperty(isAttribute = true, localName = "threadId")
    @Override
    public abstract long getThreadId();
    
    @JsonProperty("thread")
    @JacksonXmlProperty(isAttribute = true, localName = "thread")
    @Override
    public abstract String getThreadName();
    
    @JsonProperty("threadPriority")
    @JacksonXmlProperty(isAttribute = true, localName = "threadPriority")
    @Override
    public abstract int getThreadPriority();
    
    @JsonIgnore
    @Override
    public abstract Throwable getThrown();
    
    @JsonProperty("thrown")
    @JacksonXmlProperty(namespace = "http://logging.apache.org/log4j/2.0/events", localName = "Thrown")
    @Override
    public abstract ThrowableProxy getThrownProxy();
    
    @JsonProperty(value = "timeMillis", access = JsonProperty.Access.READ_ONLY)
    @JacksonXmlProperty(isAttribute = true)
    @Override
    public abstract long getTimeMillis();
    
    @JsonProperty("instant")
    @JacksonXmlProperty(namespace = "http://logging.apache.org/log4j/2.0/events", localName = "Instant")
    @Override
    public abstract Instant getInstant();
    
    @JsonProperty
    @JacksonXmlProperty(isAttribute = true)
    @Override
    public abstract boolean isEndOfBatch();
    
    @JsonIgnore
    @Override
    public abstract boolean isIncludeLocation();
    
    @Override
    public abstract void setEndOfBatch(final boolean endOfBatch);
    
    @Override
    public abstract void setIncludeLocation(final boolean locationRequired);
}
