package org.apache.logging.log4j.core.net;

import java.io.Serializable;
import java.util.Properties;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import org.apache.logging.log4j.core.appender.AbstractManager;

public abstract class AbstractJMSManager extends AbstractManager {
   public AbstractJMSManager(String var1) {
      super(var1);
   }

   protected static Context createContext(String var0, String var1, String var2, String var3, String var4) throws NamingException {
      Properties var5 = getEnvironment(var0, var1, var2, var3, var4);
      return new InitialContext(var5);
   }

   protected static Object lookup(Context var0, String var1) throws NamingException {
      try {
         return var0.lookup(var1);
      } catch (NameNotFoundException var3) {
         LOGGER.warn("Could not find name [" + var1 + "].");
         throw var3;
      }
   }

   protected static Properties getEnvironment(String var0, String var1, String var2, String var3, String var4) {
      Properties var5 = new Properties();
      if (var0 != null) {
         var5.put("java.naming.factory.initial", var0);
         if (var1 != null) {
            var5.put("java.naming.provider.url", var1);
         } else {
            LOGGER.warn("The InitialContext factory name has been provided without a ProviderURL. This is likely to cause problems");
         }

         if (var2 != null) {
            var5.put("java.naming.factory.url.pkgs", var2);
         }

         if (var3 != null) {
            var5.put("java.naming.security.principal", var3);
            if (var4 != null) {
               var5.put("java.naming.security.credentials", var4);
            } else {
               LOGGER.warn("SecurityPrincipalName has been set without SecurityCredentials. This is likely to cause problems.");
            }
         }

         return var5;
      } else {
         return null;
      }
   }

   public abstract void send(Serializable var1) throws Exception;

   public synchronized void send(Serializable var1, Session var2, MessageProducer var3) throws Exception {
      try {
         Object var4;
         if (var1 instanceof String) {
            var4 = var2.createTextMessage();
            ((TextMessage)var4).setText((String)var1);
         } else {
            var4 = var2.createObjectMessage();
            ((ObjectMessage)var4).setObject(var1);
         }

         var3.send((Message)var4);
      } catch (JMSException var5) {
         LOGGER.error("Could not publish message via JMS " + this.getName());
         throw var5;
      }
   }
}
