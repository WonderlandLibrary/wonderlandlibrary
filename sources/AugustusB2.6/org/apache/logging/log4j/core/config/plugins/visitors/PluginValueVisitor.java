// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.plugins.visitors;

import org.apache.logging.log4j.util.StringBuilders;
import org.apache.logging.log4j.util.Strings;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.PluginValue;

public class PluginValueVisitor extends AbstractPluginVisitor<PluginValue>
{
    public PluginValueVisitor() {
        super(PluginValue.class);
    }
    
    @Override
    public Object visit(final Configuration configuration, final Node node, final LogEvent event, final StringBuilder log) {
        final String name = ((PluginValue)this.annotation).value();
        final String elementValue = node.getValue();
        final String attributeValue = node.getAttributes().get(name);
        String rawValue = null;
        if (Strings.isNotEmpty(elementValue)) {
            if (Strings.isNotEmpty(attributeValue)) {
                PluginValueVisitor.LOGGER.error("Configuration contains {} with both attribute value ({}) AND element value ({}). Please specify only one value. Using the element value.", node.getName(), attributeValue, elementValue);
            }
            rawValue = elementValue;
        }
        else {
            rawValue = AbstractPluginVisitor.removeAttributeValue(node.getAttributes(), name, new String[0]);
        }
        final String value = this.substitutor.replace(event, rawValue);
        StringBuilders.appendKeyDqValue(log, name, value);
        return value;
    }
}
