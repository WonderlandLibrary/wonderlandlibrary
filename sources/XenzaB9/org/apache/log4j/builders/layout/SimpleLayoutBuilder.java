// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.builders.layout;

import org.apache.log4j.config.PropertiesConfiguration;
import org.apache.log4j.bridge.LayoutWrapper;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.log4j.Layout;
import org.apache.log4j.xml.XmlConfiguration;
import org.w3c.dom.Element;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "org.apache.log4j.SimpleLayout", category = "Log4j Builder")
public class SimpleLayoutBuilder implements LayoutBuilder
{
    @Override
    public Layout parse(final Element layoutElement, final XmlConfiguration config) {
        return new LayoutWrapper(PatternLayout.newBuilder().withPattern("%v1Level - %m%n").withConfiguration(config).build());
    }
    
    @Override
    public Layout parse(final PropertiesConfiguration config) {
        return new LayoutWrapper(PatternLayout.newBuilder().withPattern("%v1Level - %m%n").withConfiguration(config).build());
    }
}
