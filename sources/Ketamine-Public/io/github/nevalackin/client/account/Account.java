package io.github.nevalackin.client.account;

import com.google.gson.JsonObject;
import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.UserType;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.util.UUIDTypeAdapter;
import io.github.nevalackin.client.config.Serializable;
import io.github.nevalackin.client.notification.NotificationType;
import io.github.nevalackin.client.core.KetamineClient;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import java.net.Proxy;
import java.util.*;

public final class Account {

    private String username, accessToken, refreshToken;
    private long unbanTime;
    private boolean working;

    public Account(String username, String accessToken, String refreshToken, long unbanTime, boolean working) {
        this.username = username;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.unbanTime = unbanTime;
        this.working = working;
    }

    @Override
    public int hashCode() {
        // Lazy mans hashCode
        return System.identityHashCode(this.username);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getUnbanDate() {
        return unbanTime;
    }

    public void setUnbanDate(final long unbanDate) {
        this.unbanTime = unbanDate;
    }

    public boolean isBanned() {
        return this.unbanTime == -1337 || System.currentTimeMillis() < this.unbanTime;
    }

    public boolean isWorking() {
        return working;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    public JsonObject save() {
        final JsonObject object = new JsonObject();

        object.addProperty("username", this.username);
        object.addProperty("accessToken", this.accessToken);
        object.addProperty("refreshToken", this.refreshToken);

        object.addProperty("working", this.working);
        object.addProperty("unban_time", this.unbanTime);

        return object;
    }

    public static Account from(JsonObject object) {
        final AccountBuilder builder = new AccountBuilder();

        if (object.has("username"))
            builder.username(object.get("username").getAsString());

        if (object.has("accessToken"))
            builder.accessToken(object.get("accessToken").getAsString());

        if (object.has("refreshToken"))
            builder.refreshToken(object.get("refreshToken").getAsString());

        if (object.has("working"))
            builder.working(object.get("working").getAsBoolean());

        if (object.has("unban_time"))
            builder.unbanTime(object.get("unban_time").getAsLong());

        return builder.build();
    }

    public static AccountBuilder builder() {
        return new AccountBuilder();
    }

    public static class AccountBuilder {
        private String username = "", refreshToken = "", accessToken = "";
        private long unbanTime;
        private boolean working;

        private AccountBuilder() {
        }

        public AccountBuilder username(final String username) {
            this.username = username;

            return this;
        }

        public AccountBuilder refreshToken(final String refreshToken) {
            this.refreshToken = refreshToken;

            return this;
        }

        public AccountBuilder accessToken(final String accessToken) {
            this.accessToken = accessToken;

            return this;
        }

        public AccountBuilder unbanTime(final long unbanTime) {
            this.unbanTime = unbanTime;

            return this;
        }

        public AccountBuilder working(final boolean working) {
            this.working = working;

            return this;
        }

        public Account build() {
            return new Account(username, accessToken, refreshToken, unbanTime, working);
        }
    }
}
