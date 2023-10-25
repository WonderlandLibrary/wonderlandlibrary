/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.logging;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;

public abstract class LogFactory {
    public static final String PRIORITY_KEY = "priority";
    public static final String TCCL_KEY = "use_tccl";
    public static final String FACTORY_PROPERTY = "org.apache.commons.logging.LogFactory";
    public static final String FACTORY_DEFAULT = "org.apache.commons.logging.impl.LogFactoryImpl";
    public static final String FACTORY_PROPERTIES = "commons-logging.properties";
    protected static final String SERVICE_ID = "META-INF/services/org.apache.commons.logging.LogFactory";
    public static final String DIAGNOSTICS_DEST_PROPERTY = "org.apache.commons.logging.diagnostics.dest";
    private static PrintStream diagnosticsStream;
    private static final String diagnosticPrefix;
    public static final String HASHTABLE_IMPLEMENTATION_PROPERTY = "org.apache.commons.logging.LogFactory.HashtableImpl";
    private static final String WEAK_HASHTABLE_CLASSNAME = "org.apache.commons.logging.impl.WeakHashtable";
    private static final ClassLoader thisClassLoader;
    protected static Hashtable factories;
    protected static volatile LogFactory nullClassLoaderFactory;
    static /* synthetic */ Class class$java$lang$Thread;
    static /* synthetic */ Class class$org$apache$commons$logging$LogFactory;

    protected LogFactory() {
    }

    public abstract Object getAttribute(String var1);

    public abstract String[] getAttributeNames();

    public abstract Log getInstance(Class var1) throws LogConfigurationException;

    public abstract Log getInstance(String var1) throws LogConfigurationException;

    public abstract void release();

    public abstract void removeAttribute(String var1);

    public abstract void setAttribute(String var1, Object var2);

    private static final Hashtable createFactoryStore() {
        Hashtable result;
        block7: {
            String storeImplementationClass;
            result = null;
            try {
                storeImplementationClass = LogFactory.getSystemProperty("org.apache.commons.logging.LogFactory.HashtableImpl", null);
            }
            catch (SecurityException ex) {
                storeImplementationClass = null;
            }
            if (storeImplementationClass == null) {
                storeImplementationClass = "org.apache.commons.logging.impl.WeakHashtable";
            }
            try {
                Class<?> implementationClass = Class.forName(storeImplementationClass);
                result = (Hashtable)implementationClass.newInstance();
            }
            catch (Throwable t) {
                LogFactory.handleThrowable(t);
                if ("org.apache.commons.logging.impl.WeakHashtable".equals(storeImplementationClass)) break block7;
                if (LogFactory.isDiagnosticsEnabled()) {
                    LogFactory.logDiagnostic("[ERROR] LogFactory: Load of custom hashtable failed");
                }
                System.err.println("[ERROR] LogFactory: Load of custom hashtable failed");
            }
        }
        if (result == null) {
            result = new Hashtable();
        }
        return result;
    }

    private static String trim(String src) {
        if (src == null) {
            return null;
        }
        return src.trim();
    }

    protected static void handleThrowable(Throwable t) {
        if (t instanceof ThreadDeath) {
            throw (ThreadDeath)t;
        }
        if (t instanceof VirtualMachineError) {
            throw (VirtualMachineError)t;
        }
    }

    public static LogFactory getFactory() throws LogConfigurationException {
        String factoryClass;
        ClassLoader baseClassLoader;
        Properties props;
        LogFactory factory;
        ClassLoader contextClassLoader;
        block38: {
            String useTCCLStr;
            contextClassLoader = LogFactory.getContextClassLoaderInternal();
            if (contextClassLoader == null && LogFactory.isDiagnosticsEnabled()) {
                LogFactory.logDiagnostic("Context classloader is null.");
            }
            if ((factory = LogFactory.getCachedFactory(contextClassLoader)) != null) {
                return factory;
            }
            if (LogFactory.isDiagnosticsEnabled()) {
                LogFactory.logDiagnostic("[LOOKUP] LogFactory implementation requested for the first time for context classloader " + LogFactory.objectId(contextClassLoader));
                LogFactory.logHierarchy("[LOOKUP] ", contextClassLoader);
            }
            props = LogFactory.getConfigurationFile(contextClassLoader, "commons-logging.properties");
            baseClassLoader = contextClassLoader;
            if (props != null && (useTCCLStr = props.getProperty("use_tccl")) != null && !Boolean.valueOf(useTCCLStr).booleanValue()) {
                baseClassLoader = thisClassLoader;
            }
            if (LogFactory.isDiagnosticsEnabled()) {
                LogFactory.logDiagnostic("[LOOKUP] Looking for system property [org.apache.commons.logging.LogFactory] to define the LogFactory subclass to use...");
            }
            try {
                factoryClass = LogFactory.getSystemProperty("org.apache.commons.logging.LogFactory", null);
                if (factoryClass != null) {
                    if (LogFactory.isDiagnosticsEnabled()) {
                        LogFactory.logDiagnostic("[LOOKUP] Creating an instance of LogFactory class '" + factoryClass + "' as specified by system property " + "org.apache.commons.logging.LogFactory");
                    }
                    factory = LogFactory.newFactory(factoryClass, baseClassLoader, contextClassLoader);
                } else if (LogFactory.isDiagnosticsEnabled()) {
                    LogFactory.logDiagnostic("[LOOKUP] No system property [org.apache.commons.logging.LogFactory] defined.");
                }
            }
            catch (SecurityException e) {
                if (LogFactory.isDiagnosticsEnabled()) {
                    LogFactory.logDiagnostic("[LOOKUP] A security exception occurred while trying to create an instance of the custom factory class: [" + LogFactory.trim(e.getMessage()) + "]. Trying alternative implementations...");
                }
            }
            catch (RuntimeException e) {
                if (LogFactory.isDiagnosticsEnabled()) {
                    LogFactory.logDiagnostic("[LOOKUP] An exception occurred while trying to create an instance of the custom factory class: [" + LogFactory.trim(e.getMessage()) + "] as specified by a system property.");
                }
                throw e;
            }
            if (factory == null) {
                if (LogFactory.isDiagnosticsEnabled()) {
                    LogFactory.logDiagnostic("[LOOKUP] Looking for a resource file of name [META-INF/services/org.apache.commons.logging.LogFactory] to define the LogFactory subclass to use...");
                }
                try {
                    InputStream is = LogFactory.getResourceAsStream(contextClassLoader, "META-INF/services/org.apache.commons.logging.LogFactory");
                    if (is != null) {
                        BufferedReader rd;
                        try {
                            rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                        }
                        catch (UnsupportedEncodingException e) {
                            rd = new BufferedReader(new InputStreamReader(is));
                        }
                        String factoryClassName = rd.readLine();
                        rd.close();
                        if (factoryClassName != null && !"".equals(factoryClassName)) {
                            if (LogFactory.isDiagnosticsEnabled()) {
                                LogFactory.logDiagnostic("[LOOKUP]  Creating an instance of LogFactory class " + factoryClassName + " as specified by file '" + "META-INF/services/org.apache.commons.logging.LogFactory" + "' which was present in the path of the context classloader.");
                            }
                            factory = LogFactory.newFactory(factoryClassName, baseClassLoader, contextClassLoader);
                        }
                        break block38;
                    }
                    if (LogFactory.isDiagnosticsEnabled()) {
                        LogFactory.logDiagnostic("[LOOKUP] No resource file with name 'META-INF/services/org.apache.commons.logging.LogFactory' found.");
                    }
                }
                catch (Exception ex) {
                    if (!LogFactory.isDiagnosticsEnabled()) break block38;
                    LogFactory.logDiagnostic("[LOOKUP] A security exception occurred while trying to create an instance of the custom factory class: [" + LogFactory.trim(ex.getMessage()) + "]. Trying alternative implementations...");
                }
            }
        }
        if (factory == null) {
            if (props != null) {
                if (LogFactory.isDiagnosticsEnabled()) {
                    LogFactory.logDiagnostic("[LOOKUP] Looking in properties file for entry with key 'org.apache.commons.logging.LogFactory' to define the LogFactory subclass to use...");
                }
                if ((factoryClass = props.getProperty("org.apache.commons.logging.LogFactory")) != null) {
                    if (LogFactory.isDiagnosticsEnabled()) {
                        LogFactory.logDiagnostic("[LOOKUP] Properties file specifies LogFactory subclass '" + factoryClass + "'");
                    }
                    factory = LogFactory.newFactory(factoryClass, baseClassLoader, contextClassLoader);
                } else if (LogFactory.isDiagnosticsEnabled()) {
                    LogFactory.logDiagnostic("[LOOKUP] Properties file has no entry specifying LogFactory subclass.");
                }
            } else if (LogFactory.isDiagnosticsEnabled()) {
                LogFactory.logDiagnostic("[LOOKUP] No properties file available to determine LogFactory subclass from..");
            }
        }
        if (factory == null) {
            if (LogFactory.isDiagnosticsEnabled()) {
                LogFactory.logDiagnostic("[LOOKUP] Loading the default LogFactory implementation 'org.apache.commons.logging.impl.LogFactoryImpl' via the same classloader that loaded this LogFactory class (ie not looking in the context classloader).");
            }
            factory = LogFactory.newFactory("org.apache.commons.logging.impl.LogFactoryImpl", thisClassLoader, contextClassLoader);
        }
        if (factory != null) {
            LogFactory.cacheFactory(contextClassLoader, factory);
            if (props != null) {
                Enumeration<?> names = props.propertyNames();
                while (names.hasMoreElements()) {
                    String name = (String)names.nextElement();
                    String value = props.getProperty(name);
                    factory.setAttribute(name, value);
                }
            }
        }
        return factory;
    }

