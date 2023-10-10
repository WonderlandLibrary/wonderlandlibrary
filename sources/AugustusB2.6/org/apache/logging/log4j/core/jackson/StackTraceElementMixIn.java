// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "nativeMethod" })
abstract class StackTraceElementMixIn
{
    @JsonCreator
    StackTraceElementMixIn(@JsonProperty("class") final String declaringClass, @JsonProperty("method") final String methodName, @JsonProperty("file") final String fileName, @JsonProperty("line") final int lineNumber) {
    }
    
    @JsonProperty("class")
    @JacksonXmlProperty(localName = "class", isAttribute = true)
    abstract String getClassName();
    
    @JsonProperty("file")
    @JacksonXmlProperty(localName = "file", isAttribute = true)
    abstract String getFileName();
    
    @JsonProperty("line")
    @JacksonXmlProperty(localName = "line", isAttribute = true)
    abstract int getLineNumber();
    
    @JsonProperty("method")
    @JacksonXmlProperty(localName = "method", isAttribute = true)
    abstract String getMethodName();
}
