/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.plugins.visitors;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.PluginValue;
import org.apache.logging.log4j.core.config.plugins.visitors.AbstractPluginVisitor;
import org.apache.logging.log4j.util.StringBuilders;
import org.apache.logging.log4j.util.Strings;

public class PluginValueVisitor
extends AbstractPluginVisitor<PluginValue> {
    public PluginValueVisitor() {
        super(PluginValue.class);
    }

    @Override
    public Object visit(Configuration configuration, Node node, LogEvent event, StringBuilder log) {
        String name = ((PluginValue)this.annotation).value();
        String elementValue = node.getValue();
        String attributeValue = node.getAttributes().get(name);
        String rawValue = null;
        if (Strings.isNotEmpty(elementValue)) {
            if (Strings.isNotEmpty(attributeValue)) {
                LOGGER.error("Configuration contains {} with both attribute value ({}) AND element value ({}). Please specify only one value. Using the element value.", node.getName(), attributeValue, elementValue);
            }
            rawValue = elementValue;
        } else {
            rawValue = PluginValueVisitor.removeAttributeValue(node.getAttributes(), name, new String[0]);
        }
        String value = this.substitutor.replace(event, rawValue);
        StringBuilders.appendKeyDqValue(log, name, value);
        return value;
    }
}

