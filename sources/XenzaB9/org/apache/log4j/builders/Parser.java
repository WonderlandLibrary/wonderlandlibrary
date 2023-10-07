// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.builders;

import org.apache.log4j.config.PropertiesConfiguration;
import org.apache.log4j.xml.XmlConfiguration;
import org.w3c.dom.Element;

public interface Parser<T> extends Builder<T>
{
    T parse(final Element element, final XmlConfiguration config);
    
    T parse(final PropertiesConfiguration config);
}
