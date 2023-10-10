// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.routing;

import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.Booleans;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.LogEvent;
import java.util.Iterator;
import org.apache.logging.log4j.core.LifeCycle2;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.Appender;
import javax.script.Bindings;
import org.apache.logging.log4j.core.script.ScriptManager;
import java.util.Objects;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.script.AbstractScript;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
import org.apache.logging.log4j.core.config.AppenderControl;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.appender.AbstractAppender;

@Plugin(name = "Routing", category = "Core", elementType = "appender", printObject = true)
public final class RoutingAppender extends AbstractAppender
{
    public static final String STATIC_VARIABLES_KEY = "staticVariables";
    private static final String DEFAULT_KEY = "ROUTING_APPENDER_DEFAULT";
    private final Routes routes;
    private Route defaultRoute;
    private final Configuration configuration;
    private final ConcurrentMap<String, CreatedRouteAppenderControl> createdAppenders;
    private final Map<String, AppenderControl> createdAppendersUnmodifiableView;
    private final ConcurrentMap<String, RouteAppenderControl> referencedAppenders;
    private final RewritePolicy rewritePolicy;
    private final PurgePolicy purgePolicy;
    private final AbstractScript defaultRouteScript;
    private final ConcurrentMap<Object, Object> scriptStaticVariables;
    
    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return new Builder<B>().asBuilder();
    }
    
    private RoutingAppender(final String name, final Filter filter, final boolean ignoreExceptions, final Routes routes, final RewritePolicy rewritePolicy, final Configuration configuration, final PurgePolicy purgePolicy, final AbstractScript defaultRouteScript, final Property[] properties) {
        super(name, filter, null, ignoreExceptions, properties);
        this.createdAppenders = new ConcurrentHashMap<String, CreatedRouteAppenderControl>();
        this.createdAppendersUnmodifiableView = Collections.unmodifiableMap((Map<? extends String, ? extends AppenderControl>)this.createdAppenders);
        this.referencedAppenders = new ConcurrentHashMap<String, RouteAppenderControl>();
        this.scriptStaticVariables = new ConcurrentHashMap<Object, Object>();
        this.routes = routes;
        this.configuration = configuration;
        this.rewritePolicy = rewritePolicy;
        this.purgePolicy = purgePolicy;
        if (this.purgePolicy != null) {
            this.purgePolicy.initialize(this);
        }
        this.defaultRouteScript = defaultRouteScript;
        Route defRoute = null;
        for (final Route route : routes.getRoutes()) {
            if (route.getKey() == null) {
                if (defRoute == null) {
                    defRoute = route;
                }
                else {
                    this.error("Multiple default routes. Route " + route.toString() + " will be ignored");
                }
            }
        }
        this.defaultRoute = defRoute;
    }
    
    @Override
    public void start() {
        if (this.defaultRouteScript != null) {
            if (this.configuration == null) {
                this.error("No Configuration defined for RoutingAppender; required for Script element.");
            }
            else {
                final ScriptManager scriptManager = this.configuration.getScriptManager();
                scriptManager.addScript(this.defaultRouteScript);
                final Bindings bindings = scriptManager.createBindings(this.defaultRouteScript);
                bindings.put("staticVariables", (Object)this.scriptStaticVariables);
                final Object object = scriptManager.execute(this.defaultRouteScript.getName(), bindings);
                final Route route = this.routes.getRoute(Objects.toString(object, null));
                if (route != null) {
                    this.defaultRoute = route;
                }
            }
        }
        final Route[] routes = this.routes.getRoutes();
        for (int length = routes.length, i = 0; i < length; ++i) {
            final Route route = routes[i];
            if (route.getAppenderRef() != null) {
                final Appender appender = this.configuration.getAppender(route.getAppenderRef());
                if (appender != null) {
                    final String key = (route == this.defaultRoute) ? "ROUTING_APPENDER_DEFAULT" : route.getKey();
                    this.referencedAppenders.put(key, new ReferencedRouteAppenderControl(appender));
                }
                else {
                    this.error("Appender " + route.getAppenderRef() + " cannot be located. Route ignored");
                }
            }
        }
        super.start();
    }
    
    @Override
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        this.setStopping();
        super.stop(timeout, timeUnit, false);
        for (final Map.Entry<String, CreatedRouteAppenderControl> entry : this.createdAppenders.entrySet()) {
            final Appender appender = entry.getValue().getAppender();
            if (appender instanceof LifeCycle2) {
                ((LifeCycle2)appender).stop(timeout, timeUnit);
            }
            else {
                appender.stop();
            }
        }
        this.setStopped();
        return true;
    }
    
    @Override
    public void append(LogEvent event) {
        if (this.rewritePolicy != null) {
            event = this.rewritePolicy.rewrite(event);
        }
        final String pattern = this.routes.getPattern(event, this.scriptStaticVariables);
        final String key = (pattern != null) ? this.configuration.getStrSubstitutor().replace(event, pattern) : ((this.defaultRoute.getKey() != null) ? this.defaultRoute.getKey() : "ROUTING_APPENDER_DEFAULT");
        final RouteAppenderControl control = this.getControl(key, event);
        if (control != null) {
            try {
                control.callAppender(event);
            }
            finally {
                control.release();
            }
        }
        this.updatePurgePolicy(key, event);
    }
    
    private void updatePurgePolicy(final String key, final LogEvent event) {
        if (this.purgePolicy != null && !this.referencedAppenders.containsKey(key)) {
            this.purgePolicy.update(key, event);
        }
    }
    
    private synchronized RouteAppenderControl getControl(final String key, final LogEvent event) {
        RouteAppenderControl control = this.getAppender(key);
        if (control != null) {
            control.checkout();
            return control;
        }
        Route route = null;
        for (final Route r : this.routes.getRoutes()) {
            if (r.getAppenderRef() == null && key.equals(r.getKey())) {
                route = r;
                break;
            }
        }
        if (route == null) {
            route = this.defaultRoute;
            control = this.getAppender("ROUTING_APPENDER_DEFAULT");
            if (control != null) {
                control.checkout();
                return control;
            }
        }
        if (route != null) {
            final Appender app = this.createAppender(route, event);
            if (app == null) {
                return null;
            }
            final CreatedRouteAppenderControl created = (CreatedRouteAppenderControl)(control = new CreatedRouteAppenderControl(app));
            this.createdAppenders.put(key, created);
        }
        if (control != null) {
            control.checkout();
        }
        return control;
    }
    
    private RouteAppenderControl getAppender(final String key) {
        final RouteAppenderControl result = this.referencedAppenders.get(key);
        if (result == null) {
            return this.createdAppenders.get(key);
        }
        return result;
    }
    
    private Appender createAppender(final Route route, final LogEvent event) {
        final Node routeNode = route.getNode();
        for (final Node node : routeNode.getChildren()) {
            if (node.getType().getElementName().equals("appender")) {
                final Node appNode = new Node(node);
                this.configuration.createConfiguration(appNode, event);
                if (appNode.getObject() instanceof Appender) {
                    final Appender app = appNode.getObject();
                    app.start();
                    return app;
                }
                this.error("Unable to create Appender of type " + node.getName());
                return null;
            }
        }
        this.error("No Appender was configured for route " + route.getKey());
        return null;
    }
    
    public Map<String, AppenderControl> getAppenders() {
        return this.createdAppendersUnmodifiableView;
    }
    
    public void deleteAppender(final String key) {
        RoutingAppender.LOGGER.debug("Deleting route with {} key ", key);
        final CreatedRouteAppenderControl control = this.createdAppenders.remove(key);
        if (null != control) {
            RoutingAppender.LOGGER.debug("Stopping route with {} key", key);
            synchronized (this) {
                control.pendingDeletion = true;
            }
            control.tryStopAppender();
        }
        else if (this.referencedAppenders.containsKey(key)) {
            RoutingAppender.LOGGER.debug("Route {} using an appender reference may not be removed because the appender may be used outside of the RoutingAppender", key);
        }
        else {
            RoutingAppender.LOGGER.debug("Route with {} key already deleted", key);
        }
    }
    
    @Deprecated
    public static RoutingAppender createAppender(final String name, final String ignore, final Routes routes, final Configuration config, final RewritePolicy rewritePolicy, final PurgePolicy purgePolicy, final Filter filter) {
        final boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        if (name == null) {
            RoutingAppender.LOGGER.error("No name provided for RoutingAppender");
            return null;
        }
        if (routes == null) {
            RoutingAppender.LOGGER.error("No routes defined for RoutingAppender");
            return null;
        }
        return new RoutingAppender(name, filter, ignoreExceptions, routes, rewritePolicy, config, purgePolicy, null, null);
    }
    
    public Route getDefaultRoute() {
        return this.defaultRoute;
    }
    
    public AbstractScript getDefaultRouteScript() {
        return this.defaultRouteScript;
    }
    
    public PurgePolicy getPurgePolicy() {
        return this.purgePolicy;
    }
    
    public RewritePolicy getRewritePolicy() {
        return this.rewritePolicy;
    }
    
    public Routes getRoutes() {
        return this.routes;
    }
    
    public Configuration getConfiguration() {
        return this.configuration;
    }
    
    public ConcurrentMap<Object, Object> getScriptStaticVariables() {
        return this.scriptStaticVariables;
    }
    
    public static class Builder<B extends Builder<B>> extends AbstractAppender.Builder<B> implements org.apache.logging.log4j.core.util.Builder<RoutingAppender>
    {
        @PluginElement("Script")
        private AbstractScript defaultRouteScript;
        @PluginElement("Routes")
        private Routes routes;
        @PluginElement("RewritePolicy")
        private RewritePolicy rewritePolicy;
        @PluginElement("PurgePolicy")
        private PurgePolicy purgePolicy;
        
        @Override
        public RoutingAppender build() {
            final String name = this.getName();
            if (name == null) {
                RoutingAppender.LOGGER.error("No name defined for this RoutingAppender");
                return null;
            }
            if (this.routes == null) {
                RoutingAppender.LOGGER.error("No routes defined for RoutingAppender {}", name);
                return null;
            }
            return new RoutingAppender(name, this.getFilter(), this.isIgnoreExceptions(), this.routes, this.rewritePolicy, this.getConfiguration(), this.purgePolicy, this.defaultRouteScript, this.getPropertyArray(), null);
        }
        
        public Routes getRoutes() {
            return this.routes;
        }
        
        public AbstractScript getDefaultRouteScript() {
            return this.defaultRouteScript;
        }
        
        public RewritePolicy getRewritePolicy() {
            return this.rewritePolicy;
        }
        
        public PurgePolicy getPurgePolicy() {
            return this.purgePolicy;
        }
        
        public B withRoutes(final Routes routes) {
            this.routes = routes;
            return this.asBuilder();
        }
        
        public B withDefaultRouteScript(final AbstractScript defaultRouteScript) {
            this.defaultRouteScript = defaultRouteScript;
            return this.asBuilder();
        }
        
        public B withRewritePolicy(final RewritePolicy rewritePolicy) {
            this.rewritePolicy = rewritePolicy;
            return this.asBuilder();
        }
        
        public void withPurgePolicy(final PurgePolicy purgePolicy) {
            this.purgePolicy = purgePolicy;
        }
    }
    
    private abstract static class RouteAppenderControl extends AppenderControl
    {
        RouteAppenderControl(final Appender appender) {
            super(appender, null, null);
        }
        
        abstract void checkout();
        
        abstract void release();
    }
    
    private static final class CreatedRouteAppenderControl extends RouteAppenderControl
    {
        private volatile boolean pendingDeletion;
        private final AtomicInteger depth;
        
        CreatedRouteAppenderControl(final Appender appender) {
            super(appender);
            this.depth = new AtomicInteger();
        }
        
        @Override
        void checkout() {
            if (this.pendingDeletion) {
                CreatedRouteAppenderControl.LOGGER.warn("CreatedRouteAppenderControl.checkout invoked on a RouteAppenderControl that is pending deletion");
            }
            this.depth.incrementAndGet();
        }
        
        @Override
        void release() {
            this.depth.decrementAndGet();
            this.tryStopAppender();
        }
        
        void tryStopAppender() {
            if (this.pendingDeletion && this.depth.compareAndSet(0, -100000)) {
                final Appender appender = this.getAppender();
                CreatedRouteAppenderControl.LOGGER.debug("Stopping appender {}", appender);
                appender.stop();
            }
        }
    }
    
    private static final class ReferencedRouteAppenderControl extends RouteAppenderControl
    {
        ReferencedRouteAppenderControl(final Appender appender) {
            super(appender);
        }
        
        @Override
        void checkout() {
        }
        
        @Override
        void release() {
        }
    }
}
