// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.plugins.visitors;

import java.util.Map;
import org.apache.logging.log4j.util.StringBuilders;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;

public class PluginAttributeVisitor extends AbstractPluginVisitor<PluginAttribute>
{
    public PluginAttributeVisitor() {
        super(PluginAttribute.class);
    }
    
    @Override
    public Object visit(final Configuration configuration, final Node node, final LogEvent event, final StringBuilder log) {
        final String name = ((PluginAttribute)this.annotation).value();
        final Map<String, String> attributes = node.getAttributes();
        final String rawValue = AbstractPluginVisitor.removeAttributeValue(attributes, name, this.aliases);
        final String replacedValue = this.substitutor.replace(event, rawValue);
        final Object defaultValue = this.findDefaultValue(event);
        final Object value = this.convert(replacedValue, defaultValue);
        final Object debugValue = ((PluginAttribute)this.annotation).sensitive() ? "*****" : value;
        StringBuilders.appendKeyDqValue(log, name, debugValue);
        return value;
    }
    
    private Object findDefaultValue(final LogEvent event) {
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
            return ((PluginAttribute)this.annotation).defaultFloat();
        }
        if (this.conversionType == Double.TYPE || this.conversionType == Double.class) {
            return ((PluginAttribute)this.annotation).defaultDouble();
        }
        if (this.conversionType == Byte.TYPE || this.conversionType == Byte.class) {
            return ((PluginAttribute)this.annotation).defaultByte();
        }
        if (this.conversionType == Character.TYPE || this.conversionType == Character.class) {
            return ((PluginAttribute)this.annotation).defaultChar();
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
