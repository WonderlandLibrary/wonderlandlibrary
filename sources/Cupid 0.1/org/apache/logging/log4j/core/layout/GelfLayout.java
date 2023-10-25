package org.apache.logging.log4j.core.layout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.layout.internal.ExcludeChecker;
import org.apache.logging.log4j.core.layout.internal.IncludeChecker;
import org.apache.logging.log4j.core.layout.internal.ListChecker;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.core.net.Severity;
import org.apache.logging.log4j.core.util.JsonUtils;
import org.apache.logging.log4j.core.util.KeyValuePair;
import org.apache.logging.log4j.core.util.NetUtils;
import org.apache.logging.log4j.core.util.Patterns;
import org.apache.logging.log4j.message.MapMessage;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.StringBuilderFormattable;
import org.apache.logging.log4j.util.Strings;
import org.apache.logging.log4j.util.TriConsumer;

@Plugin(name = "GelfLayout", category = "Core", elementType = "layout", printObject = true)
public final class GelfLayout extends AbstractStringLayout {
  private static final char C = ',';
  
  private static final int COMPRESSION_THRESHOLD = 1024;
  
  private static final char Q = '"';
  
  private static final String QC = "\",";
  
  private static final String QU = "\"_";
  
  private final KeyValuePair[] additionalFields;
  
  private final int compressionThreshold;
  
  private final CompressionType compressionType;
  
  private final String host;
  
  private final boolean includeStacktrace;
  
  private final boolean includeThreadContext;
  
  private final boolean includeMapMessage;
  
  private final boolean includeNullDelimiter;
  
  private final boolean includeNewLineDelimiter;
  
  private final boolean omitEmptyFields;
  
  private final PatternLayout layout;
  
  private final FieldWriter mdcWriter;
  
  private final FieldWriter mapWriter;
  
  public enum CompressionType {
    GZIP {
      public DeflaterOutputStream createDeflaterOutputStream(OutputStream os) throws IOException {
        return new GZIPOutputStream(os);
      }
    },
    ZLIB {
      public DeflaterOutputStream createDeflaterOutputStream(OutputStream os) throws IOException {
        return new DeflaterOutputStream(os);
      }
    },
    OFF {
      public DeflaterOutputStream createDeflaterOutputStream(OutputStream os) throws IOException {
        return null;
      }
    };
    
    public abstract DeflaterOutputStream createDeflaterOutputStream(OutputStream param1OutputStream) throws IOException;
  }
  
  public static class Builder<B extends Builder<B>> extends AbstractStringLayout.Builder<B> implements org.apache.logging.log4j.core.util.Builder<GelfLayout> {
    @PluginBuilderAttribute
    private String host;
    
    @PluginElement("AdditionalField")
    private KeyValuePair[] additionalFields;
    
    @PluginBuilderAttribute
    private GelfLayout.CompressionType compressionType = GelfLayout.CompressionType.GZIP;
    
    @PluginBuilderAttribute
    private int compressionThreshold = 1024;
    
    @PluginBuilderAttribute
    private boolean includeStacktrace = true;
    
    @PluginBuilderAttribute
    private boolean includeThreadContext = true;
    
    @PluginBuilderAttribute
    private boolean includeNullDelimiter = false;
    
    @PluginBuilderAttribute
    private boolean includeNewLineDelimiter = false;
    
    @PluginBuilderAttribute
    private String threadContextIncludes = null;
    
    @PluginBuilderAttribute
    private String threadContextExcludes = null;
    
    @PluginBuilderAttribute
    private String mapMessageIncludes = null;
    
    @PluginBuilderAttribute
    private String mapMessageExcludes = null;
    
    @PluginBuilderAttribute
    private boolean includeMapMessage = true;
    
    @PluginBuilderAttribute
    private boolean omitEmptyFields = false;
    
    @PluginBuilderAttribute
    private String messagePattern = null;
    
    @PluginBuilderAttribute
    private String threadContextPrefix = "";
    
    @PluginBuilderAttribute
    private String mapPrefix = "";
    
    @PluginElement("PatternSelector")
    private PatternSelector patternSelector = null;
    
    public Builder() {
      setCharset(StandardCharsets.UTF_8);
    }
    
