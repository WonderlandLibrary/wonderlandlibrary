/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.PrettyPrinter
 *  com.fasterxml.jackson.core.util.DefaultPrettyPrinter
 *  com.fasterxml.jackson.core.util.MinimalPrettyPrinter
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.fasterxml.jackson.databind.ObjectWriter
 *  com.fasterxml.jackson.databind.ser.FilterProvider
 *  com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter
 *  com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider
 *  com.fasterxml.jackson.dataformat.xml.util.DefaultXmlPrettyPrinter
 *  org.codehaus.stax2.XMLStreamWriter2
 */
package org.apache.logging.log4j.core.layout;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.dataformat.xml.util.DefaultXmlPrettyPrinter;
import java.util.HashSet;
import javax.xml.stream.XMLStreamException;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.jackson.Log4jJsonObjectMapper;
import org.apache.logging.log4j.core.jackson.Log4jXmlObjectMapper;
import org.apache.logging.log4j.core.jackson.Log4jYamlObjectMapper;
import org.codehaus.stax2.XMLStreamWriter2;

abstract class JacksonFactory {
    JacksonFactory() {
    }

    protected abstract String getPropertyNameForTimeMillis();

    protected abstract String getPropertyNameForInstant();

    protected abstract String getPropertNameForContextMap();

    protected abstract String getPropertNameForSource();

    protected abstract String getPropertNameForNanoTime();

    protected abstract PrettyPrinter newCompactPrinter();

    protected abstract ObjectMapper newObjectMapper();

    protected abstract PrettyPrinter newPrettyPrinter();

    ObjectWriter newWriter(boolean locationInfo, boolean properties, boolean compact) {
        return this.newWriter(locationInfo, properties, compact, false);
    }

    ObjectWriter newWriter(boolean locationInfo, boolean properties, boolean compact, boolean includeMillis) {
        SimpleFilterProvider filters = new SimpleFilterProvider();
        HashSet<String> except = new HashSet<String>(3);
        if (!locationInfo) {
            except.add(this.getPropertNameForSource());
        }
        if (!properties) {
            except.add(this.getPropertNameForContextMap());
        }
        if (includeMillis) {
            except.add(this.getPropertyNameForInstant());
        } else {
            except.add(this.getPropertyNameForTimeMillis());
        }
        except.add(this.getPropertNameForNanoTime());
        filters.addFilter(Log4jLogEvent.class.getName(), SimpleBeanPropertyFilter.serializeAllExcept(except));
        ObjectWriter writer = this.newObjectMapper().writer(compact ? this.newCompactPrinter() : this.newPrettyPrinter());
        return writer.with((FilterProvider)filters);
    }

    static class Log4jXmlPrettyPrinter
    extends DefaultXmlPrettyPrinter {
        private static final long serialVersionUID = 1L;

        Log4jXmlPrettyPrinter(int nesting) {
            this._nesting = nesting;
        }

        public void writePrologLinefeed(XMLStreamWriter2 sw) throws XMLStreamException {
        }

        public DefaultXmlPrettyPrinter createInstance() {
            return new Log4jXmlPrettyPrinter(1);
        }
    }

    static class YAML
    extends JacksonFactory {
        private final boolean includeStacktrace;
        private final boolean stacktraceAsString;

        public YAML(boolean includeStacktrace, boolean stacktraceAsString) {
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
            return new MinimalPrettyPrinter();
        }

        @Override
        protected ObjectMapper newObjectMapper() {
            return new Log4jYamlObjectMapper(false, this.includeStacktrace, this.stacktraceAsString);
        }

        @Override
        protected PrettyPrinter newPrettyPrinter() {
            return new DefaultPrettyPrinter();
        }
    }

    static class XML
    extends JacksonFactory {
        static final int DEFAULT_INDENT = 1;
        private final boolean includeStacktrace;
        private final boolean stacktraceAsString;

        public XML(boolean includeStacktrace, boolean stacktraceAsString) {
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
            return new Log4jXmlObjectMapper(this.includeStacktrace, this.stacktraceAsString);
        }

        @Override
        protected PrettyPrinter newPrettyPrinter() {
            return new Log4jXmlPrettyPrinter(1);
        }
    }

    static class JSON
    extends JacksonFactory {
        private final boolean encodeThreadContextAsList;
        private final boolean includeStacktrace;
        private final boolean stacktraceAsString;
        private final boolean objectMessageAsJsonObject;

        public JSON(boolean encodeThreadContextAsList, boolean includeStacktrace, boolean stacktraceAsString, boolean objectMessageAsJsonObject) {
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
            return new MinimalPrettyPrinter();
        }

        @Override
        protected ObjectMapper newObjectMapper() {
            return new Log4jJsonObjectMapper(this.encodeThreadContextAsList, this.includeStacktrace, this.stacktraceAsString, this.objectMessageAsJsonObject);
        }

        @Override
        protected PrettyPrinter newPrettyPrinter() {
            return new DefaultPrettyPrinter();
        }
    }
}

