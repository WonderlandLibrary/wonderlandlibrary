// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.layout;

import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.util.Builder;
import java.io.IOException;
import java.io.Writer;
import org.apache.logging.log4j.core.LogEvent;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.core.util.KeyValuePair;
import java.nio.charset.Charset;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "JsonLayout", category = "Core", elementType = "layout", printObject = true)
public final class JsonLayout extends AbstractJacksonLayout
{
    private static final String DEFAULT_FOOTER = "]";
    private static final String DEFAULT_HEADER = "[";
    static final String CONTENT_TYPE = "application/json";
    
    @Deprecated
    protected JsonLayout(final Configuration config, final boolean locationInfo, final boolean properties, final boolean encodeThreadContextAsList, final boolean complete, final boolean compact, final boolean eventEol, final String endOfLine, final String headerPattern, final String footerPattern, final Charset charset, final boolean includeStacktrace) {
        super(config, new JacksonFactory.JSON(encodeThreadContextAsList, includeStacktrace, false, false).newWriter(locationInfo, properties, compact), charset, compact, complete, eventEol, endOfLine, PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(headerPattern).setDefaultPattern("[").build(), PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(footerPattern).setDefaultPattern("]").build(), false, null);
    }
    
    private JsonLayout(final Configuration config, final boolean locationInfo, final boolean properties, final boolean encodeThreadContextAsList, final boolean complete, final boolean compact, final boolean eventEol, final String endOfLine, final String headerPattern, final String footerPattern, final Charset charset, final boolean includeStacktrace, final boolean stacktraceAsString, final boolean includeNullDelimiter, final boolean includeTimeMillis, final KeyValuePair[] additionalFields, final boolean objectMessageAsJsonObject) {
        super(config, new JacksonFactory.JSON(encodeThreadContextAsList, includeStacktrace, stacktraceAsString, objectMessageAsJsonObject).newWriter(locationInfo, properties, compact, includeTimeMillis), charset, compact, complete, eventEol, endOfLine, PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(headerPattern).setDefaultPattern("[").build(), PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(footerPattern).setDefaultPattern("]").build(), includeNullDelimiter, additionalFields);
    }
    
    @Override
    public byte[] getHeader() {
        if (!this.complete) {
            return null;
        }
        final StringBuilder buf = new StringBuilder();
        final String str = this.serializeToString(this.getHeaderSerializer());
        if (str != null) {
            buf.append(str);
        }
        buf.append(this.eol);
        return this.getBytes(buf.toString());
    }
    
    @Override
    public byte[] getFooter() {
        if (!this.complete) {
            return null;
        }
        final StringBuilder buf = new StringBuilder();
        buf.append(this.eol);
        final String str = this.serializeToString(this.getFooterSerializer());
        if (str != null) {
            buf.append(str);
        }
        buf.append(this.eol);
        return this.getBytes(buf.toString());
    }
    
    @Override
    public Map<String, String> getContentFormat() {
        final Map<String, String> result = new HashMap<String, String>();
        result.put("version", "2.0");
        return result;
    }
    
    @Override
    public String getContentType() {
        return "application/json; charset=" + this.getCharset();
    }
    
    @Deprecated
    public static JsonLayout createLayout(final Configuration config, final boolean locationInfo, final boolean properties, final boolean propertiesAsList, final boolean complete, final boolean compact, final boolean eventEol, final String headerPattern, final String footerPattern, final Charset charset, final boolean includeStacktrace) {
        final boolean encodeThreadContextAsList = properties && propertiesAsList;
        return new JsonLayout(config, locationInfo, properties, encodeThreadContextAsList, complete, compact, eventEol, null, headerPattern, footerPattern, charset, includeStacktrace, false, false, false, null, false);
    }
    
    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return new Builder<B>().asBuilder();
    }
    
    public static JsonLayout createDefaultLayout() {
        return new JsonLayout(new DefaultConfiguration(), false, false, false, false, false, false, null, "[", "]", StandardCharsets.UTF_8, true, false, false, false, null, false);
    }
    
    @Override
    public void toSerializable(final LogEvent event, final Writer writer) throws IOException {
        if (this.complete && this.eventCount > 0L) {
            writer.append((CharSequence)", ");
        }
        super.toSerializable(event, writer);
    }
    
    public static class Builder<B extends Builder<B>> extends AbstractJacksonLayout.Builder<B> implements org.apache.logging.log4j.core.util.Builder<JsonLayout>
    {
        @PluginBuilderAttribute
        private boolean propertiesAsList;
        @PluginBuilderAttribute
        private boolean objectMessageAsJsonObject;
        @PluginElement("AdditionalField")
        private KeyValuePair[] additionalFields;
        
        public Builder() {
            this.setCharset(StandardCharsets.UTF_8);
        }
        
        @Override
        public JsonLayout build() {
            final boolean encodeThreadContextAsList = this.isProperties() && this.propertiesAsList;
            final String headerPattern = this.toStringOrNull(this.getHeader());
            final String footerPattern = this.toStringOrNull(this.getFooter());
            return new JsonLayout(this.getConfiguration(), this.isLocationInfo(), this.isProperties(), encodeThreadContextAsList, this.isComplete(), this.isCompact(), this.getEventEol(), this.getEndOfLine(), headerPattern, footerPattern, this.getCharset(), this.isIncludeStacktrace(), this.isStacktraceAsString(), this.isIncludeNullDelimiter(), this.isIncludeTimeMillis(), this.getAdditionalFields(), this.getObjectMessageAsJsonObject(), null);
        }
        
        public boolean isPropertiesAsList() {
            return this.propertiesAsList;
        }
        
        public B setPropertiesAsList(final boolean propertiesAsList) {
            this.propertiesAsList = propertiesAsList;
            return this.asBuilder();
        }
        
        public boolean getObjectMessageAsJsonObject() {
            return this.objectMessageAsJsonObject;
        }
        
        public B setObjectMessageAsJsonObject(final boolean objectMessageAsJsonObject) {
            this.objectMessageAsJsonObject = objectMessageAsJsonObject;
            return this.asBuilder();
        }
        
        @Override
        public KeyValuePair[] getAdditionalFields() {
            return this.additionalFields;
        }
        
        @Override
        public B setAdditionalFields(final KeyValuePair[] additionalFields) {
            this.additionalFields = additionalFields;
            return this.asBuilder();
        }
    }
}
