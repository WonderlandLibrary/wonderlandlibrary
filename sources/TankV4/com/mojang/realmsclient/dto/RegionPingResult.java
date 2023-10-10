package com.mojang.realmsclient.dto;

public class RegionPingResult {
   private String regionName;
   private int ping;

   public RegionPingResult(String var1, int var2) {
      this.regionName = var1;
      this.ping = var2;
   }

   public int ping() {
      return this.ping;
   }

   public String toString() {
      return String.format("%s --> %.2f ms", this.regionName, this.ping);
   }
}
