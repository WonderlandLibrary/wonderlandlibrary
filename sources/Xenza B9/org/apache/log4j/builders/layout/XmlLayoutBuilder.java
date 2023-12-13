// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.builders.layout;

import org.apache.log4j.bridge.LayoutWrapper;
import org.apache.log4j.layout.Log4j1XmlLayout;
import org.apache.log4j.config.PropertiesConfiguration;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.log4j.xml.XmlConfiguration;
import org.w3c.dom.Element;
import java.util.Properties;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.log4j.Layout;
import org.apache.log4j.builders.AbstractBuilder;

@Plugin(name = "org.apache.log4j.xml.XMLLayout", category = "Log4j Builder")
public class XmlLayoutBuilder extends AbstractBuilder<Layout> implements LayoutBuilder
{
    private static final String LOCATION_INFO = "LocationInfo";
    private static final String PROPERTIES = "Properties";
    
    public XmlLayoutBuilder() {
    }
    
    public XmlLayoutBuilder(final String prefix, final Properties props) {
        super(prefix, props);
    }
    
    @Override
    public Layout parse(final Element layoutElement, final XmlConfiguration config) {
        final AtomicBoolean properties = new AtomicBoolean();
        final AtomicBoolean locationInfo = new AtomicBoolean();
        XmlConfiguration.forEachElement(layoutElement.getElementsByTagName("param"), currentElement -> {
            if ("Properties".equalsIgnoreCase(currentElement.getAttribute("name"))) {
                properties.set(this.getBooleanValueAttribute(currentElement));
            }
            else if ("LocationInfo".equalsIgnoreCase(currentElement.getAttribute("name"))) {
                locationInfo.set(this.getBooleanValueAttribute(currentElement));
            }
            return;
        });
        return this.createLayout(properties.get(), locationInfo.get());
    }
    
    @Override
    public Layout parse(final PropertiesConfiguration config) {
        final boolean properties = this.getBooleanProperty("Properties");
        final boolean locationInfo = this.getBooleanProperty("LocationInfo");
        return this.createLayout(properties, locationInfo);
    }
    
    private Layout createLayout(final boolean properties, final boolean locationInfo) {
        return LayoutWrapper.adapt(Log4j1XmlLayout.createLayout(locationInfo, properties));
    }
}
