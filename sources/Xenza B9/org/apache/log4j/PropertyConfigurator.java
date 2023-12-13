// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j;

import org.apache.log4j.helpers.FileWatchdog;
import org.apache.log4j.spi.ThrowableRenderer;
import org.apache.log4j.spi.ThrowableRendererSupport;
import org.apache.log4j.or.RendererMap;
import org.apache.log4j.spi.RendererSupport;
import org.apache.log4j.config.Log4j1Configuration;
import java.util.StringTokenizer;
import java.util.Enumeration;
import org.apache.log4j.bridge.FilterAdapter;
import org.apache.log4j.spi.Filter;
import java.util.Vector;
import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.OptionHandler;
import java.net.URLConnection;
import java.io.IOException;
import org.apache.logging.log4j.core.net.UrlConnectionFactory;
import java.io.InterruptedIOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import org.apache.log4j.config.PropertiesConfiguration;
import org.apache.log4j.config.PropertySetter;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.logging.log4j.core.config.Configuration;
import java.net.URL;
import java.util.Properties;
import org.apache.logging.log4j.util.StackLocatorUtil;
import java.io.InputStream;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.log4j.spi.LoggerRepository;
import java.util.Hashtable;
import org.apache.log4j.spi.Configurator;

public class PropertyConfigurator implements Configurator
{
    static final String CATEGORY_PREFIX = "log4j.category.";
    static final String LOGGER_PREFIX = "log4j.logger.";
    static final String FACTORY_PREFIX = "log4j.factory";
    static final String ADDITIVITY_PREFIX = "log4j.additivity.";
    static final String ROOT_CATEGORY_PREFIX = "log4j.rootCategory";
    static final String ROOT_LOGGER_PREFIX = "log4j.rootLogger";
    static final String APPENDER_PREFIX = "log4j.appender.";
    static final String RENDERER_PREFIX = "log4j.renderer.";
    static final String THRESHOLD_PREFIX = "log4j.threshold";
    private static final String THROWABLE_RENDERER_PREFIX = "log4j.throwableRenderer";
    private static final String LOGGER_REF = "logger-ref";
    private static final String ROOT_REF = "root-ref";
    private static final String APPENDER_REF_TAG = "appender-ref";
    public static final String LOGGER_FACTORY_KEY = "log4j.loggerFactory";
    private static final String RESET_KEY = "log4j.reset";
    private static final String INTERNAL_ROOT_NAME = "root";
    protected Hashtable registry;
    private LoggerRepository repository;
    protected LoggerFactory loggerFactory;
    
    public PropertyConfigurator() {
        this.registry = new Hashtable(11);
        this.loggerFactory = new DefaultCategoryFactory();
    }
    
    public static void configure(final InputStream inputStream) {
        new PropertyConfigurator().doConfigure(inputStream, LogManager.getLoggerRepository(), StackLocatorUtil.getCallerClassLoader(2));
    }
    
    public static void configure(final Properties properties) {
        new PropertyConfigurator().doConfigure(properties, LogManager.getLoggerRepository(), StackLocatorUtil.getCallerClassLoader(2));
    }
    
    public static void configure(final String fileName) {
        new PropertyConfigurator().doConfigure(fileName, LogManager.getLoggerRepository(), StackLocatorUtil.getCallerClassLoader(2));
    }
    
    public static void configure(final URL configURL) {
        new PropertyConfigurator().doConfigure(configURL, LogManager.getLoggerRepository(), StackLocatorUtil.getCallerClassLoader(2));
    }
    
    public static void configureAndWatch(final String configFilename) {
        configureAndWatch(configFilename, 60000L, StackLocatorUtil.getCallerClassLoader(2));
    }
    
    public static void configureAndWatch(final String configFilename, final long delayMillis) {
        configureAndWatch(configFilename, delayMillis, StackLocatorUtil.getCallerClassLoader(2));
    }
    
    static void configureAndWatch(final String configFilename, final long delay, final ClassLoader classLoader) {
        final PropertyWatchdog watchdog = new PropertyWatchdog(configFilename, classLoader);
        watchdog.setDelay(delay);
        watchdog.start();
    }
    
    private static Configuration reconfigure(final Configuration configuration) {
        org.apache.logging.log4j.core.config.Configurator.reconfigure(configuration);
        return configuration;
    }
    
    protected void configureLoggerFactory(final Properties properties) {
        final String factoryClassName = OptionConverter.findAndSubst("log4j.loggerFactory", properties);
        if (factoryClassName != null) {
            LogLog.debug("Setting category factory to [" + factoryClassName + "].");
            PropertySetter.setProperties(this.loggerFactory = (LoggerFactory)OptionConverter.instantiateByClassName(factoryClassName, LoggerFactory.class, this.loggerFactory), properties, "log4j.factory.");
        }
    }
    
    void configureRootCategory(final Properties properties, final LoggerRepository loggerRepository) {
        String effectiveFrefix = "log4j.rootLogger";
        String value = OptionConverter.findAndSubst("log4j.rootLogger", properties);
        if (value == null) {
            value = OptionConverter.findAndSubst("log4j.rootCategory", properties);
            effectiveFrefix = "log4j.rootCategory";
        }
        if (value == null) {
            LogLog.debug("Could not find root logger information. Is this OK?");
        }
        else {
            final Logger root = loggerRepository.getRootLogger();
            synchronized (root) {
                this.parseCategory(properties, root, effectiveFrefix, "root", value);
            }
        }
    }
    
    @Override
    public void doConfigure(final InputStream inputStream, final LoggerRepository loggerRepository) {
        this.doConfigure(inputStream, loggerRepository, StackLocatorUtil.getCallerClassLoader(2));
    }
    
    Configuration doConfigure(final InputStream inputStream, final LoggerRepository loggerRepository, final ClassLoader classLoader) {
        return this.doConfigure(this.loadProperties(inputStream), loggerRepository, classLoader);
    }
    
    public void doConfigure(final Properties properties, final LoggerRepository loggerRepository) {
        this.doConfigure(properties, loggerRepository, StackLocatorUtil.getCallerClassLoader(2));
    }
    
    Configuration doConfigure(final Properties properties, final LoggerRepository loggerRepository, final ClassLoader classLoader) {
        final PropertiesConfiguration configuration = new PropertiesConfiguration(LogManager.getContext(classLoader), properties);
        configuration.doConfigure();
        this.repository = loggerRepository;
        this.registry.clear();
        return reconfigure(configuration);
    }
    
    public void doConfigure(final String fileName, final LoggerRepository loggerRepository) {
        this.doConfigure(fileName, loggerRepository, StackLocatorUtil.getCallerClassLoader(2));
    }
    
    Configuration doConfigure(final String fileName, final LoggerRepository loggerRepository, final ClassLoader classLoader) {
        try (final InputStream inputStream = Files.newInputStream(Paths.get(fileName, new String[0]), new OpenOption[0])) {
            return this.doConfigure(inputStream, loggerRepository, classLoader);
        }
        catch (final Exception e) {
            if (e instanceof InterruptedIOException || e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            LogLog.error("Could not read configuration file [" + fileName + "].", e);
            LogLog.error("Ignoring configuration file [" + fileName + "].");
            return null;
        }
    }
    
    @Override
    public void doConfigure(final URL url, final LoggerRepository loggerRepository) {
        this.doConfigure(url, loggerRepository, StackLocatorUtil.getCallerClassLoader(2));
    }
    
    Configuration doConfigure(final URL url, final LoggerRepository loggerRepository, final ClassLoader classLoader) {
        LogLog.debug("Reading configuration from URL " + url);
        try {
            final URLConnection urlConnection = UrlConnectionFactory.createConnection(url);
            try (final InputStream inputStream = urlConnection.getInputStream()) {
                return this.doConfigure(inputStream, loggerRepository, classLoader);
            }
        }
        catch (final IOException e) {
            LogLog.error("Could not read configuration file from URL [" + url + "].", e);
            LogLog.error("Ignoring configuration file [" + url + "].");
            return null;
        }
    }
    
    private Properties loadProperties(final InputStream inputStream) {
        final Properties loaded = new Properties();
        try {
            loaded.load(inputStream);
        }
        catch (final IOException | IllegalArgumentException e) {
            if (e instanceof InterruptedIOException) {
                Thread.currentThread().interrupt();
            }
            LogLog.error("Could not read configuration file from InputStream [" + inputStream + "].", e);
            LogLog.error("Ignoring configuration InputStream [" + inputStream + "].");
            return null;
        }
        return loaded;
    }
    
    void parseAdditivityForLogger(final Properties properties, final Logger logger, final String loggerName) {
        final String value = OptionConverter.findAndSubst("log4j.additivity." + loggerName, properties);
        LogLog.debug("Handling log4j.additivity." + loggerName + "=[" + value + "]");
        if (value != null && !value.equals("")) {
            final boolean additivity = OptionConverter.toBoolean(value, true);
            LogLog.debug("Setting additivity for \"" + loggerName + "\" to " + additivity);
            logger.setAdditivity(additivity);
        }
    }
    
    Appender parseAppender(final Properties properties, final String appenderName) {
        Appender appender = this.registryGet(appenderName);
        if (appender != null) {
            LogLog.debug("Appender \"" + appenderName + "\" was already parsed.");
            return appender;
        }
        final String prefix = "log4j.appender." + appenderName;
        final String layoutPrefix = prefix + ".layout";
        appender = (Appender)OptionConverter.instantiateByKey(properties, prefix, Appender.class, null);
        if (appender == null) {
            LogLog.error("Could not instantiate appender named \"" + appenderName + "\".");
            return null;
        }
        appender.setName(appenderName);
        if (appender instanceof OptionHandler) {
            if (appender.requiresLayout()) {
                final Layout layout = (Layout)OptionConverter.instantiateByKey(properties, layoutPrefix, Layout.class, null);
                if (layout != null) {
                    appender.setLayout(layout);
                    LogLog.debug("Parsing layout options for \"" + appenderName + "\".");
                    PropertySetter.setProperties(layout, properties, layoutPrefix + ".");
                    LogLog.debug("End of parsing for \"" + appenderName + "\".");
                }
            }
            final String errorHandlerPrefix = prefix + ".errorhandler";
            final String errorHandlerClass = OptionConverter.findAndSubst(errorHandlerPrefix, properties);
            if (errorHandlerClass != null) {
                final ErrorHandler eh = (ErrorHandler)OptionConverter.instantiateByKey(properties, errorHandlerPrefix, ErrorHandler.class, null);
                if (eh != null) {
                    appender.setErrorHandler(eh);
                    LogLog.debug("Parsing errorhandler options for \"" + appenderName + "\".");
                    this.parseErrorHandler(eh, errorHandlerPrefix, properties, this.repository);
                    final Properties edited = new Properties();
                    final String[] keys = { errorHandlerPrefix + "." + "root-ref", errorHandlerPrefix + "." + "logger-ref", errorHandlerPrefix + "." + "appender-ref" };
                    for (final Object element : properties.entrySet()) {
                        Map.Entry entry;
                        int i;
                        for (entry = (Map.Entry)element, i = 0; i < keys.length && !keys[i].equals(entry.getKey()); ++i) {}
                        if (i == keys.length) {
                            edited.put(entry.getKey(), entry.getValue());
                        }
                    }
                    PropertySetter.setProperties(eh, edited, errorHandlerPrefix + ".");
                    LogLog.debug("End of errorhandler parsing for \"" + appenderName + "\".");
                }
            }
            PropertySetter.setProperties(appender, properties, prefix + ".");
            LogLog.debug("Parsed \"" + appenderName + "\" options.");
        }
        this.parseAppenderFilters(properties, appenderName, appender);
        this.registryPut(appender);
        return appender;
    }
    
    void parseAppenderFilters(final Properties properties, final String appenderName, final Appender appender) {
        final String filterPrefix = "log4j.appender." + appenderName + ".filter.";
        final int fIdx = filterPrefix.length();
        final Hashtable filters = new Hashtable();
        final Enumeration e = properties.keys();
        String name = "";
        while (e.hasMoreElements()) {
            final String key = e.nextElement();
            if (key.startsWith(filterPrefix)) {
                final int dotIdx = key.indexOf(46, fIdx);
                String filterKey = key;
                if (dotIdx != -1) {
                    filterKey = key.substring(0, dotIdx);
                    name = key.substring(dotIdx + 1);
                }
                Vector filterOpts = filters.get(filterKey);
                if (filterOpts == null) {
                    filterOpts = new Vector();
                    filters.put(filterKey, filterOpts);
                }
                if (dotIdx == -1) {
                    continue;
                }
                final String value = OptionConverter.findAndSubst(key, properties);
                filterOpts.add(new NameValue(name, value));
            }
        }
        final Enumeration g = new SortedKeyEnumeration(filters);
        Filter head = null;
        while (g.hasMoreElements()) {
            final String key2 = g.nextElement();
            final String clazz = properties.getProperty(key2);
            if (clazz != null) {
                LogLog.debug("Filter key: [" + key2 + "] class: [" + properties.getProperty(key2) + "] props: " + filters.get(key2));
                final Filter filter = (Filter)OptionConverter.instantiateByClassName(clazz, Filter.class, null);
                if (filter == null) {
                    continue;
                }
                final PropertySetter propSetter = new PropertySetter(filter);
                final Vector v = filters.get(key2);
                final Enumeration filterProps = v.elements();
                while (filterProps.hasMoreElements()) {
                    final NameValue kv = filterProps.nextElement();
                    propSetter.setProperty(kv.key, kv.value);
                }
                propSetter.activate();
                LogLog.debug("Adding filter of type [" + filter.getClass() + "] to appender named [" + appender.getName() + "].");
                head = FilterAdapter.addFilter(head, filter);
            }
            else {
                LogLog.warn("Missing class definition for filter: [" + key2 + "]");
            }
        }
        appender.addFilter(head);
    }
    
    void parseCategory(final Properties properties, final Logger logger, final String optionKey, final String loggerName, final String value) {
        LogLog.debug("Parsing for [" + loggerName + "] with value=[" + value + "].");
        final StringTokenizer st = new StringTokenizer(value, ",");
        if (!value.startsWith(",") && !value.equals("")) {
            if (!st.hasMoreTokens()) {
                return;
            }
            final String levelStr = st.nextToken();
            LogLog.debug("Level token is [" + levelStr + "].");
            if ("inherited".equalsIgnoreCase(levelStr) || "null".equalsIgnoreCase(levelStr)) {
                if (loggerName.equals("root")) {
                    LogLog.warn("The root logger cannot be set to null.");
                }
                else {
                    logger.setLevel((Level)null);
                }
            }
            else {
                logger.setLevel(OptionConverter.toLevel(levelStr, Log4j1Configuration.DEFAULT_LEVEL));
            }
            LogLog.debug("Category " + loggerName + " set to " + logger.getLevel());
        }
        logger.removeAllAppenders();
        while (st.hasMoreTokens()) {
            final String appenderName = st.nextToken().trim();
            if (appenderName != null) {
                if (appenderName.equals(",")) {
                    continue;
                }
                LogLog.debug("Parsing appender named \"" + appenderName + "\".");
                final Appender appender = this.parseAppender(properties, appenderName);
                if (appender == null) {
                    continue;
                }
                logger.addAppender(appender);
            }
        }
    }
    
    protected void parseCatsAndRenderers(final Properties properties, final LoggerRepository loggerRepository) {
        final Enumeration enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            final String key = enumeration.nextElement();
            if (key.startsWith("log4j.category.") || key.startsWith("log4j.logger.")) {
                String loggerName = null;
                if (key.startsWith("log4j.category.")) {
                    loggerName = key.substring("log4j.category.".length());
                }
                else if (key.startsWith("log4j.logger.")) {
                    loggerName = key.substring("log4j.logger.".length());
                }
                final String value = OptionConverter.findAndSubst(key, properties);
                final Logger logger = loggerRepository.getLogger(loggerName, this.loggerFactory);
                synchronized (logger) {
                    this.parseCategory(properties, logger, key, loggerName, value);
                    this.parseAdditivityForLogger(properties, logger, loggerName);
                }
            }
            else if (key.startsWith("log4j.renderer.")) {
                final String renderedClass = key.substring("log4j.renderer.".length());
                final String renderingClass = OptionConverter.findAndSubst(key, properties);
                if (!(loggerRepository instanceof RendererSupport)) {
                    continue;
                }
                RendererMap.addRenderer((RendererSupport)loggerRepository, renderedClass, renderingClass);
            }
            else {
                if (!key.equals("log4j.throwableRenderer") || !(loggerRepository instanceof ThrowableRendererSupport)) {
                    continue;
                }
                final ThrowableRenderer tr = (ThrowableRenderer)OptionConverter.instantiateByKey(properties, "log4j.throwableRenderer", ThrowableRenderer.class, null);
                if (tr == null) {
                    LogLog.error("Could not instantiate throwableRenderer.");
                }
                else {
                    final PropertySetter setter = new PropertySetter(tr);
                    setter.setProperties(properties, "log4j.throwableRenderer.");
                    ((ThrowableRendererSupport)loggerRepository).setThrowableRenderer(tr);
                }
            }
        }
    }
    
    private void parseErrorHandler(final ErrorHandler errorHandler, final String errorHandlerPrefix, final Properties props, final LoggerRepository loggerRepository) {
        if (errorHandler != null && loggerRepository != null) {
            final boolean rootRef = OptionConverter.toBoolean(OptionConverter.findAndSubst(errorHandlerPrefix + "root-ref", props), false);
            if (rootRef) {
                errorHandler.setLogger(loggerRepository.getRootLogger());
            }
            final String loggerName = OptionConverter.findAndSubst(errorHandlerPrefix + "logger-ref", props);
            if (loggerName != null) {
                final Logger logger = (this.loggerFactory == null) ? loggerRepository.getLogger(loggerName) : loggerRepository.getLogger(loggerName, this.loggerFactory);
                errorHandler.setLogger(logger);
            }
            final String appenderName = OptionConverter.findAndSubst(errorHandlerPrefix + "appender-ref", props);
            if (appenderName != null) {
                final Appender backup = this.parseAppender(props, appenderName);
                if (backup != null) {
                    errorHandler.setBackupAppender(backup);
                }
            }
        }
    }
    
    Appender registryGet(final String name) {
        return this.registry.get(name);
    }
    
    void registryPut(final Appender appender) {
        this.registry.put(appender.getName(), appender);
    }
    
