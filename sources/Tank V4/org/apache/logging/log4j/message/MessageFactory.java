package org.apache.logging.log4j.message;

public interface MessageFactory {
   Message newMessage(Object var1);

   Message newMessage(String var1);

   Message newMessage(String var1, Object... var2);
}
