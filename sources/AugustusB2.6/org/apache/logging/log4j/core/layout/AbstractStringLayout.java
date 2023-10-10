// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.layout;

import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.Property;
import java.util.List;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.util.StringEncoder;
import org.apache.logging.log4j.core.impl.DefaultLogEventFactory;
import org.apache.logging.log4j.core.util.Constants;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.util.StringBuilders;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.spi.AbstractLogger;
import java.nio.charset.Charset;
import org.apache.logging.log4j.core.impl.LocationAware;
import org.apache.logging.log4j.core.StringLayout;

public abstract class AbstractStringLayout extends AbstractLayout<String> implements StringLayout, LocationAware
{
    protected static final int DEFAULT_STRING_BUILDER_SIZE = 1024;
    protected static final int MAX_STRING_BUILDER_SIZE;
    private static final ThreadLocal<StringBuilder> threadLocal;
    private Encoder<StringBuilder> textEncoder;
    private final Charset charset;
    private final Serializer footerSerializer;
    private final Serializer headerSerializer;
    
    @Override
    public boolean requiresLocation() {
        return false;
    }
    
    protected static StringBuilder getStringBuilder() {
        if (AbstractLogger.getRecursionDepth() > 1) {
            return new StringBuilder(1024);
        }
        StringBuilder result = AbstractStringLayout.threadLocal.get();
        if (result == null) {
            result = new StringBuilder(1024);
            AbstractStringLayout.threadLocal.set(result);
        }
        trimToMaxSize(result);
        result.setLength(0);
        return result;
    }
    
    private static int size(final String property, final int defaultValue) {
        return PropertiesUtil.getProperties().getIntegerProperty(property, defaultValue);
    }
    
    protected static void trimToMaxSize(final StringBuilder stringBuilder) {
        StringBuilders.trimToMaxSize(stringBuilder, AbstractStringLayout.MAX_STRING_BUILDER_SIZE);
    }
    
    protected AbstractStringLayout(final Charset charset) {
        this(charset, null, null);
    }
    
    protected AbstractStringLayout(final Charset aCharset, final byte[] header, final byte[] footer) {
        super(null, header, footer);
        this.headerSerializer = null;
        this.footerSerializer = null;
        this.charset = ((aCharset == null) ? StandardCharsets.UTF_8 : aCharset);
        this.textEncoder = (Constants.ENABLE_DIRECT_ENCODERS ? new StringBuilderEncoder(this.charset) : null);
    }
    
    protected AbstractStringLayout(final Configuration config, final Charset aCharset, final Serializer headerSerializer, final Serializer footerSerializer) {
        super(config, null, null);
        this.headerSerializer = headerSerializer;
        this.footerSerializer = footerSerializer;
        this.charset = ((aCharset == null) ? StandardCharsets.UTF_8 : aCharset);
        this.textEncoder = (Constants.ENABLE_DIRECT_ENCODERS ? new StringBuilderEncoder(this.charset) : null);
    }
    
    protected byte[] getBytes(final String s) {
        return s.getBytes(this.charset);
    }
    
    @Override
    public Charset getCharset() {
        return this.charset;
    }
    
    @Override
    public String getContentType() {
        return "text/plain";
    }
    
    @Override
    public byte[] getFooter() {
        return this.serializeToBytes(this.footerSerializer, super.getFooter());
    }
    
    public Serializer getFooterSerializer() {
        return this.footerSerializer;
    }
    
    @Override
    public byte[] getHeader() {
        return this.serializeToBytes(this.headerSerializer, super.getHeader());
    }
    
    public Serializer getHeaderSerializer() {
        return this.headerSerializer;
    }
    
    private DefaultLogEventFactory getLogEventFactory() {
        return DefaultLogEventFactory.getInstance();
    }
    
    protected Encoder<StringBuilder> getStringBuilderEncoder() {
        if (this.textEncoder == null) {
            this.textEncoder = new StringBuilderEncoder(this.getCharset());
        }
        return this.textEncoder;
    }
    
    protected byte[] serializeToBytes(final Serializer serializer, final byte[] defaultValue) {
        final String serializable = this.serializeToString(serializer);
        if (serializable == null) {
            return defaultValue;
        }
        return StringEncoder.toBytes(serializable, this.getCharset());
    }
    
    protected String serializeToString(final Serializer serializer) {
        if (serializer == null) {
            return null;
        }
        final LoggerConfig rootLogger = this.getConfiguration().getRootLogger();
        final LogEvent logEvent = this.getLogEventFactory().createEvent(rootLogger.getName(), null, "", rootLogger.getLevel(), null, null, null);
        return serializer.toSerializable(logEvent);
    }
    
    @Override
    public byte[] toByteArray(final LogEvent event) {
        return this.getBytes(this.toSerializable(event));
    }
    
    static {
        MAX_STRING_BUILDER_SIZE = Math.max(1024, size("log4j.layoutStringBuilder.maxSize", 2048));
        threadLocal = new ThreadLocal<StringBuilder>();
    }
    
    public abstract static class Builder<B extends Builder<B>> extends AbstractLayout.Builder<B>
    {
        @PluginBuilderAttribute("charset")
        private Charset charset;
        @PluginElement("footerSerializer")
        private Serializer footerSerializer;
        @PluginElement("headerSerializer")
        private Serializer headerSerializer;
        
        public Charset getCharset() {
            return this.charset;
        }
        
        public Serializer getFooterSerializer() {
            return this.footerSerializer;
        }
        
        public Serializer getHeaderSerializer() {
            return this.headerSerializer;
        }
        
        public B setCharset(final Charset charset) {
            this.charset = charset;
            return this.asBuilder();
        }
        
        public B setFooterSerializer(final Serializer footerSerializer) {
            this.footerSerializer = footerSerializer;
            return this.asBuilder();
        }
        
        public B setHeaderSerializer(final Serializer headerSerializer) {
            this.headerSerializer = headerSerializer;
            return this.asBuilder();
        }
    }
    
    public interface Serializer extends Serializer2
    {
        String toSerializable(final LogEvent event);
        
        default StringBuilder toSerializable(final LogEvent event, final StringBuilder builder) {
            builder.append(this.toSerializable(event));
            return builder;
        }
    }
    
    public interface Serializer2
    {
        StringBuilder toSerializable(final LogEvent event, final StringBuilder builder);
    }
}
