package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonPropertyOrder({"key", "value"})
final class MapEntry {
  @JsonProperty
  @JacksonXmlProperty(isAttribute = true)
  private String key;
  
  @JsonProperty
  @JacksonXmlProperty(isAttribute = true)
  private String value;
  
  @JsonCreator
  public MapEntry(@JsonProperty("key") String key, @JsonProperty("value") String value) {
    setKey(key);
    setValue(value);
  }
  
  public boolean equals(Object obj) {
    if (this == obj)
      return true; 
    if (obj == null)
      return false; 
    if (!(obj instanceof MapEntry))
      return false; 
    MapEntry other = (MapEntry)obj;
    if (getKey() == null) {
      if (other.getKey() != null)
        return false; 
    } else if (!getKey().equals(other.getKey())) {
      return false;
    } 
    if (getValue() == null) {
      if (other.getValue() != null)
        return false; 
    } else if (!getValue().equals(other.getValue())) {
      return false;
    } 
    return true;
  }
  
  public String getKey() {
    return this.key;
  }
  
  public String getValue() {
    return this.value;
  }
  
  public int hashCode() {
    int prime = 31;
    int result = 1;
    result = 31 * result + ((getKey() == null) ? 0 : getKey().hashCode());
    result = 31 * result + ((getValue() == null) ? 0 : getValue().hashCode());
    return result;
  }
  
  public void setKey(String key) {
    this.key = key;
  }
  
  public void setValue(String value) {
    this.value = value;
  }
  
  public String toString() {
    return "" + getKey() + "=" + getValue();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\jackson\MapEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */