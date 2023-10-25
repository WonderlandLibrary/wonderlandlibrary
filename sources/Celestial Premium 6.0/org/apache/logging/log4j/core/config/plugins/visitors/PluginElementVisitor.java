/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.plugins.visitors;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.util.PluginType;
import org.apache.logging.log4j.core.config.plugins.visitors.AbstractPluginVisitor;

public class PluginElementVisitor
extends AbstractPluginVisitor<PluginElement> {
    public PluginElementVisitor() {
        super(PluginElement.class);
    }

    @Override
    public Object visit(Configuration configuration, Node node, LogEvent event, StringBuilder log) {
        String name = ((PluginElement)this.annotation).value();
        if (this.conversionType.isArray()) {
            this.setConversionType(this.conversionType.getComponentType());
            ArrayList<Object> values = new ArrayList<Object>();
            ArrayList<Node> used = new ArrayList<Node>();
            log.append("={");
            boolean first = true;
            for (Node child : node.getChildren()) {
                PluginType childType = child.getType();
                if (!name.equalsIgnoreCase(childType.getElementName()) && !this.conversionType.isAssignableFrom(childType.getPluginClass())) continue;
                if (!first) {
                    log.append(", ");
                }
                first = false;
                used.add(child);
                Object childObject = child.getObject();
                if (childObject == null) {
                    LOGGER.error("Null object returned for {} in {}.", child.getName(), node.getName());
                    continue;
                }
                if (childObject.getClass().isArray()) {
                    log.append(Arrays.toString((Object[])childObject)).append('}');
                    node.getChildren().removeAll(used);
                    return childObject;
                }
                log.append(child.toString());
                values.add(childObject);
            }
            log.append('}');
            if (!values.isEmpty() && !this.conversionType.isAssignableFrom(values.get(0).getClass())) {
                LOGGER.error("Attempted to assign attribute {} to list of type {} which is incompatible with {}.", name, values.get(0).getClass(), this.conversionType);
                return null;
            }
            node.getChildren().removeAll(used);
            Object[] array = (Object[])Array.newInstance(this.conversionType, values.size());
            for (int i = 0; i < array.length; ++i) {
                array[i] = values.get(i);
            }
            return array;
        }
        Node namedNode = this.findNamedNode(name, node.getChildren());
        if (namedNode == null) {
            log.append(name).append("=null");
            return null;
        }
        log.append(namedNode.getName()).append('(').append(namedNode.toString()).append(')');
        node.getChildren().remove(namedNode);
        return namedNode.getObject();
    }

    private Node findNamedNode(String name, Iterable<Node> children) {
        for (Node child : children) {
            PluginType childType = child.getType();
            if (childType == null) {
                // empty if block
            }
            if (!name.equalsIgnoreCase(childType.getElementName()) && !this.conversionType.isAssignableFrom(childType.getPluginClass())) continue;
            return child;
        }
        return null;
    }
}

