package org.apache.logging.log4j.core.jmx;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.helpers.Assert;
import org.apache.logging.log4j.core.helpers.Charsets;
import org.apache.logging.log4j.core.helpers.Closer;
import org.apache.logging.log4j.status.StatusLogger;

public class LoggerContextAdmin extends NotificationBroadcasterSupport implements LoggerContextAdminMBean, PropertyChangeListener {
   private static final int PAGE = 4096;
   private static final int TEXT_BUFFER = 65536;
   private static final int BUFFER_SIZE = 2048;
   private static final StatusLogger LOGGER = StatusLogger.getLogger();
   private final AtomicLong sequenceNo = new AtomicLong();
   private final ObjectName objectName;
   private final LoggerContext loggerContext;
   private String customConfigText;

   public LoggerContextAdmin(LoggerContext var1, Executor var2) {
      super(var2, new MBeanNotificationInfo[]{createNotificationInfo()});
      this.loggerContext = (LoggerContext)Assert.isNotNull(var1, "loggerContext");

      try {
         String var3 = Server.escape(var1.getName());
         String var4 = String.format("org.apache.logging.log4j2:type=LoggerContext,ctx=%s", var3);
         this.objectName = new ObjectName(var4);
      } catch (Exception var5) {
         throw new IllegalStateException(var5);
      }

      var1.addPropertyChangeListener(this);
   }

   private static MBeanNotificationInfo createNotificationInfo() {
      String[] var0 = new String[]{"com.apache.logging.log4j.core.jmx.config.reconfigured"};
      String var1 = Notification.class.getName();
      String var2 = "Configuration reconfigured";
      return new MBeanNotificationInfo(var0, var1, "Configuration reconfigured");
   }

   public String getStatus() {
      return this.loggerContext.getStatus().toString();
   }

   public String getName() {
      return this.loggerContext.getName();
   }

   private Configuration getConfig() {
      return this.loggerContext.getConfiguration();
   }

   public String getConfigLocationURI() {
      if (this.loggerContext.getConfigLocation() != null) {
         return String.valueOf(this.loggerContext.getConfigLocation());
      } else {
         return this.getConfigName() != null ? String.valueOf((new File(this.getConfigName())).toURI()) : "";
      }
   }

   public void setConfigLocationURI(String var1) throws URISyntaxException, IOException {
      LOGGER.debug("---------");
      LOGGER.debug("Remote request to reconfigure using location " + var1);
      URI var2 = new URI(var1);
      var2.toURL().openStream().close();
      this.loggerContext.setConfigLocation(var2);
      LOGGER.debug("Completed remote request to reconfigure.");
   }

   public void propertyChange(PropertyChangeEvent var1) {
      if ("config".equals(var1.getPropertyName())) {
         if (this.loggerContext.getConfiguration().getName() != null) {
            this.customConfigText = null;
         }

         Notification var2 = new Notification("com.apache.logging.log4j.core.jmx.config.reconfigured", this.getObjectName(), this.nextSeqNo(), this.now(), (String)null);
         this.sendNotification(var2);
      }
   }

   public String getConfigText() throws IOException {
      return this.getConfigText(Charsets.UTF_8.name());
   }

   public String getConfigText(String var1) throws IOException {
      if (this.customConfigText != null) {
         return this.customConfigText;
      } else {
         try {
            Charset var2 = Charset.forName(var1);
            return this.readContents(new URI(this.getConfigLocationURI()), var2);
         } catch (Exception var4) {
            StringWriter var3 = new StringWriter(2048);
            var4.printStackTrace(new PrintWriter(var3));
            return var3.toString();
         }
      }
   }

   public void setConfigText(String var1, String var2) {
      String var3 = this.customConfigText;
      this.customConfigText = (String)Assert.isNotNull(var1, "configText");
      LOGGER.debug("---------");
      LOGGER.debug("Remote request to reconfigure from config text.");

      try {
         ByteArrayInputStream var4 = new ByteArrayInputStream(var1.getBytes(var2));
         ConfigurationFactory.ConfigurationSource var8 = new ConfigurationFactory.ConfigurationSource(var4);
         Configuration var6 = ConfigurationFactory.getInstance().getConfiguration(var8);
         this.loggerContext.start(var6);
         LOGGER.debug("Completed remote request to reconfigure from config text.");
      } catch (Exception var7) {
         this.customConfigText = var3;
         String var5 = "Could not reconfigure from config text";
         LOGGER.error("Could not reconfigure from config text", var7);
         throw new IllegalArgumentException("Could not reconfigure from config text", var7);
      }
   }

   private String readContents(URI var1, Charset var2) throws IOException {
      InputStream var3 = null;
      InputStreamReader var4 = null;
      var3 = var1.toURL().openStream();
      var4 = new InputStreamReader(var3, var2);
      StringBuilder var5 = new StringBuilder(65536);
      char[] var6 = new char[4096];
      boolean var7 = true;

      int var10;
      while((var10 = var4.read(var6)) >= 0) {
         var5.append(var6, 0, var10);
      }

      String var8 = var5.toString();
      Closer.closeSilent((Closeable)var3);
      Closer.closeSilent((Closeable)var4);
      return var8;
   }

   public String getConfigName() {
      return this.getConfig().getName();
   }

   public String getConfigClassName() {
      return this.getConfig().getClass().getName();
   }

   public String getConfigFilter() {
      return String.valueOf(this.getConfig().getFilter());
   }

   public String getConfigMonitorClassName() {
      return this.getConfig().getConfigurationMonitor().getClass().getName();
   }

   public Map getConfigProperties() {
      return this.getConfig().getProperties();
   }

   public ObjectName getObjectName() {
      return this.objectName;
   }

   private long nextSeqNo() {
      return this.sequenceNo.getAndIncrement();
   }

   private long now() {
      return System.currentTimeMillis();
   }
}
