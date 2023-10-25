package org.apache.logging.log4j.core.pattern;

import java.util.List;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.util.EnglishEnums;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.util.StringBuilders;

@Plugin(name = "encode", category = "Converter")
@ConverterKeys({"enc", "encode"})
@PerformanceSensitive({"allocation"})
public final class EncodingPatternConverter extends LogEventPatternConverter {
  private final List<PatternFormatter> formatters;
  
  private final EscapeFormat escapeFormat;
  
  private EncodingPatternConverter(List<PatternFormatter> formatters, EscapeFormat escapeFormat) {
    super("encode", "encode");
    this.formatters = formatters;
    this.escapeFormat = escapeFormat;
  }
  
  public boolean handlesThrowable() {
    return (this.formatters != null && this.formatters.stream()
      .map(PatternFormatter::getConverter)
      .anyMatch(LogEventPatternConverter::handlesThrowable));
  }
  
  public static EncodingPatternConverter newInstance(Configuration config, String[] options) {
    if (options.length > 2 || options.length == 0) {
      LOGGER.error("Incorrect number of options on escape. Expected 1 or 2, but received {}", 
          Integer.valueOf(options.length));
      return null;
    } 
    if (options[0] == null) {
      LOGGER.error("No pattern supplied on escape");
      return null;
    } 
    EscapeFormat escapeFormat = (options.length < 2) ? EscapeFormat.HTML : (EscapeFormat)EnglishEnums.valueOf(EscapeFormat.class, options[1], EscapeFormat.HTML);
    PatternParser parser = PatternLayout.createPatternParser(config);
    List<PatternFormatter> formatters = parser.parse(options[0]);
    return new EncodingPatternConverter(formatters, escapeFormat);
  }
  
  public void format(LogEvent event, StringBuilder toAppendTo) {
    int start = toAppendTo.length();
    for (int i = 0; i < this.formatters.size(); i++)
      ((PatternFormatter)this.formatters.get(i)).format(event, toAppendTo); 
    this.escapeFormat.escape(toAppendTo, start);
  }
  
  private enum EscapeFormat {
    HTML {
      void escape(StringBuilder toAppendTo, int start) {
        int origLength = toAppendTo.length();
        int firstSpecialChar = origLength;
        int i;
        for (i = origLength - 1; i >= start; i--) {
          char c = toAppendTo.charAt(i);
          String escaped = escapeChar(c);
          if (escaped != null) {
            firstSpecialChar = i;
            for (int k = 0; k < escaped.length() - 1; k++)
              toAppendTo.append(' '); 
          } 
        } 
        int j;
        for (i = origLength - 1, j = toAppendTo.length(); i >= firstSpecialChar; i--) {
          char c = toAppendTo.charAt(i);
          String escaped = escapeChar(c);
          if (escaped == null) {
            toAppendTo.setCharAt(--j, c);
          } else {
            toAppendTo.replace(j - escaped.length(), j, escaped);
            j -= escaped.length();
          } 
        } 
      }
      
      private String escapeChar(char c) {
        switch (c) {
          case '\r':
            return "\\r";
          case '\n':
            return "\\n";
          case '&':
            return "&amp;";
          case '<':
            return "&lt;";
          case '>':
            return "&gt;";
          case '"':
            return "&quot;";
          case '\'':
            return "&apos;";
          case '/':
            return "&#x2F;";
        } 
        return null;
      }
    },
    JSON {
      void escape(StringBuilder toAppendTo, int start) {
        StringBuilders.escapeJson(toAppendTo, start);
      }
    },
    CRLF {
      void escape(StringBuilder toAppendTo, int start) {
        int origLength = toAppendTo.length();
        int firstSpecialChar = origLength;
        int i;
        for (i = origLength - 1; i >= start; i--) {
          char c = toAppendTo.charAt(i);
          if (c == '\r' || c == '\n') {
            firstSpecialChar = i;
            toAppendTo.append(' ');
          } 
        } 
        int j;
        for (i = origLength - 1, j = toAppendTo.length(); i >= firstSpecialChar; i--) {
          char c = toAppendTo.charAt(i);
          switch (c) {
            case '\r':
              toAppendTo.setCharAt(--j, 'r');
              toAppendTo.setCharAt(--j, '\\');
              break;
            case '\n':
              toAppendTo.setCharAt(--j, 'n');
              toAppendTo.setCharAt(--j, '\\');
              break;
            default:
              toAppendTo.setCharAt(--j, c);
              break;
          } 
        } 
      }
    },
    XML {
      void escape(StringBuilder toAppendTo, int start) {
        StringBuilders.escapeXml(toAppendTo, start);
      }
    };
    
    abstract void escape(StringBuilder param1StringBuilder, int param1Int);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\EncodingPatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */