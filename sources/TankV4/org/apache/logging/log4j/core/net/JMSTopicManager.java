package org.apache.logging.log4j.core.net;

import java.io.Serializable;
import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.NamingException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.ManagerFactory;

public class JMSTopicManager extends AbstractJMSManager {
   private static final JMSTopicManager.JMSTopicManagerFactory FACTORY = new JMSTopicManager.JMSTopicManagerFactory();
   private JMSTopicManager.TopicInfo info;
   private final String factoryBindingName;
   private final String topicBindingName;
   private final String userName;
   private final String password;
   private final Context context;

   protected JMSTopicManager(String var1, Context var2, String var3, String var4, String var5, String var6, JMSTopicManager.TopicInfo var7) {
      super(var1);
      this.context = var2;
      this.factoryBindingName = var3;
      this.topicBindingName = var4;
      this.userName = var5;
      this.password = var6;
      this.info = var7;
   }

   public static JMSTopicManager getJMSTopicManager(String var0, String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8) {
      if (var5 == null) {
         LOGGER.error("No factory name provided for JMSTopicManager");
         return null;
      } else if (var6 == null) {
         LOGGER.error("No topic name provided for JMSTopicManager");
         return null;
      } else {
         String var9 = "JMSTopic:" + var5 + '.' + var6;
         return (JMSTopicManager)getManager(var9, FACTORY, new JMSTopicManager.FactoryData(var0, var1, var2, var3, var4, var5, var6, var7, var8));
      }
   }

   public void send(Serializable var1) throws Exception {
      if (this.info == null) {
         this.info = connect(this.context, this.factoryBindingName, this.topicBindingName, this.userName, this.password, false);
      }

      try {
         super.send(var1, JMSTopicManager.TopicInfo.access$100(this.info), JMSTopicManager.TopicInfo.access$200(this.info));
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
         JMSTopicManager.TopicInfo.access$100(this.info).close();
      } catch (Exception var4) {
         if (!var1) {
            LOGGER.error((String)("Error closing session for " + this.getName()), (Throwable)var4);
         }
      }

      try {
         JMSTopicManager.TopicInfo.access$300(this.info).close();
      } catch (Exception var3) {
         if (!var1) {
            LOGGER.error((String)("Error closing connection for " + this.getName()), (Throwable)var3);
         }
      }

      this.info = null;
   }

   private static JMSTopicManager.TopicInfo connect(Context var0, String var1, String var2, String var3, String var4, boolean var5) throws Exception {
      try {
         TopicConnectionFactory var6 = (TopicConnectionFactory)lookup(var0, var1);
         TopicConnection var7;
         if (var3 != null) {
            var7 = var6.createTopicConnection(var3, var4);
         } else {
            var7 = var6.createTopicConnection();
         }

         TopicSession var8 = var7.createTopicSession(false, 1);
         Topic var9 = (Topic)lookup(var0, var2);
         TopicPublisher var10 = var8.createPublisher(var9);
         var7.start();
         return new JMSTopicManager.TopicInfo(var7, var8, var10);
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

   static JMSTopicManager.TopicInfo access$1300(Context var0, String var1, String var2, String var3, String var4, boolean var5) throws Exception {
      return connect(var0, var1, var2, var3, var4, var5);
   }

   static Logger access$1400() {
      return LOGGER;
   }

   static Logger access$1500() {
      return LOGGER;
   }

   private static class JMSTopicManagerFactory implements ManagerFactory {
      private JMSTopicManagerFactory() {
      }

      public JMSTopicManager createManager(String var1, JMSTopicManager.FactoryData var2) {
         try {
            Context var3 = AbstractJMSManager.createContext(JMSTopicManager.FactoryData.access$400(var2), JMSTopicManager.FactoryData.access$500(var2), JMSTopicManager.FactoryData.access$600(var2), JMSTopicManager.FactoryData.access$700(var2), JMSTopicManager.FactoryData.access$800(var2));
            JMSTopicManager.TopicInfo var4 = JMSTopicManager.access$1300(var3, JMSTopicManager.FactoryData.access$900(var2), JMSTopicManager.FactoryData.access$1000(var2), JMSTopicManager.FactoryData.access$1100(var2), JMSTopicManager.FactoryData.access$1200(var2), true);
            return new JMSTopicManager(var1, var3, JMSTopicManager.FactoryData.access$900(var2), JMSTopicManager.FactoryData.access$1000(var2), JMSTopicManager.FactoryData.access$1100(var2), JMSTopicManager.FactoryData.access$1200(var2), var4);
         } catch (NamingException var5) {
            JMSTopicManager.access$1400().error((String)"Unable to locate resource", (Throwable)var5);
         } catch (Exception var6) {
            JMSTopicManager.access$1500().error((String)"Unable to connect", (Throwable)var6);
         }

         return null;
      }

      public Object createManager(String var1, Object var2) {
         return this.createManager(var1, (JMSTopicManager.FactoryData)var2);
      }

      JMSTopicManagerFactory(Object var1) {
         this();
      }
   }

   private static class TopicInfo {
      private final TopicConnection conn;
      private final TopicSession session;
      private final TopicPublisher publisher;

      public TopicInfo(TopicConnection var1, TopicSession var2, TopicPublisher var3) {
         this.conn = var1;
         this.session = var2;
         this.publisher = var3;
      }

      static TopicSession access$100(JMSTopicManager.TopicInfo var0) {
         return var0.session;
      }

      static TopicPublisher access$200(JMSTopicManager.TopicInfo var0) {
         return var0.publisher;
      }

      static TopicConnection access$300(JMSTopicManager.TopicInfo var0) {
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
      private final String topicBindingName;
      private final String userName;
      private final String password;

      public FactoryData(String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9) {
         this.factoryName = var1;
         this.providerURL = var2;
         this.urlPkgPrefixes = var3;
         this.securityPrincipalName = var4;
         this.securityCredentials = var5;
         this.factoryBindingName = var6;
         this.topicBindingName = var7;
         this.userName = var8;
         this.password = var9;
      }

      static String access$400(JMSTopicManager.FactoryData var0) {
         return var0.factoryName;
      }

      static String access$500(JMSTopicManager.FactoryData var0) {
         return var0.providerURL;
      }

      static String access$600(JMSTopicManager.FactoryData var0) {
         return var0.urlPkgPrefixes;
      }

      static String access$700(JMSTopicManager.FactoryData var0) {
         return var0.securityPrincipalName;
      }

      static String access$800(JMSTopicManager.FactoryData var0) {
         return var0.securityCredentials;
      }

      static String access$900(JMSTopicManager.FactoryData var0) {
         return var0.factoryBindingName;
      }

      static String access$1000(JMSTopicManager.FactoryData var0) {
         return var0.topicBindingName;
      }

      static String access$1100(JMSTopicManager.FactoryData var0) {
         return var0.userName;
      }

      static String access$1200(JMSTopicManager.FactoryData var0) {
         return var0.password;
      }
   }
}
