package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.util.PerformanceSensitive;

@Plugin(name = "NdcPatternConverter", category = "Converter")
@ConverterKeys({"x", "NDC"})
public final class NdcPatternConverter extends LogEventPatternConverter {
  private static final NdcPatternConverter INSTANCE = new NdcPatternConverter();
  
  private NdcPatternConverter() {
    super("NDC", "ndc");
  }
  
  public static NdcPatternConverter newInstance(String[] options) {
    return INSTANCE;
  }
  
  @PerformanceSensitive({"allocation"})
  public void format(LogEvent event, StringBuilder toAppendTo) {
    toAppendTo.append(event.getContextStack());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\NdcPatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */