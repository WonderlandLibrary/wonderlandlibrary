package com.sun.prism.impl;

import java.util.HashMap;
import java.util.Map;

public class PrismTrace {
   private static final boolean enabled;
   private static Map texData;
   private static long texBytes;
   private static Map rttData;
   private static long rttBytes;

   private static String summary(long var0, long var2, String var4) {
      return String.format("%s=%d@%,dKB", var4, var0, var2 >> 10);
   }

   private static String summary(SummaryType var0) {
      switch (var0) {
         case TYPE_TEX:
            return summary((long)texData.size(), texBytes, " tex");
         case TYPE_RTT:
            return summary((long)rttData.size(), rttBytes, " rtt");
         case TYPE_ALL:
            return summary((long)(texData.size() + rttData.size()), texBytes + rttBytes, " combined");
         default:
            return "ERROR";
      }
   }

   private static long computeSize(int var0, int var1, int var2) {
      long var3 = (long)var0;
      var3 *= (long)var1;
      var3 *= (long)var2;
      return var3;
   }

   public static void textureCreated(long var0, int var2, int var3, long var4) {
      if (enabled) {
         texData.put(var0, var4);
         texBytes += var4;
         System.out.println("Created Texture: id=" + var0 + ", " + var2 + "x" + var3 + " pixels, " + var4 + " bytes," + summary(PrismTrace.SummaryType.TYPE_TEX) + summary(PrismTrace.SummaryType.TYPE_ALL));
      }
   }

   public static void textureCreated(long var0, int var2, int var3, int var4) {
      if (enabled) {
         long var5 = computeSize(var2, var3, var4);
         texData.put(var0, var5);
         texBytes += var5;
         System.out.println("Created Texture: id=" + var0 + ", " + var2 + "x" + var3 + " pixels, " + var5 + " bytes," + summary(PrismTrace.SummaryType.TYPE_TEX) + summary(PrismTrace.SummaryType.TYPE_ALL));
      }
   }

   public static void textureDisposed(long var0) {
      if (enabled) {
         Long var2 = (Long)texData.remove(var0);
         if (var2 == null) {
            throw new InternalError("Disposing unknown Texture " + var0);
         } else {
            texBytes -= var2;
            System.out.println("Disposed Texture: id=" + var0 + ", " + var2 + " bytes" + summary(PrismTrace.SummaryType.TYPE_TEX) + summary(PrismTrace.SummaryType.TYPE_ALL));
         }
      }
   }

   public static void rttCreated(long var0, int var2, int var3, long var4) {
      if (enabled) {
         rttData.put(var0, var4);
         rttBytes += var4;
         System.out.println("Created RTTexture: id=" + var0 + ", " + var2 + "x" + var3 + " pixels, " + var4 + " bytes," + summary(PrismTrace.SummaryType.TYPE_RTT) + summary(PrismTrace.SummaryType.TYPE_ALL));
      }
   }

   public static void rttCreated(long var0, int var2, int var3, int var4) {
      if (enabled) {
         long var5 = computeSize(var2, var3, var4);
         rttData.put(var0, var5);
         rttBytes += var5;
         System.out.println("Created RTTexture: id=" + var0 + ", " + var2 + "x" + var3 + " pixels, " + var5 + " bytes," + summary(PrismTrace.SummaryType.TYPE_RTT) + summary(PrismTrace.SummaryType.TYPE_ALL));
      }
   }

   public static void rttDisposed(long var0) {
      if (enabled) {
         Long var2 = (Long)rttData.remove(var0);
         if (var2 == null) {
            throw new InternalError("Disposing unknown RTTexture " + var0);
         } else {
            rttBytes -= var2;
            System.out.println("Disposed RTTexture: id=" + var0 + ", " + var2 + " bytes" + summary(PrismTrace.SummaryType.TYPE_RTT) + summary(PrismTrace.SummaryType.TYPE_ALL));
         }
      }
   }

   private PrismTrace() {
   }

   static {
      enabled = PrismSettings.printAllocs;
      if (enabled) {
         texData = new HashMap();
         rttData = new HashMap();
         Runtime.getRuntime().addShutdownHook(new Thread("RTT printAlloc shutdown hook") {
            public void run() {
               System.out.println("Final Texture resources:" + PrismTrace.summary(PrismTrace.SummaryType.TYPE_TEX) + PrismTrace.summary(PrismTrace.SummaryType.TYPE_RTT) + PrismTrace.summary(PrismTrace.SummaryType.TYPE_ALL));
            }
         });
      }

   }

   private static enum SummaryType {
      TYPE_TEX,
      TYPE_RTT,
      TYPE_ALL;
   }
}
