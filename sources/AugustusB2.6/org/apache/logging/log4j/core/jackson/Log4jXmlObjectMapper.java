// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class Log4jXmlObjectMapper extends XmlMapper
{
    private static final long serialVersionUID = 1L;
    
    public Log4jXmlObjectMapper() {
        this(true, false);
    }
    
    public Log4jXmlObjectMapper(final boolean includeStacktrace, final boolean stacktraceAsString) {
        super((JacksonXmlModule)new Log4jXmlModule(includeStacktrace, stacktraceAsString));
        this.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }
}
