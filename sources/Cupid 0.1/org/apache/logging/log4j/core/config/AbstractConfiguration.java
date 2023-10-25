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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.async.AsyncLoggerConfigDelegate;
import org.apache.logging.log4j.core.async.AsyncLoggerConfigDisruptor;
import org.apache.logging.log4j.core.config.arbiters.Arbiter;
import org.apache.logging.log4j.core.config.arbiters.SelectArbiter;
import org.apache.logging.log4j.core.config.plugins.util.PluginBuilder;
import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
import org.apache.logging.log4j.core.config.plugins.util.PluginType;
import org.apache.logging.log4j.core.filter.AbstractFilterable;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.lookup.ConfigurationStrSubstitutor;
import org.apache.logging.log4j.core.lookup.Interpolator;
import org.apache.logging.log4j.core.lookup.MapLookup;
import org.apache.logging.log4j.core.lookup.RuntimeStrSubstitutor;
import org.apache.logging.log4j.core.lookup.StrLookup;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.script.AbstractScript;
import org.apache.logging.log4j.core.script.ScriptManager;
import org.apache.logging.log4j.core.util.DummyNanoClock;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.core.util.NameUtil;
import org.apache.logging.log4j.core.util.NanoClock;
import org.apache.logging.log4j.core.util.Source;
import org.apache.logging.log4j.core.util.WatchManager;
import org.apache.logging.log4j.core.util.Watcher;
import org.apache.logging.log4j.core.util.WatcherFactory;
import org.apache.logging.log4j.util.PropertiesUtil;

public abstract class AbstractConfiguration extends AbstractFilterable implements Configuration {
  private static final int BUF_SIZE = 16384;
  
  protected Node rootNode;
  
  protected final List<ConfigurationListener> listeners = new CopyOnWriteArrayList<>();
  
  protected final List<String> pluginPackages = new ArrayList<>();
  
  protected PluginManager pluginManager;
  
  protected boolean isShutdownHookEnabled = true;
  
  protected long shutdownTimeoutMillis = 0L;
  
  protected ScriptManager scriptManager;
  
  private Advertiser advertiser = new DefaultAdvertiser();
  
  private Node advertiserNode = null;
  
  private Object advertisement;
  
  private String name;
  
  private ConcurrentMap<String, Appender> appenders = new ConcurrentHashMap<>();
  
  private ConcurrentMap<String, LoggerConfig> loggerConfigs = new ConcurrentHashMap<>();
  
  private List<CustomLevelConfig> customLevels = Collections.emptyList();
  
  private final ConcurrentMap<String, String> propertyMap = new ConcurrentHashMap<>();
  
  private final StrLookup tempLookup = (StrLookup)new Interpolator(this.propertyMap);
  
  private final StrSubstitutor subst = (StrSubstitutor)new RuntimeStrSubstitutor(this.tempLookup);
  
  private final StrSubstitutor configurationStrSubstitutor = (StrSubstitutor)new ConfigurationStrSubstitutor(this.subst);
  
  private LoggerConfig root = new LoggerConfig();
  
  private final ConcurrentMap<String, Object> componentMap = new ConcurrentHashMap<>();
  
  private final ConfigurationSource configurationSource;
  
  private final ConfigurationScheduler configurationScheduler = new ConfigurationScheduler();
  
  private final WatchManager watchManager = new WatchManager(this.configurationScheduler);
  
  private AsyncLoggerConfigDisruptor asyncLoggerConfigDisruptor;
  
  private NanoClock nanoClock = (NanoClock)new DummyNanoClock();
  
  private final WeakReference<LoggerContext> loggerContext;
  
  protected AbstractConfiguration(LoggerContext loggerContext, ConfigurationSource configurationSource) {
    this.loggerContext = new WeakReference<>(loggerContext);
    this.configurationSource = Objects.<ConfigurationSource>requireNonNull(configurationSource, "configurationSource is null");
    this.componentMap.put("ContextProperties", this.propertyMap);
    this.pluginManager = new PluginManager("Core");
    this.rootNode = new Node();
    setState(LifeCycle.State.INITIALIZING);
  }
  
  public ConfigurationSource getConfigurationSource() {
    return this.configurationSource;
  }
  
