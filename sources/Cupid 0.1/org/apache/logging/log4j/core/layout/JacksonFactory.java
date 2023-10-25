package org.apache.logging.log4j.core.layout;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.dataformat.xml.util.DefaultXmlPrettyPrinter;
import java.util.HashSet;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.jackson.Log4jJsonObjectMapper;
import org.apache.logging.log4j.core.jackson.Log4jXmlObjectMapper;
import org.apache.logging.log4j.core.jackson.Log4jYamlObjectMapper;
import org.codehaus.stax2.XMLStreamWriter2;

abstract class JacksonFactory {
  protected abstract String getPropertyNameForTimeMillis();
  
  protected abstract String getPropertyNameForInstant();
  
  protected abstract String getPropertNameForContextMap();
  
  protected abstract String getPropertNameForSource();
  
  protected abstract String getPropertNameForNanoTime();
  
  protected abstract PrettyPrinter newCompactPrinter();
  
  protected abstract ObjectMapper newObjectMapper();
  
  protected abstract PrettyPrinter newPrettyPrinter();
  
  static class JSON extends JacksonFactory {
    private final boolean encodeThreadContextAsList;
    
    private final boolean includeStacktrace;
    
    private final boolean stacktraceAsString;
    
    private final boolean objectMessageAsJsonObject;
    
    public JSON(boolean encodeThreadContextAsList, boolean includeStacktrace, boolean stacktraceAsString, boolean objectMessageAsJsonObject) {
      this.encodeThreadContextAsList = encodeThreadContextAsList;
      this.includeStacktrace = includeStacktrace;
      this.stacktraceAsString = stacktraceAsString;
      this.objectMessageAsJsonObject = objectMessageAsJsonObject;
    }
    
    protected String getPropertNameForContextMap() {
      return "contextMap";
    }
    
    protected String getPropertyNameForTimeMillis() {
      return "timeMillis";
    }
    
    protected String getPropertyNameForInstant() {
      return "instant";
    }
    
    protected String getPropertNameForSource() {
      return "source";
    }
    
    protected String getPropertNameForNanoTime() {
      return "nanoTime";
    }
    
    protected PrettyPrinter newCompactPrinter() {
      return (PrettyPrinter)new MinimalPrettyPrinter();
    }
    
    protected ObjectMapper newObjectMapper() {
      return (ObjectMapper)new Log4jJsonObjectMapper(this.encodeThreadContextAsList, this.includeStacktrace, this.stacktraceAsString, this.objectMessageAsJsonObject);
    }
    
    protected PrettyPrinter newPrettyPrinter() {
      return (PrettyPrinter)new DefaultPrettyPrinter();
    }
  }
  
  static class XML extends JacksonFactory {
    static final int DEFAULT_INDENT = 1;
    
    private final boolean includeStacktrace;
    
    private final boolean stacktraceAsString;
    
    public XML(boolean includeStacktrace, boolean stacktraceAsString) {
      this.includeStacktrace = includeStacktrace;
      this.stacktraceAsString = stacktraceAsString;
    }
    
    protected String getPropertyNameForTimeMillis() {
      return "TimeMillis";
    }
    
    protected String getPropertyNameForInstant() {
      return "Instant";
    }
    
    protected String getPropertNameForContextMap() {
      return "ContextMap";
    }
    
    protected String getPropertNameForSource() {
      return "Source";
    }
    
    protected String getPropertNameForNanoTime() {
      return "nanoTime";
    }
    
    protected PrettyPrinter newCompactPrinter() {
      return null;
    }
    
    protected ObjectMapper newObjectMapper() {
      return (ObjectMapper)new Log4jXmlObjectMapper(this.includeStacktrace, this.stacktraceAsString);
    }
    
    protected PrettyPrinter newPrettyPrinter() {
      return (PrettyPrinter)new JacksonFactory.Log4jXmlPrettyPrinter(1);
    }
  }
  
  static class YAML extends JacksonFactory {
    private final boolean includeStacktrace;
    
    private final boolean stacktraceAsString;
    
    public YAML(boolean includeStacktrace, boolean stacktraceAsString) {
      this.includeStacktrace = includeStacktrace;
      this.stacktraceAsString = stacktraceAsString;
    }
    
    protected String getPropertyNameForTimeMillis() {
      return "timeMillis";
    }
    
    protected String getPropertyNameForInstant() {
      return "instant";
    }
    
    protected String getPropertNameForContextMap() {
      return "contextMap";
    }
    
    protected String getPropertNameForSource() {
      return "source";
    }
    
    protected String getPropertNameForNanoTime() {
      return "nanoTime";
    }
    
    protected PrettyPrinter newCompactPrinter() {
      return (PrettyPrinter)new MinimalPrettyPrinter();
    }
    
    protected ObjectMapper newObjectMapper() {
      return (ObjectMapper)new Log4jYamlObjectMapper(false, this.includeStacktrace, this.stacktraceAsString);
    }
    
    protected PrettyPrinter newPrettyPrinter() {
      return (PrettyPrinter)new DefaultPrettyPrinter();
    }
  }
  
  static class Log4jXmlPrettyPrinter extends DefaultXmlPrettyPrinter {
    private static final long serialVersionUID = 1L;
    
    Log4jXmlPrettyPrinter(int nesting) {
      this._nesting = nesting;
    }
    
    public void writePrologLinefeed(XMLStreamWriter2 sw) throws XMLStreamException {}
    
    public DefaultXmlPrettyPrinter createInstance() {
      return new Log4jXmlPrettyPrinter(1);
    }
  }
  
  ObjectWriter newWriter(boolean locationInfo, boolean properties, boolean compact) {
    return newWriter(locationInfo, properties, compact, false);
  }
  
  ObjectWriter newWriter(boolean locationInfo, boolean properties, boolean compact, boolean includeMillis) {
    SimpleFilterProvider filters = new SimpleFilterProvider();
    Set<String> except = new HashSet<>(3);
    if (!locationInfo)
      except.add(getPropertNameForSource()); 
    if (!properties)
      except.add(getPropertNameForContextMap()); 
    if (includeMillis) {
      except.add(getPropertyNameForInstant());
    } else {
      except.add(getPropertyNameForTimeMillis());
    } 
    except.add(getPropertNameForNanoTime());
    filters.addFilter(Log4jLogEvent.class.getName(), SimpleBeanPropertyFilter.serializeAllExcept(except));
    ObjectWriter writer = newObjectMapper().writer(compact ? newCompactPrinter() : newPrettyPrinter());
    return writer.with((FilterProvider)filters);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\layout\JacksonFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */