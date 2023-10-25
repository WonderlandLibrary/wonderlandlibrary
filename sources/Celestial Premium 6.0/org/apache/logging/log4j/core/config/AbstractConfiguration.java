/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LifeCycle2;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.Version;
import org.apache.logging.log4j.core.appender.AsyncAppender;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.async.AsyncLoggerConfig;
import org.apache.logging.log4j.core.async.AsyncLoggerConfigDelegate;
import org.apache.logging.log4j.core.async.AsyncLoggerConfigDisruptor;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFileWatcher;
import org.apache.logging.log4j.core.config.ConfigurationListener;
import org.apache.logging.log4j.core.config.ConfigurationScheduler;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.CustomLevelConfig;
import org.apache.logging.log4j.core.config.CustomLevels;
import org.apache.logging.log4j.core.config.DefaultAdvertiser;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.Loggers;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.Reconfigurable;
import org.apache.logging.log4j.core.config.ReliabilityStrategy;
import org.apache.logging.log4j.core.config.ReliabilityStrategyFactory;
import org.apache.logging.log4j.core.config.Scheduled;
import org.apache.logging.log4j.core.config.plugins.util.PluginBuilder;
import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
import org.apache.logging.log4j.core.config.plugins.util.PluginType;
import org.apache.logging.log4j.core.filter.AbstractFilterable;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.lookup.Interpolator;
import org.apache.logging.log4j.core.lookup.MapLookup;
import org.apache.logging.log4j.core.lookup.StrLookup;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.script.AbstractScript;
import org.apache.logging.log4j.core.script.ScriptManager;
import org.apache.logging.log4j.core.script.ScriptRef;
import org.apache.logging.log4j.core.util.DummyNanoClock;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.core.util.NameUtil;
import org.apache.logging.log4j.core.util.NanoClock;
import org.apache.logging.log4j.core.util.Source;
import org.apache.logging.log4j.core.util.WatchManager;
import org.apache.logging.log4j.core.util.Watcher;
import org.apache.logging.log4j.core.util.WatcherFactory;
import org.apache.logging.log4j.util.PropertiesUtil;

public abstract class AbstractConfiguration
extends AbstractFilterable
implements Configuration {
    private static final int BUF_SIZE = 16384;
    protected Node rootNode;
    protected final List<ConfigurationListener> listeners = new CopyOnWriteArrayList<ConfigurationListener>();
    protected final List<String> pluginPackages = new ArrayList<String>();
    protected PluginManager pluginManager;
    protected boolean isShutdownHookEnabled = true;
    protected long shutdownTimeoutMillis = 0L;
    protected ScriptManager scriptManager;
    private Advertiser advertiser = new DefaultAdvertiser();
    private Node advertiserNode = null;
    private Object advertisement;
    private String name;
    private ConcurrentMap<String, Appender> appenders = new ConcurrentHashMap<String, Appender>();
    private ConcurrentMap<String, LoggerConfig> loggerConfigs = new ConcurrentHashMap<String, LoggerConfig>();
    private List<CustomLevelConfig> customLevels = Collections.emptyList();
    private final ConcurrentMap<String, String> propertyMap = new ConcurrentHashMap<String, String>();
    private final StrLookup tempLookup = new Interpolator(this.propertyMap);
    private final StrSubstitutor subst = new StrSubstitutor(this.tempLookup);
    private LoggerConfig root = new LoggerConfig();
    private final ConcurrentMap<String, Object> componentMap = new ConcurrentHashMap<String, Object>();
    private final ConfigurationSource configurationSource;
    private final ConfigurationScheduler configurationScheduler = new ConfigurationScheduler();
    private final WatchManager watchManager = new WatchManager(this.configurationScheduler);
    private AsyncLoggerConfigDisruptor asyncLoggerConfigDisruptor;
    private NanoClock nanoClock = new DummyNanoClock();
    private final WeakReference<LoggerContext> loggerContext;

    protected AbstractConfiguration(LoggerContext loggerContext, ConfigurationSource configurationSource) {
        this.loggerContext = new WeakReference<LoggerContext>(loggerContext);
        this.configurationSource = Objects.requireNonNull(configurationSource, "configurationSource is null");
        this.componentMap.put("ContextProperties", this.propertyMap);
        this.pluginManager = new PluginManager("Core");
        this.rootNode = new Node();
        this.setState(LifeCycle.State.INITIALIZING);
    }

    public ConfigurationSource getConfigurationSource() {
        return this.configurationSource;
    }

    public List<String> getPluginPackages() {
        return this.pluginPackages;
    }

    @Override
    public Map<String, String> getProperties() {
        return this.propertyMap;
    }

    public ScriptManager getScriptManager() {
        return this.scriptManager;
    }

    public void setScriptManager(ScriptManager scriptManager) {
        this.scriptManager = scriptManager;
    }

    public PluginManager getPluginManager() {
        return this.pluginManager;
    }

    public void setPluginManager(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    public WatchManager getWatchManager() {
        return this.watchManager;
    }

    public ConfigurationScheduler getScheduler() {
        return this.configurationScheduler;
    }

    public Node getRootNode() {
        return this.rootNode;
    }

    public AsyncLoggerConfigDelegate getAsyncLoggerConfigDelegate() {
        if (this.asyncLoggerConfigDisruptor == null) {
            this.asyncLoggerConfigDisruptor = new AsyncLoggerConfigDisruptor();
        }
        return this.asyncLoggerConfigDisruptor;
    }

    public void initialize() {
        LOGGER.debug(Version.getProductString() + " initializing configuration {}", this);
        this.subst.setConfiguration(this);
        try {
            this.scriptManager = new ScriptManager(this, this.watchManager);
        }
        catch (Exception | LinkageError e) {
            LOGGER.info("Cannot initialize scripting support because this JRE does not support it.", e);
        }
        this.pluginManager.collectPlugins(this.pluginPackages);
        PluginManager levelPlugins = new PluginManager("Level");
        levelPlugins.collectPlugins(this.pluginPackages);
        Map<String, PluginType<?>> plugins = levelPlugins.getPlugins();
        if (plugins != null) {
            for (PluginType<?> type : plugins.values()) {
                try {
                    Loader.initializeClass(type.getPluginClass().getName(), type.getPluginClass().getClassLoader());
                }
                catch (Exception e) {
                    LOGGER.error("Unable to initialize {} due to {}", type.getPluginClass().getName(), e.getClass().getSimpleName(), e);
                }
            }
        }
        this.setup();
        this.setupAdvertisement();
        this.doConfigure();
        this.setState(LifeCycle.State.INITIALIZED);
        LOGGER.debug("Configuration {} initialized", this);
    }

    protected void initializeWatchers(Reconfigurable reconfigurable, ConfigurationSource configSource, int monitorIntervalSeconds) {
        if (configSource.getFile() != null || configSource.getURL() != null) {
            if (monitorIntervalSeconds > 0) {
                this.watchManager.setIntervalSeconds(monitorIntervalSeconds);
                if (configSource.getFile() != null) {
                    Source cfgSource = new Source(configSource);
                    long lastModifeid = configSource.getFile().lastModified();
                    ConfigurationFileWatcher watcher = new ConfigurationFileWatcher(this, reconfigurable, this.listeners, lastModifeid);
                    this.watchManager.watch(cfgSource, watcher);
                } else if (configSource.getURL() != null) {
                    this.monitorSource(reconfigurable, configSource);
                }
            } else if (this.watchManager.hasEventListeners() && configSource.getURL() != null && monitorIntervalSeconds >= 0) {
                this.monitorSource(reconfigurable, configSource);
            }
        }
    }

    private void monitorSource(Reconfigurable reconfigurable, ConfigurationSource configSource) {
        if (configSource.getLastModified() > 0L) {
            Source cfgSource = new Source(configSource);
            Watcher watcher = WatcherFactory.getInstance(this.pluginPackages).newWatcher(cfgSource, this, reconfigurable, this.listeners, configSource.getLastModified());
            if (watcher != null) {
                this.watchManager.watch(cfgSource, watcher);
            }
        } else {
            LOGGER.info("{} does not support dynamic reconfiguration", configSource.getURI());
        }
    }

    @Override
    public void start() {
        if (this.getState().equals((Object)LifeCycle.State.INITIALIZING)) {
            this.initialize();
        }
        LOGGER.debug("Starting configuration {}", this);
        this.setStarting();
        if (this.watchManager.getIntervalSeconds() >= 0) {
            this.watchManager.start();
        }
        if (this.hasAsyncLoggers()) {
            this.asyncLoggerConfigDisruptor.start();
        }
        HashSet<LoggerConfig> alreadyStarted = new HashSet<LoggerConfig>();
        for (LoggerConfig logger : this.loggerConfigs.values()) {
            logger.start();
            alreadyStarted.add(logger);
        }
        for (Appender appender : this.appenders.values()) {
            appender.start();
        }
        if (!alreadyStarted.contains(this.root)) {
            this.root.start();
        }
        super.start();
        LOGGER.debug("Started configuration {} OK.", this);
    }

    private boolean hasAsyncLoggers() {
        if (this.root instanceof AsyncLoggerConfig) {
            return true;
        }
        for (LoggerConfig logger : this.loggerConfigs.values()) {
            if (!(logger instanceof AsyncLoggerConfig)) continue;
            return true;
        }
        return false;
    }

    public boolean stop(long timeout, TimeUnit timeUnit) {
        this.setStopping();
        super.stop(timeout, timeUnit, false);
        LOGGER.trace("Stopping {}...", this);
        for (Iterator loggerConfig : this.loggerConfigs.values()) {
            loggerConfig.getReliabilityStrategy().beforeStopConfiguration(this);
        }
        this.root.getReliabilityStrategy().beforeStopConfiguration(this);
        String cls = this.getClass().getSimpleName();
        LOGGER.trace("{} notified {} ReliabilityStrategies that config will be stopped.", cls, this.loggerConfigs.size() + 1);
        if (!this.loggerConfigs.isEmpty()) {
            LOGGER.trace("{} stopping {} LoggerConfigs.", cls, this.loggerConfigs.size());
            for (LoggerConfig logger : this.loggerConfigs.values()) {
                logger.stop(timeout, timeUnit);
            }
        }
        LOGGER.trace("{} stopping root LoggerConfig.", cls);
        if (!this.root.isStopped()) {
            this.root.stop(timeout, timeUnit);
        }
        if (this.hasAsyncLoggers()) {
            LOGGER.trace("{} stopping AsyncLoggerConfigDisruptor.", cls);
            this.asyncLoggerConfigDisruptor.stop(timeout, timeUnit);
        }
        LOGGER.trace("{} notifying ReliabilityStrategies that appenders will be stopped.", cls);
        for (LoggerConfig loggerConfig : this.loggerConfigs.values()) {
            loggerConfig.getReliabilityStrategy().beforeStopAppenders();
        }
        this.root.getReliabilityStrategy().beforeStopAppenders();
        Appender[] array = this.appenders.values().toArray(new Appender[this.appenders.size()]);
        List<Appender> async = this.getAsyncAppenders(array);
        if (!async.isEmpty()) {
            LOGGER.trace("{} stopping {} AsyncAppenders.", cls, async.size());
            for (Appender appender : async) {
                if (appender instanceof LifeCycle2) {
                    ((LifeCycle2)((Object)appender)).stop(timeout, timeUnit);
                    continue;
                }
                appender.stop();
            }
        }
        LOGGER.trace("{} stopping remaining Appenders.", cls);
        int appenderCount = 0;
        for (int i = array.length - 1; i >= 0; --i) {
            if (!array[i].isStarted()) continue;
            if (array[i] instanceof LifeCycle2) {
                ((LifeCycle2)((Object)array[i])).stop(timeout, timeUnit);
            } else {
                array[i].stop();
            }
            ++appenderCount;
        }
        LOGGER.trace("{} stopped {} remaining Appenders.", cls, appenderCount);
        LOGGER.trace("{} cleaning Appenders from {} LoggerConfigs.", cls, this.loggerConfigs.size() + 1);
        for (LoggerConfig loggerConfig : this.loggerConfigs.values()) {
            loggerConfig.clearAppenders();
        }
        this.root.clearAppenders();
        if (this.watchManager.isStarted()) {
            this.watchManager.stop(timeout, timeUnit);
        }
        this.configurationScheduler.stop(timeout, timeUnit);
        if (this.advertiser != null && this.advertisement != null) {
            this.advertiser.unadvertise(this.advertisement);
        }
        this.setStopped();
        LOGGER.debug("Stopped {} OK", this);
        return true;
    }

    private List<Appender> getAsyncAppenders(Appender[] all) {
        ArrayList<Appender> result = new ArrayList<Appender>();
        for (int i = all.length - 1; i >= 0; --i) {
            if (!(all[i] instanceof AsyncAppender)) continue;
            result.add(all[i]);
        }
        return result;
    }

    @Override
    public boolean isShutdownHookEnabled() {
        return this.isShutdownHookEnabled;
    }

    public long getShutdownTimeoutMillis() {
        return this.shutdownTimeoutMillis;
    }

    public void setup() {
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

    protected void createAdvertiser(String advertiserString, ConfigurationSource configSource, byte[] buffer, String contentType) {
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
        String nodeName;
        PluginType<?> type;
        if (this.advertiserNode != null && (type = this.pluginManager.getPluginType(nodeName = this.advertiserNode.getName())) != null) {
            Class<Advertiser> clazz = type.getPluginClass().asSubclass(Advertiser.class);
            try {
                this.advertiser = clazz.newInstance();
                this.advertisement = this.advertiser.advertise(this.advertiserNode.getAttributes());
            }
            catch (InstantiationException e) {
                LOGGER.error("InstantiationException attempting to instantiate advertiser: {}", nodeName, e);
            }
            catch (IllegalAccessException e) {
                LOGGER.error("IllegalAccessException attempting to instantiate advertiser: {}", nodeName, e);
            }
        }
    }

    @Override
    public <T> T getComponent(String componentName) {
        return (T)this.componentMap.get(componentName);
    }

    @Override
    public void addComponent(String componentName, Object obj) {
        this.componentMap.putIfAbsent(componentName, obj);
    }

    protected void preConfigure(Node node) {
        try {
            for (Node child : node.getChildren()) {
                if (child.getType() == null) {
                    LOGGER.error("Unable to locate plugin type for " + child.getName());
                    continue;
                }
                Class clazz = child.getType().getPluginClass();
                if (clazz.isAnnotationPresent(Scheduled.class)) {
                    this.configurationScheduler.incrementScheduledItems();
                }
                this.preConfigure(child);
            }
        }
        catch (Exception ex) {
            LOGGER.error("Error capturing node data for node " + node.getName(), (Throwable)ex);
        }
    }

    protected void doConfigure() {
        this.preConfigure(this.rootNode);
        this.configurationScheduler.start();
        if (this.rootNode.hasChildren() && this.rootNode.getChildren().get(0).getName().equalsIgnoreCase("Properties")) {
            Node first = this.rootNode.getChildren().get(0);
            this.createConfiguration(first, null);
            if (first.getObject() != null) {
                this.subst.setVariableResolver((StrLookup)first.getObject());
            }
        } else {
            Map map = (Map)this.getComponent("ContextProperties");
            MapLookup lookup = map == null ? null : new MapLookup(map);
            this.subst.setVariableResolver(new Interpolator((StrLookup)lookup, this.pluginPackages));
        }
        boolean setLoggers = false;
        boolean setRoot = false;
        for (Node node : this.rootNode.getChildren()) {
            if (node.getName().equalsIgnoreCase("Properties")) {
                if (this.tempLookup != this.subst.getVariableResolver()) continue;
                LOGGER.error("Properties declaration must be the first element in the configuration");
                continue;
            }
            this.createConfiguration(node, null);
            if (node.getObject() == null) continue;
            if (node.getName().equalsIgnoreCase("Scripts")) {
                for (AbstractScript script : (AbstractScript[])node.getObject(AbstractScript[].class)) {
                    if (script instanceof ScriptRef) {
                        LOGGER.error("Script reference to {} not added. Scripts definition cannot contain script references", script.getName());
                        continue;
                    }
                    if (this.scriptManager == null) continue;
                    this.scriptManager.addScript(script);
                }
                continue;
            }
            if (node.getName().equalsIgnoreCase("Appenders")) {
                this.appenders = (ConcurrentMap)node.getObject();
                continue;
            }
            if (node.isInstanceOf(Filter.class)) {
                this.addFilter((Filter)node.getObject(Filter.class));
                continue;
            }
            if (node.getName().equalsIgnoreCase("Loggers")) {
                Loggers l = (Loggers)node.getObject();
                this.loggerConfigs = l.getMap();
                setLoggers = true;
                if (l.getRoot() == null) continue;
                this.root = l.getRoot();
                setRoot = true;
                continue;
            }
            if (node.getName().equalsIgnoreCase("CustomLevels")) {
                this.customLevels = ((CustomLevels)node.getObject(CustomLevels.class)).getCustomLevels();
                continue;
            }
            if (node.isInstanceOf(CustomLevelConfig.class)) {
                ArrayList<CustomLevelConfig> copy = new ArrayList<CustomLevelConfig>(this.customLevels);
                copy.add((CustomLevelConfig)node.getObject(CustomLevelConfig.class));
                this.customLevels = copy;
                continue;
            }
            List<String> expected = Arrays.asList("\"Appenders\"", "\"Loggers\"", "\"Properties\"", "\"Scripts\"", "\"CustomLevels\"");
            LOGGER.error("Unknown object \"{}\" of type {} is ignored: try nesting it inside one of: {}.", node.getName(), node.getObject().getClass().getName(), expected);
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
        for (Map.Entry entry : this.loggerConfigs.entrySet()) {
            LoggerConfig loggerConfig = (LoggerConfig)entry.getValue();
            for (AppenderRef ref : loggerConfig.getAppenderRefs()) {
                Appender app = (Appender)this.appenders.get(ref.getRef());
                if (app != null) {
                    loggerConfig.addAppender(app, ref.getLevel(), ref.getFilter());
                    continue;
                }
                LOGGER.error("Unable to locate appender \"{}\" for logger config \"{}\"", ref.getRef(), loggerConfig);
            }
        }
        this.setParents();
    }

    protected void setToDefault() {
        this.setName("Default@" + Integer.toHexString(this.hashCode()));
        PatternLayout layout = PatternLayout.newBuilder().withPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n").withConfiguration(this).build();
        ConsoleAppender appender = ConsoleAppender.createDefaultAppenderForLayout((Layout)layout);
        appender.start();
        this.addAppender(appender);
        LoggerConfig rootLoggerConfig = this.getRootLogger();
        rootLoggerConfig.addAppender(appender, null, null);
        Level defaultLevel = Level.ERROR;
        String levelName = PropertiesUtil.getProperties().getStringProperty("org.apache.logging.log4j.level", defaultLevel.name());
        Level level = Level.valueOf(levelName);
        rootLoggerConfig.setLevel(level != null ? level : defaultLevel);
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

    public <T extends Appender> T getAppender(String appenderName) {
        return (T)(appenderName != null ? (Appender)this.appenders.get(appenderName) : null);
    }

    @Override
    public Map<String, Appender> getAppenders() {
        return this.appenders;
    }

    public void addAppender(Appender appender) {
        if (appender != null) {
            this.appenders.putIfAbsent(appender.getName(), appender);
        }
    }

    @Override
    public StrSubstitutor getStrSubstitutor() {
        return this.subst;
    }

    @Override
    public void setAdvertiser(Advertiser advertiser) {
        this.advertiser = advertiser;
    }

    @Override
    public Advertiser getAdvertiser() {
        return this.advertiser;
    }

    public ReliabilityStrategy getReliabilityStrategy(LoggerConfig loggerConfig) {
        return ReliabilityStrategyFactory.getReliabilityStrategy(loggerConfig);
    }

    @Override
    public synchronized void addLoggerAppender(Logger logger, Appender appender) {
        if (appender == null || logger == null) {
            return;
        }
        String loggerName = logger.getName();
        this.appenders.putIfAbsent(appender.getName(), appender);
        LoggerConfig lc = this.getLoggerConfig(loggerName);
        if (lc.getName().equals(loggerName)) {
            lc.addAppender(appender, null, null);
        } else {
            LoggerConfig nlc = new LoggerConfig(loggerName, lc.getLevel(), lc.isAdditive());
            nlc.addAppender(appender, null, null);
            nlc.setParent(lc);
            this.loggerConfigs.putIfAbsent(loggerName, nlc);
            this.setParents();
            logger.getContext().updateLoggers();
        }
    }

    @Override
    public synchronized void addLoggerFilter(Logger logger, Filter filter) {
        String loggerName = logger.getName();
        LoggerConfig lc = this.getLoggerConfig(loggerName);
        if (lc.getName().equals(loggerName)) {
            lc.addFilter(filter);
        } else {
            LoggerConfig nlc = new LoggerConfig(loggerName, lc.getLevel(), lc.isAdditive());
            nlc.addFilter(filter);
            nlc.setParent(lc);
            this.loggerConfigs.putIfAbsent(loggerName, nlc);
            this.setParents();
            logger.getContext().updateLoggers();
        }
    }

    @Override
    public synchronized void setLoggerAdditive(Logger logger, boolean additive) {
        String loggerName = logger.getName();
        LoggerConfig lc = this.getLoggerConfig(loggerName);
        if (lc.getName().equals(loggerName)) {
            lc.setAdditive(additive);
        } else {
            LoggerConfig nlc = new LoggerConfig(loggerName, lc.getLevel(), additive);
            nlc.setParent(lc);
            this.loggerConfigs.putIfAbsent(loggerName, nlc);
            this.setParents();
            logger.getContext().updateLoggers();
        }
    }

    public synchronized void removeAppender(String appenderName) {
        Appender app;
        for (LoggerConfig logger : this.loggerConfigs.values()) {
            logger.removeAppender(appenderName);
        }
        Appender appender = app = appenderName != null ? (Appender)this.appenders.remove(appenderName) : null;
        if (app != null) {
            app.stop();
        }
    }

    public List<CustomLevelConfig> getCustomLevels() {
        return Collections.unmodifiableList(this.customLevels);
    }

    @Override
    public LoggerConfig getLoggerConfig(String loggerName) {
        LoggerConfig loggerConfig = (LoggerConfig)this.loggerConfigs.get(loggerName);
        if (loggerConfig != null) {
            return loggerConfig;
        }
        String substr = loggerName;
        while ((substr = NameUtil.getSubName(substr)) != null) {
            loggerConfig = (LoggerConfig)this.loggerConfigs.get(substr);
            if (loggerConfig == null) continue;
            return loggerConfig;
        }
        return this.root;
    }

    public LoggerContext getLoggerContext() {
        return (LoggerContext)this.loggerContext.get();
    }

    public LoggerConfig getRootLogger() {
        return this.root;
    }

    @Override
    public Map<String, LoggerConfig> getLoggers() {
        return Collections.unmodifiableMap(this.loggerConfigs);
    }

    public LoggerConfig getLogger(String loggerName) {
        return (LoggerConfig)this.loggerConfigs.get(loggerName);
    }

    public synchronized void addLogger(String loggerName, LoggerConfig loggerConfig) {
        this.loggerConfigs.putIfAbsent(loggerName, loggerConfig);
        this.setParents();
    }

    public synchronized void removeLogger(String loggerName) {
        this.loggerConfigs.remove(loggerName);
        this.setParents();
    }

    @Override
    public void createConfiguration(Node node, LogEvent event) {
        PluginType type = node.getType();
        if (type != null && type.isDeferChildren()) {
            node.setObject(this.createPluginObject(type, node, event));
        } else {
            for (Node child : node.getChildren()) {
                this.createConfiguration(child, event);
            }
            if (type == null) {
                if (node.getParent() != null) {
                    LOGGER.error("Unable to locate plugin for {}", node.getName());
                }
            } else {
                node.setObject(this.createPluginObject(type, node, event));
            }
        }
    }

    private Object createPluginObject(PluginType<?> type, Node node, LogEvent event) {
        Class<?> clazz = type.getPluginClass();
        if (Map.class.isAssignableFrom(clazz)) {
            try {
                return AbstractConfiguration.createPluginMap(node);
            }
            catch (Exception e) {
                LOGGER.warn("Unable to create Map for {} of class {}", type.getElementName(), clazz, e);
            }
        }
        if (Collection.class.isAssignableFrom(clazz)) {
            try {
                return AbstractConfiguration.createPluginCollection(node);
            }
            catch (Exception e) {
                LOGGER.warn("Unable to create List for {} of class {}", type.getElementName(), clazz, e);
            }
        }
        return new PluginBuilder(type).withConfiguration(this).withConfigurationNode(node).forLogEvent(event).build();
    }

    private static Map<String, ?> createPluginMap(Node node) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        for (Node child : node.getChildren()) {
            Object object = child.getObject();
            map.put(child.getName(), object);
        }
        return map;
    }

    private static Collection<?> createPluginCollection(Node node) {
        List<Node> children = node.getChildren();
        ArrayList<Object> list = new ArrayList<Object>(children.size());
        for (Node child : children) {
            Object object = child.getObject();
            list.add(object);
        }
        return list;
    }

    private void setParents() {
        for (Map.Entry entry : this.loggerConfigs.entrySet()) {
            LoggerConfig logger = (LoggerConfig)entry.getValue();
            String key = (String)entry.getKey();
            if (key.isEmpty()) continue;
            int i = key.lastIndexOf(46);
            if (i > 0) {
                LoggerConfig parent = this.getLoggerConfig(key = key.substring(0, i));
                if (parent == null) {
                    parent = this.root;
                }
                logger.setParent(parent);
                continue;
            }
            logger.setParent(this.root);
        }
    }

    protected static byte[] toByteArray(InputStream is) throws IOException {
        int nRead;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[16384];
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }

    public NanoClock getNanoClock() {
        return this.nanoClock;
    }

    public void setNanoClock(NanoClock nanoClock) {
        this.nanoClock = Objects.requireNonNull(nanoClock, "nanoClock");
    }
}

