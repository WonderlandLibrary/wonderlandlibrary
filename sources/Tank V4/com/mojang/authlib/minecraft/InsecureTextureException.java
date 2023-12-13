package com.mojang.authlib.minecraft;

import com.mojang.authlib.GameProfile;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class InsecureTextureException extends RuntimeException {
   public InsecureTextureException(String var1) {
      super(var1);
   }

   public static class MissingTextureException extends InsecureTextureException {
      public MissingTextureException() {
         super("No texture information found");
      }
   }

   public static class WrongTextureOwnerException extends InsecureTextureException {
      private final GameProfile expected;
      private final UUID resultId;
      private final String resultName;

      public WrongTextureOwnerException(GameProfile var1, UUID var2, String var3) {
         super("Decrypted textures payload was for another user (expected " + var1.getId() + "/" + var1.getName() + " but was for " + var2 + "/" + var3 + ")");
         this.expected = var1;
         this.resultId = var2;
         this.resultName = var3;
      }
   }

   public static class OutdatedTextureException extends InsecureTextureException {
      private final Date validFrom;
      private final Calendar limit;

      public OutdatedTextureException(Date var1, Calendar var2) {
         super("Decrypted textures payload is too old (" + var1 + ", but we need it to be at least " + var2 + ")");
         this.validFrom = var1;
         this.limit = var2;
      }
   }
}
