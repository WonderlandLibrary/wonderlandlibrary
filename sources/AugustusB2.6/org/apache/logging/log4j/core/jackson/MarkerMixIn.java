// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.logging.log4j.MarkerManager;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.logging.log4j.Marker;

@JsonDeserialize(as = MarkerManager.Log4jMarker.class)
abstract class MarkerMixIn implements Marker
{
    private static final long serialVersionUID = 1L;
    
    @JsonCreator
    MarkerMixIn(@JsonProperty("name") final String name) {
    }
    
    @JsonProperty("name")
    @JacksonXmlProperty(isAttribute = true)
    @Override
    public abstract String getName();
    
    @JsonProperty("parents")
    @JacksonXmlElementWrapper(namespace = "http://logging.apache.org/log4j/2.0/events", localName = "Parents")
    @JacksonXmlProperty(namespace = "http://logging.apache.org/log4j/2.0/events", localName = "Marker")
    @Override
    public abstract Marker[] getParents();
}
