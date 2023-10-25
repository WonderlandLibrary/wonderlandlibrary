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
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.util.KeyValuePair;

@Plugin(name = "YamlLayout", category = "Core", elementType = "layout", printObject = true)
public final class YamlLayout extends AbstractJacksonLayout {
  private static final String DEFAULT_FOOTER = "";
  
  private static final String DEFAULT_HEADER = "";
  
  static final String CONTENT_TYPE = "application/yaml";
  
  public static class Builder<B extends Builder<B>> extends AbstractJacksonLayout.Builder<B> implements org.apache.logging.log4j.core.util.Builder<YamlLayout> {
    public Builder() {
      setCharset(StandardCharsets.UTF_8);
    }
    
    public YamlLayout build() {
      String headerPattern = toStringOrNull(getHeader());
      String footerPattern = toStringOrNull(getFooter());
      return new YamlLayout(getConfiguration(), isLocationInfo(), isProperties(), isComplete(), 
          isCompact(), getEventEol(), getEndOfLine(), headerPattern, footerPattern, getCharset(), 
          isIncludeStacktrace(), isStacktraceAsString(), isIncludeNullDelimiter(), 
          isIncludeTimeMillis(), getAdditionalFields());
    }
  }
  
  @Deprecated
  protected YamlLayout(Configuration config, boolean locationInfo, boolean properties, boolean complete, boolean compact, boolean eventEol, String headerPattern, String footerPattern, Charset charset, boolean includeStacktrace) {
    super(config, (new JacksonFactory.YAML(includeStacktrace, false)).newWriter(locationInfo, properties, compact), charset, compact, complete, eventEol, (String)null, 
        
        PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(headerPattern).setDefaultPattern("").build(), 
        PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(footerPattern).setDefaultPattern("").build(), false, (KeyValuePair[])null);
  }
  
  private YamlLayout(Configuration config, boolean locationInfo, boolean properties, boolean complete, boolean compact, boolean eventEol, String endOfLine, String headerPattern, String footerPattern, Charset charset, boolean includeStacktrace, boolean stacktraceAsString, boolean includeNullDelimiter, boolean includeTimeMillis, KeyValuePair[] additionalFields) {
    super(config, (new JacksonFactory.YAML(includeStacktrace, stacktraceAsString))
        .newWriter(locationInfo, properties, compact, includeTimeMillis), charset, compact, complete, eventEol, endOfLine, 
        
        PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(headerPattern).setDefaultPattern("").build(), 
        PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(footerPattern).setDefaultPattern("").build(), includeNullDelimiter, additionalFields);
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
    return "application/yaml; charset=" + getCharset();
  }
  
  @Deprecated
  public static AbstractJacksonLayout createLayout(Configuration config, boolean locationInfo, boolean properties, String headerPattern, String footerPattern, Charset charset, boolean includeStacktrace) {
    return new YamlLayout(config, locationInfo, properties, false, false, true, null, headerPattern, footerPattern, charset, includeStacktrace, false, false, false, null);
  }
  
  @PluginBuilderFactory
  public static <B extends Builder<B>> B newBuilder() {
    return (B)(new Builder<>()).asBuilder();
  }
  
  public static AbstractJacksonLayout createDefaultLayout() {
    return new YamlLayout((Configuration)new DefaultConfiguration(), false, false, false, false, false, null, "", "", StandardCharsets.UTF_8, true, false, false, false, null);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\layout\YamlLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */