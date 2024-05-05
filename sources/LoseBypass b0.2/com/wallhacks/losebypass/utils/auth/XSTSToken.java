/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.utils.auth;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.wallhacks.losebypass.utils.auth.AuthException;
import com.wallhacks.losebypass.utils.auth.Request;
import java.io.IOException;

public class XSTSToken {
    public String token;
    public String userHash;

    public XSTSToken(String XBLToken2) throws AuthException {
        try {
            Request pr = new Request("https://xsts.auth.xboxlive.com/xsts/authorize");
            pr.header("Content-Type", "application/json");
            pr.header("Accept", "application/json");
            JsonObject req = new JsonObject();
            JsonObject reqProps = new JsonObject();
            JsonArray userTokens = new JsonArray();
            userTokens.add(XBLToken2);
            reqProps.add("UserTokens", userTokens);
            reqProps.addProperty("SandboxId", "RETAIL");
            req.add("Properties", reqProps);
            req.addProperty("RelyingParty", "rp://api.minecraftservices.com/");
            req.addProperty("TokenType", "JWT");
            pr.post(req.toString());
            if (pr.response() == 401) {
                throw new AuthException("No XBox account found");
            }
            if (pr.response() < 200) throw new AuthException("authXSTS response: " + pr.response());
            if (pr.response() >= 300) {
                throw new AuthException("authXSTS response: " + pr.response());
            }
            Gson gson = new Gson();
            JsonObject json = gson.fromJson(pr.body(), JsonObject.class);
            this.userHash = json.getAsJsonObject("DisplayClaims").getAsJsonArray("xui").get(0).getAsJsonObject().get("uhs").getAsString();
            this.token = json.get("Token").toString();
            this.token = this.token.substring(1, this.token.length() - 1);
            return;
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new AuthException("Failed getting the XSTSToken");
        }
    }
}

