package org.apache.logging.log4j.core.pattern;

import java.util.concurrent.atomic.AtomicLong;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
   name = "SequenceNumberPatternConverter",
   category = "Converter"
)
@ConverterKeys({"sn", "sequenceNumber"})
public final class SequenceNumberPatternConverter extends LogEventPatternConverter {
   private static final AtomicLong SEQUENCE = new AtomicLong();
   private static final SequenceNumberPatternConverter INSTANCE = new SequenceNumberPatternConverter();

   private SequenceNumberPatternConverter() {
      super("Sequence Number", "sn");
   }

   public static SequenceNumberPatternConverter newInstance(String[] var0) {
      return INSTANCE;
   }

   public void format(LogEvent var1, StringBuilder var2) {
      var2.append(Long.toString(SEQUENCE.incrementAndGet()));
   }
}
