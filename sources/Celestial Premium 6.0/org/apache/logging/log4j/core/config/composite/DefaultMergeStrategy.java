/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.composite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.config.AbstractConfiguration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.composite.MergeStrategy;
import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
import org.apache.logging.log4j.core.config.plugins.util.PluginType;
import org.apache.logging.log4j.core.filter.CompositeFilter;

public class DefaultMergeStrategy
implements MergeStrategy {
    private static final String APPENDERS = "appenders";
    private static final String PROPERTIES = "properties";
    private static final String LOGGERS = "loggers";
    private static final String SCRIPTS = "scripts";
    private static final String FILTERS = "filters";
    private static final String STATUS = "status";
    private static final String NAME = "name";
    private static final String REF = "ref";

    @Override
    public void mergeRootProperties(Node rootNode, AbstractConfiguration configuration) {
        for (Map.Entry<String, String> attribute : configuration.getRootNode().getAttributes().entrySet()) {
            boolean isFound = false;
            for (Map.Entry<String, String> targetAttribute : rootNode.getAttributes().entrySet()) {
                if (!targetAttribute.getKey().equalsIgnoreCase(attribute.getKey())) continue;
                if (attribute.getKey().equalsIgnoreCase(STATUS)) {
                    Level targetLevel = Level.getLevel((String)targetAttribute.getValue().toUpperCase());
                    Level sourceLevel = Level.getLevel((String)attribute.getValue().toUpperCase());
                    if (targetLevel != null && sourceLevel != null) {
                        if (sourceLevel.isLessSpecificThan(targetLevel)) {
                            targetAttribute.setValue(attribute.getValue());
                        }
                    } else if (sourceLevel != null) {
                        targetAttribute.setValue(attribute.getValue());
                    }
                } else if (attribute.getKey().equalsIgnoreCase("monitorInterval")) {
                    int sourceInterval = Integer.parseInt(attribute.getValue());
                    int targetInterval = Integer.parseInt(targetAttribute.getValue());
                    if (targetInterval == 0 || sourceInterval < targetInterval) {
                        targetAttribute.setValue(attribute.getValue());
                    }
                } else if (attribute.getKey().equalsIgnoreCase("packages")) {
                    String sourcePackages = attribute.getValue();
                    String targetPackages = targetAttribute.getValue();
                    if (sourcePackages != null) {
                        if (targetPackages != null) {
                            targetAttribute.setValue(targetPackages + "," + sourcePackages);
                        } else {
                            targetAttribute.setValue(sourcePackages);
                        }
                    }
                } else {
                    targetAttribute.setValue(attribute.getValue());
                }
                isFound = true;
            }
            if (isFound) continue;
            rootNode.getAttributes().put(attribute.getKey(), attribute.getValue());
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @Override
    public void mergConfigurations(Node target, Node source, PluginManager pluginManager) {
        block27: {
            var4_4 = source.getChildren().iterator();
            while (var4_4.hasNext() != false) {
                sourceChildNode = var4_4.next();
                isFilter = this.isFilterNode(sourceChildNode);
                isMerged = false;
                for (Node targetChildNode : target.getChildren()) {
                    if (isFilter) {
                        if (!this.isFilterNode(targetChildNode)) continue;
                        this.updateFilterNode(target, targetChildNode, sourceChildNode, pluginManager);
                        isMerged = true;
                        break;
                    }
                    if (!targetChildNode.getName().equalsIgnoreCase(sourceChildNode.getName())) continue;
                    var10_10 = targetChildNode.getName().toLowerCase();
                    var11_11 = -1;
                    switch (var10_10.hashCode()) {
                        case -926053069: {
                            if (!var10_10.equals("properties")) break;
                            var11_11 = 0;
                            break;
                        }
                        case 1926514952: {
                            if (!var10_10.equals("scripts")) break;
                            var11_11 = 1;
                            break;
                        }
                        case 2009213964: {
                            if (!var10_10.equals("appenders")) break;
                            var11_11 = 2;
                            break;
                        }
                        case 342277347: {
                            if (!var10_10.equals("loggers")) break;
                            var11_11 = 3;
                            break;
                        }
                    }
                    block6 : switch (var11_11) {
                        case 0: 
                        case 1: 
                        case 2: {
                            for (Node node : sourceChildNode.getChildren()) {
                                for (Node targetNode : targetChildNode.getChildren()) {
                                    if (!Objects.equals(targetNode.getAttributes().get("name"), node.getAttributes().get("name"))) continue;
                                    targetChildNode.getChildren().remove(targetNode);
                                    break;
                                }
                                targetChildNode.getChildren().add(node);
                            }
                            isMerged = true;
                            break;
                        }
                        case 3: {
                            targetLoggers = new HashMap<String, Node>();
                            for (Node node : targetChildNode.getChildren()) {
                                targetLoggers.put(node.getName(), node);
                            }
                            var13_13 = sourceChildNode.getChildren().iterator();
lbl52:
                            // 2 sources

                            while (true) {
                                if (var13_13.hasNext()) {
                                    node = var13_13.next();
                                    targetNode = this.getLoggerNode(targetChildNode, node.getAttributes().get("name"));
                                    loggerNode = new Node(targetChildNode, node.getName(), node.getType());
                                    if (targetNode != null) {
                                        targetNode.getAttributes().putAll(node.getAttributes());
                                        var17_17 = node.getChildren().iterator();
                                        break block27;
                                    }
                                    loggerNode.getAttributes().putAll(node.getAttributes());
                                    loggerNode.getChildren().addAll(node.getChildren());
                                    targetChildNode.getChildren().add(loggerNode);
                                    continue;
                                }
                                isMerged = true;
                                break block6;
                                break;
                            }
                        }
                        default: {
                            targetChildNode.getChildren().addAll(sourceChildNode.getChildren());
                            isMerged = true;
                            break;
                        }
                    }
                }
                if (isMerged) continue;
                if (sourceChildNode.getName().equalsIgnoreCase("Properties")) {
                    target.getChildren().add(0, sourceChildNode);
                    continue;
                }
                target.getChildren().add(sourceChildNode);
            }
            return;
        }
        while (true) {
            if (var17_17.hasNext()) ** break;
            ** continue;
            sourceLoggerChild = var17_17.next();
            if (this.isFilterNode(sourceLoggerChild)) {
                foundFilter = false;
                for (Node targetChild : targetNode.getChildren()) {
                    if (!this.isFilterNode(targetChild)) continue;
                    this.updateFilterNode(loggerNode, targetChild, sourceLoggerChild, pluginManager);
                    foundFilter = true;
                    break;
                }
                if (foundFilter) continue;
                childNode = new Node(loggerNode, sourceLoggerChild.getName(), sourceLoggerChild.getType());
                childNode.getAttributes().putAll(sourceLoggerChild.getAttributes());
                childNode.getChildren().addAll(sourceLoggerChild.getChildren());
                targetNode.getChildren().add(childNode);
                continue;
            }
            childNode = new Node(loggerNode, sourceLoggerChild.getName(), sourceLoggerChild.getType());
            childNode.getAttributes().putAll(sourceLoggerChild.getAttributes());
            childNode.getChildren().addAll(sourceLoggerChild.getChildren());
            if (childNode.getName().equalsIgnoreCase("AppenderRef")) {
                for (Node targetChild : targetNode.getChildren()) {
                    if (!this.isSameReference(targetChild, childNode)) continue;
                    targetNode.getChildren().remove(targetChild);
                    break;
                }
            } else {
                for (Node targetChild : targetNode.getChildren()) {
                    if (!this.isSameName(targetChild, childNode)) continue;
                    targetNode.getChildren().remove(targetChild);
                    break;
                }
            }
            targetNode.getChildren().add(childNode);
        }
    }

    private Node getLoggerNode(Node parentNode, String name) {
        for (Node node : parentNode.getChildren()) {
            String nodeName = node.getAttributes().get(NAME);
            if (name == null && nodeName == null) {
                return node;
            }
            if (nodeName == null || !nodeName.equals(name)) continue;
            return node;
        }
        return null;
    }

    private void updateFilterNode(Node target, Node targetChildNode, Node sourceChildNode, PluginManager pluginManager) {
        if (CompositeFilter.class.isAssignableFrom(targetChildNode.getType().getPluginClass())) {
            Node node = new Node(targetChildNode, sourceChildNode.getName(), sourceChildNode.getType());
            node.getChildren().addAll(sourceChildNode.getChildren());
            node.getAttributes().putAll(sourceChildNode.getAttributes());
            targetChildNode.getChildren().add(node);
        } else {
            PluginType<?> pluginType = pluginManager.getPluginType(FILTERS);
            Node filtersNode = new Node(targetChildNode, FILTERS, pluginType);
            Node node = new Node(filtersNode, sourceChildNode.getName(), sourceChildNode.getType());
            node.getAttributes().putAll(sourceChildNode.getAttributes());
            List<Node> children = filtersNode.getChildren();
            children.add(targetChildNode);
            children.add(node);
            List<Node> nodes = target.getChildren();
            nodes.remove(targetChildNode);
            nodes.add(filtersNode);
        }
    }

    private boolean isFilterNode(Node node) {
        return Filter.class.isAssignableFrom(node.getType().getPluginClass());
    }

    private boolean isSameName(Node node1, Node node2) {
        String value = node1.getAttributes().get(NAME);
        return value != null && value.toLowerCase().equals(node2.getAttributes().get(NAME).toLowerCase());
    }

    private boolean isSameReference(Node node1, Node node2) {
        String value = node1.getAttributes().get(REF);
        return value != null && value.toLowerCase().equals(node2.getAttributes().get(REF).toLowerCase());
    }
}

