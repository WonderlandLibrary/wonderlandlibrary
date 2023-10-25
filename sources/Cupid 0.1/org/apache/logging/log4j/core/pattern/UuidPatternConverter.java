package org.apache.logging.log4j.core.pattern;

import java.util.UUID;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.util.UuidUtil;

@Plugin(name = "UuidPatternConverter", category = "Converter")
@ConverterKeys({"u", "uuid"})
public final class UuidPatternConverter extends LogEventPatternConverter {
  private final boolean isRandom;
  
  private UuidPatternConverter(boolean isRandom) {
    super("u", "uuid");
    this.isRandom = isRandom;
  }
  
  public static UuidPatternConverter newInstance(String[] options) {
    if (options.length == 0)
      return new UuidPatternConverter(false); 
    if (options.length > 1 || (!options[0].equalsIgnoreCase("RANDOM") && !options[0].equalsIgnoreCase("Time")))
      LOGGER.error("UUID Pattern Converter only accepts a single option with the value \"RANDOM\" or \"TIME\""); 
    return new UuidPatternConverter(options[0].equalsIgnoreCase("RANDOM"));
  }
  
  public void format(LogEvent event, StringBuilder toAppendTo) {
    UUID uuid = this.isRandom ? UUID.randomUUID() : UuidUtil.getTimeBasedUuid();
    toAppendTo.append(uuid.toString());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\UuidPatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */