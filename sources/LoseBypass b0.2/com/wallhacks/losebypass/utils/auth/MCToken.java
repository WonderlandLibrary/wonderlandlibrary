/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.utils.auth;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wallhacks.losebypass.utils.auth.AuthException;
import com.wallhacks.losebypass.utils.auth.Request;
import java.io.IOException;

public class MCToken {
    public String token;

    public MCToken(String userHash, String xstsToken) throws AuthException {
        try {
            Request request = new Request("https://api.minecraftservices.com/authentication/login_with_xbox");
            request.header("Content-Type", "application/json");
            request.header("Accept", "application/json");
            JsonObject req = new JsonObject();
            req.addProperty("identityToken", "XBL3.0 x=" + userHash + ";" + xstsToken);
            request.post(req.toString());
            if (request.response() < 200) throw new AuthException("authMinecraft response: " + request.response());
            if (request.response() >= 300) {
                throw new AuthException("authMinecraft response: " + request.response());
            }
            Gson gson = new Gson();
            this.token = gson.fromJson(request.body(), JsonObject.class).get("access_token").getAsString();
            return;
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new AuthException("Failed getting mcToken");
        }
    }
}

