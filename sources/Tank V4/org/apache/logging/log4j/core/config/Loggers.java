package org.apache.logging.log4j.core.config;

import java.util.concurrent.ConcurrentMap;

public class Loggers {
   private final ConcurrentMap map;
   private final LoggerConfig root;

   public Loggers(ConcurrentMap var1, LoggerConfig var2) {
      this.map = var1;
      this.root = var2;
   }

   public ConcurrentMap getMap() {
      return this.map;
   }

   public LoggerConfig getRoot() {
      return this.root;
   }
}
