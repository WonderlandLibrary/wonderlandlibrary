package org.lwjgl.openal;

public final class Util {
   private Util() {
   }

   public static void checkALCError(ALCdevice var0) {
      int var1 = ALC10.alcGetError(var0);
      if (var1 != 0) {
         throw new OpenALException(ALC10.alcGetString(AL.getDevice(), var1));
      }
   }

   public static void checkALError() {
      int var0 = AL10.alGetError();
      if (var0 != 0) {
         throw new OpenALException(var0);
      }
   }

   public static void checkALCValidDevice(ALCdevice var0) {
      if (!var0.isValid()) {
         throw new OpenALException("Invalid device: " + var0);
      }
   }

   public static void checkALCValidContext(ALCcontext var0) {
      if (!var0.isValid()) {
         throw new OpenALException("Invalid context: " + var0);
      }
   }
}
