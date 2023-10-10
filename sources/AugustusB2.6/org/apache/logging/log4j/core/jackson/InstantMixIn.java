// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "epochMillisecond", "nanoOfMillisecond" })
abstract class InstantMixIn
{
    @JsonCreator
    InstantMixIn(@JsonProperty("epochSecond") final long epochSecond, @JsonProperty("nanoOfSecond") final int nanoOfSecond) {
    }
    
    @JsonProperty("epochSecond")
    @JacksonXmlProperty(localName = "epochSecond", isAttribute = true)
    abstract long getEpochSecond();
    
    @JsonProperty("nanoOfSecond")
    @JacksonXmlProperty(localName = "nanoOfSecond", isAttribute = true)
    abstract int getNanoOfSecond();
}
