/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  com.mojang.authlib.Agent
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.UserAuthentication
 *  com.mojang.authlib.exceptions.AuthenticationException
 *  com.mojang.authlib.exceptions.AuthenticationUnavailableException
 *  com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
 *  com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.Session
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.utils.login;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import java.net.Proxy;
import java.nio.charset.Charset;
import java.util.Base64;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.SessionEvent;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.login.UserUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0001\fB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001c\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006H\u0007J\u0012\u0010\b\u001a\u00020\t2\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0007J\u0010\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0006H\u0007\u00a8\u0006\r"}, d2={"Lnet/ccbluex/liquidbounce/utils/login/LoginUtils;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "()V", "login", "Lnet/ccbluex/liquidbounce/utils/login/LoginUtils$LoginResult;", "username", "", "password", "loginCracked", "", "loginSessionId", "sessionId", "LoginResult", "KyinoClient"})
public final class LoginUtils
extends MinecraftInstance {
    public static final LoginUtils INSTANCE;

    @JvmStatic
    @NotNull
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
            Minecraft minecraft = LoginUtils.access$getMc$p$s1046033730();
            GameProfile gameProfile = userAuthentication2.getSelectedProfile();
            Intrinsics.checkExpressionValueIsNotNull(gameProfile, "userAuthentication.selectedProfile");
            String string = gameProfile.getName();
            GameProfile gameProfile2 = userAuthentication2.getSelectedProfile();
            Intrinsics.checkExpressionValueIsNotNull(gameProfile2, "userAuthentication.selectedProfile");
            minecraft.field_71449_j = new Session(string, gameProfile2.getId().toString(), userAuthentication2.getAuthenticatedToken(), "mojang");
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
            loginResult = StringsKt.contains((CharSequence)message, "invalid username or password.", true) ? LoginResult.INVALID_ACCOUNT_DATA : (StringsKt.contains((CharSequence)message, "account migrated", true) ? LoginResult.MIGRATED : LoginResult.NO_CONTACT);
        }
        catch (NullPointerException exception) {
            loginResult = LoginResult.WRONG_PASSWORD;
        }
        return loginResult;
    }

    @JvmStatic
    public static final void loginCracked(@Nullable String username) {
        Minecraft minecraft = LoginUtils.access$getMc$p$s1046033730();
        String string = username;
        if (string == null) {
            Intrinsics.throwNpe();
        }
        minecraft.field_71449_j = new Session(username, UserUtils.INSTANCE.getUUID(string), "-", "legacy");
        LiquidBounce.INSTANCE.getEventManager().callEvent(new SessionEvent());
    }

    @JvmStatic
    @NotNull
    public static final LoginResult loginSessionId(@NotNull String sessionId) {
        String accessToken;
        JsonObject e2;
        Object object;
        Intrinsics.checkParameterIsNotNull(sessionId, "sessionId");
        try {
            byte[] byArray = Base64.getDecoder().decode((String)StringsKt.split$default((CharSequence)sessionId, new String[]{"."}, false, 0, 6, null).get(1));
            Intrinsics.checkExpressionValueIsNotNull(byArray, "Base64.getDecoder().deco\u2026(sessionId.split(\".\")[1])");
            object = byArray;
            Charset charset = Charsets.UTF_8;
            boolean bl = false;
            object = new String((byte[])object, charset);
        }
        catch (Exception e2) {
            return LoginResult.FAILED_PARSE_TOKEN;
        }
        Object decodedSessionData = object;
        try {
            JsonElement jsonElement = new JsonParser().parse((String)decodedSessionData);
            Intrinsics.checkExpressionValueIsNotNull(jsonElement, "JsonParser().parse(decodedSessionData)");
            e2 = jsonElement.getAsJsonObject();
        }
        catch (Exception e3) {
            return LoginResult.FAILED_PARSE_TOKEN;
        }
        JsonObject sessionObject = e2;
        JsonElement jsonElement = sessionObject.get("spr");
        Intrinsics.checkExpressionValueIsNotNull(jsonElement, "sessionObject.get(\"spr\")");
        String uuid = jsonElement.getAsString();
        JsonElement jsonElement2 = sessionObject.get("yggt");
        Intrinsics.checkExpressionValueIsNotNull(jsonElement2, "sessionObject.get(\"yggt\")");
        String string = accessToken = jsonElement2.getAsString();
        Intrinsics.checkExpressionValueIsNotNull(string, "accessToken");
        if (!UserUtils.INSTANCE.isValidToken(string)) {
            return LoginResult.INVALID_ACCOUNT_DATA;
        }
        String string2 = uuid;
        Intrinsics.checkExpressionValueIsNotNull(string2, "uuid");
        String string3 = UserUtils.INSTANCE.getUsername(string2);
        if (string3 == null) {
            return LoginResult.INVALID_ACCOUNT_DATA;
        }
        String username = string3;
        LoginUtils.access$getMc$p$s1046033730().field_71449_j = new Session(username, uuid, accessToken, "mojang");
        LiquidBounce.INSTANCE.getEventManager().callEvent(new SessionEvent());
        return LoginResult.LOGGED;
    }

    private LoginUtils() {
    }

    static {
        LoginUtils loginUtils;
        INSTANCE = loginUtils = new LoginUtils();
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\b\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\b\u00a8\u0006\t"}, d2={"Lnet/ccbluex/liquidbounce/utils/login/LoginUtils$LoginResult;", "", "(Ljava/lang/String;I)V", "WRONG_PASSWORD", "NO_CONTACT", "INVALID_ACCOUNT_DATA", "MIGRATED", "LOGGED", "FAILED_PARSE_TOKEN", "KyinoClient"})
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

