// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class Log4jYamlObjectMapper extends YAMLMapper
{
    private static final long serialVersionUID = 1L;
    
    public Log4jYamlObjectMapper() {
        this(false, true, false);
    }
    
    public Log4jYamlObjectMapper(final boolean encodeThreadContextAsList, final boolean includeStacktrace, final boolean stacktraceAsString) {
        this.registerModule((Module)new Log4jYamlModule(encodeThreadContextAsList, includeStacktrace, stacktraceAsString));
        this.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }
}
