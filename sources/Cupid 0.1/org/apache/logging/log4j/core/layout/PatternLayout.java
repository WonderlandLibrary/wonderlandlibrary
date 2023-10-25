package org.apache.logging.log4j.core.layout;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.impl.LocationAware;
import org.apache.logging.log4j.core.pattern.FormattingInfo;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternFormatter;
import org.apache.logging.log4j.core.pattern.PatternParser;
import org.apache.logging.log4j.core.pattern.RegexReplacement;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.util.Strings;

@Plugin(name = "PatternLayout", category = "Core", elementType = "layout", printObject = true)
public final class PatternLayout extends AbstractStringLayout {
  public static final String DEFAULT_CONVERSION_PATTERN = "%m%n";
  
  public static final String TTCC_CONVERSION_PATTERN = "%r [%t] %p %c %notEmpty{%x }- %m%n";
  
  public static final String SIMPLE_CONVERSION_PATTERN = "%d [%t] %p %c - %m%n";
  
  public static final String KEY = "Converter";
  
  private final String conversionPattern;
  
  private final PatternSelector patternSelector;
  
  private final AbstractStringLayout.Serializer eventSerializer;
  
  private PatternLayout(Configuration config, RegexReplacement replace, String eventPattern, PatternSelector patternSelector, Charset charset, boolean alwaysWriteExceptions, boolean disableAnsi, boolean noConsoleNoAnsi, String headerPattern, String footerPattern) {
    super(config, charset, 
        newSerializerBuilder()
        .setConfiguration(config)
        .setReplace(replace)
        .setPatternSelector(patternSelector)
        .setAlwaysWriteExceptions(alwaysWriteExceptions)
        .setDisableAnsi(disableAnsi)
        .setNoConsoleNoAnsi(noConsoleNoAnsi)
        .setPattern(headerPattern)
        .build(), 
        newSerializerBuilder()
        .setConfiguration(config)
        .setReplace(replace)
        .setPatternSelector(patternSelector)
        .setAlwaysWriteExceptions(alwaysWriteExceptions)
        .setDisableAnsi(disableAnsi)
        .setNoConsoleNoAnsi(noConsoleNoAnsi)
        .setPattern(footerPattern)
        .build());
    this.conversionPattern = eventPattern;
    this.patternSelector = patternSelector;
    this
      
      .eventSerializer = newSerializerBuilder().setConfiguration(config).setReplace(replace).setPatternSelector(patternSelector).setAlwaysWriteExceptions(alwaysWriteExceptions).setDisableAnsi(disableAnsi).setNoConsoleNoAnsi(noConsoleNoAnsi).setPattern(eventPattern).setDefaultPattern("%m%n").build();
  }
  
  public static SerializerBuilder newSerializerBuilder() {
    return new SerializerBuilder();
  }
  
  public boolean requiresLocation() {
    return (this.eventSerializer instanceof LocationAware && ((LocationAware)this.eventSerializer).requiresLocation());
  }
  
  @Deprecated
  public static AbstractStringLayout.Serializer createSerializer(Configuration configuration, RegexReplacement replace, String pattern, String defaultPattern, PatternSelector patternSelector, boolean alwaysWriteExceptions, boolean noConsoleNoAnsi) {
    SerializerBuilder builder = newSerializerBuilder();
    builder.setAlwaysWriteExceptions(alwaysWriteExceptions);
    builder.setConfiguration(configuration);
    builder.setDefaultPattern(defaultPattern);
    builder.setNoConsoleNoAnsi(noConsoleNoAnsi);
    builder.setPattern(pattern);
    builder.setPatternSelector(patternSelector);
    builder.setReplace(replace);
    return builder.build();
  }
  
  public String getConversionPattern() {
    return this.conversionPattern;
  }
  
  public Map<String, String> getContentFormat() {
    Map<String, String> result = new HashMap<>();
    result.put("structured", "false");
    result.put("formatType", "conversion");
    result.put("format", this.conversionPattern);
    return result;
  }
  
  public String toSerializable(LogEvent event) {
    return this.eventSerializer.toSerializable(event);
  }
  
  public void serialize(LogEvent event, StringBuilder stringBuilder) {
    this.eventSerializer.toSerializable(event, stringBuilder);
  }
  
  public void encode(LogEvent event, ByteBufferDestination destination) {
    StringBuilder text = toText(this.eventSerializer, event, getStringBuilder());
    Encoder<StringBuilder> encoder = getStringBuilderEncoder();
    encoder.encode(text, destination);
    trimToMaxSize(text);
  }
  
