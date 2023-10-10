package org.apache.logging.log4j.message;

public abstract class AbstractMessageFactory implements MessageFactory {
   public Message newMessage(Object var1) {
      return new ObjectMessage(var1);
   }

   public Message newMessage(String var1) {
      return new SimpleMessage(var1);
   }

   public abstract Message newMessage(String var1, Object... var2);
}
