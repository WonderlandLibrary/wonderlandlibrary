package org.apache.logging.log4j.core.net;

import java.io.Serializable;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.naming.Context;
import javax.naming.NamingException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.ManagerFactory;

public class JMSQueueManager extends AbstractJMSManager {
   private static final JMSQueueManager.JMSQueueManagerFactory FACTORY = new JMSQueueManager.JMSQueueManagerFactory();
   private JMSQueueManager.QueueInfo info;
   private final String factoryBindingName;
   private final String queueBindingName;
   private final String userName;
   private final String password;
   private final Context context;

   protected JMSQueueManager(String var1, Context var2, String var3, String var4, String var5, String var6, JMSQueueManager.QueueInfo var7) {
      super(var1);
      this.context = var2;
      this.factoryBindingName = var3;
      this.queueBindingName = var4;
      this.userName = var5;
      this.password = var6;
      this.info = var7;
   }

   public static JMSQueueManager getJMSQueueManager(String var0, String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8) {
      if (var5 == null) {
         LOGGER.error("No factory name provided for JMSQueueManager");
         return null;
      } else if (var6 == null) {
         LOGGER.error("No topic name provided for JMSQueueManager");
         return null;
      } else {
         String var9 = "JMSQueue:" + var5 + '.' + var6;
         return (JMSQueueManager)getManager(var9, FACTORY, new JMSQueueManager.FactoryData(var0, var1, var2, var3, var4, var5, var6, var7, var8));
      }
   }

   public synchronized void send(Serializable var1) throws Exception {
      if (this.info == null) {
         this.info = connect(this.context, this.factoryBindingName, this.queueBindingName, this.userName, this.password, false);
      }

      try {
         super.send(var1, JMSQueueManager.QueueInfo.access$100(this.info), JMSQueueManager.QueueInfo.access$200(this.info));
      } catch (Exception var3) {
         this.cleanup(true);
         throw var3;
      }
   }

   public void releaseSub() {
      if (this.info != null) {
         this.cleanup(false);
      }

   }

   private void cleanup(boolean var1) {
      try {
         JMSQueueManager.QueueInfo.access$100(this.info).close();
      } catch (Exception var4) {
         if (!var1) {
            LOGGER.error((String)("Error closing session for " + this.getName()), (Throwable)var4);
         }
      }

      try {
         JMSQueueManager.QueueInfo.access$300(this.info).close();
      } catch (Exception var3) {
         if (!var1) {
            LOGGER.error((String)("Error closing connection for " + this.getName()), (Throwable)var3);
         }
      }

      this.info = null;
   }

   private static JMSQueueManager.QueueInfo connect(Context var0, String var1, String var2, String var3, String var4, boolean var5) throws Exception {
      try {
         QueueConnectionFactory var6 = (QueueConnectionFactory)lookup(var0, var1);
         QueueConnection var7;
         if (var3 != null) {
            var7 = var6.createQueueConnection(var3, var4);
         } else {
            var7 = var6.createQueueConnection();
         }

         QueueSession var8 = var7.createQueueSession(false, 1);
         Queue var9 = (Queue)lookup(var0, var2);
         QueueSender var10 = var8.createSender(var9);
         var7.start();
         return new JMSQueueManager.QueueInfo(var7, var8, var10);
      } catch (NamingException var11) {
         LOGGER.warn((String)("Unable to locate connection factory " + var1), (Throwable)var11);
         if (!var5) {
            throw var11;
         }
      } catch (JMSException var12) {
         LOGGER.warn((String)("Unable to create connection to queue " + var2), (Throwable)var12);
         if (!var5) {
            throw var12;
         }
      }

      return null;
   }

   static JMSQueueManager.QueueInfo access$1300(Context var0, String var1, String var2, String var3, String var4, boolean var5) throws Exception {
      return connect(var0, var1, var2, var3, var4, var5);
   }

   static Logger access$1400() {
      return LOGGER;
   }

   static Logger access$1500() {
      return LOGGER;
   }

   private static class JMSQueueManagerFactory implements ManagerFactory {
      private JMSQueueManagerFactory() {
      }

      public JMSQueueManager createManager(String var1, JMSQueueManager.FactoryData var2) {
         try {
            Context var3 = AbstractJMSManager.createContext(JMSQueueManager.FactoryData.access$400(var2), JMSQueueManager.FactoryData.access$500(var2), JMSQueueManager.FactoryData.access$600(var2), JMSQueueManager.FactoryData.access$700(var2), JMSQueueManager.FactoryData.access$800(var2));
            JMSQueueManager.QueueInfo var4 = JMSQueueManager.access$1300(var3, JMSQueueManager.FactoryData.access$900(var2), JMSQueueManager.FactoryData.access$1000(var2), JMSQueueManager.FactoryData.access$1100(var2), JMSQueueManager.FactoryData.access$1200(var2), true);
            return new JMSQueueManager(var1, var3, JMSQueueManager.FactoryData.access$900(var2), JMSQueueManager.FactoryData.access$1000(var2), JMSQueueManager.FactoryData.access$1100(var2), JMSQueueManager.FactoryData.access$1200(var2), var4);
         } catch (NamingException var5) {
            JMSQueueManager.access$1400().error((String)"Unable to locate resource", (Throwable)var5);
         } catch (Exception var6) {
            JMSQueueManager.access$1500().error((String)"Unable to connect", (Throwable)var6);
         }

         return null;
      }

      public Object createManager(String var1, Object var2) {
         return this.createManager(var1, (JMSQueueManager.FactoryData)var2);
      }

      JMSQueueManagerFactory(Object var1) {
         this();
      }
   }

   private static class QueueInfo {
      private final QueueConnection conn;
      private final QueueSession session;
      private final QueueSender sender;

      public QueueInfo(QueueConnection var1, QueueSession var2, QueueSender var3) {
         this.conn = var1;
         this.session = var2;
         this.sender = var3;
      }

      static QueueSession access$100(JMSQueueManager.QueueInfo var0) {
         return var0.session;
      }

      static QueueSender access$200(JMSQueueManager.QueueInfo var0) {
         return var0.sender;
      }

      static QueueConnection access$300(JMSQueueManager.QueueInfo var0) {
         return var0.conn;
      }
   }

   private static class FactoryData {
      private final String factoryName;
      private final String providerURL;
      private final String urlPkgPrefixes;
      private final String securityPrincipalName;
      private final String securityCredentials;
      private final String factoryBindingName;
      private final String queueBindingName;
      private final String userName;
      private final String password;

      public FactoryData(String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9) {
         this.factoryName = var1;
         this.providerURL = var2;
         this.urlPkgPrefixes = var3;
         this.securityPrincipalName = var4;
         this.securityCredentials = var5;
         this.factoryBindingName = var6;
         this.queueBindingName = var7;
         this.userName = var8;
         this.password = var9;
      }

      static String access$400(JMSQueueManager.FactoryData var0) {
         return var0.factoryName;
      }

      static String access$500(JMSQueueManager.FactoryData var0) {
         return var0.providerURL;
      }

      static String access$600(JMSQueueManager.FactoryData var0) {
         return var0.urlPkgPrefixes;
      }

      static String access$700(JMSQueueManager.FactoryData var0) {
         return var0.securityPrincipalName;
      }

      static String access$800(JMSQueueManager.FactoryData var0) {
         return var0.securityCredentials;
      }

      static String access$900(JMSQueueManager.FactoryData var0) {
         return var0.factoryBindingName;
      }

      static String access$1000(JMSQueueManager.FactoryData var0) {
         return var0.queueBindingName;
      }

      static String access$1100(JMSQueueManager.FactoryData var0) {
         return var0.userName;
      }

      static String access$1200(JMSQueueManager.FactoryData var0) {
         return var0.password;
      }
   }
}