    public GelfLayout build() {
      ListChecker mdcChecker = createChecker(this.threadContextExcludes, this.threadContextIncludes);
      ListChecker mapChecker = createChecker(this.mapMessageExcludes, this.mapMessageIncludes);
      PatternLayout patternLayout = null;
      if (this.messagePattern != null && this.patternSelector != null) {
        AbstractLayout.LOGGER.error("A message pattern and PatternSelector cannot both be specified on GelfLayout, ignoring message pattern");
        this.messagePattern = null;
      } 
      if (this.messagePattern != null)
        patternLayout = PatternLayout.newBuilder().withPattern(this.messagePattern).withAlwaysWriteExceptions(this.includeStacktrace).withConfiguration(getConfiguration()).build(); 
      if (this.patternSelector != null)
        patternLayout = PatternLayout.newBuilder().withPatternSelector(this.patternSelector).withAlwaysWriteExceptions(this.includeStacktrace).withConfiguration(getConfiguration()).build(); 
      return new GelfLayout(getConfiguration(), this.host, this.additionalFields, this.compressionType, this.compressionThreshold, this.includeStacktrace, this.includeThreadContext, this.includeMapMessage, this.includeNullDelimiter, this.includeNewLineDelimiter, this.omitEmptyFields, mdcChecker, mapChecker, patternLayout, this.threadContextPrefix, this.mapPrefix);
    }
    
    private ListChecker createChecker(String excludes, String includes) {
      IncludeChecker includeChecker;
      ListChecker.NoopChecker noopChecker;
      ListChecker checker = null;
      if (excludes != null) {
        String[] array = excludes.split(Patterns.COMMA_SEPARATOR);
        if (array.length > 0) {
          List<String> excludeList = new ArrayList<>(array.length);
          for (String str : array)
            excludeList.add(str.trim()); 
          ExcludeChecker excludeChecker = new ExcludeChecker(excludeList);
        } 
      } 
      if (includes != null) {
        String[] array = includes.split(Patterns.COMMA_SEPARATOR);
        if (array.length > 0) {
          List<String> includeList = new ArrayList<>(array.length);
          for (String str : array)
            includeList.add(str.trim()); 
          includeChecker = new IncludeChecker(includeList);
        } 
      } 
      if (includeChecker == null)
        noopChecker = ListChecker.NOOP_CHECKER; 
      return (ListChecker)noopChecker;
    }
    
    public String getHost() {
      return this.host;
    }
    
    public GelfLayout.CompressionType getCompressionType() {
      return this.compressionType;
    }
    
    public int getCompressionThreshold() {
      return this.compressionThreshold;
    }
    
    public boolean isIncludeStacktrace() {
      return this.includeStacktrace;
    }
    
    public boolean isIncludeThreadContext() {
      return this.includeThreadContext;
    }
    
    public boolean isIncludeNullDelimiter() {
      return this.includeNullDelimiter;
    }
    
    public boolean isIncludeNewLineDelimiter() {
      return this.includeNewLineDelimiter;
    }
    
    public KeyValuePair[] getAdditionalFields() {
      return this.additionalFields;
    }
    
    public B setHost(String host) {
      this.host = host;
      return asBuilder();
    }
    
    public B setCompressionType(GelfLayout.CompressionType compressionType) {
      this.compressionType = compressionType;
      return asBuilder();
    }
    
    public B setCompressionThreshold(int compressionThreshold) {
      this.compressionThreshold = compressionThreshold;
      return asBuilder();
    }
    
    public B setIncludeStacktrace(boolean includeStacktrace) {
      this.includeStacktrace = includeStacktrace;
      return asBuilder();
    }
    
    public B setIncludeThreadContext(boolean includeThreadContext) {
      this.includeThreadContext = includeThreadContext;
      return asBuilder();
    }
    
    public B setIncludeNullDelimiter(boolean includeNullDelimiter) {
      this.includeNullDelimiter = includeNullDelimiter;
      return asBuilder();
    }
    
    public B setIncludeNewLineDelimiter(boolean includeNewLineDelimiter) {
      this.includeNewLineDelimiter = includeNewLineDelimiter;
      return asBuilder();
    }
    
    public B setAdditionalFields(KeyValuePair[] additionalFields) {
      this.additionalFields = additionalFields;
      return asBuilder();
    }
    
