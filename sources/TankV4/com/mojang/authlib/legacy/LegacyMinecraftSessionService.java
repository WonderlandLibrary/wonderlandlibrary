package com.mojang.authlib.legacy;

import com.mojang.authlib.AuthenticationService;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.minecraft.HttpMinecraftSessionService;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LegacyMinecraftSessionService extends HttpMinecraftSessionService {
   private static final String BASE_URL = "http://session.minecraft.net/game/";
   private static final URL JOIN_URL = HttpAuthenticationService.constantURL("http://session.minecraft.net/game/joinserver.jsp");
   private static final URL CHECK_URL = HttpAuthenticationService.constantURL("http://session.minecraft.net/game/checkserver.jsp");

   protected LegacyMinecraftSessionService(LegacyAuthenticationService var1) {
      super(var1);
   }

   public void joinServer(GameProfile var1, String var2, String var3) throws AuthenticationException {
      HashMap var4 = new HashMap();
      var4.put("user", var1.getName());
      var4.put("sessionId", var2);
      var4.put("serverId", var3);
      URL var5 = HttpAuthenticationService.concatenateURL(JOIN_URL, HttpAuthenticationService.buildQuery(var4));

      try {
         String var6 = this.getAuthenticationService().performGetRequest(var5);
         if (!var6.equals("OK")) {
            throw new AuthenticationException(var6);
         }
      } catch (IOException var7) {
         throw new AuthenticationUnavailableException(var7);
      }
   }

   public GameProfile hasJoinedServer(GameProfile var1, String var2) throws AuthenticationUnavailableException {
      HashMap var3 = new HashMap();
      var3.put("user", var1.getName());
      var3.put("serverId", var2);
      URL var4 = HttpAuthenticationService.concatenateURL(CHECK_URL, HttpAuthenticationService.buildQuery(var3));

      try {
         String var5 = this.getAuthenticationService().performGetRequest(var4);
         return var5.equals("YES") ? var1 : null;
      } catch (IOException var6) {
         throw new AuthenticationUnavailableException(var6);
      }
   }

   public Map getTextures(GameProfile var1, boolean var2) {
      return new HashMap();
   }

   public GameProfile fillProfileProperties(GameProfile var1, boolean var2) {
      return var1;
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
