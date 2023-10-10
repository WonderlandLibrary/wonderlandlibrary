// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "key", "value" })
final class MapEntry
{
    @JsonProperty
    @JacksonXmlProperty(isAttribute = true)
    private String key;
    @JsonProperty
    @JacksonXmlProperty(isAttribute = true)
    private String value;
    
    @JsonCreator
    public MapEntry(@JsonProperty("key") final String key, @JsonProperty("value") final String value) {
        this.setKey(key);
        this.setValue(value);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MapEntry)) {
            return false;
        }
        final MapEntry other = (MapEntry)obj;
        if (this.getKey() == null) {
            if (other.getKey() != null) {
                return false;
            }
        }
        else if (!this.getKey().equals(other.getKey())) {
            return false;
        }
        if (this.getValue() == null) {
            if (other.getValue() != null) {
                return false;
            }
        }
        else if (!this.getValue().equals(other.getValue())) {
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
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.getKey() == null) ? 0 : this.getKey().hashCode());
        result = 31 * result + ((this.getValue() == null) ? 0 : this.getValue().hashCode());
        return result;
    }
    
    public void setKey(final String key) {
        this.key = key;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return "" + this.getKey() + "=" + this.getValue();
    }
}
