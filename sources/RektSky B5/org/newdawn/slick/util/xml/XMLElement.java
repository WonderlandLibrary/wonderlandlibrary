/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.util.xml;

import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElementList;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class XMLElement {
    private Element dom;
    private XMLElementList children;
    private String name;

    XMLElement(Element xmlElement) {
        this.dom = xmlElement;
        this.name = this.dom.getTagName();
    }

    public String[] getAttributeNames() {
        NamedNodeMap map = this.dom.getAttributes();
        String[] names = new String[map.getLength()];
        for (int i2 = 0; i2 < names.length; ++i2) {
            names[i2] = map.item(i2).getNodeName();
        }
        return names;
    }

    public String getName() {
        return this.name;
    }

    public String getAttribute(String name) {
        return this.dom.getAttribute(name);
    }

    public String getAttribute(String name, String def) {
        String value = this.dom.getAttribute(name);
        if (value == null || value.length() == 0) {
            return def;
        }
        return value;
    }

    public int getIntAttribute(String name) throws SlickXMLException {
        try {
            return Integer.parseInt(this.getAttribute(name));
        }
        catch (NumberFormatException e2) {
            throw new SlickXMLException("Value read: '" + this.getAttribute(name) + "' is not an integer", e2);
        }
    }

    public int getIntAttribute(String name, int def) throws SlickXMLException {
        try {
            return Integer.parseInt(this.getAttribute(name, "" + def));
        }
        catch (NumberFormatException e2) {
            throw new SlickXMLException("Value read: '" + this.getAttribute(name, "" + def) + "' is not an integer", e2);
        }
    }

    public double getDoubleAttribute(String name) throws SlickXMLException {
        try {
            return Double.parseDouble(this.getAttribute(name));
        }
        catch (NumberFormatException e2) {
            throw new SlickXMLException("Value read: '" + this.getAttribute(name) + "' is not a double", e2);
        }
    }

    public double getDoubleAttribute(String name, double def) throws SlickXMLException {
        try {
            return Double.parseDouble(this.getAttribute(name, "" + def));
        }
        catch (NumberFormatException e2) {
            throw new SlickXMLException("Value read: '" + this.getAttribute(name, "" + def) + "' is not a double", e2);
        }
    }

    public boolean getBooleanAttribute(String name) throws SlickXMLException {
        String value = this.getAttribute(name);
        if (value.equalsIgnoreCase("true")) {
            return true;
        }
        if (value.equalsIgnoreCase("false")) {
            return false;
        }
        throw new SlickXMLException("Value read: '" + this.getAttribute(name) + "' is not a boolean");
    }

    public boolean getBooleanAttribute(String name, boolean def) throws SlickXMLException {
        String value = this.getAttribute(name, "" + def);
        if (value.equalsIgnoreCase("true")) {
            return true;
        }
        if (value.equalsIgnoreCase("false")) {
            return false;
        }
        throw new SlickXMLException("Value read: '" + this.getAttribute(name, "" + def) + "' is not a boolean");
    }

    public String getContent() {
        String content = "";
        NodeList list = this.dom.getChildNodes();
        for (int i2 = 0; i2 < list.getLength(); ++i2) {
            if (!(list.item(i2) instanceof Text)) continue;
            content = content + list.item(i2).getNodeValue();
        }
        return content;
    }

    public XMLElementList getChildren() {
        if (this.children != null) {
            return this.children;
        }
        NodeList list = this.dom.getChildNodes();
        this.children = new XMLElementList();
        for (int i2 = 0; i2 < list.getLength(); ++i2) {
            if (!(list.item(i2) instanceof Element)) continue;
            this.children.add(new XMLElement((Element)list.item(i2)));
        }
        return this.children;
    }

    public XMLElementList getChildrenByName(String name) {
        XMLElementList selected = new XMLElementList();
        XMLElementList children = this.getChildren();
        for (int i2 = 0; i2 < children.size(); ++i2) {
            if (!children.get(i2).getName().equals(name)) continue;
            selected.add(children.get(i2));
        }
        return selected;
    }

    public String toString() {
        String value = "[XML " + this.getName();
        String[] attrs = this.getAttributeNames();
        for (int i2 = 0; i2 < attrs.length; ++i2) {
            value = value + " " + attrs[i2] + "=" + this.getAttribute(attrs[i2]);
        }
        value = value + "]";
        return value;
    }
}

