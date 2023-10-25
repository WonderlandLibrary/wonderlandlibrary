/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonCreator
 *  com.fasterxml.jackson.annotation.JsonIgnore
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.fasterxml.jackson.annotation.JsonPropertyOrder
 *  com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
 */
package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import org.apache.logging.log4j.core.impl.ExtendedClassInfo;

@JsonPropertyOrder(value={"class", "method", "file", "line", "exact", "location", "version"})
abstract class ExtendedStackTraceElementMixIn
implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonCreator
    public ExtendedStackTraceElementMixIn(@JsonProperty(value="class") String declaringClass, @JsonProperty(value="method") String methodName, @JsonProperty(value="file") String fileName, @JsonProperty(value="line") int lineNumber, @JsonProperty(value="exact") boolean exact, @JsonProperty(value="location") String location, @JsonProperty(value="version") String version) {
    }

    @JsonProperty(value="class")
    @JacksonXmlProperty(localName="class", isAttribute=true)
    public abstract String getClassName();

    @JsonProperty
    @JacksonXmlProperty(isAttribute=true)
    public abstract boolean getExact();

    @JsonIgnore
    public abstract ExtendedClassInfo getExtraClassInfo();

    @JsonProperty(value="file")
    @JacksonXmlProperty(localName="file", isAttribute=true)
    public abstract String getFileName();

    @JsonProperty(value="line")
    @JacksonXmlProperty(localName="line", isAttribute=true)
    public abstract int getLineNumber();

    @JsonProperty
    @JacksonXmlProperty(isAttribute=true)
    public abstract String getLocation();

    @JsonProperty(value="method")
    @JacksonXmlProperty(localName="method", isAttribute=true)
    public abstract String getMethodName();

    @JsonIgnore
    abstract StackTraceElement getStackTraceElement();

    @JsonProperty
    @JacksonXmlProperty(isAttribute=true)
    public abstract String getVersion();

    @JsonIgnore
    public abstract boolean isNativeMethod();
}

