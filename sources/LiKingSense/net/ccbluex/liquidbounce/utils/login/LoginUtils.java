/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.Agent
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.UserAuthentication
 *  com.mojang.authlib.exceptions.AuthenticationException
 *  com.mojang.authlib.exceptions.AuthenticationUnavailableException
 *  com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
 *  com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.JvmStatic
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.utils.login;

import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import java.net.Proxy;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.SessionEvent;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.login.UserUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0001\fB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001c\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006H\u0007J\u0012\u0010\b\u001a\u00020\t2\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0007J\u0010\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0006H\u0007\u00a8\u0006\r"}, d2={"Lnet/ccbluex/liquidbounce/utils/login/LoginUtils;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "()V", "login", "Lnet/ccbluex/liquidbounce/utils/login/LoginUtils$LoginResult;", "username", "", "password", "loginCracked", "", "loginSessionId", "sessionId", "LoginResult", "LiKingSense"})
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
            GameProfile gameProfile = userAuthentication2.getSelectedProfile();
            Intrinsics.checkExpressionValueIsNotNull((Object)gameProfile, (String)"userAuthentication.selectedProfile");
            String string = gameProfile.getName();
            Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"userAuthentication.selectedProfile.name");
            GameProfile gameProfile2 = userAuthentication2.getSelectedProfile();
            Intrinsics.checkExpressionValueIsNotNull((Object)gameProfile2, (String)"userAuthentication.selectedProfile");
            String string2 = gameProfile2.getId().toString();
            Intrinsics.checkExpressionValueIsNotNull((Object)string2, (String)"userAuthentication.selectedProfile.id.toString()");
            String string3 = userAuthentication2.getAuthenticatedToken();
            Intrinsics.checkExpressionValueIsNotNull((Object)string3, (String)"userAuthentication.authenticatedToken");
            MinecraftInstance.mc.setSession(MinecraftInstance.classProvider.createSession(string, string2, string3, "mojang"));
            LiquidBounce.INSTANCE.getEventManager().callEvent(new SessionEvent());
            loginResult = LoginResult.LOGGED;
        }
        catch (AuthenticationUnavailableException exception) {
            loginResult = LoginResult.NO_CONTACT;
        }
        catch (AuthenticationException exception) {
            String message = exception.getMessage();
            loginResult = StringsKt.contains((CharSequence)message, (CharSequence)"invalid username or password.", (boolean)true) ? LoginResult.INVALID_ACCOUNT_DATA : (StringsKt.contains((CharSequence)message, (CharSequence)"account migrated", (boolean)true) ? LoginResult.MIGRATED : LoginResult.NO_CONTACT);
        }
        catch (NullPointerException exception) {
            loginResult = LoginResult.WRONG_PASSWORD;
        }
        return loginResult;
    }

    @JvmStatic
    public static final void loginCracked(@Nullable String username) {
        MinecraftInstance.mc.setSession(MinecraftInstance.classProvider.createSession(username, UserUtils.INSTANCE.getUUID(username), "-", "legacy"));
        LiquidBounce.INSTANCE.getEventManager().callEvent(new SessionEvent());
    }

    /*
     * Exception decompiling
     */
    @JvmStatic
    @NotNull
    public static final LoginResult loginSessionId(@NotNull String sessionId) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl16 : INVOKESTATIC - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    private LoginUtils() {
    }

    static {
        LoginUtils loginUtils;
        INSTANCE = loginUtils = new LoginUtils();
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\b\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\b\u00a8\u0006\t"}, d2={"Lnet/ccbluex/liquidbounce/utils/login/LoginUtils$LoginResult;", "", "(Ljava/lang/String;I)V", "WRONG_PASSWORD", "NO_CONTACT", "INVALID_ACCOUNT_DATA", "MIGRATED", "LOGGED", "FAILED_PARSE_TOKEN", "LiKingSense"})
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

