package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.util.PerformanceSensitive;

@Plugin(name = "ThreadPatternConverter", category = "Converter")
@ConverterKeys({"t", "tn", "thread", "threadName"})
@PerformanceSensitive({"allocation"})
public final class ThreadNamePatternConverter extends LogEventPatternConverter {
  private static final ThreadNamePatternConverter INSTANCE = new ThreadNamePatternConverter();
  
  private ThreadNamePatternConverter() {
    super("Thread", "thread");
  }
  
  public static ThreadNamePatternConverter newInstance(String[] options) {
    return INSTANCE;
  }
  
  public void format(LogEvent event, StringBuilder toAppendTo) {
    toAppendTo.append(event.getThreadName());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\ThreadNamePatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */