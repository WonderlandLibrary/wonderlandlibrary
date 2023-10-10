package org.apache.logging.log4j.message;

public class FormattedMessageFactory extends AbstractMessageFactory {
   public Message newMessage(String var1, Object... var2) {
      return new FormattedMessage(var1, var2);
   }
}
