package org.apache.logging.log4j.core.appender;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.nio.charset.Charset;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.helpers.Loader;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.pattern.RegexReplacement;
import org.apache.logging.log4j.util.PropertiesUtil;

@Plugin(
   name = "Console",
   category = "Core",
   elementType = "appender",
   printObject = true
)
public final class ConsoleAppender extends AbstractOutputStreamAppender {
   private static final String JANSI_CLASS = "org.fusesource.jansi.WindowsAnsiOutputStream";
   private static ConsoleAppender.ConsoleManagerFactory factory = new ConsoleAppender.ConsoleManagerFactory();

   private ConsoleAppender(String var1, Layout var2, Filter var3, OutputStreamManager var4, boolean var5) {
      super(var1, var2, var3, var5, true, var4);
   }

   @PluginFactory
   public static ConsoleAppender createAppender(@PluginElement("Layout") Layout var0, @PluginElement("Filters") Filter var1, @PluginAttribute("target") String var2, @PluginAttribute("name") String var3, @PluginAttribute("follow") String var4, @PluginAttribute("ignoreExceptions") String var5) {
      if (var3 == null) {
         LOGGER.error("No name provided for ConsoleAppender");
         return null;
      } else {
         if (var0 == null) {
            var0 = PatternLayout.createLayout((String)null, (Configuration)null, (RegexReplacement)null, (String)null, (String)null);
         }

         boolean var6 = Boolean.parseBoolean(var4);
         boolean var7 = Booleans.parseBoolean(var5, true);
         ConsoleAppender.Target var8 = var2 == null ? ConsoleAppender.Target.SYSTEM_OUT : ConsoleAppender.Target.valueOf(var2);
         return new ConsoleAppender(var3, (Layout)var0, var1, getManager(var6, var8, (Layout)var0), var7);
      }
   }

   private static OutputStreamManager getManager(boolean var0, ConsoleAppender.Target var1, Layout var2) {
      String var3 = var1.name();
      OutputStream var4 = getOutputStream(var0, var1);
      return OutputStreamManager.getManager(var1.name() + "." + var0, new ConsoleAppender.FactoryData(var4, var3, var2), factory);
   }

   private static OutputStream getOutputStream(boolean var0, ConsoleAppender.Target var1) {
      String var2 = Charset.defaultCharset().name();
      PrintStream var3 = null;

      try {
         var3 = var1 == ConsoleAppender.Target.SYSTEM_OUT ? (var0 ? new PrintStream(new ConsoleAppender.SystemOutStream(), true, var2) : System.out) : (var0 ? new PrintStream(new ConsoleAppender.SystemErrStream(), true, var2) : System.err);
      } catch (UnsupportedEncodingException var11) {
         throw new IllegalStateException("Unsupported default encoding " + var2, var11);
      }

      PropertiesUtil var4 = PropertiesUtil.getProperties();
      if (var4.getStringProperty("os.name").startsWith("Windows") && !var4.getBooleanProperty("log4j.skipJansi")) {
         try {
            ClassLoader var5 = Loader.getClassLoader();
            Class var6 = var5.loadClass("org.fusesource.jansi.WindowsAnsiOutputStream");
            Constructor var7 = var6.getConstructor(OutputStream.class);
            return (OutputStream)var7.newInstance(var3);
         } catch (ClassNotFoundException var8) {
            LOGGER.debug("Jansi is not installed, cannot find {}", "org.fusesource.jansi.WindowsAnsiOutputStream");
         } catch (NoSuchMethodException var9) {
            LOGGER.warn("{} is missing the proper constructor", "org.fusesource.jansi.WindowsAnsiOutputStream");
         } catch (Exception var10) {
            LOGGER.warn("Unable to instantiate {}", "org.fusesource.jansi.WindowsAnsiOutputStream");
         }

         return var3;
      } else {
         return var3;
      }
   }

   private static class ConsoleManagerFactory implements ManagerFactory {
      private ConsoleManagerFactory() {
      }

      public OutputStreamManager createManager(String var1, ConsoleAppender.FactoryData var2) {
         return new OutputStreamManager(ConsoleAppender.FactoryData.access$100(var2), ConsoleAppender.FactoryData.access$200(var2), ConsoleAppender.FactoryData.access$300(var2));
      }

      public Object createManager(String var1, Object var2) {
         return this.createManager(var1, (ConsoleAppender.FactoryData)var2);
      }

      ConsoleManagerFactory(Object var1) {
         this();
      }
   }

   private static class FactoryData {
      private final OutputStream os;
      private final String type;
      private final Layout layout;

      public FactoryData(OutputStream var1, String var2, Layout var3) {
         this.os = var1;
         this.type = var2;
         this.layout = var3;
      }

      static OutputStream access$100(ConsoleAppender.FactoryData var0) {
         return var0.os;
      }

      static String access$200(ConsoleAppender.FactoryData var0) {
         return var0.type;
      }

      static Layout access$300(ConsoleAppender.FactoryData var0) {
         return var0.layout;
      }
   }

   private static class SystemOutStream extends OutputStream {
      public SystemOutStream() {
      }

      public void close() {
      }

      public void flush() {
         System.out.flush();
      }

      public void write(byte[] var1) throws IOException {
         System.out.write(var1);
      }

      public void write(byte[] var1, int var2, int var3) throws IOException {
         System.out.write(var1, var2, var3);
      }

      public void write(int var1) throws IOException {
         System.out.write(var1);
      }
   }

   private static class SystemErrStream extends OutputStream {
      public SystemErrStream() {
      }

      public void close() {
      }

      public void flush() {
         System.err.flush();
      }

      public void write(byte[] var1) throws IOException {
         System.err.write(var1);
      }

      public void write(byte[] var1, int var2, int var3) throws IOException {
         System.err.write(var1, var2, var3);
      }

      public void write(int var1) {
         System.err.write(var1);
      }
   }

   public static enum Target {
      SYSTEM_OUT,
      SYSTEM_ERR;

      private static final ConsoleAppender.Target[] $VALUES = new ConsoleAppender.Target[]{SYSTEM_OUT, SYSTEM_ERR};
   }
}
