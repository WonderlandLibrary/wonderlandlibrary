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

public abstract class LogFactory {
  public static final String PRIORITY_KEY = "priority";
  
  public static final String TCCL_KEY = "use_tccl";
  
  public static final String FACTORY_PROPERTY = "org.apache.commons.logging.LogFactory";
  
  public static final String FACTORY_DEFAULT = "org.apache.commons.logging.impl.LogFactoryImpl";
  
  public static final String FACTORY_PROPERTIES = "commons-logging.properties";
  
  protected static final String SERVICE_ID = "META-INF/services/org.apache.commons.logging.LogFactory";
  
  public static final String DIAGNOSTICS_DEST_PROPERTY = "org.apache.commons.logging.diagnostics.dest";
  
  static {
    String str;
  }
  
  private static PrintStream diagnosticsStream = null;
  
  private static final String diagnosticPrefix;
  
  public static final String HASHTABLE_IMPLEMENTATION_PROPERTY = "org.apache.commons.logging.LogFactory.HashtableImpl";
  
  private static final String WEAK_HASHTABLE_CLASSNAME = "org.apache.commons.logging.impl.WeakHashtable";
  
  private static final ClassLoader thisClassLoader;
  
  protected static Hashtable factories = null;
  
  protected static volatile LogFactory nullClassLoaderFactory = null;
  
  private static final Hashtable createFactoryStore() {
    String str;
    Hashtable result = null;
    try {
      str = getSystemProperty("org.apache.commons.logging.LogFactory.HashtableImpl", null);
    } catch (SecurityException ex) {
      str = null;
    } 
    if (str == null)
      str = "org.apache.commons.logging.impl.WeakHashtable"; 
    try {
      Class implementationClass = Class.forName(str);
      result = (Hashtable)implementationClass.newInstance();
    } catch (Throwable t) {
      handleThrowable(t);
      if (!"org.apache.commons.logging.impl.WeakHashtable".equals(str))
        if (isDiagnosticsEnabled()) {
          logDiagnostic("[ERROR] LogFactory: Load of custom hashtable failed");
        } else {
          System.err.println("[ERROR] LogFactory: Load of custom hashtable failed");
        }  
    } 
    if (result == null)
      result = new Hashtable(); 
    return result;
  }
  
  private static String trim(String src) {
    if (src == null)
      return null; 
    return src.trim();
  }
  
  protected static void handleThrowable(Throwable t) {
    if (t instanceof ThreadDeath)
      throw (ThreadDeath)t; 
    if (t instanceof VirtualMachineError)
      throw (VirtualMachineError)t; 
  }
  
