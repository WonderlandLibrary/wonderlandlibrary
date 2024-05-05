/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.utils.auth;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wallhacks.losebypass.utils.auth.AuthException;
import com.wallhacks.losebypass.utils.auth.Request;
import java.io.IOException;
import java.util.HashMap;

public class MSToken {
    public String refresh;
    public String access;

    public MSToken(String code) throws AuthException {
        try {
            Request tokenRequest = new Request("https://login.live.com/oauth20_token.srf");
            tokenRequest.header("Content-Type", "application/x-www-form-urlencoded");
            HashMap<Object, Object> req = new HashMap<Object, Object>();
            req.put("client_id", "f187964d-b663-4d6f-8b35-71f146a1e5b7");
            req.put("code", code);
            req.put("redirect_uri", "http://localhost:48375");
            req.put("grant_type", "authorization_code");
            req.put("scope", "XboxLive.signin XboxLive.offline_access");
            req.put("client_secret", "hQH7Q~KLvutybVmaDO8YvJ2HrP_CATs_Lq-Wp");
            tokenRequest.post(req);
            if (tokenRequest.response() < 200) throw new AuthException("authCode2Token response: " + tokenRequest.response());
            if (tokenRequest.response() >= 300) {
                throw new AuthException("authCode2Token response: " + tokenRequest.response());
            }
            Gson gson = new Gson();
            JsonObject resp = gson.fromJson(tokenRequest.body(), JsonObject.class);
            this.refresh = resp.get("refresh_token").getAsString();
            this.access = resp.get("access_token").getAsString();
            return;
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new AuthException("Failed getting token");
        }
    }
}

