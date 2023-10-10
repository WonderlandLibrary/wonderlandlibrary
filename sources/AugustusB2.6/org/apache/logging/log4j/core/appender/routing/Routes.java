// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.routing;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.status.StatusLogger;
import javax.script.Bindings;
import org.apache.logging.log4j.core.script.ScriptManager;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.script.AbstractScript;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "Routes", category = "Core", printObject = true)
public final class Routes
{
    private static final String LOG_EVENT_KEY = "logEvent";
    private static final Logger LOGGER;
    private final Configuration configuration;
    private final String pattern;
    private final AbstractScript patternScript;
    private final Route[] routes;
    
    @Deprecated
    public static Routes createRoutes(final String pattern, final Route... routes) {
        if (routes == null || routes.length == 0) {
            Routes.LOGGER.error("No routes configured");
            return null;
        }
        return new Routes(null, null, pattern, routes);
    }
    
    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }
    
    private Routes(final Configuration configuration, final AbstractScript patternScript, final String pattern, final Route... routes) {
        this.configuration = configuration;
        this.patternScript = patternScript;
        this.pattern = pattern;
        this.routes = routes;
    }
    
    public String getPattern(final LogEvent event, final ConcurrentMap<Object, Object> scriptStaticVariables) {
        if (this.patternScript != null) {
            final ScriptManager scriptManager = this.configuration.getScriptManager();
            final Bindings bindings = scriptManager.createBindings(this.patternScript);
            bindings.put("staticVariables", (Object)scriptStaticVariables);
            bindings.put("logEvent", (Object)event);
            final Object object = scriptManager.execute(this.patternScript.getName(), bindings);
            bindings.remove("logEvent");
            return Objects.toString(object, null);
        }
        return this.pattern;
    }
    
    public AbstractScript getPatternScript() {
        return this.patternScript;
    }
    
    public Route getRoute(final String key) {
        for (final Route route : this.routes) {
            if (Objects.equals(route.getKey(), key)) {
                return route;
            }
        }
        return null;
    }
    
    public Route[] getRoutes() {
        return this.routes;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (final Route route : this.routes) {
            if (!first) {
                sb.append(',');
            }
            first = false;
            sb.append(route.toString());
        }
        sb.append('}');
        return sb.toString();
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
    
    public static class Builder implements org.apache.logging.log4j.core.util.Builder<Routes>
    {
        @PluginConfiguration
        private Configuration configuration;
        @PluginAttribute("pattern")
        private String pattern;
        @PluginElement("Script")
        private AbstractScript patternScript;
        @PluginElement("Routes")
        @Required
        private Route[] routes;
        
        @Override
        public Routes build() {
            if (this.routes == null || this.routes.length == 0) {
                Routes.LOGGER.error("No Routes configured.");
                return null;
            }
            if ((this.patternScript != null && this.pattern != null) || (this.patternScript == null && this.pattern == null)) {
                Routes.LOGGER.warn("In a Routes element, you must configure either a Script element or a pattern attribute.");
            }
            if (this.patternScript != null) {
                if (this.configuration == null) {
                    Routes.LOGGER.error("No Configuration defined for Routes; required for Script");
                }
                else {
                    this.configuration.getScriptManager().addScript(this.patternScript);
                }
            }
            return new Routes(this.configuration, this.patternScript, this.pattern, this.routes, null);
        }
        
        public Configuration getConfiguration() {
            return this.configuration;
        }
        
        public String getPattern() {
            return this.pattern;
        }
        
        public AbstractScript getPatternScript() {
            return this.patternScript;
        }
        
        public Route[] getRoutes() {
            return this.routes;
        }
        
        public Builder withConfiguration(final Configuration configuration) {
            this.configuration = configuration;
            return this;
        }
        
        public Builder withPattern(final String pattern) {
            this.pattern = pattern;
            return this;
        }
        
        public Builder withPatternScript(final AbstractScript patternScript) {
            this.patternScript = patternScript;
            return this;
        }
        
        public Builder withRoutes(final Route[] routes) {
            this.routes = routes;
            return this;
        }
    }
}
