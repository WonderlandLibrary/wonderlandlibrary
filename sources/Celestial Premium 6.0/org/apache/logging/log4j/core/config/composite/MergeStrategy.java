/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.composite;

import org.apache.logging.log4j.core.config.AbstractConfiguration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.util.PluginManager;

public interface MergeStrategy {
    public void mergeRootProperties(Node var1, AbstractConfiguration var2);

    public void mergConfigurations(Node var1, Node var2, PluginManager var3);
}

