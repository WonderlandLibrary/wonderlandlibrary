/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.layout;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.layout.AbstractJacksonLayout;
import org.apache.logging.log4j.core.layout.JacksonFactory;
import org.apache.logging.log4j.core.util.KeyValuePair;

@Plugin(name="XmlLayout", category="Core", elementType="layout", printObject=true)
public final class XmlLayout
extends AbstractJacksonLayout {
    private static final String ROOT_TAG = "Events";

    @Deprecated
    protected XmlLayout(boolean locationInfo, boolean properties, boolean complete, boolean compact, Charset charset, boolean includeStacktrace) {
        this(null, locationInfo, properties, complete, compact, null, charset, includeStacktrace, false, false, false, null);
    }

    private XmlLayout(Configuration config, boolean locationInfo, boolean properties, boolean complete, boolean compact, String endOfLine, Charset charset, boolean includeStacktrace, boolean stacktraceAsString, boolean includeNullDelimiter, boolean includeTimeMillis, KeyValuePair[] additionalFields) {
        super(config, new JacksonFactory.XML(includeStacktrace, stacktraceAsString).newWriter(locationInfo, properties, compact, includeTimeMillis), charset, compact, complete, false, endOfLine, null, null, includeNullDelimiter, additionalFields);
    }

    @Override
    public byte[] getHeader() {
        if (!this.complete) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        buf.append("<?xml version=\"1.0\" encoding=\"");
        buf.append(this.getCharset().name());
        buf.append("\"?>");
        buf.append(this.eol);
        buf.append('<');
        buf.append(ROOT_TAG);
        buf.append(" xmlns=\"http://logging.apache.org/log4j/2.0/events\">");
        buf.append(this.eol);
        return buf.toString().getBytes(this.getCharset());
    }

    @Override
    public byte[] getFooter() {
        if (!this.complete) {
            return null;
        }
        return this.getBytes("</Events>" + this.eol);
    }

    @Override
    public Map<String, String> getContentFormat() {
        HashMap<String, String> result = new HashMap<String, String>();
        result.put("xsd", "log4j-events.xsd");
        result.put("version", "2.0");
        return result;
    }

    @Override
    public String getContentType() {
        return "text/xml; charset=" + this.getCharset();
    }

    @Deprecated
    public static XmlLayout createLayout(boolean locationInfo, boolean properties, boolean complete, boolean compact, Charset charset, boolean includeStacktrace) {
        return new XmlLayout(null, locationInfo, properties, complete, compact, null, charset, includeStacktrace, false, false, false, null);
    }

    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return (B)((Builder)new Builder().asBuilder());
    }

    public static XmlLayout createDefaultLayout() {
        return new XmlLayout(null, false, false, false, false, null, StandardCharsets.UTF_8, true, false, false, false, null);
    }

    public static class Builder<B extends Builder<B>>
    extends AbstractJacksonLayout.Builder<B>
    implements org.apache.logging.log4j.core.util.Builder<XmlLayout> {
        public Builder() {
            this.setCharset(StandardCharsets.UTF_8);
        }

        @Override
        public XmlLayout build() {
            return new XmlLayout(this.getConfiguration(), this.isLocationInfo(), this.isProperties(), this.isComplete(), this.isCompact(), this.getEndOfLine(), this.getCharset(), this.isIncludeStacktrace(), this.isStacktraceAsString(), this.isIncludeNullDelimiter(), this.isIncludeTimeMillis(), this.getAdditionalFields());
        }
    }
}

