package org.apache.logging.log4j.core.pattern;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.util.PerformanceSensitive;

public abstract class AbstractStyleNameConverter extends LogEventPatternConverter {
  private final List<PatternFormatter> formatters;
  
  private final String style;
  
  protected AbstractStyleNameConverter(String name, List<PatternFormatter> formatters, String styling) {
    super(name, "style");
    this.formatters = formatters;
    this.style = styling;
  }
  
  @Plugin(name = "black", category = "Converter")
  @ConverterKeys({"black"})
  public static final class Black extends AbstractStyleNameConverter {
    protected static final String NAME = "black";
    
    public Black(List<PatternFormatter> formatters, String styling) {
      super("black", formatters, styling);
    }
    
    public static Black newInstance(Configuration config, String[] options) {
      return newInstance(Black.class, "black", config, options);
    }
  }
  
  @Plugin(name = "blue", category = "Converter")
  @ConverterKeys({"blue"})
  public static final class Blue extends AbstractStyleNameConverter {
    protected static final String NAME = "blue";
    
    public Blue(List<PatternFormatter> formatters, String styling) {
      super("blue", formatters, styling);
    }
    
    public static Blue newInstance(Configuration config, String[] options) {
      return newInstance(Blue.class, "blue", config, options);
    }
  }
  
  @Plugin(name = "cyan", category = "Converter")
  @ConverterKeys({"cyan"})
  public static final class Cyan extends AbstractStyleNameConverter {
    protected static final String NAME = "cyan";
    
    public Cyan(List<PatternFormatter> formatters, String styling) {
      super("cyan", formatters, styling);
    }
    
    public static Cyan newInstance(Configuration config, String[] options) {
      return newInstance(Cyan.class, "cyan", config, options);
    }
  }
  
  @Plugin(name = "green", category = "Converter")
  @ConverterKeys({"green"})
  public static final class Green extends AbstractStyleNameConverter {
    protected static final String NAME = "green";
    
    public Green(List<PatternFormatter> formatters, String styling) {
      super("green", formatters, styling);
    }
    
    public static Green newInstance(Configuration config, String[] options) {
      return newInstance(Green.class, "green", config, options);
    }
  }
  
  @Plugin(name = "magenta", category = "Converter")
  @ConverterKeys({"magenta"})
  public static final class Magenta extends AbstractStyleNameConverter {
    protected static final String NAME = "magenta";
    
    public Magenta(List<PatternFormatter> formatters, String styling) {
      super("magenta", formatters, styling);
    }
    
    public static Magenta newInstance(Configuration config, String[] options) {
      return newInstance(Magenta.class, "magenta", config, options);
    }
  }
  
  @Plugin(name = "red", category = "Converter")
  @ConverterKeys({"red"})
  public static final class Red extends AbstractStyleNameConverter {
    protected static final String NAME = "red";
    
    public Red(List<PatternFormatter> formatters, String styling) {
      super("red", formatters, styling);
    }
    
    public static Red newInstance(Configuration config, String[] options) {
      return newInstance(Red.class, "red", config, options);
    }
  }
  
  @Plugin(name = "white", category = "Converter")
  @ConverterKeys({"white"})
  public static final class White extends AbstractStyleNameConverter {
    protected static final String NAME = "white";
    
    public White(List<PatternFormatter> formatters, String styling) {
      super("white", formatters, styling);
    }
    
    public static White newInstance(Configuration config, String[] options) {
      return newInstance(White.class, "white", config, options);
    }
  }
  
  @Plugin(name = "yellow", category = "Converter")
  @ConverterKeys({"yellow"})
  public static final class Yellow extends AbstractStyleNameConverter {
    protected static final String NAME = "yellow";
    
    public Yellow(List<PatternFormatter> formatters, String styling) {
      super("yellow", formatters, styling);
    }
    
    public static Yellow newInstance(Configuration config, String[] options) {
      return newInstance(Yellow.class, "yellow", config, options);
    }
  }
  
  protected static <T extends AbstractStyleNameConverter> T newInstance(Class<T> asnConverterClass, String name, Configuration config, String[] options) {
    List<PatternFormatter> formatters = toPatternFormatterList(config, options);
    if (formatters == null)
      return null; 
    try {
      Constructor<T> constructor = asnConverterClass.getConstructor(new Class[] { List.class, String.class });
      return constructor.newInstance(new Object[] { formatters, AnsiEscape.createSequence(new String[] { name }) });
    } catch (SecurityException|NoSuchMethodException|IllegalArgumentException|InstantiationException|IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
      LOGGER.error(e.toString(), e);
      return null;
    } 
  }
  
  private static List<PatternFormatter> toPatternFormatterList(Configuration config, String[] options) {
    if (options.length == 0 || options[0] == null) {
      LOGGER.error("No pattern supplied on style for config=" + config);
      return null;
    } 
    PatternParser parser = PatternLayout.createPatternParser(config);
    if (parser == null) {
      LOGGER.error("No PatternParser created for config=" + config + ", options=" + Arrays.toString((Object[])options));
      return null;
    } 
    return parser.parse(options[0]);
  }
  
  @PerformanceSensitive({"allocation"})
  public void format(LogEvent event, StringBuilder toAppendTo) {
    int start = toAppendTo.length();
    for (int i = 0; i < this.formatters.size(); i++) {
      PatternFormatter formatter = this.formatters.get(i);
      formatter.format(event, toAppendTo);
    } 
    if (toAppendTo.length() > start) {
      toAppendTo.insert(start, this.style);
      toAppendTo.append(AnsiEscape.getDefaultStyle());
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\AbstractStyleNameConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */