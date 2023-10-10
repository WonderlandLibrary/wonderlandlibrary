package org.lwjgl.openal;

import java.nio.IntBuffer;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.Sys;

public final class AL {
   static ALCdevice device;
   static ALCcontext context;
   private static boolean created;

   private AL() {
   }

   private static native void nCreate(String var0) throws LWJGLException;

   private static native void nCreateDefault() throws LWJGLException;

   private static native void nDestroy();

   public static boolean isCreated() {
      return created;
   }

   public static void create(String var0, int var1, int var2, boolean var3) throws LWJGLException {
      create(var0, var1, var2, var3, true);
   }

   public static void create(String var0, int var1, int var2, boolean var3, boolean var4) throws LWJGLException {
      if (created) {
         throw new IllegalStateException("Only one OpenAL context may be instantiated at any one time.");
      } else {
         String var5;
         String[] var6;
         switch(LWJGLUtil.getPlatform()) {
         case 1:
            var5 = "openal";
            var6 = new String[]{"libopenal64.so", "libopenal.so", "libopenal.so.0"};
            break;
         case 2:
            var5 = "openal";
            var6 = new String[]{"openal.dylib"};
            break;
         case 3:
            var5 = "OpenAL32";
            var6 = new String[]{"OpenAL64.dll", "OpenAL32.dll"};
            break;
         default:
            throw new LWJGLException("Unknown platform: " + LWJGLUtil.getPlatform());
         }

         String[] var7 = LWJGLUtil.getLibraryPaths(var5, var6, AL.class.getClassLoader());
         LWJGLUtil.log("Found " + var7.length + " OpenAL paths");
         String[] var8 = var7;
         int var9 = var7.length;
         int var10 = 0;

         while(var10 < var9) {
            String var11 = var8[var10];

            try {
               nCreate(var11);
               created = true;
               init(var0, var1, var2, var3, var4);
               break;
            } catch (LWJGLException var13) {
               LWJGLUtil.log("Failed to load " + var11 + ": " + var13.getMessage());
               ++var10;
            }
         }

         if (!created && LWJGLUtil.getPlatform() == 2) {
            nCreateDefault();
            created = true;
            init(var0, var1, var2, var3, var4);
         }

         if (!created) {
            throw new LWJGLException("Could not locate OpenAL library.");
         }
      }
   }

   private static void init(String var0, int var1, int var2, boolean var3, boolean var4) throws LWJGLException {
      try {
         AL10.initNativeStubs();
         ALC10.initNativeStubs();
         if (var4) {
            device = ALC10.alcOpenDevice(var0);
            if (device == null) {
               throw new LWJGLException("Could not open ALC device");
            }

            if (var1 == -1) {
               context = ALC10.alcCreateContext(device, (IntBuffer)null);
            } else {
               context = ALC10.alcCreateContext(device, ALCcontext.createAttributeList(var1, var2, var3 ? 1 : 0));
            }

            ALC10.alcMakeContextCurrent(context);
         }
      } catch (LWJGLException var6) {
         destroy();
         throw var6;
      }

      ALC11.initialize();
      if (ALC10.alcIsExtensionPresent(device, "ALC_EXT_EFX")) {
         EFX10.initNativeStubs();
      }

   }

   public static void create() throws LWJGLException {
      create((String)null, 44100, 60, false);
   }

   public static void destroy() {
      if (context != null) {
         ALC10.alcMakeContextCurrent((ALCcontext)null);
         ALC10.alcDestroyContext(context);
         context = null;
      }

      if (device != null) {
         boolean var0 = ALC10.alcCloseDevice(device);
         device = null;
      }

      resetNativeStubs(AL10.class);
      resetNativeStubs(AL11.class);
      resetNativeStubs(ALC10.class);
      resetNativeStubs(ALC11.class);
      resetNativeStubs(EFX10.class);
      if (created) {
         nDestroy();
      }

      created = false;
   }

   private static native void resetNativeStubs(Class var0);

   public static ALCcontext getContext() {
      return context;
   }

   public static ALCdevice getDevice() {
      return device;
   }

   static {
      Sys.initialize();
   }
}
