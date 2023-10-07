// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.builders.filter;

import org.apache.log4j.config.PropertiesConfiguration;
import org.apache.log4j.bridge.FilterWrapper;
import org.apache.logging.log4j.core.filter.DenyAllFilter;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.xml.XmlConfiguration;
import org.w3c.dom.Element;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "org.apache.log4j.varia.DenyAllFilter", category = "Log4j Builder")
public class DenyAllFilterBuilder implements FilterBuilder
{
    @Override
    public Filter parse(final Element filterElement, final XmlConfiguration config) {
        return new FilterWrapper(DenyAllFilter.newBuilder().build());
    }
    
    @Override
    public Filter parse(final PropertiesConfiguration config) {
        return new FilterWrapper(DenyAllFilter.newBuilder().build());
    }
}
