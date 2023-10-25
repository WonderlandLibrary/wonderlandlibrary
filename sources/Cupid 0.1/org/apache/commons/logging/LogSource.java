package org.apache.commons.logging;

import java.lang.reflect.Constructor;
import java.util.Hashtable;
import org.apache.commons.logging.impl.NoOpLog;

public class LogSource {
  protected static Hashtable logs = new Hashtable();
  
  protected static boolean log4jIsAvailable = false;
  
  protected static boolean jdk14IsAvailable = false;
  
  protected static Constructor logImplctor = null;
  
  static {
    try {
      log4jIsAvailable = (null != Class.forName("org.apache.log4j.Logger"));
    } catch (Throwable t) {
      log4jIsAvailable = false;
    } 
    try {
      jdk14IsAvailable = (null != Class.forName("java.util.logging.Logger") && null != Class.forName("org.apache.commons.logging.impl.Jdk14Logger"));
    } catch (Throwable t) {
      jdk14IsAvailable = false;
    } 
    String name = null;
    try {
      name = System.getProperty("org.apache.commons.logging.log");
      if (name == null)
        name = System.getProperty("org.apache.commons.logging.Log"); 
    } catch (Throwable t) {}
    if (name != null) {
      try {
        setLogImplementation(name);
      } catch (Throwable t) {
        try {
          setLogImplementation("org.apache.commons.logging.impl.NoOpLog");
        } catch (Throwable u) {}
      } 
    } else {
      try {
        if (log4jIsAvailable) {
          setLogImplementation("org.apache.commons.logging.impl.Log4JLogger");
        } else if (jdk14IsAvailable) {
          setLogImplementation("org.apache.commons.logging.impl.Jdk14Logger");
        } else {
          setLogImplementation("org.apache.commons.logging.impl.NoOpLog");
        } 
      } catch (Throwable t) {
        try {
          setLogImplementation("org.apache.commons.logging.impl.NoOpLog");
        } catch (Throwable u) {}
      } 
    } 
  }
  
  public static void setLogImplementation(String classname) throws LinkageError, NoSuchMethodException, SecurityException, ClassNotFoundException {
    try {
      Class logclass = Class.forName(classname);
      Class[] argtypes = new Class[1];
      argtypes[0] = "".getClass();
      logImplctor = logclass.getConstructor(argtypes);
    } catch (Throwable t) {
      logImplctor = null;
    } 
  }
  
  public static void setLogImplementation(Class logclass) throws LinkageError, ExceptionInInitializerError, NoSuchMethodException, SecurityException {
    Class[] argtypes = new Class[1];
    argtypes[0] = "".getClass();
    logImplctor = logclass.getConstructor(argtypes);
  }
  
  public static Log getInstance(String name) {
    Log log = (Log)logs.get(name);
    if (null == log) {
      log = makeNewLogInstance(name);
      logs.put(name, log);
    } 
    return log;
  }
  
  public static Log getInstance(Class clazz) {
    return getInstance(clazz.getName());
  }
  
  public static Log makeNewLogInstance(String name) {
    NoOpLog noOpLog;
    try {
      Object[] args = { name };
      noOpLog = (NoOpLog)logImplctor.newInstance(args);
    } catch (Throwable t) {
      noOpLog = null;
    } 
    if (null == noOpLog)
      noOpLog = new NoOpLog(name); 
    return (Log)noOpLog;
  }
  
  public static String[] getLogNames() {
    return (String[])logs.keySet().toArray((Object[])new String[logs.size()]);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\logging\LogSource.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */