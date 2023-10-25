/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.logging.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Hashtable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;

public class LogFactoryImpl
extends LogFactory {
    private static final String LOGGING_IMPL_LOG4J_LOGGER = "org.apache.commons.logging.impl.Log4JLogger";
    private static final String LOGGING_IMPL_JDK14_LOGGER = "org.apache.commons.logging.impl.Jdk14Logger";
    private static final String LOGGING_IMPL_LUMBERJACK_LOGGER = "org.apache.commons.logging.impl.Jdk13LumberjackLogger";
    private static final String LOGGING_IMPL_SIMPLE_LOGGER = "org.apache.commons.logging.impl.SimpleLog";
    private static final String PKG_IMPL = "org.apache.commons.logging.impl.";
    private static final int PKG_LEN = "org.apache.commons.logging.impl.".length();
    public static final String LOG_PROPERTY = "org.apache.commons.logging.Log";
    protected static final String LOG_PROPERTY_OLD = "org.apache.commons.logging.log";
    public static final String ALLOW_FLAWED_CONTEXT_PROPERTY = "org.apache.commons.logging.Log.allowFlawedContext";
    public static final String ALLOW_FLAWED_DISCOVERY_PROPERTY = "org.apache.commons.logging.Log.allowFlawedDiscovery";
    public static final String ALLOW_FLAWED_HIERARCHY_PROPERTY = "org.apache.commons.logging.Log.allowFlawedHierarchy";
    private static final String[] classesToDiscover = new String[]{"org.apache.commons.logging.impl.Log4JLogger", "org.apache.commons.logging.impl.Jdk14Logger", "org.apache.commons.logging.impl.Jdk13LumberjackLogger", "org.apache.commons.logging.impl.SimpleLog"};
    private boolean useTCCL = true;
    private String diagnosticPrefix;
    protected Hashtable attributes = new Hashtable();
    protected Hashtable instances = new Hashtable();
    private String logClassName;
    protected Constructor logConstructor = null;
    protected Class[] logConstructorSignature = new Class[]{class$java$lang$String == null ? (class$java$lang$String = LogFactoryImpl.class$("java.lang.String")) : class$java$lang$String};
    protected Method logMethod = null;
    protected Class[] logMethodSignature = new Class[]{class$org$apache$commons$logging$LogFactory == null ? (class$org$apache$commons$logging$LogFactory = LogFactoryImpl.class$("org.apache.commons.logging.LogFactory")) : class$org$apache$commons$logging$LogFactory};
    private boolean allowFlawedContext;
    private boolean allowFlawedDiscovery;
    private boolean allowFlawedHierarchy;
    static /* synthetic */ Class class$java$lang$String;
    static /* synthetic */ Class class$org$apache$commons$logging$LogFactory;
    static /* synthetic */ Class class$org$apache$commons$logging$impl$LogFactoryImpl;
    static /* synthetic */ Class class$org$apache$commons$logging$Log;

    public LogFactoryImpl() {
        this.initDiagnostics();
        if (LogFactoryImpl.isDiagnosticsEnabled()) {
            this.logDiagnostic("Instance created.");
        }
    }

    public Object getAttribute(String name) {
        return this.attributes.get(name);
    }

    public String[] getAttributeNames() {
        return this.attributes.keySet().toArray(new String[this.attributes.size()]);
    }

    public Log getInstance(Class clazz) throws LogConfigurationException {
        return this.getInstance(clazz.getName());
    }

    public Log getInstance(String name) throws LogConfigurationException {
        Log instance = (Log)this.instances.get(name);
        if (instance == null) {
            instance = this.newInstance(name);
            this.instances.put(name, instance);
        }
        return instance;
    }

    public void release() {
        this.logDiagnostic("Releasing all known loggers");
        this.instances.clear();
    }

    public void removeAttribute(String name) {
        this.attributes.remove(name);
    }

    public void setAttribute(String name, Object value) {
        if (this.logConstructor != null) {
            this.logDiagnostic("setAttribute: call too late; configuration already performed.");
        }
        if (value == null) {
            this.attributes.remove(name);
        } else {
            this.attributes.put(name, value);
        }
        if (name.equals("use_tccl")) {
            this.useTCCL = value != null && Boolean.valueOf(value.toString()) != false;
        }
    }

    protected static ClassLoader getContextClassLoader() throws LogConfigurationException {
        return LogFactory.getContextClassLoader();
    }

    protected static boolean isDiagnosticsEnabled() {
        return LogFactory.isDiagnosticsEnabled();
    }

    protected static ClassLoader getClassLoader(Class clazz) {
        return LogFactory.getClassLoader(clazz);
    }

    private void initDiagnostics() {
        String classLoaderName;
        Class<?> clazz = this.getClass();
        ClassLoader classLoader = LogFactoryImpl.getClassLoader(clazz);
        try {
            classLoaderName = classLoader == null ? "BOOTLOADER" : LogFactory.objectId(classLoader);
        }
        catch (SecurityException e) {
            classLoaderName = "UNKNOWN";
        }
        this.diagnosticPrefix = "[LogFactoryImpl@" + System.identityHashCode(this) + " from " + classLoaderName + "] ";
    }

    protected void logDiagnostic(String msg) {
        if (LogFactoryImpl.isDiagnosticsEnabled()) {
            LogFactory.logRawDiagnostic(this.diagnosticPrefix + msg);
        }
    }

    protected String getLogClassName() {
        if (this.logClassName == null) {
            this.discoverLogImplementation(this.getClass().getName());
        }
        return this.logClassName;
    }

    protected Constructor getLogConstructor() throws LogConfigurationException {
        if (this.logConstructor == null) {
            this.discoverLogImplementation(this.getClass().getName());
        }
        return this.logConstructor;
    }

    protected boolean isJdk13LumberjackAvailable() {
        return this.isLogLibraryAvailable("Jdk13Lumberjack", LOGGING_IMPL_LUMBERJACK_LOGGER);
    }

    protected boolean isJdk14Available() {
        return this.isLogLibraryAvailable("Jdk14", LOGGING_IMPL_JDK14_LOGGER);
    }

    protected boolean isLog4JAvailable() {
        return this.isLogLibraryAvailable("Log4J", LOGGING_IMPL_LOG4J_LOGGER);
    }

    protected Log newInstance(String name) throws LogConfigurationException {
        try {
            Object[] params;
            Log instance;
            if (this.logConstructor == null) {
                instance = this.discoverLogImplementation(name);
            } else {
                params = new Object[]{name};
                instance = (Log)this.logConstructor.newInstance(params);
            }
            if (this.logMethod != null) {
                params = new Object[]{this};
                this.logMethod.invoke(instance, params);
            }
            return instance;
        }
        catch (LogConfigurationException lce) {
            throw lce;
        }
        catch (InvocationTargetException e) {
            Throwable c = e.getTargetException();
            throw new LogConfigurationException(c == null ? e : c);
        }
        catch (Throwable t) {
            LogFactory.handleThrowable(t);
            throw new LogConfigurationException(t);
        }
    }

    private static ClassLoader getContextClassLoaderInternal() throws LogConfigurationException {
        return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction(){

            public Object run() {
                return LogFactoryImpl.access$000();
            }
        });
    }

    private static String getSystemProperty(final String key, final String def) throws SecurityException {
        return (String)AccessController.doPrivileged(new PrivilegedAction(){

            public Object run() {
                return System.getProperty(key, def);
            }
        });
    }

    private ClassLoader getParentClassLoader(final ClassLoader cl) {
        try {
            return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction(){

                public Object run() {
                    return cl.getParent();
                }
            });
        }
        catch (SecurityException ex) {
            this.logDiagnostic("[SECURITY] Unable to obtain parent classloader");
            return null;
        }
    }

    private boolean isLogLibraryAvailable(String name, String classname) {
        if (LogFactoryImpl.isDiagnosticsEnabled()) {
            this.logDiagnostic("Checking for '" + name + "'.");
        }
        try {
            Log log = this.createLogFromClass(classname, this.getClass().getName(), false);
            if (log == null) {
                if (LogFactoryImpl.isDiagnosticsEnabled()) {
                    this.logDiagnostic("Did not find '" + name + "'.");
                }
                return false;
            }
            if (LogFactoryImpl.isDiagnosticsEnabled()) {
                this.logDiagnostic("Found '" + name + "'.");
            }
            return true;
        }
        catch (LogConfigurationException e) {
            if (LogFactoryImpl.isDiagnosticsEnabled()) {
                this.logDiagnostic("Logging system '" + name + "' is available but not useable.");
            }
            return false;
        }
    }

    private String getConfigurationValue(String property) {
        block10: {
            Object valueObj;
            if (LogFactoryImpl.isDiagnosticsEnabled()) {
                this.logDiagnostic("[ENV] Trying to get configuration for item " + property);
            }
            if ((valueObj = this.getAttribute(property)) != null) {
                if (LogFactoryImpl.isDiagnosticsEnabled()) {
                    this.logDiagnostic("[ENV] Found LogFactory attribute [" + valueObj + "] for " + property);
                }
                return valueObj.toString();
            }
            if (LogFactoryImpl.isDiagnosticsEnabled()) {
                this.logDiagnostic("[ENV] No LogFactory attribute found for " + property);
            }
            try {
                String value = LogFactoryImpl.getSystemProperty(property, null);
                if (value != null) {
                    if (LogFactoryImpl.isDiagnosticsEnabled()) {
                        this.logDiagnostic("[ENV] Found system property [" + value + "] for " + property);
                    }
                    return value;
                }
                if (LogFactoryImpl.isDiagnosticsEnabled()) {
                    this.logDiagnostic("[ENV] No system property found for property " + property);
                }
            }
            catch (SecurityException e) {
                if (!LogFactoryImpl.isDiagnosticsEnabled()) break block10;
                this.logDiagnostic("[ENV] Security prevented reading system property " + property);
            }
        }
        if (LogFactoryImpl.isDiagnosticsEnabled()) {
            this.logDiagnostic("[ENV] No configuration defined for item " + property);
        }
        return null;
    }

    private boolean getBooleanConfiguration(String key, boolean dflt) {
        String val = this.getConfigurationValue(key);
        if (val == null) {
            return dflt;
        }
        return Boolean.valueOf(val);
    }

    private void initConfiguration() {
        this.allowFlawedContext = this.getBooleanConfiguration(ALLOW_FLAWED_CONTEXT_PROPERTY, true);
        this.allowFlawedDiscovery = this.getBooleanConfiguration(ALLOW_FLAWED_DISCOVERY_PROPERTY, true);
        this.allowFlawedHierarchy = this.getBooleanConfiguration(ALLOW_FLAWED_HIERARCHY_PROPERTY, true);
    }

    private Log discoverLogImplementation(String logCategory) throws LogConfigurationException {
        if (LogFactoryImpl.isDiagnosticsEnabled()) {
            this.logDiagnostic("Discovering a Log implementation...");
        }
        this.initConfiguration();
        Log result = null;
        String specifiedLogClassName = this.findUserSpecifiedLogClassName();
        if (specifiedLogClassName != null) {
            if (LogFactoryImpl.isDiagnosticsEnabled()) {
                this.logDiagnostic("Attempting to load user-specified log class '" + specifiedLogClassName + "'...");
            }
            if ((result = this.createLogFromClass(specifiedLogClassName, logCategory, true)) == null) {
                StringBuffer messageBuffer = new StringBuffer("User-specified log class '");
                messageBuffer.append(specifiedLogClassName);
                messageBuffer.append("' cannot be found or is not useable.");
                this.informUponSimilarName(messageBuffer, specifiedLogClassName, LOGGING_IMPL_LOG4J_LOGGER);
                this.informUponSimilarName(messageBuffer, specifiedLogClassName, LOGGING_IMPL_JDK14_LOGGER);
                this.informUponSimilarName(messageBuffer, specifiedLogClassName, LOGGING_IMPL_LUMBERJACK_LOGGER);
                this.informUponSimilarName(messageBuffer, specifiedLogClassName, LOGGING_IMPL_SIMPLE_LOGGER);
                throw new LogConfigurationException(messageBuffer.toString());
            }
            return result;
        }
        if (LogFactoryImpl.isDiagnosticsEnabled()) {
            this.logDiagnostic("No user-specified Log implementation; performing discovery using the standard supported logging implementations...");
        }
        for (int i = 0; i < classesToDiscover.length && result == null; ++i) {
            result = this.createLogFromClass(classesToDiscover[i], logCategory, true);
        }
        if (result == null) {
            throw new LogConfigurationException("No suitable Log implementation");
        }
        return result;
    }

    private void informUponSimilarName(StringBuffer messageBuffer, String name, String candidate) {
        if (name.equals(candidate)) {
            return;
        }
        if (name.regionMatches(true, 0, candidate, 0, PKG_LEN + 5)) {
            messageBuffer.append(" Did you mean '");
            messageBuffer.append(candidate);
            messageBuffer.append("'?");
        }
    }

    private String findUserSpecifiedLogClassName() {
        String specifiedClass;
        block13: {
            block12: {
                if (LogFactoryImpl.isDiagnosticsEnabled()) {
                    this.logDiagnostic("Trying to get log class from attribute 'org.apache.commons.logging.Log'");
                }
                if ((specifiedClass = (String)this.getAttribute(LOG_PROPERTY)) == null) {
                    if (LogFactoryImpl.isDiagnosticsEnabled()) {
                        this.logDiagnostic("Trying to get log class from attribute 'org.apache.commons.logging.log'");
                    }
                    specifiedClass = (String)this.getAttribute(LOG_PROPERTY_OLD);
                }
                if (specifiedClass == null) {
                    if (LogFactoryImpl.isDiagnosticsEnabled()) {
                        this.logDiagnostic("Trying to get log class from system property 'org.apache.commons.logging.Log'");
                    }
                    try {
                        specifiedClass = LogFactoryImpl.getSystemProperty(LOG_PROPERTY, null);
                    }
                    catch (SecurityException e) {
                        if (!LogFactoryImpl.isDiagnosticsEnabled()) break block12;
                        this.logDiagnostic("No access allowed to system property 'org.apache.commons.logging.Log' - " + e.getMessage());
                    }
                }
            }
            if (specifiedClass == null) {
                if (LogFactoryImpl.isDiagnosticsEnabled()) {
                    this.logDiagnostic("Trying to get log class from system property 'org.apache.commons.logging.log'");
                }
                try {
                    specifiedClass = LogFactoryImpl.getSystemProperty(LOG_PROPERTY_OLD, null);
                }
                catch (SecurityException e) {
                    if (!LogFactoryImpl.isDiagnosticsEnabled()) break block13;
                    this.logDiagnostic("No access allowed to system property 'org.apache.commons.logging.log' - " + e.getMessage());
                }
            }
        }
        if (specifiedClass != null) {
            specifiedClass = specifiedClass.trim();
        }
        return specifiedClass;
    }

    private Log createLogFromClass(String logAdapterClassName, String logCategory, boolean affectState) throws LogConfigurationException {
        if (LogFactoryImpl.isDiagnosticsEnabled()) {
            this.logDiagnostic("Attempting to instantiate '" + logAdapterClassName + "'");
        }
        Object[] params = new Object[]{logCategory};
        Log logAdapter = null;
        Constructor<?> constructor = null;
        Class<?> logAdapterClass = null;
        ClassLoader currentCL = this.getBaseClassLoader();
        while (true) {
            String msg;
            this.logDiagnostic("Trying to load '" + logAdapterClassName + "' from classloader " + LogFactory.objectId(currentCL));
            try {
                Class<?> c;
                if (LogFactoryImpl.isDiagnosticsEnabled()) {
                    String resourceName = logAdapterClassName.replace('.', '/') + ".class";
                    URL url = currentCL != null ? currentCL.getResource(resourceName) : ClassLoader.getSystemResource(resourceName + ".class");
                    if (url == null) {
                        this.logDiagnostic("Class '" + logAdapterClassName + "' [" + resourceName + "] cannot be found.");
                    } else {
                        this.logDiagnostic("Class '" + logAdapterClassName + "' was found at '" + url + "'");
                    }
                }
                try {
                    c = Class.forName(logAdapterClassName, true, currentCL);
                }
                catch (ClassNotFoundException originalClassNotFoundException) {
                    String msg2 = originalClassNotFoundException.getMessage();
                    this.logDiagnostic("The log adapter '" + logAdapterClassName + "' is not available via classloader " + LogFactory.objectId(currentCL) + ": " + msg2.trim());
                    try {
                        c = Class.forName(logAdapterClassName);
                    }
                    catch (ClassNotFoundException secondaryClassNotFoundException) {
                        msg2 = secondaryClassNotFoundException.getMessage();
                        this.logDiagnostic("The log adapter '" + logAdapterClassName + "' is not available via the LogFactoryImpl class classloader: " + msg2.trim());
                        break;
                    }
                }
                constructor = c.getConstructor(this.logConstructorSignature);
                Object o = constructor.newInstance(params);
                if (o instanceof Log) {
                    logAdapterClass = c;
                    logAdapter = (Log)o;
                    break;
                }
                this.handleFlawedHierarchy(currentCL, c);
            }
            catch (NoClassDefFoundError e) {
                msg = e.getMessage();
                this.logDiagnostic("The log adapter '" + logAdapterClassName + "' is missing dependencies when loaded via classloader " + LogFactory.objectId(currentCL) + ": " + msg.trim());
                break;
            }
            catch (ExceptionInInitializerError e) {
                msg = e.getMessage();
                this.logDiagnostic("The log adapter '" + logAdapterClassName + "' is unable to initialize itself when loaded via classloader " + LogFactory.objectId(currentCL) + ": " + msg.trim());
                break;
            }
            catch (LogConfigurationException e) {
                throw e;
            }
            catch (Throwable t) {
                LogFactory.handleThrowable(t);
                this.handleFlawedDiscovery(logAdapterClassName, currentCL, t);
            }
            if (currentCL == null) break;
            currentCL = this.getParentClassLoader(currentCL);
        }
        if (logAdapterClass != null && affectState) {
            this.logClassName = logAdapterClassName;
            this.logConstructor = constructor;
            try {
                this.logMethod = logAdapterClass.getMethod("setLogFactory", this.logMethodSignature);
                this.logDiagnostic("Found method setLogFactory(LogFactory) in '" + logAdapterClassName + "'");
            }
            catch (Throwable t) {
                LogFactory.handleThrowable(t);
                this.logMethod = null;
                this.logDiagnostic("[INFO] '" + logAdapterClassName + "' from classloader " + LogFactory.objectId(currentCL) + " does not declare optional method " + "setLogFactory(LogFactory)");
            }
            this.logDiagnostic("Log adapter '" + logAdapterClassName + "' from classloader " + LogFactory.objectId(logAdapterClass.getClassLoader()) + " has been selected for use.");
        }
        return logAdapter;
    }

    private ClassLoader getBaseClassLoader() throws LogConfigurationException {
        ClassLoader thisClassLoader = LogFactoryImpl.getClassLoader(class$org$apache$commons$logging$impl$LogFactoryImpl == null ? (class$org$apache$commons$logging$impl$LogFactoryImpl = LogFactoryImpl.class$("org.apache.commons.logging.impl.LogFactoryImpl")) : class$org$apache$commons$logging$impl$LogFactoryImpl);
        if (!this.useTCCL) {
            return thisClassLoader;
        }
        ClassLoader contextClassLoader = LogFactoryImpl.getContextClassLoaderInternal();
        ClassLoader baseClassLoader = this.getLowestClassLoader(contextClassLoader, thisClassLoader);
        if (baseClassLoader == null) {
            if (this.allowFlawedContext) {
                if (LogFactoryImpl.isDiagnosticsEnabled()) {
                    this.logDiagnostic("[WARNING] the context classloader is not part of a parent-child relationship with the classloader that loaded LogFactoryImpl.");
                }
                return contextClassLoader;
            }
            throw new LogConfigurationException("Bad classloader hierarchy; LogFactoryImpl was loaded via a classloader that is not related to the current context classloader.");
        }
        if (baseClassLoader != contextClassLoader) {
            if (this.allowFlawedContext) {
                if (LogFactoryImpl.isDiagnosticsEnabled()) {
                    this.logDiagnostic("Warning: the context classloader is an ancestor of the classloader that loaded LogFactoryImpl; it should be the same or a descendant. The application using commons-logging should ensure the context classloader is used correctly.");
                }
            } else {
                throw new LogConfigurationException("Bad classloader hierarchy; LogFactoryImpl was loaded via a classloader that is not related to the current context classloader.");
            }
        }
        return baseClassLoader;
    }

    private ClassLoader getLowestClassLoader(ClassLoader c1, ClassLoader c2) {
        if (c1 == null) {
            return c2;
        }
        if (c2 == null) {
            return c1;
        }
        ClassLoader current = c1;
        while (current != null) {
            if (current == c2) {
                return c1;
            }
            current = this.getParentClassLoader(current);
        }
        current = c2;
        while (current != null) {
            if (current == c1) {
                return c2;
            }
            current = this.getParentClassLoader(current);
        }
        return null;
    }

    private void handleFlawedDiscovery(String logAdapterClassName, ClassLoader classLoader, Throwable discoveryFlaw) {
        if (LogFactoryImpl.isDiagnosticsEnabled()) {
            InvocationTargetException ite;
            Throwable cause;
            this.logDiagnostic("Could not instantiate Log '" + logAdapterClassName + "' -- " + discoveryFlaw.getClass().getName() + ": " + discoveryFlaw.getLocalizedMessage());
            if (discoveryFlaw instanceof InvocationTargetException && (cause = (ite = (InvocationTargetException)discoveryFlaw).getTargetException()) != null) {
                ExceptionInInitializerError eiie;
                Throwable cause2;
                this.logDiagnostic("... InvocationTargetException: " + cause.getClass().getName() + ": " + cause.getLocalizedMessage());
                if (cause instanceof ExceptionInInitializerError && (cause2 = (eiie = (ExceptionInInitializerError)cause).getException()) != null) {
                    StringWriter sw = new StringWriter();
                    cause2.printStackTrace(new PrintWriter(sw, true));
                    this.logDiagnostic("... ExceptionInInitializerError: " + sw.toString());
                }
            }
        }
        if (!this.allowFlawedDiscovery) {
            throw new LogConfigurationException(discoveryFlaw);
        }
    }

    private void handleFlawedHierarchy(ClassLoader badClassLoader, Class badClass) throws LogConfigurationException {
        boolean implementsLog = false;
        String logInterfaceName = (class$org$apache$commons$logging$Log == null ? (class$org$apache$commons$logging$Log = LogFactoryImpl.class$(LOG_PROPERTY)) : class$org$apache$commons$logging$Log).getName();
        Class<?>[] interfaces = badClass.getInterfaces();
        for (int i = 0; i < interfaces.length; ++i) {
            if (!logInterfaceName.equals(interfaces[i].getName())) continue;
            implementsLog = true;
            break;
        }
        if (implementsLog) {
            if (LogFactoryImpl.isDiagnosticsEnabled()) {
                try {
                    ClassLoader logInterfaceClassLoader = LogFactoryImpl.getClassLoader(class$org$apache$commons$logging$Log == null ? (class$org$apache$commons$logging$Log = LogFactoryImpl.class$(LOG_PROPERTY)) : class$org$apache$commons$logging$Log);
                    this.logDiagnostic("Class '" + badClass.getName() + "' was found in classloader " + LogFactory.objectId(badClassLoader) + ". It is bound to a Log interface which is not" + " the one loaded from classloader " + LogFactory.objectId(logInterfaceClassLoader));
                }
                catch (Throwable t) {
                    LogFactory.handleThrowable(t);
                    this.logDiagnostic("Error while trying to output diagnostics about bad class '" + badClass + "'");
                }
            }
            if (!this.allowFlawedHierarchy) {
                StringBuffer msg = new StringBuffer();
                msg.append("Terminating logging for this context ");
                msg.append("due to bad log hierarchy. ");
                msg.append("You have more than one version of '");
                msg.append((class$org$apache$commons$logging$Log == null ? (class$org$apache$commons$logging$Log = LogFactoryImpl.class$(LOG_PROPERTY)) : class$org$apache$commons$logging$Log).getName());
                msg.append("' visible.");
                if (LogFactoryImpl.isDiagnosticsEnabled()) {
                    this.logDiagnostic(msg.toString());
                }
                throw new LogConfigurationException(msg.toString());
            }
            if (LogFactoryImpl.isDiagnosticsEnabled()) {
                StringBuffer msg = new StringBuffer();
                msg.append("Warning: bad log hierarchy. ");
                msg.append("You have more than one version of '");
                msg.append((class$org$apache$commons$logging$Log == null ? (class$org$apache$commons$logging$Log = LogFactoryImpl.class$(LOG_PROPERTY)) : class$org$apache$commons$logging$Log).getName());
                msg.append("' visible.");
                this.logDiagnostic(msg.toString());
            }
        } else {
            if (!this.allowFlawedDiscovery) {
                StringBuffer msg = new StringBuffer();
                msg.append("Terminating logging for this context. ");
                msg.append("Log class '");
                msg.append(badClass.getName());
                msg.append("' does not implement the Log interface.");
                if (LogFactoryImpl.isDiagnosticsEnabled()) {
                    this.logDiagnostic(msg.toString());
                }
                throw new LogConfigurationException(msg.toString());
            }
            if (LogFactoryImpl.isDiagnosticsEnabled()) {
                StringBuffer msg = new StringBuffer();
                msg.append("[WARNING] Log class '");
                msg.append(badClass.getName());
                msg.append("' does not implement the Log interface.");
                this.logDiagnostic(msg.toString());
            }
        }
    }

    static /* synthetic */ Class class$(String x0) {
        try {
            return Class.forName(x0);
        }
        catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static /* synthetic */ ClassLoader access$000() throws LogConfigurationException {
        return LogFactory.directGetContextClassLoader();
    }
}

