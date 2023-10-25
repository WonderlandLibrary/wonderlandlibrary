package org.apache.logging.log4j.core.layout;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name = "CsvLogEventLayout", category = "Core", elementType = "layout", printObject = true)
public class CsvLogEventLayout extends AbstractCsvLayout {
  public static CsvLogEventLayout createDefaultLayout() {
    return new CsvLogEventLayout(null, Charset.forName("UTF-8"), CSVFormat.valueOf("Default"), null, null);
  }
  
  public static CsvLogEventLayout createLayout(CSVFormat format) {
    return new CsvLogEventLayout(null, Charset.forName("UTF-8"), format, null, null);
  }
  
  @PluginFactory
  public static CsvLogEventLayout createLayout(@PluginConfiguration Configuration config, @PluginAttribute(value = "format", defaultString = "Default") String format, @PluginAttribute("delimiter") Character delimiter, @PluginAttribute("escape") Character escape, @PluginAttribute("quote") Character quote, @PluginAttribute("quoteMode") QuoteMode quoteMode, @PluginAttribute("nullString") String nullString, @PluginAttribute("recordSeparator") String recordSeparator, @PluginAttribute(value = "charset", defaultString = "UTF-8") Charset charset, @PluginAttribute("header") String header, @PluginAttribute("footer") String footer) {
    CSVFormat csvFormat = createFormat(format, delimiter, escape, quote, quoteMode, nullString, recordSeparator);
    return new CsvLogEventLayout(config, charset, csvFormat, header, footer);
  }
  
  protected CsvLogEventLayout(Configuration config, Charset charset, CSVFormat csvFormat, String header, String footer) {
    super(config, charset, csvFormat, header, footer);
  }
  
  public String toSerializable(LogEvent event) {
    StringBuilder buffer = getStringBuilder();
    CSVFormat format = getFormat();
    try {
      format.print(Long.valueOf(event.getNanoTime()), buffer, true);
      format.print(Long.valueOf(event.getTimeMillis()), buffer, false);
      format.print(event.getLevel(), buffer, false);
      format.print(Long.valueOf(event.getThreadId()), buffer, false);
      format.print(event.getThreadName(), buffer, false);
      format.print(Integer.valueOf(event.getThreadPriority()), buffer, false);
      format.print(event.getMessage().getFormattedMessage(), buffer, false);
      format.print(event.getLoggerFqcn(), buffer, false);
      format.print(event.getLoggerName(), buffer, false);
      format.print(event.getMarker(), buffer, false);
      format.print(event.getThrownProxy(), buffer, false);
      format.print(event.getSource(), buffer, false);
      format.print(event.getContextData(), buffer, false);
      format.print(event.getContextStack(), buffer, false);
      format.println(buffer);
      return buffer.toString();
    } catch (IOException e) {
      StatusLogger.getLogger().error(event.toString(), e);
      return format.getCommentMarker() + " " + e;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\layout\CsvLogEventLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */