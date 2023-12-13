package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.Level;

public class NullConfiguration extends BaseConfiguration {
   public static final String NULL_NAME = "Null";

   public NullConfiguration() {
      this.setName("Null");
      LoggerConfig var1 = this.getRootLogger();
      var1.setLevel(Level.OFF);
   }
}
