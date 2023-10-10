package com.thealtening.auth;

import com.thealtening.auth.service.AlteningServiceType;
import com.thealtening.auth.service.ServiceSwitcher;

public final class TheAlteningAuthentication {
   private final ServiceSwitcher serviceSwitcher = new ServiceSwitcher();
   private final SSLController sslController = new SSLController();
   private static TheAlteningAuthentication instance;
   private AlteningServiceType service;
   private static volatile int[] $SWITCH_TABLE$com$thealtening$auth$service$AlteningServiceType;

   private TheAlteningAuthentication(AlteningServiceType var1) {
      this.updateService(var1);
   }

   public void updateService(AlteningServiceType var1) {
      if (var1 != null && this.service != var1) {
         switch($SWITCH_TABLE$com$thealtening$auth$service$AlteningServiceType()[var1.ordinal()]) {
         case 1:
            this.sslController.enableCertificateValidation();
            break;
         case 2:
            this.sslController.disableCertificateValidation();
         }

         this.service = this.serviceSwitcher.switchToService(var1);
      }
   }

   public AlteningServiceType getService() {
      return this.service;
   }

   public static TheAlteningAuthentication mojang() {
      return withService(AlteningServiceType.MOJANG);
   }

   public static TheAlteningAuthentication theAltening() {
      return withService(AlteningServiceType.THEALTENING);
   }

   private static TheAlteningAuthentication withService(AlteningServiceType var0) {
      if (instance == null) {
         instance = new TheAlteningAuthentication(var0);
      } else if (instance.getService() != var0) {
         instance.updateService(var0);
      }

      return instance;
   }

   static int[] $SWITCH_TABLE$com$thealtening$auth$service$AlteningServiceType() {
      int[] var10000 = $SWITCH_TABLE$com$thealtening$auth$service$AlteningServiceType;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[AlteningServiceType.values().length];

         try {
            var0[AlteningServiceType.MOJANG.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[AlteningServiceType.THEALTENING.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$com$thealtening$auth$service$AlteningServiceType = var0;
         return var0;
      }
   }
}
