package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.util.Strings;

@Plugin(name = "RootThrowablePatternConverter", category = "Converter")
@ConverterKeys({"rEx", "rThrowable", "rException"})
public final class RootThrowablePatternConverter extends ThrowablePatternConverter {
  private RootThrowablePatternConverter(Configuration config, String[] options) {
    super("RootThrowable", "throwable", options, config);
  }
  
  public static RootThrowablePatternConverter newInstance(Configuration config, String[] options) {
    return new RootThrowablePatternConverter(config, options);
  }
  
  public void format(LogEvent event, StringBuilder toAppendTo) {
    ThrowableProxy proxy = event.getThrownProxy();
    Throwable throwable = event.getThrown();
    if (throwable != null && this.options.anyLines()) {
      if (proxy == null) {
        super.format(event, toAppendTo);
        return;
      } 
      String trace = proxy.getCauseStackTraceAsString(this.options.getIgnorePackages(), this.options.getTextRenderer(), getSuffix(event), this.options.getSeparator());
      int len = toAppendTo.length();
      if (len > 0 && !Character.isWhitespace(toAppendTo.charAt(len - 1)))
        toAppendTo.append(' '); 
      if (!this.options.allLines() || !Strings.LINE_SEPARATOR.equals(this.options.getSeparator())) {
        StringBuilder sb = new StringBuilder();
        String[] array = trace.split(Strings.LINE_SEPARATOR);
        int limit = this.options.minLines(array.length) - 1;
        for (int i = 0; i <= limit; i++) {
          sb.append(array[i]);
          if (i < limit)
            sb.append(this.options.getSeparator()); 
        } 
        toAppendTo.append(sb.toString());
      } else {
        toAppendTo.append(trace);
      } 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\RootThrowablePatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */