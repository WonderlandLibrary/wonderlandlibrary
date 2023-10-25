package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.util.PerformanceSensitive;

@Plugin(name = "EndOfBatchPatternConverter", category = "Converter")
@ConverterKeys({"endOfBatch"})
@PerformanceSensitive({"allocation"})
public final class EndOfBatchPatternConverter extends LogEventPatternConverter {
  private static final EndOfBatchPatternConverter INSTANCE = new EndOfBatchPatternConverter();
  
  private EndOfBatchPatternConverter() {
    super("LoggerFqcn", "loggerFqcn");
  }
  
  public static EndOfBatchPatternConverter newInstance(String[] options) {
    return INSTANCE;
  }
  
  public void format(LogEvent event, StringBuilder toAppendTo) {
    toAppendTo.append(event.isEndOfBatch());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\EndOfBatchPatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */