/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonCreator
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
 */
package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(value={"epochMillisecond", "nanoOfMillisecond"})
abstract class InstantMixIn {
    @JsonCreator
    InstantMixIn(@JsonProperty(value="epochSecond") long epochSecond, @JsonProperty(value="nanoOfSecond") int nanoOfSecond) {
    }

    @JsonProperty(value="epochSecond")
    @JacksonXmlProperty(localName="epochSecond", isAttribute=true)
    abstract long getEpochSecond();

    @JsonProperty(value="nanoOfSecond")
    @JacksonXmlProperty(localName="nanoOfSecond", isAttribute=true)
    abstract int getNanoOfSecond();
}

