package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.impl.ThrowableProxy;

@Plugin(name = "ExtendedThrowablePatternConverter", category = "Converter")
@ConverterKeys({"xEx", "xThrowable", "xException"})
public final class ExtendedThrowablePatternConverter extends ThrowablePatternConverter {
  private ExtendedThrowablePatternConverter(Configuration config, String[] options) {
    super("ExtendedThrowable", "throwable", options, config);
  }
  
  public static ExtendedThrowablePatternConverter newInstance(Configuration config, String[] options) {
    return new ExtendedThrowablePatternConverter(config, options);
  }
  
  public void format(LogEvent event, StringBuilder toAppendTo) {
    ThrowableProxy proxy = event.getThrownProxy();
    Throwable throwable = event.getThrown();
    if ((throwable != null || proxy != null) && this.options.anyLines()) {
      if (proxy == null) {
        super.format(event, toAppendTo);
        return;
      } 
      int len = toAppendTo.length();
      if (len > 0 && !Character.isWhitespace(toAppendTo.charAt(len - 1)))
        toAppendTo.append(' '); 
      proxy.formatExtendedStackTraceTo(toAppendTo, this.options.getIgnorePackages(), this.options
          .getTextRenderer(), getSuffix(event), this.options.getSeparator());
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\ExtendedThrowablePatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */