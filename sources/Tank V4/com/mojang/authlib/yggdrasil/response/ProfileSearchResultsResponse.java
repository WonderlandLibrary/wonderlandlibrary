package com.mojang.authlib.yggdrasil.response;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.authlib.GameProfile;
import java.lang.reflect.Type;

public class ProfileSearchResultsResponse extends Response {
   private GameProfile[] profiles;

   public GameProfile[] getProfiles() {
      return this.profiles;
   }

   static GameProfile[] access$002(ProfileSearchResultsResponse var0, GameProfile[] var1) {
      return var0.profiles = var1;
   }

   public static class Serializer implements JsonDeserializer {
      public ProfileSearchResultsResponse deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         ProfileSearchResultsResponse var4 = new ProfileSearchResultsResponse();
         if (var1 instanceof JsonObject) {
            JsonObject var5 = (JsonObject)var1;
            if (var5.has("error")) {
               var4.setError(var5.getAsJsonPrimitive("error").getAsString());
            }

            if (var5.has("errorMessage")) {
               var4.setError(var5.getAsJsonPrimitive("errorMessage").getAsString());
            }

            if (var5.has("cause")) {
               var4.setError(var5.getAsJsonPrimitive("cause").getAsString());
            }
         } else {
            ProfileSearchResultsResponse.access$002(var4, (GameProfile[])var3.deserialize(var1, GameProfile[].class));
         }

         return var4;
      }

      public Object deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         return this.deserialize(var1, var2, var3);
      }
   }
}
