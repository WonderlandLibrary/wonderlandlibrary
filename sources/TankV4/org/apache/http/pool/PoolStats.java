package org.apache.http.pool;

import org.apache.http.annotation.Immutable;

@Immutable
public class PoolStats {
   private final int leased;
   private final int pending;
   private final int available;
   private final int max;

   public PoolStats(int var1, int var2, int var3, int var4) {
      this.leased = var1;
      this.pending = var2;
      this.available = var3;
      this.max = var4;
   }

   public int getLeased() {
      return this.leased;
   }

   public int getPending() {
      return this.pending;
   }

   public int getAvailable() {
      return this.available;
   }

   public int getMax() {
      return this.max;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("[leased: ");
      var1.append(this.leased);
      var1.append("; pending: ");
      var1.append(this.pending);
      var1.append("; available: ");
      var1.append(this.available);
      var1.append("; max: ");
      var1.append(this.max);
      var1.append("]");
      return var1.toString();
   }
}
