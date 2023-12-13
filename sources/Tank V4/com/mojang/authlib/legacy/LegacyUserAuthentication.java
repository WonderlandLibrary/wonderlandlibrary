package com.mojang.authlib.legacy;

import com.mojang.authlib.AuthenticationService;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.HttpUserAuthentication;
import com.mojang.authlib.UserType;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.util.UUIDTypeAdapter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;

public class LegacyUserAuthentication extends HttpUserAuthentication {
   private static final URL AUTHENTICATION_URL = HttpAuthenticationService.constantURL("https://login.minecraft.net");
   private static final int AUTHENTICATION_VERSION = 14;
   private static final int RESPONSE_PART_PROFILE_NAME = 2;
   private static final int RESPONSE_PART_SESSION_TOKEN = 3;
   private static final int RESPONSE_PART_PROFILE_ID = 4;
   private String sessionToken;

   protected LegacyUserAuthentication(LegacyAuthenticationService var1) {
      super(var1);
   }

   public void logIn() throws AuthenticationException {
      if (StringUtils.isBlank(this.getUsername())) {
         throw new InvalidCredentialsException("Invalid username");
      } else if (StringUtils.isBlank(this.getPassword())) {
         throw new InvalidCredentialsException("Invalid password");
      } else {
         HashMap var1 = new HashMap();
         var1.put("user", this.getUsername());
         var1.put("password", this.getPassword());
         var1.put("version", 14);

         String var2;
         try {
            var2 = this.getAuthenticationService().performPostRequest(AUTHENTICATION_URL, HttpAuthenticationService.buildQuery(var1), "application/x-www-form-urlencoded").trim();
         } catch (IOException var7) {
            throw new AuthenticationException("Authentication server is not responding", var7);
         }

         String[] var3 = var2.split(":");
         if (var3.length == 5) {
            String var4 = var3[4];
            String var5 = var3[2];
            String var6 = var3[3];
            if (!StringUtils.isBlank(var4) && !StringUtils.isBlank(var5) && !StringUtils.isBlank(var6)) {
               this.setSelectedProfile(new GameProfile(UUIDTypeAdapter.fromString(var4), var5));
               this.sessionToken = var6;
               this.setUserType(UserType.LEGACY);
            } else {
               throw new AuthenticationException("Unknown response from authentication server: " + var2);
            }
         } else {
            throw new InvalidCredentialsException(var2);
         }
      }
   }

   public void logOut() {
      super.logOut();
      this.sessionToken = null;
   }

   public boolean canPlayOnline() {
      return this.isLoggedIn() && this.getSelectedProfile() != null && this.getAuthenticatedToken() != null;
   }

   public GameProfile[] getAvailableProfiles() {
      return this.getSelectedProfile() != null ? new GameProfile[]{this.getSelectedProfile()} : new GameProfile[0];
   }

   public void selectGameProfile(GameProfile var1) throws AuthenticationException {
      throw new UnsupportedOperationException("Game profiles cannot be changed in the legacy authentication service");
   }

   public String getAuthenticatedToken() {
      return this.sessionToken;
   }

   public String getUserID() {
      return this.getUsername();
   }

   public LegacyAuthenticationService getAuthenticationService() {
      return (LegacyAuthenticationService)super.getAuthenticationService();
   }

   public HttpAuthenticationService getAuthenticationService() {
      return this.getAuthenticationService();
   }

   public AuthenticationService getAuthenticationService() {
      return this.getAuthenticationService();
   }
}
