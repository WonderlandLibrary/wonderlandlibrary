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
 *  net.minecraft.util.Session
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.utils.login;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.Agent;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import java.net.Proxy;
import java.util.Base64;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.event.SessionEvent;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.login.UserUtils;
import net.minecraft.util.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0001\fB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001c\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006H\u0007J\u0012\u0010\b\u001a\u00020\t2\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0007J\u0010\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0006H\u0007\u00a8\u0006\r"}, d2={"Lnet/dev/important/utils/login/LoginUtils;", "Lnet/dev/important/utils/MinecraftInstance;", "()V", "login", "Lnet/dev/important/utils/login/LoginUtils$LoginResult;", "username", "", "password", "loginCracked", "", "loginSessionId", "sessionId", "LoginResult", "LiquidBounce"})
public final class LoginUtils
extends MinecraftInstance {
    @NotNull
    public static final LoginUtils INSTANCE = new LoginUtils();

    private LoginUtils() {
    }

    @JvmStatic
    @NotNull
    public static final LoginResult login(@Nullable String username, @Nullable String password) {
        LoginResult loginResult;
        UserAuthentication userAuthentication = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
        if (userAuthentication == null) {
            throw new NullPointerException("null cannot be cast to non-null type com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication");
        }
        YggdrasilUserAuthentication userAuthentication2 = (YggdrasilUserAuthentication)userAuthentication;
        userAuthentication2.setUsername(username);
        userAuthentication2.setPassword(password);
        try {
            userAuthentication2.logIn();
            MinecraftInstance.mc.field_71449_j = new Session(userAuthentication2.getSelectedProfile().getName(), userAuthentication2.getSelectedProfile().getId().toString(), userAuthentication2.getAuthenticatedToken(), "mojang");
            Client.INSTANCE.getEventManager().callEvent(new SessionEvent());
            loginResult = LoginResult.LOGGED;
        }
        catch (AuthenticationUnavailableException exception) {
            loginResult = LoginResult.NO_CONTACT;
        }
        catch (AuthenticationException exception) {
            String string = exception.getMessage();
            Intrinsics.checkNotNull(string);
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
        String string = username;
        Intrinsics.checkNotNull(string);
        MinecraftInstance.mc.field_71449_j = new Session(username, UserUtils.INSTANCE.getUUID(string), "-", "legacy");
        Client.INSTANCE.getEventManager().callEvent(new SessionEvent());
    }

    @JvmStatic
    @NotNull
    public static final LoginResult loginSessionId(@NotNull String sessionId) {
        JsonObject e2;
        Object object;
        Intrinsics.checkNotNullParameter(sessionId, "sessionId");
        try {
            String[] stringArray = new String[]{"."};
            object = Base64.getDecoder().decode((String)StringsKt.split$default((CharSequence)sessionId, stringArray, false, 0, 6, null).get(1));
            Intrinsics.checkNotNullExpressionValue(object, "getDecoder().decode(sessionId.split(\".\")[1])");
            object = new String((byte[])object, Charsets.UTF_8);
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
        Intrinsics.checkNotNullExpressionValue(accessToken, "accessToken");
        if (!UserUtils.INSTANCE.isValidToken(accessToken)) {
            return LoginResult.INVALID_ACCOUNT_DATA;
        }
        Intrinsics.checkNotNullExpressionValue(uuid, "uuid");
        String string = UserUtils.INSTANCE.getUsername(uuid);
        if (string == null) {
            return LoginResult.INVALID_ACCOUNT_DATA;
        }
        String username = string;
        MinecraftInstance.mc.field_71449_j = new Session(username, uuid, accessToken, "mojang");
        Client.INSTANCE.getEventManager().callEvent(new SessionEvent());
        return LoginResult.LOGGED;
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\b\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\b\u00a8\u0006\t"}, d2={"Lnet/dev/important/utils/login/LoginUtils$LoginResult;", "", "(Ljava/lang/String;I)V", "WRONG_PASSWORD", "NO_CONTACT", "INVALID_ACCOUNT_DATA", "MIGRATED", "LOGGED", "FAILED_PARSE_TOKEN", "LiquidBounce"})
    public static final class LoginResult
    extends Enum<LoginResult> {
        public static final /* enum */ LoginResult WRONG_PASSWORD = new LoginResult();
        public static final /* enum */ LoginResult NO_CONTACT = new LoginResult();
        public static final /* enum */ LoginResult INVALID_ACCOUNT_DATA = new LoginResult();
        public static final /* enum */ LoginResult MIGRATED = new LoginResult();
        public static final /* enum */ LoginResult LOGGED = new LoginResult();
        public static final /* enum */ LoginResult FAILED_PARSE_TOKEN = new LoginResult();
        private static final /* synthetic */ LoginResult[] $VALUES;

        public static LoginResult[] values() {
            return (LoginResult[])$VALUES.clone();
        }

        public static LoginResult valueOf(String value) {
            return Enum.valueOf(LoginResult.class, value);
        }

        static {
            $VALUES = loginResultArray = new LoginResult[]{LoginResult.WRONG_PASSWORD, LoginResult.NO_CONTACT, LoginResult.INVALID_ACCOUNT_DATA, LoginResult.MIGRATED, LoginResult.LOGGED, LoginResult.FAILED_PARSE_TOKEN};
        }
    }
}

