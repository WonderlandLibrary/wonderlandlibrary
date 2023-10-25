package org.apache.logging.log4j.core.layout;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.StringLayout;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.impl.DefaultLogEventFactory;
import org.apache.logging.log4j.core.impl.LocationAware;
import org.apache.logging.log4j.core.util.Constants;
import org.apache.logging.log4j.core.util.StringEncoder;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.util.StringBuilders;

public abstract class AbstractStringLayout extends AbstractLayout<String> implements StringLayout, LocationAware {
  protected static final int DEFAULT_STRING_BUILDER_SIZE = 1024;
  
  public static abstract class Builder<B extends Builder<B>> extends AbstractLayout.Builder<B> {
    @PluginBuilderAttribute("charset")
    private Charset charset;
    
    @PluginElement("footerSerializer")
    private AbstractStringLayout.Serializer footerSerializer;
    
    @PluginElement("headerSerializer")
    private AbstractStringLayout.Serializer headerSerializer;
    
    public Charset getCharset() {
      return this.charset;
    }
    
    public AbstractStringLayout.Serializer getFooterSerializer() {
      return this.footerSerializer;
    }
    
    public AbstractStringLayout.Serializer getHeaderSerializer() {
      return this.headerSerializer;
    }
    
    public B setCharset(Charset charset) {
      this.charset = charset;
      return asBuilder();
    }
    
    public B setFooterSerializer(AbstractStringLayout.Serializer footerSerializer) {
      this.footerSerializer = footerSerializer;
      return asBuilder();
    }
    
    public B setHeaderSerializer(AbstractStringLayout.Serializer headerSerializer) {
      this.headerSerializer = headerSerializer;
      return asBuilder();
    }
  }
  
  public boolean requiresLocation() {
    return false;
  }
  
  public static interface Serializer extends Serializer2 {
    String toSerializable(LogEvent param1LogEvent);
    
    default StringBuilder toSerializable(LogEvent event, StringBuilder builder) {
      builder.append(toSerializable(event));
      return builder;
    }
  }
  
  protected static final int MAX_STRING_BUILDER_SIZE = Math.max(1024, 
      size("log4j.layoutStringBuilder.maxSize", 2048));
  
  private static final ThreadLocal<StringBuilder> threadLocal = new ThreadLocal<>();
  
  private Encoder<StringBuilder> textEncoder;
  
  private final Charset charset;
  
  private final Serializer footerSerializer;
  
  private final Serializer headerSerializer;
  
  protected static StringBuilder getStringBuilder() {
    if (AbstractLogger.getRecursionDepth() > 1)
      return new StringBuilder(1024); 
    StringBuilder result = threadLocal.get();
    if (result == null) {
      result = new StringBuilder(1024);
      threadLocal.set(result);
    } 
    trimToMaxSize(result);
    result.setLength(0);
    return result;
  }
  
  private static int size(String property, int defaultValue) {
    return PropertiesUtil.getProperties().getIntegerProperty(property, defaultValue);
  }
  
  protected static void trimToMaxSize(StringBuilder stringBuilder) {
    StringBuilders.trimToMaxSize(stringBuilder, MAX_STRING_BUILDER_SIZE);
  }
  
  protected AbstractStringLayout(Charset charset) {
    this(charset, (byte[])null, (byte[])null);
  }
  
  protected AbstractStringLayout(Charset aCharset, byte[] header, byte[] footer) {
    super((Configuration)null, header, footer);
    this.headerSerializer = null;
    this.footerSerializer = null;
    this.charset = (aCharset == null) ? StandardCharsets.UTF_8 : aCharset;
    this.textEncoder = Constants.ENABLE_DIRECT_ENCODERS ? new StringBuilderEncoder(this.charset) : null;
  }
  
  protected AbstractStringLayout(Configuration config, Charset aCharset, Serializer headerSerializer, Serializer footerSerializer) {
    super(config, (byte[])null, (byte[])null);
    this.headerSerializer = headerSerializer;
    this.footerSerializer = footerSerializer;
    this.charset = (aCharset == null) ? StandardCharsets.UTF_8 : aCharset;
    this.textEncoder = Constants.ENABLE_DIRECT_ENCODERS ? new StringBuilderEncoder(this.charset) : null;
  }
  
  protected byte[] getBytes(String s) {
    return s.getBytes(this.charset);
  }
  
  public Charset getCharset() {
    return this.charset;
  }
  
  public String getContentType() {
    return "text/plain";
  }
  
  public byte[] getFooter() {
    return serializeToBytes(this.footerSerializer, super.getFooter());
  }
  
  public Serializer getFooterSerializer() {
    return this.footerSerializer;
  }
  
  public byte[] getHeader() {
    return serializeToBytes(this.headerSerializer, super.getHeader());
  }
  
  public Serializer getHeaderSerializer() {
    return this.headerSerializer;
  }
  
  private DefaultLogEventFactory getLogEventFactory() {
    return DefaultLogEventFactory.getInstance();
  }
  
  protected Encoder<StringBuilder> getStringBuilderEncoder() {
    if (this.textEncoder == null)
      this.textEncoder = new StringBuilderEncoder(getCharset()); 
    return this.textEncoder;
  }
  
  protected byte[] serializeToBytes(Serializer serializer, byte[] defaultValue) {
    String serializable = serializeToString(serializer);
    if (serializable == null)
      return defaultValue; 
    return StringEncoder.toBytes(serializable, getCharset());
  }
  
  protected String serializeToString(Serializer serializer) {
    if (serializer == null)
      return null; 
    LoggerConfig rootLogger = getConfiguration().getRootLogger();
    LogEvent logEvent = getLogEventFactory().createEvent(rootLogger.getName(), null, "", rootLogger
        .getLevel(), null, null, null);
    return serializer.toSerializable(logEvent);
  }
  
  public byte[] toByteArray(LogEvent event) {
    return getBytes((String)toSerializable(event));
  }
  
  public static interface Serializer2 {
    StringBuilder toSerializable(LogEvent param1LogEvent, StringBuilder param1StringBuilder);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\layout\AbstractStringLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */