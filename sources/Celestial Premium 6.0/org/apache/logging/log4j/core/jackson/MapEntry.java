/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonCreator
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.fasterxml.jackson.annotation.JsonPropertyOrder
 *  com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
 */
package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonPropertyOrder(value={"key", "value"})
final class MapEntry {
    @JsonProperty
    @JacksonXmlProperty(isAttribute=true)
    private String key;
    @JsonProperty
    @JacksonXmlProperty(isAttribute=true)
    private String value;

    @JsonCreator
    public MapEntry(@JsonProperty(value="key") String key, @JsonProperty(value="value") String value) {
        this.setKey(key);
        this.setValue(value);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MapEntry)) {
            return false;
        }
        MapEntry other = (MapEntry)obj;
        if (this.getKey() == null ? other.getKey() != null : !this.getKey().equals(other.getKey())) {
            return false;
        }
        return !(this.getValue() == null ? other.getValue() != null : !this.getValue().equals(other.getValue()));
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
        result = 31 * result + (this.getKey() == null ? 0 : this.getKey().hashCode());
        result = 31 * result + (this.getValue() == null ? 0 : this.getValue().hashCode());
        return result;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return "" + this.getKey() + "=" + this.getValue();
    }
}

