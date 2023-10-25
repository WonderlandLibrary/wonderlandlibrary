package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties({"nativeMethod"})
abstract class StackTraceElementMixIn {
  @JsonCreator
  StackTraceElementMixIn(@JsonProperty("class") String declaringClass, @JsonProperty("method") String methodName, @JsonProperty("file") String fileName, @JsonProperty("line") int lineNumber) {}
  
  @JsonProperty("class")
  @JacksonXmlProperty(localName = "class", isAttribute = true)
  abstract String getClassName();
  
  @JsonProperty("file")
  @JacksonXmlProperty(localName = "file", isAttribute = true)
  abstract String getFileName();
  
  @JsonProperty("line")
  @JacksonXmlProperty(localName = "line", isAttribute = true)
  abstract int getLineNumber();
  
  @JsonProperty("method")
  @JacksonXmlProperty(localName = "method", isAttribute = true)
  abstract String getMethodName();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\jackson\StackTraceElementMixIn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */