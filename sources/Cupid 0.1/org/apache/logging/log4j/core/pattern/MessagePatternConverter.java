package org.apache.logging.log4j.core.pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MultiformatMessage;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.MultiFormatStringBuilderFormattable;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.util.StringBuilderFormattable;

@Plugin(name = "MessagePatternConverter", category = "Converter")
@ConverterKeys({"m", "msg", "message"})
@PerformanceSensitive({"allocation"})
public class MessagePatternConverter extends LogEventPatternConverter {
  private static final String LOOKUPS = "lookups";
  
  private static final String NOLOOKUPS = "nolookups";
  
  private MessagePatternConverter() {
    super("Message", "message");
  }
  
  private static TextRenderer loadMessageRenderer(String[] options) {
    if (options != null)
      for (String option : options) {
        switch (option.toUpperCase(Locale.ROOT)) {
          case "ANSI":
            if (Loader.isJansiAvailable())
              return new JAnsiTextRenderer(options, JAnsiTextRenderer.DefaultMessageStyleMap); 
            StatusLogger.getLogger()
              .warn("You requested ANSI message rendering but JANSI is not on the classpath.");
            return null;
          case "HTML":
            return new HtmlTextRenderer(options);
        } 
      }  
    return null;
  }
  
  public static MessagePatternConverter newInstance(Configuration config, String[] options) {
    String[] formats = withoutLookupOptions(options);
    TextRenderer textRenderer = loadMessageRenderer(formats);
    MessagePatternConverter result = (formats == null || formats.length == 0) ? SimpleMessagePatternConverter.INSTANCE : new FormattedMessagePatternConverter(formats);
    if (textRenderer != null)
      result = new RenderingPatternConverter(result, textRenderer); 
    return result;
  }
  
  private static String[] withoutLookupOptions(String[] options) {
    if (options == null || options.length == 0)
      return options; 
    List<String> results = new ArrayList<>(options.length);
    for (String option : options) {
      if ("lookups".equalsIgnoreCase(option) || "nolookups".equalsIgnoreCase(option)) {
        LOGGER.info("The {} option will be ignored. Message Lookups are no longer supported.", option);
      } else {
        results.add(option);
      } 
    } 
    return results.<String>toArray(new String[0]);
  }
  
  public void format(LogEvent event, StringBuilder toAppendTo) {
    throw new UnsupportedOperationException();
  }
  
  private static final class SimpleMessagePatternConverter extends MessagePatternConverter {
    private static final MessagePatternConverter INSTANCE = new SimpleMessagePatternConverter();
    
    public void format(LogEvent event, StringBuilder toAppendTo) {
      Message msg = event.getMessage();
      if (msg instanceof StringBuilderFormattable) {
        ((StringBuilderFormattable)msg).formatTo(toAppendTo);
      } else if (msg != null) {
        toAppendTo.append(msg.getFormattedMessage());
      } 
    }
  }
  
  private static final class FormattedMessagePatternConverter extends MessagePatternConverter {
    private final String[] formats;
    
    FormattedMessagePatternConverter(String[] formats) {
      this.formats = formats;
    }
    
    public void format(LogEvent event, StringBuilder toAppendTo) {
      Message msg = event.getMessage();
      if (msg instanceof StringBuilderFormattable) {
        if (msg instanceof MultiFormatStringBuilderFormattable) {
          ((MultiFormatStringBuilderFormattable)msg).formatTo(this.formats, toAppendTo);
        } else {
          ((StringBuilderFormattable)msg).formatTo(toAppendTo);
        } 
      } else if (msg != null) {
        toAppendTo.append((msg instanceof MultiformatMessage) ? ((MultiformatMessage)msg)
            .getFormattedMessage(this.formats) : msg
            .getFormattedMessage());
      } 
    }
  }
  
  private static final class RenderingPatternConverter extends MessagePatternConverter {
    private final MessagePatternConverter delegate;
    
    private final TextRenderer textRenderer;
    
    RenderingPatternConverter(MessagePatternConverter delegate, TextRenderer textRenderer) {
      this.delegate = delegate;
      this.textRenderer = textRenderer;
    }
    
    public void format(LogEvent event, StringBuilder toAppendTo) {
      StringBuilder workingBuilder = new StringBuilder(80);
      this.delegate.format(event, workingBuilder);
      this.textRenderer.render(workingBuilder, toAppendTo);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\MessagePatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */