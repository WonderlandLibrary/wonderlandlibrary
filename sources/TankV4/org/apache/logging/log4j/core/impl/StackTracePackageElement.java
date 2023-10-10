package org.apache.logging.log4j.core.impl;

import java.io.Serializable;

public class StackTracePackageElement implements Serializable {
   private static final long serialVersionUID = -2171069569241280505L;
   private final String location;
   private final String version;
   private final boolean isExact;

   public StackTracePackageElement(String var1, String var2, boolean var3) {
      this.location = var1;
      this.version = var2;
      this.isExact = var3;
   }

   public String getLocation() {
      return this.location;
   }

   public String getVersion() {
      return this.version;
   }

   public boolean isExact() {
      return this.isExact;
   }

   public String toString() {
      String var1 = this.isExact ? "" : "~";
      return var1 + "[" + this.location + ":" + this.version + "]";
   }
}
