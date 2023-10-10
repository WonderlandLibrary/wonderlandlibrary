package com.mojang.authlib.yggdrasil.response;

public class Response {
   private String error;
   private String errorMessage;
   private String cause;

   public String getError() {
      return this.error;
   }

   public String getCause() {
      return this.cause;
   }

   public String getErrorMessage() {
      return this.errorMessage;
   }

   protected void setError(String var1) {
      this.error = var1;
   }

   protected void setErrorMessage(String var1) {
      this.errorMessage = var1;
   }

   protected void setCause(String var1) {
      this.cause = var1;
   }
}
