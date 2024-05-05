/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.utils.auth;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wallhacks.losebypass.utils.auth.AuthException;
import com.wallhacks.losebypass.utils.auth.Request;
import java.io.IOException;

public class Profile {
    public String uuid;
    public String name;
    public String token;

    public Profile(String accessToken) throws AuthException {
        try {
            this.token = accessToken;
            Request request = new Request("https://api.minecraftservices.com/entitlements/mcstore");
            request.header("Authorization", "Bearer " + accessToken);
            request.get();
            if (request.response() < 200) throw new IllegalArgumentException("checkGameOwnership response: " + request.response());
            if (request.response() >= 300) {
                throw new IllegalArgumentException("checkGameOwnership response: " + request.response());
            }
            Gson gson = new Gson();
            if (gson.fromJson(request.body(), JsonObject.class).getAsJsonArray("items").size() == 0) {
                throw new AuthException("Game not owned");
            }
            Request pRequest = new Request("https://api.minecraftservices.com/minecraft/profile");
            pRequest.header("Authorization", "Bearer " + accessToken);
            pRequest.get();
            if (pRequest.response() < 200) throw new IllegalArgumentException("getProfile response: " + pRequest.response());
            if (pRequest.response() >= 300) {
                throw new IllegalArgumentException("getProfile response: " + pRequest.response());
            }
            JsonObject resp = gson.fromJson(pRequest.body(), JsonObject.class);
            this.uuid = resp.get("id").getAsString();
            this.name = resp.get("name").getAsString();
            return;
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new AuthException("Failed getting mc profile");
        }
    }
}

