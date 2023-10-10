package org.apache.logging.log4j.core.net;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.AbstractServer;
import org.apache.logging.log4j.core.LogEvent;

public abstract class AbstractJMSReceiver extends AbstractServer implements MessageListener {
   protected Logger logger = LogManager.getLogger(this.getClass().getName());

   public void onMessage(Message var1) {
      try {
         if (var1 instanceof ObjectMessage) {
            ObjectMessage var2 = (ObjectMessage)var1;
            this.log((LogEvent)var2.getObject());
         } else {
            this.logger.warn("Received message is of type " + var1.getJMSType() + ", was expecting ObjectMessage.");
         }
      } catch (JMSException var3) {
         this.logger.error((String)"Exception thrown while processing incoming message.", (Throwable)var3);
      }

   }

   protected Object lookup(Context var1, String var2) throws NamingException {
      try {
         return var1.lookup(var2);
      } catch (NameNotFoundException var4) {
         this.logger.error("Could not find name [" + var2 + "].");
         throw var4;
      }
   }
}