    static class NameValue
    {
        String key;
        String value;
        
        public NameValue(final String key, final String value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }
    
    static class PropertyWatchdog extends FileWatchdog
    {
        private final ClassLoader classLoader;
        
        PropertyWatchdog(final String fileName, final ClassLoader classLoader) {
            super(fileName);
            this.classLoader = classLoader;
        }
        
        public void doOnChange() {
            new PropertyConfigurator().doConfigure(this.filename, LogManager.getLoggerRepository(), this.classLoader);
        }
    }
    
    class SortedKeyEnumeration implements Enumeration
    {
        private final Enumeration e;
        
        public SortedKeyEnumeration(final Hashtable ht) {
            final Enumeration f = ht.keys();
            final Vector keys = new Vector(ht.size());
            int last = 0;
            while (f.hasMoreElements()) {
                final String key = f.nextElement();
                int i;
                for (i = 0; i < last; ++i) {
                    final String s = keys.get(i);
                    if (key.compareTo(s) <= 0) {
                        break;
                    }
                }
                keys.add(i, key);
                ++last;
            }
            this.e = keys.elements();
        }
        
        @Override
        public boolean hasMoreElements() {
            return this.e.hasMoreElements();
        }
        
        @Override
        public Object nextElement() {
            return this.e.nextElement();
        }
    }
}
