// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.composite;

import org.apache.logging.log4j.core.Filter;
import java.util.List;
import org.apache.logging.log4j.core.config.plugins.util.PluginType;
import org.apache.logging.log4j.core.filter.CompositeFilter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
import java.util.Iterator;
import org.apache.logging.log4j.Level;
import java.util.Map;
import org.apache.logging.log4j.core.config.AbstractConfiguration;
import org.apache.logging.log4j.core.config.Node;

public class DefaultMergeStrategy implements MergeStrategy
{
    private static final String APPENDERS = "appenders";
    private static final String PROPERTIES = "properties";
    private static final String LOGGERS = "loggers";
    private static final String SCRIPTS = "scripts";
    private static final String FILTERS = "filters";
    private static final String STATUS = "status";
    private static final String NAME = "name";
    private static final String REF = "ref";
    
    @Override
    public void mergeRootProperties(final Node rootNode, final AbstractConfiguration configuration) {
        for (final Map.Entry<String, String> attribute : configuration.getRootNode().getAttributes().entrySet()) {
            boolean isFound = false;
            for (final Map.Entry<String, String> targetAttribute : rootNode.getAttributes().entrySet()) {
                if (targetAttribute.getKey().equalsIgnoreCase(attribute.getKey())) {
                    if (attribute.getKey().equalsIgnoreCase("status")) {
                        final Level targetLevel = Level.getLevel(targetAttribute.getValue().toUpperCase());
                        final Level sourceLevel = Level.getLevel(attribute.getValue().toUpperCase());
                        if (targetLevel != null && sourceLevel != null) {
                            if (sourceLevel.isLessSpecificThan(targetLevel)) {
                                targetAttribute.setValue(attribute.getValue());
                            }
                        }
                        else if (sourceLevel != null) {
                            targetAttribute.setValue(attribute.getValue());
                        }
                    }
                    else if (attribute.getKey().equalsIgnoreCase("monitorInterval")) {
                        final int sourceInterval = Integer.parseInt(attribute.getValue());
                        final int targetInterval = Integer.parseInt(targetAttribute.getValue());
                        if (targetInterval == 0 || sourceInterval < targetInterval) {
                            targetAttribute.setValue(attribute.getValue());
                        }
                    }
                    else if (attribute.getKey().equalsIgnoreCase("packages")) {
                        final String sourcePackages = attribute.getValue();
                        final String targetPackages = targetAttribute.getValue();
                        if (sourcePackages != null) {
                            if (targetPackages != null) {
                                targetAttribute.setValue(targetPackages + "," + sourcePackages);
                            }
                            else {
                                targetAttribute.setValue(sourcePackages);
                            }
                        }
                    }
                    else {
                        targetAttribute.setValue(attribute.getValue());
                    }
                    isFound = true;
                }
            }
            if (!isFound) {
                rootNode.getAttributes().put(attribute.getKey(), attribute.getValue());
            }
        }
    }
    
