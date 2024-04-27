/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  com.mojang.authlib.Agent
 *  com.mojang.authlib.UserAuthentication
 *  com.mojang.authlib.exceptions.AuthenticationException
 *  com.mojang.authlib.exceptions.AuthenticationUnavailableException
 *  com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
 *  com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication
 *  org.jetbrains.annotations.NotNull
 */
package me.liuli.elixir.account;

import com.google.gson.JsonObject;
import com.mojang.authlib.Agent;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import java.net.Proxy;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import me.liuli.elixir.account.MinecraftAccount;
import me.liuli.elixir.compat.Session;
import me.liuli.elixir.exception.LoginException;
import me.liuli.elixir.utils.GsonExtensionKt;
import org.jetbrains.annotations.NotNull;

@Deprecated(message="Mojang removed support for MojangAccount")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J\u0010\u0010\u0019\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J\b\u0010\u001a\u001a\u00020\u0016H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u001a\u0010\n\u001a\u00020\u0004X\u0096\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\u0007\"\u0004\b\f\u0010\tR\u001a\u0010\r\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u0007\"\u0004\b\u000f\u0010\tR\u0014\u0010\u0010\u001a\u00020\u00118VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013R\u000e\u0010\u0014\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2={"Lme/liuli/elixir/account/MojangAccount;", "Lme/liuli/elixir/account/MinecraftAccount;", "()V", "accessToken", "", "email", "getEmail", "()Ljava/lang/String;", "setEmail", "(Ljava/lang/String;)V", "name", "getName", "setName", "password", "getPassword", "setPassword", "session", "Lme/liuli/elixir/compat/Session;", "getSession", "()Lme/liuli/elixir/compat/Session;", "uuid", "fromRawJson", "", "json", "Lcom/google/gson/JsonObject;", "toRawJson", "update", "Elixir"})
public final class MojangAccount
extends MinecraftAccount {
    @NotNull
    private String name = "";
    @NotNull
    private String email = "";
    @NotNull
    private String password = "";
    @NotNull
    private String uuid = "";
    @NotNull
    private String accessToken = "";

    public MojangAccount() {
        super("Mojang");
    }

    @Override
    @NotNull
    public String getName() {
        return this.name;
    }

    public void setName(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "<set-?>");
        this.name = string;
    }

    @NotNull
    public final String getEmail() {
        return this.email;
    }

    public final void setEmail(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "<set-?>");
        this.email = string;
    }

    @NotNull
    public final String getPassword() {
        return this.password;
    }

    public final void setPassword(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "<set-?>");
        this.password = string;
    }

    @Override
    @NotNull
    public Session getSession() {
        if (((CharSequence)this.getName()).length() == 0 || ((CharSequence)this.uuid).length() == 0 || ((CharSequence)this.accessToken).length() == 0) {
            this.update();
        }
        return new Session(this.getName(), this.uuid, this.accessToken, "mojang");
    }

    @Override
    public void update() {
        UserAuthentication userAuthentication = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
        if (userAuthentication == null) {
            throw new NullPointerException("null cannot be cast to non-null type com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication");
        }
        YggdrasilUserAuthentication userAuthentication2 = (YggdrasilUserAuthentication)userAuthentication;
        userAuthentication2.setUsername(this.email);
        userAuthentication2.setPassword(this.password);
        try {
            userAuthentication2.logIn();
            String string = userAuthentication2.getSelectedProfile().getName();
            Intrinsics.checkNotNullExpressionValue(string, "userAuthentication.selectedProfile.name");
            this.setName(string);
            string = userAuthentication2.getSelectedProfile().getId().toString();
            Intrinsics.checkNotNullExpressionValue(string, "userAuthentication.selectedProfile.id.toString()");
            this.uuid = string;
            string = userAuthentication2.getAuthenticatedToken();
            Intrinsics.checkNotNullExpressionValue(string, "userAuthentication.authenticatedToken");
            this.accessToken = string;
        }
        catch (AuthenticationUnavailableException exception) {
            throw new LoginException("Mojang server is unavailable");
        }
        catch (AuthenticationException exception) {
            String string = exception.getMessage();
            if (string == null) {
                string = "Unknown error";
            }
            throw new LoginException(string);
        }
    }

    @Override
    public void toRawJson(@NotNull JsonObject json) {
        Intrinsics.checkNotNullParameter(json, "json");
        GsonExtensionKt.set(json, "name", this.getName());
        GsonExtensionKt.set(json, "email", this.email);
        GsonExtensionKt.set(json, "password", this.password);
    }

    @Override
    public void fromRawJson(@NotNull JsonObject json) {
        Intrinsics.checkNotNullParameter(json, "json");
        String string = GsonExtensionKt.string(json, "name");
        Intrinsics.checkNotNull(string);
        this.setName(string);
        String string2 = GsonExtensionKt.string(json, "email");
        Intrinsics.checkNotNull(string2);
        this.email = string2;
        String string3 = GsonExtensionKt.string(json, "password");
        Intrinsics.checkNotNull(string3);
        this.password = string3;
    }
}

