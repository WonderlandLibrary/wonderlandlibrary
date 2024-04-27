/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.slf4j.impl.StaticLoggerBinder
 */
package org.slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.event.SubstituteLoggingEvent;
import org.slf4j.helpers.NOPLoggerFactory;
import org.slf4j.helpers.SubstituteLogger;
import org.slf4j.helpers.SubstituteLoggerFactory;
import org.slf4j.helpers.Util;
import org.slf4j.impl.StaticLoggerBinder;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class LoggerFactory {
    static final String CODES_PREFIX = "http://www.slf4j.org/codes.html";
    static final String NO_STATICLOGGERBINDER_URL = "http://www.slf4j.org/codes.html#StaticLoggerBinder";
    static final String MULTIPLE_BINDINGS_URL = "http://www.slf4j.org/codes.html#multiple_bindings";
    static final String NULL_LF_URL = "http://www.slf4j.org/codes.html#null_LF";
    static final String VERSION_MISMATCH = "http://www.slf4j.org/codes.html#version_mismatch";
    static final String SUBSTITUTE_LOGGER_URL = "http://www.slf4j.org/codes.html#substituteLogger";
    static final String LOGGER_NAME_MISMATCH_URL = "http://www.slf4j.org/codes.html#loggerNameMismatch";
    static final String REPLAY_URL = "http://www.slf4j.org/codes.html#replay";
    static final String UNSUCCESSFUL_INIT_URL = "http://www.slf4j.org/codes.html#unsuccessfulInit";
    static final String UNSUCCESSFUL_INIT_MSG = "org.slf4j.LoggerFactory in failed state. Original exception was thrown EARLIER. See also http://www.slf4j.org/codes.html#unsuccessfulInit";
    static final int UNINITIALIZED = 0;
    static final int ONGOING_INITIALIZATION = 1;
    static final int FAILED_INITIALIZATION = 2;
    static final int SUCCESSFUL_INITIALIZATION = 3;
    static final int NOP_FALLBACK_INITIALIZATION = 4;
    static volatile int INITIALIZATION_STATE = 0;
    static final SubstituteLoggerFactory SUBST_FACTORY = new SubstituteLoggerFactory();
    static final NOPLoggerFactory NOP_FALLBACK_FACTORY = new NOPLoggerFactory();
    static final String DETECT_LOGGER_NAME_MISMATCH_PROPERTY = "slf4j.detectLoggerNameMismatch";
    static final String JAVA_VENDOR_PROPERTY = "java.vendor.url";
    static boolean DETECT_LOGGER_NAME_MISMATCH = Util.safeGetBooleanSystemProperty("slf4j.detectLoggerNameMismatch");
    private static final String[] API_COMPATIBILITY_LIST = new String[]{"1.6", "1.7"};
    private static String STATIC_LOGGER_BINDER_PATH = "org/slf4j/impl/StaticLoggerBinder.class";

    private LoggerFactory() {
    }

    static void reset() {
        INITIALIZATION_STATE = 0;
    }

    private static final void performInitialization() {
        LoggerFactory.bind();
        if (INITIALIZATION_STATE == 3) {
            LoggerFactory.versionSanityCheck();
        }
    }

    private static boolean messageContainsOrgSlf4jImplStaticLoggerBinder(String msg) {
        if (msg == null) {
            return false;
        }
        if (msg.contains("org/slf4j/impl/StaticLoggerBinder")) {
            return true;
        }
        return msg.contains("org.slf4j.impl.StaticLoggerBinder");
    }

    private static final void bind() {
        try {
            Set<URL> staticLoggerBinderPathSet = null;
            if (!LoggerFactory.isAndroid()) {
                staticLoggerBinderPathSet = LoggerFactory.findPossibleStaticLoggerBinderPathSet();
                LoggerFactory.reportMultipleBindingAmbiguity(staticLoggerBinderPathSet);
            }
            StaticLoggerBinder.getSingleton();
            INITIALIZATION_STATE = 3;
            LoggerFactory.reportActualBinding(staticLoggerBinderPathSet);
            LoggerFactory.fixSubstituteLoggers();
            LoggerFactory.replayEvents();
            SUBST_FACTORY.clear();
        }
        catch (NoClassDefFoundError ncde) {
            String msg = ncde.getMessage();
            if (LoggerFactory.messageContainsOrgSlf4jImplStaticLoggerBinder(msg)) {
                INITIALIZATION_STATE = 4;
                Util.report("Failed to load class \"org.slf4j.impl.StaticLoggerBinder\".");
                Util.report("Defaulting to no-operation (NOP) logger implementation");
                Util.report("See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.");
            }
            LoggerFactory.failedBinding(ncde);
            throw ncde;
        }
        catch (NoSuchMethodError nsme) {
            String msg = nsme.getMessage();
            if (msg != null && msg.contains("org.slf4j.impl.StaticLoggerBinder.getSingleton()")) {
                INITIALIZATION_STATE = 2;
                Util.report("slf4j-api 1.6.x (or later) is incompatible with this binding.");
                Util.report("Your binding is version 1.5.5 or earlier.");
                Util.report("Upgrade your binding to version 1.6.x.");
            }
            throw nsme;
        }
        catch (Exception e) {
            LoggerFactory.failedBinding(e);
            throw new IllegalStateException("Unexpected initialization failure", e);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void fixSubstituteLoggers() {
        SubstituteLoggerFactory substituteLoggerFactory = SUBST_FACTORY;
        synchronized (substituteLoggerFactory) {
            SUBST_FACTORY.postInitialization();
            for (SubstituteLogger substLogger : SUBST_FACTORY.getLoggers()) {
                Logger logger = LoggerFactory.getLogger(substLogger.getName());
                substLogger.setDelegate(logger);
            }
        }
    }

    static void failedBinding(Throwable t) {
        INITIALIZATION_STATE = 2;
        Util.report("Failed to instantiate SLF4J LoggerFactory", t);
    }

    private static void replayEvents() {
        int numDrained;
        LinkedBlockingQueue<SubstituteLoggingEvent> queue = SUBST_FACTORY.getEventQueue();
        int queueSize = queue.size();
        int count = 0;
        int maxDrain = 128;
        ArrayList eventList = new ArrayList(128);
        while ((numDrained = queue.drainTo(eventList, 128)) != 0) {
            for (SubstituteLoggingEvent event : eventList) {
                LoggerFactory.replaySingleEvent(event);
                if (count++ != 0) continue;
                LoggerFactory.emitReplayOrSubstituionWarning(event, queueSize);
            }
            eventList.clear();
        }
    }

    private static void emitReplayOrSubstituionWarning(SubstituteLoggingEvent event, int queueSize) {
        if (event.getLogger().isDelegateEventAware()) {
            LoggerFactory.emitReplayWarning(queueSize);
        } else if (!event.getLogger().isDelegateNOP()) {
            LoggerFactory.emitSubstitutionWarning();
        }
    }

    private static void replaySingleEvent(SubstituteLoggingEvent event) {
        if (event == null) {
            return;
        }
        SubstituteLogger substLogger = event.getLogger();
        String loggerName = substLogger.getName();
        if (substLogger.isDelegateNull()) {
            throw new IllegalStateException("Delegate logger cannot be null at this state.");
        }
        if (!substLogger.isDelegateNOP()) {
            if (substLogger.isDelegateEventAware()) {
                substLogger.log(event);
            } else {
                Util.report(loggerName);
            }
        }
    }

    private static void emitSubstitutionWarning() {
        Util.report("The following set of substitute loggers may have been accessed");
        Util.report("during the initialization phase. Logging calls during this");
        Util.report("phase were not honored. However, subsequent logging calls to these");
        Util.report("loggers will work as normally expected.");
        Util.report("See also http://www.slf4j.org/codes.html#substituteLogger");
    }

    private static void emitReplayWarning(int eventCount) {
        Util.report("A number (" + eventCount + ") of logging calls during the initialization phase have been intercepted and are");
        Util.report("now being replayed. These are subject to the filtering rules of the underlying logging system.");
        Util.report("See also http://www.slf4j.org/codes.html#replay");
    }

    private static final void versionSanityCheck() {
        try {
            String requested = StaticLoggerBinder.REQUESTED_API_VERSION;
            boolean match = false;
            for (String aAPI_COMPATIBILITY_LIST : API_COMPATIBILITY_LIST) {
                if (!requested.startsWith(aAPI_COMPATIBILITY_LIST)) continue;
                match = true;
            }
            if (!match) {
                Util.report("The requested version " + requested + " by your slf4j binding is not compatible with " + Arrays.asList(API_COMPATIBILITY_LIST).toString());
                Util.report("See http://www.slf4j.org/codes.html#version_mismatch for further details.");
            }
        }
        catch (NoSuchFieldError nsfe) {
        }
        catch (Throwable e) {
            Util.report("Unexpected problem occured during version sanity check", e);
        }
    }

    static Set<URL> findPossibleStaticLoggerBinderPathSet() {
        LinkedHashSet<URL> staticLoggerBinderPathSet = new LinkedHashSet<URL>();
        try {
            ClassLoader loggerFactoryClassLoader = LoggerFactory.class.getClassLoader();
            Enumeration<URL> paths = loggerFactoryClassLoader == null ? ClassLoader.getSystemResources(STATIC_LOGGER_BINDER_PATH) : loggerFactoryClassLoader.getResources(STATIC_LOGGER_BINDER_PATH);
            while (paths.hasMoreElements()) {
                URL path = paths.nextElement();
                staticLoggerBinderPathSet.add(path);
            }
        }
        catch (IOException ioe) {
            Util.report("Error getting resources from path", ioe);
        }
        return staticLoggerBinderPathSet;
    }

    private static boolean isAmbiguousStaticLoggerBinderPathSet(Set<URL> binderPathSet) {
        return binderPathSet.size() > 1;
    }

    private static void reportMultipleBindingAmbiguity(Set<URL> binderPathSet) {
        if (LoggerFactory.isAmbiguousStaticLoggerBinderPathSet(binderPathSet)) {
            Util.report("Class path contains multiple SLF4J bindings.");
            for (URL path : binderPathSet) {
                Util.report("Found binding in [" + path + "]");
            }
            Util.report("See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.");
        }
    }

    private static boolean isAndroid() {
        String vendor = Util.safeGetSystemProperty(JAVA_VENDOR_PROPERTY);
        if (vendor == null) {
            return false;
        }
        return vendor.toLowerCase().contains("android");
    }

    private static void reportActualBinding(Set<URL> binderPathSet) {
        if (binderPathSet != null && LoggerFactory.isAmbiguousStaticLoggerBinderPathSet(binderPathSet)) {
            Util.report("Actual binding is of type [" + StaticLoggerBinder.getSingleton().getLoggerFactoryClassStr() + "]");
        }
    }

    public static Logger getLogger(String name) {
        ILoggerFactory iLoggerFactory = LoggerFactory.getILoggerFactory();
        return iLoggerFactory.getLogger(name);
    }

    public static Logger getLogger(Class<?> clazz) {
        Class<?> autoComputedCallingClass;
        Logger logger = LoggerFactory.getLogger(clazz.getName());
        if (DETECT_LOGGER_NAME_MISMATCH && (autoComputedCallingClass = Util.getCallingClass()) != null && LoggerFactory.nonMatchingClasses(clazz, autoComputedCallingClass)) {
            Util.report(String.format("Detected logger name mismatch. Given name: \"%s\"; computed name: \"%s\".", logger.getName(), autoComputedCallingClass.getName()));
            Util.report("See http://www.slf4j.org/codes.html#loggerNameMismatch for an explanation");
        }
        return logger;
    }

    private static boolean nonMatchingClasses(Class<?> clazz, Class<?> autoComputedCallingClass) {
        return !autoComputedCallingClass.isAssignableFrom(clazz);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public static ILoggerFactory getILoggerFactory() {
        if (INITIALIZATION_STATE == 0) {
            Class<LoggerFactory> clazz = LoggerFactory.class;
            // MONITORENTER : org.slf4j.LoggerFactory.class
            if (INITIALIZATION_STATE == 0) {
                INITIALIZATION_STATE = 1;
                LoggerFactory.performInitialization();
            }
            // MONITOREXIT : clazz
        }
        switch (INITIALIZATION_STATE) {
            case 3: {
                return StaticLoggerBinder.getSingleton().getLoggerFactory();
            }
            case 4: {
                return NOP_FALLBACK_FACTORY;
            }
            case 2: {
                throw new IllegalStateException(UNSUCCESSFUL_INIT_MSG);
            }
            case 1: {
                return SUBST_FACTORY;
            }
        }
        throw new IllegalStateException("Unreachable code");
    }
}

