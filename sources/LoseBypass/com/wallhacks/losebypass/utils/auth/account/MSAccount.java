/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package com.wallhacks.losebypass.utils.auth.account;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.realmsclient.gui.ChatFormatting;
import com.wallhacks.losebypass.utils.GuiUtil;
import com.wallhacks.losebypass.utils.auth.AuthException;
import com.wallhacks.losebypass.utils.auth.MCToken;
import com.wallhacks.losebypass.utils.auth.Profile;
import com.wallhacks.losebypass.utils.auth.Request;
import com.wallhacks.losebypass.utils.auth.XBLToken;
import com.wallhacks.losebypass.utils.auth.XSTSToken;
import com.wallhacks.losebypass.utils.auth.account.Account;
import com.wallhacks.losebypass.utils.auth.account.AccountType;
import java.util.HashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class MSAccount
extends Account {
    String refreshToken;
    String backupName;
    String accessToken = "";
    boolean invalid = false;

    public MSAccount(Session session, String refreshToken, String backupName, String uuid) {
        super(session, AccountType.MICROSOFT, uuid);
        this.refreshToken = refreshToken;
        this.backupName = backupName;
    }

    @Override
    public String getName() {
        if (this.session == null) return this.backupName;
        return this.session.getProfile().getName();
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public void refresh() {
        new RefreshThread().start();
    }

    @Override
    public void setSession() {
        new LoginThread().start();
    }

    @Override
    public String getStatus() {
        if (this.loading && !this.accessToken.equals("")) {
            return "Logging in" + GuiUtil.getLoadingText(false);
        }
        if (this.loading) {
            return "Refreshing token" + GuiUtil.getLoadingText(false);
        }
        if (!this.invalid) return "Microsoft account";
        return "Microsoft Account " + ChatFormatting.RED + "(Invalid)";
    }

    class LoginThread
    extends Thread {
        LoginThread() {
        }

        @Override
        public void run() {
            MSAccount.this.session = null;
            MSAccount.this.invalid = false;
            MSAccount.this.loading = true;
            try {
                XBLToken xblToken = new XBLToken(MSAccount.this.accessToken);
                XSTSToken xstsToken = new XSTSToken(xblToken.token);
                MCToken mcToken = new MCToken(xstsToken.userHash, xstsToken.token);
                Profile profile = new Profile(mcToken.token);
                MSAccount.this.session = new Session(profile.name, profile.uuid, mcToken.token, "mojang");
                MSAccount.this.uuid = profile.uuid;
                Minecraft.getMinecraft().session = MSAccount.this.session;
            }
            catch (Exception e) {
                MSAccount.this.invalid = true;
            }
            MSAccount.this.loading = false;
        }
    }

    class RefreshThread
    extends Thread {
        RefreshThread() {
        }

        @Override
        public void run() {
            MSAccount.this.loading = true;
            try {
                Request tokenRequest = new Request("https://login.live.com/oauth20_token.srf");
                tokenRequest.header("Content-Type", "application/x-www-form-urlencoded");
                HashMap<Object, Object> req = new HashMap<Object, Object>();
                req.put("client_id", "f187964d-b663-4d6f-8b35-71f146a1e5b7");
                req.put("refresh_token", MSAccount.this.refreshToken);
                req.put("redirect_uri", "http://localhost:48375");
                req.put("grant_type", "refresh_token");
                req.put("scope", "XboxLive.signin XboxLive.offline_access");
                req.put("client_secret", "hQH7Q~KLvutybVmaDO8YvJ2HrP_CATs_Lq-Wp");
                tokenRequest.post(req);
                if (tokenRequest.response() < 200) throw new AuthException("authCode2Token response: " + tokenRequest.response());
                if (tokenRequest.response() >= 300) {
                    throw new AuthException("authCode2Token response: " + tokenRequest.response());
                }
                Gson gson = new Gson();
                JsonObject resp = gson.fromJson(tokenRequest.body(), JsonObject.class);
                MSAccount.this.accessToken = resp.get("access_token").toString();
                MSAccount.this.refreshToken = resp.get("refresh_token").getAsString();
            }
            catch (Exception e) {
                e.printStackTrace();
                MSAccount.this.invalid = true;
            }
            MSAccount.this.loading = false;
        }
    }
}

