package org.apache.logging.log4j.core.net;

public enum Protocol {
   TCP,
   UDP;

   private static final Protocol[] $VALUES = new Protocol[]{TCP, UDP};

   public boolean isEqual(String var1) {
      return this.name().equalsIgnoreCase(var1);
   }
}
