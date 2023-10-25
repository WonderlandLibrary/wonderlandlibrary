package org.apache.commons.logging.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;

public class SimpleLog implements Log, Serializable {
  private static final long serialVersionUID = 136942970684951178L;
  
  protected static final String systemPrefix = "org.apache.commons.logging.simplelog.";
  
  protected static final Properties simpleLogProps = new Properties();
  
  protected static final String DEFAULT_DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss:SSS zzz";
  
  protected static volatile boolean showLogName = false;
  
  protected static volatile boolean showShortName = true;
  
  protected static volatile boolean showDateTime = false;
  
  protected static volatile String dateTimeFormat = "yyyy/MM/dd HH:mm:ss:SSS zzz";
  
  protected static DateFormat dateFormatter = null;
  
  public static final int LOG_LEVEL_TRACE = 1;
  
  public static final int LOG_LEVEL_DEBUG = 2;
  
  public static final int LOG_LEVEL_INFO = 3;
  
  public static final int LOG_LEVEL_WARN = 4;
  
  public static final int LOG_LEVEL_ERROR = 5;
  
  public static final int LOG_LEVEL_FATAL = 6;
  
  public static final int LOG_LEVEL_ALL = 0;
  
  public static final int LOG_LEVEL_OFF = 7;
  
  private static String getStringProperty(String name) {
    String prop = null;
    try {
      prop = System.getProperty(name);
    } catch (SecurityException e) {}
    return (prop == null) ? simpleLogProps.getProperty(name) : prop;
  }
  
  private static String getStringProperty(String name, String dephault) {
    String prop = getStringProperty(name);
    return (prop == null) ? dephault : prop;
  }
  
  private static boolean getBooleanProperty(String name, boolean dephault) {
    String prop = getStringProperty(name);
    return (prop == null) ? dephault : "true".equalsIgnoreCase(prop);
  }
  
  static {
    InputStream in = getResourceAsStream("simplelog.properties");
    if (null != in)
      try {
        simpleLogProps.load(in);
        in.close();
      } catch (IOException e) {} 
    showLogName = getBooleanProperty("org.apache.commons.logging.simplelog.showlogname", showLogName);
    showShortName = getBooleanProperty("org.apache.commons.logging.simplelog.showShortLogname", showShortName);
    showDateTime = getBooleanProperty("org.apache.commons.logging.simplelog.showdatetime", showDateTime);
    if (showDateTime) {
      dateTimeFormat = getStringProperty("org.apache.commons.logging.simplelog.dateTimeFormat", dateTimeFormat);
      try {
        dateFormatter = new SimpleDateFormat(dateTimeFormat);
      } catch (IllegalArgumentException e) {
        dateTimeFormat = "yyyy/MM/dd HH:mm:ss:SSS zzz";
        dateFormatter = new SimpleDateFormat(dateTimeFormat);
      } 
    } 
  }
  
  protected volatile String logName = null;
  
  protected volatile int currentLogLevel;
  
  private volatile String shortLogName = null;
  
  public SimpleLog(String name) {
    this.logName = name;
    setLevel(3);
    String lvl = getStringProperty("org.apache.commons.logging.simplelog.log." + this.logName);
    int i = String.valueOf(name).lastIndexOf(".");
    while (null == lvl && i > -1) {
      name = name.substring(0, i);
      lvl = getStringProperty("org.apache.commons.logging.simplelog.log." + name);
      i = String.valueOf(name).lastIndexOf(".");
    } 
    if (null == lvl)
      lvl = getStringProperty("org.apache.commons.logging.simplelog.defaultlog"); 
    if ("all".equalsIgnoreCase(lvl)) {
      setLevel(0);
    } else if ("trace".equalsIgnoreCase(lvl)) {
      setLevel(1);
    } else if ("debug".equalsIgnoreCase(lvl)) {
      setLevel(2);
    } else if ("info".equalsIgnoreCase(lvl)) {
      setLevel(3);
    } else if ("warn".equalsIgnoreCase(lvl)) {
      setLevel(4);
    } else if ("error".equalsIgnoreCase(lvl)) {
      setLevel(5);
    } else if ("fatal".equalsIgnoreCase(lvl)) {
      setLevel(6);
    } else if ("off".equalsIgnoreCase(lvl)) {
      setLevel(7);
    } 
  }
  
  public void setLevel(int currentLogLevel) {
    this.currentLogLevel = currentLogLevel;
  }
  
  public int getLevel() {
    return this.currentLogLevel;
  }
  
  protected void log(int type, Object message, Throwable t) {
    StringBuffer buf = new StringBuffer();
    if (showDateTime) {
      String dateText;
      Date now = new Date();
      synchronized (dateFormatter) {
        dateText = dateFormatter.format(now);
      } 
      buf.append(dateText);
      buf.append(" ");
    } 
    switch (type) {
      case 1:
        buf.append("[TRACE] ");
        break;
      case 2:
        buf.append("[DEBUG] ");
        break;
      case 3:
        buf.append("[INFO] ");
        break;
      case 4:
        buf.append("[WARN] ");
        break;
      case 5:
        buf.append("[ERROR] ");
        break;
      case 6:
        buf.append("[FATAL] ");
        break;
    } 
    if (showShortName) {
      if (this.shortLogName == null) {
        String slName = this.logName.substring(this.logName.lastIndexOf(".") + 1);
        this.shortLogName = slName.substring(slName.lastIndexOf("/") + 1);
      } 
      buf.append(String.valueOf(this.shortLogName)).append(" - ");
    } else if (showLogName) {
      buf.append(String.valueOf(this.logName)).append(" - ");
    } 
    buf.append(String.valueOf(message));
    if (t != null) {
      buf.append(" <");
      buf.append(t.toString());
      buf.append(">");
      StringWriter sw = new StringWriter(1024);
      PrintWriter pw = new PrintWriter(sw);
      t.printStackTrace(pw);
      pw.close();
      buf.append(sw.toString());
    } 
    write(buf);
  }
  
  protected void write(StringBuffer buffer) {
    System.err.println(buffer.toString());
  }
  
  protected boolean isLevelEnabled(int logLevel) {
    return (logLevel >= this.currentLogLevel);
  }
  
  public final void debug(Object message) {
    if (isLevelEnabled(2))
      log(2, message, null); 
  }
  
  public final void debug(Object message, Throwable t) {
    if (isLevelEnabled(2))
      log(2, message, t); 
  }
  
  public final void trace(Object message) {
    if (isLevelEnabled(1))
      log(1, message, null); 
  }
  
  public final void trace(Object message, Throwable t) {
    if (isLevelEnabled(1))
      log(1, message, t); 
  }
  
  public final void info(Object message) {
    if (isLevelEnabled(3))
      log(3, message, null); 
  }
  
  public final void info(Object message, Throwable t) {
    if (isLevelEnabled(3))
      log(3, message, t); 
  }
  
  public final void warn(Object message) {
    if (isLevelEnabled(4))
      log(4, message, null); 
  }
  
  public final void warn(Object message, Throwable t) {
    if (isLevelEnabled(4))
      log(4, message, t); 
  }
  
  public final void error(Object message) {
    if (isLevelEnabled(5))
      log(5, message, null); 
  }
  
  public final void error(Object message, Throwable t) {
    if (isLevelEnabled(5))
      log(5, message, t); 
  }
  
  public final void fatal(Object message) {
    if (isLevelEnabled(6))
      log(6, message, null); 
  }
  
  public final void fatal(Object message, Throwable t) {
    if (isLevelEnabled(6))
      log(6, message, t); 
  }
  
  public final boolean isDebugEnabled() {
    return isLevelEnabled(2);
  }
  
  public final boolean isErrorEnabled() {
    return isLevelEnabled(5);
  }
  
  public final boolean isFatalEnabled() {
    return isLevelEnabled(6);
  }
  
  public final boolean isInfoEnabled() {
    return isLevelEnabled(3);
  }
  
  public final boolean isTraceEnabled() {
    return isLevelEnabled(1);
  }
  
  public final boolean isWarnEnabled() {
    return isLevelEnabled(4);
  }
  
  private static ClassLoader getContextClassLoader() {
    ClassLoader classLoader = null;
    try {
      Method method = Thread.class.getMethod("getContextClassLoader", (Class[])null);
      try {
        classLoader = (ClassLoader)method.invoke(Thread.currentThread(), (Object[])null);
      } catch (IllegalAccessException e) {
      
      } catch (InvocationTargetException e) {
        if (!(e.getTargetException() instanceof SecurityException))
          throw new LogConfigurationException("Unexpected InvocationTargetException", e.getTargetException()); 
      } 
    } catch (NoSuchMethodException e) {}
    if (classLoader == null)
      classLoader = SimpleLog.class.getClassLoader(); 
    return classLoader;
  }
  
  private static InputStream getResourceAsStream(String name) {
    return AccessController.<InputStream>doPrivileged(new PrivilegedAction(name) {
          private final String val$name;
          
          public Object run() {
            ClassLoader threadCL = SimpleLog.getContextClassLoader();
            if (threadCL != null)
              return threadCL.getResourceAsStream(this.val$name); 
            return ClassLoader.getSystemResourceAsStream(this.val$name);
          }
        });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\logging\impl\SimpleLog.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */