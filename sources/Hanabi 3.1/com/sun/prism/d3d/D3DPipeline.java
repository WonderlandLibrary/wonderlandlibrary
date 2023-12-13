package com.sun.prism.d3d;

import com.sun.glass.ui.Screen;
import com.sun.glass.utils.NativeLibLoader;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.ResourceFactory;
import com.sun.prism.impl.PrismSettings;
import java.security.AccessController;
import java.util.Iterator;
import java.util.List;

public final class D3DPipeline extends GraphicsPipeline {
   private static final boolean d3dEnabled = (Boolean)AccessController.doPrivileged(() -> {
      if (PrismSettings.verbose) {
         System.out.println("Loading D3D native library ...");
      }

      NativeLibLoader.loadLibrary("prism_d3d");
      if (PrismSettings.verbose) {
         System.out.println("\tsucceeded.");
      }

      return nInit(PrismSettings.class);
   });
   private static Thread creator;
   private static D3DPipeline theInstance;
   private static D3DResourceFactory[] factories;
   D3DResourceFactory _default;
   private int maxSamples = -1;

   public static D3DPipeline getInstance() {
      return theInstance;
   }

   private static boolean isDriverWarning(String var0) {
      return var0.contains("driver version");
   }

   private static void printDriverWarning(D3DDriverInformation var0) {
      if (var0 != null && var0.warningMessage != null && (PrismSettings.verbose || isDriverWarning(var0.warningMessage))) {
         System.out.println("Device \"" + var0.deviceDescription + "\" (" + var0.deviceName + ") initialization failed : ");
         System.out.println(var0.warningMessage);
      }

   }

   private static void printDriverWarning(int var0) {
      printDriverWarning(nGetDriverInformation(var0, new D3DDriverInformation()));
   }

   private static void printDriverInformation(int var0) {
      D3DDriverInformation var1 = nGetDriverInformation(var0, new D3DDriverInformation());
      if (var1 != null) {
         System.out.println("OS Information:");
         System.out.println("\t" + var1.getOsVersion() + " build " + var1.osBuildNumber);
         System.out.println("D3D Driver Information:");
         System.out.println("\t" + var1.deviceDescription);
         System.out.println("\t" + var1.deviceName);
         System.out.println("\tDriver " + var1.driverName + ", version " + var1.getDriverVersion());
         System.out.println("\tPixel Shader version " + var1.psVersionMajor + "." + var1.psVersionMinor);
         System.out.println("\tDevice : " + var1.getDeviceID());
         System.out.println("\tMax Multisamples supported: " + var1.maxSamples);
         if (var1.warningMessage != null) {
            System.out.println("\t *** " + var1.warningMessage);
         }
      }

   }

   private static void printDriverWarnings() {
      int var0 = 0;

      while(true) {
         D3DDriverInformation var1 = nGetDriverInformation(var0, new D3DDriverInformation());
         if (var1 == null) {
            return;
         }

         printDriverWarning(var1);
         ++var0;
      }
   }

   private D3DPipeline() {
   }

   public boolean init() {
      return d3dEnabled;
   }

   private static native boolean nInit(Class var0);

   private static native String nGetErrorMessage();

   private static native void nDispose();

   private static native int nGetAdapterOrdinal(long var0);

   private static native int nGetAdapterCount();

   private static native D3DDriverInformation nGetDriverInformation(int var0, D3DDriverInformation var1);

   private static native int nGetMaxSampleSupport(int var0);

   public void dispose() {
      if (creator != Thread.currentThread()) {
         throw new IllegalStateException("This operation is not permitted on the current thread [" + Thread.currentThread().getName() + "]");
      } else {
         notifyAllResourcesReleased();
         nDispose();

         for(int var1 = 0; var1 != factories.length; ++var1) {
            factories[var1] = null;
         }

         super.dispose();
      }
   }

   private static D3DResourceFactory createResourceFactory(int var0, Screen var1) {
      long var2 = D3DResourceFactory.nGetContext(var0);
      return var2 != 0L ? new D3DResourceFactory(var2, var1) : null;
   }

   private static D3DResourceFactory getD3DResourceFactory(int var0, Screen var1) {
      D3DResourceFactory var2 = factories[var0];
      if (var2 == null && var1 != null) {
         var2 = createResourceFactory(var0, var1);
         factories[var0] = var2;
      }

      return var2;
   }

   private static void notifyAllResourcesReleased() {
      D3DResourceFactory[] var0 = factories;
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         D3DResourceFactory var3 = var0[var2];
         if (var3 != null) {
            var3.notifyReleased();
         }
      }

   }

   private static Screen getScreenForAdapter(List var0, int var1) {
      Iterator var2 = var0.iterator();

      Screen var3;
      do {
         if (!var2.hasNext()) {
            return Screen.getMainScreen();
         }

         var3 = (Screen)var2.next();
      } while(var3.getAdapterOrdinal() != var1);

      return var3;
   }

   public int getAdapterOrdinal(Screen var1) {
      return nGetAdapterOrdinal(var1.getNativeScreen());
   }

   private static D3DResourceFactory findDefaultResourceFactory(List var0) {
      int var1 = 0;

      for(int var2 = nGetAdapterCount(); var1 != var2; ++var1) {
         D3DResourceFactory var3 = getD3DResourceFactory(var1, getScreenForAdapter(var0, var1));
         if (var3 != null) {
            if (PrismSettings.verbose) {
               printDriverInformation(var1);
            }

            return var3;
         }

         if (!PrismSettings.disableBadDriverWarning) {
            printDriverWarning(var1);
         }
      }

      return null;
   }

   public ResourceFactory getDefaultResourceFactory(List var1) {
      if (this._default == null) {
         this._default = findDefaultResourceFactory(var1);
      }

      return this._default;
   }

   public ResourceFactory getResourceFactory(Screen var1) {
      return getD3DResourceFactory(var1.getAdapterOrdinal(), var1);
   }

   public boolean is3DSupported() {
      return true;
   }

   int getMaxSamples() {
      if (this.maxSamples < 0) {
         this.isMSAASupported();
      }

      return this.maxSamples;
   }

   public boolean isMSAASupported() {
      if (this.maxSamples < 0) {
         this.maxSamples = nGetMaxSampleSupport(0);
      }

      return this.maxSamples > 0;
   }

   public boolean isVsyncSupported() {
      return true;
   }

   public boolean supportsShaderType(GraphicsPipeline.ShaderType var1) {
      switch (var1) {
         case HLSL:
            return true;
         default:
            return false;
      }
   }

   public boolean supportsShaderModel(GraphicsPipeline.ShaderModel var1) {
      switch (var1) {
         case SM3:
            return true;
         default:
            return false;
      }
   }

   static {
      if (PrismSettings.verbose) {
         System.out.println("Direct3D initialization " + (d3dEnabled ? "succeeded" : "failed"));
      }

      boolean var0 = PrismSettings.verbose || !PrismSettings.disableBadDriverWarning;
      if (!d3dEnabled && var0) {
         if (PrismSettings.verbose) {
            System.out.println(nGetErrorMessage());
         }

         printDriverWarnings();
      }

      creator = Thread.currentThread();
      if (d3dEnabled) {
         theInstance = new D3DPipeline();
         factories = new D3DResourceFactory[nGetAdapterCount()];
      }

   }
}
