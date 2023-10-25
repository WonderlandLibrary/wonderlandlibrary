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
  
  public static class Serializer implements JsonDeserializer<ProfileSearchResultsResponse> {
    public ProfileSearchResultsResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      ProfileSearchResultsResponse result = new ProfileSearchResultsResponse();
      if (json instanceof JsonObject) {
        JsonObject object = (JsonObject)json;
        if (object.has("error"))
          result.setError(object.getAsJsonPrimitive("error").getAsString()); 
        if (object.has("errorMessage"))
          result.setError(object.getAsJsonPrimitive("errorMessage").getAsString()); 
        if (object.has("cause"))
          result.setError(object.getAsJsonPrimitive("cause").getAsString()); 
      } else {
        result.profiles = (GameProfile[])context.deserialize(json, GameProfile[].class);
      } 
      return result;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\authlib\yggdrasil\response\ProfileSearchResultsResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */