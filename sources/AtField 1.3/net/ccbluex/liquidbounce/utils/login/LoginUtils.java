/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
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
    public static final LoginResult login(@Nullable String string, @Nullable String string2) {
        LoginResult loginResult;
        UserAuthentication userAuthentication = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
        if (userAuthentication == null) {
            throw new TypeCastException("null cannot be cast to non-null type com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication");
        }
        YggdrasilUserAuthentication yggdrasilUserAuthentication = (YggdrasilUserAuthentication)userAuthentication;
        yggdrasilUserAuthentication.setUsername(string);
        yggdrasilUserAuthentication.setPassword(string2);
        try {
            yggdrasilUserAuthentication.logIn();
            MinecraftInstance.mc.setSession(MinecraftInstance.classProvider.createSession(yggdrasilUserAuthentication.getSelectedProfile().getName(), yggdrasilUserAuthentication.getSelectedProfile().getId().toString(), yggdrasilUserAuthentication.getAuthenticatedToken(), "mojang"));
            LiquidBounce.INSTANCE.getEventManager().callEvent(new SessionEvent());
            loginResult = LoginResult.LOGGED;
        }
        catch (AuthenticationUnavailableException authenticationUnavailableException) {
            loginResult = LoginResult.NO_CONTACT;
        }
        catch (AuthenticationException authenticationException) {
            String string3 = authenticationException.getMessage();
            if (string3 == null) {
                Intrinsics.throwNpe();
            }
            String string4 = string3;
            loginResult = StringsKt.contains((CharSequence)string4, (CharSequence)"invalid username or password.", (boolean)true) ? LoginResult.INVALID_ACCOUNT_DATA : (StringsKt.contains((CharSequence)string4, (CharSequence)"account migrated", (boolean)true) ? LoginResult.MIGRATED : LoginResult.NO_CONTACT);
        }
        catch (NullPointerException nullPointerException) {
            loginResult = LoginResult.WRONG_PASSWORD;
        }
        return loginResult;
    }

    @JvmStatic
    public static final LoginResult loginSessionId(String string) {
        Object object;
        Object object2;
        try {
            object2 = Base64.getDecoder().decode((String)StringsKt.split$default((CharSequence)string, (String[])new String[]{"."}, (boolean)false, (int)0, (int)6, null).get(1));
            object = Charsets.UTF_8;
            boolean bl = false;
            object2 = new String((byte[])object2, (Charset)object);
        }
        catch (Exception exception) {
            return LoginResult.FAILED_PARSE_TOKEN;
        }
        Object object3 = object2;
        try {
            object = new JsonParser().parse((String)object3).getAsJsonObject();
        }
        catch (Exception exception) {
            return LoginResult.FAILED_PARSE_TOKEN;
        }
        object2 = object;
        object = object2.get("spr").getAsString();
        String string2 = object2.get("yggt").getAsString();
        if (!UserUtils.INSTANCE.isValidToken(string2)) {
            return LoginResult.INVALID_ACCOUNT_DATA;
        }
        String string3 = UserUtils.INSTANCE.getUsername((String)object);
        if (string3 == null) {
            return LoginResult.INVALID_ACCOUNT_DATA;
        }
        String string4 = string3;
        MinecraftInstance.mc.setSession(MinecraftInstance.classProvider.createSession(string4, (String)object, string2, "mojang"));
        LiquidBounce.INSTANCE.getEventManager().callEvent(new SessionEvent());
        return LoginResult.LOGGED;
    }

    @JvmStatic
    public static final void loginCracked(@Nullable String string) {
        String string2 = string;
        if (string2 == null) {
            Intrinsics.throwNpe();
        }
        MinecraftInstance.mc.setSession(MinecraftInstance.classProvider.createSession(string2, UserUtils.INSTANCE.getUUID(string), "-", "legacy"));
        LiquidBounce.INSTANCE.getEventManager().callEvent(new SessionEvent());
    }

    private LoginUtils() {
    }

    static {
        LoginUtils loginUtils;
        INSTANCE = loginUtils = new LoginUtils();
    }

    public static final class LoginResult
    extends Enum {
        public static final /* enum */ LoginResult LOGGED;
        public static final /* enum */ LoginResult NO_CONTACT;
        public static final /* enum */ LoginResult FAILED_PARSE_TOKEN;
        public static final /* enum */ LoginResult MIGRATED;
        public static final /* enum */ LoginResult INVALID_ACCOUNT_DATA;
        public static final /* enum */ LoginResult WRONG_PASSWORD;
        private static final LoginResult[] $VALUES;

        static {
            LoginResult[] loginResultArray = new LoginResult[6];
            LoginResult[] loginResultArray2 = loginResultArray;
            loginResultArray[0] = WRONG_PASSWORD = new LoginResult("WRONG_PASSWORD", 0);
            loginResultArray[1] = NO_CONTACT = new LoginResult("NO_CONTACT", 1);
            loginResultArray[2] = INVALID_ACCOUNT_DATA = new LoginResult("INVALID_ACCOUNT_DATA", 2);
            loginResultArray[3] = MIGRATED = new LoginResult("MIGRATED", 3);
            loginResultArray[4] = LOGGED = new LoginResult("LOGGED", 4);
            loginResultArray[5] = FAILED_PARSE_TOKEN = new LoginResult("FAILED_PARSE_TOKEN", 5);
            $VALUES = loginResultArray;
        }

        public static LoginResult[] values() {
            return (LoginResult[])$VALUES.clone();
        }

        /*
         * WARNING - Possible parameter corruption
         * WARNING - void declaration
         */
        private LoginResult() {
            void var2_-1;
            void var1_-1;
        }

        public static LoginResult valueOf(String string) {
            return Enum.valueOf(LoginResult.class, string);
        }
    }
}

