// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j;

import org.apache.logging.log4j.spi.AbstractLoggerAdapter;
import org.apache.log4j.legacy.core.ContextUtil;
import org.apache.log4j.helpers.OptionConverter;
import java.util.ResourceBundle;
import org.apache.logging.log4j.util.StackLocatorUtil;
import java.util.Enumeration;
import org.apache.log4j.or.ObjectRenderer;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.HierarchyEventListener;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.spi.ThrowableRenderer;
import org.apache.log4j.or.RendererMap;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.log4j.spi.LoggerFactory;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.spi.LoggerContext;
import java.util.WeakHashMap;
import org.apache.log4j.spi.ThrowableRendererSupport;
import org.apache.log4j.spi.RendererSupport;
import org.apache.log4j.spi.LoggerRepository;

public class Hierarchy implements LoggerRepository, RendererSupport, ThrowableRendererSupport
{
    private static final PrivateLoggerAdapter LOGGER_ADAPTER;
    private static final WeakHashMap<LoggerContext, ConcurrentMap<String, Logger>> CONTEXT_MAP;
    private final LoggerFactory defaultFactory;
    private final Vector listeners;
    Hashtable ht;
    Logger root;
    RendererMap rendererMap;
    int thresholdInt;
    Level threshold;
    boolean emittedNoAppenderWarning;
    boolean emittedNoResourceBundleWarning;
    private ThrowableRenderer throwableRenderer;
    
    static LoggerContext getContext() {
        return PrivateLogManager.getContext();
    }
    
    private Logger getInstance(final LoggerContext context, final String name) {
        return this.getInstance(context, name, Hierarchy.LOGGER_ADAPTER);
    }
    
    private Logger getInstance(final LoggerContext context, final String name, final LoggerFactory factory) {
        return getLoggersMap(context).computeIfAbsent(name, k -> {
            final Logger logger = factory.makeNewLoggerInstance(name);
            logger.setHierarchy(this);
            return logger;
        });
    }
    
    private Logger getInstance(final LoggerContext context, final String name, final PrivateLoggerAdapter factory) {
        return getLoggersMap(context).computeIfAbsent(name, k -> {
            final Logger logger = factory.newLogger(name, context);
            logger.setHierarchy(this);
            return logger;
        });
    }
    
    static ConcurrentMap<String, Logger> getLoggersMap(final LoggerContext context) {
        synchronized (Hierarchy.CONTEXT_MAP) {
            return Hierarchy.CONTEXT_MAP.computeIfAbsent(context, k -> new ConcurrentHashMap());
        }
    }
    
    public Hierarchy(final Logger root) {
        this.ht = new Hashtable();
        this.listeners = new Vector(1);
        this.root = root;
        this.setThreshold(Level.ALL);
        this.root.setHierarchy(this);
        this.rendererMap = new RendererMap();
        this.defaultFactory = new DefaultCategoryFactory();
    }
    
    @Override
    public void addHierarchyEventListener(final HierarchyEventListener listener) {
        if (this.listeners.contains(listener)) {
            LogLog.warn("Ignoring attempt to add an existent listener.");
        }
        else {
            this.listeners.addElement(listener);
        }
    }
    
    public void addRenderer(final Class classToRender, final ObjectRenderer or) {
        this.rendererMap.put(classToRender, or);
    }
    
    public void clear() {
        this.ht.clear();
        getLoggersMap(getContext()).clear();
    }
    
    @Override
    public void emitNoAppenderWarning(final Category cat) {
        if (!this.emittedNoAppenderWarning) {
            LogLog.warn("No appenders could be found for logger (" + cat.getName() + ").");
            LogLog.warn("Please initialize the log4j system properly.");
            LogLog.warn("See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.");
            this.emittedNoAppenderWarning = true;
        }
    }
    
    @Override
    public Logger exists(final String name) {
        return this.exists(name, getContext());
    }
    
    Logger exists(final String name, final ClassLoader classLoader) {
        return this.exists(name, this.getContext(classLoader));
    }
    
    Logger exists(final String name, final LoggerContext loggerContext) {
        if (!loggerContext.hasLogger(name)) {
            return null;
        }
        return Logger.getLogger(name);
    }
    
    @Override
    public void fireAddAppenderEvent(final Category logger, final Appender appender) {
        if (this.listeners != null) {
            for (int size = this.listeners.size(), i = 0; i < size; ++i) {
                final HierarchyEventListener listener = this.listeners.elementAt(i);
                listener.addAppenderEvent(logger, appender);
            }
        }
    }
    
    void fireRemoveAppenderEvent(final Category logger, final Appender appender) {
        if (this.listeners != null) {
            for (int size = this.listeners.size(), i = 0; i < size; ++i) {
                final HierarchyEventListener listener = this.listeners.elementAt(i);
                listener.removeAppenderEvent(logger, appender);
            }
        }
    }
    
    LoggerContext getContext(final ClassLoader classLoader) {
        return LogManager.getContext(classLoader);
    }
    