  private StringBuilder toText(AbstractStringLayout.Serializer2 serializer, LogEvent event, StringBuilder destination) {
    return serializer.toSerializable(event, destination);
  }
  
  public static PatternParser createPatternParser(Configuration config) {
    if (config == null)
      return new PatternParser(config, "Converter", LogEventPatternConverter.class); 
    PatternParser parser = (PatternParser)config.getComponent("Converter");
    if (parser == null) {
      parser = new PatternParser(config, "Converter", LogEventPatternConverter.class);
      config.addComponent("Converter", parser);
      parser = (PatternParser)config.getComponent("Converter");
    } 
    return parser;
  }
  
  public String toString() {
    return (this.patternSelector == null) ? this.conversionPattern : this.patternSelector.toString();
  }
  
  @PluginFactory
  @Deprecated
  public static PatternLayout createLayout(@PluginAttribute(value = "pattern", defaultString = "%m%n") String pattern, @PluginElement("PatternSelector") PatternSelector patternSelector, @PluginConfiguration Configuration config, @PluginElement("Replace") RegexReplacement replace, @PluginAttribute("charset") Charset charset, @PluginAttribute(value = "alwaysWriteExceptions", defaultBoolean = true) boolean alwaysWriteExceptions, @PluginAttribute("noConsoleNoAnsi") boolean noConsoleNoAnsi, @PluginAttribute("header") String headerPattern, @PluginAttribute("footer") String footerPattern) {
    return newBuilder()
      .withPattern(pattern)
      .withPatternSelector(patternSelector)
      .withConfiguration(config)
      .withRegexReplacement(replace)
      .withCharset(charset)
      .withAlwaysWriteExceptions(alwaysWriteExceptions)
      .withNoConsoleNoAnsi(noConsoleNoAnsi)
      .withHeader(headerPattern)
      .withFooter(footerPattern)
      .build();
  }
  
  private static interface PatternSerializer extends AbstractStringLayout.Serializer, AbstractStringLayout.Serializer2, LocationAware {}
  
  private static final class NoFormatPatternSerializer implements PatternSerializer {
    private final LogEventPatternConverter[] converters;
    
    private NoFormatPatternSerializer(PatternFormatter[] formatters) {
      this.converters = new LogEventPatternConverter[formatters.length];
      for (int i = 0; i < formatters.length; i++)
        this.converters[i] = formatters[i].getConverter(); 
    }
    
    public String toSerializable(LogEvent event) {
      StringBuilder sb = AbstractStringLayout.getStringBuilder();
      try {
        return toSerializable(event, sb).toString();
      } finally {
        AbstractStringLayout.trimToMaxSize(sb);
      } 
    }
    
    public StringBuilder toSerializable(LogEvent event, StringBuilder buffer) {
      for (LogEventPatternConverter converter : this.converters)
        converter.format(event, buffer); 
      return buffer;
    }
    
    public boolean requiresLocation() {
      for (LogEventPatternConverter converter : this.converters) {
        if (converter instanceof LocationAware && ((LocationAware)converter).requiresLocation())
          return true; 
      } 
      return false;
    }
    
    public String toString() {
      return super.toString() + "[converters=" + Arrays.toString((Object[])this.converters) + "]";
    }
  }
  
  private static final class PatternFormatterPatternSerializer implements PatternSerializer {
    private final PatternFormatter[] formatters;
    
    private PatternFormatterPatternSerializer(PatternFormatter[] formatters) {
      this.formatters = formatters;
    }
    
    public String toSerializable(LogEvent event) {
      StringBuilder sb = AbstractStringLayout.getStringBuilder();
      try {
        return toSerializable(event, sb).toString();
      } finally {
        AbstractStringLayout.trimToMaxSize(sb);
      } 
    }
    
    public StringBuilder toSerializable(LogEvent event, StringBuilder buffer) {
      for (PatternFormatter formatter : this.formatters)
        formatter.format(event, buffer); 
      return buffer;
    }
    
    public boolean requiresLocation() {
      for (PatternFormatter formatter : this.formatters) {
        if (formatter.requiresLocation())
          return true; 
      } 
      return false;
    }
    
    public String toString() {
      return super.toString() + "[formatters=" + 
        
        Arrays.toString((Object[])this.formatters) + "]";
    }
  }
  
  private static final class PatternSerializerWithReplacement implements AbstractStringLayout.Serializer, AbstractStringLayout.Serializer2, LocationAware {
    private final PatternLayout.PatternSerializer delegate;
    
