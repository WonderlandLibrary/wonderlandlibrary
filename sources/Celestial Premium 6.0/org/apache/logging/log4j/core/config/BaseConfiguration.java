/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationListener;
import org.apache.logging.log4j.core.config.ConfigurationMonitor;
import org.apache.logging.log4j.core.config.DefaultAdvertiser;
import org.apache.logging.log4j.core.config.DefaultConfigurationMonitor;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.Loggers;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.PluginAliases;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginManager;
import org.apache.logging.log4j.core.config.plugins.PluginNode;
import org.apache.logging.log4j.core.config.plugins.PluginType;
import org.apache.logging.log4j.core.config.plugins.PluginValue;
import org.apache.logging.log4j.core.filter.AbstractFilterable;
import org.apache.logging.log4j.core.helpers.NameUtil;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.lookup.Interpolator;
import org.apache.logging.log4j.core.lookup.MapLookup;
import org.apache.logging.log4j.core.lookup.StrLookup;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

public class BaseConfiguration
extends AbstractFilterable
implements Configuration {
    protected static final Logger LOGGER = StatusLogger.getLogger();
    protected Node rootNode;
    protected final List<ConfigurationListener> listeners = new CopyOnWriteArrayList<ConfigurationListener>();
    protected ConfigurationMonitor monitor = new DefaultConfigurationMonitor();
    private Advertiser advertiser = new DefaultAdvertiser();
    protected Map<String, String> advertisedConfiguration;
    private Node advertiserNode = null;
    private Object advertisement;
    protected boolean isShutdownHookEnabled = true;
    private String name;
    private ConcurrentMap<String, Appender> appenders = new ConcurrentHashMap<String, Appender>();
    private ConcurrentMap<String, LoggerConfig> loggers = new ConcurrentHashMap<String, LoggerConfig>();
    private final StrLookup tempLookup = new Interpolator();
    private final StrSubstitutor subst = new StrSubstitutor(this.tempLookup);
    private LoggerConfig root = new LoggerConfig();
    private final boolean started = false;
    private final ConcurrentMap<String, Object> componentMap = new ConcurrentHashMap<String, Object>();
    protected PluginManager pluginManager = new PluginManager("Core");

    protected BaseConfiguration() {
        this.rootNode = new Node();
    }

    @Override
    public Map<String, String> getProperties() {
        return (Map)this.componentMap.get("ContextProperties");
    }

    @Override
    public void start() {
        this.pluginManager.collectPlugins();
        this.setup();
        this.setupAdvertisement();
        this.doConfigure();
        for (LoggerConfig logger : this.loggers.values()) {
            logger.startFilter();
        }
        for (Appender appender : this.appenders.values()) {
            appender.start();
        }
        this.root.startFilter();
        this.startFilter();
    }

    @Override
    public void stop() {
        Appender[] array = this.appenders.values().toArray(new Appender[this.appenders.size()]);
        for (int i = array.length - 1; i >= 0; --i) {
            array[i].stop();
        }
        for (LoggerConfig logger : this.loggers.values()) {
            logger.clearAppenders();
            logger.stopFilter();
        }
        this.root.stopFilter();
        this.stopFilter();
        if (this.advertiser != null && this.advertisement != null) {
            this.advertiser.unadvertise(this.advertisement);
        }
    }

    @Override
    public boolean isShutdownHookEnabled() {
        return this.isShutdownHookEnabled;
    }

    protected void setup() {
    }

    protected Level getDefaultStatus() {
        String statusLevel = PropertiesUtil.getProperties().getStringProperty("Log4jDefaultStatusLevel", Level.ERROR.name());
        try {
            return Level.toLevel(statusLevel);
        }
        catch (Exception ex) {
            return Level.ERROR;
        }
    }

    protected void createAdvertiser(String advertiserString, ConfigurationFactory.ConfigurationSource configSource, byte[] buffer, String contentType) {
        if (advertiserString != null) {
            Node node = new Node(null, advertiserString, null);
            Map<String, String> attributes = node.getAttributes();
            attributes.put("content", new String(buffer));
            attributes.put("contentType", contentType);
            attributes.put("name", "configuration");
            if (configSource.getLocation() != null) {
                attributes.put("location", configSource.getLocation());
            }
            this.advertiserNode = node;
        }
    }

    private void setupAdvertisement() {
        String name;
        PluginType<?> type;
        if (this.advertiserNode != null && (type = this.pluginManager.getPluginType(name = this.advertiserNode.getName())) != null) {
            Class<?> clazz = type.getPluginClass();
            try {
                this.advertiser = (Advertiser)clazz.newInstance();
                this.advertisement = this.advertiser.advertise(this.advertiserNode.getAttributes());
            }
            catch (InstantiationException e) {
                System.err.println("InstantiationException attempting to instantiate advertiser: " + name);
            }
            catch (IllegalAccessException e) {
                System.err.println("IllegalAccessException attempting to instantiate advertiser: " + name);
            }
        }
    }

    public Object getComponent(String name) {
        return this.componentMap.get(name);
    }

    @Override
    public void addComponent(String name, Object obj) {
        this.componentMap.putIfAbsent(name, obj);
    }

    protected void doConfigure() {
        Object l;
        boolean setRoot = false;
        boolean setLoggers = false;
        for (Node node : this.rootNode.getChildren()) {
            this.createConfiguration(node, null);
            if (node.getObject() == null) continue;
            if (node.getName().equalsIgnoreCase("Properties")) {
                if (this.tempLookup == this.subst.getVariableResolver()) {
                    this.subst.setVariableResolver((StrLookup)node.getObject());
                    continue;
                }
                LOGGER.error("Properties declaration must be the first element in the configuration");
                continue;
            }
            if (this.tempLookup == this.subst.getVariableResolver()) {
                Map map = (Map)this.componentMap.get("ContextProperties");
                MapLookup lookup = map == null ? null : new MapLookup(map);
                this.subst.setVariableResolver(new Interpolator(lookup));
            }
            if (node.getName().equalsIgnoreCase("Appenders")) {
                this.appenders = (ConcurrentMap)node.getObject();
                continue;
            }
            if (node.getObject() instanceof Filter) {
                this.addFilter((Filter)node.getObject());
                continue;
            }
            if (node.getName().equalsIgnoreCase("Loggers")) {
                l = (Loggers)node.getObject();
                this.loggers = ((Loggers)l).getMap();
                setLoggers = true;
                if (((Loggers)l).getRoot() == null) continue;
                this.root = ((Loggers)l).getRoot();
                setRoot = true;
                continue;
            }
            LOGGER.error("Unknown object \"" + node.getName() + "\" of type " + node.getObject().getClass().getName() + " is ignored");
        }
        if (!setLoggers) {
            LOGGER.warn("No Loggers were configured, using default. Is the Loggers element missing?");
            this.setToDefault();
            return;
        }
        if (!setRoot) {
            LOGGER.warn("No Root logger was configured, creating default ERROR-level Root logger with Console appender");
            this.setToDefault();
        }
        for (Map.Entry entry : this.loggers.entrySet()) {
            l = (LoggerConfig)entry.getValue();
            for (AppenderRef ref : ((LoggerConfig)l).getAppenderRefs()) {
                Appender app = (Appender)this.appenders.get(ref.getRef());
                if (app != null) {
                    ((LoggerConfig)l).addAppender(app, ref.getLevel(), ref.getFilter());
                    continue;
                }
                LOGGER.error("Unable to locate appender " + ref.getRef() + " for logger " + ((LoggerConfig)l).getName());
            }
        }
        this.setParents();
    }

    private void setToDefault() {
        this.setName("Default");
        PatternLayout layout = PatternLayout.createLayout("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n", null, null, null, null);
        ConsoleAppender appender = ConsoleAppender.createAppender(layout, null, "SYSTEM_OUT", "Console", "false", "true");
        appender.start();
        this.addAppender(appender);
        LoggerConfig root = this.getRootLogger();
        root.addAppender(appender, null, null);
        String levelName = PropertiesUtil.getProperties().getStringProperty("org.apache.logging.log4j.level");
        Level level = levelName != null && Level.valueOf(levelName) != null ? Level.valueOf(levelName) : Level.ERROR;
        root.setLevel(level);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void addListener(ConfigurationListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(ConfigurationListener listener) {
        this.listeners.remove(listener);
    }

    public Appender getAppender(String name) {
        return (Appender)this.appenders.get(name);
    }

    @Override
    public Map<String, Appender> getAppenders() {
        return this.appenders;
    }

    public void addAppender(Appender appender) {
        this.appenders.put(appender.getName(), appender);
    }

    @Override
    public StrSubstitutor getStrSubstitutor() {
        return this.subst;
    }

    @Override
    public void setConfigurationMonitor(ConfigurationMonitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public ConfigurationMonitor getConfigurationMonitor() {
        return this.monitor;
    }

    @Override
    public void setAdvertiser(Advertiser advertiser) {
        this.advertiser = advertiser;
    }

    @Override
    public Advertiser getAdvertiser() {
        return this.advertiser;
    }

    @Override
    public synchronized void addLoggerAppender(org.apache.logging.log4j.core.Logger logger, Appender appender) {
        String name = logger.getName();
        this.appenders.putIfAbsent(appender.getName(), appender);
        LoggerConfig lc = this.getLoggerConfig(name);
        if (lc.getName().equals(name)) {
            lc.addAppender(appender, null, null);
        } else {
            LoggerConfig nlc = new LoggerConfig(name, lc.getLevel(), lc.isAdditive());
            nlc.addAppender(appender, null, null);
            nlc.setParent(lc);
            this.loggers.putIfAbsent(name, nlc);
            this.setParents();
            logger.getContext().updateLoggers();
        }
    }

    @Override
    public synchronized void addLoggerFilter(org.apache.logging.log4j.core.Logger logger, Filter filter) {
        String name = logger.getName();
        LoggerConfig lc = this.getLoggerConfig(name);
        if (lc.getName().equals(name)) {
            lc.addFilter(filter);
        } else {
            LoggerConfig nlc = new LoggerConfig(name, lc.getLevel(), lc.isAdditive());
            nlc.addFilter(filter);
            nlc.setParent(lc);
            this.loggers.putIfAbsent(name, nlc);
            this.setParents();
            logger.getContext().updateLoggers();
        }
    }

    @Override
    public synchronized void setLoggerAdditive(org.apache.logging.log4j.core.Logger logger, boolean additive) {
        String name = logger.getName();
        LoggerConfig lc = this.getLoggerConfig(name);
        if (lc.getName().equals(name)) {
            lc.setAdditive(additive);
        } else {
            LoggerConfig nlc = new LoggerConfig(name, lc.getLevel(), additive);
            nlc.setParent(lc);
            this.loggers.putIfAbsent(name, nlc);
            this.setParents();
            logger.getContext().updateLoggers();
        }
    }

    public synchronized void removeAppender(String name) {
        for (LoggerConfig logger : this.loggers.values()) {
            logger.removeAppender(name);
        }
        Appender app = (Appender)this.appenders.remove(name);
        if (app != null) {
            app.stop();
        }
    }

    @Override
    public LoggerConfig getLoggerConfig(String name) {
        if (this.loggers.containsKey(name)) {
            return (LoggerConfig)this.loggers.get(name);
        }
        String substr = name;
        while ((substr = NameUtil.getSubName(substr)) != null) {
            if (!this.loggers.containsKey(substr)) continue;
            return (LoggerConfig)this.loggers.get(substr);
        }
        return this.root;
    }

    public LoggerConfig getRootLogger() {
        return this.root;
    }

    @Override
    public Map<String, LoggerConfig> getLoggers() {
        return Collections.unmodifiableMap(this.loggers);
    }

    public LoggerConfig getLogger(String name) {
        return (LoggerConfig)this.loggers.get(name);
    }

    public void addLogger(String name, LoggerConfig loggerConfig) {
        this.loggers.put(name, loggerConfig);
        this.setParents();
    }

    public void removeLogger(String name) {
        this.loggers.remove(name);
        this.setParents();
    }

    @Override
    public void createConfiguration(Node node, LogEvent event) {
        PluginType<?> type = node.getType();
        if (type != null && type.isDeferChildren()) {
            node.setObject(this.createPluginObject(type, node, event));
        } else {
            for (Node child : node.getChildren()) {
                this.createConfiguration(child, event);
            }
            if (type == null) {
                if (node.getParent() != null) {
                    LOGGER.error("Unable to locate plugin for " + node.getName());
                }
            } else {
                node.setObject(this.createPluginObject(type, node, event));
            }
        }
    }

    private <T> Object createPluginObject(PluginType<T> type, Node node, LogEvent event) {
        Class<?>[] parmClasses;
        Class<T> clazz = type.getPluginClass();
        if (Map.class.isAssignableFrom(clazz)) {
            try {
                Map map = (Map)clazz.newInstance();
                for (Node child : node.getChildren()) {
                    map.put(child.getName(), child.getObject());
                }
                return map;
            }
            catch (Exception ex) {
                LOGGER.warn("Unable to create Map for " + type.getElementName() + " of class " + clazz);
            }
        }
        if (List.class.isAssignableFrom(clazz)) {
            try {
                List list = (List)clazz.newInstance();
                for (Node child : node.getChildren()) {
                    list.add(child.getObject());
                }
                return list;
            }
            catch (Exception ex) {
                LOGGER.warn("Unable to create List for " + type.getElementName() + " of class " + clazz);
            }
        }
        Method factoryMethod = null;
        for (Method method : clazz.getMethods()) {
            if (!method.isAnnotationPresent(PluginFactory.class)) continue;
            factoryMethod = method;
            break;
        }
        if (factoryMethod == null) {
            return null;
        }
        Annotation[][] parmArray = factoryMethod.getParameterAnnotations();
        if (parmArray.length != (parmClasses = factoryMethod.getParameterTypes()).length) {
            LOGGER.error("Number of parameter annotations does not equal the number of paramters");
        }
        Object[] parms = new Object[parmClasses.length];
        int index = 0;
        Map<String, String> attrs = node.getAttributes();
        List<Node> children = node.getChildren();
        StringBuilder sb = new StringBuilder();
        ArrayList<Node> used = new ArrayList<Node>();
        for (Annotation[] parmTypes : parmArray) {
            String[] aliases = null;
            for (Annotation a : parmTypes) {
                if (!(a instanceof PluginAliases)) continue;
                aliases = ((PluginAliases)a).value();
            }
            for (Annotation a : parmTypes) {
                Class<?> parmClass;
                String name;
                String value;
                if (a instanceof PluginAliases) continue;
                if (sb.length() == 0) {
                    sb.append(" with params(");
                } else {
                    sb.append(", ");
                }
                if (a instanceof PluginNode) {
                    parms[index] = node;
                    sb.append("Node=").append(node.getName());
                    continue;
                }
                if (a instanceof PluginConfiguration) {
                    parms[index] = this;
                    if (this.name != null) {
                        sb.append("Configuration(").append(this.name).append(")");
                        continue;
                    }
                    sb.append("Configuration");
                    continue;
                }
                if (a instanceof PluginValue) {
                    String name2 = ((PluginValue)a).value();
                    String v = node.getValue();
                    if (v == null) {
                        v = this.getAttrValue("value", null, attrs);
                    }
                    value = this.subst.replace(event, v);
                    sb.append(name2).append("=\"").append(value).append("\"");
                    parms[index] = value;
                    continue;
                }
                if (a instanceof PluginAttribute) {
                    PluginAttribute attr = (PluginAttribute)a;
                    name = attr.value();
                    value = this.subst.replace(event, this.getAttrValue(name, aliases, attrs));
                    sb.append(name).append("=\"").append(value).append("\"");
                    parms[index] = value;
                    continue;
                }
                if (!(a instanceof PluginElement)) continue;
                PluginElement elem = (PluginElement)a;
                name = elem.value();
                if (parmClasses[index].isArray()) {
                    Object obj;
                    parmClass = parmClasses[index].getComponentType();
                    ArrayList<Object> list = new ArrayList<Object>();
                    sb.append(name).append("={");
                    boolean first = true;
                    for (Node child : children) {
                        PluginType<?> childType = child.getType();
                        if (!elem.value().equalsIgnoreCase(childType.getElementName()) && !parmClass.isAssignableFrom(childType.getPluginClass())) continue;
                        used.add(child);
                        if (!first) {
                            sb.append(", ");
                        }
                        first = false;
                        obj = child.getObject();
                        if (obj == null) {
                            LOGGER.error("Null object returned for " + child.getName() + " in " + node.getName());
                            continue;
                        }
                        if (obj.getClass().isArray()) {
                            this.printArray(sb, (Object[])obj);
                            parms[index] = obj;
                            break;
                        }
                        sb.append(child.toString());
                        list.add(obj);
                    }
                    sb.append("}");
                    if (parms[index] != null) break;
                    if (list.size() > 0 && !parmClass.isAssignableFrom(list.get(0).getClass())) {
                        LOGGER.error("Attempted to assign List containing class " + list.get(0).getClass().getName() + " to array of type " + parmClass + " for attribute " + name);
                        break;
                    }
                    Object[] array = (Object[])Array.newInstance(parmClass, list.size());
                    int i = 0;
                    Iterator i$ = list.iterator();
                    while (i$.hasNext()) {
                        array[i] = obj = i$.next();
                        ++i;
                    }
                    parms[index] = array;
                    continue;
                }
                parmClass = parmClasses[index];
                boolean present = false;
                for (Node child : children) {
                    PluginType<?> childType = child.getType();
                    if (!elem.value().equals(childType.getElementName()) && !parmClass.isAssignableFrom(childType.getPluginClass())) continue;
                    sb.append(child.getName()).append("(").append(child.toString()).append(")");
                    present = true;
                    used.add(child);
                    parms[index] = child.getObject();
                    break;
                }
                if (present) continue;
                sb.append("null");
            }
            ++index;
        }
        if (sb.length() > 0) {
            sb.append(")");
        }
        if (attrs.size() > 0) {
            StringBuilder eb = new StringBuilder();
            for (String key : attrs.keySet()) {
                if (eb.length() == 0) {
                    eb.append(node.getName());
                    eb.append(" contains ");
                    if (attrs.size() == 1) {
                        eb.append("an invalid element or attribute ");
                    } else {
                        eb.append("invalid attributes ");
                    }
                } else {
                    eb.append(", ");
                }
                eb.append("\"");
                eb.append(key);
                eb.append("\"");
            }
            LOGGER.error(eb.toString());
        }
        if (!type.isDeferChildren() && used.size() != children.size()) {
            for (Node child : children) {
                if (used.contains(child)) continue;
                String nodeType = node.getType().getElementName();
                String start = nodeType.equals(node.getName()) ? node.getName() : nodeType + " " + node.getName();
                LOGGER.error(start + " has no parameter that matches element " + child.getName());
            }
        }
        try {
            int mod = factoryMethod.getModifiers();
            if (!Modifier.isStatic(mod)) {
                LOGGER.error(factoryMethod.getName() + " method is not static on class " + clazz.getName() + " for element " + node.getName());
                return null;
            }
            LOGGER.debug("Calling {} on class {} for element {}{}", factoryMethod.getName(), clazz.getName(), node.getName(), sb.toString());
            return factoryMethod.invoke(null, parms);
        }
        catch (Exception e) {
            LOGGER.error("Unable to invoke method " + factoryMethod.getName() + " in class " + clazz.getName() + " for element " + node.getName(), (Throwable)e);
            return null;
        }
    }

    private void printArray(StringBuilder sb, Object ... array) {
        boolean first = true;
        for (Object obj : array) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(obj.toString());
            first = false;
        }
    }

    private String getAttrValue(String name, String[] aliases, Map<String, String> attrs) {
        for (String key : attrs.keySet()) {
            if (key.equalsIgnoreCase(name)) {
                String attr = attrs.get(key);
                attrs.remove(key);
                return attr;
            }
            if (aliases == null) continue;
            for (String alias : aliases) {
                if (!key.equalsIgnoreCase(alias)) continue;
                String attr = attrs.get(key);
                attrs.remove(key);
                return attr;
            }
        }
        return null;
    }

    private void setParents() {
        for (Map.Entry entry : this.loggers.entrySet()) {
            LoggerConfig logger = (LoggerConfig)entry.getValue();
            String name = (String)entry.getKey();
            if (name.equals("")) continue;
            int i = name.lastIndexOf(46);
            if (i > 0) {
                LoggerConfig parent = this.getLoggerConfig(name = name.substring(0, i));
                if (parent == null) {
                    parent = this.root;
                }
                logger.setParent(parent);
                continue;
            }
            logger.setParent(this.root);
        }
    }
}

