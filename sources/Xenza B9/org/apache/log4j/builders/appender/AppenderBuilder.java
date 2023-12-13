// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.builders.appender;

import org.apache.log4j.config.PropertiesConfiguration;
import java.util.Properties;
import org.apache.log4j.xml.XmlConfiguration;
import org.w3c.dom.Element;
import org.apache.log4j.builders.Builder;
import org.apache.log4j.Appender;

public interface AppenderBuilder<T extends Appender> extends Builder<T>
{
    Appender parseAppender(final Element element, final XmlConfiguration configuration);
    
    Appender parseAppender(final String name, final String appenderPrefix, final String layoutPrefix, final String filterPrefix, final Properties props, final PropertiesConfiguration configuration);
}
