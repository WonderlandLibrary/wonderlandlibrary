/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonFilter
 *  com.fasterxml.jackson.annotation.JsonIgnore
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.fasterxml.jackson.annotation.JsonProperty$Access
 *  com.fasterxml.jackson.annotation.JsonPropertyOrder
 *  com.fasterxml.jackson.annotation.JsonRootName
 *  com.fasterxml.jackson.databind.annotation.JsonDeserialize
 *  com.fasterxml.jackson.databind.annotation.JsonSerialize
 *  com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
 *  com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
 *  com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
 */
package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.core.jackson.ContextDataDeserializer;
import org.apache.logging.log4j.core.jackson.ContextDataSerializer;
import org.apache.logging.log4j.core.jackson.Log4jStackTraceElementDeserializer;
import org.apache.logging.log4j.core.jackson.SimpleMessageDeserializer;
import org.apache.logging.log4j.core.time.Instant;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.ReadOnlyStringMap;

@JsonRootName(value="Event")
@JacksonXmlRootElement(namespace="http://logging.apache.org/log4j/2.0/events", localName="Event")
@JsonFilter(value="org.apache.logging.log4j.core.impl.Log4jLogEvent")
@JsonPropertyOrder(value={"timeMillis", "instant", "threadName", "level", "loggerName", "marker", "message", "thrown", "ContextMap", "contextStack", "loggerFQCN", "Source", "endOfBatch"})
abstract class LogEventJsonMixIn
implements LogEvent {
    private static final long serialVersionUID = 1L;

    LogEventJsonMixIn() {
    }

    @Override
    @JsonIgnore
    public abstract Map<String, String> getContextMap();

    @JsonProperty(value="contextMap")
    @JacksonXmlProperty(namespace="http://logging.apache.org/log4j/2.0/events", localName="ContextMap")
    @JsonSerialize(using=ContextDataSerializer.class)
    @JsonDeserialize(using=ContextDataDeserializer.class)
    public abstract ReadOnlyStringMap getContextData();

    @Override
    @JsonProperty(value="contextStack")
    @JacksonXmlElementWrapper(namespace="http://logging.apache.org/log4j/2.0/events", localName="ContextStack")
    @JacksonXmlProperty(namespace="http://logging.apache.org/log4j/2.0/events", localName="ContextStackItem")
    public abstract ThreadContext.ContextStack getContextStack();

    @Override
    @JsonProperty
    @JacksonXmlProperty(isAttribute=true)
    public abstract Level getLevel();

    @JsonProperty
    @JacksonXmlProperty(isAttribute=true)
    public abstract String getLoggerFqcn();

    @Override
    @JsonProperty
    @JacksonXmlProperty(isAttribute=true)
    public abstract String getLoggerName();

    @Override
    @JsonProperty(value="marker")
    @JacksonXmlProperty(namespace="http://logging.apache.org/log4j/2.0/events", localName="Marker")
    public abstract Marker getMarker();

    @Override
    @JsonProperty(value="message")
    @JsonDeserialize(using=SimpleMessageDeserializer.class)
    @JacksonXmlProperty(namespace="http://logging.apache.org/log4j/2.0/events", localName="Message")
    public abstract Message getMessage();

    @Override
    @JsonProperty(value="source")
    @JsonDeserialize(using=Log4jStackTraceElementDeserializer.class)
    @JacksonXmlProperty(namespace="http://logging.apache.org/log4j/2.0/events", localName="Source")
    public abstract StackTraceElement getSource();

    @JsonProperty(value="threadId")
    @JacksonXmlProperty(isAttribute=true, localName="threadId")
    public abstract long getThreadId();

    @Override
    @JsonProperty(value="thread")
    @JacksonXmlProperty(isAttribute=true, localName="thread")
    public abstract String getThreadName();

    @JsonProperty(value="threadPriority")
    @JacksonXmlProperty(isAttribute=true, localName="threadPriority")
    public abstract int getThreadPriority();

    @Override
    @JsonIgnore
    public abstract Throwable getThrown();

    @JsonProperty(value="thrown")
    @JacksonXmlProperty(namespace="http://logging.apache.org/log4j/2.0/events", localName="Thrown")
    public abstract ThrowableProxy getThrownProxy();

    @JsonProperty(value="timeMillis", access=JsonProperty.Access.READ_ONLY)
    @JacksonXmlProperty(isAttribute=true)
    public abstract long getTimeMillis();

    @JsonProperty(value="instant")
    @JacksonXmlProperty(namespace="http://logging.apache.org/log4j/2.0/events", localName="Instant")
    public abstract Instant getInstant();

    @Override
    @JsonProperty
    @JacksonXmlProperty(isAttribute=true)
    public abstract boolean isEndOfBatch();

    @Override
    @JsonIgnore
    public abstract boolean isIncludeLocation();

    @Override
    public abstract void setEndOfBatch(boolean var1);

    @Override
    public abstract void setIncludeLocation(boolean var1);
}

