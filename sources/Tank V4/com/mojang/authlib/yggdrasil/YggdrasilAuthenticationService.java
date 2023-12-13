package com.mojang.authlib.yggdrasil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.exceptions.UserMigratedException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.yggdrasil.response.ProfileSearchResultsResponse;
import com.mojang.authlib.yggdrasil.response.Response;
import com.mojang.util.UUIDTypeAdapter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Proxy;
import java.net.URL;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

public class YggdrasilAuthenticationService extends HttpAuthenticationService {
   private final String clientToken;
   private final Gson gson;

   public YggdrasilAuthenticationService(Proxy var1, String var2) {
      super(var1);
      this.clientToken = var2;
      GsonBuilder var3 = new GsonBuilder();
      var3.registerTypeAdapter(GameProfile.class, new YggdrasilAuthenticationService.GameProfileSerializer());
      var3.registerTypeAdapter(PropertyMap.class, new PropertyMap.Serializer());
      var3.registerTypeAdapter(UUID.class, new UUIDTypeAdapter());
      var3.registerTypeAdapter(ProfileSearchResultsResponse.class, new ProfileSearchResultsResponse.Serializer());
      this.gson = var3.create();
   }

   public UserAuthentication createUserAuthentication(Agent var1) {
      return new YggdrasilUserAuthentication(this, var1);
   }

   public MinecraftSessionService createMinecraftSessionService() {
      return new YggdrasilMinecraftSessionService(this);
   }

   public GameProfileRepository createProfileRepository() {
      return new YggdrasilGameProfileRepository(this);
   }

   protected Response makeRequest(URL var1, Object var2, Class var3) throws AuthenticationException {
      try {
         String var4 = var2 == null ? this.performGetRequest(var1) : this.performPostRequest(var1, this.gson.toJson(var2), "application/json");
         Response var5 = (Response)this.gson.fromJson(var4, var3);
         if (var5 == null) {
            return null;
         } else if (StringUtils.isNotBlank(var5.getError())) {
            if ("UserMigratedException".equals(var5.getCause())) {
               throw new UserMigratedException(var5.getErrorMessage());
            } else if (var5.getError().equals("ForbiddenOperationException")) {
               throw new InvalidCredentialsException(var5.getErrorMessage());
            } else {
               throw new AuthenticationException(var5.getErrorMessage());
            }
         } else {
            return var5;
         }
      } catch (IOException var6) {
         throw new AuthenticationUnavailableException("Cannot contact authentication server", var6);
      } catch (IllegalStateException var7) {
         throw new AuthenticationUnavailableException("Cannot contact authentication server", var7);
      } catch (JsonParseException var8) {
         throw new AuthenticationUnavailableException("Cannot contact authentication server", var8);
      }
   }

   public String getClientToken() {
      return this.clientToken;
   }

   private static class GameProfileSerializer implements JsonSerializer, JsonDeserializer {
      private GameProfileSerializer() {
      }

      public GameProfile deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         JsonObject var4 = (JsonObject)var1;
         UUID var5 = var4.has("id") ? (UUID)var3.deserialize(var4.get("id"), UUID.class) : null;
         String var6 = var4.has("name") ? var4.getAsJsonPrimitive("name").getAsString() : null;
         return new GameProfile(var5, var6);
      }

      public JsonElement serialize(GameProfile var1, Type var2, JsonSerializationContext var3) {
         JsonObject var4 = new JsonObject();
         if (var1.getId() != null) {
            var4.add("id", var3.serialize(var1.getId()));
         }

         if (var1.getName() != null) {
            var4.addProperty("name", var1.getName());
         }

         return var4;
      }

      public JsonElement serialize(Object var1, Type var2, JsonSerializationContext var3) {
         return this.serialize((GameProfile)var1, var2, var3);
      }

      public Object deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         return this.deserialize(var1, var2, var3);
      }

      GameProfileSerializer(Object var1) {
         this();
      }
   }
}
