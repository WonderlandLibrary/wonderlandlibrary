/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnore
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
 *  com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
 */
package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.apache.logging.log4j.core.impl.ExtendedStackTraceElement;
import org.apache.logging.log4j.core.impl.ThrowableProxy;

abstract class ThrowableProxyMixIn {
    @JsonProperty(value="cause")
    @JacksonXmlProperty(namespace="http://logging.apache.org/log4j/2.0/events", localName="Cause")
    private ThrowableProxyMixIn causeProxy;
    @JsonProperty
    @JacksonXmlProperty(isAttribute=true)
    private int commonElementCount;
    @JsonProperty(value="extendedStackTrace")
    @JacksonXmlElementWrapper(namespace="http://logging.apache.org/log4j/2.0/events", localName="ExtendedStackTrace")
    @JacksonXmlProperty(namespace="http://logging.apache.org/log4j/2.0/events", localName="ExtendedStackTraceItem")
    private ExtendedStackTraceElement[] extendedStackTrace;
    @JsonProperty
    @JacksonXmlProperty(isAttribute=true)
    private String localizedMessage;
    @JsonProperty
    @JacksonXmlProperty(isAttribute=true)
    private String message;
    @JsonProperty
    @JacksonXmlProperty(isAttribute=true)
    private String name;
    @JsonIgnore
    private transient Throwable throwable;

    ThrowableProxyMixIn() {
    }

    @JsonIgnore
    public abstract String getCauseStackTraceAsString();

    @JsonIgnore
    public abstract String getExtendedStackTraceAsString();

    @JsonIgnore
    public abstract StackTraceElement[] getStackTrace();

    @JsonProperty(value="suppressed")
    @JacksonXmlElementWrapper(namespace="http://logging.apache.org/log4j/2.0/events", localName="Suppressed")
    @JacksonXmlProperty(namespace="http://logging.apache.org/log4j/2.0/events", localName="SuppressedItem")
    public abstract ThrowableProxy[] getSuppressedProxies();

    @JsonIgnore
    public abstract String getSuppressedStackTrace();

    @JsonIgnore
    public abstract Throwable getThrowable();
}

