package org.apache.logging.log4j.message;

public class MessageFormatMessageFactory extends AbstractMessageFactory {
   public Message newMessage(String var1, Object... var2) {
      return new MessageFormatMessage(var1, var2);
   }
}
