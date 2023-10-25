package org.apache.logging.log4j.core.layout;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.util.KeyValuePair;

@Plugin(name = "JsonLayout", category = "Core", elementType = "layout", printObject = true)
public final class JsonLayout extends AbstractJacksonLayout {
  private static final String DEFAULT_FOOTER = "]";
  
  private static final String DEFAULT_HEADER = "[";
  
  static final String CONTENT_TYPE = "application/json";
  
  public static class Builder<B extends Builder<B>> extends AbstractJacksonLayout.Builder<B> implements org.apache.logging.log4j.core.util.Builder<JsonLayout> {
    @PluginBuilderAttribute
    private boolean propertiesAsList;
    
    @PluginBuilderAttribute
    private boolean objectMessageAsJsonObject;
    
    @PluginElement("AdditionalField")
    private KeyValuePair[] additionalFields;
    
    public Builder() {
      setCharset(StandardCharsets.UTF_8);
    }
    
    public JsonLayout build() {
      boolean encodeThreadContextAsList = (isProperties() && this.propertiesAsList);
      String headerPattern = toStringOrNull(getHeader());
      String footerPattern = toStringOrNull(getFooter());
      return new JsonLayout(getConfiguration(), isLocationInfo(), isProperties(), encodeThreadContextAsList, 
          isComplete(), isCompact(), getEventEol(), getEndOfLine(), headerPattern, footerPattern, getCharset(), 
          isIncludeStacktrace(), isStacktraceAsString(), isIncludeNullDelimiter(), isIncludeTimeMillis(), 
          getAdditionalFields(), getObjectMessageAsJsonObject());
    }
    
    public boolean isPropertiesAsList() {
      return this.propertiesAsList;
    }
    
    public B setPropertiesAsList(boolean propertiesAsList) {
      this.propertiesAsList = propertiesAsList;
      return asBuilder();
    }
    
    public boolean getObjectMessageAsJsonObject() {
      return this.objectMessageAsJsonObject;
    }
    
    public B setObjectMessageAsJsonObject(boolean objectMessageAsJsonObject) {
      this.objectMessageAsJsonObject = objectMessageAsJsonObject;
      return asBuilder();
    }
    
    public KeyValuePair[] getAdditionalFields() {
      return this.additionalFields;
    }
    
    public B setAdditionalFields(KeyValuePair[] additionalFields) {
      this.additionalFields = additionalFields;
      return asBuilder();
    }
  }
  
  @Deprecated
  protected JsonLayout(Configuration config, boolean locationInfo, boolean properties, boolean encodeThreadContextAsList, boolean complete, boolean compact, boolean eventEol, String endOfLine, String headerPattern, String footerPattern, Charset charset, boolean includeStacktrace) {
    super(config, (new JacksonFactory.JSON(encodeThreadContextAsList, includeStacktrace, false, false)).newWriter(locationInfo, properties, compact), charset, compact, complete, eventEol, endOfLine, 
        
        PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(headerPattern).setDefaultPattern("[").build(), 
        PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(footerPattern).setDefaultPattern("]").build(), false, (KeyValuePair[])null);
  }
  
  private JsonLayout(Configuration config, boolean locationInfo, boolean properties, boolean encodeThreadContextAsList, boolean complete, boolean compact, boolean eventEol, String endOfLine, String headerPattern, String footerPattern, Charset charset, boolean includeStacktrace, boolean stacktraceAsString, boolean includeNullDelimiter, boolean includeTimeMillis, KeyValuePair[] additionalFields, boolean objectMessageAsJsonObject) {
    super(config, (new JacksonFactory.JSON(encodeThreadContextAsList, includeStacktrace, stacktraceAsString, objectMessageAsJsonObject)).newWriter(locationInfo, properties, compact, includeTimeMillis), charset, compact, complete, eventEol, endOfLine, 
        
        PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(headerPattern).setDefaultPattern("[").build(), 
        PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(footerPattern).setDefaultPattern("]").build(), includeNullDelimiter, additionalFields);
  }
  
  public byte[] getHeader() {
    if (!this.complete)
      return null; 
    StringBuilder buf = new StringBuilder();
    String str = serializeToString(getHeaderSerializer());
    if (str != null)
      buf.append(str); 
    buf.append(this.eol);
    return getBytes(buf.toString());
  }
  
  public byte[] getFooter() {
    if (!this.complete)
      return null; 
    StringBuilder buf = new StringBuilder();
    buf.append(this.eol);
    String str = serializeToString(getFooterSerializer());
    if (str != null)
      buf.append(str); 
    buf.append(this.eol);
    return getBytes(buf.toString());
  }
  
  public Map<String, String> getContentFormat() {
    Map<String, String> result = new HashMap<>();
    result.put("version", "2.0");
    return result;
  }
  
  public String getContentType() {
    return "application/json; charset=" + getCharset();
  }
  
  @Deprecated
  public static JsonLayout createLayout(Configuration config, boolean locationInfo, boolean properties, boolean propertiesAsList, boolean complete, boolean compact, boolean eventEol, String headerPattern, String footerPattern, Charset charset, boolean includeStacktrace) {
    boolean encodeThreadContextAsList = (properties && propertiesAsList);
    return new JsonLayout(config, locationInfo, properties, encodeThreadContextAsList, complete, compact, eventEol, null, headerPattern, footerPattern, charset, includeStacktrace, false, false, false, null, false);
  }
  
  @PluginBuilderFactory
  public static <B extends Builder<B>> B newBuilder() {
    return (B)(new Builder<>()).asBuilder();
  }
  
  public static JsonLayout createDefaultLayout() {
    return new JsonLayout((Configuration)new DefaultConfiguration(), false, false, false, false, false, false, null, "[", "]", StandardCharsets.UTF_8, true, false, false, false, null, false);
  }
  
  public void toSerializable(LogEvent event, Writer writer) throws IOException {
    if (this.complete && this.eventCount > 0L)
      writer.append(", "); 
    super.toSerializable(event, writer);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\layout\JsonLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */