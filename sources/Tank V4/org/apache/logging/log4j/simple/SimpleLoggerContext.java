package org.apache.logging.log4j.simple;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.util.PropertiesUtil;

public class SimpleLoggerContext implements LoggerContext {
   protected static final String DEFAULT_DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss:SSS zzz";
   protected static final String SYSTEM_PREFIX = "org.apache.logging.log4j.simplelog.";
   private final Properties simpleLogProps = new Properties();
   private final PropertiesUtil props = new PropertiesUtil("log4j2.simplelog.properties");
   private final boolean showLogName;
   private final boolean showShortName;
   private final boolean showDateTime;
   private final boolean showContextMap;
   private final String dateTimeFormat;
   private final Level defaultLevel;
   private final PrintStream stream;
   private final ConcurrentMap loggers = new ConcurrentHashMap();

   public SimpleLoggerContext() {
      this.showContextMap = this.props.getBooleanProperty("org.apache.logging.log4j.simplelog.showContextMap", false);
      this.showLogName = this.props.getBooleanProperty("org.apache.logging.log4j.simplelog.showlogname", false);
      this.showShortName = this.props.getBooleanProperty("org.apache.logging.log4j.simplelog.showShortLogname", true);
      this.showDateTime = this.props.getBooleanProperty("org.apache.logging.log4j.simplelog.showdatetime", false);
      String var1 = this.props.getStringProperty("org.apache.logging.log4j.simplelog.level");
      this.defaultLevel = Level.toLevel(var1, Level.ERROR);
      this.dateTimeFormat = this.showDateTime ? this.props.getStringProperty("org.apache.logging.log4j.simplelog.dateTimeFormat", "yyyy/MM/dd HH:mm:ss:SSS zzz") : null;
      String var2 = this.props.getStringProperty("org.apache.logging.log4j.simplelog.logFile", "system.err");
      PrintStream var3;
      if ("system.err".equalsIgnoreCase(var2)) {
         var3 = System.err;
      } else if ("system.out".equalsIgnoreCase(var2)) {
         var3 = System.out;
      } else {
         try {
            FileOutputStream var4 = new FileOutputStream(var2);
            var3 = new PrintStream(var4);
         } catch (FileNotFoundException var5) {
            var3 = System.err;
         }
      }

      this.stream = var3;
   }

   public Logger getLogger(String var1) {
      return this.getLogger(var1, (MessageFactory)null);
   }

   public Logger getLogger(String var1, MessageFactory var2) {
      if (this.loggers.containsKey(var1)) {
         Logger var3 = (Logger)this.loggers.get(var1);
         AbstractLogger.checkMessageFactory(var3, var2);
         return var3;
      } else {
         this.loggers.putIfAbsent(var1, new SimpleLogger(var1, this.defaultLevel, this.showLogName, this.showShortName, this.showDateTime, this.showContextMap, this.dateTimeFormat, var2, this.props, this.stream));
         return (Logger)this.loggers.get(var1);
      }
   }

   public boolean hasLogger(String var1) {
      return false;
   }

   public Object getExternalContext() {
      return null;
   }
}
