package org.apache.logging.log4j.core.config;

public class ConfigurationException extends RuntimeException {
   private static final long serialVersionUID = -2413951820300775294L;

   public ConfigurationException(String var1) {
      super(var1);
   }

   public ConfigurationException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
