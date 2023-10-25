/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.routing;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
import org.apache.logging.log4j.core.appender.routing.Route;
import org.apache.logging.log4j.core.appender.routing.Routes;
import org.apache.logging.log4j.core.config.AppenderControl;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;

@Plugin(name="Routing", category="Core", elementType="appender", printObject=true)
public final class RoutingAppender
extends AbstractAppender {
    private static final String DEFAULT_KEY = "ROUTING_APPENDER_DEFAULT";
    private final Routes routes;
    private final Route defaultRoute;
    private final Configuration config;
    private final ConcurrentMap<String, AppenderControl> appenders = new ConcurrentHashMap<String, AppenderControl>();
    private final RewritePolicy rewritePolicy;

    private RoutingAppender(String name, Filter filter, boolean ignoreExceptions, Routes routes, RewritePolicy rewritePolicy, Configuration config) {
        super(name, filter, null, ignoreExceptions);
        this.routes = routes;
        this.config = config;
        this.rewritePolicy = rewritePolicy;
        Route defRoute = null;
        for (Route route : routes.getRoutes()) {
            if (route.getKey() != null) continue;
            if (defRoute == null) {
                defRoute = route;
                continue;
            }
            this.error("Multiple default routes. Route " + route.toString() + " will be ignored");
        }
        this.defaultRoute = defRoute;
    }

    @Override
    public void start() {
        Map<String, Appender> map = this.config.getAppenders();
        for (Route route : this.routes.getRoutes()) {
            if (route.getAppenderRef() == null) continue;
            Appender appender = map.get(route.getAppenderRef());
            if (appender != null) {
                String key = route == this.defaultRoute ? DEFAULT_KEY : route.getKey();
                this.appenders.put(key, new AppenderControl(appender, null, null));
                continue;
            }
            LOGGER.error("Appender " + route.getAppenderRef() + " cannot be located. Route ignored");
        }
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        Map<String, Appender> map = this.config.getAppenders();
        for (Map.Entry entry : this.appenders.entrySet()) {
            String name = ((AppenderControl)entry.getValue()).getAppender().getName();
            if (map.containsKey(name)) continue;
            ((AppenderControl)entry.getValue()).getAppender().stop();
        }
    }

    @Override
    public void append(LogEvent event) {
        String key;
        AppenderControl control;
        if (this.rewritePolicy != null) {
            event = this.rewritePolicy.rewrite(event);
        }
        if ((control = this.getControl(key = this.config.getStrSubstitutor().replace(event, this.routes.getPattern()), event)) != null) {
            control.callAppender(event);
        }
    }

    private synchronized AppenderControl getControl(String key, LogEvent event) {
        AppenderControl control = (AppenderControl)this.appenders.get(key);
        if (control != null) {
            return control;
        }
        Route route = null;
        for (Route r : this.routes.getRoutes()) {
            if (r.getAppenderRef() != null || !key.equals(r.getKey())) continue;
            route = r;
            break;
        }
        if (route == null) {
            route = this.defaultRoute;
            control = (AppenderControl)this.appenders.get(DEFAULT_KEY);
            if (control != null) {
                return control;
            }
        }
        if (route != null) {
            Appender app = this.createAppender(route, event);
            if (app == null) {
                return null;
            }
            control = new AppenderControl(app, null, null);
            this.appenders.put(key, control);
        }
        return control;
    }

    private Appender createAppender(Route route, LogEvent event) {
        Node routeNode = route.getNode();
        for (Node node : routeNode.getChildren()) {
            if (!node.getType().getElementName().equals("appender")) continue;
            Node appNode = new Node(node);
            this.config.createConfiguration(appNode, event);
            if (appNode.getObject() instanceof Appender) {
                Appender app = (Appender)appNode.getObject();
                app.start();
                return app;
            }
            LOGGER.error("Unable to create Appender of type " + node.getName());
            return null;
        }
        LOGGER.error("No Appender was configured for route " + route.getKey());
        return null;
    }

    @PluginFactory
    public static RoutingAppender createAppender(@PluginAttribute(value="name") String name, @PluginAttribute(value="ignoreExceptions") String ignore, @PluginElement(value="Routes") Routes routes, @PluginConfiguration Configuration config, @PluginElement(value="RewritePolicy") RewritePolicy rewritePolicy, @PluginElement(value="Filters") Filter filter) {
        boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        if (name == null) {
            LOGGER.error("No name provided for RoutingAppender");
            return null;
        }
        if (routes == null) {
            LOGGER.error("No routes defined for RoutingAppender");
            return null;
        }
        return new RoutingAppender(name, filter, ignoreExceptions, routes, rewritePolicy, config);
    }
}

