/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.script;

import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginValue;
import org.apache.logging.log4j.core.script.AbstractScript;

@Plugin(name="Script", category="Core", printObject=true)
public class Script
extends AbstractScript {
    private static final String ATTR_LANGUAGE = "language";
    private static final String ATTR_SCRIPT_TEXT = "scriptText";
    static final String PLUGIN_NAME = "Script";

    public Script(String name, String language, String scriptText) {
        super(name, language, scriptText);
    }

    @PluginFactory
    public static Script createScript(@PluginAttribute(value="name") String name, @PluginAttribute(value="language") String language, @PluginValue(value="scriptText") String scriptText) {
        if (language == null) {
            LOGGER.error("No '{}' attribute provided for {} plugin '{}'", ATTR_LANGUAGE, PLUGIN_NAME, name);
            language = "JavaScript";
        }
        if (scriptText == null) {
            LOGGER.error("No '{}' attribute provided for {} plugin '{}'", ATTR_SCRIPT_TEXT, PLUGIN_NAME, name);
            return null;
        }
        return new Script(name, language, scriptText);
    }

    public String toString() {
        return this.getName() != null ? this.getName() : super.toString();
    }
}

