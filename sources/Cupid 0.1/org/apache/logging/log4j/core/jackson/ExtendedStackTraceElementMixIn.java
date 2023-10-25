package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import org.apache.logging.log4j.core.impl.ExtendedClassInfo;

@JsonPropertyOrder({"class", "method", "file", "line", "exact", "location", "version"})
abstract class ExtendedStackTraceElementMixIn implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @JsonCreator
  public ExtendedStackTraceElementMixIn(@JsonProperty("class") String declaringClass, @JsonProperty("method") String methodName, @JsonProperty("file") String fileName, @JsonProperty("line") int lineNumber, @JsonProperty("exact") boolean exact, @JsonProperty("location") String location, @JsonProperty("version") String version) {}
  
  @JsonProperty("class")
  @JacksonXmlProperty(localName = "class", isAttribute = true)
  public abstract String getClassName();
  
  @JsonProperty
  @JacksonXmlProperty(isAttribute = true)
  public abstract boolean getExact();
  
  @JsonIgnore
  public abstract ExtendedClassInfo getExtraClassInfo();
  
  @JsonProperty("file")
  @JacksonXmlProperty(localName = "file", isAttribute = true)
  public abstract String getFileName();
  
  @JsonProperty("line")
  @JacksonXmlProperty(localName = "line", isAttribute = true)
  public abstract int getLineNumber();
  
  @JsonProperty
  @JacksonXmlProperty(isAttribute = true)
  public abstract String getLocation();
  
  @JsonProperty("method")
  @JacksonXmlProperty(localName = "method", isAttribute = true)
  public abstract String getMethodName();
  
  @JsonIgnore
  abstract StackTraceElement getStackTraceElement();
  
  @JsonProperty
  @JacksonXmlProperty(isAttribute = true)
  public abstract String getVersion();
  
  @JsonIgnore
  public abstract boolean isNativeMethod();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\jackson\ExtendedStackTraceElementMixIn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */