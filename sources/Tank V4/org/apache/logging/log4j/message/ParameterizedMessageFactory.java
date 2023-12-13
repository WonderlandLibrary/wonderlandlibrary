package org.apache.logging.log4j.message;

public final class ParameterizedMessageFactory extends AbstractMessageFactory {
   public static final ParameterizedMessageFactory INSTANCE = new ParameterizedMessageFactory();

   public Message newMessage(String var1, Object... var2) {
      return new ParameterizedMessage(var1, var2);
   }
}
