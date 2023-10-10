// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.composite;

import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
import org.apache.logging.log4j.core.config.AbstractConfiguration;
import org.apache.logging.log4j.core.config.Node;

public interface MergeStrategy
{
    void mergeRootProperties(final Node rootNode, final AbstractConfiguration configuration);
    
    void mergConfigurations(final Node target, final Node source, final PluginManager pluginManager);
}