    private final RegexReplacement replace;
    
    private PatternSerializerWithReplacement(PatternLayout.PatternSerializer delegate, RegexReplacement replace) {
      this.delegate = delegate;
      this.replace = replace;
    }
    
    public String toSerializable(LogEvent event) {
      StringBuilder sb = AbstractStringLayout.getStringBuilder();
      try {
        return toSerializable(event, sb).toString();
      } finally {
        AbstractStringLayout.trimToMaxSize(sb);
      } 
    }
    
    public StringBuilder toSerializable(LogEvent event, StringBuilder buf) {
      StringBuilder buffer = this.delegate.toSerializable(event, buf);
      String str = buffer.toString();
      str = this.replace.format(str);
      buffer.setLength(0);
      buffer.append(str);
      return buffer;
    }
    
    public boolean requiresLocation() {
      return this.delegate.requiresLocation();
    }
    
    public String toString() {
      return super.toString() + "[delegate=" + this.delegate + ", replace=" + this.replace + "]";
    }
  }
  
  public static class SerializerBuilder implements org.apache.logging.log4j.core.util.Builder<AbstractStringLayout.Serializer> {
    private Configuration configuration;
    
    private RegexReplacement replace;
    
    private String pattern;
    
    private String defaultPattern;
    
    private PatternSelector patternSelector;
    
    private boolean alwaysWriteExceptions;
    
    private boolean disableAnsi;
    
    private boolean noConsoleNoAnsi;
    
    public AbstractStringLayout.Serializer build() {
      if (Strings.isEmpty(this.pattern) && Strings.isEmpty(this.defaultPattern))
        return null; 
      if (this.patternSelector == null)
        try {
          PatternParser parser = PatternLayout.createPatternParser(this.configuration);
          List<PatternFormatter> list = parser.parse((this.pattern == null) ? this.defaultPattern : this.pattern, this.alwaysWriteExceptions, this.disableAnsi, this.noConsoleNoAnsi);
          PatternFormatter[] formatters = list.<PatternFormatter>toArray(PatternFormatter.EMPTY_ARRAY);
          boolean hasFormattingInfo = false;
          for (PatternFormatter formatter : formatters) {
            FormattingInfo info = formatter.getFormattingInfo();
            if (info != null && info != FormattingInfo.getDefault()) {
              hasFormattingInfo = true;
              break;
            } 
          } 
          PatternLayout.PatternSerializer serializer = hasFormattingInfo ? new PatternLayout.PatternFormatterPatternSerializer(formatters) : new PatternLayout.NoFormatPatternSerializer(formatters);
          return (this.replace == null) ? serializer : new PatternLayout.PatternSerializerWithReplacement(serializer, this.replace);
        } catch (RuntimeException ex) {
          throw new IllegalArgumentException("Cannot parse pattern '" + this.pattern + "'", ex);
        }  
      return new PatternLayout.PatternSelectorSerializer(this.patternSelector, this.replace);
    }
    
    public SerializerBuilder setConfiguration(Configuration configuration) {
      this.configuration = configuration;
      return this;
    }
    
    public SerializerBuilder setReplace(RegexReplacement replace) {
      this.replace = replace;
      return this;
    }
    
    public SerializerBuilder setPattern(String pattern) {
      this.pattern = pattern;
      return this;
    }
    
    public SerializerBuilder setDefaultPattern(String defaultPattern) {
      this.defaultPattern = defaultPattern;
      return this;
    }
    
    public SerializerBuilder setPatternSelector(PatternSelector patternSelector) {
      this.patternSelector = patternSelector;
      return this;
    }
    
    public SerializerBuilder setAlwaysWriteExceptions(boolean alwaysWriteExceptions) {
      this.alwaysWriteExceptions = alwaysWriteExceptions;
      return this;
    }
    
    public SerializerBuilder setDisableAnsi(boolean disableAnsi) {
      this.disableAnsi = disableAnsi;
      return this;
    }
    
    public SerializerBuilder setNoConsoleNoAnsi(boolean noConsoleNoAnsi) {
      this.noConsoleNoAnsi = noConsoleNoAnsi;
      return this;
    }
  }
  
  private static final class PatternSelectorSerializer implements AbstractStringLayout.Serializer, AbstractStringLayout.Serializer2, LocationAware {
    private final PatternSelector patternSelector;
    
    private final RegexReplacement replace;
    
    private PatternSelectorSerializer(PatternSelector patternSelector, RegexReplacement replace) {
      this.patternSelector = patternSelector;
      this.replace = replace;
    }
    
