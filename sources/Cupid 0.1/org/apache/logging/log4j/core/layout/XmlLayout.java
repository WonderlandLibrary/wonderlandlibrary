package org.apache.logging.log4j.core.layout;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.util.KeyValuePair;

@Plugin(name = "XmlLayout", category = "Core", elementType = "layout", printObject = true)
public final class XmlLayout extends AbstractJacksonLayout {
  private static final String ROOT_TAG = "Events";
  
  public static class Builder<B extends Builder<B>> extends AbstractJacksonLayout.Builder<B> implements org.apache.logging.log4j.core.util.Builder<XmlLayout> {
    public Builder() {
      setCharset(StandardCharsets.UTF_8);
    }
    
    public XmlLayout build() {
      return new XmlLayout(getConfiguration(), isLocationInfo(), isProperties(), isComplete(), 
          isCompact(), getEndOfLine(), getCharset(), isIncludeStacktrace(), isStacktraceAsString(), 
          isIncludeNullDelimiter(), isIncludeTimeMillis(), getAdditionalFields());
    }
  }
  
  @Deprecated
  protected XmlLayout(boolean locationInfo, boolean properties, boolean complete, boolean compact, Charset charset, boolean includeStacktrace) {
    this((Configuration)null, locationInfo, properties, complete, compact, (String)null, charset, includeStacktrace, false, false, false, (KeyValuePair[])null);
  }
  
  private XmlLayout(Configuration config, boolean locationInfo, boolean properties, boolean complete, boolean compact, String endOfLine, Charset charset, boolean includeStacktrace, boolean stacktraceAsString, boolean includeNullDelimiter, boolean includeTimeMillis, KeyValuePair[] additionalFields) {
    super(config, (new JacksonFactory.XML(includeStacktrace, stacktraceAsString)).newWriter(locationInfo, properties, compact, includeTimeMillis), charset, compact, complete, false, endOfLine, (AbstractStringLayout.Serializer)null, (AbstractStringLayout.Serializer)null, includeNullDelimiter, additionalFields);
  }
  
  public byte[] getHeader() {
    if (!this.complete)
      return null; 
    StringBuilder buf = new StringBuilder();
    buf.append("<?xml version=\"1.0\" encoding=\"");
    buf.append(getCharset().name());
    buf.append("\"?>");
    buf.append(this.eol);
    buf.append('<');
    buf.append("Events");
    buf.append(" xmlns=\"http://logging.apache.org/log4j/2.0/events\">");
    buf.append(this.eol);
    return buf.toString().getBytes(getCharset());
  }
  
  public byte[] getFooter() {
    if (!this.complete)
      return null; 
    return getBytes("</Events>" + this.eol);
  }
  
  public Map<String, String> getContentFormat() {
    Map<String, String> result = new HashMap<>();
    result.put("xsd", "log4j-events.xsd");
    result.put("version", "2.0");
    return result;
  }
  
  public String getContentType() {
    return "text/xml; charset=" + getCharset();
  }
  
  @Deprecated
  public static XmlLayout createLayout(boolean locationInfo, boolean properties, boolean complete, boolean compact, Charset charset, boolean includeStacktrace) {
    return new XmlLayout(null, locationInfo, properties, complete, compact, null, charset, includeStacktrace, false, false, false, null);
  }
  
  @PluginBuilderFactory
  public static <B extends Builder<B>> B newBuilder() {
    return (B)(new Builder<>()).asBuilder();
  }
  
  public static XmlLayout createDefaultLayout() {
    return new XmlLayout(null, false, false, false, false, null, StandardCharsets.UTF_8, true, false, false, false, null);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\layout\XmlLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */