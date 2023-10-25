package org.apache.logging.log4j.core.pattern;

import java.util.concurrent.atomic.AtomicLong;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.util.PerformanceSensitive;

@Plugin(name = "SequenceNumberPatternConverter", category = "Converter")
@ConverterKeys({"sn", "sequenceNumber"})
@PerformanceSensitive({"allocation"})
public final class SequenceNumberPatternConverter extends LogEventPatternConverter {
  private static final AtomicLong SEQUENCE = new AtomicLong();
  
  private static final SequenceNumberPatternConverter INSTANCE = new SequenceNumberPatternConverter();
  
  private SequenceNumberPatternConverter() {
    super("Sequence Number", "sn");
  }
  
  public static SequenceNumberPatternConverter newInstance(String[] options) {
    return INSTANCE;
  }
  
  public void format(LogEvent event, StringBuilder toAppendTo) {
    toAppendTo.append(SEQUENCE.incrementAndGet());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\SequenceNumberPatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */