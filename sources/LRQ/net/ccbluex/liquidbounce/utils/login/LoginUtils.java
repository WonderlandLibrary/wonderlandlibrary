/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  com.mojang.authlib.Agent
 *  com.mojang.authlib.UserAuthentication
 *  com.mojang.authlib.exceptions.AuthenticationException
 *  com.mojang.authlib.exceptions.AuthenticationUnavailableException
 *  com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
 *  com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication
 *  kotlin.TypeCastException
 *  kotlin.jvm.JvmStatic
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.Charsets
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.utils.login;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.Agent;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import java.net.Proxy;
import java.nio.charset.Charset;
import java.util.Base64;
import kotlin.TypeCastException;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.SessionEvent;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.login.UserUtils;
import org.jetbrains.annotations.Nullable;

public final class LoginUtils
extends MinecraftInstance {
    public static final LoginUtils INSTANCE;

    @JvmStatic
    public static final LoginResult login(@Nullable String username, @Nullable String password) {
        LoginResult loginResult;
        UserAuthentication userAuthentication = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
        if (userAuthentication == null) {
            throw new TypeCastException("null cannot be cast to non-null type com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication");
        }
        YggdrasilUserAuthentication userAuthentication2 = (YggdrasilUserAuthentication)userAuthentication;
        userAuthentication2.setUsername(username);
        userAuthentication2.setPassword(password);
        try {
            userAuthentication2.logIn();
            MinecraftInstance.mc.setSession(MinecraftInstance.classProvider.createSession(userAuthentication2.getSelectedProfile().getName(), userAuthentication2.getSelectedProfile().getId().toString(), userAuthentication2.getAuthenticatedToken(), "mojang"));
            LiquidBounce.INSTANCE.getEventManager().callEvent(new SessionEvent());
            loginResult = LoginResult.LOGGED;
        }
        catch (AuthenticationUnavailableException exception) {
            loginResult = LoginResult.NO_CONTACT;
        }
        catch (AuthenticationException exception) {
            String string = exception.getMessage();
            if (string == null) {
                Intrinsics.throwNpe();
            }
            String message = string;
            loginResult = StringsKt.contains((CharSequence)message, (CharSequence)"invalid username or password.", (boolean)true) ? LoginResult.INVALID_ACCOUNT_DATA : (StringsKt.contains((CharSequence)message, (CharSequence)"account migrated", (boolean)true) ? LoginResult.MIGRATED : LoginResult.NO_CONTACT);
        }
        catch (NullPointerException exception) {
            loginResult = LoginResult.WRONG_PASSWORD;
        }
        return loginResult;
    }

    @JvmStatic
    public static final void loginCracked(@Nullable String username) {
        String string = username;
        if (string == null) {
            Intrinsics.throwNpe();
        }
        MinecraftInstance.mc.setSession(MinecraftInstance.classProvider.createSession(string, UserUtils.INSTANCE.getUUID(username), "-", "legacy"));
        LiquidBounce.INSTANCE.getEventManager().callEvent(new SessionEvent());
    }

    @JvmStatic
    public static final LoginResult loginSessionId(String sessionId) {
        JsonObject e2;
        Object object;
        try {
            object = Base64.getDecoder().decode((String)StringsKt.split$default((CharSequence)sessionId, (String[])new String[]{"."}, (boolean)false, (int)0, (int)6, null).get(1));
            Charset charset = Charsets.UTF_8;
            boolean bl = false;
            object = new String((byte[])object, charset);
        }
        catch (Exception e2) {
            return LoginResult.FAILED_PARSE_TOKEN;
        }
        Object decodedSessionData = object;
        try {
            e2 = new JsonParser().parse((String)decodedSessionData).getAsJsonObject();
        }
        catch (Exception e3) {
            return LoginResult.FAILED_PARSE_TOKEN;
        }
        JsonObject sessionObject = e2;
        String uuid = sessionObject.get("spr").getAsString();
        String accessToken = sessionObject.get("yggt").getAsString();
        if (!UserUtils.INSTANCE.isValidToken(accessToken)) {
            return LoginResult.INVALID_ACCOUNT_DATA;
        }
        String string = UserUtils.INSTANCE.getUsername(uuid);
        if (string == null) {
            return LoginResult.INVALID_ACCOUNT_DATA;
        }
        String username = string;
        MinecraftInstance.mc.setSession(MinecraftInstance.classProvider.createSession(username, uuid, accessToken, "mojang"));
        LiquidBounce.INSTANCE.getEventManager().callEvent(new SessionEvent());
        return LoginResult.LOGGED;
    }

    private LoginUtils() {
    }

    static {
        LoginUtils loginUtils;
        INSTANCE = loginUtils = new LoginUtils();
    }

    public static final class LoginResult
    extends Enum<LoginResult> {
        public static final /* enum */ LoginResult WRONG_PASSWORD;
        public static final /* enum */ LoginResult NO_CONTACT;
        public static final /* enum */ LoginResult INVALID_ACCOUNT_DATA;
        public static final /* enum */ LoginResult MIGRATED;
        public static final /* enum */ LoginResult LOGGED;
        public static final /* enum */ LoginResult FAILED_PARSE_TOKEN;
        private static final /* synthetic */ LoginResult[] $VALUES;

        static {
            LoginResult[] loginResultArray = new LoginResult[6];
            LoginResult[] loginResultArray2 = loginResultArray;
            loginResultArray[0] = WRONG_PASSWORD = new LoginResult();
            loginResultArray[1] = NO_CONTACT = new LoginResult();
            loginResultArray[2] = INVALID_ACCOUNT_DATA = new LoginResult();
            loginResultArray[3] = MIGRATED = new LoginResult();
            loginResultArray[4] = LOGGED = new LoginResult();
            loginResultArray[5] = FAILED_PARSE_TOKEN = new LoginResult();
            $VALUES = loginResultArray;
        }

        public static LoginResult[] values() {
            return (LoginResult[])$VALUES.clone();
        }

        public static LoginResult valueOf(String string) {
            return Enum.valueOf(LoginResult.class, string);
        }
    }
}

