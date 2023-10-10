// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.plugins.visitors;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.PluginNode;

public class PluginNodeVisitor extends AbstractPluginVisitor<PluginNode>
{
    public PluginNodeVisitor() {
        super(PluginNode.class);
    }
    
    @Override
    public Object visit(final Configuration configuration, final Node node, final LogEvent event, final StringBuilder log) {
        if (this.conversionType.isInstance(node)) {
            log.append("Node=").append(node.getName());
            return node;
        }
        PluginNodeVisitor.LOGGER.warn("Variable annotated with @PluginNode is not compatible with the type {}.", node.getClass());
        return null;
    }
}
