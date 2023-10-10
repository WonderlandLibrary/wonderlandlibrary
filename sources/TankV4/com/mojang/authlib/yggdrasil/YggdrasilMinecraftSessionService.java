package com.mojang.authlib.yggdrasil;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.mojang.authlib.AuthenticationService;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.minecraft.HttpMinecraftSessionService;
import com.mojang.authlib.minecraft.InsecureTextureException;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.yggdrasil.request.JoinMinecraftServerRequest;
import com.mojang.authlib.yggdrasil.response.HasJoinedMinecraftServerResponse;
import com.mojang.authlib.yggdrasil.response.MinecraftProfilePropertiesResponse;
import com.mojang.authlib.yggdrasil.response.MinecraftTexturesPayload;
import com.mojang.authlib.yggdrasil.response.Response;
import com.mojang.util.UUIDTypeAdapter;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class YggdrasilMinecraftSessionService extends HttpMinecraftSessionService {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final String BASE_URL = "https://sessionserver.mojang.com/session/minecraft/";
   private static final URL JOIN_URL = HttpAuthenticationService.constantURL("https://sessionserver.mojang.com/session/minecraft/join");
   private static final URL CHECK_URL = HttpAuthenticationService.constantURL("https://sessionserver.mojang.com/session/minecraft/hasJoined");
   private final PublicKey publicKey;
   private final Gson gson = (new GsonBuilder()).registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create();
   private final LoadingCache insecureProfiles;

   protected YggdrasilMinecraftSessionService(YggdrasilAuthenticationService var1) {
      super(var1);
      this.insecureProfiles = CacheBuilder.newBuilder().expireAfterWrite(6L, TimeUnit.HOURS).build(new CacheLoader(this) {
         final YggdrasilMinecraftSessionService this$0;

         {
            this.this$0 = var1;
         }

         public GameProfile load(GameProfile var1) throws Exception {
            return this.this$0.fillGameProfile(var1, false);
         }

         public Object load(Object var1) throws Exception {
            return this.load((GameProfile)var1);
         }
      });

      try {
         X509EncodedKeySpec var2 = new X509EncodedKeySpec(IOUtils.toByteArray(YggdrasilMinecraftSessionService.class.getResourceAsStream("/yggdrasil_session_pubkey.der")));
         KeyFactory var3 = KeyFactory.getInstance("RSA");
         this.publicKey = var3.generatePublic(var2);
      } catch (Exception var4) {
         throw new Error("Missing/invalid yggdrasil public key!");
      }
   }

   public void joinServer(GameProfile var1, String var2, String var3) throws AuthenticationException {
      JoinMinecraftServerRequest var4 = new JoinMinecraftServerRequest();
      var4.accessToken = var2;
      var4.selectedProfile = var1.getId();
      var4.serverId = var3;
      this.getAuthenticationService().makeRequest(JOIN_URL, var4, Response.class);
   }

   public GameProfile hasJoinedServer(GameProfile var1, String var2) throws AuthenticationUnavailableException {
      HashMap var3 = new HashMap();
      var3.put("username", var1.getName());
      var3.put("serverId", var2);
      URL var4 = HttpAuthenticationService.concatenateURL(CHECK_URL, HttpAuthenticationService.buildQuery(var3));

      try {
         HasJoinedMinecraftServerResponse var5 = (HasJoinedMinecraftServerResponse)this.getAuthenticationService().makeRequest(var4, (Object)null, HasJoinedMinecraftServerResponse.class);
         if (var5 != null && var5.getId() != null) {
            GameProfile var6 = new GameProfile(var5.getId(), var1.getName());
            if (var5.getProperties() != null) {
               var6.getProperties().putAll(var5.getProperties());
            }

            return var6;
         } else {
            return null;
         }
      } catch (AuthenticationUnavailableException var7) {
         throw var7;
      } catch (AuthenticationException var8) {
         return null;
      }
   }

   public Map getTextures(GameProfile var1, boolean var2) {
      Property var3 = (Property)Iterables.getFirst(var1.getProperties().get("textures"), (Object)null);
      if (var3 == null) {
         return new HashMap();
      } else {
         if (var2) {
            if (!var3.hasSignature()) {
               LOGGER.error("Signature is missing from textures payload");
               throw new InsecureTextureException("Signature is missing from textures payload");
            }

            if (!var3.isSignatureValid(this.publicKey)) {
               LOGGER.error("Textures payload has been tampered with (signature invalid)");
               throw new InsecureTextureException("Textures payload has been tampered with (signature invalid)");
            }
         }

         MinecraftTexturesPayload var4;
         try {
            String var5 = new String(Base64.decodeBase64(var3.getValue()), Charsets.UTF_8);
            var4 = (MinecraftTexturesPayload)this.gson.fromJson(var5, MinecraftTexturesPayload.class);
         } catch (JsonParseException var6) {
            LOGGER.error((String)"Could not decode textures payload", (Throwable)var6);
            return new HashMap();
         }

         return (Map)(var4.getTextures() == null ? new HashMap() : var4.getTextures());
      }
   }

   public GameProfile fillProfileProperties(GameProfile var1, boolean var2) {
      if (var1.getId() == null) {
         return var1;
      } else {
         return !var2 ? (GameProfile)this.insecureProfiles.getUnchecked(var1) : this.fillGameProfile(var1, true);
      }
   }

   protected GameProfile fillGameProfile(GameProfile var1, boolean var2) {
      try {
         URL var3 = HttpAuthenticationService.constantURL("https://sessionserver.mojang.com/session/minecraft/profile/" + UUIDTypeAdapter.fromUUID(var1.getId()));
         var3 = HttpAuthenticationService.concatenateURL(var3, "unsigned=" + !var2);
         MinecraftProfilePropertiesResponse var4 = (MinecraftProfilePropertiesResponse)this.getAuthenticationService().makeRequest(var3, (Object)null, MinecraftProfilePropertiesResponse.class);
         if (var4 == null) {
            LOGGER.debug("Couldn't fetch profile properties for " + var1 + " as the profile does not exist");
            return var1;
         } else {
            GameProfile var5 = new GameProfile(var4.getId(), var4.getName());
            var5.getProperties().putAll(var4.getProperties());
            var1.getProperties().putAll(var4.getProperties());
            LOGGER.debug("Successfully fetched profile properties for " + var1);
            return var5;
         }
      } catch (AuthenticationException var6) {
         LOGGER.warn((String)("Couldn't look up profile properties for " + var1), (Throwable)var6);
         return var1;
      }
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
