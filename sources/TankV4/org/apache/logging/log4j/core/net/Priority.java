package org.apache.logging.log4j.core.net;

import org.apache.logging.log4j.Level;

public class Priority {
   private final Facility facility;
   private final Severity severity;

   public Priority(Facility var1, Severity var2) {
      this.facility = var1;
      this.severity = var2;
   }

   public static int getPriority(Facility var0, Level var1) {
      return (var0.getCode() << 3) + Severity.getSeverity(var1).getCode();
   }

   public Facility getFacility() {
      return this.facility;
   }

   public Severity getSeverity() {
      return this.severity;
   }

   public int getValue() {
      return this.facility.getCode() << 3 + this.severity.getCode();
   }

   public String toString() {
      return Integer.toString(this.getValue());
   }
}
