/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.plugins.visitors;

import java.util.Map;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.visitors.AbstractPluginVisitor;
import org.apache.logging.log4j.core.util.NameUtil;
import org.apache.logging.log4j.util.StringBuilders;

public class PluginAttributeVisitor
extends AbstractPluginVisitor<PluginAttribute> {
    public PluginAttributeVisitor() {
        super(PluginAttribute.class);
    }

    @Override
    public Object visit(Configuration configuration, Node node, LogEvent event, StringBuilder log) {
        String name = ((PluginAttribute)this.annotation).value();
        Map<String, String> attributes = node.getAttributes();
        String rawValue = PluginAttributeVisitor.removeAttributeValue(attributes, name, this.aliases);
        String replacedValue = this.substitutor.replace(event, rawValue);
        Object defaultValue = this.findDefaultValue(event);
        Object value = this.convert(replacedValue, defaultValue);
        Object debugValue = ((PluginAttribute)this.annotation).sensitive() ? NameUtil.md5(value + this.getClass().getName()) : value;
        StringBuilders.appendKeyDqValue(log, name, debugValue);
        return value;
    }

    private Object findDefaultValue(LogEvent event) {
        if (this.conversionType == Integer.TYPE || this.conversionType == Integer.class) {
            return ((PluginAttribute)this.annotation).defaultInt();
        }
        if (this.conversionType == Long.TYPE || this.conversionType == Long.class) {
            return ((PluginAttribute)this.annotation).defaultLong();
        }
        if (this.conversionType == Boolean.TYPE || this.conversionType == Boolean.class) {
            return ((PluginAttribute)this.annotation).defaultBoolean();
        }
        if (this.conversionType == Float.TYPE || this.conversionType == Float.class) {
            return Float.valueOf(((PluginAttribute)this.annotation).defaultFloat());
        }
        if (this.conversionType == Double.TYPE || this.conversionType == Double.class) {
            return ((PluginAttribute)this.annotation).defaultDouble();
        }
        if (this.conversionType == Byte.TYPE || this.conversionType == Byte.class) {
            return ((PluginAttribute)this.annotation).defaultByte();
        }
        if (this.conversionType == Character.TYPE || this.conversionType == Character.class) {
            return Character.valueOf(((PluginAttribute)this.annotation).defaultChar());
        }
        if (this.conversionType == Short.TYPE || this.conversionType == Short.class) {
            return ((PluginAttribute)this.annotation).defaultShort();
        }
        if (this.conversionType == Class.class) {
            return ((PluginAttribute)this.annotation).defaultClass();
        }
        return this.substitutor.replace(event, ((PluginAttribute)this.annotation).defaultString());
    }
}

