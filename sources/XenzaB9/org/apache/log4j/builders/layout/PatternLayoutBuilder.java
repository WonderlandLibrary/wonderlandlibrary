// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.builders.layout;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.log4j.bridge.LayoutWrapper;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.log4j.config.PropertiesConfiguration;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.apache.log4j.config.Log4j1Configuration;
import org.apache.log4j.xml.XmlConfiguration;
import org.w3c.dom.Element;
import java.util.Properties;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.PluginAliases;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.log4j.Layout;
import org.apache.log4j.builders.AbstractBuilder;

@Plugin(name = "org.apache.log4j.PatternLayout", category = "Log4j Builder")
@PluginAliases({ "org.apache.log4j.EnhancedPatternLayout" })
public class PatternLayoutBuilder extends AbstractBuilder<Layout> implements LayoutBuilder
{
    private static final Logger LOGGER;
    private static final String PATTERN = "ConversionPattern";
    
    public PatternLayoutBuilder() {
    }
    
    public PatternLayoutBuilder(final String prefix, final Properties props) {
        super(prefix, props);
    }
    
    @Override
    public Layout parse(final Element layoutElement, final XmlConfiguration config) {
        final NodeList params = layoutElement.getElementsByTagName("param");
        final int length = params.getLength();
        String pattern = null;
        for (int index = 0; index < length; ++index) {
            final Node currentNode = params.item(index);
            if (currentNode.getNodeType() == 1) {
                final Element currentElement = (Element)currentNode;
                if (currentElement.getTagName().equals("param") && "ConversionPattern".equalsIgnoreCase(currentElement.getAttribute("name"))) {
                    pattern = currentElement.getAttribute("value");
                    break;
                }
            }
        }
        return this.createLayout(pattern, config);
    }
    
    @Override
    public Layout parse(final PropertiesConfiguration config) {
        final String pattern = this.getProperty("ConversionPattern");
        return this.createLayout(pattern, config);
    }
    
    Layout createLayout(String pattern, final Log4j1Configuration config) {
        if (pattern == null) {
            PatternLayoutBuilder.LOGGER.info("No pattern provided for pattern layout, using default pattern");
            pattern = "%m%n";
        }
        return LayoutWrapper.adapt(PatternLayout.newBuilder().withPattern(pattern.replaceAll("%([-\\.\\d]*)p(?!\\w)", "%$1v1Level").replaceAll("%([-\\.\\d]*)x(?!\\w)", "%$1ndc").replaceAll("%([-\\.\\d]*)X(?!\\w)", "%$1properties")).withConfiguration(config).build());
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
