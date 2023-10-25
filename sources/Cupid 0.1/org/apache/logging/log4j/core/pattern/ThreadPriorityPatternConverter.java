package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.util.PerformanceSensitive;

@Plugin(name = "ThreadPriorityPatternConverter", category = "Converter")
@ConverterKeys({"tp", "threadPriority"})
@PerformanceSensitive({"allocation"})
public final class ThreadPriorityPatternConverter extends LogEventPatternConverter {
  private static final ThreadPriorityPatternConverter INSTANCE = new ThreadPriorityPatternConverter();
  
  private ThreadPriorityPatternConverter() {
    super("ThreadPriority", "threadPriority");
  }
  
  public static ThreadPriorityPatternConverter newInstance(String[] options) {
    return INSTANCE;
  }
  
  public void format(LogEvent event, StringBuilder toAppendTo) {
    toAppendTo.append(event.getThreadPriority());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\ThreadPriorityPatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */