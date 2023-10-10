package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.message.ParameterizedMessageFactory;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.apache.logging.log4j.status.StatusLogger;

public abstract class AbstractLogger implements Logger {
   public static final Marker FLOW_MARKER = MarkerManager.getMarker("FLOW");
   public static final Marker ENTRY_MARKER;
   public static final Marker EXIT_MARKER;
   public static final Marker EXCEPTION_MARKER;
   public static final Marker THROWING_MARKER;
   public static final Marker CATCHING_MARKER;
   public static final Class DEFAULT_MESSAGE_FACTORY_CLASS;
   private static final String FQCN;
   private static final String THROWING = "throwing";
   private static final String CATCHING = "catching";
   private final String name;
   private final MessageFactory messageFactory;

   public AbstractLogger() {
      this.name = this.getClass().getName();
      this.messageFactory = this.createDefaultMessageFactory();
   }

   public AbstractLogger(String var1) {
      this.name = var1;
      this.messageFactory = this.createDefaultMessageFactory();
   }

   public AbstractLogger(String var1, MessageFactory var2) {
      this.name = var1;
      this.messageFactory = var2 == null ? this.createDefaultMessageFactory() : var2;
   }

   public static void checkMessageFactory(Logger var0, MessageFactory var1) {
      String var2 = var0.getName();
      MessageFactory var3 = var0.getMessageFactory();
      if (var1 != null && !var3.equals(var1)) {
         StatusLogger.getLogger().warn("The Logger {} was created with the message factory {} and is now requested with the message factory {}, which may create log events with unexpected formatting.", new Object[]{var2, var3, var1});
      } else if (var1 == null && !var3.getClass().equals(DEFAULT_MESSAGE_FACTORY_CLASS)) {
         StatusLogger.getLogger().warn("The Logger {} was created with the message factory {} and is now requested with a null message factory (defaults to {}), which may create log events with unexpected formatting.", new Object[]{var2, var3, DEFAULT_MESSAGE_FACTORY_CLASS.getName()});
      }

   }

   public void catching(Level var1, Throwable var2) {
      this.catching(FQCN, var1, var2);
   }

   public void catching(Throwable var1) {
      this.catching(FQCN, Level.ERROR, var1);
   }

   protected void catching(String var1, Level var2, Throwable var3) {
      if (this.isEnabled(var2, CATCHING_MARKER, (Object)((Object)null), (Throwable)null)) {
         this.log(CATCHING_MARKER, var1, var2, this.messageFactory.newMessage("catching"), var3);
      }

   }

   private MessageFactory createDefaultMessageFactory() {
      try {
         return (MessageFactory)DEFAULT_MESSAGE_FACTORY_CLASS.newInstance();
      } catch (InstantiationException var2) {
         throw new IllegalStateException(var2);
      } catch (IllegalAccessException var3) {
         throw new IllegalStateException(var3);
      }
   }

   public void debug(Marker var1, Message var2) {
      if (this.isEnabled(Level.DEBUG, var1, (Message)var2, (Throwable)null)) {
         this.log(var1, FQCN, Level.DEBUG, var2, (Throwable)null);
      }

   }

   public void debug(Marker var1, Message var2, Throwable var3) {
      if (this.isEnabled(Level.DEBUG, var1, var2, var3)) {
         this.log(var1, FQCN, Level.DEBUG, var2, var3);
      }

   }

   public void debug(Marker var1, Object var2) {
      if (this.isEnabled(Level.DEBUG, var1, (Object)var2, (Throwable)null)) {
         this.log(var1, FQCN, Level.DEBUG, this.messageFactory.newMessage(var2), (Throwable)null);
      }

   }

   public void debug(Marker var1, Object var2, Throwable var3) {
      if (this.isEnabled(Level.DEBUG, var1, var2, var3)) {
         this.log(var1, FQCN, Level.DEBUG, this.messageFactory.newMessage(var2), var3);
      }

   }

   public void debug(Marker var1, String var2) {
      if (this.isEnabled(Level.DEBUG, var1, var2)) {
         this.log(var1, FQCN, Level.DEBUG, this.messageFactory.newMessage(var2), (Throwable)null);
      }

   }

   public void debug(Marker var1, String var2, Object... var3) {
      if (this.isEnabled(Level.DEBUG, var1, var2, var3)) {
         Message var4 = this.messageFactory.newMessage(var2, var3);
         this.log(var1, FQCN, Level.DEBUG, var4, var4.getThrowable());
      }

   }

   public void debug(Marker var1, String var2, Throwable var3) {
      if (this.isEnabled(Level.DEBUG, var1, var2, var3)) {
         this.log(var1, FQCN, Level.DEBUG, this.messageFactory.newMessage(var2), var3);
      }

   }

   public void debug(Message var1) {
      if (this.isEnabled(Level.DEBUG, (Marker)null, (Message)var1, (Throwable)null)) {
         this.log((Marker)null, FQCN, Level.DEBUG, var1, (Throwable)null);
      }

   }

   public void debug(Message var1, Throwable var2) {
      if (this.isEnabled(Level.DEBUG, (Marker)null, (Message)var1, (Throwable)var2)) {
         this.log((Marker)null, FQCN, Level.DEBUG, var1, var2);
      }

   }

   public void debug(Object var1) {
      if (this.isEnabled(Level.DEBUG, (Marker)null, (Object)var1, (Throwable)null)) {
         this.log((Marker)null, FQCN, Level.DEBUG, this.messageFactory.newMessage(var1), (Throwable)null);
      }

   }

   public void debug(Object var1, Throwable var2) {
      if (this.isEnabled(Level.DEBUG, (Marker)null, (Object)var1, (Throwable)var2)) {
         this.log((Marker)null, FQCN, Level.DEBUG, this.messageFactory.newMessage(var1), var2);
      }

   }

   public void debug(String var1) {
      if (this.isEnabled(Level.DEBUG, (Marker)null, var1)) {
         this.log((Marker)null, FQCN, Level.DEBUG, this.messageFactory.newMessage(var1), (Throwable)null);
      }

   }

   public void debug(String var1, Object... var2) {
      if (this.isEnabled(Level.DEBUG, (Marker)null, (String)var1, (Object[])var2)) {
         Message var3 = this.messageFactory.newMessage(var1, var2);
         this.log((Marker)null, FQCN, Level.DEBUG, var3, var3.getThrowable());
      }

   }

   public void debug(String var1, Throwable var2) {
      if (this.isEnabled(Level.DEBUG, (Marker)null, (String)var1, (Throwable)var2)) {
         this.log((Marker)null, FQCN, Level.DEBUG, this.messageFactory.newMessage(var1), var2);
      }

   }

   public void entry() {
      this.entry(FQCN);
   }

   public void entry(Object... var1) {
      this.entry(FQCN, var1);
   }

   protected void entry(String var1, Object... var2) {
      if (this.isEnabled(Level.TRACE, ENTRY_MARKER, (Object)((Object)null), (Throwable)null)) {
         this.log(ENTRY_MARKER, var1, Level.TRACE, this.entryMsg(var2.length, var2), (Throwable)null);
      }

   }

   private Message entryMsg(int var1, Object... var2) {
      if (var1 == 0) {
         return this.messageFactory.newMessage("entry");
      } else {
         StringBuilder var3 = new StringBuilder("entry params(");
         int var4 = 0;
         Object[] var5 = var2;
         int var6 = var2.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Object var8 = var5[var7];
            if (var8 != null) {
               var3.append(var8.toString());
            } else {
               var3.append("null");
            }

            ++var4;
            if (var4 < var2.length) {
               var3.append(", ");
            }
         }

         var3.append(")");
         return this.messageFactory.newMessage(var3.toString());
      }
   }

   public void error(Marker var1, Message var2) {
      if (this.isEnabled(Level.ERROR, var1, (Message)var2, (Throwable)null)) {
         this.log(var1, FQCN, Level.ERROR, var2, (Throwable)null);
      }

   }

   public void error(Marker var1, Message var2, Throwable var3) {
      if (this.isEnabled(Level.ERROR, var1, var2, var3)) {
         this.log(var1, FQCN, Level.ERROR, var2, var3);
      }

   }

   public void error(Marker var1, Object var2) {
      if (this.isEnabled(Level.ERROR, var1, (Object)var2, (Throwable)null)) {
         this.log(var1, FQCN, Level.ERROR, this.messageFactory.newMessage(var2), (Throwable)null);
      }

   }

   public void error(Marker var1, Object var2, Throwable var3) {
      if (this.isEnabled(Level.ERROR, var1, var2, var3)) {
         this.log(var1, FQCN, Level.ERROR, this.messageFactory.newMessage(var2), var3);
      }

   }

   public void error(Marker var1, String var2) {
      if (this.isEnabled(Level.ERROR, var1, var2)) {
         this.log(var1, FQCN, Level.ERROR, this.messageFactory.newMessage(var2), (Throwable)null);
      }

   }

   public void error(Marker var1, String var2, Object... var3) {
      if (this.isEnabled(Level.ERROR, var1, var2, var3)) {
         Message var4 = this.messageFactory.newMessage(var2, var3);
         this.log(var1, FQCN, Level.ERROR, var4, var4.getThrowable());
      }

   }

   public void error(Marker var1, String var2, Throwable var3) {
      if (this.isEnabled(Level.ERROR, var1, var2, var3)) {
         this.log(var1, FQCN, Level.ERROR, this.messageFactory.newMessage(var2), var3);
      }

   }

   public void error(Message var1) {
      if (this.isEnabled(Level.ERROR, (Marker)null, (Message)var1, (Throwable)null)) {
         this.log((Marker)null, FQCN, Level.ERROR, var1, (Throwable)null);
      }

   }

   public void error(Message var1, Throwable var2) {
      if (this.isEnabled(Level.ERROR, (Marker)null, (Message)var1, (Throwable)var2)) {
         this.log((Marker)null, FQCN, Level.ERROR, var1, var2);
      }

   }

   public void error(Object var1) {
      if (this.isEnabled(Level.ERROR, (Marker)null, (Object)var1, (Throwable)null)) {
         this.log((Marker)null, FQCN, Level.ERROR, this.messageFactory.newMessage(var1), (Throwable)null);
      }

   }

   public void error(Object var1, Throwable var2) {
      if (this.isEnabled(Level.ERROR, (Marker)null, (Object)var1, (Throwable)var2)) {
         this.log((Marker)null, FQCN, Level.ERROR, this.messageFactory.newMessage(var1), var2);
      }

   }

   public void error(String var1) {
      if (this.isEnabled(Level.ERROR, (Marker)null, var1)) {
         this.log((Marker)null, FQCN, Level.ERROR, this.messageFactory.newMessage(var1), (Throwable)null);
      }

   }

   public void error(String var1, Object... var2) {
      if (this.isEnabled(Level.ERROR, (Marker)null, (String)var1, (Object[])var2)) {
         Message var3 = this.messageFactory.newMessage(var1, var2);
         this.log((Marker)null, FQCN, Level.ERROR, var3, var3.getThrowable());
      }

   }

   public void error(String var1, Throwable var2) {
      if (this.isEnabled(Level.ERROR, (Marker)null, (String)var1, (Throwable)var2)) {
         this.log((Marker)null, FQCN, Level.ERROR, this.messageFactory.newMessage(var1), var2);
      }

   }

   public void exit() {
      this.exit(FQCN, (Object)null);
   }

   public Object exit(Object var1) {
      return this.exit(FQCN, var1);
   }

   protected Object exit(String var1, Object var2) {
      if (this.isEnabled(Level.TRACE, EXIT_MARKER, (Object)((Object)null), (Throwable)null)) {
         this.log(EXIT_MARKER, var1, Level.TRACE, this.toExitMsg(var2), (Throwable)null);
      }

      return var2;
   }

   public void fatal(Marker var1, Message var2) {
      if (this.isEnabled(Level.FATAL, var1, (Message)var2, (Throwable)null)) {
         this.log(var1, FQCN, Level.FATAL, var2, (Throwable)null);
      }

   }

   public void fatal(Marker var1, Message var2, Throwable var3) {
      if (this.isEnabled(Level.FATAL, var1, var2, var3)) {
         this.log(var1, FQCN, Level.FATAL, var2, var3);
      }

   }

   public void fatal(Marker var1, Object var2) {
      if (this.isEnabled(Level.FATAL, var1, (Object)var2, (Throwable)null)) {
         this.log(var1, FQCN, Level.FATAL, this.messageFactory.newMessage(var2), (Throwable)null);
      }

   }

   public void fatal(Marker var1, Object var2, Throwable var3) {
      if (this.isEnabled(Level.FATAL, var1, var2, var3)) {
         this.log(var1, FQCN, Level.FATAL, this.messageFactory.newMessage(var2), var3);
      }

   }

   public void fatal(Marker var1, String var2) {
      if (this.isEnabled(Level.FATAL, var1, var2)) {
         this.log(var1, FQCN, Level.FATAL, this.messageFactory.newMessage(var2), (Throwable)null);
      }

   }

   public void fatal(Marker var1, String var2, Object... var3) {
      if (this.isEnabled(Level.FATAL, var1, var2, var3)) {
         Message var4 = this.messageFactory.newMessage(var2, var3);
         this.log(var1, FQCN, Level.FATAL, var4, var4.getThrowable());
      }

   }

   public void fatal(Marker var1, String var2, Throwable var3) {
      if (this.isEnabled(Level.FATAL, var1, var2, var3)) {
         this.log(var1, FQCN, Level.FATAL, this.messageFactory.newMessage(var2), var3);
      }

   }

   public void fatal(Message var1) {
      if (this.isEnabled(Level.FATAL, (Marker)null, (Message)var1, (Throwable)null)) {
         this.log((Marker)null, FQCN, Level.FATAL, var1, (Throwable)null);
      }

   }

   public void fatal(Message var1, Throwable var2) {
      if (this.isEnabled(Level.FATAL, (Marker)null, (Message)var1, (Throwable)var2)) {
         this.log((Marker)null, FQCN, Level.FATAL, var1, var2);
      }

   }

   public void fatal(Object var1) {
      if (this.isEnabled(Level.FATAL, (Marker)null, (Object)var1, (Throwable)null)) {
         this.log((Marker)null, FQCN, Level.FATAL, this.messageFactory.newMessage(var1), (Throwable)null);
      }

   }

   public void fatal(Object var1, Throwable var2) {
      if (this.isEnabled(Level.FATAL, (Marker)null, (Object)var1, (Throwable)var2)) {
         this.log((Marker)null, FQCN, Level.FATAL, this.messageFactory.newMessage(var1), var2);
      }

   }

   public void fatal(String var1) {
      if (this.isEnabled(Level.FATAL, (Marker)null, var1)) {
         this.log((Marker)null, FQCN, Level.FATAL, this.messageFactory.newMessage(var1), (Throwable)null);
      }

   }

   public void fatal(String var1, Object... var2) {
      if (this.isEnabled(Level.FATAL, (Marker)null, (String)var1, (Object[])var2)) {
         Message var3 = this.messageFactory.newMessage(var1, var2);
         this.log((Marker)null, FQCN, Level.FATAL, var3, var3.getThrowable());
      }

   }

   public void fatal(String var1, Throwable var2) {
      if (this.isEnabled(Level.FATAL, (Marker)null, (String)var1, (Throwable)var2)) {
         this.log((Marker)null, FQCN, Level.FATAL, this.messageFactory.newMessage(var1), var2);
      }

   }

   public MessageFactory getMessageFactory() {
      return this.messageFactory;
   }

   public String getName() {
      return this.name;
   }

   public void info(Marker var1, Message var2) {
      if (this.isEnabled(Level.INFO, var1, (Message)var2, (Throwable)null)) {
         this.log(var1, FQCN, Level.INFO, var2, (Throwable)null);
      }

   }

   public void info(Marker var1, Message var2, Throwable var3) {
      if (this.isEnabled(Level.INFO, var1, var2, var3)) {
         this.log(var1, FQCN, Level.INFO, var2, var3);
      }

   }

   public void info(Marker var1, Object var2) {
      if (this.isEnabled(Level.INFO, var1, (Object)var2, (Throwable)null)) {
         this.log(var1, FQCN, Level.INFO, this.messageFactory.newMessage(var2), (Throwable)null);
      }

   }

   public void info(Marker var1, Object var2, Throwable var3) {
      if (this.isEnabled(Level.INFO, var1, var2, var3)) {
         this.log(var1, FQCN, Level.INFO, this.messageFactory.newMessage(var2), var3);
      }

   }

   public void info(Marker var1, String var2) {
      if (this.isEnabled(Level.INFO, var1, var2)) {
         this.log(var1, FQCN, Level.INFO, this.messageFactory.newMessage(var2), (Throwable)null);
      }

   }

   public void info(Marker var1, String var2, Object... var3) {
      if (this.isEnabled(Level.INFO, var1, var2, var3)) {
         Message var4 = this.messageFactory.newMessage(var2, var3);
         this.log(var1, FQCN, Level.INFO, var4, var4.getThrowable());
      }

   }

   public void info(Marker var1, String var2, Throwable var3) {
      if (this.isEnabled(Level.INFO, var1, var2, var3)) {
         this.log(var1, FQCN, Level.INFO, this.messageFactory.newMessage(var2), var3);
      }

   }

   public void info(Message var1) {
      if (this.isEnabled(Level.INFO, (Marker)null, (Message)var1, (Throwable)null)) {
         this.log((Marker)null, FQCN, Level.INFO, var1, (Throwable)null);
      }

   }

   public void info(Message var1, Throwable var2) {
      if (this.isEnabled(Level.INFO, (Marker)null, (Message)var1, (Throwable)var2)) {
         this.log((Marker)null, FQCN, Level.INFO, var1, var2);
      }

   }

   public void info(Object var1) {
      if (this.isEnabled(Level.INFO, (Marker)null, (Object)var1, (Throwable)null)) {
         this.log((Marker)null, FQCN, Level.INFO, this.messageFactory.newMessage(var1), (Throwable)null);
      }

   }

   public void info(Object var1, Throwable var2) {
      if (this.isEnabled(Level.INFO, (Marker)null, (Object)var1, (Throwable)var2)) {
         this.log((Marker)null, FQCN, Level.INFO, this.messageFactory.newMessage(var1), var2);
      }

   }

   public void info(String var1) {
      if (this.isEnabled(Level.INFO, (Marker)null, var1)) {
         this.log((Marker)null, FQCN, Level.INFO, this.messageFactory.newMessage(var1), (Throwable)null);
      }

   }

   public void info(String var1, Object... var2) {
      if (this.isEnabled(Level.INFO, (Marker)null, (String)var1, (Object[])var2)) {
         Message var3 = this.messageFactory.newMessage(var1, var2);
         this.log((Marker)null, FQCN, Level.INFO, var3, var3.getThrowable());
      }

   }

   public void info(String var1, Throwable var2) {
      if (this.isEnabled(Level.INFO, (Marker)null, (String)var1, (Throwable)var2)) {
         this.log((Marker)null, FQCN, Level.INFO, this.messageFactory.newMessage(var1), var2);
      }

   }

   public boolean isDebugEnabled() {
      return this.isEnabled(Level.DEBUG, (Marker)null, (String)null);
   }

   public boolean isDebugEnabled(Marker var1) {
      return this.isEnabled(Level.DEBUG, var1, (Object)((Object)null), (Throwable)null);
   }

   public boolean isEnabled(Level var1) {
      return this.isEnabled(var1, (Marker)null, (Object)((Object)null), (Throwable)null);
   }

   protected abstract boolean isEnabled(Level var1, Marker var2, Message var3, Throwable var4);

   protected abstract boolean isEnabled(Level var1, Marker var2, Object var3, Throwable var4);

   protected abstract boolean isEnabled(Level var1, Marker var2, String var3);

   protected abstract boolean isEnabled(Level var1, Marker var2, String var3, Object... var4);

   protected abstract boolean isEnabled(Level var1, Marker var2, String var3, Throwable var4);

   public boolean isErrorEnabled() {
      return this.isEnabled(Level.ERROR, (Marker)null, (Object)((Object)null), (Throwable)null);
   }

   public boolean isErrorEnabled(Marker var1) {
      return this.isEnabled(Level.ERROR, var1, (Object)((Object)null), (Throwable)null);
   }

   public boolean isFatalEnabled() {
      return this.isEnabled(Level.FATAL, (Marker)null, (Object)((Object)null), (Throwable)null);
   }

   public boolean isFatalEnabled(Marker var1) {
      return this.isEnabled(Level.FATAL, var1, (Object)((Object)null), (Throwable)null);
   }

   public boolean isInfoEnabled() {
      return this.isEnabled(Level.INFO, (Marker)null, (Object)((Object)null), (Throwable)null);
   }

   public boolean isInfoEnabled(Marker var1) {
      return this.isEnabled(Level.INFO, var1, (Object)((Object)null), (Throwable)null);
   }

   public boolean isTraceEnabled() {
      return this.isEnabled(Level.TRACE, (Marker)null, (Object)((Object)null), (Throwable)null);
   }

   public boolean isTraceEnabled(Marker var1) {
      return this.isEnabled(Level.TRACE, var1, (Object)((Object)null), (Throwable)null);
   }

   public boolean isWarnEnabled() {
      return this.isEnabled(Level.WARN, (Marker)null, (Object)((Object)null), (Throwable)null);
   }

   public boolean isWarnEnabled(Marker var1) {
      return this.isEnabled(Level.WARN, var1, (Object)((Object)null), (Throwable)null);
   }

   public boolean isEnabled(Level var1, Marker var2) {
      return this.isEnabled(var1, var2, (Object)((Object)null), (Throwable)null);
   }

   public void log(Level var1, Marker var2, Message var3) {
      if (this.isEnabled(var1, var2, (Message)var3, (Throwable)null)) {
         this.log(var2, FQCN, var1, var3, (Throwable)null);
      }

   }

   public void log(Level var1, Marker var2, Message var3, Throwable var4) {
      if (this.isEnabled(var1, var2, var3, var4)) {
         this.log(var2, FQCN, var1, var3, var4);
      }

   }

   public void log(Level var1, Marker var2, Object var3) {
      if (this.isEnabled(var1, var2, (Object)var3, (Throwable)null)) {
         this.log(var2, FQCN, var1, this.messageFactory.newMessage(var3), (Throwable)null);
      }

   }

   public void log(Level var1, Marker var2, Object var3, Throwable var4) {
      if (this.isEnabled(var1, var2, var3, var4)) {
         this.log(var2, FQCN, var1, this.messageFactory.newMessage(var3), var4);
      }

   }

   public void log(Level var1, Marker var2, String var3) {
      if (this.isEnabled(var1, var2, var3)) {
         this.log(var2, FQCN, var1, this.messageFactory.newMessage(var3), (Throwable)null);
      }

   }

   public void log(Level var1, Marker var2, String var3, Object... var4) {
      if (this.isEnabled(var1, var2, var3, var4)) {
         Message var5 = this.messageFactory.newMessage(var3, var4);
         this.log(var2, FQCN, var1, var5, var5.getThrowable());
      }

   }

   public void log(Level var1, Marker var2, String var3, Throwable var4) {
      if (this.isEnabled(var1, var2, var3, var4)) {
         this.log(var2, FQCN, var1, this.messageFactory.newMessage(var3), var4);
      }

   }

   public void log(Level var1, Message var2) {
      if (this.isEnabled(var1, (Marker)null, (Message)var2, (Throwable)null)) {
         this.log((Marker)null, FQCN, var1, var2, (Throwable)null);
      }

   }

   public void log(Level var1, Message var2, Throwable var3) {
      if (this.isEnabled(var1, (Marker)null, (Message)var2, (Throwable)var3)) {
         this.log((Marker)null, FQCN, var1, var2, var3);
      }

   }

   public void log(Level var1, Object var2) {
      if (this.isEnabled(var1, (Marker)null, (Object)var2, (Throwable)null)) {
         this.log((Marker)null, FQCN, var1, this.messageFactory.newMessage(var2), (Throwable)null);
      }

   }

   public void log(Level var1, Object var2, Throwable var3) {
      if (this.isEnabled(var1, (Marker)null, (Object)var2, (Throwable)var3)) {
         this.log((Marker)null, FQCN, var1, this.messageFactory.newMessage(var2), var3);
      }

   }

   public void log(Level var1, String var2) {
      if (this.isEnabled(var1, (Marker)null, var2)) {
         this.log((Marker)null, FQCN, var1, this.messageFactory.newMessage(var2), (Throwable)null);
      }

   }

   public void log(Level var1, String var2, Object... var3) {
      if (this.isEnabled(var1, (Marker)null, (String)var2, (Object[])var3)) {
         Message var4 = this.messageFactory.newMessage(var2, var3);
         this.log((Marker)null, FQCN, var1, var4, var4.getThrowable());
      }

   }

   public void log(Level var1, String var2, Throwable var3) {
      if (this.isEnabled(var1, (Marker)null, (String)var2, (Throwable)var3)) {
         this.log((Marker)null, FQCN, var1, this.messageFactory.newMessage(var2), var3);
      }

   }

   public void printf(Level var1, String var2, Object... var3) {
      if (this.isEnabled(var1, (Marker)null, (String)var2, (Object[])var3)) {
         StringFormattedMessage var4 = new StringFormattedMessage(var2, var3);
         this.log((Marker)null, FQCN, var1, var4, var4.getThrowable());
      }

   }

   public void printf(Level var1, Marker var2, String var3, Object... var4) {
      if (this.isEnabled(var1, var2, var3, var4)) {
         StringFormattedMessage var5 = new StringFormattedMessage(var3, var4);
         this.log(var2, FQCN, var1, var5, var5.getThrowable());
      }

   }

   public abstract void log(Marker var1, String var2, Level var3, Message var4, Throwable var5);

   public Throwable throwing(Level var1, Throwable var2) {
      return this.throwing(FQCN, var1, var2);
   }

   public Throwable throwing(Throwable var1) {
      return this.throwing(FQCN, Level.ERROR, var1);
   }

   protected Throwable throwing(String var1, Level var2, Throwable var3) {
      if (this.isEnabled(var2, THROWING_MARKER, (Object)((Object)null), (Throwable)null)) {
         this.log(THROWING_MARKER, var1, var2, this.messageFactory.newMessage("throwing"), var3);
      }

      return var3;
   }

   private Message toExitMsg(Object var1) {
      return var1 == null ? this.messageFactory.newMessage("exit") : this.messageFactory.newMessage("exit with(" + var1 + ")");
   }

   public String toString() {
      return this.name;
   }

   public void trace(Marker var1, Message var2) {
      if (this.isEnabled(Level.TRACE, var1, (Message)var2, (Throwable)null)) {
         this.log(var1, FQCN, Level.TRACE, var2, (Throwable)null);
      }

   }

   public void trace(Marker var1, Message var2, Throwable var3) {
      if (this.isEnabled(Level.TRACE, var1, var2, var3)) {
         this.log(var1, FQCN, Level.TRACE, var2, var3);
      }

   }

   public void trace(Marker var1, Object var2) {
      if (this.isEnabled(Level.TRACE, var1, (Object)var2, (Throwable)null)) {
         this.log(var1, FQCN, Level.TRACE, this.messageFactory.newMessage(var2), (Throwable)null);
      }

   }

   public void trace(Marker var1, Object var2, Throwable var3) {
      if (this.isEnabled(Level.TRACE, var1, var2, var3)) {
         this.log(var1, FQCN, Level.TRACE, this.messageFactory.newMessage(var2), var3);
      }

   }

   public void trace(Marker var1, String var2) {
      if (this.isEnabled(Level.TRACE, var1, var2)) {
         this.log(var1, FQCN, Level.TRACE, this.messageFactory.newMessage(var2), (Throwable)null);
      }

   }

   public void trace(Marker var1, String var2, Object... var3) {
      if (this.isEnabled(Level.TRACE, var1, var2, var3)) {
         Message var4 = this.messageFactory.newMessage(var2, var3);
         this.log(var1, FQCN, Level.TRACE, var4, var4.getThrowable());
      }

   }

   public void trace(Marker var1, String var2, Throwable var3) {
      if (this.isEnabled(Level.TRACE, var1, var2, var3)) {
         this.log(var1, FQCN, Level.TRACE, this.messageFactory.newMessage(var2), var3);
      }

   }

   public void trace(Message var1) {
      if (this.isEnabled(Level.TRACE, (Marker)null, (Message)var1, (Throwable)null)) {
         this.log((Marker)null, FQCN, Level.TRACE, var1, (Throwable)null);
      }

   }

   public void trace(Message var1, Throwable var2) {
      if (this.isEnabled(Level.TRACE, (Marker)null, (Message)var1, (Throwable)var2)) {
         this.log((Marker)null, FQCN, Level.TRACE, var1, var2);
      }

   }

   public void trace(Object var1) {
      if (this.isEnabled(Level.TRACE, (Marker)null, (Object)var1, (Throwable)null)) {
         this.log((Marker)null, FQCN, Level.TRACE, this.messageFactory.newMessage(var1), (Throwable)null);
      }

   }

   public void trace(Object var1, Throwable var2) {
      if (this.isEnabled(Level.TRACE, (Marker)null, (Object)var1, (Throwable)var2)) {
         this.log((Marker)null, FQCN, Level.TRACE, this.messageFactory.newMessage(var1), var2);
      }

   }

   public void trace(String var1) {
      if (this.isEnabled(Level.TRACE, (Marker)null, var1)) {
         this.log((Marker)null, FQCN, Level.TRACE, this.messageFactory.newMessage(var1), (Throwable)null);
      }

   }

   public void trace(String var1, Object... var2) {
      if (this.isEnabled(Level.TRACE, (Marker)null, (String)var1, (Object[])var2)) {
         Message var3 = this.messageFactory.newMessage(var1, var2);
         this.log((Marker)null, FQCN, Level.TRACE, var3, var3.getThrowable());
      }

   }

   public void trace(String var1, Throwable var2) {
      if (this.isEnabled(Level.TRACE, (Marker)null, (String)var1, (Throwable)var2)) {
         this.log((Marker)null, FQCN, Level.TRACE, this.messageFactory.newMessage(var1), var2);
      }

   }

   public void warn(Marker var1, Message var2) {
      if (this.isEnabled(Level.WARN, var1, (Message)var2, (Throwable)null)) {
         this.log(var1, FQCN, Level.WARN, var2, (Throwable)null);
      }

   }

   public void warn(Marker var1, Message var2, Throwable var3) {
      if (this.isEnabled(Level.WARN, var1, var2, var3)) {
         this.log(var1, FQCN, Level.WARN, var2, var3);
      }

   }

   public void warn(Marker var1, Object var2) {
      if (this.isEnabled(Level.WARN, var1, (Object)var2, (Throwable)null)) {
         this.log(var1, FQCN, Level.WARN, this.messageFactory.newMessage(var2), (Throwable)null);
      }

   }

   public void warn(Marker var1, Object var2, Throwable var3) {
      if (this.isEnabled(Level.WARN, var1, var2, var3)) {
         this.log(var1, FQCN, Level.WARN, this.messageFactory.newMessage(var2), var3);
      }

   }

   public void warn(Marker var1, String var2) {
      if (this.isEnabled(Level.WARN, var1, var2)) {
         this.log(var1, FQCN, Level.WARN, this.messageFactory.newMessage(var2), (Throwable)null);
      }

   }

   public void warn(Marker var1, String var2, Object... var3) {
      if (this.isEnabled(Level.WARN, var1, var2, var3)) {
         Message var4 = this.messageFactory.newMessage(var2, var3);
         this.log(var1, FQCN, Level.WARN, var4, var4.getThrowable());
      }

   }

   public void warn(Marker var1, String var2, Throwable var3) {
      if (this.isEnabled(Level.WARN, var1, var2, var3)) {
         this.log(var1, FQCN, Level.WARN, this.messageFactory.newMessage(var2), var3);
      }

   }

   public void warn(Message var1) {
      if (this.isEnabled(Level.WARN, (Marker)null, (Message)var1, (Throwable)null)) {
         this.log((Marker)null, FQCN, Level.WARN, var1, (Throwable)null);
      }

   }

   public void warn(Message var1, Throwable var2) {
      if (this.isEnabled(Level.WARN, (Marker)null, (Message)var1, (Throwable)var2)) {
         this.log((Marker)null, FQCN, Level.WARN, var1, var2);
      }

   }

   public void warn(Object var1) {
      if (this.isEnabled(Level.WARN, (Marker)null, (Object)var1, (Throwable)null)) {
         this.log((Marker)null, FQCN, Level.WARN, this.messageFactory.newMessage(var1), (Throwable)null);
      }

   }

   public void warn(Object var1, Throwable var2) {
      if (this.isEnabled(Level.WARN, (Marker)null, (Object)var1, (Throwable)var2)) {
         this.log((Marker)null, FQCN, Level.WARN, this.messageFactory.newMessage(var1), var2);
      }

   }

   public void warn(String var1) {
      if (this.isEnabled(Level.WARN, (Marker)null, var1)) {
         this.log((Marker)null, FQCN, Level.WARN, this.messageFactory.newMessage(var1), (Throwable)null);
      }

   }

   public void warn(String var1, Object... var2) {
      if (this.isEnabled(Level.WARN, (Marker)null, (String)var1, (Object[])var2)) {
         Message var3 = this.messageFactory.newMessage(var1, var2);
         this.log((Marker)null, FQCN, Level.WARN, var3, var3.getThrowable());
      }

   }

   public void warn(String var1, Throwable var2) {
      if (this.isEnabled(Level.WARN, (Marker)null, (String)var1, (Throwable)var2)) {
         this.log((Marker)null, FQCN, Level.WARN, this.messageFactory.newMessage(var1), var2);
      }

   }

   static {
      ENTRY_MARKER = MarkerManager.getMarker("ENTRY", FLOW_MARKER);
      EXIT_MARKER = MarkerManager.getMarker("EXIT", FLOW_MARKER);
      EXCEPTION_MARKER = MarkerManager.getMarker("EXCEPTION");
      THROWING_MARKER = MarkerManager.getMarker("THROWING", EXCEPTION_MARKER);
      CATCHING_MARKER = MarkerManager.getMarker("CATCHING", EXCEPTION_MARKER);
      DEFAULT_MESSAGE_FACTORY_CLASS = ParameterizedMessageFactory.class;
      FQCN = AbstractLogger.class.getName();
   }
}
