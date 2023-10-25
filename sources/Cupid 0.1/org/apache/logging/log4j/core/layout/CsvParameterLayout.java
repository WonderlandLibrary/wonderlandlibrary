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
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name = "CsvParameterLayout", category = "Core", elementType = "layout", printObject = true)
public class CsvParameterLayout extends AbstractCsvLayout {
  public static AbstractCsvLayout createDefaultLayout() {
    return new CsvParameterLayout(null, Charset.forName("UTF-8"), CSVFormat.valueOf("Default"), null, null);
  }
  
  public static AbstractCsvLayout createLayout(CSVFormat format) {
    return new CsvParameterLayout(null, Charset.forName("UTF-8"), format, null, null);
  }
  
  @PluginFactory
  public static AbstractCsvLayout createLayout(@PluginConfiguration Configuration config, @PluginAttribute(value = "format", defaultString = "Default") String format, @PluginAttribute("delimiter") Character delimiter, @PluginAttribute("escape") Character escape, @PluginAttribute("quote") Character quote, @PluginAttribute("quoteMode") QuoteMode quoteMode, @PluginAttribute("nullString") String nullString, @PluginAttribute("recordSeparator") String recordSeparator, @PluginAttribute(value = "charset", defaultString = "UTF-8") Charset charset, @PluginAttribute("header") String header, @PluginAttribute("footer") String footer) {
    CSVFormat csvFormat = createFormat(format, delimiter, escape, quote, quoteMode, nullString, recordSeparator);
    return new CsvParameterLayout(config, charset, csvFormat, header, footer);
  }
  
  public CsvParameterLayout(Configuration config, Charset charset, CSVFormat csvFormat, String header, String footer) {
    super(config, charset, csvFormat, header, footer);
  }
  
  public String toSerializable(LogEvent event) {
    Message message = event.getMessage();
    Object[] parameters = message.getParameters();
    StringBuilder buffer = getStringBuilder();
    try {
      getFormat().printRecord(buffer, parameters);
      return buffer.toString();
    } catch (IOException e) {
      StatusLogger.getLogger().error(message, e);
      return getFormat().getCommentMarker() + " " + e;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\layout\CsvParameterLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */