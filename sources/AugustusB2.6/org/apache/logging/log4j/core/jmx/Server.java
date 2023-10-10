// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jmx;

import javax.management.JMException;
import org.apache.logging.log4j.core.appender.AsyncAppender;
import org.apache.logging.log4j.core.Appender;
import java.util.Map;
import org.apache.logging.log4j.core.async.AsyncLoggerConfig;
import org.apache.logging.log4j.core.config.LoggerConfig;
import java.util.Set;
import javax.management.InstanceNotFoundException;
import javax.management.QueryExp;
import javax.management.ObjectName;
import javax.management.NotCompliantMBeanException;
import javax.management.MBeanRegistrationException;
import javax.management.InstanceAlreadyExistsException;
import org.apache.logging.log4j.spi.LoggerContextFactory;
import org.apache.logging.log4j.core.impl.Log4jContextFactory;
import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.core.selector.ContextSelector;
import org.apache.logging.log4j.core.async.AsyncLoggerContext;
import org.apache.logging.log4j.core.LoggerContext;
import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.Executors;
import org.apache.logging.log4j.core.util.Log4jThreadFactory;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.core.util.Constants;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executor;
import org.apache.logging.log4j.status.StatusLogger;

public final class Server
{
    private static final String CONTEXT_NAME_ALL = "*";
    public static final String DOMAIN = "org.apache.logging.log4j2";
    private static final String PROPERTY_DISABLE_JMX = "log4j2.disable.jmx";
    private static final String PROPERTY_ASYNC_NOTIF = "log4j2.jmx.notify.async";
    private static final String THREAD_NAME_PREFIX = "jmx.notif";
    private static final StatusLogger LOGGER;
    static final Executor executor;
    
    private Server() {
    }
    
    private static ExecutorService createExecutor() {
        final boolean defaultAsync = !Constants.IS_WEB_APP;
        final boolean async = PropertiesUtil.getProperties().getBooleanProperty("log4j2.jmx.notify.async", defaultAsync);
        return async ? Executors.newFixedThreadPool(1, Log4jThreadFactory.createDaemonThreadFactory("jmx.notif")) : null;
    }
    
    public static String escape(final String name) {
        final StringBuilder sb = new StringBuilder(name.length() * 2);
        boolean needsQuotes = false;
        for (int i = 0; i < name.length(); ++i) {
            final char c = name.charAt(i);
            switch (c) {
                case '\"':
                case '*':
                case '?':
                case '\\': {
                    sb.append('\\');
                    needsQuotes = true;
                    break;
                }
                case ',':
                case ':':
                case '=': {
                    needsQuotes = true;
                    break;
                }
                case '\r': {
                    continue;
                }
                case '\n': {
                    sb.append("\\n");
                    needsQuotes = true;
                    continue;
                }
            }
            sb.append(c);
        }
        if (needsQuotes) {
            sb.insert(0, '\"');
            sb.append('\"');
        }
        return sb.toString();
    }
    
    private static boolean isJmxDisabled() {
        return PropertiesUtil.getProperties().getBooleanProperty("log4j2.disable.jmx");
    }
    
    public static void reregisterMBeansAfterReconfigure() {
        if (isJmxDisabled()) {
            Server.LOGGER.debug("JMX disabled for Log4j2. Not registering MBeans.");
            return;
        }
        final MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        reregisterMBeansAfterReconfigure(mbs);
    }
    
    public static void reregisterMBeansAfterReconfigure(final MBeanServer mbs) {
        if (isJmxDisabled()) {
            Server.LOGGER.debug("JMX disabled for Log4j2. Not registering MBeans.");
            return;
        }
        try {
            final ContextSelector selector = getContextSelector();
            if (selector == null) {
                Server.LOGGER.debug("Could not register MBeans: no ContextSelector found.");
                return;
            }
            Server.LOGGER.trace("Reregistering MBeans after reconfigure. Selector={}", selector);
            final List<LoggerContext> contexts = selector.getLoggerContexts();
            int i = 0;
            for (final LoggerContext ctx : contexts) {
                Server.LOGGER.trace("Reregistering context ({}/{}): '{}' {}", (Object)(++i), contexts.size(), ctx.getName(), ctx);
                unregisterLoggerContext(ctx.getName(), mbs);
                final LoggerContextAdmin mbean = new LoggerContextAdmin(ctx, Server.executor);
                register(mbs, mbean, mbean.getObjectName());
                if (ctx instanceof AsyncLoggerContext) {
                    final RingBufferAdmin rbmbean = ((AsyncLoggerContext)ctx).createRingBufferAdmin();
                    if (rbmbean.getBufferSize() > 0L) {
                        register(mbs, rbmbean, rbmbean.getObjectName());
                    }
                }
                registerStatusLogger(ctx.getName(), mbs, Server.executor);
                registerContextSelector(ctx.getName(), selector, mbs, Server.executor);
                registerLoggerConfigs(ctx, mbs, Server.executor);
                registerAppenders(ctx, mbs, Server.executor);
            }
        }
        catch (Exception ex) {
            Server.LOGGER.error("Could not register mbeans", ex);
        }
    }
    
    public static void unregisterMBeans() {
        if (isJmxDisabled()) {
            Server.LOGGER.debug("JMX disabled for Log4j2. Not unregistering MBeans.");
            return;
        }
        unregisterMBeans(ManagementFactory.getPlatformMBeanServer());
    }
    
    public static void unregisterMBeans(final MBeanServer mbs) {
        if (mbs != null) {
            unregisterStatusLogger("*", mbs);
            unregisterContextSelector("*", mbs);
            unregisterContexts(mbs);
            unregisterLoggerConfigs("*", mbs);
            unregisterAsyncLoggerRingBufferAdmins("*", mbs);
            unregisterAsyncLoggerConfigRingBufferAdmins("*", mbs);
            unregisterAppenders("*", mbs);
            unregisterAsyncAppenders("*", mbs);
        }
    }
    
    private static ContextSelector getContextSelector() {
        final LoggerContextFactory factory = LogManager.getFactory();
        if (factory instanceof Log4jContextFactory) {
            final ContextSelector selector = ((Log4jContextFactory)factory).getSelector();
            return selector;
        }
        return null;
    }
    
    public static void unregisterLoggerContext(final String loggerContextName) {
        if (isJmxDisabled()) {
            Server.LOGGER.debug("JMX disabled for Log4j2. Not unregistering MBeans.");
            return;
        }
        final MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        unregisterLoggerContext(loggerContextName, mbs);
    }
    
    public static void unregisterLoggerContext(final String contextName, final MBeanServer mbs) {
        final String search = String.format("org.apache.logging.log4j2:type=%s", escape(contextName), "*");
        unregisterAllMatching(search, mbs);
        unregisterStatusLogger(contextName, mbs);
        unregisterContextSelector(contextName, mbs);
        unregisterLoggerConfigs(contextName, mbs);
        unregisterAppenders(contextName, mbs);
        unregisterAsyncAppenders(contextName, mbs);
        unregisterAsyncLoggerRingBufferAdmins(contextName, mbs);
        unregisterAsyncLoggerConfigRingBufferAdmins(contextName, mbs);
    }
    
    private static void registerStatusLogger(final String contextName, final MBeanServer mbs, final Executor executor) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
        final StatusLoggerAdmin mbean = new StatusLoggerAdmin(contextName, executor);
        register(mbs, mbean, mbean.getObjectName());
    }
    
    private static void registerContextSelector(final String contextName, final ContextSelector selector, final MBeanServer mbs, final Executor executor) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
        final ContextSelectorAdmin mbean = new ContextSelectorAdmin(contextName, selector);
        register(mbs, mbean, mbean.getObjectName());
    }
    
    private static void unregisterStatusLogger(final String contextName, final MBeanServer mbs) {
        final String search = String.format("org.apache.logging.log4j2:type=%s,component=StatusLogger", escape(contextName), "*");
        unregisterAllMatching(search, mbs);
    }
    
    private static void unregisterContextSelector(final String contextName, final MBeanServer mbs) {
        final String search = String.format("org.apache.logging.log4j2:type=%s,component=ContextSelector", escape(contextName), "*");
        unregisterAllMatching(search, mbs);
    }
    
    private static void unregisterLoggerConfigs(final String contextName, final MBeanServer mbs) {
        final String pattern = "org.apache.logging.log4j2:type=%s,component=Loggers,name=%s";
        final String search = String.format("org.apache.logging.log4j2:type=%s,component=Loggers,name=%s", escape(contextName), "*");
        unregisterAllMatching(search, mbs);
    }
    
    private static void unregisterContexts(final MBeanServer mbs) {
        final String pattern = "org.apache.logging.log4j2:type=%s";
        final String search = String.format("org.apache.logging.log4j2:type=%s", "*");
        unregisterAllMatching(search, mbs);
    }
    
    private static void unregisterAppenders(final String contextName, final MBeanServer mbs) {
        final String pattern = "org.apache.logging.log4j2:type=%s,component=Appenders,name=%s";
        final String search = String.format("org.apache.logging.log4j2:type=%s,component=Appenders,name=%s", escape(contextName), "*");
        unregisterAllMatching(search, mbs);
    }
    
    private static void unregisterAsyncAppenders(final String contextName, final MBeanServer mbs) {
        final String pattern = "org.apache.logging.log4j2:type=%s,component=AsyncAppenders,name=%s";
        final String search = String.format("org.apache.logging.log4j2:type=%s,component=AsyncAppenders,name=%s", escape(contextName), "*");
        unregisterAllMatching(search, mbs);
    }
    
    private static void unregisterAsyncLoggerRingBufferAdmins(final String contextName, final MBeanServer mbs) {
        final String pattern1 = "org.apache.logging.log4j2:type=%s,component=AsyncLoggerRingBuffer";
        final String search1 = String.format("org.apache.logging.log4j2:type=%s,component=AsyncLoggerRingBuffer", escape(contextName));
        unregisterAllMatching(search1, mbs);
    }
    
    private static void unregisterAsyncLoggerConfigRingBufferAdmins(final String contextName, final MBeanServer mbs) {
        final String pattern2 = "org.apache.logging.log4j2:type=%s,component=Loggers,name=%s,subtype=RingBuffer";
        final String search2 = String.format("org.apache.logging.log4j2:type=%s,component=Loggers,name=%s,subtype=RingBuffer", escape(contextName), "*");
        unregisterAllMatching(search2, mbs);
    }
    
    private static void unregisterAllMatching(final String search, final MBeanServer mbs) {
        try {
            final ObjectName pattern = new ObjectName(search);
            final Set<ObjectName> found = mbs.queryNames(pattern, null);
            if (found == null || found.isEmpty()) {
                Server.LOGGER.trace("Unregistering but no MBeans found matching '{}'", search);
            }
            else {
                Server.LOGGER.trace("Unregistering {} MBeans: {}", (Object)found.size(), found);
            }
            if (found != null) {
                for (final ObjectName objectName : found) {
                    mbs.unregisterMBean(objectName);
                }
            }
        }
        catch (InstanceNotFoundException ex) {
            Server.LOGGER.debug("Could not unregister MBeans for " + search + ". Ignoring " + ex);
        }
        catch (Exception ex2) {
            Server.LOGGER.error("Could not unregister MBeans for " + search, ex2);
        }
    }
    
    private static void registerLoggerConfigs(final LoggerContext ctx, final MBeanServer mbs, final Executor executor) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
        final Map<String, LoggerConfig> map = ctx.getConfiguration().getLoggers();
        for (final String name : map.keySet()) {
            final LoggerConfig cfg = map.get(name);
            final LoggerConfigAdmin mbean = new LoggerConfigAdmin(ctx, cfg);
            register(mbs, mbean, mbean.getObjectName());
            if (cfg instanceof AsyncLoggerConfig) {
                final AsyncLoggerConfig async = (AsyncLoggerConfig)cfg;
                final RingBufferAdmin rbmbean = async.createRingBufferAdmin(ctx.getName());
                register(mbs, rbmbean, rbmbean.getObjectName());
            }
        }
    }
    
    private static void registerAppenders(final LoggerContext ctx, final MBeanServer mbs, final Executor executor) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
        final Map<String, Appender> map = ctx.getConfiguration().getAppenders();
        for (final String name : map.keySet()) {
            final Appender appender = map.get(name);
            if (appender instanceof AsyncAppender) {
                final AsyncAppender async = (AsyncAppender)appender;
                final AsyncAppenderAdmin mbean = new AsyncAppenderAdmin(ctx.getName(), async);
                register(mbs, mbean, mbean.getObjectName());
            }
            else {
                final AppenderAdmin mbean2 = new AppenderAdmin(ctx.getName(), appender);
                register(mbs, mbean2, mbean2.getObjectName());
            }
        }
    }
    
    private static void register(final MBeanServer mbs, final Object mbean, final ObjectName objectName) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
        if (mbs.isRegistered(objectName)) {
            try {
                mbs.unregisterMBean(objectName);
            }
            catch (MBeanRegistrationException | InstanceNotFoundException ex3) {
                final JMException ex2;
                final JMException ex = ex2;
                Server.LOGGER.trace("Failed to unregister MBean {}", objectName);
            }
        }
        Server.LOGGER.debug("Registering MBean {}", objectName);
        mbs.registerMBean(mbean, objectName);
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        executor = (isJmxDisabled() ? null : createExecutor());
    }
}