    @Override
    public void mergConfigurations(final Node target, final Node source, final PluginManager pluginManager) {
        for (final Node sourceChildNode : source.getChildren()) {
            final boolean isFilter = this.isFilterNode(sourceChildNode);
            boolean isMerged = false;
            for (final Node targetChildNode : target.getChildren()) {
                if (isFilter) {
                    if (this.isFilterNode(targetChildNode)) {
                        this.updateFilterNode(target, targetChildNode, sourceChildNode, pluginManager);
                        isMerged = true;
                        break;
                    }
                    continue;
                }
                else {
                    if (!targetChildNode.getName().equalsIgnoreCase(sourceChildNode.getName())) {
                        continue;
                    }
                    final String lowerCase = targetChildNode.getName().toLowerCase();
                    switch (lowerCase) {
                        case "properties":
                        case "scripts":
                        case "appenders": {
                            for (final Node node : sourceChildNode.getChildren()) {
                                for (final Node targetNode : targetChildNode.getChildren()) {
                                    if (Objects.equals(targetNode.getAttributes().get("name"), node.getAttributes().get("name"))) {
                                        targetChildNode.getChildren().remove(targetNode);
                                        break;
                                    }
                                }
                                targetChildNode.getChildren().add(node);
                            }
                            isMerged = true;
                            continue;
                        }
                        case "loggers": {
                            final Map<String, Node> targetLoggers = new HashMap<String, Node>();
                            for (final Node node2 : targetChildNode.getChildren()) {
                                targetLoggers.put(node2.getName(), node2);
                            }
                            for (final Node node2 : sourceChildNode.getChildren()) {
                                final Node targetNode = this.getLoggerNode(targetChildNode, node2.getAttributes().get("name"));
                                final Node loggerNode = new Node(targetChildNode, node2.getName(), node2.getType());
                                if (targetNode != null) {
                                    targetNode.getAttributes().putAll(node2.getAttributes());
                                    for (final Node sourceLoggerChild : node2.getChildren()) {
                                        if (this.isFilterNode(sourceLoggerChild)) {
                                            boolean foundFilter = false;
                                            for (final Node targetChild : targetNode.getChildren()) {
                                                if (this.isFilterNode(targetChild)) {
                                                    this.updateFilterNode(loggerNode, targetChild, sourceLoggerChild, pluginManager);
                                                    foundFilter = true;
                                                    break;
                                                }
                                            }
                                            if (foundFilter) {
                                                continue;
                                            }
                                            final Node childNode = new Node(loggerNode, sourceLoggerChild.getName(), sourceLoggerChild.getType());
                                            childNode.getAttributes().putAll(sourceLoggerChild.getAttributes());
                                            childNode.getChildren().addAll(sourceLoggerChild.getChildren());
                                            targetNode.getChildren().add(childNode);
                                        }
                                        else {
                                            final Node childNode2 = new Node(loggerNode, sourceLoggerChild.getName(), sourceLoggerChild.getType());
                                            childNode2.getAttributes().putAll(sourceLoggerChild.getAttributes());
                                            childNode2.getChildren().addAll(sourceLoggerChild.getChildren());
                                            if (childNode2.getName().equalsIgnoreCase("AppenderRef")) {
                                                for (final Node targetChild : targetNode.getChildren()) {
                                                    if (this.isSameReference(targetChild, childNode2)) {
                                                        targetNode.getChildren().remove(targetChild);
                                                        break;
                                                    }
                                                }
                                            }
                                            else {
                                                for (final Node targetChild : targetNode.getChildren()) {
                                                    if (this.isSameName(targetChild, childNode2)) {
                                                        targetNode.getChildren().remove(targetChild);
                                                        break;
                                                    }
                                                }
                                            }
                                            targetNode.getChildren().add(childNode2);
                                        }
                                    }
                                }
                                else {
                                    loggerNode.getAttributes().putAll(node2.getAttributes());
                                    loggerNode.getChildren().addAll(node2.getChildren());
                                    targetChildNode.getChildren().add(loggerNode);
                                }
                            }
                            isMerged = true;
                            continue;
                        }
                        default: {
                            targetChildNode.getChildren().addAll(sourceChildNode.getChildren());
                            isMerged = true;
                            continue;
                        }
                    }
                }
            }
            if (!isMerged) {
                if (sourceChildNode.getName().equalsIgnoreCase("Properties")) {
                    target.getChildren().add(0, sourceChildNode);
                }
                else {
                    target.getChildren().add(sourceChildNode);
                }
            }
        }
    }
    
    private Node getLoggerNode(final Node parentNode, final String name) {
        for (final Node node : parentNode.getChildren()) {
            final String nodeName = node.getAttributes().get("name");
            if (name == null && nodeName == null) {
                return node;
            }
            if (nodeName != null && nodeName.equals(name)) {
                return node;
            }
        }
        return null;
    }
    
    private void updateFilterNode(final Node target, final Node targetChildNode, final Node sourceChildNode, final PluginManager pluginManager) {
        if (CompositeFilter.class.isAssignableFrom(targetChildNode.getType().getPluginClass())) {
            final Node node = new Node(targetChildNode, sourceChildNode.getName(), sourceChildNode.getType());
            node.getChildren().addAll(sourceChildNode.getChildren());
            node.getAttributes().putAll(sourceChildNode.getAttributes());
            targetChildNode.getChildren().add(node);
        }
        else {
            final PluginType pluginType = pluginManager.getPluginType("filters");
            final Node filtersNode = new Node(targetChildNode, "filters", pluginType);
            final Node node2 = new Node(filtersNode, sourceChildNode.getName(), sourceChildNode.getType());
            node2.getAttributes().putAll(sourceChildNode.getAttributes());
            final List<Node> children = filtersNode.getChildren();
            children.add(targetChildNode);
            children.add(node2);
            final List<Node> nodes = target.getChildren();
            nodes.remove(targetChildNode);
            nodes.add(filtersNode);
        }
    }
    
    private boolean isFilterNode(final Node node) {
        return Filter.class.isAssignableFrom(node.getType().getPluginClass());
    }
    
    private boolean isSameName(final Node node1, final Node node2) {
        final String value = node1.getAttributes().get("name");
        return value != null && value.toLowerCase().equals(node2.getAttributes().get("name").toLowerCase());
    }
    
    private boolean isSameReference(final Node node1, final Node node2) {
        final String value = node1.getAttributes().get("ref");
        return value != null && value.toLowerCase().equals(node2.getAttributes().get("ref").toLowerCase());
    }
}
