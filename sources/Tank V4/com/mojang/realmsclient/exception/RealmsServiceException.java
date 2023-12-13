package com.mojang.realmsclient.exception;

import com.mojang.realmsclient.client.RealmsError;
import net.minecraft.realms.RealmsScreen;

public class RealmsServiceException extends Exception {
   public final int httpResultCode;
   public final String httpResponseContent;
   public final int errorCode;
   public final String errorMsg;

   public RealmsServiceException(int var1, String var2, RealmsError var3) {
      super(var2);
      this.httpResultCode = var1;
      this.httpResponseContent = var2;
      this.errorCode = var3.getErrorCode();
      this.errorMsg = var3.getErrorMessage();
   }

   public RealmsServiceException(int var1, String var2, int var3, String var4) {
      super(var2);
      this.httpResultCode = var1;
      this.httpResponseContent = var2;
      this.errorCode = var3;
      this.errorMsg = var4;
   }

   public String toString() {
      return this.errorCode != -1 ? "Realms ( ErrorCode: " + this.errorCode + " ): " + RealmsScreen.getLocalizedString("mco.errorMessage." + this.errorCode) : "Realms (" + this.httpResultCode + ") " + this.httpResponseContent;
   }
}
