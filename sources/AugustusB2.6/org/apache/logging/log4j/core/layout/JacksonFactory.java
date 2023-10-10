// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.layout;

import javax.xml.stream.XMLStreamException;
import org.codehaus.stax2.XMLStreamWriter2;
import com.fasterxml.jackson.dataformat.xml.util.DefaultXmlPrettyPrinter;
import org.apache.logging.log4j.core.jackson.Log4jYamlObjectMapper;
import org.apache.logging.log4j.core.jackson.Log4jXmlObjectMapper;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import org.apache.logging.log4j.core.jackson.Log4jJsonObjectMapper;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import java.util.Set;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import java.util.HashSet;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.PrettyPrinter;

abstract class JacksonFactory
{
    protected abstract String getPropertyNameForTimeMillis();
    
    protected abstract String getPropertyNameForInstant();
    
    protected abstract String getPropertNameForContextMap();
    
    protected abstract String getPropertNameForSource();
    
    protected abstract String getPropertNameForNanoTime();
    
    protected abstract PrettyPrinter newCompactPrinter();
    
    protected abstract ObjectMapper newObjectMapper();
    
    protected abstract PrettyPrinter newPrettyPrinter();
    
    ObjectWriter newWriter(final boolean locationInfo, final boolean properties, final boolean compact) {
        return this.newWriter(locationInfo, properties, compact, false);
    }
    
    ObjectWriter newWriter(final boolean locationInfo, final boolean properties, final boolean compact, final boolean includeMillis) {
        final SimpleFilterProvider filters = new SimpleFilterProvider();
        final Set<String> except = new HashSet<String>(3);
        if (!locationInfo) {
            except.add(this.getPropertNameForSource());
        }
        if (!properties) {
            except.add(this.getPropertNameForContextMap());
        }
        if (includeMillis) {
            except.add(this.getPropertyNameForInstant());
        }
        else {
            except.add(this.getPropertyNameForTimeMillis());
        }
        except.add(this.getPropertNameForNanoTime());
        filters.addFilter(Log4jLogEvent.class.getName(), SimpleBeanPropertyFilter.serializeAllExcept((Set)except));
        final ObjectWriter writer = this.newObjectMapper().writer(compact ? this.newCompactPrinter() : this.newPrettyPrinter());
        return writer.with((FilterProvider)filters);
    }
    
    static class JSON extends JacksonFactory
    {
        private final boolean encodeThreadContextAsList;
        private final boolean includeStacktrace;
        private final boolean stacktraceAsString;
        private final boolean objectMessageAsJsonObject;
        
        public JSON(final boolean encodeThreadContextAsList, final boolean includeStacktrace, final boolean stacktraceAsString, final boolean objectMessageAsJsonObject) {
            this.encodeThreadContextAsList = encodeThreadContextAsList;
            this.includeStacktrace = includeStacktrace;
            this.stacktraceAsString = stacktraceAsString;
            this.objectMessageAsJsonObject = objectMessageAsJsonObject;
        }
        
        @Override
        protected String getPropertNameForContextMap() {
            return "contextMap";
        }
        
        @Override
        protected String getPropertyNameForTimeMillis() {
            return "timeMillis";
        }
        
        @Override
        protected String getPropertyNameForInstant() {
            return "instant";
        }
        
        @Override
        protected String getPropertNameForSource() {
            return "source";
        }
        
        @Override
        protected String getPropertNameForNanoTime() {
            return "nanoTime";
        }
        
        @Override
        protected PrettyPrinter newCompactPrinter() {
            return (PrettyPrinter)new MinimalPrettyPrinter();
        }
        
        @Override
        protected ObjectMapper newObjectMapper() {
            return new Log4jJsonObjectMapper(this.encodeThreadContextAsList, this.includeStacktrace, this.stacktraceAsString, this.objectMessageAsJsonObject);
        }
        
        @Override
        protected PrettyPrinter newPrettyPrinter() {
            return (PrettyPrinter)new DefaultPrettyPrinter();
        }
    }
    
    static class XML extends JacksonFactory
    {
        static final int DEFAULT_INDENT = 1;
        private final boolean includeStacktrace;
        private final boolean stacktraceAsString;
        
        public XML(final boolean includeStacktrace, final boolean stacktraceAsString) {
            this.includeStacktrace = includeStacktrace;
            this.stacktraceAsString = stacktraceAsString;
        }
        
        @Override
        protected String getPropertyNameForTimeMillis() {
            return "TimeMillis";
        }
        
        @Override
        protected String getPropertyNameForInstant() {
            return "Instant";
        }
        
        @Override
        protected String getPropertNameForContextMap() {
            return "ContextMap";
        }
        
        @Override
        protected String getPropertNameForSource() {
            return "Source";
        }
        
        @Override
        protected String getPropertNameForNanoTime() {
            return "nanoTime";
        }
        
        @Override
        protected PrettyPrinter newCompactPrinter() {
            return null;
        }
        
        @Override
        protected ObjectMapper newObjectMapper() {
            return (ObjectMapper)new Log4jXmlObjectMapper(this.includeStacktrace, this.stacktraceAsString);
        }
        
        @Override
        protected PrettyPrinter newPrettyPrinter() {
            return (PrettyPrinter)new Log4jXmlPrettyPrinter(1);
        }
    }
    
    static class YAML extends JacksonFactory
    {
        private final boolean includeStacktrace;
        private final boolean stacktraceAsString;
        
        public YAML(final boolean includeStacktrace, final boolean stacktraceAsString) {
            this.includeStacktrace = includeStacktrace;
            this.stacktraceAsString = stacktraceAsString;
        }
        
        @Override
        protected String getPropertyNameForTimeMillis() {
            return "timeMillis";
        }
        
        @Override
        protected String getPropertyNameForInstant() {
            return "instant";
        }
        
        @Override
        protected String getPropertNameForContextMap() {
            return "contextMap";
        }
        
        @Override
        protected String getPropertNameForSource() {
            return "source";
        }
        
        @Override
        protected String getPropertNameForNanoTime() {
            return "nanoTime";
        }
        
        @Override
        protected PrettyPrinter newCompactPrinter() {
            return (PrettyPrinter)new MinimalPrettyPrinter();
        }
        
        @Override
        protected ObjectMapper newObjectMapper() {
            return (ObjectMapper)new Log4jYamlObjectMapper(false, this.includeStacktrace, this.stacktraceAsString);
        }
        
        @Override
        protected PrettyPrinter newPrettyPrinter() {
            return (PrettyPrinter)new DefaultPrettyPrinter();
        }
    }
    
    static class Log4jXmlPrettyPrinter extends DefaultXmlPrettyPrinter
    {
        private static final long serialVersionUID = 1L;
        
        Log4jXmlPrettyPrinter(final int nesting) {
            this._nesting = nesting;
        }
        
        public void writePrologLinefeed(final XMLStreamWriter2 sw) throws XMLStreamException {
        }
        
        public DefaultXmlPrettyPrinter createInstance() {
            return new Log4jXmlPrettyPrinter(1);
        }
    }
}