  public List<String> getPluginPackages() {
    return this.pluginPackages;
  }
  
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
    if (this.asyncLoggerConfigDisruptor == null)
      this.asyncLoggerConfigDisruptor = new AsyncLoggerConfigDisruptor(); 
    return (AsyncLoggerConfigDelegate)this.asyncLoggerConfigDisruptor;
  }
  
  public void initialize() {
    LOGGER.debug(Version.getProductString() + " initializing configuration {}", this);
    this.subst.setConfiguration(this);
    this.configurationStrSubstitutor.setConfiguration(this);
    try {
      this.scriptManager = new ScriptManager(this, this.watchManager);
    } catch (LinkageError|Exception e) {
      LOGGER.info("Cannot initialize scripting support because this JRE does not support it.", e);
    } 
    this.pluginManager.collectPlugins(this.pluginPackages);
    PluginManager levelPlugins = new PluginManager("Level");
    levelPlugins.collectPlugins(this.pluginPackages);
    Map<String, PluginType<?>> plugins = levelPlugins.getPlugins();
    if (plugins != null)
      for (PluginType<?> type : plugins.values()) {
        try {
          Loader.initializeClass(type.getPluginClass().getName(), type.getPluginClass().getClassLoader());
        } catch (Exception e) {
          LOGGER.error("Unable to initialize {} due to {}", type.getPluginClass().getName(), e.getClass()
              .getSimpleName(), e);
        } 
      }  
    setup();
    setupAdvertisement();
    doConfigure();
    setState(LifeCycle.State.INITIALIZED);
    LOGGER.debug("Configuration {} initialized", this);
  }
  
  protected void initializeWatchers(Reconfigurable reconfigurable, ConfigurationSource configSource, int monitorIntervalSeconds) {
    if (configSource.getFile() != null || configSource.getURL() != null)
      if (monitorIntervalSeconds > 0) {
        this.watchManager.setIntervalSeconds(monitorIntervalSeconds);
        if (configSource.getFile() != null) {
          Source cfgSource = new Source(configSource);
          long lastModifeid = configSource.getFile().lastModified();
          ConfigurationFileWatcher watcher = new ConfigurationFileWatcher(this, reconfigurable, this.listeners, lastModifeid);
          this.watchManager.watch(cfgSource, (Watcher)watcher);
        } else if (configSource.getURL() != null) {
          monitorSource(reconfigurable, configSource);
        } 
      } else if (this.watchManager.hasEventListeners() && configSource.getURL() != null && monitorIntervalSeconds >= 0) {
        monitorSource(reconfigurable, configSource);
      }  
  }
  
  private void monitorSource(Reconfigurable reconfigurable, ConfigurationSource configSource) {
    if (configSource.getLastModified() > 0L) {
      Source cfgSource = new Source(configSource);
      Watcher watcher = WatcherFactory.getInstance(this.pluginPackages).newWatcher(cfgSource, this, reconfigurable, this.listeners, configSource.getLastModified());
      if (watcher != null)
        this.watchManager.watch(cfgSource, watcher); 
    } else {
      LOGGER.info("{} does not support dynamic reconfiguration", configSource.getURI());
    } 
  }
  
  public void start() {
    if (getState().equals(LifeCycle.State.INITIALIZING))
      initialize(); 
    LOGGER.debug("Starting configuration {}", this);
    setStarting();
    if (this.watchManager.getIntervalSeconds() >= 0)
      this.watchManager.start(); 
    if (hasAsyncLoggers())
      this.asyncLoggerConfigDisruptor.start(); 
    Set<LoggerConfig> alreadyStarted = new HashSet<>();
    for (LoggerConfig logger : this.loggerConfigs.values()) {
      logger.start();
      alreadyStarted.add(logger);
    } 
    for (Appender appender : this.appenders.values())
      appender.start(); 
    if (!alreadyStarted.contains(this.root))
      this.root.start(); 
    super.start();
    LOGGER.debug("Started configuration {} OK.", this);
  }
  
  private boolean hasAsyncLoggers() {
    if (this.root instanceof org.apache.logging.log4j.core.async.AsyncLoggerConfig)
      return true; 
    for (LoggerConfig logger : this.loggerConfigs.values()) {
      if (logger instanceof org.apache.logging.log4j.core.async.AsyncLoggerConfig)
        return true; 
    } 
    return false;
  }
  
  public boolean stop(long timeout, TimeUnit timeUnit) {
    setStopping();
    stop(timeout, timeUnit, false);
    LOGGER.trace("Stopping {}...", this);
    for (LoggerConfig loggerConfig : this.loggerConfigs.values())
      loggerConfig.getReliabilityStrategy().beforeStopConfiguration(this); 
    this.root.getReliabilityStrategy().beforeStopConfiguration(this);
    String cls = getClass().getSimpleName();
    LOGGER.trace("{} notified {} ReliabilityStrategies that config will be stopped.", cls, Integer.valueOf(this.loggerConfigs.size() + 1));
    if (!this.loggerConfigs.isEmpty()) {
      LOGGER.trace("{} stopping {} LoggerConfigs.", cls, Integer.valueOf(this.loggerConfigs.size()));
      for (LoggerConfig logger : this.loggerConfigs.values())
        logger.stop(timeout, timeUnit); 
    } 
    LOGGER.trace("{} stopping root LoggerConfig.", cls);
    if (!this.root.isStopped())
      this.root.stop(timeout, timeUnit); 
    if (hasAsyncLoggers()) {
      LOGGER.trace("{} stopping AsyncLoggerConfigDisruptor.", cls);
      this.asyncLoggerConfigDisruptor.stop(timeout, timeUnit);
    } 
    LOGGER.trace("{} notifying ReliabilityStrategies that appenders will be stopped.", cls);
    for (LoggerConfig loggerConfig : this.loggerConfigs.values())
      loggerConfig.getReliabilityStrategy().beforeStopAppenders(); 
    this.root.getReliabilityStrategy().beforeStopAppenders();
    Appender[] array = (Appender[])this.appenders.values().toArray((Object[])new Appender[this.appenders.size()]);
    List<Appender> async = getAsyncAppenders(array);
    if (!async.isEmpty()) {
      LOGGER.trace("{} stopping {} AsyncAppenders.", cls, Integer.valueOf(async.size()));
      for (Appender appender : async) {
        if (appender instanceof LifeCycle2) {
          ((LifeCycle2)appender).stop(timeout, timeUnit);
          continue;
        } 
        appender.stop();
      } 
    } 
    LOGGER.trace("{} stopping remaining Appenders.", cls);
    int appenderCount = 0;
    for (int i = array.length - 1; i >= 0; i--) {
      if (array[i].isStarted()) {
        if (array[i] instanceof LifeCycle2) {
          ((LifeCycle2)array[i]).stop(timeout, timeUnit);
        } else {
          array[i].stop();
        } 
        appenderCount++;
      } 
    } 
    LOGGER.trace("{} stopped {} remaining Appenders.", cls, Integer.valueOf(appenderCount));
    LOGGER.trace("{} cleaning Appenders from {} LoggerConfigs.", cls, Integer.valueOf(this.loggerConfigs.size() + 1));
    for (LoggerConfig loggerConfig : this.loggerConfigs.values())
      loggerConfig.clearAppenders(); 
    this.root.clearAppenders();
    if (this.watchManager.isStarted())
      this.watchManager.stop(timeout, timeUnit); 
    this.configurationScheduler.stop(timeout, timeUnit);
    if (this.advertiser != null && this.advertisement != null)
      this.advertiser.unadvertise(this.advertisement); 
    setStopped();
    LOGGER.debug("Stopped {} OK", this);
    return true;
  }
  
  private List<Appender> getAsyncAppenders(Appender[] all) {
    List<Appender> result = new ArrayList<>();
    for (int i = all.length - 1; i >= 0; i--) {
      if (all[i] instanceof org.apache.logging.log4j.core.appender.AsyncAppender)
        result.add(all[i]); 
    } 
    return result;
  }
  
  public boolean isShutdownHookEnabled() {
    return this.isShutdownHookEnabled;
  }
  
  public long getShutdownTimeoutMillis() {
    return this.shutdownTimeoutMillis;
  }
  
  public void setup() {}
  
  protected Level getDefaultStatus() {
    String statusLevel = PropertiesUtil.getProperties().getStringProperty("Log4jDefaultStatusLevel", Level.ERROR
        .name());
    try {
      return Level.toLevel(statusLevel);
    } catch (Exception ex) {
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
      if (configSource.getLocation() != null)
        attributes.put("location", configSource.getLocation()); 
      this.advertiserNode = node;
    } 
  }
  
  private void setupAdvertisement() {
    if (this.advertiserNode != null) {
      String nodeName = this.advertiserNode.getName();
      PluginType<?> type = this.pluginManager.getPluginType(nodeName);
      if (type != null) {
        Class<? extends Advertiser> clazz = type.getPluginClass().asSubclass(Advertiser.class);
        try {
          this.advertiser = clazz.newInstance();
          this.advertisement = this.advertiser.advertise(this.advertiserNode.getAttributes());
        } catch (InstantiationException e) {
          LOGGER.error("InstantiationException attempting to instantiate advertiser: {}", nodeName, e);
        } catch (IllegalAccessException e) {
          LOGGER.error("IllegalAccessException attempting to instantiate advertiser: {}", nodeName, e);
        } 
      } 
    } 
  }
  
  public <T> T getComponent(String componentName) {
    return (T)this.componentMap.get(componentName);
  }
  
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
        Class<?> clazz = child.getType().getPluginClass();
        if (clazz.isAnnotationPresent((Class)Scheduled.class))
          this.configurationScheduler.incrementScheduledItems(); 
        preConfigure(child);
      } 
    } catch (Exception ex) {
      LOGGER.error("Error capturing node data for node " + node.getName(), ex);
    } 
  }
  
  protected void processConditionals(Node node) {
    try {
      List<Node> addList = new ArrayList<>();
      List<Node> removeList = new ArrayList<>();
      for (Node child : node.getChildren()) {
        PluginType<?> type = child.getType();
        if (type != null && "Arbiter".equals(type.getElementName())) {
          Class<?> clazz = type.getPluginClass();
          if (SelectArbiter.class.isAssignableFrom(clazz)) {
            removeList.add(child);
            addList.addAll(processSelect(child, type));
            continue;
          } 
          if (Arbiter.class.isAssignableFrom(clazz)) {
            removeList.add(child);
            try {
              Arbiter condition = (Arbiter)createPluginObject(type, child, (LogEvent)null);
              if (condition.isCondition()) {
                addList.addAll(child.getChildren());
                processConditionals(child);
              } 
            } catch (Exception inner) {
              LOGGER.error("Exception processing {}: Ignoring and including children", type
                  .getPluginClass());
              processConditionals(child);
            } 
            continue;
          } 
          LOGGER.error("Encountered Condition Plugin that does not implement Condition: {}", child
              .getName());
          processConditionals(child);
          continue;
        } 
        processConditionals(child);
      } 
      if (!removeList.isEmpty()) {
        List<Node> children = node.getChildren();
        children.removeAll(removeList);
        children.addAll(addList);
        for (Node grandChild : addList)
          grandChild.setParent(node); 
      } 
    } catch (Exception ex) {
      LOGGER.error("Error capturing node data for node " + node.getName(), ex);
    } 
  }
  
  protected List<Node> processSelect(Node selectNode, PluginType<?> type) {
    List<Node> addList = new ArrayList<>();
    SelectArbiter select = (SelectArbiter)createPluginObject(type, selectNode, (LogEvent)null);
    List<Arbiter> conditions = new ArrayList<>();
    for (Node child : selectNode.getChildren()) {
      PluginType<?> nodeType = child.getType();
      if (nodeType != null) {
        if (Arbiter.class.isAssignableFrom(nodeType.getPluginClass())) {
          Arbiter arbiter = (Arbiter)createPluginObject(nodeType, child, (LogEvent)null);
          conditions.add(arbiter);
          child.setObject(arbiter);
          continue;
        } 
        LOGGER.error("Invalid Node {} for Select. Must be a Condition", child
            .getName());
        continue;
      } 
      LOGGER.error("No PluginType for node {}", child.getName());
    } 
    Arbiter condition = select.evaluateConditions(conditions);
    if (condition != null)
      for (Node child : selectNode.getChildren()) {
        if (condition == child.getObject()) {
          addList.addAll(child.getChildren());
          processConditionals(child);
        } 
      }  
    return addList;
  }
  
  protected void doConfigure() {
    processConditionals(this.rootNode);
    preConfigure(this.rootNode);
    this.configurationScheduler.start();
    if (this.rootNode.hasChildren() && ((Node)this.rootNode.getChildren().get(0)).getName().equalsIgnoreCase("Properties")) {
      Node first = this.rootNode.getChildren().get(0);
      createConfiguration(first, (LogEvent)null);
      if (first.getObject() != null) {
        StrLookup lookup = first.<StrLookup>getObject();
        this.subst.setVariableResolver(lookup);
        this.configurationStrSubstitutor.setVariableResolver(lookup);
      } 
    } else {
      Map<String, String> map = getComponent("ContextProperties");
      MapLookup mapLookup = (map == null) ? null : new MapLookup(map);
      Interpolator interpolator = new Interpolator((StrLookup)mapLookup, this.pluginPackages);
      this.subst.setVariableResolver((StrLookup)interpolator);
      this.configurationStrSubstitutor.setVariableResolver((StrLookup)interpolator);
    } 
    boolean setLoggers = false;
    boolean setRoot = false;
    for (Node child : this.rootNode.getChildren()) {
      if (child.getName().equalsIgnoreCase("Properties")) {
        if (this.tempLookup == this.subst.getVariableResolver())
          LOGGER.error("Properties declaration must be the first element in the configuration"); 
        continue;
      } 
      createConfiguration(child, (LogEvent)null);
      if (child.getObject() == null)
        continue; 
      if (child.getName().equalsIgnoreCase("Scripts")) {
        for (AbstractScript script : (AbstractScript[])child.<AbstractScript[]>getObject((Class)AbstractScript[].class)) {
          if (script instanceof org.apache.logging.log4j.core.script.ScriptRef) {
            LOGGER.error("Script reference to {} not added. Scripts definition cannot contain script references", script
                .getName());
          } else if (this.scriptManager != null) {
            this.scriptManager.addScript(script);
          } 
        } 
        continue;
      } 
      if (child.getName().equalsIgnoreCase("Appenders")) {
        this.appenders = child.<ConcurrentMap<String, Appender>>getObject();
        continue;
      } 
      if (child.isInstanceOf(Filter.class)) {
        addFilter(child.<Filter>getObject(Filter.class));
        continue;
      } 
      if (child.getName().equalsIgnoreCase("Loggers")) {
        Loggers l = child.<Loggers>getObject();
        this.loggerConfigs = l.getMap();
        setLoggers = true;
        if (l.getRoot() != null) {
          this.root = l.getRoot();
          setRoot = true;
        } 
        continue;
      } 
      if (child.getName().equalsIgnoreCase("CustomLevels")) {
        this.customLevels = ((CustomLevels)child.<CustomLevels>getObject(CustomLevels.class)).getCustomLevels();
        continue;
      } 
      if (child.isInstanceOf(CustomLevelConfig.class)) {
        List<CustomLevelConfig> copy = new ArrayList<>(this.customLevels);
        copy.add(child.getObject(CustomLevelConfig.class));
        this.customLevels = copy;
        continue;
      } 
      List<String> expected = Arrays.asList(new String[] { "\"Appenders\"", "\"Loggers\"", "\"Properties\"", "\"Scripts\"", "\"CustomLevels\"" });
      LOGGER.error("Unknown object \"{}\" of type {} is ignored: try nesting it inside one of: {}.", child
          .getName(), child.<T>getObject().getClass().getName(), expected);
    } 
    if (!setLoggers) {
      LOGGER.warn("No Loggers were configured, using default. Is the Loggers element missing?");
      setToDefault();
      return;
    } 
    if (!setRoot) {
      LOGGER.warn("No Root logger was configured, creating default ERROR-level Root logger with Console appender");
      setToDefault();
    } 
    for (Map.Entry<String, LoggerConfig> entry : this.loggerConfigs.entrySet()) {
      LoggerConfig loggerConfig = entry.getValue();
      for (AppenderRef ref : loggerConfig.getAppenderRefs()) {
        Appender app = this.appenders.get(ref.getRef());
        if (app != null) {
          loggerConfig.addAppender(app, ref.getLevel(), ref.getFilter());
          continue;
        } 
        LOGGER.error("Unable to locate appender \"{}\" for logger config \"{}\"", ref.getRef(), loggerConfig);
      } 
    } 
    setParents();
  }
  
  protected void setToDefault() {
    setName("Default@" + Integer.toHexString(hashCode()));
    PatternLayout patternLayout = PatternLayout.newBuilder().withPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n").withConfiguration(this).build();
    ConsoleAppender consoleAppender = ConsoleAppender.createDefaultAppenderForLayout((Layout)patternLayout);
    consoleAppender.start();
    addAppender((Appender)consoleAppender);
    LoggerConfig rootLoggerConfig = getRootLogger();
    rootLoggerConfig.addAppender((Appender)consoleAppender, (Level)null, (Filter)null);
    Level defaultLevel = Level.ERROR;
    String levelName = PropertiesUtil.getProperties().getStringProperty("org.apache.logging.log4j.level", defaultLevel
        .name());
    Level level = Level.valueOf(levelName);
    rootLoggerConfig.setLevel((level != null) ? level : defaultLevel);
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void addListener(ConfigurationListener listener) {
    this.listeners.add(listener);
  }
  
  public void removeListener(ConfigurationListener listener) {
    this.listeners.remove(listener);
  }
  
  public <T extends Appender> T getAppender(String appenderName) {
    return (appenderName != null) ? (T)this.appenders.get(appenderName) : null;
  }
  
  public Map<String, Appender> getAppenders() {
    return this.appenders;
  }
  
  public void addAppender(Appender appender) {
    if (appender != null)
      this.appenders.putIfAbsent(appender.getName(), appender); 
  }
  
  public StrSubstitutor getStrSubstitutor() {
    return this.subst;
  }
  
  public StrSubstitutor getConfigurationStrSubstitutor() {
    return this.configurationStrSubstitutor;
  }
  
  public void setAdvertiser(Advertiser advertiser) {
    this.advertiser = advertiser;
  }
  
  public Advertiser getAdvertiser() {
    return this.advertiser;
  }
  
  public ReliabilityStrategy getReliabilityStrategy(LoggerConfig loggerConfig) {
    return ReliabilityStrategyFactory.getReliabilityStrategy(loggerConfig);
  }
  
  public synchronized void addLoggerAppender(Logger logger, Appender appender) {
    if (appender == null || logger == null)
      return; 
    String loggerName = logger.getName();
    this.appenders.putIfAbsent(appender.getName(), appender);
    LoggerConfig lc = getLoggerConfig(loggerName);
    if (lc.getName().equals(loggerName)) {
      lc.addAppender(appender, (Level)null, (Filter)null);
    } else {
      LoggerConfig nlc = new LoggerConfig(loggerName, lc.getLevel(), lc.isAdditive());
      nlc.addAppender(appender, (Level)null, (Filter)null);
      nlc.setParent(lc);
      this.loggerConfigs.putIfAbsent(loggerName, nlc);
      setParents();
      logger.getContext().updateLoggers();
    } 
  }
  
  public synchronized void addLoggerFilter(Logger logger, Filter filter) {
    String loggerName = logger.getName();
    LoggerConfig lc = getLoggerConfig(loggerName);
    if (lc.getName().equals(loggerName)) {
      lc.addFilter(filter);
    } else {
      LoggerConfig nlc = new LoggerConfig(loggerName, lc.getLevel(), lc.isAdditive());
      nlc.addFilter(filter);
      nlc.setParent(lc);
      this.loggerConfigs.putIfAbsent(loggerName, nlc);
      setParents();
      logger.getContext().updateLoggers();
    } 
  }
  
  public synchronized void setLoggerAdditive(Logger logger, boolean additive) {
    String loggerName = logger.getName();
    LoggerConfig lc = getLoggerConfig(loggerName);
    if (lc.getName().equals(loggerName)) {
      lc.setAdditive(additive);
    } else {
      LoggerConfig nlc = new LoggerConfig(loggerName, lc.getLevel(), additive);
      nlc.setParent(lc);
      this.loggerConfigs.putIfAbsent(loggerName, nlc);
      setParents();
      logger.getContext().updateLoggers();
    } 
  }
  
  public synchronized void removeAppender(String appenderName) {
    for (LoggerConfig logger : this.loggerConfigs.values())
      logger.removeAppender(appenderName); 
    Appender app = (appenderName != null) ? this.appenders.remove(appenderName) : null;
    if (app != null)
      app.stop(); 
  }
  
  public List<CustomLevelConfig> getCustomLevels() {
    return Collections.unmodifiableList(this.customLevels);
  }
  
  public LoggerConfig getLoggerConfig(String loggerName) {
    LoggerConfig loggerConfig = this.loggerConfigs.get(loggerName);
    if (loggerConfig != null)
      return loggerConfig; 
    String substr = loggerName;
    while ((substr = NameUtil.getSubName(substr)) != null) {
      loggerConfig = this.loggerConfigs.get(substr);
      if (loggerConfig != null)
        return loggerConfig; 
    } 
    return this.root;
  }
  
  public LoggerContext getLoggerContext() {
    return this.loggerContext.get();
  }
  
  public LoggerConfig getRootLogger() {
    return this.root;
  }
  
  public Map<String, LoggerConfig> getLoggers() {
    return Collections.unmodifiableMap(this.loggerConfigs);
  }
  
  public LoggerConfig getLogger(String loggerName) {
    return this.loggerConfigs.get(loggerName);
  }
  
  public synchronized void addLogger(String loggerName, LoggerConfig loggerConfig) {
    this.loggerConfigs.putIfAbsent(loggerName, loggerConfig);
    setParents();
  }
  
  public synchronized void removeLogger(String loggerName) {
    this.loggerConfigs.remove(loggerName);
    setParents();
  }
  
  public void createConfiguration(Node node, LogEvent event) {
    PluginType<?> type = node.getType();
    if (type != null && type.isDeferChildren()) {
      node.setObject(createPluginObject(type, node, event));
    } else {
      for (Node child : node.getChildren())
        createConfiguration(child, event); 
      if (type == null) {
        if (node.getParent() != null)
          LOGGER.error("Unable to locate plugin for {}", node.getName()); 
      } else {
        node.setObject(createPluginObject(type, node, event));
      } 
    } 
  }
  
  public Object createPluginObject(PluginType<?> type, Node node) {
    if (getState().equals(LifeCycle.State.INITIALIZING))
      return createPluginObject(type, node, (LogEvent)null); 
    LOGGER.warn("Plugin Object creation is not allowed after initialization");
    return null;
  }
  
  private Object createPluginObject(PluginType<?> type, Node node, LogEvent event) {
    Class<?> clazz = type.getPluginClass();
    if (Map.class.isAssignableFrom(clazz))
      try {
        return createPluginMap(node);
      } catch (Exception e) {
        LOGGER.warn("Unable to create Map for {} of class {}", type.getElementName(), clazz, e);
      }  
    if (Collection.class.isAssignableFrom(clazz))
      try {
        return createPluginCollection(node);
      } catch (Exception e) {
        LOGGER.warn("Unable to create List for {} of class {}", type.getElementName(), clazz, e);
      }  
    return (new PluginBuilder(type)).withConfiguration(this).withConfigurationNode(node).forLogEvent(event).build();
  }
  
  private static Map<String, ?> createPluginMap(Node node) {
    Map<String, Object> map = new LinkedHashMap<>();
    for (Node child : node.getChildren()) {
      Object object = child.getObject();
      map.put(child.getName(), object);
    } 
    return map;
  }
  
  private static Collection<?> createPluginCollection(Node node) {
    List<Node> children = node.getChildren();
    Collection<Object> list = new ArrayList(children.size());
    for (Node child : children) {
      Object object = child.getObject();
      list.add(object);
    } 
    return list;
  }
  
  private void setParents() {
    for (Map.Entry<String, LoggerConfig> entry : this.loggerConfigs.entrySet()) {
      LoggerConfig logger = entry.getValue();
      String key = entry.getKey();
      if (!key.isEmpty()) {
        int i = key.lastIndexOf('.');
        if (i > 0) {
          key = key.substring(0, i);
          LoggerConfig parent = getLoggerConfig(key);
          if (parent == null)
            parent = this.root; 
          logger.setParent(parent);
          continue;
        } 
        logger.setParent(this.root);
      } 
    } 
  }
  
  protected static byte[] toByteArray(InputStream is) throws IOException {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    byte[] data = new byte[16384];
    int nRead;
    while ((nRead = is.read(data, 0, data.length)) != -1)
      buffer.write(data, 0, nRead); 
    return buffer.toByteArray();
  }
  
  public NanoClock getNanoClock() {
    return this.nanoClock;
  }
  
  public void setNanoClock(NanoClock nanoClock) {
    this.nanoClock = Objects.<NanoClock>requireNonNull(nanoClock, "nanoClock");
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\AbstractConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */