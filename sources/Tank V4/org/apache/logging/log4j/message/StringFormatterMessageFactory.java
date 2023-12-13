package org.apache.logging.log4j.message;

public final class StringFormatterMessageFactory extends AbstractMessageFactory {
   public static final StringFormatterMessageFactory INSTANCE = new StringFormatterMessageFactory();

   public Message newMessage(String var1, Object... var2) {
      return new StringFormattedMessage(var1, var2);
   }
}
