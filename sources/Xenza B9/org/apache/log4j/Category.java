// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j;

import java.util.Iterator;
import java.util.Vector;
import org.apache.logging.log4j.message.LocalizedMessage;
import java.util.concurrent.ConcurrentMap;
import org.apache.log4j.helpers.NullEnumeration;
import java.util.Collections;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import org.apache.log4j.bridge.AppenderWrapper;
import java.util.function.Predicate;
import java.util.Collection;
import org.apache.log4j.or.ObjectRenderer;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.message.ObjectMessage;
import org.apache.logging.log4j.message.MapMessage;
import java.util.Map;
import org.apache.logging.log4j.message.SimpleMessage;
import org.apache.logging.log4j.message.Message;
import org.apache.log4j.spi.HierarchyEventListener;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.log4j.bridge.LogEventWrapper;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.legacy.core.CategoryUtil;
import org.apache.log4j.bridge.AppenderAdapter;
import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.util.Strings;
import java.util.Enumeration;
import org.apache.logging.log4j.util.StackLocatorUtil;
import org.apache.log4j.helpers.AppenderAttachableImpl;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.logging.log4j.Logger;
import java.util.ResourceBundle;
import org.apache.log4j.or.RendererMap;
import org.apache.log4j.spi.AppenderAttachable;

public class Category implements AppenderAttachable
{
    private static final String FQCN;
    protected String name;
    protected boolean additive;
    protected volatile Level level;
    private RendererMap rendererMap;
    protected volatile Category parent;
    protected ResourceBundle bundle;
    private final Logger logger;
    protected LoggerRepository repository;
    AppenderAttachableImpl aai;
    
    @Deprecated
    public static org.apache.log4j.Logger exists(final String name) {
        return LogManager.exists(name, StackLocatorUtil.getCallerClassLoader(2));
    }
    
    @Deprecated
    public static Enumeration getCurrentCategories() {
        return LogManager.getCurrentLoggers(StackLocatorUtil.getCallerClassLoader(2));
    }
    
    @Deprecated
    public static LoggerRepository getDefaultHierarchy() {
        return LogManager.getLoggerRepository();
    }
    
    public static Category getInstance(final Class clazz) {
        return LogManager.getLogger(clazz.getName(), StackLocatorUtil.getCallerClassLoader(2));
    }
    
    public static Category getInstance(final String name) {
        return LogManager.getLogger(name, StackLocatorUtil.getCallerClassLoader(2));
    }
    
    public static Category getRoot() {
        return LogManager.getRootLogger(StackLocatorUtil.getCallerClassLoader(2));
    }
    
    private static String getSubName(final String name) {
        if (Strings.isEmpty(name)) {
            return null;
        }
        final int i = name.lastIndexOf(46);
        return (i > 0) ? name.substring(0, i) : "";
    }
    
    public static void shutdown() {
        LogManager.shutdown(StackLocatorUtil.getCallerClassLoader(2));
    }
    
    protected Category(final LoggerContext context, final String name) {
        this.additive = true;
        this.name = name;
        this.logger = context.getLogger(name);
    }
    
    Category(final Logger logger) {
        this.additive = true;
        this.logger = logger;
    }
    
    protected Category(final String name) {
        this(Hierarchy.getContext(), name);
    }
    
    @Override
    public void addAppender(final Appender appender) {
        if (appender != null) {
            if (LogManager.isLog4jCorePresent()) {
                CategoryUtil.addAppender(this.logger, AppenderAdapter.adapt(appender));
            }
            else {
                synchronized (this) {
                    if (this.aai == null) {
                        this.aai = new AppenderAttachableImpl();
                    }
                    this.aai.addAppender(appender);
                }
            }
            this.repository.fireAddAppenderEvent(this, appender);
        }
    }
    
    public void assertLog(final boolean assertion, final String msg) {
        if (!assertion) {
            this.error(msg);
        }
    }
    
    public void callAppenders(final LoggingEvent event) {
        if (LogManager.isLog4jCorePresent()) {
            CategoryUtil.log(this.logger, new LogEventWrapper(event));
            return;
        }
        int writes = 0;
        for (Category c = this; c != null; c = c.parent) {
            synchronized (c) {
                if (c.aai != null) {
                    writes += c.aai.appendLoopOnAppenders(event);
                }
                if (!c.additive) {
                    break;
                }
            }
        }
        if (writes == 0) {
            this.repository.emitNoAppenderWarning(this);
        }
    }
    
