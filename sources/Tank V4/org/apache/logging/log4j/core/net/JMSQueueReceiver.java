package org.apache.logging.log4j.core.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JMSQueueReceiver extends AbstractJMSReceiver {
   public JMSQueueReceiver(String var1, String var2, String var3, String var4) {
      try {
         InitialContext var5 = new InitialContext();
         QueueConnectionFactory var6 = (QueueConnectionFactory)this.lookup(var5, var1);
         QueueConnection var7 = var6.createQueueConnection(var3, var4);
         var7.start();
         QueueSession var8 = var7.createQueueSession(false, 1);
         Queue var9 = (Queue)var5.lookup(var2);
         QueueReceiver var10 = var8.createReceiver(var9);
         var10.setMessageListener(this);
      } catch (JMSException var11) {
         this.logger.error((String)"Could not read JMS message.", (Throwable)var11);
      } catch (NamingException var12) {
         this.logger.error((String)"Could not read JMS message.", (Throwable)var12);
      } catch (RuntimeException var13) {
         this.logger.error((String)"Could not read JMS message.", (Throwable)var13);
      }

   }

   public static void main(String[] var0) throws Exception {
      if (var0.length != 4) {
         usage("Wrong number of arguments.");
      }

      String var1 = var0[0];
      String var2 = var0[1];
      String var3 = var0[2];
      String var4 = var0[3];
      new JMSQueueReceiver(var1, var2, var3, var4);
      Charset var5 = Charset.defaultCharset();
      BufferedReader var6 = new BufferedReader(new InputStreamReader(System.in, var5));
      System.out.println("Type \"exit\" to quit JMSQueueReceiver.");

      String var7;
      do {
         var7 = var6.readLine();
      } while(var7 != null && !var7.equalsIgnoreCase("exit"));

      System.out.println("Exiting. Kill the application if it does not exit due to daemon threads.");
   }

   private static void usage(String var0) {
      System.err.println(var0);
      System.err.println("Usage: java " + JMSQueueReceiver.class.getName() + " QueueConnectionFactoryBindingName QueueBindingName username password");
      System.exit(1);
   }
}
