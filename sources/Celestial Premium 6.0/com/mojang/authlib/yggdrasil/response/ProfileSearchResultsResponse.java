/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.authlib.yggdrasil.response;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.yggdrasil.response.Response;
import java.lang.reflect.Type;

public class ProfileSearchResultsResponse
extends Response {
    private GameProfile[] profiles;

    public GameProfile[] getProfiles() {
        return this.profiles;
    }

    static /* synthetic */ GameProfile[] access$002(ProfileSearchResultsResponse x0, GameProfile[] x1) {
        x0.profiles = x1;
        return x1;
    }

    public static class Serializer
    implements JsonDeserializer<ProfileSearchResultsResponse> {
        @Override
        public ProfileSearchResultsResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ProfileSearchResultsResponse result = new ProfileSearchResultsResponse();
            if (json instanceof JsonObject) {
                JsonObject object = (JsonObject)json;
                if (object.has("error")) {
                    result.setError(object.getAsJsonPrimitive("error").getAsString());
                }
                if (object.has("errorMessage")) {
                    result.setError(object.getAsJsonPrimitive("errorMessage").getAsString());
                }
                if (object.has("cause")) {
                    result.setError(object.getAsJsonPrimitive("cause").getAsString());
                }
            } else {
                ProfileSearchResultsResponse.access$002(result, (GameProfile[])context.deserialize(json, (Type)((Object)GameProfile[].class)));
            }
            return result;
        }
    }
}