    synchronized void closeNestedAppenders() {
        final Enumeration enumeration = this.getAllAppenders();
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                final Appender a = enumeration.nextElement();
                if (a instanceof AppenderAttachable) {
                    a.close();
                }
            }
        }
    }
    
    public void debug(final Object message) {
        this.maybeLog(Category.FQCN, org.apache.logging.log4j.Level.DEBUG, message, null);
    }
    
    public void debug(final Object message, final Throwable t) {
        this.maybeLog(Category.FQCN, org.apache.logging.log4j.Level.DEBUG, message, t);
    }
    
    public void error(final Object message) {
        this.maybeLog(Category.FQCN, org.apache.logging.log4j.Level.ERROR, message, null);
    }
    
    public void error(final Object message, final Throwable t) {
        this.maybeLog(Category.FQCN, org.apache.logging.log4j.Level.ERROR, message, t);
    }
    
    public void fatal(final Object message) {
        this.maybeLog(Category.FQCN, org.apache.logging.log4j.Level.FATAL, message, null);
    }
    
    public void fatal(final Object message, final Throwable t) {
        this.maybeLog(Category.FQCN, org.apache.logging.log4j.Level.FATAL, message, t);
    }
    
    private void fireRemoveAppenderEvent(final Appender appender) {
        if (appender != null) {
            if (this.repository instanceof Hierarchy) {
                ((Hierarchy)this.repository).fireRemoveAppenderEvent(this, appender);
            }
            else if (this.repository instanceof HierarchyEventListener) {
                ((HierarchyEventListener)this.repository).removeAppenderEvent(this, appender);
            }
        }
    }
    
    private static Message createMessage(final Object message) {
        if (message instanceof String) {
            return new SimpleMessage((String)message);
        }
        if (message instanceof CharSequence) {
            return new SimpleMessage((CharSequence)message);
        }
        if (message instanceof Map) {
            return new MapMessage<Object, Object>((Map<String, ?>)message);
        }
        if (message instanceof Message) {
            return (Message)message;
        }
        return new ObjectMessage(message);
    }
    
    public void forcedLog(final String fqcn, final Priority level, final Object message, final Throwable t) {
        final org.apache.logging.log4j.Level lvl = level.getVersion2Level();
        final Message msg = createMessage(message);
        if (this.logger instanceof ExtendedLogger) {
            ((ExtendedLogger)this.logger).logMessage(fqcn, lvl, null, msg, t);
        }
        else {
            this.logger.log(lvl, msg, t);
        }
    }
    
    private <T> ObjectRenderer get(final Class<T> clazz) {
        ObjectRenderer renderer = null;
        for (Class<? super T> c = clazz; c != null; c = c.getSuperclass()) {
            renderer = this.rendererMap.get(c);
            if (renderer != null) {
                return renderer;
            }
            renderer = this.searchInterfaces(c);
            if (renderer != null) {
                return renderer;
            }
        }
        return null;
    }
    
    public boolean getAdditivity() {
        return LogManager.isLog4jCorePresent() && CategoryUtil.isAdditive(this.logger);
    }
    
    @Override
    public Enumeration getAllAppenders() {
        if (LogManager.isLog4jCorePresent()) {
            final Collection<org.apache.logging.log4j.core.Appender> appenders = CategoryUtil.getAppenders(this.logger).values();
            return Collections.enumeration((Collection<Object>)appenders.stream().filter(AppenderAdapter.Adapter.class::isInstance).map((Function<? super org.apache.logging.log4j.core.Appender, ?>)AppenderWrapper::adapt).collect((Collector<? super Object, ?, Collection<T>>)Collectors.toSet()));
        }
        return (this.aai == null) ? NullEnumeration.getInstance() : this.aai.getAllAppenders();
    }
    
    @Override
    public Appender getAppender(final String name) {
        if (LogManager.isLog4jCorePresent()) {
            return AppenderWrapper.adapt(CategoryUtil.getAppenders(this.logger).get(name));
        }
        return (this.aai != null) ? this.aai.getAppender(name) : null;
    }
    
    public Priority getChainedPriority() {
        return this.getEffectiveLevel();
    }
    
    public Level getEffectiveLevel() {
        switch (this.logger.getLevel().getStandardLevel()) {
            case ALL: {
                return Level.ALL;
            }
            case TRACE: {
                return Level.TRACE;
            }
            case DEBUG: {
                return Level.DEBUG;
            }
            case INFO: {
                return Level.INFO;
            }
            case WARN: {
                return Level.WARN;
            }
            case ERROR: {
                return Level.ERROR;
            }
            case FATAL: {
                return Level.FATAL;
            }
            default: {
                return Level.OFF;
            }
        }
    }
    
    @Deprecated
    public LoggerRepository getHierarchy() {
        return this.repository;
    }
    
    public final Level getLevel() {
        return this.getEffectiveLevel();
    }
    
    private String getLevelStr(final Priority priority) {
        return (priority == null) ? null : priority.levelStr;
    }
    
    Logger getLogger() {
        return this.logger;
    }
    
    public LoggerRepository getLoggerRepository() {
        return this.repository;
    }
    
    public final String getName() {
        return this.logger.getName();
    }
    
    public final Category getParent() {
        if (!LogManager.isLog4jCorePresent()) {
            return null;
        }
        final Logger parent = CategoryUtil.getParent(this.logger);
        final LoggerContext loggerContext = CategoryUtil.getLoggerContext(this.logger);
        if (parent == null || loggerContext == null) {
            return null;
        }
        final ConcurrentMap<String, org.apache.log4j.Logger> loggers = Hierarchy.getLoggersMap(loggerContext);
        Category parentLogger = loggers.get(parent.getName());
        if (parentLogger == null) {
            parentLogger = new Category(parent);
            parentLogger.setHierarchy(this.getLoggerRepository());
        }
        return parentLogger;
    }
    
    public final Level getPriority() {
        return this.getEffectiveLevel();
    }
    
    public ResourceBundle getResourceBundle() {
        if (this.bundle != null) {
            return this.bundle;
        }
        String name = this.logger.getName();
        if (LogManager.isLog4jCorePresent()) {
            final LoggerContext ctx = CategoryUtil.getLoggerContext(this.logger);
            if (ctx != null) {
                final ConcurrentMap<String, org.apache.log4j.Logger> loggers = Hierarchy.getLoggersMap(ctx);
                while ((name = getSubName(name)) != null) {
                    final org.apache.log4j.Logger subLogger = loggers.get(name);
                    if (subLogger != null) {
                        final ResourceBundle rb = subLogger.bundle;
                        if (rb != null) {
                            return rb;
                        }
                        continue;
                    }
                }
            }
        }
        return null;
    }
    
    public void info(final Object message) {
        this.maybeLog(Category.FQCN, org.apache.logging.log4j.Level.INFO, message, null);
    }
    
    public void info(final Object message, final Throwable t) {
        this.maybeLog(Category.FQCN, org.apache.logging.log4j.Level.INFO, message, t);
    }
    
    @Override
    public boolean isAttached(final Appender appender) {
        return this.aai != null && this.aai.isAttached(appender);
    }
    
    public boolean isDebugEnabled() {
        return this.logger.isDebugEnabled();
    }
    
    private boolean isEnabledFor(final org.apache.logging.log4j.Level level) {
        return this.logger.isEnabled(level);
    }
    
    public boolean isEnabledFor(final Priority level) {
        return this.isEnabledFor(level.getVersion2Level());
    }
    
    public boolean isErrorEnabled() {
        return this.logger.isErrorEnabled();
    }
    
    public boolean isFatalEnabled() {
        return this.logger.isFatalEnabled();
    }
    
    public boolean isInfoEnabled() {
        return this.logger.isInfoEnabled();
    }
    
    public boolean isWarnEnabled() {
        return this.logger.isWarnEnabled();
    }
    
    public void l7dlog(final Priority priority, final String key, final Object[] params, final Throwable t) {
        if (this.isEnabledFor(priority)) {
            final Message msg = new LocalizedMessage(this.bundle, key, params);
            this.forcedLog(Category.FQCN, priority, msg, t);
        }
    }
    
    public void l7dlog(final Priority priority, final String key, final Throwable t) {
        if (this.isEnabledFor(priority)) {
            final Message msg = new LocalizedMessage(this.bundle, key, null);
            this.forcedLog(Category.FQCN, priority, msg, t);
        }
    }
    
    public void log(final Priority priority, final Object message) {
        if (this.isEnabledFor(priority)) {
            this.forcedLog(Category.FQCN, priority, message, null);
        }
    }
    
    public void log(final Priority priority, final Object message, final Throwable t) {
        if (this.isEnabledFor(priority)) {
            this.forcedLog(Category.FQCN, priority, message, t);
        }
    }
    
    public void log(final String fqcn, final Priority priority, final Object message, final Throwable t) {
        if (this.isEnabledFor(priority)) {
            this.forcedLog(fqcn, priority, message, t);
        }
    }
    
    void maybeLog(final String fqcn, final org.apache.logging.log4j.Level level, final Object message, final Throwable throwable) {
        if (this.logger.isEnabled(level)) {
            final Message msg = createMessage(message);
            if (this.logger instanceof ExtendedLogger) {
                ((ExtendedLogger)this.logger).logMessage(fqcn, level, null, msg, throwable);
            }
            else {
                this.logger.log(level, msg, throwable);
            }
        }
    }
    
    @Override
    public void removeAllAppenders() {
        if (this.aai != null) {
            final Vector appenders = new Vector();
            final Enumeration iter = this.aai.getAllAppenders();
            while (iter != null && iter.hasMoreElements()) {
                appenders.add(iter.nextElement());
            }
            this.aai.removeAllAppenders();
            for (final Object appender : appenders) {
                this.fireRemoveAppenderEvent((Appender)appender);
            }
            this.aai = null;
        }
    }
    
    @Override
    public void removeAppender(final Appender appender) {
        if (appender == null || this.aai == null) {
            return;
        }
        final boolean wasAttached = this.aai.isAttached(appender);
        this.aai.removeAppender(appender);
        if (wasAttached) {
            this.fireRemoveAppenderEvent(appender);
        }
    }
    
    @Override
    public void removeAppender(final String name) {
        if (name == null || this.aai == null) {
            return;
        }
        final Appender appender = this.aai.getAppender(name);
        this.aai.removeAppender(name);
        if (appender != null) {
            this.fireRemoveAppenderEvent(appender);
        }
    }
    
    ObjectRenderer searchInterfaces(final Class<?> c) {
        ObjectRenderer renderer = this.rendererMap.get(c);
        if (renderer != null) {
            return renderer;
        }
        final Class<?>[] interfaces;
        final Class<?>[] ia = interfaces = c.getInterfaces();
        for (final Class<?> clazz : interfaces) {
            renderer = this.searchInterfaces(clazz);
            if (renderer != null) {
                return renderer;
            }
        }
        return null;
    }
    
    public void setAdditivity(final boolean additivity) {
        if (LogManager.isLog4jCorePresent()) {
            CategoryUtil.setAdditivity(this.logger, additivity);
        }
    }
    
    final void setHierarchy(final LoggerRepository repository) {
        this.repository = repository;
    }
    
    public void setLevel(final Level level) {
        this.setLevel((level != null) ? level.getVersion2Level() : null);
    }
    
    private void setLevel(final org.apache.logging.log4j.Level level) {
        if (LogManager.isLog4jCorePresent()) {
            CategoryUtil.setLevel(this.logger, level);
        }
    }
    
    public void setPriority(final Priority priority) {
        this.setLevel((priority != null) ? priority.getVersion2Level() : null);
    }
    
    public void setResourceBundle(final ResourceBundle bundle) {
        this.bundle = bundle;
    }
    
    public void warn(final Object message) {
        this.maybeLog(Category.FQCN, org.apache.logging.log4j.Level.WARN, message, null);
    }
    
    public void warn(final Object message, final Throwable t) {
        this.maybeLog(Category.FQCN, org.apache.logging.log4j.Level.WARN, message, t);
    }
    
    static {
        FQCN = Category.class.getName();
    }
}