    public B setMessagePattern(String pattern) {
      this.messagePattern = pattern;
      return asBuilder();
    }
    
    public B setPatternSelector(PatternSelector patternSelector) {
      this.patternSelector = patternSelector;
      return asBuilder();
    }
    
    public B setMdcIncludes(String mdcIncludes) {
      this.threadContextIncludes = mdcIncludes;
      return asBuilder();
    }
    
    public B setMdcExcludes(String mdcExcludes) {
      this.threadContextExcludes = mdcExcludes;
      return asBuilder();
    }
    
    public B setIncludeMapMessage(boolean includeMapMessage) {
      this.includeMapMessage = includeMapMessage;
      return asBuilder();
    }
    
    public B setMapMessageIncludes(String mapMessageIncludes) {
      this.mapMessageIncludes = mapMessageIncludes;
      return asBuilder();
    }
    
    public B setMapMessageExcludes(String mapMessageExcludes) {
      this.mapMessageExcludes = mapMessageExcludes;
      return asBuilder();
    }
    
    public B setThreadContextPrefix(String prefix) {
      if (prefix != null)
        this.threadContextPrefix = prefix; 
      return asBuilder();
    }
    
    public B setMapPrefix(String prefix) {
      if (prefix != null)
        this.mapPrefix = prefix; 
      return asBuilder();
    }
  }
  
  @Deprecated
  public GelfLayout(String host, KeyValuePair[] additionalFields, CompressionType compressionType, int compressionThreshold, boolean includeStacktrace) {
    this((Configuration)null, host, additionalFields, compressionType, compressionThreshold, includeStacktrace, true, true, false, false, false, (ListChecker)null, (ListChecker)null, (PatternLayout)null, "", "");
  }
  
  private GelfLayout(Configuration config, String host, KeyValuePair[] additionalFields, CompressionType compressionType, int compressionThreshold, boolean includeStacktrace, boolean includeThreadContext, boolean includeMapMessage, boolean includeNullDelimiter, boolean includeNewLineDelimiter, boolean omitEmptyFields, ListChecker mdcChecker, ListChecker mapChecker, PatternLayout patternLayout, String mdcPrefix, String mapPrefix) {
    super(config, StandardCharsets.UTF_8, (AbstractStringLayout.Serializer)null, (AbstractStringLayout.Serializer)null);
    this.host = (host != null) ? host : NetUtils.getLocalHostname();
    this.additionalFields = (additionalFields != null) ? additionalFields : KeyValuePair.EMPTY_ARRAY;
    if (config == null)
      for (KeyValuePair additionalField : this.additionalFields) {
        if (valueNeedsLookup(additionalField.getValue()))
          throw new IllegalArgumentException("configuration needs to be set when there are additional fields with variables"); 
      }  
    this.compressionType = compressionType;
    this.compressionThreshold = compressionThreshold;
    this.includeStacktrace = includeStacktrace;
    this.includeThreadContext = includeThreadContext;
    this.includeMapMessage = includeMapMessage;
    this.includeNullDelimiter = includeNullDelimiter;
    this.includeNewLineDelimiter = includeNewLineDelimiter;
    this.omitEmptyFields = omitEmptyFields;
    if (includeNullDelimiter && compressionType != CompressionType.OFF)
      throw new IllegalArgumentException("null delimiter cannot be used with compression"); 
    this.mdcWriter = new FieldWriter(mdcChecker, mdcPrefix);
    this.mapWriter = new FieldWriter(mapChecker, mapPrefix);
    this.layout = patternLayout;
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("host=").append(this.host);
    sb.append(", compressionType=").append(this.compressionType.toString());
    sb.append(", compressionThreshold=").append(this.compressionThreshold);
    sb.append(", includeStackTrace=").append(this.includeStacktrace);
    sb.append(", includeThreadContext=").append(this.includeThreadContext);
    sb.append(", includeNullDelimiter=").append(this.includeNullDelimiter);
    sb.append(", includeNewLineDelimiter=").append(this.includeNewLineDelimiter);
    String threadVars = this.mdcWriter.getChecker().toString();
    if (threadVars.length() > 0)
      sb.append(", ").append(threadVars); 
    String mapVars = this.mapWriter.getChecker().toString();
    if (mapVars.length() > 0)
      sb.append(", ").append(mapVars); 
    if (this.layout != null)
      sb.append(", PatternLayout{").append(this.layout.toString()).append("}"); 
    return sb.toString();
  }
  
