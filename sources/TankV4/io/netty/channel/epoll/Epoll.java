package io.netty.channel.epoll;

public final class Epoll {
   private static final Throwable UNAVAILABILITY_CAUSE;

   public static boolean isAvailable() {
      return UNAVAILABILITY_CAUSE == null;
   }

   public static void ensureAvailability() {
      if (UNAVAILABILITY_CAUSE != null) {
         throw (Error)(new UnsatisfiedLinkError("failed to load the required native library")).initCause(UNAVAILABILITY_CAUSE);
      }
   }

   public static Throwable unavailabilityCause() {
      return UNAVAILABILITY_CAUSE;
   }

   private Epoll() {
   }

   static {
      Throwable var0 = null;
      byte var1 = -1;
      byte var2 = -1;

      label61: {
         int var11;
         int var12;
         try {
            var11 = Native.epollCreate();
            var12 = Native.eventFd();
         } catch (Throwable var10) {
            var0 = var10;
            if (var1 != -1) {
               try {
                  Native.close(var1);
               } catch (Exception var7) {
               }
            }

            if (var2 != -1) {
               try {
                  Native.close(var2);
               } catch (Exception var6) {
               }
            }
            break label61;
         }

         if (var11 != -1) {
            try {
               Native.close(var11);
            } catch (Exception var9) {
            }
         }

         if (var12 != -1) {
            try {
               Native.close(var12);
            } catch (Exception var8) {
            }
         }
      }

      if (var0 != null) {
         UNAVAILABILITY_CAUSE = var0;
      } else {
         UNAVAILABILITY_CAUSE = null;
      }

   }
}
