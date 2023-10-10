package org.apache.logging.log4j.core;

public interface ErrorHandler {
   void error(String var1);

   void error(String var1, Throwable var2);

   void error(String var1, LogEvent var2, Throwable var3);
}
