package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties({"epochMillisecond", "nanoOfMillisecond"})
abstract class InstantMixIn {
  @JsonCreator
  InstantMixIn(@JsonProperty("epochSecond") long epochSecond, @JsonProperty("nanoOfSecond") int nanoOfSecond) {}
  
  @JsonProperty("epochSecond")
  @JacksonXmlProperty(localName = "epochSecond", isAttribute = true)
  abstract long getEpochSecond();
  
  @JsonProperty("nanoOfSecond")
  @JacksonXmlProperty(localName = "nanoOfSecond", isAttribute = true)
  abstract int getNanoOfSecond();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\jackson\InstantMixIn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */