package org.apache.logging.log4j.core.pattern;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.impl.ThrowableFormatOptions;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.util.StringBuilderWriter;
import org.apache.logging.log4j.util.Strings;

@Plugin(name = "ThrowablePatternConverter", category = "Converter")
@ConverterKeys({"ex", "throwable", "exception"})
public class ThrowablePatternConverter extends LogEventPatternConverter {
  protected final List<PatternFormatter> formatters;
  
  private String rawOption;
  
  private final boolean subShortOption;
  
  private final boolean nonStandardLineSeparator;
  
  protected final ThrowableFormatOptions options;
  
  @Deprecated
  protected ThrowablePatternConverter(String name, String style, String[] options) {
    this(name, style, options, null);
  }
  
  protected ThrowablePatternConverter(String name, String style, String[] options, Configuration config) {
    super(name, style);
    this.options = ThrowableFormatOptions.newInstance(options);
    if (options != null && options.length > 0)
      this.rawOption = options[0]; 
    if (this.options.getSuffix() != null) {
      PatternParser parser = PatternLayout.createPatternParser(config);
      List<PatternFormatter> parsedSuffixFormatters = parser.parse(this.options.getSuffix());
      boolean hasThrowableSuffixFormatter = false;
      for (PatternFormatter suffixFormatter : parsedSuffixFormatters) {
        if (suffixFormatter.handlesThrowable())
          hasThrowableSuffixFormatter = true; 
      } 
      if (!hasThrowableSuffixFormatter) {
        this.formatters = parsedSuffixFormatters;
      } else {
        List<PatternFormatter> suffixFormatters = new ArrayList<>();
        for (PatternFormatter suffixFormatter : parsedSuffixFormatters) {
          if (!suffixFormatter.handlesThrowable())
            suffixFormatters.add(suffixFormatter); 
        } 
        this.formatters = suffixFormatters;
      } 
    } else {
      this.formatters = Collections.emptyList();
    } 
    this
      
      .subShortOption = ("short.message".equalsIgnoreCase(this.rawOption) || "short.localizedMessage".equalsIgnoreCase(this.rawOption) || "short.fileName".equalsIgnoreCase(this.rawOption) || "short.lineNumber".equalsIgnoreCase(this.rawOption) || "short.methodName".equalsIgnoreCase(this.rawOption) || "short.className".equalsIgnoreCase(this.rawOption));
    this.nonStandardLineSeparator = !Strings.LINE_SEPARATOR.equals(this.options.getSeparator());
  }
  
  public static ThrowablePatternConverter newInstance(Configuration config, String[] options) {
    return new ThrowablePatternConverter("Throwable", "throwable", options, config);
  }
  
  public void format(LogEvent event, StringBuilder buffer) {
    Throwable t = event.getThrown();
    if (this.subShortOption) {
      formatSubShortOption(t, getSuffix(event), buffer);
    } else if (t != null && this.options.anyLines()) {
      formatOption(t, getSuffix(event), buffer);
    } 
  }
  
  private void formatSubShortOption(Throwable t, String suffix, StringBuilder buffer) {
    StackTraceElement throwingMethod = null;
    if (t != null) {
      StackTraceElement[] trace = t.getStackTrace();
      if (trace != null && trace.length > 0)
        throwingMethod = trace[0]; 
    } 
    if (t != null && throwingMethod != null) {
      String toAppend = "";
      if ("short.className".equalsIgnoreCase(this.rawOption)) {
        toAppend = throwingMethod.getClassName();
      } else if ("short.methodName".equalsIgnoreCase(this.rawOption)) {
        toAppend = throwingMethod.getMethodName();
      } else if ("short.lineNumber".equalsIgnoreCase(this.rawOption)) {
        toAppend = String.valueOf(throwingMethod.getLineNumber());
      } else if ("short.message".equalsIgnoreCase(this.rawOption)) {
        toAppend = t.getMessage();
      } else if ("short.localizedMessage".equalsIgnoreCase(this.rawOption)) {
        toAppend = t.getLocalizedMessage();
      } else if ("short.fileName".equalsIgnoreCase(this.rawOption)) {
        toAppend = throwingMethod.getFileName();
      } 
      int len = buffer.length();
      if (len > 0 && !Character.isWhitespace(buffer.charAt(len - 1)))
        buffer.append(' '); 
      buffer.append(toAppend);
      if (Strings.isNotBlank(suffix)) {
        buffer.append(' ');
        buffer.append(suffix);
      } 
    } 
  }
  
  private void formatOption(Throwable throwable, String suffix, StringBuilder buffer) {
    int len = buffer.length();
    if (len > 0 && !Character.isWhitespace(buffer.charAt(len - 1)))
      buffer.append(' '); 
    if (!this.options.allLines() || this.nonStandardLineSeparator || Strings.isNotBlank(suffix)) {
      StringWriter w = new StringWriter();
      throwable.printStackTrace(new PrintWriter(w));
      String[] array = w.toString().split(Strings.LINE_SEPARATOR);
      int limit = this.options.minLines(array.length) - 1;
      boolean suffixNotBlank = Strings.isNotBlank(suffix);
      for (int i = 0; i <= limit; i++) {
        buffer.append(array[i]);
        if (suffixNotBlank) {
          buffer.append(' ');
          buffer.append(suffix);
        } 
        if (i < limit)
          buffer.append(this.options.getSeparator()); 
      } 
    } else {
      throwable.printStackTrace(new PrintWriter((Writer)new StringBuilderWriter(buffer)));
    } 
  }
  
  public boolean handlesThrowable() {
    return true;
  }
  
  protected String getSuffix(LogEvent event) {
    if (this.formatters.isEmpty())
      return ""; 
    StringBuilder toAppendTo = new StringBuilder();
    for (int i = 0, size = this.formatters.size(); i < size; i++)
      ((PatternFormatter)this.formatters.get(i)).format(event, toAppendTo); 
    return toAppendTo.toString();
  }
  
  public ThrowableFormatOptions getOptions() {
    return this.options;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\ThrowablePatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */