// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;
import org.apache.logging.log4j.core.config.plugins.util.PluginBuilder;
import org.apache.logging.log4j.core.util.NameUtil;
import org.apache.logging.log4j.core.Logger;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;
import java.util.Arrays;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.script.ScriptRef;
import org.apache.logging.log4j.core.script.AbstractScript;
import org.apache.logging.log4j.core.lookup.MapLookup;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.arbiters.Arbiter;
import java.util.Collection;
import org.apache.logging.log4j.core.config.arbiters.SelectArbiter;
import java.lang.annotation.Annotation;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.appender.AsyncAppender;
import org.apache.logging.log4j.core.LifeCycle2;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.async.AsyncLoggerConfig;
import java.util.Set;
import java.util.HashSet;
import org.apache.logging.log4j.core.util.WatcherFactory;
import org.apache.logging.log4j.core.util.Watcher;
import org.apache.logging.log4j.core.util.Source;
import java.util.Iterator;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.core.config.plugins.util.PluginType;
import org.apache.logging.log4j.core.Version;
import org.apache.logging.log4j.core.async.AsyncLoggerConfigDelegate;
import org.apache.logging.log4j.core.LifeCycle;
import java.util.Objects;
import org.apache.logging.log4j.core.util.DummyNanoClock;
import java.util.Map;
import org.apache.logging.log4j.core.lookup.Interpolator;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.logging.log4j.core.LoggerContext;
import java.lang.ref.WeakReference;
import org.apache.logging.log4j.core.util.NanoClock;
import org.apache.logging.log4j.core.async.AsyncLoggerConfigDisruptor;
import org.apache.logging.log4j.core.util.WatchManager;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.core.lookup.StrLookup;
import org.apache.logging.log4j.core.Appender;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.script.ScriptManager;
import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
import java.util.List;
import org.apache.logging.log4j.core.filter.AbstractFilterable;

public abstract class AbstractConfiguration extends AbstractFilterable implements Configuration
{
    private static final int BUF_SIZE = 16384;
    protected Node rootNode;
    protected final List<ConfigurationListener> listeners;
    protected final List<String> pluginPackages;
    protected PluginManager pluginManager;
    protected boolean isShutdownHookEnabled;
    protected long shutdownTimeoutMillis;
    protected ScriptManager scriptManager;
    private Advertiser advertiser;
    private Node advertiserNode;
    private Object advertisement;
    private String name;
    private ConcurrentMap<String, Appender> appenders;
    private ConcurrentMap<String, LoggerConfig> loggerConfigs;
    private List<CustomLevelConfig> customLevels;
    private final ConcurrentMap<String, String> propertyMap;
    private final StrLookup tempLookup;
    private final StrSubstitutor subst;
    private LoggerConfig root;
    private final ConcurrentMap<String, Object> componentMap;
    private final ConfigurationSource configurationSource;
    private final ConfigurationScheduler configurationScheduler;
    private final WatchManager watchManager;
    private AsyncLoggerConfigDisruptor asyncLoggerConfigDisruptor;
    private NanoClock nanoClock;
    private final WeakReference<LoggerContext> loggerContext;
    
    protected AbstractConfiguration(final LoggerContext loggerContext, final ConfigurationSource configurationSource) {
        this.listeners = new CopyOnWriteArrayList<ConfigurationListener>();
        this.pluginPackages = new ArrayList<String>();
        this.isShutdownHookEnabled = true;
        this.shutdownTimeoutMillis = 0L;
        this.advertiser = new DefaultAdvertiser();
        this.advertiserNode = null;
        this.appenders = new ConcurrentHashMap<String, Appender>();
        this.loggerConfigs = new ConcurrentHashMap<String, LoggerConfig>();
        this.customLevels = Collections.emptyList();
        this.propertyMap = new ConcurrentHashMap<String, String>();
        this.tempLookup = new Interpolator(this.propertyMap);
        this.subst = new StrSubstitutor(this.tempLookup);
        this.root = new LoggerConfig();
        this.componentMap = new ConcurrentHashMap<String, Object>();
        this.configurationScheduler = new ConfigurationScheduler();
        this.watchManager = new WatchManager(this.configurationScheduler);
        this.nanoClock = new DummyNanoClock();
        this.loggerContext = new WeakReference<LoggerContext>(loggerContext);
        this.configurationSource = Objects.requireNonNull(configurationSource, "configurationSource is null");
        this.componentMap.put("ContextProperties", this.propertyMap);
        this.pluginManager = new PluginManager("Core");
        this.rootNode = new Node();
        this.setState(LifeCycle.State.INITIALIZING);
    }
    
    @Override
    public ConfigurationSource getConfigurationSource() {
        return this.configurationSource;
    }
    
    @Override
    public List<String> getPluginPackages() {
        return this.pluginPackages;
    }
    
    @Override
    public Map<String, String> getProperties() {
        return this.propertyMap;
    }
    
    @Override
    public ScriptManager getScriptManager() {
        return this.scriptManager;
    }
    
    public void setScriptManager(final ScriptManager scriptManager) {
        this.scriptManager = scriptManager;
    }
    
    public PluginManager getPluginManager() {
        return this.pluginManager;
    }
    
    public void setPluginManager(final PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }
    
    @Override
    public WatchManager getWatchManager() {
        return this.watchManager;
    }
    
    @Override
    public ConfigurationScheduler getScheduler() {
        return this.configurationScheduler;
    }
    
    public Node getRootNode() {
        return this.rootNode;
    }
    
    @Override
    public AsyncLoggerConfigDelegate getAsyncLoggerConfigDelegate() {
        if (this.asyncLoggerConfigDisruptor == null) {
            this.asyncLoggerConfigDisruptor = new AsyncLoggerConfigDisruptor();
        }
        return this.asyncLoggerConfigDisruptor;
    }
    
    @Override
    public void initialize() {
        AbstractConfiguration.LOGGER.debug(Version.getProductString() + " initializing configuration {}", this);
        this.subst.setConfiguration(this);
        try {
            this.scriptManager = new ScriptManager(this, this.watchManager);
        }
        catch (LinkageError | Exception linkageError) {
            final Throwable t;
            final Throwable e = t;
            AbstractConfiguration.LOGGER.info("Cannot initialize scripting support because this JRE does not support it.", e);
        }
        this.pluginManager.collectPlugins(this.pluginPackages);
        final PluginManager levelPlugins = new PluginManager("Level");
        levelPlugins.collectPlugins(this.pluginPackages);
        final Map<String, PluginType<?>> plugins = levelPlugins.getPlugins();
        if (plugins != null) {
            for (final PluginType<?> type : plugins.values()) {
                try {
                    Loader.initializeClass(type.getPluginClass().getName(), type.getPluginClass().getClassLoader());
                }
                catch (Exception e2) {
                    AbstractConfiguration.LOGGER.error("Unable to initialize {} due to {}", type.getPluginClass().getName(), e2.getClass().getSimpleName(), e2);
                }
            }
        }
        this.setup();
        this.setupAdvertisement();
        this.doConfigure();
        this.setState(LifeCycle.State.INITIALIZED);
        AbstractConfiguration.LOGGER.debug("Configuration {} initialized", this);
    }
    
    protected void initializeWatchers(final Reconfigurable reconfigurable, final ConfigurationSource configSource, final int monitorIntervalSeconds) {
        if (configSource.getFile() != null || configSource.getURL() != null) {
            if (monitorIntervalSeconds > 0) {
                this.watchManager.setIntervalSeconds(monitorIntervalSeconds);
                if (configSource.getFile() != null) {
                    final Source cfgSource = new Source(configSource);
                    final long lastModifeid = configSource.getFile().lastModified();
                    final ConfigurationFileWatcher watcher = new ConfigurationFileWatcher(this, reconfigurable, this.listeners, lastModifeid);
                    this.watchManager.watch(cfgSource, watcher);
                }
                else if (configSource.getURL() != null) {
                    this.monitorSource(reconfigurable, configSource);
                }
            }
            else if (this.watchManager.hasEventListeners() && configSource.getURL() != null && monitorIntervalSeconds >= 0) {
                this.monitorSource(reconfigurable, configSource);
            }
        }
    }
    
    private void monitorSource(final Reconfigurable reconfigurable, final ConfigurationSource configSource) {
        if (configSource.getLastModified() > 0L) {
            final Source cfgSource = new Source(configSource);
            final Watcher watcher = WatcherFactory.getInstance(this.pluginPackages).newWatcher(cfgSource, this, reconfigurable, this.listeners, configSource.getLastModified());
            if (watcher != null) {
                this.watchManager.watch(cfgSource, watcher);
            }
        }
        else {
            AbstractConfiguration.LOGGER.info("{} does not support dynamic reconfiguration", configSource.getURI());
        }
    }
    
    @Override
    public void start() {
        if (this.getState().equals(LifeCycle.State.INITIALIZING)) {
            this.initialize();
        }
        AbstractConfiguration.LOGGER.debug("Starting configuration {}", this);
        this.setStarting();
        if (this.watchManager.getIntervalSeconds() >= 0) {
            this.watchManager.start();
        }
        if (this.hasAsyncLoggers()) {
            this.asyncLoggerConfigDisruptor.start();
        }
        final Set<LoggerConfig> alreadyStarted = new HashSet<LoggerConfig>();
        for (final LoggerConfig logger : this.loggerConfigs.values()) {
            logger.start();
            alreadyStarted.add(logger);
        }
        for (final Appender appender : this.appenders.values()) {
            appender.start();
        }
        if (!alreadyStarted.contains(this.root)) {
            this.root.start();
        }
        super.start();
        AbstractConfiguration.LOGGER.debug("Started configuration {} OK.", this);
    }
    
    private boolean hasAsyncLoggers() {
        if (this.root instanceof AsyncLoggerConfig) {
            return true;
        }
        for (final LoggerConfig logger : this.loggerConfigs.values()) {
            if (logger instanceof AsyncLoggerConfig) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        this.setStopping();
        super.stop(timeout, timeUnit, false);
        AbstractConfiguration.LOGGER.trace("Stopping {}...", this);
        for (final LoggerConfig loggerConfig : this.loggerConfigs.values()) {
            loggerConfig.getReliabilityStrategy().beforeStopConfiguration(this);
        }
        this.root.getReliabilityStrategy().beforeStopConfiguration(this);
        final String cls = this.getClass().getSimpleName();
        AbstractConfiguration.LOGGER.trace("{} notified {} ReliabilityStrategies that config will be stopped.", cls, this.loggerConfigs.size() + 1);
        if (!this.loggerConfigs.isEmpty()) {
            AbstractConfiguration.LOGGER.trace("{} stopping {} LoggerConfigs.", cls, this.loggerConfigs.size());
            for (final LoggerConfig logger : this.loggerConfigs.values()) {
                logger.stop(timeout, timeUnit);
            }
        }
        AbstractConfiguration.LOGGER.trace("{} stopping root LoggerConfig.", cls);
        if (!this.root.isStopped()) {
            this.root.stop(timeout, timeUnit);
        }
        if (this.hasAsyncLoggers()) {
            AbstractConfiguration.LOGGER.trace("{} stopping AsyncLoggerConfigDisruptor.", cls);
            this.asyncLoggerConfigDisruptor.stop(timeout, timeUnit);
        }
        AbstractConfiguration.LOGGER.trace("{} notifying ReliabilityStrategies that appenders will be stopped.", cls);
        for (final LoggerConfig loggerConfig2 : this.loggerConfigs.values()) {
            loggerConfig2.getReliabilityStrategy().beforeStopAppenders();
        }
        this.root.getReliabilityStrategy().beforeStopAppenders();
        final Appender[] array = this.appenders.values().toArray(new Appender[this.appenders.size()]);
        final List<Appender> async = this.getAsyncAppenders(array);
        if (!async.isEmpty()) {
            AbstractConfiguration.LOGGER.trace("{} stopping {} AsyncAppenders.", cls, async.size());
            for (final Appender appender : async) {
                if (appender instanceof LifeCycle2) {
                    ((LifeCycle2)appender).stop(timeout, timeUnit);
                }
                else {
                    appender.stop();
                }
            }
        }
        AbstractConfiguration.LOGGER.trace("{} stopping remaining Appenders.", cls);
        int appenderCount = 0;
        for (int i = array.length - 1; i >= 0; --i) {
            if (array[i].isStarted()) {
                if (array[i] instanceof LifeCycle2) {
                    ((LifeCycle2)array[i]).stop(timeout, timeUnit);
                }
                else {
                    array[i].stop();
                }
                ++appenderCount;
            }
        }
        AbstractConfiguration.LOGGER.trace("{} stopped {} remaining Appenders.", cls, appenderCount);
        AbstractConfiguration.LOGGER.trace("{} cleaning Appenders from {} LoggerConfigs.", cls, this.loggerConfigs.size() + 1);
        for (final LoggerConfig loggerConfig3 : this.loggerConfigs.values()) {
            loggerConfig3.clearAppenders();
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
        AbstractConfiguration.LOGGER.debug("Stopped {} OK", this);
        return true;
    }
    
    private List<Appender> getAsyncAppenders(final Appender[] all) {
        final List<Appender> result = new ArrayList<Appender>();
        for (int i = all.length - 1; i >= 0; --i) {
            if (all[i] instanceof AsyncAppender) {
                result.add(all[i]);
            }
        }
        return result;
    }
    
    @Override
    public boolean isShutdownHookEnabled() {
        return this.isShutdownHookEnabled;
    }
    
    @Override
    public long getShutdownTimeoutMillis() {
        return this.shutdownTimeoutMillis;
    }
    
    public void setup() {
    }
    
    protected Level getDefaultStatus() {
        final String statusLevel = PropertiesUtil.getProperties().getStringProperty("Log4jDefaultStatusLevel", Level.ERROR.name());
        try {
            return Level.toLevel(statusLevel);
        }
        catch (Exception ex) {
            return Level.ERROR;
        }
    }
    
    protected void createAdvertiser(final String advertiserString, final ConfigurationSource configSource, final byte[] buffer, final String contentType) {
        if (advertiserString != null) {
            final Node node = new Node(null, advertiserString, null);
            final Map<String, String> attributes = node.getAttributes();
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
        if (this.advertiserNode != null) {
            final String nodeName = this.advertiserNode.getName();
            final PluginType<?> type = this.pluginManager.getPluginType(nodeName);
            if (type != null) {
                final Class<? extends Advertiser> clazz = type.getPluginClass().asSubclass(Advertiser.class);
                try {
                    this.advertiser = (Advertiser)clazz.newInstance();
                    this.advertisement = this.advertiser.advertise(this.advertiserNode.getAttributes());
                }
                catch (InstantiationException e) {
                    AbstractConfiguration.LOGGER.error("InstantiationException attempting to instantiate advertiser: {}", nodeName, e);
                }
                catch (IllegalAccessException e2) {
                    AbstractConfiguration.LOGGER.error("IllegalAccessException attempting to instantiate advertiser: {}", nodeName, e2);
                }
            }
        }
    }
    
    @Override
    public <T> T getComponent(final String componentName) {
        return (T)this.componentMap.get(componentName);
    }
    
    @Override
    public void addComponent(final String componentName, final Object obj) {
        this.componentMap.putIfAbsent(componentName, obj);
    }
    
    protected void preConfigure(final Node node) {
        try {
            for (final Node child : node.getChildren()) {
                if (child.getType() == null) {
                    AbstractConfiguration.LOGGER.error("Unable to locate plugin type for " + child.getName());
                }
                else {
                    final Class<?> clazz = child.getType().getPluginClass();
                    if (clazz.isAnnotationPresent(Scheduled.class)) {
                        this.configurationScheduler.incrementScheduledItems();
                    }
                    this.preConfigure(child);
                }
            }
        }
        catch (Exception ex) {
            AbstractConfiguration.LOGGER.error("Error capturing node data for node " + node.getName(), ex);
        }
    }
    
    protected void processConditionals(final Node node) {
        try {
            final List<Node> addList = new ArrayList<Node>();
            final List<Node> removeList = new ArrayList<Node>();
            for (final Node child : node.getChildren()) {
                final PluginType<?> type = child.getType();
                if (type != null && "Arbiter".equals(type.getElementName())) {
                    final Class<?> clazz = type.getPluginClass();
                    if (SelectArbiter.class.isAssignableFrom(clazz)) {
                        removeList.add(child);
                        addList.addAll(this.processSelect(child, type));
                    }
                    else if (Arbiter.class.isAssignableFrom(clazz)) {
                        removeList.add(child);
                        try {
                            final Arbiter condition = (Arbiter)this.createPluginObject(type, child, null);
                            if (!condition.isCondition()) {
                                continue;
                            }
                            addList.addAll(child.getChildren());
                            this.processConditionals(child);
                        }
                        catch (Exception inner) {
                            AbstractConfiguration.LOGGER.error("Exception processing {}: Ignoring and including children", type.getPluginClass());
                            this.processConditionals(child);
                        }
                    }
                    else {
                        AbstractConfiguration.LOGGER.error("Encountered Condition Plugin that does not implement Condition: {}", child.getName());
                        this.processConditionals(child);
                    }
                }
                else {
                    this.processConditionals(child);
                }
            }
            if (!removeList.isEmpty()) {
                final List<Node> children = node.getChildren();
                children.removeAll(removeList);
                children.addAll(addList);
                for (final Node grandChild : addList) {
                    grandChild.setParent(node);
                }
            }
        }
        catch (Exception ex) {
            AbstractConfiguration.LOGGER.error("Error capturing node data for node " + node.getName(), ex);
        }
    }
    
    protected List<Node> processSelect(final Node selectNode, final PluginType<?> type) {
        final List<Node> addList = new ArrayList<Node>();
        final SelectArbiter select = (SelectArbiter)this.createPluginObject(type, selectNode, null);
        final List<Arbiter> conditions = new ArrayList<Arbiter>();
        for (final Node child : selectNode.getChildren()) {
            final PluginType<?> nodeType = child.getType();
            if (nodeType != null) {
                if (Arbiter.class.isAssignableFrom(nodeType.getPluginClass())) {
                    final Arbiter condition = (Arbiter)this.createPluginObject(nodeType, child, null);
                    conditions.add(condition);
                    child.setObject(condition);
                }
                else {
                    AbstractConfiguration.LOGGER.error("Invalid Node {} for Select. Must be a Condition", child.getName());
                }
            }
            else {
                AbstractConfiguration.LOGGER.error("No PluginType for node {}", child.getName());
            }
        }
        final Arbiter condition2 = select.evaluateConditions(conditions);
        if (condition2 != null) {
            for (final Node child2 : selectNode.getChildren()) {
                if (condition2 == child2.getObject()) {
                    addList.addAll(child2.getChildren());
                    this.processConditionals(child2);
                }
            }
        }
        return addList;
    }
    
    protected void doConfigure() {
        this.processConditionals(this.rootNode);
        this.preConfigure(this.rootNode);
        this.configurationScheduler.start();
        if (this.rootNode.hasChildren() && this.rootNode.getChildren().get(0).getName().equalsIgnoreCase("Properties")) {
            final Node first = this.rootNode.getChildren().get(0);
            this.createConfiguration(first, null);
            if (first.getObject() != null) {
                this.subst.setVariableResolver(first.getObject());
            }
        }
        else {
            final Map<String, String> map = this.getComponent("ContextProperties");
            final StrLookup lookup = (map == null) ? null : new MapLookup(map);
            this.subst.setVariableResolver(new Interpolator(lookup, this.pluginPackages));
        }
        boolean setLoggers = false;
        boolean setRoot = false;
        for (final Node child : this.rootNode.getChildren()) {
            if (child.getName().equalsIgnoreCase("Properties")) {
                if (this.tempLookup != this.subst.getVariableResolver()) {
                    continue;
                }
                AbstractConfiguration.LOGGER.error("Properties declaration must be the first element in the configuration");
            }
            else {
                this.createConfiguration(child, null);
                if (child.getObject() == null) {
                    continue;
                }
                if (child.getName().equalsIgnoreCase("Scripts")) {
                    for (final AbstractScript script : child.getObject(AbstractScript[].class)) {
                        if (script instanceof ScriptRef) {
                            AbstractConfiguration.LOGGER.error("Script reference to {} not added. Scripts definition cannot contain script references", script.getName());
                        }
                        else if (this.scriptManager != null) {
                            this.scriptManager.addScript(script);
                        }
                    }
                }
                else if (child.getName().equalsIgnoreCase("Appenders")) {
                    this.appenders = child.getObject();
                }
                else if (child.isInstanceOf(Filter.class)) {
                    this.addFilter(child.getObject(Filter.class));
                }
                else if (child.getName().equalsIgnoreCase("Loggers")) {
                    final Loggers l = child.getObject();
                    this.loggerConfigs = l.getMap();
                    setLoggers = true;
                    if (l.getRoot() == null) {
                        continue;
                    }
                    this.root = l.getRoot();
                    setRoot = true;
                }
                else if (child.getName().equalsIgnoreCase("CustomLevels")) {
                    this.customLevels = child.getObject(CustomLevels.class).getCustomLevels();
                }
                else if (child.isInstanceOf(CustomLevelConfig.class)) {
                    final List<CustomLevelConfig> copy = new ArrayList<CustomLevelConfig>(this.customLevels);
                    copy.add(child.getObject(CustomLevelConfig.class));
                    this.customLevels = copy;
                }
                else {
                    final List<String> expected = Arrays.asList("\"Appenders\"", "\"Loggers\"", "\"Properties\"", "\"Scripts\"", "\"CustomLevels\"");
                    AbstractConfiguration.LOGGER.error("Unknown object \"{}\" of type {} is ignored: try nesting it inside one of: {}.", child.getName(), child.getObject().getClass().getName(), expected);
                }
            }
        }
        if (!setLoggers) {
            AbstractConfiguration.LOGGER.warn("No Loggers were configured, using default. Is the Loggers element missing?");
            this.setToDefault();
            return;
        }
        if (!setRoot) {
            AbstractConfiguration.LOGGER.warn("No Root logger was configured, creating default ERROR-level Root logger with Console appender");
            this.setToDefault();
        }
        for (final Map.Entry<String, LoggerConfig> entry : this.loggerConfigs.entrySet()) {
            final LoggerConfig loggerConfig = entry.getValue();
            for (final AppenderRef ref : loggerConfig.getAppenderRefs()) {
                final Appender app = this.appenders.get(ref.getRef());
                if (app != null) {
                    loggerConfig.addAppender(app, ref.getLevel(), ref.getFilter());
                }
                else {
                    AbstractConfiguration.LOGGER.error("Unable to locate appender \"{}\" for logger config \"{}\"", ref.getRef(), loggerConfig);
                }
            }
        }
        this.setParents();
    }
    
    protected void setToDefault() {
        this.setName("Default@" + Integer.toHexString(this.hashCode()));
        final Layout<? extends Serializable> layout = PatternLayout.newBuilder().withPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n").withConfiguration(this).build();
        final Appender appender = ConsoleAppender.createDefaultAppenderForLayout(layout);
        appender.start();
        this.addAppender(appender);
        final LoggerConfig rootLoggerConfig = this.getRootLogger();
        rootLoggerConfig.addAppender(appender, null, null);
        final Level defaultLevel = Level.ERROR;
        final String levelName = PropertiesUtil.getProperties().getStringProperty("org.apache.logging.log4j.level", defaultLevel.name());
        final Level level = Level.valueOf(levelName);
        rootLoggerConfig.setLevel((level != null) ? level : defaultLevel);
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public void addListener(final ConfigurationListener listener) {
        this.listeners.add(listener);
    }
    
    @Override
    public void removeListener(final ConfigurationListener listener) {
        this.listeners.remove(listener);
    }
    
    @Override
    public <T extends Appender> T getAppender(final String appenderName) {
        return (T)((appenderName != null) ? this.appenders.get(appenderName) : null);
    }
    
    @Override
    public Map<String, Appender> getAppenders() {
        return this.appenders;
    }
    
    @Override
    public void addAppender(final Appender appender) {
        if (appender != null) {
            this.appenders.putIfAbsent(appender.getName(), appender);
        }
    }
    
    @Override
    public StrSubstitutor getStrSubstitutor() {
        return this.subst;
    }
    
    @Override
    public void setAdvertiser(final Advertiser advertiser) {
        this.advertiser = advertiser;
    }
    
    @Override
    public Advertiser getAdvertiser() {
        return this.advertiser;
    }
    
    @Override
    public ReliabilityStrategy getReliabilityStrategy(final LoggerConfig loggerConfig) {
        return ReliabilityStrategyFactory.getReliabilityStrategy(loggerConfig);
    }
    
    @Override
    public synchronized void addLoggerAppender(final Logger logger, final Appender appender) {
        if (appender == null || logger == null) {
            return;
        }
        final String loggerName = logger.getName();
        this.appenders.putIfAbsent(appender.getName(), appender);
        final LoggerConfig lc = this.getLoggerConfig(loggerName);
        if (lc.getName().equals(loggerName)) {
            lc.addAppender(appender, null, null);
        }
        else {
            final LoggerConfig nlc = new LoggerConfig(loggerName, lc.getLevel(), lc.isAdditive());
            nlc.addAppender(appender, null, null);
            nlc.setParent(lc);
            this.loggerConfigs.putIfAbsent(loggerName, nlc);
            this.setParents();
            logger.getContext().updateLoggers();
        }
    }
    
    @Override
    public synchronized void addLoggerFilter(final Logger logger, final Filter filter) {
        final String loggerName = logger.getName();
        final LoggerConfig lc = this.getLoggerConfig(loggerName);
        if (lc.getName().equals(loggerName)) {
            lc.addFilter(filter);
        }
        else {
            final LoggerConfig nlc = new LoggerConfig(loggerName, lc.getLevel(), lc.isAdditive());
            nlc.addFilter(filter);
            nlc.setParent(lc);
            this.loggerConfigs.putIfAbsent(loggerName, nlc);
            this.setParents();
            logger.getContext().updateLoggers();
        }
    }
    
    @Override
    public synchronized void setLoggerAdditive(final Logger logger, final boolean additive) {
        final String loggerName = logger.getName();
        final LoggerConfig lc = this.getLoggerConfig(loggerName);
        if (lc.getName().equals(loggerName)) {
            lc.setAdditive(additive);
        }
        else {
            final LoggerConfig nlc = new LoggerConfig(loggerName, lc.getLevel(), additive);
            nlc.setParent(lc);
            this.loggerConfigs.putIfAbsent(loggerName, nlc);
            this.setParents();
            logger.getContext().updateLoggers();
        }
    }
    
    public synchronized void removeAppender(final String appenderName) {
        for (final LoggerConfig logger : this.loggerConfigs.values()) {
            logger.removeAppender(appenderName);
        }
        final Appender app = (appenderName != null) ? this.appenders.remove(appenderName) : null;
        if (app != null) {
            app.stop();
        }
    }
    
    @Override
    public List<CustomLevelConfig> getCustomLevels() {
        return Collections.unmodifiableList((List<? extends CustomLevelConfig>)this.customLevels);
    }
    
    @Override
    public LoggerConfig getLoggerConfig(final String loggerName) {
        LoggerConfig loggerConfig = this.loggerConfigs.get(loggerName);
        if (loggerConfig != null) {
            return loggerConfig;
        }
        String substr = loggerName;
        while ((substr = NameUtil.getSubName(substr)) != null) {
            loggerConfig = this.loggerConfigs.get(substr);
            if (loggerConfig != null) {
                return loggerConfig;
            }
        }
        return this.root;
    }
    
    @Override
    public LoggerContext getLoggerContext() {
        return this.loggerContext.get();
    }
    
    @Override
    public LoggerConfig getRootLogger() {
        return this.root;
    }
    
    @Override
    public Map<String, LoggerConfig> getLoggers() {
        return Collections.unmodifiableMap((Map<? extends String, ? extends LoggerConfig>)this.loggerConfigs);
    }
    
    public LoggerConfig getLogger(final String loggerName) {
        return this.loggerConfigs.get(loggerName);
    }
    
    @Override
    public synchronized void addLogger(final String loggerName, final LoggerConfig loggerConfig) {
        this.loggerConfigs.putIfAbsent(loggerName, loggerConfig);
        this.setParents();
    }
    
    @Override
    public synchronized void removeLogger(final String loggerName) {
        this.loggerConfigs.remove(loggerName);
        this.setParents();
    }
    
    @Override
    public void createConfiguration(final Node node, final LogEvent event) {
        final PluginType<?> type = node.getType();
        if (type != null && type.isDeferChildren()) {
            node.setObject(this.createPluginObject(type, node, event));
        }
        else {
            for (final Node child : node.getChildren()) {
                this.createConfiguration(child, event);
            }
            if (type == null) {
                if (node.getParent() != null) {
                    AbstractConfiguration.LOGGER.error("Unable to locate plugin for {}", node.getName());
                }
            }
            else {
                node.setObject(this.createPluginObject(type, node, event));
            }
        }
    }
    
    public Object createPluginObject(final PluginType<?> type, final Node node) {
        if (this.getState().equals(LifeCycle.State.INITIALIZING)) {
            return this.createPluginObject(type, node, null);
        }
        AbstractConfiguration.LOGGER.warn("Plugin Object creation is not allowed after initialization");
        return null;
    }
    
    private Object createPluginObject(final PluginType<?> type, final Node node, final LogEvent event) {
        final Class<?> clazz = type.getPluginClass();
        if (Map.class.isAssignableFrom(clazz)) {
            try {
                return createPluginMap(node);
            }
            catch (Exception e) {
                AbstractConfiguration.LOGGER.warn("Unable to create Map for {} of class {}", type.getElementName(), clazz, e);
            }
        }
        if (Collection.class.isAssignableFrom(clazz)) {
            try {
                return createPluginCollection(node);
            }
            catch (Exception e) {
                AbstractConfiguration.LOGGER.warn("Unable to create List for {} of class {}", type.getElementName(), clazz, e);
            }
        }
        return new PluginBuilder(type).withConfiguration(this).withConfigurationNode(node).forLogEvent(event).build();
    }
    
    private static Map<String, ?> createPluginMap(final Node node) {
        final Map<String, Object> map = new LinkedHashMap<String, Object>();
        for (final Node child : node.getChildren()) {
            final Object object = child.getObject();
            map.put(child.getName(), object);
        }
        return map;
    }
    
    private static Collection<?> createPluginCollection(final Node node) {
        final List<Node> children = node.getChildren();
        final Collection<Object> list = new ArrayList<Object>(children.size());
        for (final Node child : children) {
            final Object object = child.getObject();
            list.add(object);
        }
        return list;
    }
    
    private void setParents() {
        for (final Map.Entry<String, LoggerConfig> entry : this.loggerConfigs.entrySet()) {
            final LoggerConfig logger = entry.getValue();
            String key = entry.getKey();
            if (!key.isEmpty()) {
                final int i = key.lastIndexOf(46);
                if (i > 0) {
                    key = key.substring(0, i);
                    LoggerConfig parent = this.getLoggerConfig(key);
                    if (parent == null) {
                        parent = this.root;
                    }
                    logger.setParent(parent);
                }
                else {
                    logger.setParent(this.root);
                }
            }
        }
    }
    
    protected static byte[] toByteArray(final InputStream is) throws IOException {
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        final byte[] data = new byte[16384];
        int nRead;
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }
    
    @Override
    public NanoClock getNanoClock() {
        return this.nanoClock;
    }
    
    @Override
    public void setNanoClock(final NanoClock nanoClock) {
        this.nanoClock = Objects.requireNonNull(nanoClock, "nanoClock");
    }
}
