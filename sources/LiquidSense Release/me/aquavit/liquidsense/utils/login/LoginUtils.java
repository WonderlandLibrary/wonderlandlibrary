package me.aquavit.liquidsense.utils.login;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.utils.mc.MinecraftInstance;
import me.aquavit.liquidsense.event.events.SessionEvent;
import net.minecraft.util.Session;

import java.io.IOException;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class LoginUtils extends MinecraftInstance {
    public static LoginResult login(final String username, final String password) {
        final YggdrasilUserAuthentication userAuthentication = (YggdrasilUserAuthentication)new YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
        userAuthentication.setUsername(username);
        userAuthentication.setPassword(password);
        try {
            userAuthentication.logIn();
            mc.session = new Session(userAuthentication.getSelectedProfile().getName(), userAuthentication.getSelectedProfile().getId().toString(), userAuthentication.getAuthenticatedToken(), "mojang");
            LiquidSense.eventManager.callEvent(new SessionEvent());
            return LoginResult.LOGGED;
        }
        catch (AuthenticationUnavailableException exception2) {
            return LoginResult.NO_CONTACT;
        }
        catch (AuthenticationException exception) {
            if (exception.getMessage().contains("Invalid username or password.")) {
                return LoginResult.INVALID_ACCOUNT_DATA;
            }
            if (exception.getMessage().toLowerCase().contains("account migrated")) {
                return LoginResult.MIGRATED;
            }
            return LoginResult.NO_CONTACT;
        }
        catch (NullPointerException exception3) {
            return LoginResult.WRONG_PASSWORD;
        }
    }

    public static void loginCracked(final String username) {
        if (username == null) return;
        mc.session = new Session(username, UserUtils.getUUID(username), "-", "legacy");
        LiquidSense.eventManager.callEvent(new SessionEvent());
    }

    public static LoginResult loginSessionId(String sessionId) throws IOException {
        String decodedSessionData;
        try {
            decodedSessionData = new String(Base64.getDecoder().decode(sessionId.split(".")[1]), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return LoginResult.FAILED_PARSE_TOKEN;
        }

        JsonObject sessionObject = new JsonParser().parse(decodedSessionData).getAsJsonObject();
        String uuid = sessionObject.get("spr").getAsString();
        String accessToken = sessionObject.get("yggt").getAsString();

        if (UserUtils.isValidToken(accessToken)) {
            return LoginResult.INVALID_ACCOUNT_DATA;
        }

        String username = UserUtils.getUsername(uuid);
        if (username == null) {
            return LoginResult.INVALID_ACCOUNT_DATA;
        }

        mc.session = new Session(username, uuid, accessToken, "mojang");
        LiquidSense.eventManager.callEvent(new SessionEvent());
        return LoginResult.LOGGED;
    }

    public enum LoginResult
    {
        WRONG_PASSWORD,
        NO_CONTACT,
        INVALID_ACCOUNT_DATA,
        MIGRATED,
        LOGGED,
        FAILED_PARSE_TOKEN;
    }
}
