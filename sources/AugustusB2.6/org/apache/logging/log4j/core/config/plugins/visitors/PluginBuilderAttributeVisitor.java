// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.plugins.visitors;

import java.util.Map;
import org.apache.logging.log4j.util.StringBuilders;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;

public class PluginBuilderAttributeVisitor extends AbstractPluginVisitor<PluginBuilderAttribute>
{
    public PluginBuilderAttributeVisitor() {
        super(PluginBuilderAttribute.class);
    }
    
    @Override
    public Object visit(final Configuration configuration, final Node node, final LogEvent event, final StringBuilder log) {
        final String overridden = ((PluginBuilderAttribute)this.annotation).value();
        final String name = overridden.isEmpty() ? this.member.getName() : overridden;
        final Map<String, String> attributes = node.getAttributes();
        final String rawValue = AbstractPluginVisitor.removeAttributeValue(attributes, name, this.aliases);
        final String replacedValue = this.substitutor.replace(event, rawValue);
        final Object value = this.convert(replacedValue, null);
        final Object debugValue = ((PluginBuilderAttribute)this.annotation).sensitive() ? "*****" : value;
        StringBuilders.appendKeyDqValue(log, name, debugValue);
        return value;
    }
}