    public String toSerializable(LogEvent event) {
      StringBuilder sb = AbstractStringLayout.getStringBuilder();
      try {
        return toSerializable(event, sb).toString();
      } finally {
        AbstractStringLayout.trimToMaxSize(sb);
      } 
    }
    
    public StringBuilder toSerializable(LogEvent event, StringBuilder buffer) {
      for (PatternFormatter formatter : this.patternSelector.getFormatters(event))
        formatter.format(event, buffer); 
      if (this.replace != null) {
        String str = buffer.toString();
        str = this.replace.format(str);
        buffer.setLength(0);
        buffer.append(str);
      } 
      return buffer;
    }
    
    public boolean requiresLocation() {
      return (this.patternSelector instanceof LocationAware && ((LocationAware)this.patternSelector).requiresLocation());
    }
    
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(super.toString());
      builder.append("[patternSelector=");
      builder.append(this.patternSelector);
      builder.append(", replace=");
      builder.append(this.replace);
      builder.append("]");
      return builder.toString();
    }
  }
  
  public static PatternLayout createDefaultLayout() {
    return newBuilder().build();
  }
  
  public static PatternLayout createDefaultLayout(Configuration configuration) {
    return newBuilder().withConfiguration(configuration).build();
  }
  
  @PluginBuilderFactory
  public static Builder newBuilder() {
    return new Builder();
  }
  
  public static class Builder implements org.apache.logging.log4j.core.util.Builder<PatternLayout> {
    @PluginBuilderAttribute
    private String pattern = "%m%n";
    
    @PluginElement("PatternSelector")
    private PatternSelector patternSelector;
    
    @PluginConfiguration
    private Configuration configuration;
    
    @PluginElement("Replace")
    private RegexReplacement regexReplacement;
    
    @PluginBuilderAttribute
    private Charset charset = Charset.defaultCharset();
    
    @PluginBuilderAttribute
    private boolean alwaysWriteExceptions = true;
    
    @PluginBuilderAttribute
    private boolean disableAnsi = !useAnsiEscapeCodes();
    
    @PluginBuilderAttribute
    private boolean noConsoleNoAnsi;
    
    @PluginBuilderAttribute
    private String header;
    
    @PluginBuilderAttribute
    private String footer;
    
    private boolean useAnsiEscapeCodes() {
      PropertiesUtil propertiesUtil = PropertiesUtil.getProperties();
      boolean isPlatformSupportsAnsi = !propertiesUtil.isOsWindows();
      boolean isJansiRequested = !propertiesUtil.getBooleanProperty("log4j.skipJansi", true);
      return (isPlatformSupportsAnsi || isJansiRequested);
    }
    
    public Builder withPattern(String pattern) {
      this.pattern = pattern;
      return this;
    }
    
    public Builder withPatternSelector(PatternSelector patternSelector) {
      this.patternSelector = patternSelector;
      return this;
    }
    
    public Builder withConfiguration(Configuration configuration) {
      this.configuration = configuration;
      return this;
    }
    
    public Builder withRegexReplacement(RegexReplacement regexReplacement) {
      this.regexReplacement = regexReplacement;
      return this;
    }
    
    public Builder withCharset(Charset charset) {
      if (charset != null)
        this.charset = charset; 
      return this;
    }
    
    public Builder withAlwaysWriteExceptions(boolean alwaysWriteExceptions) {
      this.alwaysWriteExceptions = alwaysWriteExceptions;
      return this;
    }
    
    public Builder withDisableAnsi(boolean disableAnsi) {
      this.disableAnsi = disableAnsi;
      return this;
    }
    
    public Builder withNoConsoleNoAnsi(boolean noConsoleNoAnsi) {
      this.noConsoleNoAnsi = noConsoleNoAnsi;
      return this;
    }
    
    public Builder withHeader(String header) {
      this.header = header;
      return this;
    }
    
    public Builder withFooter(String footer) {
      this.footer = footer;
      return this;
    }
    
    public PatternLayout build() {
      if (this.configuration == null)
        this.configuration = (Configuration)new DefaultConfiguration(); 
      return new PatternLayout(this.configuration, this.regexReplacement, this.pattern, this.patternSelector, this.charset, this.alwaysWriteExceptions, this.disableAnsi, this.noConsoleNoAnsi, this.header, this.footer);
    }
    
    private Builder() {}
  }
  
  public AbstractStringLayout.Serializer getEventSerializer() {
    return this.eventSerializer;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\layout\PatternLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */