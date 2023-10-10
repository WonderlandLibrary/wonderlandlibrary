// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.script;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginValue;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "Script", category = "Core", printObject = true)
public class Script extends AbstractScript
{
    private static final String ATTR_LANGUAGE = "language";
    private static final String ATTR_SCRIPT_TEXT = "scriptText";
    static final String PLUGIN_NAME = "Script";
    
    public Script(final String name, final String language, final String scriptText) {
        super(name, language, scriptText);
    }
    
    @PluginFactory
    public static Script createScript(@PluginAttribute("name") final String name, @PluginAttribute("language") String language, @PluginValue("scriptText") final String scriptText) {
        if (language == null) {
            Script.LOGGER.error("No '{}' attribute provided for {} plugin '{}'", "language", "Script", name);
            language = "JavaScript";
        }
        if (scriptText == null) {
            Script.LOGGER.error("No '{}' attribute provided for {} plugin '{}'", "scriptText", "Script", name);
            return null;
        }
        return new Script(name, language, scriptText);
    }
    
    @Override
    public String toString() {
        return (this.getName() != null) ? this.getName() : super.toString();
    }
}