  @Deprecated
  public static GelfLayout createLayout(@PluginAttribute("host") String host, @PluginElement("AdditionalField") KeyValuePair[] additionalFields, @PluginAttribute(value = "compressionType", defaultString = "GZIP") CompressionType compressionType, @PluginAttribute(value = "compressionThreshold", defaultInt = 1024) int compressionThreshold, @PluginAttribute(value = "includeStacktrace", defaultBoolean = true) boolean includeStacktrace) {
    return new GelfLayout(null, host, additionalFields, compressionType, compressionThreshold, includeStacktrace, true, true, false, false, false, null, null, null, "", "");
  }
  
  @PluginBuilderFactory
  public static <B extends Builder<B>> B newBuilder() {
    return (B)(new Builder<>()).asBuilder();
  }
  
  public Map<String, String> getContentFormat() {
    return Collections.emptyMap();
  }
  
  public String getContentType() {
    return "application/json; charset=" + getCharset();
  }
  
  public byte[] toByteArray(LogEvent event) {
    StringBuilder text = toText(event, getStringBuilder(), false);
    byte[] bytes = getBytes(text.toString());
    return (this.compressionType != CompressionType.OFF && bytes.length > this.compressionThreshold) ? compress(bytes) : bytes;
  }
  
  public void encode(LogEvent event, ByteBufferDestination destination) {
    if (this.compressionType != CompressionType.OFF) {
      super.encode(event, destination);
      return;
    } 
    StringBuilder text = toText(event, getStringBuilder(), true);
    Encoder<StringBuilder> helper = getStringBuilderEncoder();
    helper.encode(text, destination);
  }
  
  public boolean requiresLocation() {
    return (Objects.nonNull(this.layout) && this.layout.requiresLocation());
  }
  