    public static Log getLog(Class clazz) throws LogConfigurationException {
        return LogFactory.getFactory().getInstance(clazz);
    }

    public static Log getLog(String name) throws LogConfigurationException {
        return LogFactory.getFactory().getInstance(name);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void release(ClassLoader classLoader) {
        Hashtable factories;
        if (LogFactory.isDiagnosticsEnabled()) {
            LogFactory.logDiagnostic("Releasing factory for classloader " + LogFactory.objectId(classLoader));
        }
        Hashtable hashtable = factories = LogFactory.factories;
        synchronized (hashtable) {
            if (classLoader == null) {
                if (nullClassLoaderFactory != null) {
                    nullClassLoaderFactory.release();
                    nullClassLoaderFactory = null;
                }
            } else {
                LogFactory factory = (LogFactory)factories.get(classLoader);
                if (factory != null) {
                    factory.release();
                    factories.remove(classLoader);
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void releaseAll() {
        Hashtable factories;
        if (LogFactory.isDiagnosticsEnabled()) {
            LogFactory.logDiagnostic("Releasing factory for all classloaders.");
        }
        Hashtable hashtable = factories = LogFactory.factories;
        synchronized (hashtable) {
            Enumeration elements = factories.elements();
            while (elements.hasMoreElements()) {
                LogFactory element = (LogFactory)elements.nextElement();
                element.release();
            }
            factories.clear();
            if (nullClassLoaderFactory != null) {
                nullClassLoaderFactory.release();
                nullClassLoaderFactory = null;
            }
        }
    }

    protected static ClassLoader getClassLoader(Class clazz) {
        try {
            return clazz.getClassLoader();
        }
        catch (SecurityException ex) {
            if (LogFactory.isDiagnosticsEnabled()) {
                LogFactory.logDiagnostic("Unable to get classloader for class '" + clazz + "' due to security restrictions - " + ex.getMessage());
            }
            throw ex;
        }
    }

    protected static ClassLoader getContextClassLoader() throws LogConfigurationException {
        return LogFactory.directGetContextClassLoader();
    }

    private static ClassLoader getContextClassLoaderInternal() throws LogConfigurationException {
        return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction(){

            public Object run() {
                return LogFactory.directGetContextClassLoader();
            }
        });
    }

    protected static ClassLoader directGetContextClassLoader() throws LogConfigurationException {
        ClassLoader classLoader = null;
        try {
            Method method = (class$java$lang$Thread == null ? (class$java$lang$Thread = LogFactory.class$("java.lang.Thread")) : class$java$lang$Thread).getMethod("getContextClassLoader", null);
            try {
                classLoader = (ClassLoader)method.invoke(Thread.currentThread(), null);
            }
            catch (IllegalAccessException e) {
                throw new LogConfigurationException("Unexpected IllegalAccessException", e);
            }
            catch (InvocationTargetException e) {
                if (!(e.getTargetException() instanceof SecurityException)) {
                    throw new LogConfigurationException("Unexpected InvocationTargetException", e.getTargetException());
                }
            }
        }
        catch (NoSuchMethodException e) {
            classLoader = LogFactory.getClassLoader(class$org$apache$commons$logging$LogFactory == null ? (class$org$apache$commons$logging$LogFactory = LogFactory.class$("org.apache.commons.logging.LogFactory")) : class$org$apache$commons$logging$LogFactory);
        }
        return classLoader;
    }

    private static LogFactory getCachedFactory(ClassLoader contextClassLoader) {
        if (contextClassLoader == null) {
            return nullClassLoaderFactory;
        }
        return (LogFactory)factories.get(contextClassLoader);
    }

    private static void cacheFactory(ClassLoader classLoader, LogFactory factory) {
        if (factory != null) {
            if (classLoader == null) {
                nullClassLoaderFactory = factory;
            } else {
                factories.put(classLoader, factory);
            }
        }
    }

    protected static LogFactory newFactory(final String factoryClass, final ClassLoader classLoader, ClassLoader contextClassLoader) throws LogConfigurationException {
        Object result = AccessController.doPrivileged(new PrivilegedAction(){

            public Object run() {
                return LogFactory.createFactory(factoryClass, classLoader);
            }
        });
        if (result instanceof LogConfigurationException) {
            LogConfigurationException ex = (LogConfigurationException)result;
            if (LogFactory.isDiagnosticsEnabled()) {
                LogFactory.logDiagnostic("An error occurred while loading the factory class:" + ex.getMessage());
            }
            throw ex;
        }
        if (LogFactory.isDiagnosticsEnabled()) {
            LogFactory.logDiagnostic("Created object " + LogFactory.objectId(result) + " to manage classloader " + LogFactory.objectId(contextClassLoader));
        }
        return (LogFactory)result;
    }

    protected static LogFactory newFactory(String factoryClass, ClassLoader classLoader) {
        return LogFactory.newFactory(factoryClass, classLoader, null);
    }

    protected static Object createFactory(String factoryClass, ClassLoader classLoader) {
        Class<?> logFactoryClass = null;
        try {
            block21: {
                if (classLoader != null) {
                    try {
                        logFactoryClass = classLoader.loadClass(factoryClass);
                        if ((class$org$apache$commons$logging$LogFactory == null ? (class$org$apache$commons$logging$LogFactory = LogFactory.class$("org.apache.commons.logging.LogFactory")) : class$org$apache$commons$logging$LogFactory).isAssignableFrom(logFactoryClass)) {
                            if (LogFactory.isDiagnosticsEnabled()) {
                                LogFactory.logDiagnostic("Loaded class " + logFactoryClass.getName() + " from classloader " + LogFactory.objectId(classLoader));
                            }
                        } else if (LogFactory.isDiagnosticsEnabled()) {
                            LogFactory.logDiagnostic("Factory class " + logFactoryClass.getName() + " loaded from classloader " + LogFactory.objectId(logFactoryClass.getClassLoader()) + " does not extend '" + (class$org$apache$commons$logging$LogFactory == null ? (class$org$apache$commons$logging$LogFactory = LogFactory.class$("org.apache.commons.logging.LogFactory")) : class$org$apache$commons$logging$LogFactory).getName() + "' as loaded by this classloader.");
                            LogFactory.logHierarchy("[BAD CL TREE] ", classLoader);
                        }
                        return (LogFactory)logFactoryClass.newInstance();
                    }
                    catch (ClassNotFoundException ex) {
                        if (classLoader == thisClassLoader) {
                            if (LogFactory.isDiagnosticsEnabled()) {
                                LogFactory.logDiagnostic("Unable to locate any class called '" + factoryClass + "' via classloader " + LogFactory.objectId(classLoader));
                            }
                            throw ex;
                        }
                    }
                    catch (NoClassDefFoundError e) {
                        if (classLoader == thisClassLoader) {
                            if (LogFactory.isDiagnosticsEnabled()) {
                                LogFactory.logDiagnostic("Class '" + factoryClass + "' cannot be loaded" + " via classloader " + LogFactory.objectId(classLoader) + " - it depends on some other class that cannot be found.");
                            }
                            throw e;
                        }
                    }
                    catch (ClassCastException e) {
                        if (classLoader != thisClassLoader) break block21;
                        boolean implementsLogFactory = LogFactory.implementsLogFactory(logFactoryClass);
                        StringBuffer msg = new StringBuffer();
                        msg.append("The application has specified that a custom LogFactory implementation ");
                        msg.append("should be used but Class '");
                        msg.append(factoryClass);
                        msg.append("' cannot be converted to '");
                        msg.append((class$org$apache$commons$logging$LogFactory == null ? (class$org$apache$commons$logging$LogFactory = LogFactory.class$("org.apache.commons.logging.LogFactory")) : class$org$apache$commons$logging$LogFactory).getName());
                        msg.append("'. ");
                        if (implementsLogFactory) {
                            msg.append("The conflict is caused by the presence of multiple LogFactory classes ");
                            msg.append("in incompatible classloaders. ");
                            msg.append("Background can be found in http://commons.apache.org/logging/tech.html. ");
                            msg.append("If you have not explicitly specified a custom LogFactory then it is likely ");
                            msg.append("that the container has set one without your knowledge. ");
                            msg.append("In this case, consider using the commons-logging-adapters.jar file or ");
                            msg.append("specifying the standard LogFactory from the command line. ");
                        } else {
                            msg.append("Please check the custom implementation. ");
                        }
                        msg.append("Help can be found @http://commons.apache.org/logging/troubleshooting.html.");
                        if (LogFactory.isDiagnosticsEnabled()) {
                            LogFactory.logDiagnostic(msg.toString());
                        }
                        throw new ClassCastException(msg.toString());
                    }
                }
            }
            if (LogFactory.isDiagnosticsEnabled()) {
                LogFactory.logDiagnostic("Unable to load factory class via classloader " + LogFactory.objectId(classLoader) + " - trying the classloader associated with this LogFactory.");
            }
            logFactoryClass = Class.forName(factoryClass);
            return (LogFactory)logFactoryClass.newInstance();
        }
        catch (Exception e) {
            if (LogFactory.isDiagnosticsEnabled()) {
                LogFactory.logDiagnostic("Unable to create LogFactory instance.");
            }
            if (logFactoryClass != null && !(class$org$apache$commons$logging$LogFactory == null ? (class$org$apache$commons$logging$LogFactory = LogFactory.class$("org.apache.commons.logging.LogFactory")) : class$org$apache$commons$logging$LogFactory).isAssignableFrom(logFactoryClass)) {
                return new LogConfigurationException("The chosen LogFactory implementation does not extend LogFactory. Please check your configuration.", e);
            }
            return new LogConfigurationException(e);
        }
    }

    private static boolean implementsLogFactory(Class logFactoryClass) {
        boolean implementsLogFactory = false;
        if (logFactoryClass != null) {
            try {
                ClassLoader logFactoryClassLoader = logFactoryClass.getClassLoader();
                if (logFactoryClassLoader == null) {
                    LogFactory.logDiagnostic("[CUSTOM LOG FACTORY] was loaded by the boot classloader");
                } else {
                    LogFactory.logHierarchy("[CUSTOM LOG FACTORY] ", logFactoryClassLoader);
                    Class<?> factoryFromCustomLoader = Class.forName("org.apache.commons.logging.LogFactory", false, logFactoryClassLoader);
                    implementsLogFactory = factoryFromCustomLoader.isAssignableFrom(logFactoryClass);
                    if (implementsLogFactory) {
                        LogFactory.logDiagnostic("[CUSTOM LOG FACTORY] " + logFactoryClass.getName() + " implements LogFactory but was loaded by an incompatible classloader.");
                    } else {
                        LogFactory.logDiagnostic("[CUSTOM LOG FACTORY] " + logFactoryClass.getName() + " does not implement LogFactory.");
                    }
                }
            }
            catch (SecurityException e) {
                LogFactory.logDiagnostic("[CUSTOM LOG FACTORY] SecurityException thrown whilst trying to determine whether the compatibility was caused by a classloader conflict: " + e.getMessage());
            }
            catch (LinkageError e) {
                LogFactory.logDiagnostic("[CUSTOM LOG FACTORY] LinkageError thrown whilst trying to determine whether the compatibility was caused by a classloader conflict: " + e.getMessage());
            }
            catch (ClassNotFoundException e) {
                LogFactory.logDiagnostic("[CUSTOM LOG FACTORY] LogFactory class cannot be loaded by classloader which loaded the custom LogFactory implementation. Is the custom factory in the right classloader?");
            }
        }
        return implementsLogFactory;
    }

    private static InputStream getResourceAsStream(final ClassLoader loader, final String name) {
        return (InputStream)AccessController.doPrivileged(new PrivilegedAction(){

            public Object run() {
                if (loader != null) {
                    return loader.getResourceAsStream(name);
                }
                return ClassLoader.getSystemResourceAsStream(name);
            }
        });
    }

    private static Enumeration getResources(final ClassLoader loader, final String name) {
        PrivilegedAction action = new PrivilegedAction(){

            public Object run() {
                try {
                    if (loader != null) {
                        return loader.getResources(name);
                    }
                    return ClassLoader.getSystemResources(name);
                }
                catch (IOException e) {
                    if (LogFactory.isDiagnosticsEnabled()) {
                        LogFactory.logDiagnostic("Exception while trying to find configuration file " + name + ":" + e.getMessage());
                    }
                    return null;
                }
                catch (NoSuchMethodError e) {
                    return null;
                }
            }
        };
        Object result = AccessController.doPrivileged(action);
        return (Enumeration)result;
    }

    private static Properties getProperties(final URL url) {
        PrivilegedAction action = new PrivilegedAction(){

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            public Object run() {
                InputStream stream = null;
                try {
                    URLConnection connection = url.openConnection();
                    connection.setUseCaches(false);
                    stream = connection.getInputStream();
                    if (stream != null) {
                        Properties props = new Properties();
                        props.load(stream);
                        stream.close();
                        stream = null;
                        Properties properties = props;
                        return properties;
                    }
                }
                catch (IOException e) {
                    if (LogFactory.isDiagnosticsEnabled()) {
                        LogFactory.logDiagnostic("Unable to read URL " + url);
                    }
                }
                finally {
                    block17: {
                        if (stream != null) {
                            try {
                                stream.close();
                            }
                            catch (IOException e) {
                                if (!LogFactory.isDiagnosticsEnabled()) break block17;
                                LogFactory.logDiagnostic("Unable to close stream for URL " + url);
                            }
                        }
                    }
                }
                return null;
            }
        };
        return (Properties)AccessController.doPrivileged(action);
    }

    private static final Properties getConfigurationFile(ClassLoader classLoader, String fileName) {
        URL propsUrl;
        Properties props;
        block12: {
            props = null;
            double priority = 0.0;
            propsUrl = null;
            try {
                Enumeration urls = LogFactory.getResources(classLoader, fileName);
                if (urls == null) {
                    return null;
                }
                while (urls.hasMoreElements()) {
                    URL url = (URL)urls.nextElement();
                    Properties newProps = LogFactory.getProperties(url);
                    if (newProps == null) continue;
                    if (props == null) {
                        propsUrl = url;
                        props = newProps;
                        String priorityStr = props.getProperty("priority");
                        priority = 0.0;
                        if (priorityStr != null) {
                            priority = Double.parseDouble(priorityStr);
                        }
                        if (!LogFactory.isDiagnosticsEnabled()) continue;
                        LogFactory.logDiagnostic("[LOOKUP] Properties file found at '" + url + "'" + " with priority " + priority);
                        continue;
                    }
                    String newPriorityStr = newProps.getProperty("priority");
                    double newPriority = 0.0;
                    if (newPriorityStr != null) {
                        newPriority = Double.parseDouble(newPriorityStr);
                    }
                    if (newPriority > priority) {
                        if (LogFactory.isDiagnosticsEnabled()) {
                            LogFactory.logDiagnostic("[LOOKUP] Properties file at '" + url + "'" + " with priority " + newPriority + " overrides file at '" + propsUrl + "'" + " with priority " + priority);
                        }
                        propsUrl = url;
                        props = newProps;
                        priority = newPriority;
                        continue;
                    }
                    if (!LogFactory.isDiagnosticsEnabled()) continue;
                    LogFactory.logDiagnostic("[LOOKUP] Properties file at '" + url + "'" + " with priority " + newPriority + " does not override file at '" + propsUrl + "'" + " with priority " + priority);
                }
            }
            catch (SecurityException e) {
                if (!LogFactory.isDiagnosticsEnabled()) break block12;
                LogFactory.logDiagnostic("SecurityException thrown while trying to find/read config files.");
            }
        }
        if (LogFactory.isDiagnosticsEnabled()) {
            if (props == null) {
                LogFactory.logDiagnostic("[LOOKUP] No properties file of name '" + fileName + "' found.");
            } else {
                LogFactory.logDiagnostic("[LOOKUP] Properties file of name '" + fileName + "' found at '" + propsUrl + '\"');
            }
        }
        return props;
    }

    private static String getSystemProperty(final String key, final String def) throws SecurityException {
        return (String)AccessController.doPrivileged(new PrivilegedAction(){

            public Object run() {
                return System.getProperty(key, def);
            }
        });
    }

    private static PrintStream initDiagnostics() {
        String dest;
        try {
            dest = LogFactory.getSystemProperty("org.apache.commons.logging.diagnostics.dest", null);
            if (dest == null) {
                return null;
            }
        }
        catch (SecurityException ex) {
            return null;
        }
        if (dest.equals("STDOUT")) {
            return System.out;
        }
        if (dest.equals("STDERR")) {
            return System.err;
        }
        try {
            FileOutputStream fos = new FileOutputStream(dest, true);
            return new PrintStream(fos);
        }
        catch (IOException ex) {
            return null;
        }
    }

    protected static boolean isDiagnosticsEnabled() {
        return diagnosticsStream != null;
    }

    private static final void logDiagnostic(String msg) {
        if (diagnosticsStream != null) {
            diagnosticsStream.print(diagnosticPrefix);
            diagnosticsStream.println(msg);
            diagnosticsStream.flush();
        }
    }

    protected static final void logRawDiagnostic(String msg) {
        if (diagnosticsStream != null) {
            diagnosticsStream.println(msg);
            diagnosticsStream.flush();
        }
    }

    private static void logClassLoaderEnvironment(Class clazz) {
        ClassLoader classLoader;
        if (!LogFactory.isDiagnosticsEnabled()) {
            return;
        }
        try {
            LogFactory.logDiagnostic("[ENV] Extension directories (java.ext.dir): " + System.getProperty("java.ext.dir"));
            LogFactory.logDiagnostic("[ENV] Application classpath (java.class.path): " + System.getProperty("java.class.path"));
        }
        catch (SecurityException ex) {
            LogFactory.logDiagnostic("[ENV] Security setting prevent interrogation of system classpaths.");
        }
        String className = clazz.getName();
        try {
            classLoader = LogFactory.getClassLoader(clazz);
        }
        catch (SecurityException ex) {
            LogFactory.logDiagnostic("[ENV] Security forbids determining the classloader for " + className);
            return;
        }
        LogFactory.logDiagnostic("[ENV] Class " + className + " was loaded via classloader " + LogFactory.objectId(classLoader));
        LogFactory.logHierarchy("[ENV] Ancestry of classloader which loaded " + className + " is ", classLoader);
    }

    private static void logHierarchy(String prefix, ClassLoader classLoader) {
        ClassLoader systemClassLoader;
        if (!LogFactory.isDiagnosticsEnabled()) {
            return;
        }
        if (classLoader != null) {
            String classLoaderString = classLoader.toString();
            LogFactory.logDiagnostic(prefix + LogFactory.objectId(classLoader) + " == '" + classLoaderString + "'");
        }
        try {
            systemClassLoader = ClassLoader.getSystemClassLoader();
        }
        catch (SecurityException ex) {
            LogFactory.logDiagnostic(prefix + "Security forbids determining the system classloader.");
            return;
        }
        if (classLoader != null) {
            StringBuffer buf;
            block9: {
                buf = new StringBuffer(prefix + "ClassLoader tree:");
                do {
                    buf.append(LogFactory.objectId(classLoader));
                    if (classLoader == systemClassLoader) {
                        buf.append(" (SYSTEM) ");
                    }
                    try {
                        classLoader = classLoader.getParent();
                    }
                    catch (SecurityException ex) {
                        buf.append(" --> SECRET");
                        break block9;
                    }
                    buf.append(" --> ");
                } while (classLoader != null);
                buf.append("BOOT");
            }
            LogFactory.logDiagnostic(buf.toString());
        }
    }

    public static String objectId(Object o) {
        if (o == null) {
            return "null";
        }
        return o.getClass().getName() + "@" + System.identityHashCode(o);
    }

    static /* synthetic */ Class class$(String x0) {
        try {
            return Class.forName(x0);
        }
        catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        String classLoaderName;
        diagnosticsStream = null;
        factories = null;
        nullClassLoaderFactory = null;
        thisClassLoader = LogFactory.getClassLoader(class$org$apache$commons$logging$LogFactory == null ? (class$org$apache$commons$logging$LogFactory = LogFactory.class$("org.apache.commons.logging.LogFactory")) : class$org$apache$commons$logging$LogFactory);
        try {
            ClassLoader classLoader = thisClassLoader;
            classLoaderName = thisClassLoader == null ? "BOOTLOADER" : LogFactory.objectId(classLoader);
        }
        catch (SecurityException e) {
            classLoaderName = "UNKNOWN";
        }
        diagnosticPrefix = "[LogFactory from " + classLoaderName + "] ";
        diagnosticsStream = LogFactory.initDiagnostics();
        LogFactory.logClassLoaderEnvironment(class$org$apache$commons$logging$LogFactory == null ? (class$org$apache$commons$logging$LogFactory = LogFactory.class$("org.apache.commons.logging.LogFactory")) : class$org$apache$commons$logging$LogFactory);
        factories = LogFactory.createFactoryStore();
        if (LogFactory.isDiagnosticsEnabled()) {
            LogFactory.logDiagnostic("BOOTSTRAP COMPLETED");
        }
    }
}

