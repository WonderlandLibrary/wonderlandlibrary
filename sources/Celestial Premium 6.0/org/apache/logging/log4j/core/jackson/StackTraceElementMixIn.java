/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonCreator
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
 */
package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(value={"nativeMethod"})
abstract class StackTraceElementMixIn {
    @JsonCreator
    StackTraceElementMixIn(@JsonProperty(value="class") String declaringClass, @JsonProperty(value="method") String methodName, @JsonProperty(value="file") String fileName, @JsonProperty(value="line") int lineNumber) {
    }

    @JsonProperty(value="class")
    @JacksonXmlProperty(localName="class", isAttribute=true)
    abstract String getClassName();

    @JsonProperty(value="file")
    @JacksonXmlProperty(localName="file", isAttribute=true)
    abstract String getFileName();

    @JsonProperty(value="line")
    @JacksonXmlProperty(localName="line", isAttribute=true)
    abstract int getLineNumber();

    @JsonProperty(value="method")
    @JacksonXmlProperty(localName="method", isAttribute=true)
    abstract String getMethodName();
}