  private byte[] compress(byte[] bytes) {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream(this.compressionThreshold / 8);
      try (DeflaterOutputStream stream = this.compressionType.createDeflaterOutputStream(baos)) {
        if (stream == null)
          return bytes; 
        stream.write(bytes);
        stream.finish();
      } 
      return baos.toByteArray();
    } catch (IOException e) {
      StatusLogger.getLogger().error(e);
      return bytes;
    } 
  }
  
  public String toSerializable(LogEvent event) {
    StringBuilder text = toText(event, getStringBuilder(), false);
    return text.toString();
  }
  
  private StringBuilder toText(LogEvent event, StringBuilder builder, boolean gcFree) {
    builder.append('{');
    builder.append("\"version\":\"1.1\",");
    builder.append("\"host\":\"");
    JsonUtils.quoteAsString(toNullSafeString(this.host), builder);
    builder.append("\",");
    builder.append("\"timestamp\":").append(formatTimestamp(event.getTimeMillis())).append(',');
    builder.append("\"level\":").append(formatLevel(event.getLevel())).append(',');
    if (event.getThreadName() != null) {
      builder.append("\"_thread\":\"");
      JsonUtils.quoteAsString(event.getThreadName(), builder);
      builder.append("\",");
    } 
    if (event.getLoggerName() != null) {
      builder.append("\"_logger\":\"");
      JsonUtils.quoteAsString(event.getLoggerName(), builder);
      builder.append("\",");
    } 
    if (this.additionalFields.length > 0) {
      StrSubstitutor strSubstitutor = getConfiguration().getStrSubstitutor();
      for (KeyValuePair additionalField : this.additionalFields) {
        String value = valueNeedsLookup(additionalField.getValue()) ? strSubstitutor.replace(event, additionalField.getValue()) : additionalField.getValue();
        if (Strings.isNotEmpty(value) || !this.omitEmptyFields) {
          builder.append("\"_");
          JsonUtils.quoteAsString(additionalField.getKey(), builder);
          builder.append("\":\"");
          JsonUtils.quoteAsString(toNullSafeString(value), builder);
          builder.append("\",");
        } 
      } 
    } 
    if (this.includeThreadContext)
      event.getContextData().forEach(this.mdcWriter, builder); 
    if (this.includeMapMessage && event.getMessage() instanceof MapMessage)
      ((MapMessage)event.getMessage()).forEach((key, value) -> this.mapWriter.accept(key, value, builder)); 
    if (event.getThrown() != null || this.layout != null) {
      builder.append("\"full_message\":\"");
      if (this.layout != null) {
        StringBuilder messageBuffer = getMessageStringBuilder();
        this.layout.serialize(event, messageBuffer);
        JsonUtils.quoteAsString(messageBuffer, builder);
      } else if (this.includeStacktrace) {
        JsonUtils.quoteAsString(formatThrowable(event.getThrown()), builder);
      } else {
        JsonUtils.quoteAsString(event.getThrown().toString(), builder);
      } 
      builder.append("\",");
    } 
    builder.append("\"short_message\":\"");
    Message message = event.getMessage();
    if (message instanceof CharSequence) {
      JsonUtils.quoteAsString((CharSequence)message, builder);
    } else if (gcFree && message instanceof StringBuilderFormattable) {
      StringBuilder messageBuffer = getMessageStringBuilder();
      try {
        ((StringBuilderFormattable)message).formatTo(messageBuffer);
        JsonUtils.quoteAsString(messageBuffer, builder);
      } finally {
        trimToMaxSize(messageBuffer);
      } 
    } else {
      JsonUtils.quoteAsString(toNullSafeString(message.getFormattedMessage()), builder);
    } 
    builder.append('"');
    builder.append('}');
    if (this.includeNullDelimiter)
      builder.append(false); 
    if (this.includeNewLineDelimiter)
      builder.append('\n'); 
    return builder;
  }
  
  private static boolean valueNeedsLookup(String value) {
    return (value != null && value.contains("${"));
  }
  
  private class FieldWriter implements TriConsumer<String, Object, StringBuilder> {
    private final ListChecker checker;
    
    private final String prefix;
    
    FieldWriter(ListChecker checker, String prefix) {
      this.checker = checker;
      this.prefix = prefix;
    }
    
    public void accept(String key, Object value, StringBuilder stringBuilder) {
      String stringValue = String.valueOf(value);
      if (this.checker.check(key) && (Strings.isNotEmpty(stringValue) || !GelfLayout.this.omitEmptyFields)) {
        stringBuilder.append("\"_");
        JsonUtils.quoteAsString(Strings.concat(this.prefix, key), stringBuilder);
        stringBuilder.append("\":\"");
        JsonUtils.quoteAsString(GelfLayout.toNullSafeString(stringValue), stringBuilder);
        stringBuilder.append("\",");
      } 
    }
    
    public ListChecker getChecker() {
      return this.checker;
    }
  }
  
  private static final ThreadLocal<StringBuilder> messageStringBuilder = new ThreadLocal<>();
  
  private static StringBuilder getMessageStringBuilder() {
    StringBuilder result = messageStringBuilder.get();
    if (result == null) {
      result = new StringBuilder(1024);
      messageStringBuilder.set(result);
    } 
    result.setLength(0);
    return result;
  }
  
  private static CharSequence toNullSafeString(CharSequence s) {
    return (s == null) ? "" : s;
  }
  
  static CharSequence formatTimestamp(long timeMillis) {
    if (timeMillis < 1000L)
      return "0"; 
    StringBuilder builder = getTimestampStringBuilder();
    builder.append(timeMillis);
    builder.insert(builder.length() - 3, '.');
    return builder;
  }
  
  private static final ThreadLocal<StringBuilder> timestampStringBuilder = new ThreadLocal<>();
  
  private static StringBuilder getTimestampStringBuilder() {
    StringBuilder result = timestampStringBuilder.get();
    if (result == null) {
      result = new StringBuilder(20);
      timestampStringBuilder.set(result);
    } 
    result.setLength(0);
    return result;
  }
  
  private int formatLevel(Level level) {
    return Severity.getSeverity(level).getCode();
  }
  
  static CharSequence formatThrowable(Throwable throwable) {
    StringWriter sw = new StringWriter(2048);
    PrintWriter pw = new PrintWriter(sw);
    throwable.printStackTrace(pw);
    pw.flush();
    return sw.getBuffer();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\layout\GelfLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */