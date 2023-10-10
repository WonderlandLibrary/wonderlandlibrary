package org.apache.logging.log4j.core.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JMSTopicReceiver extends AbstractJMSReceiver {
   public JMSTopicReceiver(String var1, String var2, String var3, String var4) {
      try {
         InitialContext var5 = new InitialContext();
         TopicConnectionFactory var6 = (TopicConnectionFactory)this.lookup(var5, var1);
         TopicConnection var7 = var6.createTopicConnection(var3, var4);
         var7.start();
         TopicSession var8 = var7.createTopicSession(false, 1);
         Topic var9 = (Topic)var5.lookup(var2);
         TopicSubscriber var10 = var8.createSubscriber(var9);
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
      new JMSTopicReceiver(var1, var2, var3, var4);
      Charset var5 = Charset.defaultCharset();
      BufferedReader var6 = new BufferedReader(new InputStreamReader(System.in, var5));
      System.out.println("Type \"exit\" to quit JMSTopicReceiver.");

      String var7;
      do {
         var7 = var6.readLine();
      } while(var7 != null && !var7.equalsIgnoreCase("exit"));

      System.out.println("Exiting. Kill the application if it does not exit due to daemon threads.");
   }

   private static void usage(String var0) {
      System.err.println(var0);
      System.err.println("Usage: java " + JMSTopicReceiver.class.getName() + " TopicConnectionFactoryBindingName TopicBindingName username password");
      System.exit(1);
   }
}
