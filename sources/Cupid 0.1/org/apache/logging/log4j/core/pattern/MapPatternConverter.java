package org.apache.logging.log4j.core.pattern;

import java.util.Objects;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.message.MapMessage;

@Plugin(name = "MapPatternConverter", category = "Converter")
@ConverterKeys({"K", "map", "MAP"})
public final class MapPatternConverter extends LogEventPatternConverter {
  private static final String JAVA_UNQUOTED = MapMessage.MapFormat.JAVA_UNQUOTED.name();
  
  private final String key;
  
  private final String[] format;
  
  private MapPatternConverter(String[] options, String... format) {
    super((options != null && options.length > 0) ? ("MAP{" + options[0] + '}') : "MAP", "map");
    this.key = (options != null && options.length > 0) ? options[0] : null;
    this.format = format;
  }
  
  public static MapPatternConverter newInstance(String[] options) {
    return new MapPatternConverter(options, new String[] { JAVA_UNQUOTED });
  }
  
  public static MapPatternConverter newInstance(String[] options, MapMessage.MapFormat format) {
    return new MapPatternConverter(options, new String[] { Objects.toString(format, JAVA_UNQUOTED) });
  }
  
  public void format(LogEvent event, StringBuilder toAppendTo) {
    MapMessage msg;
    if (event.getMessage() instanceof MapMessage) {
      msg = (MapMessage)event.getMessage();
    } else {
      return;
    } 
    if (this.key == null) {
      msg.formatTo(this.format, toAppendTo);
    } else {
      String val = msg.get(this.key);
      if (val != null)
        toAppendTo.append(val); 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\MapPatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */