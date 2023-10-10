package com.mojang.realmsclient.client;

import com.mojang.realmsclient.dto.RegionPingResult;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Ping {
   public static List ping(Ping.Region... var0) {
      Ping.Region[] var1 = var0;
      int var2 = var0.length;

      int var3;
      for(var3 = 0; var3 < var2; ++var3) {
         Ping.Region var4 = var1[var3];
         ping(Ping.Region.access$000(var4));
      }

      ArrayList var6 = new ArrayList();
      Ping.Region[] var7 = var0;
      var3 = var0.length;

      for(int var8 = 0; var8 < var3; ++var8) {
         Ping.Region var5 = var7[var8];
         var6.add(new RegionPingResult(Ping.Region.access$100(var5), ping(Ping.Region.access$000(var5))));
      }

      Collections.sort(var6, new Comparator() {
         public int compare(RegionPingResult var1, RegionPingResult var2) {
            return var1.ping() - var2.ping();
         }

         public int compare(Object var1, Object var2) {
            return this.compare((RegionPingResult)var1, (RegionPingResult)var2);
         }
      });
      return var6;
   }

   private static int ping(String var0) {
      long var1 = -now();

      for(int var4 = 0; var4 < 10; ++var4) {
         try {
            InetSocketAddress var5 = new InetSocketAddress(var0, 80);
            Socket var3 = new Socket();
            var3.connect(var5, 700);
         } catch (Exception var6) {
         }
      }

      var1 += now();
      return (int)((double)var1 / 10.0D);
   }

   private static long now() {
      return System.currentTimeMillis();
   }

   public static List pingAllRegions() {
      return ping(Ping.Region.values());
   }

   static enum Region {
      US_EAST_1("us-east-1", "ec2.us-east-1.amazonaws.com"),
      US_WEST_2("us-west-2", "ec2.us-west-2.amazonaws.com"),
      US_WEST_1("us-west-1", "ec2.us-west-1.amazonaws.com"),
      EU_WEST_1("eu-west-1", "ec2.eu-west-1.amazonaws.com"),
      AP_SOUTHEAST_1("ap-southeast-1", "ec2.ap-southeast-1.amazonaws.com"),
      AP_SOUTHEAST_2("ap-southeast-2", "ec2.ap-southeast-2.amazonaws.com"),
      AP_NORTHEAST_1("ap-northeast-1", "ec2.ap-northeast-1.amazonaws.com"),
      SA_EAST_1("sa-east-1", "ec2.sa-east-1.amazonaws.com");

      private final String name;
      private final String endpoint;
      private static final Ping.Region[] $VALUES = new Ping.Region[]{US_EAST_1, US_WEST_2, US_WEST_1, EU_WEST_1, AP_SOUTHEAST_1, AP_SOUTHEAST_2, AP_NORTHEAST_1, SA_EAST_1};

      private Region(String var3, String var4) {
         this.name = var3;
         this.endpoint = var4;
      }

      static String access$000(Ping.Region var0) {
         return var0.endpoint;
      }

      static String access$100(Ping.Region var0) {
         return var0.name;
      }
   }
}
