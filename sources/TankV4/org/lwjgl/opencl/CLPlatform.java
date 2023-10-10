package org.lwjgl.opencl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.api.Filter;

public final class CLPlatform extends CLObject {
   private static final CLPlatform.CLPlatformUtil util = (CLPlatform.CLPlatformUtil)getInfoUtilInstance(CLPlatform.class, "CL_PLATFORM_UTIL");
   private static final FastLongMap clPlatforms = new FastLongMap();
   private final CLObjectRegistry clDevices;
   private Object caps;

   CLPlatform(long var1) {
      super(var1);
      if (this.isValid()) {
         clPlatforms.put(var1, this);
         this.clDevices = new CLObjectRegistry();
      } else {
         this.clDevices = null;
      }

   }

   public static CLPlatform getCLPlatform(long var0) {
      return (CLPlatform)clPlatforms.get(var0);
   }

   public CLDevice getCLDevice(long var1) {
      return (CLDevice)this.clDevices.getObject(var1);
   }

   static InfoUtil getInfoUtilInstance(Class var0, String var1) {
      InfoUtil var2 = null;

      try {
         Class var3 = Class.forName("org.lwjgl.opencl.InfoUtilFactory");
         var2 = (InfoUtil)var3.getDeclaredField(var1).get((Object)null);
      } catch (Exception var4) {
      }

      return var2;
   }

   public static List getPlatforms() {
      return getPlatforms((Filter)null);
   }

   public static List getPlatforms(Filter var0) {
      return util.getPlatforms(var0);
   }

   public String getInfoString(int var1) {
      return util.getInfoString(this, var1);
   }

   public List getDevices(int var1) {
      return this.getDevices(var1, (Filter)null);
   }

   public List getDevices(int var1, Filter var2) {
      return util.getDevices(this, var1, var2);
   }

   void setCapabilities(Object var1) {
      this.caps = var1;
   }

   Object getCapabilities() {
      return this.caps;
   }

   static void registerCLPlatforms(PointerBuffer var0, IntBuffer var1) {
      if (var0 != null) {
         int var2 = var0.position();
         int var3 = Math.min(var1.get(0), var0.remaining());

         for(int var4 = 0; var4 < var3; ++var4) {
            long var5 = var0.get(var2 + var4);
            if (!clPlatforms.containsKey(var5)) {
               new CLPlatform(var5);
            }
         }

      }
   }

   CLObjectRegistry getCLDeviceRegistry() {
      return this.clDevices;
   }

   void registerCLDevices(PointerBuffer var1, IntBuffer var2) {
      int var3 = var1.position();
      int var4 = Math.min(var2.get(var2.position()), var1.remaining());

      for(int var5 = 0; var5 < var4; ++var5) {
         long var6 = var1.get(var3 + var5);
         if (!this.clDevices.hasObject(var6)) {
            new CLDevice(var6, this);
         }
      }

   }

   void registerCLDevices(ByteBuffer var1, PointerBuffer var2) {
      int var3 = var1.position();
      int var4 = Math.min((int)var2.get(var2.position()), var1.remaining()) / PointerBuffer.getPointerSize();

      for(int var5 = 0; var5 < var4; ++var5) {
         int var6 = var3 + var5 * PointerBuffer.getPointerSize();
         long var7 = PointerBuffer.is64Bit() ? var1.getLong(var6) : (long)var1.getInt(var6);
         if (!this.clDevices.hasObject(var7)) {
            new CLDevice(var7, this);
         }
      }

   }

   interface CLPlatformUtil extends InfoUtil {
      List getPlatforms(Filter var1);

      List getDevices(CLPlatform var1, int var2, Filter var3);
   }
}
