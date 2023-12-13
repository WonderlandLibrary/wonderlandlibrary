package io.netty.channel.local;

import io.netty.channel.Channel;
import java.net.SocketAddress;

public final class LocalAddress extends SocketAddress implements Comparable {
   private static final long serialVersionUID = 4644331421130916435L;
   public static final LocalAddress ANY = new LocalAddress("ANY");
   private final String id;
   private final String strVal;

   LocalAddress(Channel var1) {
      StringBuilder var2 = new StringBuilder(16);
      var2.append("local:E");
      var2.append(Long.toHexString((long)var1.hashCode() & 4294967295L | 4294967296L));
      var2.setCharAt(7, ':');
      this.id = var2.substring(6);
      this.strVal = var2.toString();
   }

   public LocalAddress(String var1) {
      if (var1 == null) {
         throw new NullPointerException("id");
      } else {
         var1 = var1.trim().toLowerCase();
         if (var1.isEmpty()) {
            throw new IllegalArgumentException("empty id");
         } else {
            this.id = var1;
            this.strVal = "local:" + var1;
         }
      }
   }

   public String id() {
      return this.id;
   }

   public int hashCode() {
      return this.id.hashCode();
   }

   public boolean equals(Object var1) {
      return !(var1 instanceof LocalAddress) ? false : this.id.equals(((LocalAddress)var1).id);
   }

   public int compareTo(LocalAddress var1) {
      return this.id.compareTo(var1.id);
   }

   public String toString() {
      return this.strVal;
   }

   public int compareTo(Object var1) {
      return this.compareTo((LocalAddress)var1);
   }
}
