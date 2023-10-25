/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.script;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.script.AbstractScript;
import org.apache.logging.log4j.core.script.ScriptManager;

@Plugin(name="ScriptRef", category="Core", printObject=true)
public class ScriptRef
extends AbstractScript {
    private final ScriptManager scriptManager;

    public ScriptRef(String name, ScriptManager scriptManager) {
        super(name, null, null);
        this.scriptManager = scriptManager;
    }

    @Override
    public String getLanguage() {
        AbstractScript script = this.scriptManager.getScript(this.getName());
        return script != null ? script.getLanguage() : null;
    }

    @Override
    public String getScriptText() {
        AbstractScript script = this.scriptManager.getScript(this.getName());
        return script != null ? script.getScriptText() : null;
    }

    @PluginFactory
    public static ScriptRef createReference(@PluginAttribute(value="ref") String name, @PluginConfiguration Configuration configuration) {
        if (name == null) {
            LOGGER.error("No script name provided");
            return null;
        }
        return new ScriptRef(name, configuration.getScriptManager());
    }

    public String toString() {
        return "ref=" + this.getName();
    }
}

