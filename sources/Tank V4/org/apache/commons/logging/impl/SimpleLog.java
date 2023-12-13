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
   protected volatile String logName = null;
   protected volatile int currentLogLevel;
   private volatile String shortLogName = null;
   static Class class$java$lang$Thread;
   static Class class$org$apache$commons$logging$impl$SimpleLog;

   private static String getStringProperty(String var0) {
      String var1 = null;

      try {
         var1 = System.getProperty(var0);
      } catch (SecurityException var3) {
      }

      return var1 == null ? simpleLogProps.getProperty(var0) : var1;
   }

   private static String getStringProperty(String var0, String var1) {
      String var2 = getStringProperty(var0);
      return var2 == null ? var1 : var2;
   }

   private static boolean getBooleanProperty(String var0, boolean var1) {
      String var2 = getStringProperty(var0);
      return var2 == null ? var1 : "true".equalsIgnoreCase(var2);
   }

   public SimpleLog(String var1) {
      this.logName = var1;
      this.setLevel(3);
      String var2 = getStringProperty("org.apache.commons.logging.simplelog.log." + this.logName);

      for(int var3 = String.valueOf(var1).lastIndexOf("."); null == var2 && var3 > -1; var3 = String.valueOf(var1).lastIndexOf(".")) {
         var1 = var1.substring(0, var3);
         var2 = getStringProperty("org.apache.commons.logging.simplelog.log." + var1);
      }

      if (null == var2) {
         var2 = getStringProperty("org.apache.commons.logging.simplelog.defaultlog");
      }

      if ("all".equalsIgnoreCase(var2)) {
         this.setLevel(0);
      } else if ("trace".equalsIgnoreCase(var2)) {
         this.setLevel(1);
      } else if ("debug".equalsIgnoreCase(var2)) {
         this.setLevel(2);
      } else if ("info".equalsIgnoreCase(var2)) {
         this.setLevel(3);
      } else if ("warn".equalsIgnoreCase(var2)) {
         this.setLevel(4);
      } else if ("error".equalsIgnoreCase(var2)) {
         this.setLevel(5);
      } else if ("fatal".equalsIgnoreCase(var2)) {
         this.setLevel(6);
      } else if ("off".equalsIgnoreCase(var2)) {
         this.setLevel(7);
      }

   }

   public void setLevel(int var1) {
      this.currentLogLevel = var1;
   }

   public int getLevel() {
      return this.currentLogLevel;
   }

   protected void log(int var1, Object var2, Throwable var3) {
      StringBuffer var4 = new StringBuffer();
      if (showDateTime) {
         Date var5 = new Date();
         DateFormat var7;
         synchronized(var7 = dateFormatter){}
         String var6 = dateFormatter.format(var5);
         var4.append(var6);
         var4.append(" ");
      }

      switch(var1) {
      case 1:
         var4.append("[TRACE] ");
         break;
      case 2:
         var4.append("[DEBUG] ");
         break;
      case 3:
         var4.append("[INFO] ");
         break;
      case 4:
         var4.append("[WARN] ");
         break;
      case 5:
         var4.append("[ERROR] ");
         break;
      case 6:
         var4.append("[FATAL] ");
      }

      if (showShortName) {
         if (this.shortLogName == null) {
            String var9 = this.logName.substring(this.logName.lastIndexOf(".") + 1);
            this.shortLogName = var9.substring(var9.lastIndexOf("/") + 1);
         }

         var4.append(String.valueOf(this.shortLogName)).append(" - ");
      } else if (showLogName) {
         var4.append(String.valueOf(this.logName)).append(" - ");
      }

      var4.append(String.valueOf(var2));
      if (var3 != null) {
         var4.append(" <");
         var4.append(var3.toString());
         var4.append(">");
         StringWriter var10 = new StringWriter(1024);
         PrintWriter var11 = new PrintWriter(var10);
         var3.printStackTrace(var11);
         var11.close();
         var4.append(var10.toString());
      }

      this.write(var4);
   }

   protected void write(StringBuffer var1) {
      System.err.println(var1.toString());
   }

   public final void debug(Object var1) {
      if (this >= 2) {
         this.log(2, var1, (Throwable)null);
      }

   }

   public final void debug(Object var1, Throwable var2) {
      if (this >= 2) {
         this.log(2, var1, var2);
      }

   }

   public final void trace(Object var1) {
      if (this >= 1) {
         this.log(1, var1, (Throwable)null);
      }

   }

   public final void trace(Object var1, Throwable var2) {
      if (this >= 1) {
         this.log(1, var1, var2);
      }

   }

   public final void info(Object var1) {
      if (this >= 3) {
         this.log(3, var1, (Throwable)null);
      }

   }

   public final void info(Object var1, Throwable var2) {
      if (this >= 3) {
         this.log(3, var1, var2);
      }

   }

   public final void warn(Object var1) {
      if (this >= 4) {
         this.log(4, var1, (Throwable)null);
      }

   }

   public final void warn(Object var1, Throwable var2) {
      if (this >= 4) {
         this.log(4, var1, var2);
      }

   }

   public final void error(Object var1) {
      if (this >= 5) {
         this.log(5, var1, (Throwable)null);
      }

   }

   public final void error(Object var1, Throwable var2) {
      if (this >= 5) {
         this.log(5, var1, var2);
      }

   }

   public final void fatal(Object var1) {
      if (this >= 6) {
         this.log(6, var1, (Throwable)null);
      }

   }

   public final void fatal(Object var1, Throwable var2) {
      if (this >= 6) {
         this.log(6, var1, var2);
      }

   }

   public final boolean isDebugEnabled() {
      return this.isLevelEnabled(2);
   }

   public final boolean isErrorEnabled() {
      return this.isLevelEnabled(5);
   }

   public final boolean isFatalEnabled() {
      return this.isLevelEnabled(6);
   }

   public final boolean isInfoEnabled() {
      return this.isLevelEnabled(3);
   }

   public final boolean isTraceEnabled() {
      return this.isLevelEnabled(1);
   }

   public final boolean isWarnEnabled() {
      return this.isLevelEnabled(4);
   }

   private static ClassLoader getContextClassLoader() {
      ClassLoader var0 = null;

      try {
         Method var1 = (class$java$lang$Thread == null ? (class$java$lang$Thread = class$("java.lang.Thread")) : class$java$lang$Thread).getMethod("getContextClassLoader", (Class[])null);

         try {
            var0 = (ClassLoader)var1.invoke(Thread.currentThread(), (Class[])null);
         } catch (IllegalAccessException var3) {
         } catch (InvocationTargetException var4) {
            if (!(var4.getTargetException() instanceof SecurityException)) {
               throw new LogConfigurationException("Unexpected InvocationTargetException", var4.getTargetException());
            }
         }
      } catch (NoSuchMethodException var5) {
      }

      if (var0 == null) {
         var0 = (class$org$apache$commons$logging$impl$SimpleLog == null ? (class$org$apache$commons$logging$impl$SimpleLog = class$("org.apache.commons.logging.impl.SimpleLog")) : class$org$apache$commons$logging$impl$SimpleLog).getClassLoader();
      }

      return var0;
   }

   private static InputStream getResourceAsStream(String var0) {
      return (InputStream)AccessController.doPrivileged(new PrivilegedAction(var0) {
         private final String val$name;

         {
            this.val$name = var1;
         }

         public Object run() {
            ClassLoader var1 = SimpleLog.access$000();
            return var1 != null ? var1.getResourceAsStream(this.val$name) : ClassLoader.getSystemResourceAsStream(this.val$name);
         }
      });
   }

   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static ClassLoader access$000() {
      return getContextClassLoader();
   }

   static {
      InputStream var0 = getResourceAsStream("simplelog.properties");
      if (null != var0) {
         try {
            simpleLogProps.load(var0);
            var0.close();
         } catch (IOException var3) {
         }
      }

      showLogName = getBooleanProperty("org.apache.commons.logging.simplelog.showlogname", showLogName);
      showShortName = getBooleanProperty("org.apache.commons.logging.simplelog.showShortLogname", showShortName);
      showDateTime = getBooleanProperty("org.apache.commons.logging.simplelog.showdatetime", showDateTime);
      if (showDateTime) {
         dateTimeFormat = getStringProperty("org.apache.commons.logging.simplelog.dateTimeFormat", dateTimeFormat);

         try {
            dateFormatter = new SimpleDateFormat(dateTimeFormat);
         } catch (IllegalArgumentException var2) {
            dateTimeFormat = "yyyy/MM/dd HH:mm:ss:SSS zzz";
            dateFormatter = new SimpleDateFormat(dateTimeFormat);
         }
      }

   }
}
