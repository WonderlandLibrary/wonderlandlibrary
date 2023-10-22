package me.ratsiel.auth.model.microsoft;

import me.ratsiel.auth.abstracts.AuthenticationToken;

public class MicrosoftToken extends AuthenticationToken {
   protected String token;
   protected String refreshToken;

   public MicrosoftToken() {
   }

   public MicrosoftToken(String token, String refreshToken) {
      this.token = token;
      this.refreshToken = refreshToken;
   }

   public String getToken() {
      return this.token;
   }

   public String getRefreshToken() {
      return this.refreshToken;
   }
}
