package org.apache.logging.log4j.simple;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

public class SimpleLogger extends AbstractLogger {
   private static final char SPACE = ' ';
   private DateFormat dateFormatter;
   private Level level;
   private final boolean showDateTime;
   private final boolean showContextMap;
   private PrintStream stream;
   private final String logName;

   public SimpleLogger(String var1, Level var2, boolean var3, boolean var4, boolean var5, boolean var6, String var7, MessageFactory var8, PropertiesUtil var9, PrintStream var10) {
      super(var1, var8);
      String var11 = var9.getStringProperty("org.apache.logging.log4j.simplelog." + var1 + ".level");
      this.level = Level.toLevel(var11, var2);
      if (var4) {
         int var12 = var1.lastIndexOf(".");
         if (var12 > 0 && var12 < var1.length()) {
            this.logName = var1.substring(var12 + 1);
         } else {
            this.logName = var1;
         }
      } else if (var3) {
         this.logName = var1;
      } else {
         this.logName = null;
      }

      this.showDateTime = var5;
      this.showContextMap = var6;
      this.stream = var10;
      if (var5) {
         try {
            this.dateFormatter = new SimpleDateFormat(var7);
         } catch (IllegalArgumentException var13) {
            this.dateFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS zzz");
         }
      }

   }

   public void setStream(PrintStream var1) {
      this.stream = var1;
   }

   public Level getLevel() {
      return this.level;
   }

   public void setLevel(Level var1) {
      if (var1 != null) {
         this.level = var1;
      }

   }

   public void log(Marker var1, String var2, Level var3, Message var4, Throwable var5) {
      StringBuilder var6 = new StringBuilder();
      if (this.showDateTime) {
         Date var7 = new Date();
         DateFormat var9;
         synchronized(var9 = this.dateFormatter){}
         String var8 = this.dateFormatter.format(var7);
         var6.append(var8);
         var6.append(' ');
      }

      var6.append(var3.toString());
      var6.append(' ');
      if (this.logName != null && this.logName.length() > 0) {
         var6.append(this.logName);
         var6.append(' ');
      }

      var6.append(var4.getFormattedMessage());
      if (this.showContextMap) {
         Map var11 = ThreadContext.getContext();
         if (var11.size() > 0) {
            var6.append(' ');
            var6.append(var11.toString());
            var6.append(' ');
         }
      }

      Object[] var12 = var4.getParameters();
      Throwable var13;
      if (var5 == null && var12 != null && var12[var12.length - 1] instanceof Throwable) {
         var13 = (Throwable)var12[var12.length - 1];
      } else {
         var13 = var5;
      }

      if (var13 != null) {
         var6.append(' ');
         ByteArrayOutputStream var14 = new ByteArrayOutputStream();
         var13.printStackTrace(new PrintStream(var14));
         var6.append(var14.toString());
      }

      this.stream.println(var6.toString());
   }

   protected boolean isEnabled(Level var1, Marker var2, String var3) {
      return this.level.intLevel() >= var1.intLevel();
   }

   protected boolean isEnabled(Level var1, Marker var2, String var3, Throwable var4) {
      return this.level.intLevel() >= var1.intLevel();
   }

   protected boolean isEnabled(Level var1, Marker var2, String var3, Object... var4) {
      return this.level.intLevel() >= var1.intLevel();
   }

   protected boolean isEnabled(Level var1, Marker var2, Object var3, Throwable var4) {
      return this.level.intLevel() >= var1.intLevel();
   }

   protected boolean isEnabled(Level var1, Marker var2, Message var3, Throwable var4) {
      return this.level.intLevel() >= var1.intLevel();
   }
}
