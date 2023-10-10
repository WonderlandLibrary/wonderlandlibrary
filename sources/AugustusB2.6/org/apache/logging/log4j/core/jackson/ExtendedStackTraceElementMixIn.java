// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.logging.log4j.core.impl.ExtendedClassInfo;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;

@JsonPropertyOrder({ "class", "method", "file", "line", "exact", "location", "version" })
abstract class ExtendedStackTraceElementMixIn implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @JsonCreator
    public ExtendedStackTraceElementMixIn(@JsonProperty("class") final String declaringClass, @JsonProperty("method") final String methodName, @JsonProperty("file") final String fileName, @JsonProperty("line") final int lineNumber, @JsonProperty("exact") final boolean exact, @JsonProperty("location") final String location, @JsonProperty("version") final String version) {
    }
    
    @JsonProperty("class")
    @JacksonXmlProperty(localName = "class", isAttribute = true)
    public abstract String getClassName();
    
    @JsonProperty
    @JacksonXmlProperty(isAttribute = true)
    public abstract boolean getExact();
    
    @JsonIgnore
    public abstract ExtendedClassInfo getExtraClassInfo();
    
    @JsonProperty("file")
    @JacksonXmlProperty(localName = "file", isAttribute = true)
    public abstract String getFileName();
    
    @JsonProperty("line")
    @JacksonXmlProperty(localName = "line", isAttribute = true)
    public abstract int getLineNumber();
    
    @JsonProperty
    @JacksonXmlProperty(isAttribute = true)
    public abstract String getLocation();
    
    @JsonProperty("method")
    @JacksonXmlProperty(localName = "method", isAttribute = true)
    public abstract String getMethodName();
    
    @JsonIgnore
    abstract StackTraceElement getStackTraceElement();
    
    @JsonProperty
    @JacksonXmlProperty(isAttribute = true)
    public abstract String getVersion();
    
    @JsonIgnore
    public abstract boolean isNativeMethod();
}
