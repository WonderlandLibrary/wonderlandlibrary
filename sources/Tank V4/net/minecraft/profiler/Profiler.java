package net.minecraft.profiler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.client.renderer.GlStateManager;
import optifine.Config;
import optifine.Lagometer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Profiler {
   private final Map profilingMap = Maps.newHashMap();
   private String profilingSection = "";
   private boolean profilerLocalEnabled;
   private static final int HASH_SCHEDULED_EXECUTABLES = "scheduledExecutables".hashCode();
   private static final int HASH_TICK = "tick".hashCode();
   private static final String SCHEDULED_EXECUTABLES = "scheduledExecutables";
   private static final String PRE_RENDER_ERRORS = "preRenderErrors";
   private static final String TICK = "tick";
   public boolean profilingEnabled;
   private static final int HASH_RENDER = "render".hashCode();
   private static final Logger logger = LogManager.getLogger();
   private static final String RENDER = "render";
   public boolean profilerGlobalEnabled = true;
   private static final String __OBFID = "CL_00001497";
   private final List timestampList = Lists.newArrayList();
   private final List sectionList = Lists.newArrayList();
   private static final int HASH_PRE_RENDER_ERRORS = "preRenderErrors".hashCode();
   private static final String DISPLAY = "display";
   private static final int HASH_DISPLAY = "display".hashCode();

   public List getProfilingData(String var1) {
      this.profilerLocalEnabled = this.profilerGlobalEnabled;
      if (!this.profilerLocalEnabled) {
         return new ArrayList(Arrays.asList(new Profiler.Result("root", 0.0D, 0.0D)));
      } else if (!this.profilingEnabled) {
         return null;
      } else {
         long var2 = this.profilingMap.containsKey("root") ? (Long)this.profilingMap.get("root") : 0L;
         long var4 = this.profilingMap.containsKey(var1) ? (Long)this.profilingMap.get(var1) : -1L;
         ArrayList var6 = Lists.newArrayList();
         if (var1.length() > 0) {
            var1 = var1 + ".";
         }

         long var7 = 0L;
         Iterator var10 = this.profilingMap.keySet().iterator();

         while(var10.hasNext()) {
            Object var9 = var10.next();
            if (((String)var9).length() > var1.length() && ((String)var9).startsWith(var1) && ((String)var9).indexOf(".", var1.length() + 1) < 0) {
               var7 += (Long)this.profilingMap.get(var9);
            }
         }

         float var20 = (float)var7;
         if (var7 < var4) {
            var7 = var4;
         }

         if (var2 < var7) {
            var2 = var7;
         }

         Iterator var11 = this.profilingMap.keySet().iterator();

         Object var21;
         while(var11.hasNext()) {
            var21 = var11.next();
            String var12 = (String)var21;
            if (var12.length() > var1.length() && var12.startsWith(var1) && var12.indexOf(".", var1.length() + 1) < 0) {
               long var13 = (Long)this.profilingMap.get(var12);
               double var15 = (double)var13 * 100.0D / (double)var7;
               double var17 = (double)var13 * 100.0D / (double)var2;
               String var19 = var12.substring(var1.length());
               var6.add(new Profiler.Result(var19, var15, var17));
            }
         }

         var11 = this.profilingMap.keySet().iterator();

         while(var11.hasNext()) {
            var21 = var11.next();
            this.profilingMap.put(var21, (Long)this.profilingMap.get(var21) * 950L / 1000L);
         }

         if ((float)var7 > var20) {
            var6.add(new Profiler.Result("unspecified", (double)((float)var7 - var20) * 100.0D / (double)var7, (double)((float)var7 - var20) * 100.0D / (double)var2));
         }

         Collections.sort(var6);
         var6.add(0, new Profiler.Result(var1, 100.0D, (double)var7 * 100.0D / (double)var2));
         return var6;
      }
   }

   public Profiler() {
      this.profilerLocalEnabled = this.profilerGlobalEnabled;
   }

   public void endStartSection(String var1) {
      if (this.profilerLocalEnabled) {
         this.endSection();
         this.startSection(var1);
      }

   }

   public void startSection(String var1) {
      int var2;
      if (Lagometer.isActive()) {
         var2 = var1.hashCode();
         if (var2 == HASH_SCHEDULED_EXECUTABLES && var1.equals("scheduledExecutables")) {
            Lagometer.timerScheduledExecutables.start();
         } else if (var2 == HASH_TICK && var1.equals("tick") && Config.isMinecraftThread()) {
            Lagometer.timerScheduledExecutables.end();
            Lagometer.timerTick.start();
         } else if (var2 == HASH_PRE_RENDER_ERRORS && var1.equals("preRenderErrors")) {
            Lagometer.timerTick.end();
         }
      }

      if (Config.isFastRender()) {
         var2 = var1.hashCode();
         if (var2 == HASH_RENDER && var1.equals("render")) {
            GlStateManager.clearEnabled = false;
         } else if (var2 == HASH_DISPLAY && var1.equals("display")) {
            GlStateManager.clearEnabled = true;
         }
      }

      if (this.profilerLocalEnabled && this.profilingEnabled) {
         if (this.profilingSection.length() > 0) {
            this.profilingSection = this.profilingSection + ".";
         }

         this.profilingSection = this.profilingSection + var1;
         this.sectionList.add(this.profilingSection);
         this.timestampList.add(System.nanoTime());
      }

   }

   public void clearProfiling() {
      this.profilingMap.clear();
      this.profilingSection = "";
      this.sectionList.clear();
      this.profilerLocalEnabled = this.profilerGlobalEnabled;
   }

   public String getNameOfLastSection() {
      return this.sectionList.size() == 0 ? "[UNKNOWN]" : (String)this.sectionList.get(this.sectionList.size() - 1);
   }

   public void endSection() {
      if (this.profilerLocalEnabled && this.profilingEnabled) {
         long var1 = System.nanoTime();
         long var3 = (Long)this.timestampList.remove(this.timestampList.size() - 1);
         this.sectionList.remove(this.sectionList.size() - 1);
         long var5 = var1 - var3;
         if (this.profilingMap.containsKey(this.profilingSection)) {
            this.profilingMap.put(this.profilingSection, (Long)this.profilingMap.get(this.profilingSection) + var5);
         } else {
            this.profilingMap.put(this.profilingSection, var5);
         }

         if (var5 > 100000000L) {
            logger.warn("Something's taking too long! '" + this.profilingSection + "' took aprox " + (double)var5 / 1000000.0D + " ms");
         }

         this.profilingSection = !this.sectionList.isEmpty() ? (String)this.sectionList.get(this.sectionList.size() - 1) : "";
      }

   }

   public static final class Result implements Comparable {
      public double field_76330_b;
      private static final String __OBFID = "CL_00001498";
      public String field_76331_c;
      public double field_76332_a;

      public int compareTo(Object var1) {
         return this.compareTo((Profiler.Result)var1);
      }

      public int func_76329_a() {
         return (this.field_76331_c.hashCode() & 11184810) + 4473924;
      }

      public Result(String var1, double var2, double var4) {
         this.field_76331_c = var1;
         this.field_76332_a = var2;
         this.field_76330_b = var4;
      }

      public int compareTo(Profiler.Result var1) {
         return var1.field_76332_a < this.field_76332_a ? -1 : (var1.field_76332_a > this.field_76332_a ? 1 : var1.field_76331_c.compareTo(this.field_76331_c));
      }
   }
}
