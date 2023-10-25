/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
 *  com.fasterxml.jackson.dataformat.xml.XmlMapper
 */
package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.logging.log4j.core.jackson.Log4jXmlModule;

public class Log4jXmlObjectMapper
extends XmlMapper {
    private static final long serialVersionUID = 1L;

    public Log4jXmlObjectMapper() {
        this(true, false);
    }

    public Log4jXmlObjectMapper(boolean includeStacktrace, boolean stacktraceAsString) {
        super((JacksonXmlModule)new Log4jXmlModule(includeStacktrace, stacktraceAsString));
        this.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }
}

