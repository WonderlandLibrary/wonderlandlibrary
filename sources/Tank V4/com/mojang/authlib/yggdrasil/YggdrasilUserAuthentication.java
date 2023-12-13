package com.mojang.authlib.yggdrasil;

import com.mojang.authlib.Agent;
import com.mojang.authlib.AuthenticationService;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.HttpUserAuthentication;
import com.mojang.authlib.UserType;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.yggdrasil.request.AuthenticationRequest;
import com.mojang.authlib.yggdrasil.request.RefreshRequest;
import com.mojang.authlib.yggdrasil.response.AuthenticationResponse;
import com.mojang.authlib.yggdrasil.response.RefreshResponse;
import com.mojang.authlib.yggdrasil.response.User;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class YggdrasilUserAuthentication extends HttpUserAuthentication {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final String BASE_URL = "https://authserver.mojang.com/";
   private static final URL ROUTE_AUTHENTICATE = HttpAuthenticationService.constantURL("https://authserver.mojang.com/authenticate");
   private static final URL ROUTE_REFRESH = HttpAuthenticationService.constantURL("https://authserver.mojang.com/refresh");
   private static final URL ROUTE_VALIDATE = HttpAuthenticationService.constantURL("https://authserver.mojang.com/validate");
   private static final URL ROUTE_INVALIDATE = HttpAuthenticationService.constantURL("https://authserver.mojang.com/invalidate");
   private static final URL ROUTE_SIGNOUT = HttpAuthenticationService.constantURL("https://authserver.mojang.com/signout");
   private static final String STORAGE_KEY_ACCESS_TOKEN = "accessToken";
   private final Agent agent;
   private GameProfile[] profiles;
   private String accessToken;
   private boolean isOnline;

   public YggdrasilUserAuthentication(YggdrasilAuthenticationService var1, Agent var2) {
      super(var1);
      this.agent = var2;
   }

   public boolean canLogIn() {
      return this != false && StringUtils.isNotBlank(this.getUsername()) && (StringUtils.isNotBlank(this.getPassword()) || StringUtils.isNotBlank(this.getAuthenticatedToken()));
   }

   public void logIn() throws AuthenticationException {
      if (StringUtils.isBlank(this.getUsername())) {
         throw new InvalidCredentialsException("Invalid username");
      } else {
         if (StringUtils.isNotBlank(this.getAuthenticatedToken())) {
            this.logInWithToken();
         } else {
            if (!StringUtils.isNotBlank(this.getPassword())) {
               throw new InvalidCredentialsException("Invalid password");
            }

            this.logInWithPassword();
         }

      }
   }

   protected void logInWithPassword() throws AuthenticationException {
      if (StringUtils.isBlank(this.getUsername())) {
         throw new InvalidCredentialsException("Invalid username");
      } else if (StringUtils.isBlank(this.getPassword())) {
         throw new InvalidCredentialsException("Invalid password");
      } else {
         LOGGER.info("Logging in with username & password");
         AuthenticationRequest var1 = new AuthenticationRequest(this, this.getUsername(), this.getPassword());
         AuthenticationResponse var2 = (AuthenticationResponse)this.getAuthenticationService().makeRequest(ROUTE_AUTHENTICATE, var1, AuthenticationResponse.class);
         if (!var2.getClientToken().equals(this.getAuthenticationService().getClientToken())) {
            throw new AuthenticationException("Server requested we change our client token. Don't know how to handle this!");
         } else {
            if (var2.getSelectedProfile() != null) {
               this.setUserType(var2.getSelectedProfile().isLegacy() ? UserType.LEGACY : UserType.MOJANG);
            } else if (ArrayUtils.isNotEmpty((Object[])var2.getAvailableProfiles())) {
               this.setUserType(var2.getAvailableProfiles()[0].isLegacy() ? UserType.LEGACY : UserType.MOJANG);
            }

            User var3 = var2.getUser();
            if (var3 != null && var3.getId() != null) {
               this.setUserid(var3.getId());
            } else {
               this.setUserid(this.getUsername());
            }

            this.isOnline = true;
            this.accessToken = var2.getAccessToken();
            this.profiles = var2.getAvailableProfiles();
            this.setSelectedProfile(var2.getSelectedProfile());
            this.getModifiableUserProperties().clear();
            this.updateUserProperties(var3);
         }
      }
   }

   protected void updateUserProperties(User var1) {
      if (var1 != null) {
         if (var1.getProperties() != null) {
            this.getModifiableUserProperties().putAll(var1.getProperties());
         }

      }
   }

   protected void logInWithToken() throws AuthenticationException {
      if (StringUtils.isBlank(this.getUserID())) {
         if (!StringUtils.isBlank(this.getUsername())) {
            throw new InvalidCredentialsException("Invalid uuid & username");
         }

         this.setUserid(this.getUsername());
      }

      if (StringUtils.isBlank(this.getAuthenticatedToken())) {
         throw new InvalidCredentialsException("Invalid access token");
      } else {
         LOGGER.info("Logging in with access token");
         RefreshRequest var1 = new RefreshRequest(this);
         RefreshResponse var2 = (RefreshResponse)this.getAuthenticationService().makeRequest(ROUTE_REFRESH, var1, RefreshResponse.class);
         if (!var2.getClientToken().equals(this.getAuthenticationService().getClientToken())) {
            throw new AuthenticationException("Server requested we change our client token. Don't know how to handle this!");
         } else {
            if (var2.getSelectedProfile() != null) {
               this.setUserType(var2.getSelectedProfile().isLegacy() ? UserType.LEGACY : UserType.MOJANG);
            } else if (ArrayUtils.isNotEmpty((Object[])var2.getAvailableProfiles())) {
               this.setUserType(var2.getAvailableProfiles()[0].isLegacy() ? UserType.LEGACY : UserType.MOJANG);
            }

            if (var2.getUser() != null && var2.getUser().getId() != null) {
               this.setUserid(var2.getUser().getId());
            } else {
               this.setUserid(this.getUsername());
            }

            this.isOnline = true;
            this.accessToken = var2.getAccessToken();
            this.profiles = var2.getAvailableProfiles();
            this.setSelectedProfile(var2.getSelectedProfile());
            this.getModifiableUserProperties().clear();
            this.updateUserProperties(var2.getUser());
         }
      }
   }

   public void logOut() {
      super.logOut();
      this.accessToken = null;
      this.profiles = null;
      this.isOnline = false;
   }

   public GameProfile[] getAvailableProfiles() {
      return this.profiles;
   }

   public boolean isLoggedIn() {
      return StringUtils.isNotBlank(this.accessToken);
   }

   public void selectGameProfile(GameProfile var1) throws AuthenticationException {
      if (!this.isLoggedIn()) {
         throw new AuthenticationException("Cannot change game profile whilst not logged in");
      } else if (this.getSelectedProfile() != null) {
         throw new AuthenticationException("Cannot change game profile. You must log out and back in.");
      } else if (var1 != null && ArrayUtils.contains(this.profiles, var1)) {
         RefreshRequest var2 = new RefreshRequest(this, var1);
         RefreshResponse var3 = (RefreshResponse)this.getAuthenticationService().makeRequest(ROUTE_REFRESH, var2, RefreshResponse.class);
         if (!var3.getClientToken().equals(this.getAuthenticationService().getClientToken())) {
            throw new AuthenticationException("Server requested we change our client token. Don't know how to handle this!");
         } else {
            this.isOnline = true;
            this.accessToken = var3.getAccessToken();
            this.setSelectedProfile(var3.getSelectedProfile());
         }
      } else {
         throw new IllegalArgumentException("Invalid profile '" + var1 + "'");
      }
   }

   public void loadFromStorage(Map var1) {
      super.loadFromStorage(var1);
      this.accessToken = String.valueOf(var1.get("accessToken"));
   }

   public Map saveForStorage() {
      Map var1 = super.saveForStorage();
      if (StringUtils.isNotBlank(this.getAuthenticatedToken())) {
         var1.put("accessToken", this.getAuthenticatedToken());
      }

      return var1;
   }

   /** @deprecated */
   @Deprecated
   public String getSessionToken() {
      return this.isLoggedIn() && this.getSelectedProfile() != null && this != false ? String.format("token:%s:%s", this.getAuthenticatedToken(), this.getSelectedProfile().getId()) : null;
   }

   public String getAuthenticatedToken() {
      return this.accessToken;
   }

   public Agent getAgent() {
      return this.agent;
   }

   public String toString() {
      return "YggdrasilAuthenticationService{agent=" + this.agent + ", profiles=" + Arrays.toString(this.profiles) + ", selectedProfile=" + this.getSelectedProfile() + ", username='" + this.getUsername() + '\'' + ", isLoggedIn=" + this.isLoggedIn() + ", userType=" + this.getUserType() + ", canPlayOnline=" + this.canPlayOnline() + ", accessToken='" + this.accessToken + '\'' + ", clientToken='" + this.getAuthenticationService().getClientToken() + '\'' + '}';
   }

   public YggdrasilAuthenticationService getAuthenticationService() {
      return (YggdrasilAuthenticationService)super.getAuthenticationService();
   }

   public HttpAuthenticationService getAuthenticationService() {
      return this.getAuthenticationService();
   }

   public AuthenticationService getAuthenticationService() {
      return this.getAuthenticationService();
   }
}
