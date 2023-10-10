// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.script.AbstractScript;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "scripts", category = "Core")
public final class ScriptsPlugin
{
    private ScriptsPlugin() {
    }
    
    @PluginFactory
    public static AbstractScript[] createScripts(@PluginElement("Scripts") final AbstractScript[] scripts) {
        return scripts;
    }
}
