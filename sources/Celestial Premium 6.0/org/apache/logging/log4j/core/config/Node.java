/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.core.config.plugins.PluginType;

public class Node {
    private final Node parent;
    private final String name;
    private String value;
    private final PluginType<?> type;
    private final Map<String, String> attributes = new HashMap<String, String>();
    private final List<Node> children = new ArrayList<Node>();
    private Object object;

    public Node(Node parent, String name, PluginType<?> type) {
        this.parent = parent;
        this.name = name;
        this.type = type;
    }

    public Node() {
        this.parent = null;
        this.name = null;
        this.type = null;
    }

    public Node(Node node) {
        this.parent = node.parent;
        this.name = node.name;
        this.type = node.type;
        this.attributes.putAll(node.getAttributes());
        this.value = node.getValue();
        for (Node child : node.getChildren()) {
            this.children.add(new Node(child));
        }
        this.object = node.object;
    }

    public Map<String, String> getAttributes() {
        return this.attributes;
    }

    public List<Node> getChildren() {
        return this.children;
    }

    public boolean hasChildren() {
        return this.children.size() > 0;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Node getParent() {
        return this.parent;
    }

    public String getName() {
        return this.name;
    }

    public boolean isRoot() {
        return this.parent == null;
    }

    public void setObject(Object obj) {
        this.object = obj;
    }

    public Object getObject() {
        return this.object;
    }

    public PluginType<?> getType() {
        return this.type;
    }

    public String toString() {
        if (this.object == null) {
            return "null";
        }
        return this.type.isObjectPrintable() ? this.object.toString() : this.type.getPluginClass().getName() + " with name " + this.name;
    }
}