    @Deprecated
    @Override
    public Enumeration getCurrentCategories() {
        return this.getCurrentLoggers();
    }
    
    @Override
    public Enumeration getCurrentLoggers() {
        return LogManager.getCurrentLoggers(StackLocatorUtil.getCallerClassLoader(2));
    }
    
    @Override
    public Logger getLogger(final String name) {
        return this.getInstance(getContext(), name);
    }
    
    Logger getLogger(final String name, final ClassLoader classLoader) {
        return this.getInstance(this.getContext(classLoader), name);
    }
    
    @Override
    public Logger getLogger(final String name, final LoggerFactory factory) {
        return this.getInstance(getContext(), name, factory);
    }
    
    Logger getLogger(final String name, final LoggerFactory factory, final ClassLoader classLoader) {
        return this.getInstance(this.getContext(classLoader), name, factory);
    }
    
    @Override
    public RendererMap getRendererMap() {
        return this.rendererMap;
    }
    
    @Override
    public Logger getRootLogger() {
        return this.getInstance(getContext(), "");
    }
    
    Logger getRootLogger(final ClassLoader classLoader) {
        return this.getInstance(this.getContext(classLoader), "");
    }
    
    @Override
    public Level getThreshold() {
        return this.threshold;
    }
    
    @Override
    public ThrowableRenderer getThrowableRenderer() {
        return this.throwableRenderer;
    }
    
    @Override
    public boolean isDisabled(final int level) {
        return this.thresholdInt > level;
    }
    
    @Deprecated
    public void overrideAsNeeded(final String override) {
        LogLog.warn("The Hiearchy.overrideAsNeeded method has been deprecated.");
    }
    
    @Override
    public void resetConfiguration() {
        this.resetConfiguration(getContext());
    }
    
    void resetConfiguration(final ClassLoader classLoader) {
        this.resetConfiguration(this.getContext(classLoader));
    }
    
    void resetConfiguration(final LoggerContext loggerContext) {
        getLoggersMap(loggerContext).clear();
        this.getRootLogger().setLevel(Level.DEBUG);
        this.root.setResourceBundle(null);
        this.setThreshold(Level.ALL);
        synchronized (this.ht) {
            this.shutdown();
            final Enumeration cats = this.getCurrentLoggers();
            while (cats.hasMoreElements()) {
                final Logger c = cats.nextElement();
                c.setLevel((Level)null);
                c.setAdditivity(true);
                c.setResourceBundle(null);
            }
        }
        this.rendererMap.clear();
        this.throwableRenderer = null;
    }
    
    @Deprecated
    public void setDisableOverride(final String override) {
        LogLog.warn("The Hiearchy.setDisableOverride method has been deprecated.");
    }
    
    @Override
    public void setRenderer(final Class renderedClass, final ObjectRenderer renderer) {
        this.rendererMap.put(renderedClass, renderer);
    }
    
    @Override
    public void setThreshold(final Level level) {
        if (level != null) {
            this.thresholdInt = level.level;
            this.threshold = level;
        }
    }
    
    @Override
    public void setThreshold(final String levelStr) {
        final Level level = OptionConverter.toLevel(levelStr, null);
        if (level != null) {
            this.setThreshold(level);
        }
        else {
            LogLog.warn("Could not convert [" + levelStr + "] to Level.");
        }
    }
    
    @Override
    public void setThrowableRenderer(final ThrowableRenderer throwableRenderer) {
        this.throwableRenderer = throwableRenderer;
    }
    
    @Override
    public void shutdown() {
        this.shutdown(getContext());
    }
    
    public void shutdown(final ClassLoader classLoader) {
        this.shutdown(org.apache.logging.log4j.LogManager.getContext(classLoader, false));
    }
    
    void shutdown(final LoggerContext context) {
        getLoggersMap(context).clear();
        if (LogManager.isLog4jCorePresent()) {
            ContextUtil.shutdown(context);
        }
    }
    
    static {
        LOGGER_ADAPTER = new PrivateLoggerAdapter();
        CONTEXT_MAP = new WeakHashMap<LoggerContext, ConcurrentMap<String, Logger>>();
    }
    
    private static class PrivateLoggerAdapter extends AbstractLoggerAdapter<Logger>
    {
        @Override
        protected LoggerContext getContext() {
            return PrivateLogManager.getContext();
        }
        
        @Override
        protected Logger newLogger(final String name, final LoggerContext context) {
            return new Logger(context, name);
        }
    }
    
    private static class PrivateLogManager extends LogManager
    {
        private static final String FQCN;
        
        public static LoggerContext getContext() {
            return LogManager.getContext(PrivateLogManager.FQCN, false);
        }
        
        public static org.apache.logging.log4j.Logger getLogger(final String name) {
            return LogManager.getLogger(PrivateLogManager.FQCN, name);
        }
        
        static {
            FQCN = Hierarchy.class.getName();
        }
    }
}
