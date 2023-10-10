package org.lwjgl.opencl;

import org.lwjgl.PointerBuffer;

public final class CLDevice extends CLObjectChild {
   private static final InfoUtil util = CLPlatform.getInfoUtilInstance(CLDevice.class, "CL_DEVICE_UTIL");
   private final CLPlatform platform;
   private final CLObjectRegistry subCLDevices;
   private Object caps;

   CLDevice(long var1, CLPlatform var3) {
      this(var1, (CLDevice)null, var3);
   }

   CLDevice(long var1, CLDevice var3) {
      this(var1, var3, var3.getPlatform());
   }

   CLDevice(long var1, CLDevice var3, CLPlatform var4) {
      super(var1, var3);
      if (this.isValid()) {
         this.platform = var4;
         var4.getCLDeviceRegistry().registerObject(this);
         this.subCLDevices = new CLObjectRegistry();
         if (var3 != null) {
            var3.subCLDevices.registerObject(this);
         }
      } else {
         this.platform = null;
         this.subCLDevices = null;
      }

   }

   public CLPlatform getPlatform() {
      return this.platform;
   }

   public CLDevice getSubCLDevice(long var1) {
      return (CLDevice)this.subCLDevices.getObject(var1);
   }

   public String getInfoString(int var1) {
      return util.getInfoString(this, var1);
   }

   public int getInfoInt(int var1) {
      return util.getInfoInt(this, var1);
   }

   public boolean getInfoBoolean(int var1) {
      return util.getInfoInt(this, var1) != 0;
   }

   public long getInfoSize(int var1) {
      return util.getInfoSize(this, var1);
   }

   public long[] getInfoSizeArray(int var1) {
      return util.getInfoSizeArray(this, var1);
   }

   public long getInfoLong(int var1) {
      return util.getInfoLong(this, var1);
   }

   void setCapabilities(Object var1) {
      this.caps = var1;
   }

   Object getCapabilities() {
      return this.caps;
   }

   int retain() {
      return this.getParent() == null ? this.getReferenceCount() : super.retain();
   }

   int release() {
      if (this.getParent() == null) {
         return this.getReferenceCount();
      } else {
         int var1 = super.release();
         if (!this.isValid()) {
            ((CLDevice)this.getParent()).subCLDevices.unregisterObject(this);
         }

         return var1;
      }
   }

   CLObjectRegistry getSubCLDeviceRegistry() {
      return this.subCLDevices;
   }

   void registerSubCLDevices(PointerBuffer var1) {
      for(int var2 = var1.position(); var2 < var1.limit(); ++var2) {
         long var3 = var1.get(var2);
         if (var3 != 0L) {
            new CLDevice(var3, this);
         }
      }

   }
}
