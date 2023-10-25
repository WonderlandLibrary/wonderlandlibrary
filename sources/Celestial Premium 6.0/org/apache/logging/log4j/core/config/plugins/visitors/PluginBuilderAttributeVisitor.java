/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.plugins.visitors;

import java.util.Map;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.visitors.AbstractPluginVisitor;
import org.apache.logging.log4j.core.util.NameUtil;
import org.apache.logging.log4j.util.StringBuilders;

public class PluginBuilderAttributeVisitor
extends AbstractPluginVisitor<PluginBuilderAttribute> {
    public PluginBuilderAttributeVisitor() {
        super(PluginBuilderAttribute.class);
    }

    @Override
    public Object visit(Configuration configuration, Node node, LogEvent event, StringBuilder log) {
        String overridden = ((PluginBuilderAttribute)this.annotation).value();
        String name = overridden.isEmpty() ? this.member.getName() : overridden;
        Map<String, String> attributes = node.getAttributes();
        String rawValue = PluginBuilderAttributeVisitor.removeAttributeValue(attributes, name, this.aliases);
        String replacedValue = this.substitutor.replace(event, rawValue);
        Object value = this.convert(replacedValue, null);
        Object debugValue = ((PluginBuilderAttribute)this.annotation).sensitive() ? NameUtil.md5(value + this.getClass().getName()) : value;
        StringBuilders.appendKeyDqValue(log, name, debugValue);
        return value;
    }
}