  public static LogFactory getFactory() throws LogConfigurationException {
    ClassLoader contextClassLoader = getContextClassLoaderInternal();
    if (contextClassLoader == null)
      if (isDiagnosticsEnabled())
        logDiagnostic("Context classloader is null.");  
    LogFactory factory = getCachedFactory(contextClassLoader);
    if (factory != null)
      return factory; 
    if (isDiagnosticsEnabled()) {
      logDiagnostic("[LOOKUP] LogFactory implementation requested for the first time for context classloader " + objectId(contextClassLoader));
      logHierarchy("[LOOKUP] ", contextClassLoader);
    } 
    Properties props = getConfigurationFile(contextClassLoader, "commons-logging.properties");
    ClassLoader baseClassLoader = contextClassLoader;
    if (props != null) {
      String useTCCLStr = props.getProperty("use_tccl");
      if (useTCCLStr != null)
        if (!Boolean.valueOf(useTCCLStr).booleanValue())
          baseClassLoader = thisClassLoader;  
    } 
    if (isDiagnosticsEnabled())
      logDiagnostic("[LOOKUP] Looking for system property [org.apache.commons.logging.LogFactory] to define the LogFactory subclass to use..."); 
    try {
      String factoryClass = getSystemProperty("org.apache.commons.logging.LogFactory", null);
      if (factoryClass != null) {
        if (isDiagnosticsEnabled())
          logDiagnostic("[LOOKUP] Creating an instance of LogFactory class '" + factoryClass + "' as specified by system property " + "org.apache.commons.logging.LogFactory"); 
        factory = newFactory(factoryClass, baseClassLoader, contextClassLoader);
      } else if (isDiagnosticsEnabled()) {
        logDiagnostic("[LOOKUP] No system property [org.apache.commons.logging.LogFactory] defined.");
      } 
    } catch (SecurityException e) {
      if (isDiagnosticsEnabled())
        logDiagnostic("[LOOKUP] A security exception occurred while trying to create an instance of the custom factory class: [" + trim(e.getMessage()) + "]. Trying alternative implementations..."); 
    } catch (RuntimeException e) {
      if (isDiagnosticsEnabled())
        logDiagnostic("[LOOKUP] An exception occurred while trying to create an instance of the custom factory class: [" + trim(e.getMessage()) + "] as specified by a system property."); 
      throw e;
    } 
    if (factory == null) {
      if (isDiagnosticsEnabled())
        logDiagnostic("[LOOKUP] Looking for a resource file of name [META-INF/services/org.apache.commons.logging.LogFactory] to define the LogFactory subclass to use..."); 
      try {
        InputStream is = getResourceAsStream(contextClassLoader, "META-INF/services/org.apache.commons.logging.LogFactory");
        if (is != null) {
          BufferedReader bufferedReader;
          try {
            bufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
          } catch (UnsupportedEncodingException e) {
            bufferedReader = new BufferedReader(new InputStreamReader(is));
          } 
          String factoryClassName = bufferedReader.readLine();
          bufferedReader.close();
          if (factoryClassName != null && !"".equals(factoryClassName)) {
            if (isDiagnosticsEnabled())
              logDiagnostic("[LOOKUP]  Creating an instance of LogFactory class " + factoryClassName + " as specified by file '" + "META-INF/services/org.apache.commons.logging.LogFactory" + "' which was present in the path of the context classloader."); 
            factory = newFactory(factoryClassName, baseClassLoader, contextClassLoader);
          } 
        } else if (isDiagnosticsEnabled()) {
          logDiagnostic("[LOOKUP] No resource file with name 'META-INF/services/org.apache.commons.logging.LogFactory' found.");
        } 
      } catch (Exception ex) {
        if (isDiagnosticsEnabled())
          logDiagnostic("[LOOKUP] A security exception occurred while trying to create an instance of the custom factory class: [" + trim(ex.getMessage()) + "]. Trying alternative implementations..."); 
      } 
    } 
    if (factory == null)
      if (props != null) {
        if (isDiagnosticsEnabled())
          logDiagnostic("[LOOKUP] Looking in properties file for entry with key 'org.apache.commons.logging.LogFactory' to define the LogFactory subclass to use..."); 
        String factoryClass = props.getProperty("org.apache.commons.logging.LogFactory");
        if (factoryClass != null) {
          if (isDiagnosticsEnabled())
            logDiagnostic("[LOOKUP] Properties file specifies LogFactory subclass '" + factoryClass + "'"); 
          factory = newFactory(factoryClass, baseClassLoader, contextClassLoader);
        } else if (isDiagnosticsEnabled()) {
          logDiagnostic("[LOOKUP] Properties file has no entry specifying LogFactory subclass.");
        } 
      } else if (isDiagnosticsEnabled()) {
        logDiagnostic("[LOOKUP] No properties file available to determine LogFactory subclass from..");
      }  
    if (factory == null) {
      if (isDiagnosticsEnabled())
        logDiagnostic("[LOOKUP] Loading the default LogFactory implementation 'org.apache.commons.logging.impl.LogFactoryImpl' via the same classloader that loaded this LogFactory class (ie not looking in the context classloader)."); 
      factory = newFactory("org.apache.commons.logging.impl.LogFactoryImpl", thisClassLoader, contextClassLoader);
    } 
    if (factory != null) {
      cacheFactory(contextClassLoader, factory);
      if (props != null) {
        Enumeration names = props.propertyNames();
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
    return getFactory().getInstance(clazz);
  }
  
  public static Log getLog(String name) throws LogConfigurationException {
    return getFactory().getInstance(name);
  }
  
  public static void release(ClassLoader classLoader) {
    if (isDiagnosticsEnabled())
      logDiagnostic("Releasing factory for classloader " + objectId(classLoader)); 
    Hashtable factories = LogFactory.factories;
    synchronized (factories) {
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
  
  public static void releaseAll() {
    if (isDiagnosticsEnabled())
      logDiagnostic("Releasing factory for all classloaders."); 
    Hashtable factories = LogFactory.factories;
    synchronized (factories) {
      Enumeration elements = factories.elements();
      while (elements.hasMoreElements()) {
        LogFactory element = elements.nextElement();
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
    } catch (SecurityException ex) {
      if (isDiagnosticsEnabled())
        logDiagnostic("Unable to get classloader for class '" + clazz + "' due to security restrictions - " + ex.getMessage()); 
      throw ex;
    } 
  }
  
  protected static ClassLoader getContextClassLoader() throws LogConfigurationException {
    return directGetContextClassLoader();
  }
  
  private static ClassLoader getContextClassLoaderInternal() throws LogConfigurationException {
    return AccessController.<ClassLoader>doPrivileged(new PrivilegedAction() {
          public Object run() {
            return LogFactory.directGetContextClassLoader();
          }
        });
  }
  
  protected static ClassLoader directGetContextClassLoader() throws LogConfigurationException {
    ClassLoader classLoader = null;
    try {
      Method method = Thread.class.getMethod("getContextClassLoader", (Class[])null);
      try {
        classLoader = (ClassLoader)method.invoke(Thread.currentThread(), (Object[])null);
      } catch (IllegalAccessException e) {
        throw new LogConfigurationException("Unexpected IllegalAccessException", e);
      } catch (InvocationTargetException e) {
        if (!(e.getTargetException() instanceof SecurityException))
          throw new LogConfigurationException("Unexpected InvocationTargetException", e.getTargetException()); 
      } 
    } catch (NoSuchMethodException e) {
      classLoader = getClassLoader(LogFactory.class);
    } 
    return classLoader;
  }
  
  private static LogFactory getCachedFactory(ClassLoader contextClassLoader) {
    if (contextClassLoader == null)
      return nullClassLoaderFactory; 
    return (LogFactory)factories.get(contextClassLoader);
  }
  
  private static void cacheFactory(ClassLoader classLoader, LogFactory factory) {
    if (factory != null)
      if (classLoader == null) {
        nullClassLoaderFactory = factory;
      } else {
        factories.put(classLoader, factory);
      }  
  }
  
  protected static LogFactory newFactory(String factoryClass, ClassLoader classLoader, ClassLoader contextClassLoader) throws LogConfigurationException {
    Object result = AccessController.doPrivileged(new PrivilegedAction(factoryClass, classLoader) {
          private final String val$factoryClass;
          
          private final ClassLoader val$classLoader;
          
          public Object run() {
            return LogFactory.createFactory(this.val$factoryClass, this.val$classLoader);
          }
        });
    if (result instanceof LogConfigurationException) {
      LogConfigurationException ex = (LogConfigurationException)result;
      if (isDiagnosticsEnabled())
        logDiagnostic("An error occurred while loading the factory class:" + ex.getMessage()); 
      throw ex;
    } 
    if (isDiagnosticsEnabled())
      logDiagnostic("Created object " + objectId(result) + " to manage classloader " + objectId(contextClassLoader)); 
    return (LogFactory)result;
  }
  
  protected static LogFactory newFactory(String factoryClass, ClassLoader classLoader) {
    return newFactory(factoryClass, classLoader, null);
  }
  
  protected static Object createFactory(String factoryClass, ClassLoader classLoader) {
    Class logFactoryClass = null;
    try {
      if (classLoader != null)
        try {
          logFactoryClass = classLoader.loadClass(factoryClass);
          if (LogFactory.class.isAssignableFrom(logFactoryClass)) {
            if (isDiagnosticsEnabled())
              logDiagnostic("Loaded class " + logFactoryClass.getName() + " from classloader " + objectId(classLoader)); 
          } else if (isDiagnosticsEnabled()) {
            logDiagnostic("Factory class " + logFactoryClass.getName() + " loaded from classloader " + objectId(logFactoryClass.getClassLoader()) + " does not extend '" + LogFactory.class.getName() + "' as loaded by this classloader.");
            logHierarchy("[BAD CL TREE] ", classLoader);
          } 
          return logFactoryClass.newInstance();
        } catch (ClassNotFoundException ex) {
          if (classLoader == thisClassLoader) {
            if (isDiagnosticsEnabled())
              logDiagnostic("Unable to locate any class called '" + factoryClass + "' via classloader " + objectId(classLoader)); 
            throw ex;
          } 
        } catch (NoClassDefFoundError e) {
          if (classLoader == thisClassLoader) {
            if (isDiagnosticsEnabled())
              logDiagnostic("Class '" + factoryClass + "' cannot be loaded" + " via classloader " + objectId(classLoader) + " - it depends on some other class that cannot be found."); 
            throw e;
          } 
        } catch (ClassCastException e) {
          if (classLoader == thisClassLoader) {
            boolean implementsLogFactory = implementsLogFactory(logFactoryClass);
            StringBuffer msg = new StringBuffer();
            msg.append("The application has specified that a custom LogFactory implementation ");
            msg.append("should be used but Class '");
            msg.append(factoryClass);
            msg.append("' cannot be converted to '");
            msg.append(LogFactory.class.getName());
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
            if (isDiagnosticsEnabled())
              logDiagnostic(msg.toString()); 
            throw new ClassCastException(msg.toString());
          } 
        }  
      if (isDiagnosticsEnabled())
        logDiagnostic("Unable to load factory class via classloader " + objectId(classLoader) + " - trying the classloader associated with this LogFactory."); 
      logFactoryClass = Class.forName(factoryClass);
      return logFactoryClass.newInstance();
    } catch (Exception e) {
      if (isDiagnosticsEnabled())
        logDiagnostic("Unable to create LogFactory instance."); 
      if (logFactoryClass != null && !LogFactory.class.isAssignableFrom(logFactoryClass))
        return new LogConfigurationException("The chosen LogFactory implementation does not extend LogFactory. Please check your configuration.", e); 
      return new LogConfigurationException(e);
    } 
  }
  
  private static boolean implementsLogFactory(Class logFactoryClass) {
    boolean implementsLogFactory = false;
    if (logFactoryClass != null)
      try {
        ClassLoader logFactoryClassLoader = logFactoryClass.getClassLoader();
        if (logFactoryClassLoader == null) {
          logDiagnostic("[CUSTOM LOG FACTORY] was loaded by the boot classloader");
        } else {
          logHierarchy("[CUSTOM LOG FACTORY] ", logFactoryClassLoader);
          Class factoryFromCustomLoader = Class.forName("org.apache.commons.logging.LogFactory", false, logFactoryClassLoader);
          implementsLogFactory = factoryFromCustomLoader.isAssignableFrom(logFactoryClass);
          if (implementsLogFactory) {
            logDiagnostic("[CUSTOM LOG FACTORY] " + logFactoryClass.getName() + " implements LogFactory but was loaded by an incompatible classloader.");
          } else {
            logDiagnostic("[CUSTOM LOG FACTORY] " + logFactoryClass.getName() + " does not implement LogFactory.");
          } 
        } 
      } catch (SecurityException e) {
        logDiagnostic("[CUSTOM LOG FACTORY] SecurityException thrown whilst trying to determine whether the compatibility was caused by a classloader conflict: " + e.getMessage());
      } catch (LinkageError e) {
        logDiagnostic("[CUSTOM LOG FACTORY] LinkageError thrown whilst trying to determine whether the compatibility was caused by a classloader conflict: " + e.getMessage());
      } catch (ClassNotFoundException e) {
        logDiagnostic("[CUSTOM LOG FACTORY] LogFactory class cannot be loaded by classloader which loaded the custom LogFactory implementation. Is the custom factory in the right classloader?");
      }  
    return implementsLogFactory;
  }
  
  private static InputStream getResourceAsStream(ClassLoader loader, String name) {
    return AccessController.<InputStream>doPrivileged(new PrivilegedAction(loader, name) {
          private final ClassLoader val$loader;
          
          private final String val$name;
          
          public Object run() {
            if (this.val$loader != null)
              return this.val$loader.getResourceAsStream(this.val$name); 
            return ClassLoader.getSystemResourceAsStream(this.val$name);
          }
        });
  }
  
  private static Enumeration getResources(ClassLoader loader, String name) {
    PrivilegedAction action = new PrivilegedAction(loader, name) {
        private final ClassLoader val$loader;
        
        private final String val$name;
        
        public Object run() {
          try {
            if (this.val$loader != null)
              return this.val$loader.getResources(this.val$name); 
            return ClassLoader.getSystemResources(this.val$name);
          } catch (IOException e) {
            if (LogFactory.isDiagnosticsEnabled())
              LogFactory.logDiagnostic("Exception while trying to find configuration file " + this.val$name + ":" + e.getMessage()); 
            return null;
          } catch (NoSuchMethodError e) {
            return null;
          } 
        }
      };
    Object result = AccessController.doPrivileged(action);
    return (Enumeration)result;
  }
  
  private static Properties getProperties(URL url) {
    PrivilegedAction action = new PrivilegedAction(url) {
        private final URL val$url;
        
        public Object run() {
          InputStream stream = null;
          try {
            URLConnection connection = this.val$url.openConnection();
            connection.setUseCaches(false);
            stream = connection.getInputStream();
            if (stream != null) {
              Properties props = new Properties();
              props.load(stream);
              stream.close();
              stream = null;
              return props;
            } 
          } catch (IOException e) {
            if (LogFactory.isDiagnosticsEnabled())
              LogFactory.logDiagnostic("Unable to read URL " + this.val$url); 
          } finally {
            if (stream != null)
              try {
                stream.close();
              } catch (IOException e) {
                if (LogFactory.isDiagnosticsEnabled())
                  LogFactory.logDiagnostic("Unable to close stream for URL " + this.val$url); 
              }  
          } 
          return null;
        }
      };
    return AccessController.<Properties>doPrivileged(action);
  }
  
  private static final Properties getConfigurationFile(ClassLoader classLoader, String fileName) {
    Properties props = null;
    double priority = 0.0D;
    URL propsUrl = null;
    try {
      Enumeration urls = getResources(classLoader, fileName);
      if (urls == null)
        return null; 
      while (urls.hasMoreElements()) {
        URL url = urls.nextElement();
        Properties newProps = getProperties(url);
        if (newProps != null) {
          if (props == null) {
            propsUrl = url;
            props = newProps;
            String priorityStr = props.getProperty("priority");
            priority = 0.0D;
            if (priorityStr != null)
              priority = Double.parseDouble(priorityStr); 
            if (isDiagnosticsEnabled())
              logDiagnostic("[LOOKUP] Properties file found at '" + url + "'" + " with priority " + priority); 
            continue;
          } 
          String newPriorityStr = newProps.getProperty("priority");
          double newPriority = 0.0D;
          if (newPriorityStr != null)
            newPriority = Double.parseDouble(newPriorityStr); 
          if (newPriority > priority) {
            if (isDiagnosticsEnabled())
              logDiagnostic("[LOOKUP] Properties file at '" + url + "'" + " with priority " + newPriority + " overrides file at '" + propsUrl + "'" + " with priority " + priority); 
            propsUrl = url;
            props = newProps;
            priority = newPriority;
            continue;
          } 
          if (isDiagnosticsEnabled())
            logDiagnostic("[LOOKUP] Properties file at '" + url + "'" + " with priority " + newPriority + " does not override file at '" + propsUrl + "'" + " with priority " + priority); 
        } 
      } 
    } catch (SecurityException e) {
      if (isDiagnosticsEnabled())
        logDiagnostic("SecurityException thrown while trying to find/read config files."); 
    } 
    if (isDiagnosticsEnabled())
      if (props == null) {
        logDiagnostic("[LOOKUP] No properties file of name '" + fileName + "' found.");
      } else {
        logDiagnostic("[LOOKUP] Properties file of name '" + fileName + "' found at '" + propsUrl + '"');
      }  
    return props;
  }
  
  private static String getSystemProperty(String key, String def) throws SecurityException {
    return AccessController.<String>doPrivileged(new PrivilegedAction(key, def) {
          private final String val$key;
          
          private final String val$def;
          
          public Object run() {
            return System.getProperty(this.val$key, this.val$def);
          }
        });
  }
  
  private static PrintStream initDiagnostics() {
    String dest;
    try {
      dest = getSystemProperty("org.apache.commons.logging.diagnostics.dest", null);
      if (dest == null)
        return null; 
    } catch (SecurityException ex) {
      return null;
    } 
    if (dest.equals("STDOUT"))
      return System.out; 
    if (dest.equals("STDERR"))
      return System.err; 
    try {
      FileOutputStream fos = new FileOutputStream(dest, true);
      return new PrintStream(fos);
    } catch (IOException ex) {
      return null;
    } 
  }
  
  protected static boolean isDiagnosticsEnabled() {
    return (diagnosticsStream != null);
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
    if (!isDiagnosticsEnabled())
      return; 
    try {
      logDiagnostic("[ENV] Extension directories (java.ext.dir): " + System.getProperty("java.ext.dir"));
      logDiagnostic("[ENV] Application classpath (java.class.path): " + System.getProperty("java.class.path"));
    } catch (SecurityException ex) {
      logDiagnostic("[ENV] Security setting prevent interrogation of system classpaths.");
    } 
    String className = clazz.getName();
    try {
      classLoader = getClassLoader(clazz);
    } catch (SecurityException ex) {
      logDiagnostic("[ENV] Security forbids determining the classloader for " + className);
      return;
    } 
    logDiagnostic("[ENV] Class " + className + " was loaded via classloader " + objectId(classLoader));
    logHierarchy("[ENV] Ancestry of classloader which loaded " + className + " is ", classLoader);
  }
  
  private static void logHierarchy(String prefix, ClassLoader classLoader) {
    ClassLoader systemClassLoader;
    if (!isDiagnosticsEnabled())
      return; 
    if (classLoader != null) {
      String classLoaderString = classLoader.toString();
      logDiagnostic(prefix + objectId(classLoader) + " == '" + classLoaderString + "'");
    } 
    try {
      systemClassLoader = ClassLoader.getSystemClassLoader();
    } catch (SecurityException ex) {
      logDiagnostic(prefix + "Security forbids determining the system classloader.");
      return;
    } 
    if (classLoader != null) {
      StringBuffer buf = new StringBuffer(prefix + "ClassLoader tree:");
      while (true) {
        buf.append(objectId(classLoader));
        if (classLoader == systemClassLoader)
          buf.append(" (SYSTEM) "); 
        try {
          classLoader = classLoader.getParent();
        } catch (SecurityException ex) {
          buf.append(" --> SECRET");
          break;
        } 
        buf.append(" --> ");
        if (classLoader == null) {
          buf.append("BOOT");
          break;
        } 
      } 
      logDiagnostic(buf.toString());
    } 
  }
  
  public static String objectId(Object o) {
    if (o == null)
      return "null"; 
    return o.getClass().getName() + "@" + System.identityHashCode(o);
  }
  
  static {
    thisClassLoader = getClassLoader(LogFactory.class);
    try {
      ClassLoader classLoader = thisClassLoader;
      if (thisClassLoader == null) {
        str = "BOOTLOADER";
      } else {
        str = objectId(classLoader);
      } 
    } catch (SecurityException e) {
      str = "UNKNOWN";
    } 
    diagnosticPrefix = "[LogFactory from " + str + "] ";
    diagnosticsStream = initDiagnostics();
    logClassLoaderEnvironment(LogFactory.class);
    factories = createFactoryStore();
    if (isDiagnosticsEnabled())
      logDiagnostic("BOOTSTRAP COMPLETED"); 
  }
  
  public abstract Object getAttribute(String paramString);
  
  public abstract String[] getAttributeNames();
  
  public abstract Log getInstance(Class paramClass) throws LogConfigurationException;
  
  public abstract Log getInstance(String paramString) throws LogConfigurationException;
  
  public abstract void release();
  
  public abstract void removeAttribute(String paramString);
  
  public abstract void setAttribute(String paramString, Object paramObject);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\logging\LogFactory.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */